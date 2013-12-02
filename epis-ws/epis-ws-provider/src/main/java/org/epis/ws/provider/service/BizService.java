package org.epis.ws.provider.service;

import java.util.List;

import org.epis.ws.common.entity.BizVO;
import org.epis.ws.common.entity.JobVO;
import org.epis.ws.common.entity.MapWrapper;
import org.epis.ws.provider.dao.BizDAO;
import org.epis.ws.provider.dao.core.ConfigurationDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BizService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ConfigurationDAO configDao;
	
	@Autowired
	private BizDAO dao;
	
	public int addData(BizVO bizVO){
		
		String agentId = bizVO.getAgentId(), jobId = bizVO.getJobId();
		JobVO jobInfo = configDao.selectJobInfo(agentId,jobId);
		String sql = jobInfo.getServerSql();

		List<MapWrapper> wrapperList = bizVO.getDataList();
		int[] resultCount = dao.insert(sql, wrapperList);
		int result = 0;
		for(int i : resultCount){
			result += i;
		}
		logger.debug("Inserted Record : [{}]",result);
		return result;
	}
	
	public List<MapWrapper> getData(String sql){
		
		return dao.select(sql);
	}
}
