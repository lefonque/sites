package org.epis.ws.manager.util;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.lang3.StringUtils;
import org.epis.ws.manager.entity.JDBConnectionVO;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * <p>Job ID에 따라 Connection를 생성하는 DataSource</p>
 * 
 * Job ID에 따라 Connection을 생성하나, 키값을 Job ID로 하는 것은 비효율 적임.
 * (Job ID가 무한히 늘어남에 따라 DataSource도 무한히 늘어날 것이므로)
 * 
 * Job설정의 양을 감안하여 본 DataSource를 사용하게 될 경우,
 * DB접속정보를 JOB에서 분리하여 별도의 entity로 관리하고, DB접속정보의 키를
 * Job ID와 관계를 맺어 Map의 키를 DB접속정보의 키로 사용하는 방향으로 접근할 것.
 * (JDBConnectionVO 클래스 이용)
 * 
 * AbstractRoutingDataSource를 사용할 경우, performance이슈가 있다고 함.
 * 따라서 본 클래스의 사용상의 부하가 있을 경우, 필요에 따라 DataSoure를
 * bean정의 xml에 선언하는 쪽으로 정책을 정하는 편이 안전함.
 * 
 * 현재 본 클래스는 사용안함.
 * </pre>
 * @see org.epis.ws.manager.util.DBKeyContextHolder
 * @author developer
 *
 */
@Component("bizDataSource")
public class DynamicDataSource extends AbstractRoutingDataSource {

	private final ConcurrentHashMap<String,DataSource> dataSourceMap = new ConcurrentHashMap<String, DataSource>();
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource#determineCurrentLookupKey()
	 */
	@Override
	protected JDBConnectionVO determineCurrentLookupKey() {
		return DBKeyContextHolder.get();
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		// do not
	}
	
	/**
	 * <pre>
	 * <p>DataSource를 리턴한다.</p>
	 * 
	 * Map에서 Connection ID값으로 DataSource를 조회하여,
	 * 없으면 새로 DataSource를 작성해서 Map에 보관함.
	 * </pre>
	 * @see org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource#determineTargetDataSource()
	 */
	@Override
	protected DataSource determineTargetDataSource() {
		JDBConnectionVO jdbcInfo = determineCurrentLookupKey();
		if(jdbcInfo == null){
			throw new DynamicDataSourceException("DataSource Key isn't exist!!");
		}
		
		String key = jdbcInfo.getConnectionId();
		DataSource result = dataSourceMap.get(key);
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
	
	/**
	 * <pre>
	 * <p>인자로 받은 JDBC 접속정보로 DataSource를 생성한다.</p>
	 * 
	 * DB Type에 따라 해당 DataSource 생성 메서드를 reflection으로 invoke함.
	 * </pre>
	 * @param jdbcInfo	DataSource 생성을 위한 접속정보 Map
	 * @return
	 * @throws Exception
	 */
	protected DataSource createDataSource(JDBConnectionVO jdbcInfo) throws Exception {
		StringBuilder methodMidName = new StringBuilder();
		methodMidName.append("create").append(jdbcInfo.getDbType())
			.append("DataSource");
		
		DataSource result = null;
		try {
			Object returnValue = MethodUtils.invokeExactMethod(this, methodMidName.toString(), jdbcInfo);
			if(returnValue!=null){
				result = DataSource.class.cast(returnValue);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * <pre>
	 * <p>범용 DataSource를 작성한다.</p>
	 * 
	 * commons-dbcp쪽 BasicDataSource 으로 data source를 작성한다.
	 * </pre>
	 * @param jdbcInfo
	 * @return
	 * @throws Exception
	 */
	protected DataSource createGenericDataSource(JDBConnectionVO jdbcInfo) throws Exception{
		Properties jdbcProperties = new Properties();
		for(Field f : jdbcInfo.getClass().getDeclaredFields()){
			if(StringUtils.startsWith(f.getName(), "jdbc")){
				jdbcProperties.put(
						Introspector.decapitalize(f.getName().substring(4))
						,String.valueOf(f.get(jdbcInfo)));
			}
		}
		
		DataSource result = BasicDataSourceFactory.createDataSource(jdbcProperties);
		return result;
	}
	
}
