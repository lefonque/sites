package org.epis.ws.consumer.service.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.epis.ws.common.entity.ConfigurationVO;
import org.epis.ws.common.service.EPISWSGateway;
import org.epis.ws.consumer.dao.AgentBizDAO;
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
	
	public void executeDebug(EPISWSGateway gateway) throws Exception{
//		String str = gateway.processPrimitiveData("", "");
//		logger.debug("RESULT : {}", str);
		
		ConfigurationVO configVO = gateway.findConfigurationData(agentProp.getProperty("consumer.clientId"));
		logger.debug("ConfigurationVO retrieved : {}",configVO);
	}
	
	public void executeConfig(EPISWSGateway gateway) throws Exception {
		final AbstractScheduleRegister register = ctx.getBean(
				agentProp.getProperty("consumer.operatingSystem") + "ScheduleRegister"
				,AbstractScheduleRegister.class);
		String[] jobNames = StringUtils.splitPreserveAllTokens(jobProp.getProperty("job.ids"), ",");
		
		register.registerSchedule(jobNames);
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
	public void executeBiz(EPISWSGateway gateway) {

		String jobId = System.getProperty(PropertyEnum.SYS_JOB_NAME.getKey());
		
		HashMap<String,String> param = new HashMap<String,String>();
		param.put("FIELD1","ABC");
		param.put("FIELD2","DEF");
		
		
		try {
			//====================================================
			//Insert Something
			//====================================================
			String sql = jobProp.getProperty(jobId + PropertyEnum.JOB_SQL_PRE.getKey());
			//Convert ? to NamedParameter
			if(sql.substring(StringUtils.indexOfIgnoreCase(sql, "values")).contains("?")){
				sql = sqlUtil.convertInsertSQL(sql);
			}
			int count = agentDao.modify(sql, StringUtils.EMPTY);
			logger.info("=== {}'s Pre Process END : [{}] ===",new Object[]{jobId, count});
			
			//====================================================
			//Select & Update Something
			//====================================================
			sql = jobProp.getProperty(jobId + PropertyEnum.JOB_SQL_MAIN.getKey());
			// 	Convert paginational SQL
			sql = sqlUtil.convertPagableSelectSQL(sql);
			//	Select Something
			List<HashMap<String,Object>> dataList = agentDao.selectList(sql);
			
			// Avoid Looping Infinitly
			int maximum = 100000, loopCount = Integer.parseInt(jobProp.getProperty(jobId + ".max.selectcount"));
			while(CollectionUtils.isNotEmpty(dataList) && (loopCount < maximum)){
				
				String str = gateway.processPrimitiveData(dataList);
				logger.info("=== RESPONSED FROM PROVIDER : [{}] ===", str);
				
				//	Update Something
				for(Map<String,Object> one : dataList){
					sql = jobProp.getProperty(jobId + PropertyEnum.JOB_SQL_POST.getKey());
					if(sql.contains("?")){
						sql = sqlUtil.convertNamedParameterUpdateSQL(sql);
					}
					count = agentDao.modify(sql, one);
				}
				dataList = agentDao.selectList(sql);
				loopCount += loopCount;
			}
		} catch (Exception e) {
			logger.error("EXCEPTION OCCURED IN WEBSERVICE",e);
		}
		
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.ctx = applicationContext;
	}

}
