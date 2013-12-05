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

public class Win2000ScheduleRegister extends AbstractScheduleRegister {

	

	@Override
	public Map<ConstEnum, List<String>> registerSchedule(String[] jobNames) throws Exception {
		Runtime rt = Runtime.getRuntime();
		Map<ConstEnum,List<String>> result = new HashMap<ConstEnum,List<String>>();
		result.put(ConstEnum.SUCCESS, new ArrayList<String>());
		result.put(ConstEnum.FAIL, new ArrayList<String>());
		
		//retrieve existed & remove
		String findTemplate = "cmd /c \"at | find \"%1$s %2$s\"\"";
		String removeTemplate = "cmd /c at %1$s /delete /yes";
		String registerTemplate = "cmd /c \"at %1$s /every:M,T,W,Th,F,S,Su cmd /c \"%2$sbiz.bat %3$s\"\"";
		Map<ConstEnum,List<String>> processed = null;
		for(String jobName : jobNames){
			//retrieve AT row existed
			processCmd(rt, String.format(findTemplate, "biz.bat", jobName));
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
				processCmd(rt, String.format(removeTemplate, tokens[0]));
			}
			
			//register
			processed = processCmd(rt, String.format(
					registerTemplate
					,jobProp.getProperty(jobName+".execTime")
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
