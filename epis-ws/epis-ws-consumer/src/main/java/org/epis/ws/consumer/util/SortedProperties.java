package org.epis.ws.consumer.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

public class SortedProperties extends Properties {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1785461361885749253L;

	public SortedProperties(Properties prop){
		if(prop!=null){
			putAll(prop);
		}
	}
	
	@Override
	public synchronized Enumeration<Object> keys() {
		Enumeration<Object> keyEnum = super.keys();
		List<Object> list = Collections.list(keyEnum);
		Collections.sort(list,new KeyComparator());
		return Collections.enumeration(list);
	}
	
	
	
	static final class KeyComparator implements Comparator<Object>{
		
		public int compare(Object a, Object b){
			return String.class.cast(a).compareToIgnoreCase(String.class.cast(b));
		}
	}
}
