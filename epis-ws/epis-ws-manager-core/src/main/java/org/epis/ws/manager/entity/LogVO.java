package org.epis.ws.manager.entity;

import java.sql.Timestamp;

public class LogVO {

	private String logId;
	private String agentId;
	private String jobId;
	private String resultFlag;
	private Timestamp createDate;
	
	public String getLogId() {
		return logId;
	}
	public String getAgentId() {
		return agentId;
	}
	public String getJobId() {
		return jobId;
	}
	public String getResultFlag() {
		return resultFlag;
	}
	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setLogId(String logId) {
		this.logId = logId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public void setResultFlag(String resultFlag) {
		this.resultFlag = resultFlag;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	} 
}
