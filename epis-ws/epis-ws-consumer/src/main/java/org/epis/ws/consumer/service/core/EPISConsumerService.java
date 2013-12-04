package org.epis.ws.consumer.service.core;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.epis.ws.common.entity.BizVO;
import org.epis.ws.common.entity.RecordMap;
import org.epis.ws.common.entity.RecordMapEntry;
import org.epis.ws.common.service.EPISWSGateway;
import org.epis.ws.consumer.dao.AgentBizDAO;
import org.epis.ws.consumer.service.AgentBizService;
import org.epis.ws.consumer.util.PropertyEnum;
import org.epis.ws.consumer.util.SqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

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
	private SqlUtil sqlUtil;
	
	@Autowired
	private AgentBizDAO agentDao;
	
	@Autowired
	private AgentBizService agentService;
	
	public void executeDebug(EPISWSGateway gateway) throws Exception{
//		String str = gateway.processPrimitiveData("", "");
//		logger.debug("RESULT : {}", str);
		
//		ConfigurationVO configVO
//			= gateway.findConfigurationData(agentProp.getProperty(PropertyEnum.AGENT_ID.getKey()));
//		logger.debug("ConfigurationVO retrieved : {}",configVO);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("Field","Value");
		List<RecordMap> recordList = agentService.executeMainSQLAsRecordMap();
		
		String response = gateway.debugMethod(recordList);
		logger.debug("response of Debug : {}",response);
	}
	
	public void executeConfig(EPISWSGateway gateway) throws Exception {
		final AbstractScheduleRegister register = ctx.getBean(
				agentProp.getProperty("consumer.operatingSystem") + "ScheduleRegister"
				,AbstractScheduleRegister.class);
		String[] jobNames = StringUtils.splitPreserveAllTokens(jobProp.getProperty("job.ids"), ",");
		
		register.registerSchedule(jobNames);
	}
	
	public boolean executeBiz(EPISWSGateway gateway) {

		String jobId = System.getProperty(PropertyEnum.SYS_JOB_NAME.getKey());
		logger.info("===== Start JOB Execution : [{}] =====", jobId);
		
		int count = 0;
		String preSQL = jobProp.getProperty(jobId + PropertyEnum.JOB_SQL_PRE.getKey());
		//====================================================
		//PRE SQL
		//====================================================
		if(StringUtils.isNotEmpty(preSQL)){
			count = agentDao.modify(preSQL);
			logger.info("=== {}'s Pre SQL Process END : [{}] ===",new Object[]{jobId, count});
		}

		//	POST SQL : Update Flag & Timestamp
		String postSQL = jobProp.getProperty(jobId + PropertyEnum.JOB_SQL_POST.getKey());
		
		//	MAIN SQL : Select Something
		String mainSQL = jobProp.getProperty(jobId + PropertyEnum.JOB_SQL_MAIN.getKey());
		if(StringUtils.isNotEmpty(postSQL)){
			mainSQL = sqlUtil.convertPagableSelectSQL(mainSQL);	// Convert paginational SQL
		}
		

		String eflag = "S";Timestamp edate = null;
		// Avoid Looping Infinitly (For Debug)
		int maximum = 100000, loopCount = 0;
		int batchLimit = Integer.parseInt(
				jobProp.getProperty(jobId + PropertyEnum.JOB_BATCH_SIZE.getKey()));
		
		BizVO bizParam = new BizVO();
		bizParam.setAgentId(agentProp.getProperty(PropertyEnum.AGENT_ID.getKey()));
		bizParam.setJobId(jobId);
		boolean occurError = false, existIntfRecord = false;
		//====================================================
		//Select & Update Something
		//====================================================
		
		//	Select
		List<RecordMap> dataList = agentDao.selectListAsRecordMap(mainSQL);
		do{
			existIntfRecord = CollectionUtils.isNotEmpty(dataList);
			count = existIntfRecord ? dataList.size() : 0;
			logger.info("=== {}'s Main SQL Process END : [{}] ===",new Object[]{jobId, count});
			bizParam.setDataList(dataList);
			
			//	Execute WebService
			eflag = "F";
			try {
				String str = gateway.processPrimitiveData(bizParam);
				eflag = "S";
				logger.info("=== {}'s WebService Process END : [{}] ===",new Object[]{jobId, str});
			} catch (Exception e) {
				occurError = true;
				logger.error("##### ERROR occurred during executing WebService : processPrimitiveData #####");
			}
			
			//	Update Something
			if(StringUtils.isEmpty(postSQL)){
				logger.info("===== No SQL For the Post Processing =====");
				break;
			}
			edate = new Timestamp(System.currentTimeMillis());
			count = 0;
			for(RecordMap one : dataList){
				one.entry.add(new RecordMapEntry("EFLAG",eflag));
				one.entry.add(new RecordMapEntry("EDATE",edate));
				count += agentDao.modify(postSQL, one);
			}
			logger.info("=== {}'s Post SQL Process END : [{}] ===",new Object[]{jobId, count});
			
			//	Avoid Infinite Loop
			loopCount += batchLimit;
			if (loopCount > maximum){
				logger.warn("##### It must be in the infinite looping!!! #####");
				break;
			}
			
			//	Select
			dataList = agentDao.selectListAsRecordMap(mainSQL);
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