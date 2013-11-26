package org.epis.ws.consumer.dao;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.epis.ws.consumer.vo.ColumnInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * agent.properties에 설정된 SQL문을 실행하는 DAO
 * 
 * @author developer
 *
 */
@Repository
public class AgentBizDAO {

	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	@Qualifier("dataSource")
	public void setDataSource(DataSource dataSource){
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public Map<String,Object> selectInfo(String sql, Object... params){
		Map<String,Object> result = jdbcTemplate.queryForMap(sql, params);
		return result;
	}
	
	public Map<String,Object> selectInfo(String sql, List<ColumnInfoVO> params){
		
		Object[] args = new Object[params.size()];
		int[] argTypes = new int[params.size()];
		
		int i = 0;
		for(ColumnInfoVO colInfo : params){
			args[i] = colInfo.getValue();
			argTypes[i] = colInfo.getType();
			i++;
		}
		Map<String,Object> result = jdbcTemplate.queryForMap(sql, args, argTypes);
		return result;
	}
	
	
	
	public List<Map<String,Object>> selectList(String sql, Object... params){
		List<Map<String,Object>> result = jdbcTemplate.queryForList(sql, params);
		return result;
	}
	
	public List<Map<String,Object>> selectList(String sql, List<ColumnInfoVO> params){
		Object[] args = new Object[params.size()];
		int[] argTypes = new int[params.size()];
		
		int i = 0;
		for(ColumnInfoVO colInfo : params){
			args[i] = colInfo.getValue();
			argTypes[i] = colInfo.getType();
			i++;
		}
		
		List<Map<String,Object>> result = jdbcTemplate.queryForList(sql, args, argTypes);
		return result;
	}
	
	
	
	/**
	 * For Insert, Update, Delete
	 * @param sql
	 * @param params
	 * @return
	 */
	public int modify(String sql, Object... params){
		int result = jdbcTemplate.update(sql, params);
		return result;
	}
	
	public int modify(String sql, List<ColumnInfoVO> params){
		Object[] args = new Object[params.size()];
		int[] argTypes = new int[params.size()];
		
		int i = 0;
		for(ColumnInfoVO colInfo : params){
			args[i] = colInfo.getValue();
			argTypes[i] = colInfo.getType();
			i++;
		}
		int result = jdbcTemplate.update(sql, args, argTypes);
		return result;
	}
	
	
}
