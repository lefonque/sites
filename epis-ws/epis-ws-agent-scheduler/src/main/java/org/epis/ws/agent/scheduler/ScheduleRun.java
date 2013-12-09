package org.epis.ws.agent.scheduler;

import org.epis.ws.agent.scheduler.service.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ScheduleRun {
	
	private static final Logger logger = LoggerFactory.getLogger(ScheduleRun.class);

	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:/META-INF/spring/context-scheduler.xml");
		Scheduler scheduler = ctx.getBean(Scheduler.class);
		try {
			scheduler.register();
		} catch (Exception e) {
			logger.error("Exception Occurred",e);
		}
	}

}
