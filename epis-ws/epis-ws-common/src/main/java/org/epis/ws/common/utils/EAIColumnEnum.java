package org.epis.ws.common.utils;

/**
 * <pre>
 * 웹서비스로 송수신하는 업무데이터의 Flag값과 처리일자를 저장하는 컬럼명을
 * 가지는 Enum클래스
 * </pre>
 * @author developer
 *
 */
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
