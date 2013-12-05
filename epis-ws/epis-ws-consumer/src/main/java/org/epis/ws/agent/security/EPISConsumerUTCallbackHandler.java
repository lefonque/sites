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
