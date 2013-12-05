package org.epis.ws.agent.scheduler.service;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 
 * epis-ws-consumer.jar를 실행시킨다.
 * 
 * e.g.)java -Dconsumer.root.dir=C:\Users\developer\git\sites\epis-ws\epis-ws-consumer -Djob.id=JOB-2 -jar epis-ws-consumer.jar Biz
 * @author developer
 *
 */
@Service
public class RuntimeExecutor {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("#{systemProperties['consumer.root.dir']}")
	private String rootDir;
	
	@Autowired
	private Properties jobProp;
	
	public void execute(String cmd){
		Process process = null;
		
		Runtime rt = Runtime.getRuntime();
		try {
			process = rt.exec(cmd);
			
		} catch (IOException e) {
			logger.error("Exception Occurred on Runtime.exec()");
		} finally {
			if(process!=null){
				process.destroy();	
			}
		}
	}
}
