package org.epis.ws.provider.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;
import javax.xml.datatype.XMLGregorianCalendar;

import org.epis.ws.common.entity.MapWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedCaseInsensitiveMap;

@Repository
public class BizDAO {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	@Qualifier("dataSource")
	public void setDataSource(DataSource dataSource){
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public int[] insert(String sql, List<MapWrapper> paramList){
		
		int idx = 0;
		final Timestamp nowTime = new Timestamp(System.currentTimeMillis());
		MapSqlParameterSource[] paramSources = new MapSqlParameterSource[paramList.size()];
		for(MapWrapper wrapper : paramList){
			paramSources[idx] = new MapSqlParameterSource(wrapper.core);
			for(String key : wrapper.core.keySet()){
				logger.debug("[{}]'s type : [{}]",new Object[]{key,wrapper.core.get(key).getClass()});
				if(wrapper.core.get(key) instanceof XMLGregorianCalendar){
					GregorianCalendar cal = XMLGregorianCalendar.class.cast(wrapper.core.get(key)).toGregorianCalendar();
					paramSources[idx].addValue(
							key
							,cal.getTime());
				}
			}
			paramSources[idx].addValue("EDATE", nowTime);
			paramSources[idx].addValue("EFLAG", "S");
			idx++;
		}
		
		logger.debug("SQL : [{}]",sql);
		for(MapSqlParameterSource paramSource : paramSources){
			logger.debug("PARAMETER : [{}]",paramSource.getValues());
		}
		return jdbcTemplate.batchUpdate(sql, paramSources);
	}
	
	
	public List<MapWrapper> select(String sql){
		return jdbcTemplate.getJdbcOperations().query(sql, new ColumnHashMapRowMapper());
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
			return JdbcUtils.getResultSetValue(rs, index);
		}
	
	}
}