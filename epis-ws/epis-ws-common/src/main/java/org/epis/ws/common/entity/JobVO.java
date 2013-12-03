package org.epis.ws.common.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;


/**
 * 하나의 Configuration은 두개의 Schedule Job을 가진다.
 * - Config 설정 동기화 Schedule
 * - Business처리 Schedule
 * @author developer
 *
 */
public class JobVO {

	private String jobId;
	private String agentId;
	private String jobName;
	private String jobType;
	private String agentExecTime;

	private String sqlMain;
	private String sqlPre;
	private String sqlPost;

	private String jdbcDriverClassName;
	private String jdbcUrl;
	private String jdbcUsername;
	private String jdbcPassword;
	
	private String batchSelectCount;

	private String serverSql;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createdDate;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date modifiedDate;
	
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String clientId) {
		this.agentId = clientId;
	}
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	public String getAgentExecTime() {
		return agentExecTime;
	}
	public void setAgentExecTime(String execTime) {
		this.agentExecTime = execTime;
	}
	public String getJdbcUrl() {
		return jdbcUrl;
	}
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}
	public String getJdbcUsername() {
		return jdbcUsername;
	}
	public void setJdbcUsername(String jdbcUsername) {
		this.jdbcUsername = jdbcUsername;
	}
	public String getJdbcPassword() {
		return jdbcPassword;
	}
	public void setJdbcPassword(String jdbcPass) {
		this.jdbcPassword = jdbcPass;
	}
	public String getJdbcDriverClassName() {
		return jdbcDriverClassName;
	}
	public void setJdbcDriverClassName(String jdbcDriverClass) {
		this.jdbcDriverClassName = jdbcDriverClass;
	}
	public String getSqlMain() {
		return sqlMain;
	}
	public void setSqlMain(String sqlMain) {
		this.sqlMain = sqlMain;
	}
	public String getSqlPre() {
		return sqlPre;
	}
	public void setSqlPre(String sqlPre) {
		this.sqlPre = sqlPre;
	}
	public String getSqlPost() {
		return sqlPost;
	}
	public void setSqlPost(String sqlPost) {
		this.sqlPost = sqlPost;
	}
	public String getServerSql() {
		return serverSql;
	}
	public void setServerSql(String serverSql) {
		this.serverSql = serverSql;
	}
	public String getBatchSelectCount() {
		return batchSelectCount;
	}
	public void setBatchSelectCount(String batchSelectCount) {
		this.batchSelectCount = batchSelectCount;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	
}
