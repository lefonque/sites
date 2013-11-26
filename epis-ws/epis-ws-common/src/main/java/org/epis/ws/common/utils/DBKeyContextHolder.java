package org.epis.ws.common.utils;

import java.util.Map;

import org.springframework.util.Assert;

public class DBKeyContextHolder {

	private static final ThreadLocal<Map<String,Object>> holder = new ThreadLocal<Map<String,Object>>();
	
	public static final Map<String,Object> get(){
		return holder.get();
	}
	
	public static final void set(Map<String,Object> key){
		Assert.notNull(key, "JOB NAME cannot be null");
		holder.set(key);
	}
	
	public static final void clear(){
		holder.remove();
	}
}
