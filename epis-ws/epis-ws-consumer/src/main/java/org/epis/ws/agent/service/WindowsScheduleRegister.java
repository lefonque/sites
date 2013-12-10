package org.epis.ws.agent.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.commons.collections.CollectionUtils;
import org.epis.ws.agent.util.PropertyEnum;
import org.epis.ws.common.utils.ConstEnum;
import org.springframework.stereotype.Service;

/**
 * <pre>
 * <p>Windows 2000 이후의 NT계열 OS 에서 Schedule Job을 등록한다.</p>
 * 
 * Schtasks.exe를 이용하여 Schedule Job을 등록한다.
 * </pre>
 * @author developer
 *
 */
@Service("WindowsScheduleRegister")
public class WindowsScheduleRegister extends AbstractScheduleRegister {
	

//	@Override
//	public String getScheduleCmd(String scheduleType, String cmd){
//		String completeCmd = null;
//		
//		final String pathAgentRoot = System.getProperty(PropertyEnum.SYS_ROOT_DIR.getKey());
//		StringBuilder propFilePath = new StringBuilder();
//		propFilePath.append("\"").append(pathAgentRoot)
//				.append(File.separator)
//				.append(scheduleType).append(".bat");
//		propFilePath.append(" ").append(pathAgentRoot).append("\"");
//		completeCmd = String.format(cmd
//				,agentInfo.getProperty(PropertyEnum.SCHEDULE_NAME.getKey() + scheduleType)
//				,propFilePath.toString());
//		return completeCmd;
//	}
	/**
	 * Schedule이 등록되어 있는지를 확인한다.
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	private boolean isExistSchedule(String scheduleName)
			throws IOException, InterruptedException, ExecutionException {
		
		StringBuilder cmd = new StringBuilder();
		cmd.append("cmd /c \"")
			.append("SCHTASKS /Query | find ")
			.append("\"").append(scheduleName).append("\"")
			.append("\"");
		Map<ConstEnum,List<String>> result = runtimeExecutor.executeCMD(cmd.toString());
		
		boolean isExist = false;
		if(CollectionUtils.isNotEmpty(result.get(ConstEnum.SUCCESS))){
			isExist = true;
		}
		return isExist;
	}
	
	
	/**
	 * 등록된 Schedule을 삭제한다.
	 * @param config
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	private Map<ConstEnum,List<String>> removeSchedule(String scheduleName)
			throws IOException, InterruptedException, ExecutionException {

		StringBuilder cmd = new StringBuilder();
		cmd.append("cmd /c \"")
			.append("schtasks /Delete /TN \"")
			.append(scheduleName)
			.append("\" /F")
			.append("\"");
		
		Map<ConstEnum,List<String>> result = runtimeExecutor.executeCMD(cmd.toString());
		return result;
	}
	
	/**
	 * <pre>
	 * <p>schtasks.exe를 이용하여 Schedule을 등록한다.</p>
	 * 
	 * 다음과 같은 절처를 거친다.
	 * 	1. 해당 job id의 Job이 등록되어 있는지 조회한다.
	 * 	2. 등록되어 있다면 삭제한다.
	 * 	3. 해당 job id의 Job을 등록한다.
	 * </pre>
	 * @param config
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@Override
	public Map<ConstEnum,List<String>> registerSchedule(String[] changedJobName)
			throws IOException, InterruptedException, ExecutionException{
		
		Map<ConstEnum,List<String>> result = new HashMap<ConstEnum,List<String>>();
		result.put(ConstEnum.SUCCESS, new ArrayList<String>());
		result.put(ConstEnum.FAIL, new ArrayList<String>());
		
		StringBuilder builder = new StringBuilder();
		for(String jobName : changedJobName){
			String scheduleName = jobProp.getProperty(jobName + ".name");
			
			builder.append("cmd /c SCHTASKS /Create /TN ").append(scheduleName)
			.append(" /TR \"")
			.append(System.getProperty(PropertyEnum.SYS_ROOT_DIR.getKey()))
			.append(File.separator).append("biz.bat ").append(jobName).append("\"")
			.append(" /SC DAILY /ST ")
			.append(jobProp.getProperty(jobName + ".execTime"));
			
			if(isExistSchedule(scheduleName)){
				removeSchedule(scheduleName);
			}
			Map<ConstEnum,List<String>> processed = runtimeExecutor.executeCMD(builder.toString());
			builder.setLength(0);
			List<String> msgList = processed.get(ConstEnum.SUCCESS);
			if(CollectionUtils.isNotEmpty(msgList)){
				result.get(ConstEnum.SUCCESS).addAll(msgList);
			}
			msgList = processed.get(ConstEnum.FAIL);
			if(CollectionUtils.isNotEmpty(msgList)){
				result.get(ConstEnum.FAIL).addAll(msgList);
			}
		}
		return result;
	}
}
