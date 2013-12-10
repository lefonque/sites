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
 * <pre>
 * <p>epis-ws-consumer.jar를 실행시킨다.</p>
 * 
 * Runtime 클래스를 이용하여 consumer용 jar파일을 실행시킨다.
 * Runtime의 실질적인 사용은 RuntimeExecutor 클래스를 이용함.
 * 
 * Runtime으로 실행할 command는 다음과 같은 형태이다.
 * 
 * java -Dconsumer.root.dir=[consumer용 jar파일이 위치한 디렉토리] -Djob.id=[작업ID] -jar [consumer용 jar파일명] Biz
 * e.g.)java -Dconsumer.root.dir=C:\Users\developer\git\sites\epis-ws\epis-ws-consumer -Djob.id=JOB-2 -jar epis-ws-consumer.jar Biz
 * </pre>
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
	
	/**
	 * <pre>
	 * <p>Scheduler에 의해 실행되는 메서드</p>
	 * 
	 * 인자로 받은 jobId에 해당하는 처리를 수행하도록 command를 생성하여 실행함.
	 * </pre>
	 * @param jobId
	 */
	public void execute(String jobId){
		Map<ConstEnum, List<String>> resultMessage = new HashMap<ConstEnum,List<String>>();
		String cmd = getCmd(jobId);
		try {
//			//for debug
//			cmd = "java -Dconsumer.root.dir=C:\\Sites\\Garage\\consumer -Djob.id=JOB-2 -cp .;C:\\Sites\\Garage\\consumer -jar C:\\Sites\\Garage\\consumer\\epis-ws-consumer.jar Biz";
//			cmd = "java -version";
			logger.info("===== Start Schedule Job : [{}] =====",jobId);
			resultMessage = executor.executeCMD(cmd);
			logger.info("===== End Schedule Job : [{}] =====",jobId);
		} catch (IOException e) {
			logger.error("Exception Occurred on RuntimeExecutor.executeCMD()",e);
		} catch (InterruptedException e) {
			logger.error("Exception Occurred on RuntimeExecutor.executeCMD()",e);
		} catch (ExecutionException e) {
			logger.error("Exception Occurred on RuntimeExecutor.executeCMD()",e);
		} 

		String errorMessage = executor.getMessageListAsString(resultMessage.get(ConstEnum.FAIL));
		if(StringUtils.isNotEmpty(errorMessage)){
			logger.error("Error : [{}]",errorMessage);
		}
	}

	/**
	 * <pre>
	 * <p>consumer용 jar를 실행하는 command를 작성한다.</p>
	 * 
	 * 인자로 받은 job id에 해당하는 처리를 하도록 consumer용
	 * jar를 실행하는 command를 작성한다.
	 * </pre>
	 * @param jobId
	 * @return
	 */
	private String getCmd(String jobId){
		
		StringBuilder builder = new StringBuilder();
		
		String sysPropOption = " -D";
		builder.append("java").append(sysPropOption)
			.append("consumer.root.dir").append("=").append(rootDir);
		
		builder.append(sysPropOption).append("job.id").append("=").append(jobId);
		
		builder.append(" -jar ").append(rootDir).append(File.separator)
			.append("epis-ws-consumer.jar").append(" Biz");
		
		logger.debug("Command : [{}]",builder.toString());
		return builder.toString();
	}
}
