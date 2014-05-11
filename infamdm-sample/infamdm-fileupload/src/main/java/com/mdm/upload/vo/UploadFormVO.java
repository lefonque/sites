package com.mdm.upload.vo;

import org.springframework.web.multipart.MultipartFile;

public class UploadFormVO {

	private AttachmentsDTO attachment;
	private String SiperianRowID;
	private String download;
	private MultipartFile file;
	private String message;
	
	public AttachmentsDTO getAttachment() {
		return attachment;
	}
	public void setAttachment(AttachmentsDTO attachment) {
		this.attachment = attachment;
	}
	public String getSiperianRowID() {
		return SiperianRowID;
	}
	public void setSiperianRowID(String siperianRowID) {
		SiperianRowID = siperianRowID;
	}
	public String getDownload() {
		return download;
	}
	public void setDownload(String download) {
		this.download = download;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
