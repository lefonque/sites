package org.epis.ws.provider.entity;

public class SMSQueueRecord {

	private String mtPr;
	private String dateClientReq;
	private String content;
	private String callback;
	private String serviceType;
	private String broadcastYn;
	private String msgStatus;
	private String recipientNum;
	private String recipientNet;
	private String recipientNpsend;
	private String countryCode;
	private String charset;
	private String msgType;
	private String cryptoYn;
	private String connectionid;
	
	public String getMtPr() {
		return mtPr;
	}
	public String getDateClientReq() {
		return dateClientReq;
	}
	public String getContent() {
		return content;
	}
	public String getCallback() {
		return callback;
	}
	public String getServiceType() {
		return serviceType;
	}
	public String getBroadcastYn() {
		return broadcastYn;
	}
	public String getMsgStatus() {
		return msgStatus;
	}
	public String getRecipientNum() {
		return recipientNum;
	}
	public String getRecipientNet() {
		return recipientNet;
	}
	public String getRecipientNpsend() {
		return recipientNpsend;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public String getCharset() {
		return charset;
	}
	public String getMsgType() {
		return msgType;
	}
	public String getCryptoYn() {
		return cryptoYn;
	}
	public String getConnectionid() {
		return connectionid;
	}
	public void setMtPr(String mtPr) {
		this.mtPr = mtPr;
	}
	public void setDateClientReq(String dateClientReq) {
		this.dateClientReq = dateClientReq;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setCallback(String callback) {
		this.callback = callback;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public void setBroadcastYn(String broadcastYn) {
		this.broadcastYn = broadcastYn;
	}
	public void setMsgStatus(String msgStatus) {
		this.msgStatus = msgStatus;
	}
	public void setRecipientNum(String recipientNum) {
		this.recipientNum = recipientNum;
	}
	public void setRecipientNet(String recipientNet) {
		this.recipientNet = recipientNet;
	}
	public void setRecipientNpsend(String recipientNpsend) {
		this.recipientNpsend = recipientNpsend;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public void setCryptoYn(String cryptoYn) {
		this.cryptoYn = cryptoYn;
	}
	public void setConnectionid(String connectionid) {
		this.connectionid = connectionid;
	}
}
