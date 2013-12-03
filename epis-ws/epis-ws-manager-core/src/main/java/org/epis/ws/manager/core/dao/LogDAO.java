package org.epis.ws.manager.core.dao;

import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.epis.ws.manager.entity.JQGridVO;
import org.epis.ws.manager.entity.LogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class LogDAO {
	
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	private final RowMapper<LogVO> logRowMapper;
	
	public LogDAO(){
		logRowMapper = new BeanPropertyRowMapper<LogVO>(LogVO.class);
	}
	
	@Autowired
	@Qualifier("sql")
	private Properties sqlRepo;

	@Autowired
	@Qualifier("dataSource")
	public void setDataSource(DataSource dataSource){
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public int insertLog(LogVO log){
		String sql = sqlRepo.getProperty("insert.log");
		
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(log);
		int result = jdbcTemplate.update(sql, paramSource);
		return result;
	}

	public List<LogVO> selectLogList(JQGridVO searchParam){
		String sql = sqlRepo.getProperty("select.log.list");
		sql = String.format(sql,searchParam.getSidx(),searchParam.getSord());
		
		long startRowIndex = ((searchParam.getPage() * searchParam.getRows()) - searchParam.getRows()) + 1;
		long endRowIndex = (searchParam.getPage() * searchParam.getRows());
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("startIndex", startRowIndex);
		paramSource.addValue("endIndex", endRowIndex);
		
		List<LogVO> result = jdbcTemplate.query(sql, paramSource, logRowMapper);
		return result;
	}
}
