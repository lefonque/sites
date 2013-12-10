package org.epis.ws.agent.security;

import java.io.IOException;
import java.util.Properties;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * <p>JAX-WS에서 UsernameToken 사용시에 필요한 CallbackHandler</p>
 * 
 * 웹서비스 통신시 설정할 인증정보 및 메세지 암호화를 위한 대칭키로 사용될
 * Username Token정보를 Setting하는 역할을 한다.
 * </pre>
 * @author developer
 *
 */
@Component("EPISUTCallbackHandler")
public class EPISConsumerUTCallbackHandler implements CallbackHandler {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	@Qualifier("agentProp")
	private Properties agentProp;
	
	
	@Override
	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {
		
		for(Callback callback : callbacks){
			WSPasswordCallback wspc = WSPasswordCallback.class.cast(callback);
			if(agentProp.getProperty("consumer.userId").equals(wspc.getIdentifier())){
				wspc.setPassword(agentProp.getProperty("consumer.pass"));
			}
			logger.debug("Callback Handler processed! ({})",wspc.getIdentifier());
		}
	}

}
