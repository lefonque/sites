package com.mdm.idduxt.sample;

import java.util.Date;

import org.apache.log4j.Logger;

import com.mdm.idduxt.base.LoggerUtil;
import com.siperian.bdd.userexits.datamodel.BDDObject;
import com.siperian.bdd.userexits.datamodel.CustomizableBDDObject;
import com.siperian.bdd.userexits.datamodel.Message;
import com.siperian.bdd.userexits.operations.AbstractBaseOperationPlugin;
import com.siperian.bdd.userexits.operations.AfterOpenOperationResult;
import com.siperian.bdd.userexits.operations.IOpenOperationPlugin;
import com.siperian.bdd.userexits.operations.OperationResult;
import com.siperian.bdd.userexits.operations.OperationType;
import com.siperian.common.util.StringUtil;

/**
 * 
 * @author P25
 *
 */
public class SampleOpenUserExit extends AbstractBaseOperationPlugin implements IOpenOperationPlugin {

	protected final Logger logger = Logger.getLogger(SampleOpenUserExit.class);
	
	@Override
	public OperationType getOperationType() {
		
		return OperationType.OPEN_OPERATION;
	}

	@Override
	public AfterOpenOperationResult afterOpen(BDDObject bddObj) {
		AfterOpenOperationResult result = null;
		final String objName = bddObj.getObjectName();
		if(!"Person".equals(objName)){
			result = AfterOpenOperationResult.EMPTY;
			return result;
		}
		final String field = "C_PARTY|MIDDLE_NAME";
		result = new AfterOpenOperationResult(bddObj);
		AfterOpenOperationResult.NotificationsHolder notiHolder = result.getPrimaryObjectNotifications();
		String dispName = (String)bddObj.getValue(field);
		String debugMsg = null;
		LoggerUtil.debugLog(logger, "Middle Names's Value : [{}]", dispName);
		for(String column : bddObj.getColumns()){
			LoggerUtil.debugLog(logger, "==>{}", column);
		}
		
		if(StringUtil.isEmpty(dispName)){
			if(bddObj.canSetValue(field, new Date())){
				debugMsg = "It can be set a Date Value to {}";
			}
			else {
				debugMsg = "It can't be set a Date Value to {}";
			}
			LoggerUtil.debugLog(logger, debugMsg, field);
			notiHolder.setColumnMessage(field, Message.info("This is a Info message using setColumnMessage in NotificationsHolder"));
			notiHolder.setColumnMessage("C_PARTY|FIRST_NAME", Message.warning("This is a Warning message using setColumnMessage in NotificationsHolder"));
			notiHolder.setColumnMessage("C_PARTY|LAST_NAME", Message.error("This is a Error message using setColumnMessage in NotificationsHolder"));
			//setColumnMessage사용시 single quotation(')사용할 경우 표시안되는 증상있음 
		}
		notiHolder.addMessage(Message.error("This is a Error Message using 'addMessage' in NotificationsHolder"));
		notiHolder.addMessage(Message.warning("This is a Warning Message using 'addMessage' in NotificationsHolder"));
		notiHolder.addMessage(Message.info("This is a Info Message using 'addMessage' in NotificationsHolder"));
		
		return result;
	}

	@Override
	public OperationResult beforeOpen(CustomizableBDDObject customizedBddObj) {
		
		return OperationResult.OK;
	}


}
