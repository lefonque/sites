package org.epis.ws.common.entity;

public class JDBConnectionVO {


	/**
	 * DB 접속정보 고유 ID
	 */
	private String connectionId;
	/**
	 * 작업시 Agent가 접속할 DB의 jdbc driver class명
	 */
	private String jdbcDriverClassName;
	/**
	 * 작업시 Agent가 접속할 DB의 URL
	 */
	private String jdbcUrl;
	/**
	 * 작업시 Agent가 접속할 DB의 User명
	 */
	private String jdbcUsername;
	/**
	 * 작업시 Agent가 접속할 DB의 패스워드
	 */
	private String jdbcPassword;
	/**
	 * DB유형(Oracle,SqlServer...)
	 */
	private String dbType;

	/**
	 * Job 고유 ID
	 */
	private String jobId;
	/**
	 * Job을 소유한 Agent ID
	 */
	private String agentId;
	
	
	public String getConnectionId() {
		return connectionId;
	}
	public String getJdbcDriverClassName() {
		return jdbcDriverClassName;
	}
	public String getJdbcUrl() {
		return jdbcUrl;
	}
	public String getJdbcUsername() {
		return jdbcUsername;
	}
	public String getJdbcPassword() {
		return jdbcPassword;
	}
	public String getDbType() {
		return dbType;
	}
	public String getJobId() {
		return jobId;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setConnectionId(String jdbcInfoId) {
		this.connectionId = jdbcInfoId;
	}
	public void setJdbcDriverClassName(String jdbcDriverClassName) {
		this.jdbcDriverClassName = jdbcDriverClassName;
	}
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}
	public void setJdbcUsername(String jdbcUsername) {
		this.jdbcUsername = jdbcUsername;
	}
	public void setJdbcPassword(String jdbcPassword) {
		this.jdbcPassword = jdbcPassword;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	
}
