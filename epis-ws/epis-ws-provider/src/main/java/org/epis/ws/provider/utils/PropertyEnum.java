package org.epis.ws.provider.utils;

public enum PropertyEnum {

	SMS_CONTENT_TEMPLATE("sms.content.template")
	,SMS_PHONE_CALLER("sms.phone.caller")
	;
	
	private String key;
	
	private PropertyEnum(String key) {
		this.key = key;
	}
	
	public String getKey(){
		return key;
	}
}
