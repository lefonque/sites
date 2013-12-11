package org.epis.ws.agent.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.epis.ws.agent.util.PropertyEnum;
import org.epis.ws.common.utils.ConstEnum;
import org.springframework.stereotype.Service;

/**
 * <pre>
 * <p>Windows 2000 에서 Schedule Job을 등록한다.</p>
 * 
 * at.exe를 이용하여 schedule job을 등록한다.
 * </pre>
 * @author developer
 *
 */
@Service("Win2000ScheduleRegister")
public class Win2000ScheduleRegister extends AbstractScheduleRegister {

	

	/** 
	 * <pre>
	 * <p>at.exe으로 스케쥴등록을 한다.</p>
	 * 
	 * Windows 2000의 경우, Schtasks.exe파일이 없고, at.exe를 이용해야 한다.
	 * 처리절차는 다음과 같다.
	 * 	1. 해당 job id의 Job이 등록되어 있는지 조회한다.
	 * 	2. 등록되어 있다면 삭제한다.(중복등록되어 있다면 중복된 건 모두 삭제한다.)
	 * 	3. 해당 job id의 Job을 등록한다.
	 * 
	 * </pre>
	 * @see org.epis.ws.agent.service.AbstractScheduleRegister#registerSchedule(java.lang.String[])
	 */
	@Override
	public Map<ConstEnum, List<String>> registerSchedule(String[] jobNames) throws Exception {
		
		Map<ConstEnum,List<String>> result = new HashMap<ConstEnum,List<String>>();
		result.put(ConstEnum.SUCCESS, new ArrayList<String>());
		result.put(ConstEnum.FAIL, new ArrayList<String>());
		
		//retrieve existed & remove
		String findTemplate = "cmd /c \"at | find \"%1$s %2$s\"\"";
		String removeTemplate = "cmd /c \"at %1$s /delete /yes\"";
		String registerTemplate = "cmd /c \"at %1$s /every:M,T,W,Th,F,S,Su cmd /c \"%2$sbiz.bat %3$s\"\"";
		Map<ConstEnum,List<String>> processed = null;
		for(String jobName : jobNames){
			//retrieve AT row existed
			runtimeExecutor.executeCMD(String.format(findTemplate, "biz.bat", jobName));
			List<String> lines = processed.get(ConstEnum.SUCCESS);
			if(CollectionUtils.isEmpty(lines)){
				continue;
			}
			result.get(ConstEnum.SUCCESS).addAll(lines);
			
			//remove AT row existed
			for(String line : lines){
				String[] tokens = StringUtils.split(line.trim(), " ");
				if(ArrayUtils.isEmpty(tokens)){
					continue;
				}
				//remove AT row
				runtimeExecutor.executeCMD(String.format(removeTemplate, tokens[0]));
			}
			
			//register
			processed = runtimeExecutor.executeCMD(String.format(
					registerTemplate
					,jobProp.getProperty(jobName+PropertyEnum.JOB_SUFFIX_EXEC_TIME.getKey())
					,System.getProperty(PropertyEnum.SYS_ROOT_DIR.getKey())
					,jobName
				)
			);
			
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
