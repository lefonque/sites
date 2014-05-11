package com.mdm.upload.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mdm.upload.service.SifService;
import com.mdm.upload.vo.AttachmentsDTO;
import com.mdm.upload.vo.UploadFormVO;

@Controller
public class UploadController {

	private final Logger logger = LoggerFactory.getLogger(UploadController.class);
	
	@Autowired
	private SifService service;
	
	@RequestMapping("/popupUpload")
	public String popupUpload(@ModelAttribute("uploadForm") UploadFormVO formVO
			,HttpServletRequest request){
		String result = "popupUpload";
		logger.debug("=== popupUpload entered : [{}] ===", request.getRemoteAddr());
		return result;
	}
	
	@RequestMapping("/debug")
	public String debug() throws Exception{
		String result = "popupUpload";
		
		AttachmentsDTO attachment = new AttachmentsDTO();
		attachment.setRowid_object("1");
		attachment.setDisplay_name("왕서방.pdf");
		attachment.setStored_path("D:\\Docs\\MDM\\Multidomain_Edition_971\\왕서방.pdf");
		attachment.setBo_name("C_PARTY");
		attachment.setBo_rowid("30");
		
		service.insertAttachments(attachment);
		return result;
	}
}
