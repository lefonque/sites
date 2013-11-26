package org.epis.ws.common.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;


public class AgentVO {

	private String agentId;
	private String orgCode;
	private String operatingSystem;
	private String charset;
	private String websvcUser;
	private String websvcPass;
	private String officerName;
	private String officerContact;
	
	private String smsCellNo;
	private String smsUseYn;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createdDate;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date modifiedDate;
	
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String clientId) {
		this.agentId = clientId;
	}
	public String getOperatingSystem() {
		return operatingSystem;
	}
	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getWebsvcUser() {
		return websvcUser;
	}
	public void setWebsvcUser(String websvcUser) {
		this.websvcUser = websvcUser;
	}
	public String getWebsvcPass() {
		return websvcPass;
	}
	public void setWebsvcPass(String websvcPass) {
		this.websvcPass = websvcPass;
	}
	public String getOfficerName() {
		return officerName;
	}
	public void setOfficerName(String officerName) {
		this.officerName = officerName;
	}
	public String getOfficerContact() {
		return officerContact;
	}
	public void setOfficerContact(String officerContact) {
		this.officerContact = officerContact;
	}
	public String getSmsCellNo() {
		return smsCellNo;
	}
	public void setSmsCellNo(String smsCellNo) {
		this.smsCellNo = smsCellNo;
	}
	public String getSmsUseYn() {
		return smsUseYn;
	}
	public void setSmsUseYn(String smsUseYn) {
		this.smsUseYn = smsUseYn;
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
