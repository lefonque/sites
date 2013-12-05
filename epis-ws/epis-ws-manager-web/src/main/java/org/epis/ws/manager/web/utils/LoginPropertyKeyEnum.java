package org.epis.ws.manager.web.utils;

public enum LoginPropertyKeyEnum {

	LOGIN_USER("USER_ID")
	,LOGIN_PASS("USER_PSWD")
	;
	
	private final String colName;
	LoginPropertyKeyEnum(String colName){
		this.colName = colName;
	}
	
	public String getColName(){
		return colName;
	}
}
