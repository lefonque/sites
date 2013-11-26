package org.epis.ws.provider.service.core;

import java.util.HashMap;

import javax.jws.WebService;

import org.epis.ws.common.entity.AgentVO;
import org.epis.ws.common.service.EPISWSGateway;
import org.springframework.stereotype.Service;

@WebService(
		serviceName="EPISWSGateway", portName="EPISWSGatewayPort"
		,targetNamespace="http://ws.epis.org/provider/EPISWSGateway"
		,endpointInterface="org.epis.ws.common.service.EPISWSGateway")
@Service("EPISWSGateway")
public class EPISConcreteWSGateway implements EPISWSGateway {

	@Override
	public String processPrimitiveData(HashMap<String, String> paramMap)
			throws Exception {
		
		return null;
	}

	@Override
	public AgentVO findConfigurationData(String clientId) throws Exception {
		
		return null;
	}

}
