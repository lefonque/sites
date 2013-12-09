package org.epis.ws.manager.core.service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PreDestroy;

import org.epis.ws.common.entity.BizVO;
import org.epis.ws.manager.core.dao.LogDAO;
import org.epis.ws.manager.entity.JQGridVO;
import org.epis.ws.manager.entity.LogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {

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
				addLog(bizVO,resultFlag);
			}
		};
		executor.execute(r);
	}
	
	public int addLog(BizVO bizVO, String resultFlag){
		LogVO param = new LogVO();
		param.setLogId(UUID.randomUUID().toString());
		param.setAgentId(bizVO.getAgentId());
		param.setJobId(bizVO.getJobId());
		param.setResultFlag(resultFlag);
		if(bizVO.getDataList()!=null){
			param.setRowCount(bizVO.getDataList().size());
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
