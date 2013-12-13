package org.epis.ws.provider.dao;

import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.xml.datatype.XMLGregorianCalendar;

import org.epis.ws.common.utils.EAIColumnEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BizDAO {

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
			wrapper.put(EAIColumnEnum.EDATE.name(), nowTime);
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
	
	public List<Map<String,Object>> select(String sql){
		return jdbcTemplate.getJdbcOperations().queryForList(sql);
	}
	
}
