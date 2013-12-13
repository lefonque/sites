package org.epis.ws.provider.service;

import java.util.Properties;

import org.epis.ws.common.entity.AgentVO;
import org.epis.ws.common.entity.JobVO;
import org.epis.ws.provider.dao.SMSQueueDAO;
import org.epis.ws.provider.entity.SMSQueueRecord;
import org.epis.ws.provider.utils.PropertyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class SMSQueueService {

	@Autowired
	@Qualifier("smsProp")
	private Properties smsProp;
	
	@Autowired
	private SMSQueueDAO dao;
	
	public int addSMSQueueRecord(AgentVO agentInfo, JobVO job){
		int result = 0;
		
		String template = smsProp.getProperty(PropertyEnum.SMS_CONTENT_TEMPLATE.getKey());
		String content = String.format(template, agentInfo.getAgentId(), job.getJobId());
		
		String callback = smsProp.getProperty(PropertyEnum.SMS_PHONE_CALLER.getKey());
		
		SMSQueueRecord queueRecord = new SMSQueueRecord();
		queueRecord.setContent(content);
		queueRecord.setCallback(callback);
		queueRecord.setRecipientNum(agentInfo.getSmsCellNo());
		
		dao.insertSMSQueueRecord(queueRecord);
		return result;
	}
}