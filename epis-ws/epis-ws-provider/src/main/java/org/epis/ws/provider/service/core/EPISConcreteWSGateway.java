package org.epis.ws.provider.service.core;

import javax.jws.WebService;

import org.apache.commons.collections.CollectionUtils;
import org.epis.ws.common.entity.BizVO;
import org.epis.ws.common.entity.ConfigurationVO;
import org.epis.ws.common.service.EPISWSGateway;
import org.epis.ws.common.utils.ConstEnum;
import org.epis.ws.provider.service.BizService;
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
	
	@Autowired
	private BizService bizService;
	
	@Override
	public String processPrimitiveData(BizVO bizParam)
			throws Exception {
		String result = ConstEnum.SUCCESS.name();
		
		String agentId = bizParam.getAgentId(), jobId = bizParam.getJobId();
		if(bizParam==null || agentId==null || jobId==null){
			throw new IllegalArgumentException("PARAMETER IS NULL!!");
		}
		
		if(CollectionUtils.isEmpty(bizParam.getDataList())){
			logger.warn("===== No Data Found to execute in SERVER =====");
			return result;
		}
		bizService.addData(bizParam);
		
		//TODO Customize Logging
		logger.debug("===== Executed on Server side Successfully =====");
		return result;
	}
	
	@Override
	public ConfigurationVO findConfigurationData(String agentId) throws Exception {
		ConfigurationVO result = service.getConfigurationInfo(agentId);
		return result;
	}

	@Override
	public String debugMethod(BizVO param) throws Exception {
		logger.debug("param's dataList : {}",param.getDataList());
		return "Success";
	}

}
