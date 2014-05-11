package com.mdm.upload.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mdm.upload.dao.SifDAO;
import com.mdm.upload.vo.AttachmentsDTO;

@Service
public class SifServiceImpl implements SifService {

	@Autowired
	private SifDAO dao;
	
	@Override
	@Transactional(rollbackFor=Throwable.class, propagation=Propagation.REQUIRED)
	public boolean insertAttachments(AttachmentsDTO attachment) throws Exception {
		boolean result = dao.insertUpdateAttachments(attachment);
		if(result){
			throw new Exception("For Rollback Testing");
		}
		return result;
	}

}
