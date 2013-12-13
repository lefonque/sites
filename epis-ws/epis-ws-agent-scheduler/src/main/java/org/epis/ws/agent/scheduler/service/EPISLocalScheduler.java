package org.epis.ws.agent.scheduler.service;

import java.text.ParseException;
import java.util.Properties;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.epis.ws.agent.scheduler.utils.PropertyEnum;
import org.quartz.SchedulerException;
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
public class EPISLocalScheduler {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private AgentExecutor executor;
	
	@Autowired
	@Qualifier("jobProp")
	private Properties jobProp;
	
	private SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
	
	@PreDestroy
	public void destroy(){
		try {
			schedulerFactoryBean.destroy();
		} catch (SchedulerException e) {
			logger.error("Exception Occurred on Destroying",e);
		}
	}
	/**
	 * <pre>
	 * <p>Schedule Job을 등록한다.</p>
	 * 
	 * job.properties 파일을 읽어 기재된 job들 각각에
	 * 해당하는 Trigger를 작성하여 ScheduleFactoryBean에 등록한다.
	 * 
	 * ScheduleFactory에 등록되면 job.properties의 
	 * [job id].execTime의 시각에 맞추어 매일마다 실행이 된다.
	 * </pre>
	 * @throws Exception
	 */
	public void register() throws Exception{
		
		logger.info("===== Schedule Job Registration START =====");
		
		String[] jobIds = StringUtils.split(jobProp.getProperty("job.ids"),",");
		Trigger[] triggers = new Trigger[jobIds.length];
		int idx = 0;
		for(String jobId : jobIds){
			MethodInvokingJobDetailFactoryBean jobDetailFactoryBean
				= new MethodInvokingJobDetailFactoryBean();
			jobDetailFactoryBean.setTargetObject(executor);
			jobDetailFactoryBean.setTargetMethod("execute");
			jobDetailFactoryBean.setArguments(new Object[]{jobId});
			jobDetailFactoryBean.setName("EPISJob_" + jobId);
			jobDetailFactoryBean.afterPropertiesSet();
			logger.info("=== Create JobDetail : [{}] ===",jobId);
			
			String cronExp = convertTimeToCronExp(jobProp.getProperty(jobId + PropertyEnum.JOB_SUFFIX_EXEC_TIME.getKey()));
			CronTriggerFactoryBean triggerFactoryBean
				= new CronTriggerFactoryBean();
			triggerFactoryBean.setName("EPISTrigger_" + jobId);
			triggerFactoryBean.setJobDetail(jobDetailFactoryBean.getObject());
			triggerFactoryBean.setCronExpression(cronExp);
			triggerFactoryBean.afterPropertiesSet();
			logger.info("=== Create Trigger : [{}] ===",jobId);
			
			triggers[idx++] = triggerFactoryBean.getObject();
		}
		
		schedulerFactoryBean.setTriggers(triggers);
		schedulerFactoryBean.afterPropertiesSet();
		logger.info("=== Registered Triggers ===");
		
		schedulerFactoryBean.start();
		logger.info("===== Schedule Job Registration END =====");
	}
	
	//RMI를 사용하게 될 경우, server도 이 메서드에서 shutdown하도록 함.
	public void stopScheduler(){
		schedulerFactoryBean.stop();
	}
	
	/**
	 * <p>
	 * 시각(e.g. 11:00)을 Quartz 프레임워크의 cron표현식으로 변환한다.
	 * </p>
	 * @param time
	 * @return
	 */
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
