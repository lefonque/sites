package com.mdm.upload.vo;

public class AttachmentsDTO {

	private String rowid_object;
	private String display_name;
	private String stored_path;
	private String bo_name;
	private String bo_rowid;
	
	public String getRowid_object() {
		return rowid_object;
	}
	public void setRowid_object(String rowid_object) {
		this.rowid_object = rowid_object;
	}
	public String getDisplay_name() {
		return display_name;
	}
	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}
	public String getStored_path() {
		return stored_path;
	}
	public void setStored_path(String stored_path) {
		this.stored_path = stored_path;
	}
	public String getBo_name() {
		return bo_name;
	}
	public void setBo_name(String bo_name) {
		this.bo_name = bo_name;
	}
	public String getBo_rowid() {
		return bo_rowid;
	}
	public void setBo_rowid(String bo_rowid) {
		this.bo_rowid = bo_rowid;
	}
}
