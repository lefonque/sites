package org.epis.ws.agent;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import javax.security.auth.callback.CallbackHandler;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.security.SecurityConstants;
import org.epis.ws.agent.security.EPISConsumerUTCallbackHandler;
import org.epis.ws.agent.service.EPISConsumerService;
import org.epis.ws.agent.util.PropertyEnum;
import org.epis.ws.common.service.EPISWSGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class EPISConsumer {

	private final Logger logger = LoggerFactory.getLogger(getClass());


	private static EPISConsumer consumer;
	
	private final ApplicationContext ctx;
	private Properties agentProp;

	/**
	 * For Singleton
	 */
	private EPISConsumer() {
		ctx = new ClassPathXmlApplicationContext(
				"classpath:/META-INF/spring/context-*.xml");
		agentProp = ctx.getBean("agentProp",Properties.class);
	}

	/**
	 * For Singleton
	 * @return EPISConsumer's instance
	 */
	public static final EPISConsumer getInstance() {
		synchronized (EPISConsumer.class) {
			if (consumer == null) {
				consumer = new EPISConsumer();
			}
		}
		return consumer;
	}

	public static void main(String[] args) {
		if(args==null || args.length < 1){
			System.err.println("Usage : java -Dconsumer.root.dir=[pwd of jar file] -Djob.id=[Job Name] -jar epis-ws-consumer.jar [Config or Biz]");
			System.err.println("e.g.) java -Dconsumer.root.dir=/opt/agent -Djob.id=job1 -jar epis-ws-consumer.jar Biz");
			System.exit(-1);
		}
		if( !("Config".equals(args[0]) || "Biz".equals(args[0]) || "Debug".equals(args[0])) ){
			System.err.println("Usage : java -Dconsumer.root.dir=[pwd of jar file] -Djob.id=[Job Name] -jar epis-ws-consumer.jar [Config or Biz]");
			System.err.println("e.g.) java -Dconsumer.root.dir=/opt/agent -Djob.id=job1 -jar epis-ws-consumer.jar Biz");
			System.exit(-1);
		}
		if(!System.getProperties().containsKey(PropertyEnum.SYS_JOB_NAME.getKey())
				|| !System.getProperties().containsKey(PropertyEnum.SYS_ROOT_DIR.getKey())){
			System.err.println("Usage : java -Dconsumer.root.dir=[pwd of jar file] -Djob.id=[Job Name] -jar epis-ws-consumer.jar [Config or Biz]");
			System.err.println("e.g.) java -Dconsumer.root.dir=/opt/agent -Djob.id=job1 -jar epis-ws-consumer.jar Biz");
			System.exit(-1);
		}
		
		EPISConsumer consumer = EPISConsumer.getInstance();
		consumer.start(args[0]);
		
		
	}
	

	/**
	 * webservice call
	 */
	public void start(String command) {
		
		String targetNamespace = agentProp
				.getProperty("agent.provider.targetnamepsace");
		String wsdlURLString = agentProp.getProperty("agent.provider.endpoint");
		String svcNmString = "EPISWSGateway";
		String portNmString = "EPISWSGatewayPort";
		logger.info("===== Begin Job Execution =====");
		try {
			EPISWSGateway gateway
				= createPortType(targetNamespace,wsdlURLString
						,svcNmString,portNmString,EPISWSGateway.class);
			
			//ws-securitypolicy로 설정된 provider측에 맞추어 호출하기 위해
			//security설정을 한다.
			initSecurityParameters(gateway);
			
			String methodName = "execute" + command;
			EPISConsumerService service = ctx.getBean(EPISConsumerService.class);
			Method method = service.getClass().getDeclaredMethod(methodName, EPISWSGateway.class);
			Object result = method.invoke(service, gateway);
			String msg = "Successfully";
			if(Boolean.class.cast(result)){
				msg = " with Error";
			}
			logger.info("===== Executed Schedule Job {} =====",msg);
		} catch (MalformedURLException e) {
			logger.error("##### MalformedURLException Occurred on start() #####", e);
		} catch (Exception e){
			logger.error("##### Exception Occurred on start() #####", e);
		}
		logger.info("===== Finish Schedule Job =====");
	}

	

	public <T>T createPortType(
			String targetNamespace, String wsdlURLString
			,String serviceNameString, String portNameString, Class<T> cls) throws MalformedURLException{
		//webservice call을 위한 준비
		URL wsdlURL = new URL(String.format("%1$s?wsdl", wsdlURLString));
		QName serviceName = new QName(targetNamespace, serviceNameString);
		QName portName = new QName(targetNamespace, portNameString);
		
		javax.xml.ws.Service service = javax.xml.ws.Service.create(wsdlURL, serviceName);
		T portType = service.getPort(portName, cls);
		return portType;
	}

	
	public void garage(){
		EPISWSGateway way = ctx.getBean("consumer",EPISWSGateway.class);
		try {
			way.processPrimitiveData(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	/**
	 * <pre>
	 * <p>보안설정 적용<p>
	 * 
	 * Provider측이 ws-securitypolicy로 보안구성이 되어 있기 때문에,
	 * 이에 맞게 SOAP을 작성하기 위하여 CXF F/W에 보안설정을 한다.
	 * </pre>
	 * @param portType
	 */
	private void initSecurityParameters(EPISWSGateway portType) {
		Client client = ClientProxy.getClient(portType);
		Map<String, Object> reqCtx = client.getRequestContext();
	
		CallbackHandler callbackHandler = ctx.getBean("EPISUTCallbackHandler",
				EPISConsumerUTCallbackHandler.class);
		reqCtx.put(SecurityConstants.CALLBACK_HANDLER, callbackHandler);
		reqCtx.put(SecurityConstants.USERNAME,
				agentProp.getProperty("consumer.userId"));
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
