package org.epis.ws.agent.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.epis.ws.agent.util.PropertyEnum;
import org.epis.ws.common.entity.BizVO;
import org.epis.ws.common.entity.MapWrapper;
import org.epis.ws.common.service.EPISWSGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * <pre>
 * <p>Agent처리의 실질적인 부분을 수행하는 서비스 클래스</p>
 * 
 * 
 * </pre>
 * @author developer
 *
 */
@Service
public class EPISConsumerService implements ApplicationContextAware {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private ApplicationContext ctx;
	
	private String[] jobIds;
	
	@Value("#{jobProp['job.ids']}")
	public void setJobIds(String jobIdsString){
		jobIds = StringUtils.splitPreserveAllTokens(jobIdsString, ",");
	}
	
	public String[] getJobIds(){
		return jobIds;
	}
	
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
		List<MapWrapper> recordList = agentService.executeMainSQL();
		
		String response = gateway.debugMethod(recordList);
		logger.debug("response of Debug : {}",response);
	}
	
	public void executeConfig(EPISWSGateway gateway) throws Exception {
		
		final AbstractScheduleRegister register = ctx.getBean(
				agentProp.getProperty("consumer.operatingSystem") + "ScheduleRegister"
				,AbstractScheduleRegister.class);
		
		register.registerSchedule(jobIds);
	}
	
	public boolean executeBiz(EPISWSGateway gateway) {

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
		boolean occurError = false, existIntfRecord = false;
		//====================================================
		//Select & Update Something
		//====================================================
		
		//	Select
		List<MapWrapper> dataList = agentService.executeMainSQL();
		do{
			existIntfRecord = CollectionUtils.isNotEmpty(dataList);
			count = existIntfRecord ? dataList.size() : 0;
			logger.info("=== {}'s Main SQL Process END : [{}] ===",new Object[]{jobId, count});
			bizParam.setDataList(dataList);
			
			//	Execute WebService
			eflag = "N";
			try {
				String str = gateway.processPrimitiveData(bizParam);
				eflag = "Y";
				logger.info("=== {}'s WebService Process END : [{}] ===",new Object[]{jobId, str});
			} catch (Exception e) {
				occurError = true;
				logger.error("##### ERROR occurred during executing WebService : processPrimitiveData #####");
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
			logger.debug("=== Loop : [{}] =======================================",loopCount);
			
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