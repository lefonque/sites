package org.epis.ws.consumer.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.epis.ws.common.entity.MapWrapper;
import org.epis.ws.common.entity.RecordMap;
import org.epis.ws.common.entity.RecordMapEntry;
import org.epis.ws.consumer.vo.ColumnInfoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedCaseInsensitiveMap;

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
	
	public List<MapWrapper> selectList(String sql){
//		List<Map<String,Object>> result = jdbcTemplate.getJdbcOperations().queryForList(sql);
		List<MapWrapper> result
			= jdbcTemplate.getJdbcOperations().query(sql, new ColumnHashMapRowMapper());
		return result;
	}
	
	public List<RecordMap> selectListAsRecordMap(String sql){
		List<RecordMap> result
			= jdbcTemplate.getJdbcOperations().query(sql, new RecordMapRowMapper());
		return result;
	}
	
	public int modify(String sql){
		int result = jdbcTemplate.getJdbcOperations().update(sql);
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
	
	/**
	 * For Insert, Update, Delete with record Map
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
	
	public int modify(String sql, RecordMap recordMap){
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		for(RecordMapEntry entry : recordMap.entry){
			paramSource.addValue(entry.getKey(), entry.getValue());
		}
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
	
	class RecordMapRowMapper implements RowMapper<RecordMap>{

		public RecordMap mapRow(ResultSet rs, int rowNum) throws SQLException {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			RecordMap result = new RecordMap();
			for (int i = 1; i <= columnCount; i++) {
				String key = getColumnKey(JdbcUtils.lookupColumnName(rsmd, i));
				Object obj = getColumnValue(rs, i);
				RecordMapEntry entry = new RecordMapEntry(key,obj);
				result.entry.add(entry);
			}
			
			return result;
		}
		
		protected String getColumnKey(String columnName) {
			return columnName;
		}
		
		protected Object getColumnValue(ResultSet rs, int index) throws SQLException {
			return JdbcUtils.getResultSetValue(rs, index);
		}
	}
	
	class ColumnHashMapRowMapper implements RowMapper<MapWrapper> {
		public MapWrapper mapRow(ResultSet rs, int rowNum) throws SQLException {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			HashMap<String, Object> mapOfColValues = createColumnMap(columnCount);
			for (int i = 1; i <= columnCount; i++) {
				String key = getColumnKey(JdbcUtils.lookupColumnName(rsmd, i));
				Object obj = getColumnValue(rs, i);
				mapOfColValues.put(key, obj);
			}
			MapWrapper result = new MapWrapper(mapOfColValues);
			return result;
		}
		
		protected HashMap<String, Object> createColumnMap(int columnCount) {
			return new LinkedCaseInsensitiveMap<Object>(columnCount);
		}
		
		protected String getColumnKey(String columnName) {
			return columnName;
		}
		
		protected Object getColumnValue(ResultSet rs, int index) throws SQLException {
			Object result = JdbcUtils.getResultSetValue(rs, index);
			//CXF에서 XML Escape를 하기 때문에 처리상에 문제 발생함.
			//CDATA를 이용하려면 MOXy를 이용하는 방법밖에 없음.
//			if(result instanceof String){
//				result = "<![CDATA[" + result + "]]>";
//			}
			return result;
		}
	
	}
}
