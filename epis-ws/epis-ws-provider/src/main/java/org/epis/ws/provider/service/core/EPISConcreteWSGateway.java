package org.epis.ws.provider.service.core;

import java.util.HashMap;

import javax.jws.WebService;

import org.epis.ws.common.entity.ConfigurationVO;
import org.epis.ws.common.service.EPISWSGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@WebService(
		serviceName="EPISWSGateway", portName="EPISWSGatewayPort"
		,targetNamespace="http://ws.epis.org/provider/EPISWSGateway"
		,endpointInterface="org.epis.ws.common.service.EPISWSGateway")
@Service("EPISWSGateway")
public class EPISConcreteWSGateway implements EPISWSGateway {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ConfigurationService service;
	
	@Override
	public String processPrimitiveData(HashMap<String, String> paramMap)
			throws Exception {
		logger.debug("===== Parameter from Agent : {} =====",paramMap);
		return "Success";
	}
	
	@Override
	public ConfigurationVO findConfigurationData(String agentId) throws Exception {
		ConfigurationVO result = service.getConfigurationInfo(agentId);
		return result;
	}
}
