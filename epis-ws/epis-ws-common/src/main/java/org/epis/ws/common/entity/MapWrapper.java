package org.epis.ws.common.entity;

import java.util.HashMap;


/**
 * <pre>
 * <p>웹서비스상의 Map인스턴스를 송수신하기 위한 Wrapping 클래스</p>
 * 
 * Map계열 Collection은 JAX-WS 상에서 input, output parameter로 지원하지 않으나,
 * 본 클래스와 같이 wrapping을 하면 정상적인 사용이 가능하다.
 * 
 * </pre>
 * @author developer
 *
 */
public class MapWrapper {
	
	public final HashMap<String,Object> core;
	
	public MapWrapper(){
		core = new HashMap<String,Object>();
	}
	
	public MapWrapper(HashMap<String,Object> core){
		this.core = core;
	}
}
