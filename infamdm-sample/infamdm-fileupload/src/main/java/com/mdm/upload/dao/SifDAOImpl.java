package com.mdm.upload.dao;

import java.lang.reflect.Field;

import javax.transaction.UserTransaction;

import org.apache.commons.lang3.StringUtils;
import org.jboss.ejb.client.EJBClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mdm.upload.vo.AttachmentsDTO;
import com.siperian.sif.client.EjbSiperianClient;
import com.siperian.sif.client.SiperianClient;
import com.siperian.sif.message.Record;
import com.siperian.sif.message.RecordKey;
import com.siperian.sif.message.mrm.PutRequest;
import com.siperian.sif.message.mrm.PutResponse;

@Repository
public class SifDAOImpl implements SifDAO{
	
	private static final String SOURCE_SYSTEM = "Admin";
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private SiperianClient sifClient;
	
	
	@Override
	public boolean insertUpdateAttachments(AttachmentsDTO attachment) throws Exception {
		boolean result = false;
		logger.debug("===> jboss.node.name : {}",sifClient.getConfig().getProperty("jboss.node.name"));
		
		final Record record = new Record();
		record.setSiperianObjectUid("C_ATTACHMENTS");
		
		com.siperian.sif.message.Field column = null;
		for(Field field : attachment.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			column = new com.siperian.sif.message.Field(field.getName().toUpperCase(), field.get(attachment));
			record.setField(column);
		}
		
		final RecordKey recordKey = new RecordKey();
		recordKey.setSystemName(SOURCE_SYSTEM);
		
		final PutRequest request = new PutRequest();
		request.setGenerateSourceKey(true);
		
		request.setRecord(record);
		request.setRecordKey(recordKey);
		
		final PutResponse response = PutResponse.class.cast(sifClient.process(request));
		result = StringUtils.containsIgnoreCase(response.getMessage(), "success");
		
		logger.debug("=== response.getActionType : [{}] ===",response.getActionType());
		logger.debug("=== response.getInteractionId : [{}] ===",response.getInteractionId());
		logger.debug("=== response.getMessage : [{}] ===",response.getMessage());
		RecordKey rKey = response.getRecordKey();
		logger.debug("=== recordKey.getRowid : [{}] ===",rKey.getRowid());
		logger.debug("=== recordKey.getRowidXref : [{}] ===",rKey.getRowidXref());
		logger.debug("=== recordKey.getSourceKey : [{}] ===",rKey.getSourceKey());
		logger.debug("=== recordKey.getSystemName : [{}] ===",rKey.getSystemName());
		logger.debug("=== recordKey.getPeriodEndDate : [{}] ===",rKey.getPeriodEndDate());
		logger.debug("=== recordKey.getPeriodStartDate : [{}] ===",rKey.getPeriodStartDate());
		
		return result;
	}
	
	public boolean insertTxUpdateAttachments(AttachmentsDTO attachment) throws Exception {
		boolean result = false;
		logger.debug("===> jboss.node.name : {}",sifClient.getConfig().getProperty("jboss.node.name"));
//		UserTransaction tx = EJBClient.getUserTransaction(sifClient.getConfig().getProperty("jboss.node.name"));
		UserTransaction tx = EjbSiperianClient.class.cast(sifClient).createTX(100);
		try {
			tx.begin();
			final Record record = new Record();
			record.setSiperianObjectUid("C_ATTACHMENTS");
			
			com.siperian.sif.message.Field column = null;
			for(Field field : attachment.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				column = new com.siperian.sif.message.Field(field.getName().toUpperCase(), field.get(attachment));
				record.setField(column);
			}
			
			final RecordKey recordKey = new RecordKey();
			recordKey.setSystemName(SOURCE_SYSTEM);
			
			final PutRequest request = new PutRequest();
			request.setGenerateSourceKey(true);
			
			request.setRecord(record);
			request.setRecordKey(recordKey);
			
			final PutResponse response = PutResponse.class.cast(sifClient.process(request));
			result = StringUtils.containsIgnoreCase(response.getMessage(), "success");
			
			logger.debug("=== response.getActionType : [{}] ===",response.getActionType());
			logger.debug("=== response.getInteractionId : [{}] ===",response.getInteractionId());
			logger.debug("=== response.getMessage : [{}] ===",response.getMessage());
			RecordKey rKey = response.getRecordKey();
			logger.debug("=== recordKey.getRowid : [{}] ===",rKey.getRowid());
			logger.debug("=== recordKey.getRowidXref : [{}] ===",rKey.getRowidXref());
			logger.debug("=== recordKey.getSourceKey : [{}] ===",rKey.getSourceKey());
			logger.debug("=== recordKey.getSystemName : [{}] ===",rKey.getSystemName());
			logger.debug("=== recordKey.getPeriodEndDate : [{}] ===",rKey.getPeriodEndDate());
			logger.debug("=== recordKey.getPeriodStartDate : [{}] ===",rKey.getPeriodStartDate());
			
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		
		return result;
	}
	
}
