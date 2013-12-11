package org.epis.ws.agent.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.cxf.ws.security.SecurityConstants;
import org.epis.ws.agent.security.EPISConsumerUTCallbackHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WebServiceClientService {

	@Autowired
	@Qualifier("agentProp")
	private Properties agentProp;
	
	@Value("#{agentProp['agent.provider.targetnamepsace']}")
	private String targetNamespace;
	
	@Value("#{agentProp['agent.provider.endpoint']}")
	private String wsdlURLString;
	
	@Autowired
	private EPISConsumerUTCallbackHandler callbackHandler;

	public <T> T createClient(
			final String serviceName, final String portName
			,final Class<T> cls)
					throws MalformedURLException {
		
		T gateway = null;
		gateway = createPortType(serviceName, portName, cls);

		// ws-securitypolicy로 설정된 provider측에 맞추어 호출하기 위해
		// security설정을 한다.

		Client client = ClientProxy.getClient(gateway);
		initHttpAttributes(client);
		initSecurityParameters(client);

		return gateway;
	}

	public <T> T createPortType(String serviceNameString, String portNameString, Class<T> cls)
			throws MalformedURLException {
		// webservice call을 위한 준비
		URL wsdlURL = new URL(String.format("%1$s?wsdl", wsdlURLString));
		QName serviceName = new QName(targetNamespace, serviceNameString);
		QName portName = new QName(targetNamespace, portNameString);

		javax.xml.ws.Service service
			= javax.xml.ws.Service.create(wsdlURL, serviceName);
		T portType = service.getPort(portName, cls);
		
		return portType;
	}

	/**
	 * <pre>
	 * <p>보안설정 적용<p>
	 * 
	 * Provider측이 ws-securitypolicy로 보안구성이 되어 있기 때문에,
	 * 이에 맞게 SOAP을 작성하기 위하여 CXF F/W에 보안설정을 한다.
	 * </pre>
	 * 
	 * @param portType
	 */
	private void initSecurityParameters(Client client) {
		Map<String, Object> reqCtx = client.getRequestContext();

		reqCtx.put(SecurityConstants.CALLBACK_HANDLER, callbackHandler);
		reqCtx.put(SecurityConstants.USERNAME,
				agentProp.getProperty("consumer.websvcUser"));
	}

	/**
	 * @param client
	 */
	private void initHttpAttributes(Client client) {
		HTTPConduit httpConduit = HTTPConduit.class.cast(client.getConduit());
		HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
		httpClientPolicy.setAllowChunking(true);
		httpClientPolicy.setConnectionTimeout(NumberUtils.toLong(agentProp
				.getProperty("agent.provider.timeout.connect")));
		httpClientPolicy.setReceiveTimeout(NumberUtils.toLong(agentProp
				.getProperty("agent.provider.timeout.receive")));
		httpClientPolicy.setAllowChunking(true);
		httpConduit.setClient(httpClientPolicy);
	}

	/**
	 * <pre>
	 * <p>에러발생시 SOAP작성</p>
	 * 
	 * 이 메서드는 다음과 같은 상황에서 호출될 수 있다.
	 * <ul><li>MalformedException 발생시</li>
	 * 	<li>Provider에 전송할 데이터를 추출하는 과정에서 Exception 발생시</li>
	 * </ul>
	 * </pre>
	 * 
	 * @param e
	 * @param message
	 * @return
	 */
	public String processSoapFault(Exception e, String message) {
		SOAPFault fault = null;
		try {
			fault = SOAPFactory.newInstance().createFault();
			fault.setFaultString(e.getMessage());
			fault.setFaultCode(new QName(SOAPConstants.URI_NS_SOAP_ENVELOPE,
					"CLIENT"));
			throw new SOAPFaultException(fault);
		} catch (SOAPException e1) {
			throw new RuntimeException(String.format(message, e.toString()));
		}
	}

}
