package org.epis.ws.provider.service.core;

import java.util.List;
import java.util.Map;

import org.epis.ws.provider.dao.core.InfraDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InfraService {

	@Autowired
	private InfraDAO dao;
	
	public List<Map<String,Object>> getOrgList(String keyword){
		List<Map<String,Object>> result = dao.selectOrgList(keyword);
		return result;
	}
	
	public Map<String,Object> getUser(String userId){
		Map<String,Object> result = dao.selectUser(userId);
		return result;
	}
}
