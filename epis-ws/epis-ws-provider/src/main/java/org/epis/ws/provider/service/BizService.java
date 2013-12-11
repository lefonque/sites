package org.epis.ws.provider.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.epis.ws.common.entity.BizVO;
import org.epis.ws.common.entity.JobVO;
import org.epis.ws.manager.core.dao.ConfigurationDAO;
import org.epis.ws.provider.dao.BizDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BizService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ConfigurationDAO configDao;
	
	@Autowired
	private BizDAO dao;
	
	@Transactional(value="bizTransactionManager", rollbackFor=Throwable.class)
	public int addData(BizVO bizVO) throws Exception {
		
		String agentId = bizVO.getAgentId(), jobId = bizVO.getJobId();
		JobVO jobInfo = configDao.selectJobInfo(agentId,jobId);
		String sql = jobInfo.getServerSql();

//		List<MapWrapper> wrapperList = bizVO.getDataList();
		int result = 0;
		List<Map<String,Object>> list = null;
		if(StringUtils.isNotEmpty(bizVO.getJsonData())){
			ObjectMapper mapper = new ObjectMapper();
			list = mapper.readValue(bizVO.getJsonData(), new TypeReference<List<Map<String,Object>>>() {});
			int[] resultCount = dao.insert(sql, list);
			if(resultCount!=null){
				result = resultCount.length;
			}
		}
		
		logger.debug("Inserted Record : [{}]",result);
		return result;
	}
	
}
