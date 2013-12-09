package org.epis.ws.agent.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.epis.ws.agent.util.PropertyEnum;
import org.epis.ws.common.utils.ConstEnum;
import org.springframework.stereotype.Service;

@Service("UnixScheduleRegister")
public class UnixScheduleRegister extends AbstractScheduleRegister {

	private final String rootDir = System.getProperty(PropertyEnum.SYS_ROOT_DIR.getKey());
	
//	public String getScheduleCmd(String jobName, String cmd){
//		StringBuilder completedCmd = new StringBuilder();
//		
//		completedCmd.append(cmd).append(" ")
//			.append(rootDir)
//			.append(File.separator)
//			.append(jobName).append(".sh")
//			.append(" '").append(rootDir).append("'");
//		
//		return completedCmd.toString();
//	}
	
	public Map<ConstEnum,List<String>> registerSchedule(final String[] changedJobName)
			throws IOException, InterruptedException, ExecutionException {
		
		Writer writer = null;
		File cronFile = null;
		
		//crontab -l
		Map<ConstEnum,List<String>> result = runtimeExecutor.executeCMD("crontab -l");
		List<String> oldCronList = result.get(ConstEnum.SUCCESS);
		if(CollectionUtils.isNotEmpty(result.get(ConstEnum.FAIL))){
			logger.error("FAILE TO REGISTER SCHEDULE : Fail to get 'crontab -l'");
			return result;
		}
		
		if(CollectionUtils.isEmpty(oldCronList)) {
			oldCronList = new ArrayList<String>();
		}
		
		//기 등록된 건 제거
		List<String> crontabList = new ArrayList<String>();
		StringBuilder keyword = new StringBuilder("biz.sh ");
		final int prefixEnd = keyword.length();
		for(String cron : oldCronList){
			crontabList.add(cron);
			for(String jobName : changedJobName){
				keyword.setLength(prefixEnd);
				keyword.append(jobName);
				if(cron.contains(keyword)){
					crontabList.remove(cron);
					break;
				}
			}
		}
		oldCronList.clear();
		oldCronList = null;
		
		//변경건 반영
		//write result of (crontab -l) + cmds to file
		String cronFormat = "%1$s %2$s * * *";
		for(String jobName : changedJobName){
			String[] time = StringUtils.splitPreserveAllTokens(jobProp.getProperty(jobName + ".execTime"),":");
			crontabList.add(String.format(cronFormat, time[1], time[0]));
		}
		
		StringBuilder builder = new StringBuilder();
		builder.append(rootDir)
			.append(File.separator).append("schedule.cron");
		cronFile = new File(builder.toString());
		try {
			writer = new BufferedWriter(new FileWriter(cronFile,false));
			IOUtils.writeLines(crontabList, null, writer);
			writer.flush();
		} catch (IOException e) {
			logger.error("===== Exception Occurred On Writing Crontab File!!! =====",e);
		} finally {
			IOUtils.closeQuietly(writer);
		}
		
		//crontab cronfile
		if(cronFile.exists()){
			try {
				//crontab file명 => overwrite함
				builder.setLength(0);
				result = runtimeExecutor.executeCMD("crontab -r");
				builder.append("crontab ").append(cronFile.getAbsolutePath());
				result = runtimeExecutor.executeCMD(builder.toString());
			} finally {
				FileUtils.deleteQuietly(cronFile);
			}
		}
		
		return result;
	}
}
