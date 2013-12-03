package org.epis.ws.manager.util;

public class DynamicDataSourceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4732861096889453182L;
	
	public DynamicDataSourceException(){
		super();
	}
	
	public DynamicDataSourceException(String message){
		super(message);
	}

	public DynamicDataSourceException(String message, Throwable t){
		super(message, t);
	}
}
