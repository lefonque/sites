package org.epis.ws.agent.vo;


public class ColumnInfoVO {

	/** Column Name */
	private String fieldName;
	/** Constants in java.sql.Types */
	private int type;
	/** Values */
	private Object value;
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
	
}
