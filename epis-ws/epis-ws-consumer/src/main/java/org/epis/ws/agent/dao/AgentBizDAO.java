package org.epis.ws.agent.dao;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.epis.ws.common.entity.MapWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

/**
 * job.properties에 설정된 SQL문을 실행하는 DAO
 * 
 * @author developer
 *
 */
@Repository
public class AgentBizDAO {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	@Qualifier("dataSource")
	public void setDataSource(DataSource dataSource){
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	/**
	 * Main SQL을 실행할 때 사용되는 메서드
	 * @param sql	Main SQL
	 * @return
	 */
	public List<MapWrapper> selectListAsMap(String sql){
//		List<Map<String,Object>> result = jdbcTemplate.getJdbcOperations().queryForList(sql);
		List<MapWrapper> result
			= jdbcTemplate.getJdbcOperations().query(sql, new MapWrapperRowMapper());
		return result;
	}
	
	public List<Map<String,Object>> selectList(String sql){
		List<Map<String,Object>> result
			= jdbcTemplate.getJdbcOperations().queryForList(sql);
		return result;
	}
	
	/**
	 * 인자가 없는 Insert/Update/Delete SQL를 처리함.
	 * @param sql
	 * @return
	 */
	public int modify(String sql){
		int result = jdbcTemplate.getJdbcOperations().update(sql);
		return result;
	}
	
	/**
	 * <pre>
	 * 건단위로 처리할 경우 사용됨.
	 * 현재 사용안함.
	 * </pre>
	 * @param sql
	 * @param record
	 * @return
	 */
	public int modify(String sql, Map<String,Object> record){
		SqlParameterSource paramSource = new MapSqlParameterSource(record);
		logger.trace("SQL : [{}]",sql);
		logger.trace("Parameters : [{}]",paramSource);
		int result = jdbcTemplate.update(sql, paramSource);
		return result;
	}
	
	/**
	 * <pre>
	 * <p>한 묶음으로 Update를 처리함.</p>
	 * 
	 * PreSQL, PostSQL처리시 사용됨
	 * </pre>
	 * @param sql	
	 * @param batchArgs
	 * @return
	 */
	public int[] batchModify(String sql, SqlParameterSource[] batchArgs){
		int[] result = jdbcTemplate.batchUpdate(sql, batchArgs);
		return result;
	}
}
