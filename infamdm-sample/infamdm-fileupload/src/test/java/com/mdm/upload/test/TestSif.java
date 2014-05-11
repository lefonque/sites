package com.mdm.upload.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mdm.upload.dao.SifDAOImpl;
import com.mdm.upload.vo.AttachmentsDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:/META-INF/spring/main-context.xml"})
public class TestSif {
	
//	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SifDAOImpl dao;
	
	@Test
	public void testSif(){
		
		
		AttachmentsDTO attachment = new AttachmentsDTO();
		attachment.setRowid_object("1");
		attachment.setDisplay_name("한글_MDM_971.pdf");
		attachment.setStored_path("D:\\Docs\\MDM\\Multidomain_Edition_971\\한글_MDM_971.pdf");
		attachment.setBo_name("C_PARTY");
		attachment.setBo_rowid("79");
		
		try {
			dao.insertUpdateAttachments(attachment);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
