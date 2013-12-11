package org.epis.ws.provider.dao;

import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.xml.datatype.XMLGregorianCalendar;

import org.epis.ws.common.entity.MapWrapper;
import org.epis.ws.common.utils.EAIColumnEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BizDAO {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	@Qualifier("bizDataSource")
	public void setDataSource(DataSource dataSource){
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
public int[] insert(String sql, List<Map<String,Object>> paramList) throws Exception {
		
		int idx = 0;
		final Timestamp nowTime = new Timestamp(System.currentTimeMillis());
		MapSqlParameterSource[] paramSources = new MapSqlParameterSource[paramList.size()];
		Object value = null;
		for(Map<String,Object> wrapper : paramList){
			wrapper.put(EAIColumnEnum.RECV_EDATE.getColumnName(), nowTime);
			if(wrapper.size()!=13 && wrapper.size()!=23){
				logger.debug("Map Element Size[{}] : [{}]",new Object[]{wrapper.size(),wrapper});
			}
			for(String key : wrapper.keySet()){
				
				value = wrapper.get(key);
				if(value instanceof XMLGregorianCalendar){
					GregorianCalendar cal
						= XMLGregorianCalendar.class.cast(value)
							.toGregorianCalendar();
					value = cal.getTime();
					wrapper.put(key,value);
				}
			}
			paramSources[idx] = new MapSqlParameterSource(wrapper);
			
			idx++;
		}
		
		
		
		int[] result = jdbcTemplate.batchUpdate(sql, paramSources);
		return result;
	}
	
	public int[] insert2(String sql, List<MapWrapper> paramList) throws Exception {
		
		int idx = 0;
		final Timestamp nowTime = new Timestamp(System.currentTimeMillis());
		MapSqlParameterSource[] paramSources = new MapSqlParameterSource[paramList.size()];
		Object value = null;
		for(MapWrapper wrapper : paramList){
			wrapper.core.put(EAIColumnEnum.RECV_EDATE.getColumnName(), nowTime);
			if(wrapper.core.size()!=13 && wrapper.core.size()!=23){
				logger.debug("Map Element Size[{}] : [{}]",new Object[]{wrapper.core.size(),wrapper.core});
			}
			for(String key : wrapper.core.keySet()){
				
				value = wrapper.core.get(key);
				if(value instanceof XMLGregorianCalendar){
					GregorianCalendar cal
						= XMLGregorianCalendar.class.cast(value)
							.toGregorianCalendar();
					value = cal.getTime();
					wrapper.core.put(key,value);
				}
			}
			paramSources[idx] = new MapSqlParameterSource(wrapper.core);
			
			idx++;
		}
		
		
		
		int[] result = jdbcTemplate.batchUpdate(sql, paramSources);
		return result;
	}
}
