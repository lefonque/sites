package org.epis.ws.common.service;

import java.util.HashMap;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.epis.ws.common.entity.AgentVO;

@WebService(name="EPISWSGateway"
			,targetNamespace="http://ws.epis.org/provider/EPISWSGateway")
public interface EPISWSGateway {
	
	@WebMethod(operationName="processPrimitiveData"
			,action="http://ws.epis.org/provider/EPISWSGateway/processPrimitiveData")
	public String processPrimitiveData(HashMap<String,String> paramMap) throws Exception;
	
	@WebMethod(operationName="findConfigurationData"
			,action="http://ws.epis.org/provider/EPISWSGateway/findConfigurationData")
	public AgentVO findConfigurationData(String clientId) throws Exception;
}
