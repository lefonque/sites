package org.epis.ws.provider.dao.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class InfraDAO {

//	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	@Qualifier("sql")
	private Properties sqlRepo;

	@Autowired
	@Qualifier("dataSource")
	private void setDataSource(DataSource dataSource){
//		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public List<Map<String,Object>> selectOrgList(String keyword){
		List<Map<String,Object>> result = null;
//		String sql = sqlRepo.getProperty("");
//		SqlParameterSource paramSource = new MapSqlParameterSource("", keyword);
//		result = jdbcTemplate.queryForList(sql, paramSource);
		
		//For Testing
		result = new ArrayList<Map<String,Object>>();
//		Map<String,Object> one = new HashMap<String,Object>();
//		one.put("orgCode","ORG-1");
//		one.put("orgName","유통공사");
//		result.add(one);
//		
//		one = new HashMap<String,Object>();
//		one.put("orgCode","ORG-2");
//		one.put("orgName","농협");
//		result.add(one);
		
		return result;
	}
	
	public Map<String,Object> selectUser(String userId){
		Map<String,Object> result = null;
//		String sql = sqlRepo.getProperty("");
//		SqlParameterSource paramSource = new MapSqlParameterSource("", userId);
//		result = jdbcTemplate.queryForMap(sql, paramSource);
		
		//For Testing
		result = new HashMap<String,Object>();
		result.put("user_id","jack");
		result.put("password","jackass");
		
		return result;
	}
}
