package org.epis.ws.manager.core.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class InfraDAO {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	@Qualifier("sql")
	private Properties sqlRepo;

	@Autowired
	@Qualifier("infraDataSource")
	private void setDataSource(DataSource dataSource){
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
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
	
	public Map<String,Object> selectUser(String userId) {
		Map<String,Object> result = null;
		try{
			String sql = sqlRepo.getProperty("select.infra.user");
			SqlParameterSource paramSource = new MapSqlParameterSource("user_id", userId);
			result = jdbcTemplate.queryForMap(sql, paramSource);
		} catch(EmptyResultDataAccessException e) {
			logger.warn("### Result is Empty ###");
		}
		return result;
	}
}
