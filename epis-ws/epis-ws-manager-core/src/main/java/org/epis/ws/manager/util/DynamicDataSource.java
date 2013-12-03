package org.epis.ws.manager.util;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.epis.ws.common.utils.DBKeyContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

@Component("bizDataSource")
public class DynamicDataSource extends AbstractRoutingDataSource {

	private final Map<String,DataSource> dataSourceMap = new ConcurrentHashMap<String, DataSource>();
	
	@Override
	protected Map<String,Object> determineCurrentLookupKey() {
		return DBKeyContextHolder.get();
	}
	
	@Override
	public void afterPropertiesSet() {
		// do not
	}
	
	@Override
	protected DataSource determineTargetDataSource() {
		Map<String,Object> jdbcInfo = determineCurrentLookupKey();
		if(MapUtils.isEmpty(jdbcInfo)){
			throw new DynamicDataSourceException("DataSource Key isn't exist!!");
		}
		
		String key = jdbcInfo.get("job_id").toString();
		DataSource result = dataSourceMap.get(key);
//		if(!dataSourceMap.containsKey(key)){
		if(result==null) {
			try {
				result = createDataSource(jdbcInfo);
			} catch (Exception e) {
				throw new DynamicDataSourceException("FAIL TO CREATE DATA SOURCE", e);
			}
			dataSourceMap.put(key, result);
		}
		return result;
	}
	
	protected DataSource createDataSource(Map<String,Object> jdbcInfo) throws Exception{
		Properties jdbcProperties = new Properties();
		jdbcProperties.putAll(jdbcInfo);
		DataSource result = BasicDataSourceFactory.createDataSource(jdbcProperties);
		return result;
	}
}
