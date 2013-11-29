package org.epis.ws.common.service;

import java.util.HashMap;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.epis.ws.common.entity.ConfigurationVO;

@WebService(name="EPISWSGateway"
			,targetNamespace="http://ws.epis.org/provider/EPISWSGateway")
public interface EPISWSGateway {
	
	@WebMethod(operationName="processPrimitiveData"
			,action="http://ws.epis.org/provider/EPISWSGateway/processPrimitiveData")
	public String processPrimitiveData(List<HashMap<String,Object>> collectedDataList) throws Exception;
	
	@WebMethod(operationName="findConfigurationData"
			,action="http://ws.epis.org/provider/EPISWSGateway/findConfigurationData")
	public ConfigurationVO findConfigurationData(String agentId) throws Exception;
}
