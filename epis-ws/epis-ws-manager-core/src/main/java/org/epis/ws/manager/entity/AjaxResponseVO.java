package org.epis.ws.manager.entity;



public class AjaxResponseVO {

	private boolean success;
	private String message;
	private int count;
	
	public AjaxResponseVO(){
		this(false,null,0);
	}
	
	public AjaxResponseVO(boolean success, String message, int count){
		this.success = success;
		this.message = message;
		this.count = count;
	}
	
	public boolean isSuccess() {
		return success;
	}
	public String getMessage() {
		return message;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
		
}
