package org.epis.ws.manager.core.service;

import java.util.List;

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
	
	public int addLog(BizVO bizVO, String resultFlag){
		LogVO param = new LogVO();
		param.setAgentId(bizVO.getAgentId());
		param.setJobId(bizVO.getJobId());
		param.setResultFlag(resultFlag);
		int result = dao.insertLog(param);
		return result;
	}
	
	public List<LogVO> getLogList(JQGridVO paging){
		List<LogVO> result = dao.selectLogList(paging);
		return result;
	}
}
