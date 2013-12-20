package org.epis.ws.agent.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.epis.ws.agent.util.PropertyEnum;
import org.epis.ws.agent.util.SortedProperties;
import org.epis.ws.common.entity.AgentVO;
import org.epis.ws.common.entity.BizVO;
import org.epis.ws.common.entity.ConfigurationVO;
import org.epis.ws.common.entity.JobVO;
import org.epis.ws.common.service.EPISWSGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * <pre>
 * <p>Agent처리의 실질적인 부분을 수행하는 서비스 클래스</p>
 * 
 * 디버깅, Configuration처리, 업무처리를 수행하는 각각의 메서드들을 제공한다.
 * </pre>
 * @author developer
 *
 */
@Service
public class EPISConsumerService implements ApplicationContextAware {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private ApplicationContext ctx;
	
	@Autowired
	@Qualifier("jobProp")
	private Properties jobProp;
	
	@Autowired
	@Qualifier("agentProp")
	private Properties agentProp;
	
	@Autowired
	private AgentBizService agentService;
	
	/**
	 * <pre>
	 * <p>테스트용 메서드</p>
	 * 
	 * 웹서비스 데이터 송수신을 테스팅하기 위한 메서드.
	 * main sql을 실행한 결과를 웹서비스로 전송한다.
	 * </pre>
	 * @param gateway	웹서비스 인터페이스
	 * @throws Exception
	 */
	public void executeDebug(EPISWSGateway gateway) throws Exception{
//		String str = gateway.processPrimitiveData("", "");
//		logger.debug("RESULT : {}", str);
		
//		ConfigurationVO configVO
//			= gateway.findConfigurationData(agentProp.getProperty(PropertyEnum.AGENT_ID.getKey()));
//		logger.debug("ConfigurationVO retrieved : {}",configVO);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("Field","Value");
		String jsonData = agentService.executeMainSQLAsJSON();
		
		String response = gateway.debugMethod(jsonData);
		logger.debug("response of Debug : {}",response);
	}
	
	/**
	 * <pre>
	 * <p>Configuration처리용 메서드</p>
	 * 
	 * 웹서비스의 요청결과로 받은 Configuration 정보에서
	 * AgentVO에 담긴 내용을 agent.properties에,
	 * JobVO 리스트의 내용을 job.properties에 반영한 후,
	 * 스케쥴러에 반영한다.
	 * 
	 * (epis-ws-agent-scheduler를 이용할 경우,
	 *  정지 후, 수정된 properties내용으로 재실행한다.)
	 * </pre>
	 * @param gateway
	 * @throws Exception
	 */
	public void executeConfig(EPISWSGateway gateway) throws Exception {
		
		final String agentId = agentProp.getProperty(PropertyEnum.AGENT_ID.getKey());
		
		logger.info("===== Start Synchronizing Configuration Execution : [{}] =====", agentId);
		ConfigurationVO config = gateway.findConfigurationData(agentId);
		logger.info("=== WebService Process For Synchronizing Configuration [{}] END ===", agentId);
		
		AgentVO agentInfo = config.getAgentInfo();
		saveAgentProperties(agentInfo);
		logger.info("=== Property File 'agent.properties' is Stored Successfully ===");
		
		saveJobProperties(config.getJobList());
		logger.info("=== Property File 'job.properties' is Stored Successfully ===");
		
		if("OS".equals(agentInfo.getScheduleType())){
			String[] jobIds = StringUtils.splitPreserveAllTokens(jobProp.getProperty("job.ids"));
			final AbstractScheduleRegister register = ctx.getBean(
					agentProp.getProperty("consumer.operatingSystem") + "ScheduleRegister"
					,AbstractScheduleRegister.class);
			register.registerSchedule(jobIds);
			logger.info("=== Applied to OS Scheduler Successfully ===");
		}else if("Local".equals(agentInfo.getScheduleType())){
			//RPC 기술(e.g. RMI)을 이용하여 EPISLocalScheduler.stop()을 실행함.
			//(프로세스가 다르기 때문)
			logger.info("=== Restarted Local Scheduler Scheduler Successfully ===");
		}
		
		logger.info("===== End Synchronizing Configuration Execution =====");
	}
	
	/**
	 * <pre>
	 * <p>job 프로퍼티에 명기된 업무처리를 수행한다.</p>
	 * 
	 * 작업절차는 다음과 같다.
	 * 	1. 전처리SQL을 실행한다. (선택사항)
	 * 	2. 메인 SQL을 실행한다. (필수사항)
	 * 	3. 메인 SQL로 취득된 데이터를 WebService로 전송한다.
	 * 	4. 후처리 SQL을 실행한다. (선택사항)
	 * 
	 * 전처리 및 후처리 SQL이 설정되어 있지 않을 경우, 해당 실행 메서드는 -1을 리턴한다.
	 * 전처리 및 후처리 SQL은 설정시 반드시 Insert/Update/Delete 중 하나이어야 한다.
	 * 메인 SQL은 반드시 Select이어야 하며, 후처리 SQL이 설정되어 있을 경우,
	 * 메인 SQL은 페이징이 가능하도록 SQL의 앞 뒤로 페이징 가능한 구문이 추가된다.
	 * 
	 * 만일을 위해 무한루핑을 막기 위하여 루프에서 처리된 건이 100,000건을 초과할 경우,
	 * 루프가 종료됨
	 * </pre>
	 * @param gateway
	 * @return
	 * @throws JsonProcessingException 
	 */
	public boolean executeBiz(EPISWSGateway gateway) throws JsonProcessingException {

		String jobId = System.getProperty(PropertyEnum.SYS_JOB_NAME.getKey());
		logger.info("===== Start JOB Execution : [{}] =====", jobId);
		
		int count = 0;
		
		//====================================================
		//PRE SQL
		//====================================================
		count = agentService.executePreSQL();
		if(count!=-1){;
			logger.info("=== {}'s Pre SQL Process END : [{}] ===",new Object[]{jobId, count});
		}

		
		String eflag = "Y";
		// Avoid Looping Infinitly (For Debug)
		int maximum = 100000, loopCount = 0;
		
		BizVO bizParam = new BizVO();
		bizParam.setAgentId(agentProp.getProperty(PropertyEnum.AGENT_ID.getKey()));
		bizParam.setJobId(jobId);
		boolean occurError = false;
		//====================================================
		//Select & Update Something
		//====================================================
		
		//	Select
		List<Map<String,Object>> dataList = agentService.executeMainSQL();
		do{
			count = (CollectionUtils.isNotEmpty(dataList)) ? dataList.size() : 0;
			logger.info("=== {}'s Main SQL Process END : [{}] ===",new Object[]{jobId, count});
			String jsonData = agentService.convertListToJSON(dataList);
			bizParam.setJsonData(jsonData);
			
			occurError = false;
			//	Execute WebService
			eflag = "F";
			try {
				String str = gateway.processPrimitiveData(bizParam);
				eflag = "Y";
				logger.info("=== {}'s WebService Process END : [{}] ===",new Object[]{jobId, str});
			} catch (Exception e) {
				occurError = true;
				logger.error("##### ERROR occurred during executing WebService : processPrimitiveData #####",e);
			}
			
			//	Update Something
			logger.debug("eflag : [{}]",eflag);
			count = agentService.executePostSQL(dataList, eflag);
			if(count==-1){
				logger.info("=== No SQL For the Post Processing ===");
				break;
			}
			logger.info("=== {}'s Post SQL Process END : [{}] ===",new Object[]{jobId, count});
			
			//	Avoid Infinite Loop
			loopCount += dataList.size();
			if (loopCount > maximum){
				logger.warn("##### It must be in the infinite looping!!! #####");
				break;
			}
			logger.info("=== Loop : [{}] =======================================",loopCount);
			
			//	Select
			dataList = agentService.executeMainSQL();
		} while(CollectionUtils.isNotEmpty(dataList));
		
		logger.info("===== End JOB Execution : [{}] =====", jobId);
		return occurError;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.ctx = applicationContext;
	}
	
	/**
	 * <pre>
	 * <p>인자로 받은 jobList를 해당 Properties 인스턴스에 반영하여 저장함.</p>
	 * 
	 * job목록에서 [job id]. 으로 시작하는 프로퍼티의 값을
	 * 인자로 받은 job 목록의 것으로 변경하여, 최종적으로는
	 * job.properties파일에 저장함.
	 * </pre>
	 * @param jobList
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws IOException
	 */
	private void saveJobProperties(List<JobVO> jobList) throws IllegalAccessException
			,InvocationTargetException, NoSuchMethodException, IOException{
		for(JobVO job : jobList){
			String prefix = job.getJobId() + ".";
			for(Object key : jobProp.keySet()){
				if(!StringUtils.startsWith(String.valueOf(key), prefix)){
					continue;
				}
				
				jobProp.put(key, BeanUtils.getSimpleProperty(
						job, String.valueOf(key).substring(prefix.length())));
			}
		}
		
		storeProperties(jobProp, "job", "EPIS JOB Properties");
	}
	
	/**
	 * <pre>
	 * <p>인자로 받은 agent 정보를 해당 Properties 인스턴스에 반영하여 저장함.</p>
	 * 
	 * job목록에서 consumer. 으로 시작하는 프로퍼티의 값을
	 * 인자로 받은 agent 정보의 것으로 변경하여, 최종적으로는
	 * agent.properties파일에 저장함.
	 * </pre>
	 * @param agent
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws IOException
	 */
	private void saveAgentProperties(AgentVO agent) throws IllegalAccessException
			,InvocationTargetException, NoSuchMethodException, IOException{
		final String prefix = "consumer.";
		for(Object key : agentProp.keySet()){
			if(!StringUtils.startsWith(String.valueOf(key), prefix)){
				continue;
			}
			
			agentProp.put(key, BeanUtils.getSimpleProperty(
					agent, String.valueOf(key).substring(prefix.length())));
		}
		
		storeProperties(agentProp, "agent", "EPIS AGENT Properties");
	}
	
	/**
	 * <pre>
	 * <p>프로퍼티 인스턴스를 해당파일에 반영한다.</p>
	 * 
	 * 인자로 받은 properties 인스턴스를, 인자로 받은 파일명의 파일에 저장한다.
	 * </pre>
	 * @param prop
	 * @param fileName
	 * @param title
	 * @throws IOException
	 */
	private void storeProperties(Properties prop, String fileName, String title) throws IOException {
		
		StringBuilder path = new StringBuilder(
				System.getProperty(PropertyEnum.SYS_ROOT_DIR.getKey()));
		
		//디렉토리 구분자 체킹
		if((path.charAt(path.length()-1)!=IOUtils.DIR_SEPARATOR)){
			path.append(IOUtils.DIR_SEPARATOR);
		}
		//Full 경로 작성
		path.append("properties").append(IOUtils.DIR_SEPARATOR)
				.append(fileName).append(".properties");
		
		OutputStream output = null;
		try {
			output = new FileOutputStream(path.toString());
			Properties sortedProp = new SortedProperties(agentProp);
			sortedProp.store(output, title);
			logger.info("{}.properties IS SAVED"
					,fileName);
		} finally {
			IOUtils.closeQuietly(output);
		}
	}
	
	
/*
	public void executeConfig(EPISWSGateway gateway) throws Exception{
		
		logger.info("===== Configuration Synchronize START =====");
		final String prefixConf = PropertyEnum.CONFIG.getKey();
		final String prefixCmd = PropertyEnum.SCHEDULE_CMD.getKey();
		final String agentRootDir = System.getProperty(PropertyEnum.SYS_ROOT_DIR.getKey());
		
		//get configuration info with Web Service
		String clientId = agentProp.getProperty(prefixConf+"clientId");
		ClientVO conf = gateway.findConfigurationData(clientId);
		if(conf==null){
			logger.warn("No Configuration Data found!!");
			return;
		}
		boolean refresh = false, refreshSchedule = false;
		String field = null,value = null;
		
		
		String beanName = conf.getOperatingSystem() + "ScheduleRegister";
		AbstractScheduleRegister scheduleRegister
			= ctx.getBean(beanName, AbstractScheduleRegister.class);
		
		//set to properties
		Map<String,String> scheduleCmdMap = conf.getScheduleMap();
		for(Object key : agentProp.keySet()){
			
			//set value changed in Configuration Part
			if(StringUtils.startsWith(key.toString(), prefixConf)){
				field = StringUtils.substring(key.toString(), prefixConf.length());
				value = StringUtils.defaultString(BeanUtils.getProperty(conf, field));
				if(!agentProp.get(key).equals(value)){
					agentProp.put(key, value);
					refresh = true;
				}
			}
			
			//set value changed in Schedule Part
			else if(StringUtils.startsWith(key.toString(), prefixCmd)){
				field = StringUtils.substring(key.toString(), prefixCmd.length());
				value = scheduleRegister.getScheduleCmd(field, scheduleCmdMap.get(field));
				if(!agentProp.get(key).equals(value)){
					refreshSchedule = true;
					agentProp.put(key, value);
					scheduleCmdMap.put(field,value);
				}
				else{
					scheduleCmdMap.remove(field);
				}
			}
		}
		
		//save to properties file
		if(refresh || refreshSchedule){
			String path = agentRootDir + "/properties/agent.properties";
			Properties sortedProp = new SortedProperties(agentProp);
			sortedProp.store(new FileOutputStream(path), "EPIS AGENT Properties");
			logger.info("{}{}agent.properties IS SAVED"
					,new Object[]{agentRootDir,File.separator});
		}
		
		//register schedule
		if(refreshSchedule){
			scheduleRegister.registerSchedule(scheduleCmdMap);
		}
		logger.info("===== Configuration Synchronize END =====");
	}
*/
}