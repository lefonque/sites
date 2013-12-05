package org.epis.ws.common.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class RecordMapEntry {
	
	private String key;
	
	private Object value;
	
	public RecordMapEntry(){}
	
	public RecordMapEntry(String key, Object value){
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}
	public Object getValue() {
		return value;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public void setValue(Object value) {
		this.value = value;
	}
}
