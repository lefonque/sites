package org.epis.ws.manage.desktop.model;


public enum AgentTableColumnEnum {
	CHECKBOX(0,null,null)
	,ORG_NAME(1,"orgCode","기관명")
	,OPERATING_SYSTEM(2,"operatingSystem","OS")
	,WEBSVC_USER(3,"websvcUser","웹서비스 유저")
	,SMS_USE_YN(4,"smsUseYn","SMS사용여부")
	,OFFICER_NAME(5,"officerName","담당자")
	,CREATED_DATE(6,"createdDate","등록일")
	,MODIFIED_DATE(7,"modifiedDate","변경일")
	;
	
	AgentTableColumnEnum(int index,String fieldName,String headerText){
		this.index = index;
		this.fieldName = fieldName;
		this.headerText = headerText;
	}
	
	private final int index;
	private final String fieldName;
	private final String headerText;
	
	public static AgentTableColumnEnum valueOf(int index){
		AgentTableColumnEnum result = null;
		for(AgentTableColumnEnum columnIndex : AgentTableColumnEnum.values()){
			if(columnIndex.index==index){
				result = columnIndex;
				break;
			}
		}
		return result;
	}

	public int getIndex() {
		return index;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getHeaderText() {
		return headerText;
	}
}
