package org.epis.ws.agent.scheduler.service;

import java.text.ParseException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

@Service
public class Scheduler {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private AgentExecutor executor;
	
	@Autowired
	@Qualifier("jobProp")
	private Properties jobProp;
	
	public void register() throws Exception{
		String[] jobIds = StringUtils.split(jobProp.getProperty("job.ids"),",");
		Trigger[] triggers = new Trigger[jobIds.length];
		int idx = 0;
		for(String jobId : jobIds){
			MethodInvokingJobDetailFactoryBean jobDetailFactoryBean
				= new MethodInvokingJobDetailFactoryBean();
			jobDetailFactoryBean.setTargetObject(executor);
			jobDetailFactoryBean.setTargetMethod("executeCmd");
			jobDetailFactoryBean.setArguments(new Object[]{jobId});
			jobDetailFactoryBean.setName("EPISJob_" + jobId);
			jobDetailFactoryBean.afterPropertiesSet();
			logger.debug("Create JobDetail");
			
			String cronExp = convertTimeToCronExp(jobProp.getProperty(jobId + ".execTime"));
			CronTriggerFactoryBean triggerFactoryBean
				= new CronTriggerFactoryBean();
			triggerFactoryBean.setName("EPISTrigger_" + jobId);
			triggerFactoryBean.setJobDetail(jobDetailFactoryBean.getObject());
			triggerFactoryBean.setCronExpression(cronExp);
			triggerFactoryBean.afterPropertiesSet();
			logger.debug("Create Trigger");
			
			triggers[idx++] = triggerFactoryBean.getObject();
		}
		
		
		SchedulerFactoryBean scheduleFactoryBean = new SchedulerFactoryBean();
		scheduleFactoryBean.setTriggers(triggers);
		scheduleFactoryBean.afterPropertiesSet();
		
		scheduleFactoryBean.start();
	}
	
	private String convertTimeToCronExp(String time){
		String[] timeElem = StringUtils.split(time,":");
		if(timeElem.length!=2){
			throw new RuntimeException(new ParseException(time,0));
		}
		String template = "0 %1$s %2$s * * ?";//0 15 10 * * ?
		String result = String.format(template,timeElem[1],timeElem[0]);
		return result;
	}

	
}
