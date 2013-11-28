package org.epis.ws.consumer.dao;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.epis.ws.consumer.vo.ColumnInfoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

/**
 * agent.properties에 설정된 SQL문을 실행하는 DAO
 * 
 * @author developer
 *
 */
@Repository
public class AgentBizDAO {

	private final Logger logger = LoggerFactory.getLogger(AgentBizDAO.class);
	
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	@Qualifier("dataSource")
	public void setDataSource(DataSource dataSource){
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public Map<String,Object> selectInfo(String sql, Object... params){
		Map<String,Object> result = jdbcTemplate.getJdbcOperations().queryForMap(sql, params);
		return result;
	}
	
	public Map<String,Object> selectInfo(String sql, List<ColumnInfoVO> params){
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		
		for(ColumnInfoVO colInfo : params){
			paramSource.addValue(colInfo.getFieldName(),colInfo.getValue(), colInfo.getType());
		}
		Map<String,Object> result = jdbcTemplate.queryForMap(sql, paramSource);
		return result;
	}
	
	
	public List<Map<String,Object>> selectList(String sql){
		List<Map<String,Object>> result = jdbcTemplate.getJdbcOperations().queryForList(sql);
		return result;
	}
	
	public List<Map<String,Object>> selectList(String sql, Object... params){
		List<Map<String,Object>> result = jdbcTemplate.getJdbcOperations().queryForList(sql, params);
		return result;
	}
	
	public List<Map<String,Object>> selectList(String sql, List<ColumnInfoVO> params){
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		for(ColumnInfoVO colInfo : params){
			paramSource.addValue(colInfo.getFieldName(), colInfo.getValue(), colInfo.getType());
		}
		
		List<Map<String,Object>> result = jdbcTemplate.queryForList(sql, paramSource);
		return result;
	}
	
	
	
	/**
	 * For Insert, Update, Delete
	 * @param sql
	 * @param params
	 * @return
	 */
	public int modify(String sql, Object... params){
		int result = jdbcTemplate.getJdbcOperations().update(sql, params);
		return result;
	}
	
	public int modify(String sql, Map<String,Object> record){
		SqlParameterSource paramSource = new MapSqlParameterSource(record);
		logger.debug("SQL : [{}]",sql);
		logger.debug("Parameters : [{}]",paramSource);
		int result = jdbcTemplate.update(sql, paramSource);
		return result;
	}
	
	public int modify(String sql, List<ColumnInfoVO> params){
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		for(ColumnInfoVO colInfo : params){
			paramSource.addValue(colInfo.getFieldName(), colInfo.getValue(), colInfo.getType());
		}
		
		int result = jdbcTemplate.update(sql, paramSource);
		return result;
	}
	
	
}
