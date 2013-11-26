package org.epis.ws.manager.web.utils;

public enum ColumnName {

	LOGIN_USER("loginUsername")
	,LOGIN_PASS("loginPassword")
	;
	
	private final String colName;
	ColumnName(String colName){
		this.colName = colName;
	}
	
	public String getColName(){
		return colName;
	}
}
