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

/**
 * <pre>
 * <p>내부자원에의 데이터를 취득하는 DAO</p>
 * 
 * Agent관리용 웹어플리케이션 사용시 내부 자원(사용자정보, 조직정보)
 * 를 취득하는 메서드.
 * (내부 자원은 타 어플리케이션에서 구성한 스키마의 구성 Entity를 뜻함.)
 * 
 * 현재 Agent관리용 웹어플리케이션에 소속된 DB Schema와, 내부 자원은
 * 동일한 DB Schema를 사용하고 있으므로, "dataSource" bean을
 * 이용하고 있으나 DB혹은 DB스키마가 다를 경우, DataSource구성 및 
 * setDataSource부분의 annotation에서 Qualifier값 변경이 필요함.
 * 
 * 현재 조직정보는 사용하지 않음.
 * </pre>
 * @author developer
 *
 */
@Repository
public class InfraDAO {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	@Qualifier("sql")
	private Properties sqlRepo;

	@Autowired
	@Qualifier("dataSource")
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
	
	/**
	 * 사용자 ID에 해당하는 사용자정보를 취득한다.
	 * @param userId
	 * @return
	 */
	public Map<String,Object> selectUser(String userId) {
		Map<String,Object> result = null;
		try{
			String sql = sqlRepo.getProperty("select.infra.user");
			SqlParameterSource paramSource = new MapSqlParameterSource("USER_ID", userId);
			result = jdbcTemplate.queryForMap(sql, paramSource);
		} catch(EmptyResultDataAccessException e) {
			logger.warn("### Result is Empty ###");
		}
		return result;
	}
}
