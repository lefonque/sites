package org.epis.ws.provider.service.core;

import java.util.List;

import javax.jws.WebService;

import org.apache.commons.collections.CollectionUtils;
import org.epis.ws.common.entity.BizVO;
import org.epis.ws.common.entity.ConfigurationVO;
import org.epis.ws.common.entity.RecordMap;
import org.epis.ws.common.entity.RecordMapEntry;
import org.epis.ws.common.service.EPISWSGateway;
import org.epis.ws.common.utils.ConstEnum;
import org.epis.ws.manager.core.service.ConfigurationService;
import org.epis.ws.manager.core.service.LogService;
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
	
	@Autowired
	private LogService logService;
	
	
	
	@Override
	public String processPrimitiveData(BizVO bizParam)
			throws Exception {
		String result = ConstEnum.SUCCESS.name();
		String resultFlag = "F";
		String agentId = bizParam.getAgentId(), jobId = bizParam.getJobId();
		try{
			if(bizParam==null || agentId==null || jobId==null){
				throw new IllegalArgumentException("PARAMETER IS NULL!!");
			}
			
			if(CollectionUtils.isEmpty(bizParam.getDataList())){
				logger.warn("===== No Data Found to execute in SERVER =====");
				resultFlag = "S";
				return result;
			}
			else{
				bizService.addData(bizParam);
			}
			resultFlag = "S";
			//TODO Customize Logging
			logger.debug("===== Executed on Server side Successfully =====");
		} finally {
			logService.addLog(bizParam, resultFlag);
		}
		
		
		return result;
	}
	
	@Override
	public ConfigurationVO findConfigurationData(String agentId) throws Exception {
		ConfigurationVO result = service.getConfigurationInfo(agentId);
		return result;
	}

	@Override
	public String debugMethod(List<RecordMap> recordList) throws Exception {
		logger.debug("param's dataList : {}",recordList.size());
		for(RecordMap map : recordList){
			for(RecordMapEntry entry : map.entry){
				logger.debug("=== {} : [({}){}] ==="
						,new Object[]{entry.getKey(),entry.getValue().getClass(),entry.getValue()});
			}
			logger.debug("##########################################");
		}
		return "Success";
	}

}
