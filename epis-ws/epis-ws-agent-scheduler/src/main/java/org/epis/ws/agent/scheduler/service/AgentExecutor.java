package org.epis.ws.agent.scheduler.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringUtils;
import org.epis.ws.common.utils.ConstEnum;
import org.epis.ws.common.utils.RuntimeExecutor;
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
public class AgentExecutor {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("#{systemProperties['consumer.root.dir']}")
	private String rootDir;
	
	@Autowired
	private RuntimeExecutor executor;
	
	public void execute(String jobId){
		Map<ConstEnum, List<String>> resultMessage = new HashMap<ConstEnum,List<String>>();
		String cmd = getCmd(jobId);
		try {
//			//for debug
//			cmd = "java -Dconsumer.root.dir=C:\\Sites\\Garage\\consumer -Djob.id=JOB-2 -cp .;C:\\Sites\\Garage\\consumer -jar C:\\Sites\\Garage\\consumer\\epis-ws-consumer.jar Biz";
//			cmd = "java -version";
			resultMessage = executor.executeCMD(cmd);
			
		} catch (IOException e) {
			logger.error("Exception Occurred on Runtime.exec()",e);
		} catch (InterruptedException e) {
			logger.error("Exception Occurred on Runtime.exec()",e);
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		String errorMessage = executor.getMessageListAsString(resultMessage.get(ConstEnum.FAIL));
		if(StringUtils.isNotEmpty(errorMessage)){
			logger.error("Error : [{}]",errorMessage);
		}
	}

	private String getCmd(String job){
		
		StringBuilder builder = new StringBuilder();
		
		String sysPropOption = " -D";
		builder.append("java").append(sysPropOption)
			.append("consumer.root.dir").append("=").append(rootDir);
		
		builder.append(sysPropOption).append("job.id").append("=").append(job);
		
		builder.append(" -jar ").append(rootDir).append(File.separator)
			.append("epis-ws-consumer.jar").append(" Biz");
		
//		builder.append("\"");
		logger.debug("Command : [{}]",builder.toString());
		return builder.toString();
	}
	
}
