package org.epis.ws.provider.service.core;

import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.epis.ws.common.entity.BizVO;
import org.epis.ws.common.entity.ConfigurationVO;
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
		logger.info("===== RECEIVED WebService Request [{}:{}] =====",new Object[]{bizParam.getAgentId(),bizParam.getJobId()});
		String result = ConstEnum.SUCCESS.name();
		String resultFlag = "F";
		String agentId = bizParam.getAgentId(), jobId = bizParam.getJobId();
		try{
			if(bizParam==null || agentId==null || jobId==null){
				throw new IllegalArgumentException("PARAMETER IS NULL!!");
			}
			
			if(StringUtils.isEmpty(bizParam.getJsonData())){
				logger.warn("===== No Data Found to execute in SERVER =====");
			}
			else{
				bizService.addData(bizParam);
				logger.info("===== Executed on Server side Successfully =====");
			}
			resultFlag = "S";
		} catch(Exception e) {
			logger.error("##### JSON String : [{}] #####",bizParam.getJsonData());
			logger.error("##### Exception Occurred on Executing processPrimitiveData(BizVO) #####",e);
			throw e;
		} finally {
			logService.writeLog(bizParam, resultFlag);
		}
		
		logger.info("===== REPLY WebService Response =====");
		return result;
	}
	
	@Override
	public ConfigurationVO findConfigurationData(String agentId) throws Exception {
		ConfigurationVO result = service.getConfigurationInfo(agentId);
		return result;
	}

	@Override
	public String debugMethod(String jsonString) throws Exception {
		String result = "";
		if(StringUtils.isEmpty(jsonString)){
			result = "Empty";
			return result;
		}
		
		ObjectMapper mapper = new ObjectMapper();
		List<Map<String,Object>> recordList = mapper.readValue(
				jsonString, new TypeReference<List<Map<String,Object>>>(){});
		logger.debug("param's dataList : {}",recordList.size());
		
		for(Map<String,Object> wrapper : recordList){
			Map<String,Object> map = wrapper;
			Object value = null;Class<?> type = null;
			for(String key : map.keySet()){
				value = map.get(key);
				if(value!=null){
					type = value.getClass();
				}
				else{
					type = Null.class;
				}
				logger.debug("=== {} : [({}){}] ==="
						,new Object[]{key,type,value});
			}
			logger.debug("##########################################");
		}
		return "Success";
	}

}
