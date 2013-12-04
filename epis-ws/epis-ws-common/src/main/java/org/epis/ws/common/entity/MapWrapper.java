package org.epis.ws.common.entity;

import java.util.HashMap;


public class MapWrapper {
	
	public final HashMap<String,Object> core;
	
	public MapWrapper(){
		core = new HashMap<String,Object>();
	}
	
	public MapWrapper(HashMap<String,Object> core){
		this.core = core;
	}
}
