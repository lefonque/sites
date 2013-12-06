package org.epis.ws.manager.entity;

import java.sql.Timestamp;

import org.springframework.format.annotation.DateTimeFormat;

public class LogVO {

	private String logId;
	private String agentId;
	private String jobId;
	private String jobName;
	private int rowCount;
	private String resultFlag;
	private String resultFlagText;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Timestamp createDate;
	
	public String getLogId() {
		return logId;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	public String getAgentId() {
		return agentId;
	}
	public String getJobId() {
		return jobId;
	}
	public String getJobName() {
		return jobName;
	}
	public String getResultFlagText() {
		return resultFlagText;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public void setResultFlagText(String resultFlagText) {
		this.resultFlagText = resultFlagText;
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
