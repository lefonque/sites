package org.epis.ws.manager.core.service;

import java.util.List;
import java.util.Map;

import org.epis.ws.manager.core.dao.InfraDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <pre>
 * <p>내부자원으로부터 데이터를 취득하는 Service</p>
 * 
 * InfraDAO 를 이용하여 내부자원의 데이터를 취득하는 Service
 * </pre>
 * @author developer
 *
 */
@Service
public class InfraService {

	@Autowired
	private InfraDAO dao;
	
	/**
	 * 현재 사용안함
	 * @param keyword
	 * @return
	 */
	public List<Map<String,Object>> getOrgList(String keyword){
		List<Map<String,Object>> result = dao.selectOrgList(keyword);
		return result;
	}
	
	/**
	 * <pre>
	 * <p>로그인 정보를 취득하는 메서드</p>
	 * Agent관리용 Web Application의 로그인 처리시에 사용됨
	 * </pre>
	 * @param userId
	 * @return
	 */
	public Map<String,Object> getLoginUser(String userId){
		Map<String,Object> result = dao.selectUser(userId);
		return result;
	}
}
