package org.epis.ws.agent.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.epis.ws.agent.dao.AgentBizDAO;
import org.epis.ws.agent.util.SqlUtil;
import org.epis.ws.common.entity.MapWrapper;
import org.epis.ws.common.entity.RecordMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class AgentBizService {
	
	@Autowired
	private AgentBizDAO dao;
	
	@Autowired
	@Qualifier("jobProp")
	private Properties jobProp;
	
	@Value("#{jobProp['${job.id}.sqlPre']}")
	private String preSQL;
	
	@Value("#{jobProp['${job.id}.sqlMain']}")
	private String mainSQL;
	
	@Value("#{jobProp['${job.id}.sqlPost']}")
	private String postSQL;
	
	@Autowired
	private SqlUtil sqlUtil;
	
	public AgentBizService(){
		if(StringUtils.isNotEmpty(postSQL)){
			mainSQL = sqlUtil.convertPagableSelectSQL(mainSQL);	// Convert paginational SQL
		}
	}
	
	public int executePreSQL(){
		int result = dao.modify(preSQL);
		return result;
	}
	
	public int executePostSQL(List<Map<String,Object>> rowList, String eflag){
		int result = 0;
		Timestamp edate = new Timestamp(System.currentTimeMillis());
		for(Map<String,Object> one : rowList){
			one.put("EFLAG",eflag);
			one.put("EDATE",edate);
			result += dao.modify(postSQL, one);
		}
		return result;
	}
	
	public List<MapWrapper> executeMainSQL(){
		List<MapWrapper> result = dao.selectList(mainSQL);
		return result;
	}
	
	public List<RecordMap> executeMainSQLAsRecordMap(){
		return dao.selectListAsRecordMap(mainSQL);
	}
}
