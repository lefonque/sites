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
import org.epis.ws.common.entity.RecordMap;
import org.epis.ws.common.entity.RecordMapEntry;
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
	@Qualifier("bizDataSource")
	public void setDataSource(DataSource dataSource){
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public int[] insert2(String sql, List<RecordMap> paramList){
		
		int idx = 0;
		final Timestamp nowTime = new Timestamp(System.currentTimeMillis());
		MapSqlParameterSource[] paramSources = new MapSqlParameterSource[paramList.size()];
		Object value = null;
		for(RecordMap map : paramList){
			paramSources[idx] = new MapSqlParameterSource();
			for(RecordMapEntry entry : map.entry){
				value = entry.getValue();
				if(value instanceof XMLGregorianCalendar){
					GregorianCalendar cal = XMLGregorianCalendar.class.cast(value).toGregorianCalendar();
					value = cal.getTime();
				}
				paramSources[idx].addValue(entry.getKey() ,value);
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
	
	public int[] insert(String sql, List<MapWrapper> paramList){
		
		int idx = 0;
		final Timestamp nowTime = new Timestamp(System.currentTimeMillis());
		MapSqlParameterSource[] paramSources = new MapSqlParameterSource[paramList.size()];
		Object value = null;
		for(MapWrapper wrapper : paramList){
			paramSources[idx] = new MapSqlParameterSource(wrapper.core);
			for(String key : wrapper.core.keySet()){
				value = wrapper.core.get(key);
				if(value!=null){
					logger.debug("[{}]'s type : [{}]",new Object[]{key,value.getClass()});
				}
				if(value instanceof XMLGregorianCalendar){
					GregorianCalendar cal
						= XMLGregorianCalendar.class.cast(value)
							.toGregorianCalendar();
					value = cal.getTime();
				}
				paramSources[idx].addValue(key,value);
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
