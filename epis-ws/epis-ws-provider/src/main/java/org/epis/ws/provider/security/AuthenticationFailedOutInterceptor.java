package org.epis.ws.provider.security;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.xml.soap.SOAPMessage;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.binding.soap.saaj.SAAJInInterceptor;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.apache.ws.security.util.WSSecurityUtil;
import org.opensaml.ws.wssecurity.WSSecurityConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

@Component("AuthenticationFailedOutInterceptor")
public class AuthenticationFailedOutInterceptor extends AbstractSoapInterceptor {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public AuthenticationFailedOutInterceptor(){
		super(Phase.SETUP);
	}
	
	@Override
	public void handleMessage(SoapMessage message) throws Fault {
		logger.debug("==== I got it!! ====");
		Exception exception = message.getContent(Exception.class);
		Fault fault = Fault.class.cast(exception);
		if(fault.getCause() instanceof WSSecurityException){
			logger.error("##### Fault Code : [{}] #####",fault.getCode());
			
			
			Exchange exchange = PhaseInterceptorChain.getCurrentMessage().getExchange();
			Message oldMsg = exchange.getInMessage();
			String remoteAddr = ServletRequest.class.cast(oldMsg.get(AbstractHTTPDestination.HTTP_REQUEST)).getRemoteAddr();
			logger.error("### Remote Address : [{}] ###",remoteAddr);
			
			
			SAAJInInterceptor.INSTANCE.handleMessage(SoapMessage.class.cast(oldMsg));
			SOAPMessage oldSoapMsg = oldMsg.getContent(SOAPMessage.class);
			String actor = (String)oldMsg.getContextualProperty(WSHandlerConstants.ACTOR);
			try {
				Element elem = WSSecurityUtil.getSecurityHeader(oldSoapMsg.getSOAPPart(), actor);
				List<Element> nodeList = DOMUtils.getChildrenWithName(elem, WSSecurityConstants.WSSE_NS, WSHandlerConstants.USERNAME_TOKEN);
				Node node = nodeList.get(0);
				String prefix = node.getPrefix();
				logger.debug("Node Name : {}", Element.class.cast(node).getNodeName());
				
				String username = DOMUtils.getChildContent(node, prefix + ":" + WSConstants.USERNAME_LN);
				logger.debug("Username : {}", username);
				
			} catch (WSSecurityException e) {
				logger.error("Exception : {}",e.toString());
			}
			logger.debug("==== Received Message : {}",oldMsg);
		}
	}

}
