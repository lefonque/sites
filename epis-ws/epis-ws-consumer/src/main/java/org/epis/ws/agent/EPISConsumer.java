package org.epis.ws.agent;

import java.lang.reflect.Method;
import java.net.MalformedURLException;

import org.epis.ws.agent.service.EPISConsumerService;
import org.epis.ws.agent.service.WebServiceClientService;
import org.epis.ws.agent.util.PropertyEnum;
import org.epis.ws.common.service.EPISWSGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * <pre>
 * <p>Agent가 실행되는 Entry Point</p>
 * 
 * WebService를 위한 Service 인스턴스를 생성하여,
 * 필요한 처리를 수행함.
 * </pre>
 * @author developer
 *
 */
public class EPISConsumer {

	private final Logger logger = LoggerFactory.getLogger(getClass());


	private static EPISConsumer consumer;
	
	private final ApplicationContext ctx;
	
	private final WebServiceClientService clientService;
	
	private final EPISConsumerService service;

	/**
	 * For Singleton
	 */
	private EPISConsumer() {
		ctx = new ClassPathXmlApplicationContext(
				"classpath:/META-INF/spring/context-*.xml");
		service = ctx.getBean(EPISConsumerService.class);
		clientService = ctx.getBean(WebServiceClientService.class);
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
		
		String svcNmString = "EPISWSGateway";
		String portNmString = "EPISWSGatewayPort";
		logger.info("===== Begin Job Execution =====");
		try {
			EPISWSGateway gateway
				= clientService.createClient(
					svcNmString,portNmString,EPISWSGateway.class);
			
			String methodName = "execute" + command;
			Method method = service.getClass().getDeclaredMethod(methodName, EPISWSGateway.class);
			Object result = method.invoke(service, gateway);
			String msg = "Successfully";
			if(Boolean.class.cast(result)){
				msg = "with Error";
			}
			logger.info("===== Executed Schedule Job {} =====",msg);
		} catch (MalformedURLException e) {
			logger.error("##### MalformedURLException Occurred on start() #####", e);
		} catch (Exception e){
			logger.error("##### Exception Occurred on start() #####", e);
		}
		logger.info("===== Finish Schedule Job =====");
	}

	

}
