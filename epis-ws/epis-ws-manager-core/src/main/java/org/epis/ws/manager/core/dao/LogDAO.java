package org.epis.ws.manager.core.dao;

import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.epis.ws.manager.entity.JQGridVO;
import org.epis.ws.manager.entity.LogVO;
import org.epis.ws.manager.util.JQGridSearchOpEnum;
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

	public List<LogVO> selectLogList(JQGridVO paging){
		String sql = sqlRepo.getProperty("select.log.list");
		
		long startRowIndex = ((paging.getPage() * paging.getRows()) - paging.getRows()) + 1;
		long endRowIndex = (paging.getPage() * paging.getRows());
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("startIndex", startRowIndex);
		paramSource.addValue("endIndex", endRowIndex);
		
		StringBuilder where = new StringBuilder();
		if(paging.is_search()){
			where.append(getWhereClause(paging," WHERE "));
			paramSource.addValue(paging.getSearchField(),paging.getSearchString());
		}
		sql = String.format(sql, paging.getSidx(), paging.getSord(), where.toString());
		
		List<LogVO> result = jdbcTemplate.query(sql, paramSource, logRowMapper);
		return result;
	}
	
	public int selectLogCount(JQGridVO paging){
		int result = 0;
		StringBuilder sql = new StringBuilder(sqlRepo.getProperty("select.log.count"));
		
		if(paging.is_search()){
			sql.append(getWhereClause(paging," WHERE "));
			MapSqlParameterSource paramSource
				= new MapSqlParameterSource(paging.getSearchField(), paging.getSearchString());
			
			result = jdbcTemplate.queryForInt(sql.toString(), paramSource);
		}
		else{
			result = jdbcTemplate.getJdbcOperations().queryForInt(sql.toString());
		}
		
		return result;
	}
	
	private StringBuilder getWhereClause(JQGridVO paging, String prefix){
		String param = ":" + paging.getSearchField();
		JQGridSearchOpEnum opEnum = JQGridSearchOpEnum.get(paging.getSearchOper());
		
		StringBuilder where = new StringBuilder();
		where.append(prefix).append(paging.getSearchField())
			.append(String.format(opEnum.getValue(), param));
		return where;
	}
	
}
