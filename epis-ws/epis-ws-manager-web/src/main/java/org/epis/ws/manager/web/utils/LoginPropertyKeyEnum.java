package org.epis.ws.manager.web.utils;

public enum LoginPropertyKeyEnum {

	LOGIN_USER("loginUsername")
	,LOGIN_PASS("loginPassword")
	;
	
	private final String colName;
	LoginPropertyKeyEnum(String colName){
		this.colName = colName;
	}
	
	public String getColName(){
		return colName;
	}
}
