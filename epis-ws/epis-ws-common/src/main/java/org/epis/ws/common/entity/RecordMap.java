package org.epis.ws.common.entity;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * 미해결 : 키 중복시 덮어쓰기 가능해야 함.
 * @author developer
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class RecordMap {
	
	@XmlElementWrapper(name="entryList")
	public Set<RecordMapEntry> entry = new HashSet<RecordMapEntry>();
	
	
}
