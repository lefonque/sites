package org.epis.ws.provider.security;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.commons.lang3.StringUtils;
import org.apache.ws.security.WSPasswordCallback;
import org.epis.ws.manager.core.service.ConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("EPISProviderUTCallbackHandler")
public class EPISProviderUTCallbackHandler implements CallbackHandler {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ConfigurationService clientService;
	
	@Override
	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {
		
		logger.debug("===== Start Callback Handler =====");
		String username = null;
		String password = null;

		try {
			for(Callback callback : callbacks){
				WSPasswordCallback wspc = WSPasswordCallback.class.cast(callback);
				logger.debug("Callback's Usage : [{}]",wspc.getUsage());
				
				switch(wspc.getUsage()){
				case WSPasswordCallback.DECRYPT:
				case WSPasswordCallback.SIGNATURE:
				case WSPasswordCallback.USERNAME_TOKEN:
					username = wspc.getIdentifier();
					password = clientService.getWebServicePass(username);
					if(password!=null){
						wspc.setPassword(password);
					}
					break;
				}
			}
		} catch (Exception e) {
			logger.error("Exception Occurred", e);
		}


		if(StringUtils.isEmpty(password)){
			logger.error("##### Unknown User Accessed!! #####");
			throw new IOException();
		}
		
		logger.debug("===== END Callback Handler =====\n");
	}

	
}
