package org.epis.ws.common.utils;

public enum EAIColumnEnum {

	EFLAG("SEND_FLAG")
	,SND_EFLAG("SEND_FLAG")
	,RECV_EFLAG("SEND_FLAG")
	
	,EDATE("REG_DATE")
	,SND_EDATE("REG_DATE")
	,RECV_EDATE("REG_DATE")
	;
			
	private String columnName;
	
	EAIColumnEnum(String columnName){
		this.columnName = columnName;
	}
	
	public String getColumnName(){
		return columnName;
	}
}
