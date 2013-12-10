package org.epis.ws.common.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.epis.ws.common.entity.BizVO;
import org.epis.ws.common.entity.ConfigurationVO;
import org.epis.ws.common.entity.MapWrapper;

/**
 * 웹서비스 인터페이스
 * @author developer
 *
 */
@WebService(name="EPISWSGateway"
			,targetNamespace="http://ws.epis.org/provider/EPISWSGateway")
public interface EPISWSGateway {
	
	@WebMethod(operationName="processPrimitiveData"
			,action="http://ws.epis.org/provider/EPISWSGateway/processPrimitiveData")
	public String processPrimitiveData(@WebParam(name="bizParam") BizVO bizParam) throws Exception;
	
	@WebMethod(operationName="findConfigurationData"
			,action="http://ws.epis.org/provider/EPISWSGateway/findConfigurationData")
	public ConfigurationVO findConfigurationData(@WebParam(name="agentId") String agentId) throws Exception;
	
	@WebMethod(operationName="debugMethod"
			,action="http://ws.epis.org/provider/EPISWSGateway/debugMethod")
	public String debugMethod(@WebParam(name="param") List<MapWrapper> param) throws Exception;
	
}
