package org.epis.ws.manager.core.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.epis.ws.common.entity.BizVO;
import org.epis.ws.manager.core.dao.LogDAO;
import org.epis.ws.manager.entity.JQGridVO;
import org.epis.ws.manager.entity.LogVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <pre>
 * <p>로그 정보를 처리하는 Service</p>
 * 
 * LogDAO를 이용하여 로그정보를 처리함
 * 로그기록은 Thread를 이용하여 처리함.
 * </pre>
 * @author developer
 *
 */
@Service
public class LogService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private LogDAO dao;
	
	private ExecutorService executor = Executors.newCachedThreadPool();
	
	@PreDestroy
	private void shutdownExecutor(){
		executor.shutdownNow();
	}
	
	public void writeLog(final BizVO bizVO, final String resultFlag){
		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					addLog(bizVO,resultFlag);
				} catch (Exception e) {
					logger.error("Exception Occurred on the Thread for LogRecord",e);
				}
			}
		};
		executor.execute(r);
	}
	
	public int addLog(BizVO bizVO, String resultFlag) throws JsonParseException, JsonMappingException, IOException{
		LogVO param = new LogVO();
		param.setLogId(UUID.randomUUID().toString());
		param.setAgentId(bizVO.getAgentId());
		param.setJobId(bizVO.getJobId());
		param.setResultFlag(resultFlag);
		if(StringUtils.isNotEmpty(bizVO.getJsonData())){
			ObjectMapper mapper = new ObjectMapper();
			List<Map<String,Object>> list
				= mapper.readValue(bizVO.getJsonData()
						,new TypeReference<List<Map<String,Object>>>(){});
			param.setRowCount(list.size());
		}
		int result = dao.insertLog(param);
		return result;
	}
	
	public List<LogVO> getLogList(JQGridVO paging){
		List<LogVO> result = dao.selectLogList(paging);
		return result;
	}
	
	public int getLogCount(JQGridVO paging){
		int count = dao.selectLogCount(paging);
		return count;
	}
}
