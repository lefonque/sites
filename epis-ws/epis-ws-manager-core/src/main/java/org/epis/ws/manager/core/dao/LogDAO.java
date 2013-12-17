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

/**
 * <pre>
 * <p>Provider가 로깅데이터를 DB에 반영하거나 조회할 때 사용되는 DAO</p>
 * </pre> 
 * @author developer
 *
 */
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
	
	/**
	 * 로깅데이터를 insert한다.
	 * @param log
	 * @return
	 */
	public int insertLog(LogVO log){
		String sql = sqlRepo.getProperty("insert.log");
		
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(log);
		int result = jdbcTemplate.update(sql, paramSource);
		return result;
	}

	/**
	 * <pre>
	 * <p>로깅데이터를 조회한다.</p>
	 * 
	 * 검색어를 사용한 조건검색이 가능하며, 검색시 단 하나의 검색조건만 사용가능함.
	 * </pre>
	 * @param paging
	 * @return
	 */
	public List<LogVO> selectLogList(JQGridVO paging){
		String sql = sqlRepo.getProperty("select.log.list");
		
		long startRowIndex = ((paging.getPage() * paging.getRows()) - paging.getRows()) + 1;
		long endRowIndex = (paging.getPage() * paging.getRows());
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("startIndex", startRowIndex);
		paramSource.addValue("endIndex", endRowIndex);
		
		StringBuilder where = new StringBuilder();
		if(paging.is_search()){
			where.append(getWhereClause(paging));
			paramSource.addValue(paging.getSearchField(),paging.getSearchString());
		}
		sql = String.format(sql, paging.getSidx(), paging.getSord(), where.toString());
		
		List<LogVO> result = jdbcTemplate.query(sql, paramSource, logRowMapper);
		return result;
	}
	
	/**
	 * <pre>
	 * <p>검색조건이 반영된 총 Log 갯수를 구하는 메서드</p>
	 * 
	 * Log 목록표시 Grid 부분의 Paging을 위하여 총 record수를 구하기 위해 사용됨.
	 * 조건 검색이 가능하므로, 검색 조건을 반영하여 총 Log갯수를 구함.
	 * 
	 * 
	 * </pre>
	 * @param paging
	 * @return
	 */
	public int selectLogCount(JQGridVO paging){
		int result = 0;
		StringBuilder sql = new StringBuilder(sqlRepo.getProperty("select.log.count"));
		
		if(paging.is_search()){
			sql.append(getWhereClause(paging));
			MapSqlParameterSource paramSource
				= new MapSqlParameterSource(paging.getSearchField(), paging.getSearchString());
			
			result = jdbcTemplate.queryForInt(sql.toString(), paramSource);
		}
		else{
			result = jdbcTemplate.getJdbcOperations().queryForInt(sql.toString());
		}
		
		return result;
	}
	
	/**
	 * <pre>
	 * <p> SQL의 WHERE절 내의 부분을 작성하는 메서드<p>
	 * 
	 * 검색조건을 적용하여 검색할 경우, 검색 조건을 적용하여 SQL의
	 * WHERE절 부분을 작성하는데, JQGridSearchOpEnum을 이용하여
	 * 비교조건별 적절한 SQL연산자를 적용한다.
	 * 
	 * WHERE절을 작성하여 SQL에 삽입하는 만큼, 본 메서드가 적용되는
	 * SQL은 문장 구성시 이에 대해 유의하여야 한다.
	 * </pre>
	 * @param paging
	 * @param prefix
	 * @return
	 */
	private StringBuilder getWhereClause(JQGridVO paging){
		String param = ":" + paging.getSearchField();
		JQGridSearchOpEnum opEnum = JQGridSearchOpEnum.get(paging.getSearchOper());
		
		StringBuilder where = new StringBuilder();
		where.append(" WHERE ").append(paging.getSearchField())
			.append(String.format(opEnum.getValue(), param));
		return where;
	}
	
}
