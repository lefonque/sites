package org.epis.ws.manager.core.dao;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.epis.ws.common.entity.AgentVO;
import org.epis.ws.common.entity.JobVO;
import org.epis.ws.manager.entity.JQGridVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

/**
 * <pre>
 * <p>Agent 및 Job 정보를 DB에 관리하는 DAO 클래스</p>
 * 
 * NamedParameterJdbcTemplate 를 이용하여 데이터를 DB에 관리하기 때문에,
 * SQL내의 파라메터부분은 :[파라메터명] 의 형태로 되어야 한다.
 * 
 * EFLAG, EDATE 를 제외한 모든 파라메터명은 대문자로 변환된 컬럼명을 사용한다.
 * 
 * e.g.) INSERT INTO TBL_XXX (address,....) VALUES (:ADDRESS,....)
 * </pre> 
 * @author developer
 *
 */
@Repository
public class ConfigurationDAO {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	@Qualifier("sql")
	private Properties sqlRepo;

	private NamedParameterJdbcTemplate jdbcTemplate;
	

	/**
	 * Select된 결과를 AgentVO에 담기위한 RowMapper
	 */
	private final RowMapper<AgentVO> agentRowMapper;
	/**
	 * Select된 결과를 JobVO에 담기위한 RowMapper
	 */
	private final RowMapper<JobVO> jobRowMapper;
	
	public ConfigurationDAO(){
		jobRowMapper = new BeanPropertyRowMapper<JobVO>(JobVO.class);
		agentRowMapper = new BeanPropertyRowMapper<AgentVO>(AgentVO.class);
	}

	@Autowired
	@Qualifier("dataSource")
	public void setDataSource(DataSource dataSource){
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	
	/**
	 * 웹서비스 계정ID 에 대한 패스워드값을 취득하는 메서드
	 * @param userID
	 * @return
	 * @throws Exception
	 */
	public String selectWebserviceUserPassword(String userID) throws Exception {
		String sql = sqlRepo.getProperty("select.webservice.password");
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("websvcUser", userID);
		
		logger.trace("SQL : [{}]",sql);
		logger.trace("PARAMETERS : [{}]",userID);
		
		String result = null;
		try {
			result = jdbcTemplate.queryForObject(sql, paramSource, String.class);
		} catch (EmptyResultDataAccessException e) {
			logger.warn("No Data Found in TBL_CONFIG by USER ID : [{}]",userID);
		}
		
		return result;
	}
	
	/**
	 * Agent정보를 취득하는 메서드
	 * @param agentId
	 * @return
	 * @throws Exception
	 */
	public AgentVO selectAgentInfo(String agentId) throws Exception {
		String sql = sqlRepo.getProperty("select.config.agent.one");

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("agentId", agentId);
		
		logger.trace("SQL : [{}]",sql);
		logger.trace("PARAMETERS : [{}]",agentId);
		
		AgentVO result = null;
		try {
			result = jdbcTemplate.queryForObject(
					sql, paramSource, agentRowMapper);
		} catch (EmptyResultDataAccessException e) {
			logger.info("No Data Found in TBL_CONFIG by Agent ID : [{}]",agentId);
		}
		
		return result;
	}
	
	/**
	 * <pre>
	 * <p>총 Agent 갯수를 구하는 메서드</p>
	 * 
	 * Agent 목록표시 Grid 부분의 Paging을 위하여 총 record수를 구하기 위해 사용됨.
	 * Agent 목록표시시 조건 검색은 없음
	 * </pre>
	 * @return
	 */
	public int selectAgentCount(){
		String sql = sqlRepo.getProperty("select.config.agent.count");
		
		logger.trace("SQL : {}",sql);
		
		int result = jdbcTemplate.queryForInt(sql,(SqlParameterSource)null);
		return result;
	}
	
	/**
	 * <pre>
	 * <p>페이징된 양 만큼 Agent의 레코드를 구하는 메서드</p>
	 * 
	 * JQGridVO 에 담긴 page번호와 페이지당 표시될 row갯수를 이용하여 페이징을 한다.
	 * </pre>
	 * @param paging
	 * @return
	 */
	public List<AgentVO> selectAgentList(JQGridVO paging){
		
		String sql = sqlRepo.getProperty("select.config.agent.list");
		sql = String.format(sql,paging.getSidx(),paging.getSord());
		
//		long startIndex = ((searchParam.getPage()-1) * searchParam.getRows()) + 1;
		long startIndex = ((paging.getPage() * paging.getRows()) - paging.getRows()) + 1;
		long endIndex = (paging.getPage() * paging.getRows());

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("startIndex", startIndex);
		paramSource.addValue("endIndex", endIndex);
		
		logger.trace("SQL : {}",sql);
		logger.trace("PARAMETERS : {},{}",new Object[]{startIndex,endIndex});
		
		List<AgentVO> result = jdbcTemplate.query(
				sql, paramSource, agentRowMapper);
		return result;
	}
	
	/**
	 * Agent정보를 테이블에 insert한다.
	 * @param config
	 * @return
	 */
	public int insertAgent(AgentVO config){
		
		String sql = sqlRepo.getProperty("insert.config.agent");
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(config);
		
		int result = jdbcTemplate.update(sql, paramSource);
		return result;
	}
	
	/**
	 * Agent정보를 테이블에 update한다.
	 * @param config
	 * @return
	 */
	public int updateAgent(AgentVO config){

		String sql = sqlRepo.getProperty("update.config.agent");
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(config);
		
		int result = jdbcTemplate.update(sql, paramSource);
		return result;
	}
	
	/**
	 * Agent정보를 테이블에서 삭제한다.
	 * @param agentId
	 * @return
	 */
	public int deleteAgent(String agentId){
		
		String sql = sqlRepo.getProperty("delete.config.agent");
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("agentId",agentId);
		
		int result = jdbcTemplate.update(sql, paramSource);
		return result;
	}

	
	/**
	 * 작업설정정보를 조회한다.
	 * @param agentId
	 * @param jobId
	 * @return
	 */
	public JobVO selectJobInfo(String agentId, String jobId){
		String sql = sqlRepo.getProperty("select.config.job.one");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("agentId", agentId);
		paramSource.addValue("jobId", jobId);

		logger.debug("SQL : {}",sql);
		logger.debug("PARAMETERS : {}",paramSource);
		
		JobVO result = jdbcTemplate.queryForObject(sql, paramSource, jobRowMapper);

		return result;
	}
	
	/**
	 * <pre>
	 * <p>총 Job 갯수를 구하는 메서드</p>
	 * 
	 * Job 목록표시 Grid 부분의 Paging을 위하여 총 record수를 구하기 위해 사용됨.
	 * Job 목록표시시 조건 검색은 없음
	 * </pre>
	 * @param agentId
	 * @return
	 */
	public int selectJobCount(String agentId){
		String sql = sqlRepo.getProperty("select.config.job.count");
		MapSqlParameterSource paramSource = new MapSqlParameterSource("agentId",agentId);
		
		logger.debug("SQL : {}",sql);
		logger.debug("PARAMETERS : {}",agentId);
		
		int result = jdbcTemplate.queryForInt(sql, paramSource);
		
		return result;
	}
	
	/**
	 * <pre>
	 * <p>지정된 Agent ID에 소속된 모든 Job의 레코드를 구하는 메서드</p>
	 * 
	 * 현재 사용안함
	 * </pre>
	 * @param agentId
	 * @return
	 */
	public List<JobVO> selectJobList(String agentId){
		MapSqlParameterSource paramSource = new MapSqlParameterSource("agentId",agentId);
		
		String sql = sqlRepo.getProperty("select.config.job.list");
		
		logger.debug("SQL : {}",sql);
		logger.debug("PARAMETERS : {}",paramSource.getValues());
		
		List<JobVO> result = jdbcTemplate.query(
				sql, paramSource, jobRowMapper);
		return result;
	}
	
	/**
	 * <pre>
	 * <p>페이징된 양 만큼 Job의 레코드를 구하는 메서드</p>
	 * 
	 * JQGridVO 에 담긴 page번호와 페이지당 표시될 row갯수를 이용하여 페이징을 한다.
	 * </pre>
	 * @param searchParam
	 * @param agentId
	 * @return
	 */
	public List<JobVO> selectJobList(JQGridVO searchParam, String agentId){
		
		long startIndex = ((searchParam.getPage() * searchParam.getRows()) - searchParam.getRows()) + 1;
		long endIndex = (searchParam.getPage() * searchParam.getRows());

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("startIndex", startIndex);
		paramSource.addValue("endIndex", endIndex);
		paramSource.addValue("agentId",agentId);
		
		String sql = sqlRepo.getProperty("select.config.job.list.paging");
		sql = String.format(sql,searchParam.getSidx(),searchParam.getSord());
		
		logger.debug("SQL : {}",sql);
		logger.debug("PARAMETERS : {}",paramSource.getValues());
		
		List<JobVO> result = jdbcTemplate.query(
				sql, paramSource, jobRowMapper);
		return result;
	}
	
	/**
	 * Job정보를 테이블에 insert한다.
	 * @param job
	 * @return
	 */
	public int insertJob(JobVO job){
		String sql = sqlRepo.getProperty("insert.config.job");
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(job);

		logger.debug("SQL : {}",sql);
		logger.debug("PARAMETERS : {}",job);
		
		int result = jdbcTemplate.update(sql, paramSource);
		return result;
	}
	
	/**
	 * Job정보를 테이블에 update한다.
	 * @param job
	 * @return
	 */
	public int updateSchedule(JobVO job){
		String sql = sqlRepo.getProperty("update.config.job");
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(job);

		logger.debug("SQL : {}",sql);
		logger.debug("PARAMETERS : {}",job);
		
		int result = jdbcTemplate.update(sql, paramSource);
		return result;
	}
	
	/**
	 * Job정보를 테이블에서 삭제한다.
	 * @param jobId
	 * @return
	 */
	public int deleteJob(String jobId){
		String sql = sqlRepo.getProperty("delete.config.job");
		MapSqlParameterSource paramSource = new MapSqlParameterSource("jobId",jobId);

		logger.debug("SQL : {}",sql);
		logger.debug("PARAMETERS : {}",jobId);
		
		int result = jdbcTemplate.update(sql, paramSource);
		return result;
	}
	
	/**
	 * 지정된 Agent에 소속된 모든 Job정보를 테이블에서 삭제한다.
	 * @param agentId
	 * @return
	 */
	public int deleteJobByClientId(String agentId){
		String sql = sqlRepo.getProperty("delete.config.job.byclientid");
		MapSqlParameterSource paramSource = new MapSqlParameterSource("agentId",agentId);

		logger.debug("SQL : {}",sql);
		logger.debug("PARAMETERS : {}",agentId);
		
		int result = jdbcTemplate.update(sql, paramSource);
		return result;
	}
	
	/**
	 * <pre>
	 * <p>Job에 종속된 JDBC 정보를 취득하는 메서드 </p> 
	 * 
	 * DynamicDataSource를 사용할 경우, Job Entity에서
	 * JDBC부분을 추출하여 별도의 Entity 분리하여 둘 간의
	 * 관계를 맺는 구성이 권장되는데, 이 때 Job에 관계된 
	 * DB Connection정보를 취득하는 메서드.
	 * 
	 * DynamicDataSource는 Job별로 Connection을 분리할 경우를 위해
	 * 고안되었으나, 과부하시 performance 상 효율이 좋지 않은 단점이 있음.
	 * 
	 * 현재 DynamicDataSource가 사용되지 않음으로, 본 메서드도 사용되지 않음
	 * </pre>
	 * @param job
	 * @return
	 */
	public Map<String,Object> selectJdbcInfo(JobVO job){
		String sql = sqlRepo.getProperty("select.config.job.server.jdbc.one");
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(job);
		Map<String, Object> result = jdbcTemplate.queryForMap(sql, paramSource);
		return result;
	}
}
