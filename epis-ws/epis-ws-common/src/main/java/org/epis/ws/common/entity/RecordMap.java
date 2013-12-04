package org.epis.ws.common.entity;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
public class RecordMap {
	
	@XmlElementWrapper(name="entryList")
	public Set<RecordMapEntry> entry = new HashSet<RecordMapEntry>();
	
	
}
