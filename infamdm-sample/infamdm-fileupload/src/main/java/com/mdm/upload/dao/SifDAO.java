package com.mdm.upload.dao;

import com.mdm.upload.vo.AttachmentsDTO;

public interface SifDAO {
	public boolean insertUpdateAttachments(AttachmentsDTO attachment) throws Exception;
}
