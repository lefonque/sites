package org.epis.ws.provider.dao.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.epis.ws.common.entity.AgentVO;
import org.epis.ws.common.entity.JobVO;
import org.epis.ws.provider.entity.JQGridVO;
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

@Repository
public class ConfigurationDAO {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	@Qualifier("sql")
	private Properties sqlRepo;

	private NamedParameterJdbcTemplate jdbcTemplate;
	

	private final RowMapper<AgentVO> clientRowMapper;
	private final RowMapper<JobVO> jobRowMapper;
	
	public ConfigurationDAO(){
		jobRowMapper = new BeanPropertyRowMapper<JobVO>(JobVO.class);
		clientRowMapper = new BeanPropertyRowMapper<AgentVO>(AgentVO.class);
	}

	@Autowired
	@Qualifier("dataSource")
	public void setDataSource(DataSource dataSource){
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	
	public Map<String, String> selectLoginPassword(String loginUsername) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("loginUsername","jack");
		result.put("loginPassword","jackass");
		return result;
	}
	
	public String selectWebserviceUserPassword(String userID) throws Exception {
		String sql = sqlRepo.getProperty("select.webservice.password");
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("websvcUser", userID);
		
		logger.debug("SQL : [{}]",sql);
		logger.debug("PARAMETERS : [{}]",userID);
		
		String result = null;
		try {
			result = jdbcTemplate.queryForObject(sql, paramSource, String.class);
		} catch (EmptyResultDataAccessException e) {
			logger.warn("No Data Found in TBL_CONFIG by USER ID : [{}]",userID);
		}
		
		return result;
	}
	
	public AgentVO selectAgentInfo(String agentId) throws Exception {
//		String sql = "SELECT user_id,pass,client_id FROM tbl_client WHERE user_id = :userId";
		
		String sql = sqlRepo.getProperty("select.config.agent.one");

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("agentId", agentId);
		
		logger.debug("SQL : [{}]",sql);
		logger.debug("PARAMETERS : [{}]",agentId);
		
		AgentVO result = null;
		try {
			result = jdbcTemplate.queryForObject(
					sql, paramSource, clientRowMapper);
		} catch (EmptyResultDataAccessException e) {
			logger.info("No Data Found in TBL_CONFIG by Agent ID : [{}]",agentId);
		}
		
//		ClientVO result = jdbcTemplate.queryForObject(
//				sql, new BeanPropertyRowMapper<ClientVO>(ClientVO.class)
//				,userID);
		
		
		return result;
	}
	
	public int selectAgentCount(){
		String sql = sqlRepo.getProperty("select.config.agent.count");
		
		logger.debug("SQL : {}",sql);
		
		int result = jdbcTemplate.queryForInt(sql,(SqlParameterSource)null);
		return result;
	}
	
	public List<AgentVO> selectAgentList(JQGridVO searchParam){
		
		String sql = sqlRepo.getProperty("select.config.agent.list");
		sql = String.format(sql,searchParam.getSidx(),searchParam.getSord());
		
//		long startIndex = ((searchParam.getPage()-1) * searchParam.getRows()) + 1;
		long startIndex = ((searchParam.getPage() * searchParam.getRows()) - searchParam.getRows()) + 1;
		long endIndex = (searchParam.getPage() * searchParam.getRows());

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("startIndex", startIndex);
		paramSource.addValue("endIndex", endIndex);
		
		logger.debug("SQL : {}",sql);
		logger.debug("PARAMETERS : {},{}",new Object[]{startIndex,endIndex});
		
		List<AgentVO> result = jdbcTemplate.query(
				sql, paramSource, clientRowMapper);
		return result;
	}
	
	public int insertAgent(AgentVO config){
		
		String sql = sqlRepo.getProperty("insert.config.agent.");
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(config);
		
		int result = jdbcTemplate.update(sql, paramSource);
		return result;
	}
	
	public int updateAgent(AgentVO config){

		String sql = sqlRepo.getProperty("update.config.agent.");
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(config);
		
		int result = jdbcTemplate.update(sql, paramSource);
		return result;
	}
	
	public int deleteAgent(String agentId){
		
		String sql = sqlRepo.getProperty("delete.config.agent.");
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("agentId",agentId);
		
		int result = jdbcTemplate.update(sql, paramSource);
		return result;
	}

	
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
	
	public int selectJobCount(String agentId){
		String sql = sqlRepo.getProperty("select.config.job.count");
		MapSqlParameterSource paramSource = new MapSqlParameterSource("agentId",agentId);
		
		logger.debug("SQL : {}",sql);
		logger.debug("PARAMETERS : {}",agentId);
		
		int result = jdbcTemplate.queryForInt(sql, paramSource);
		
		return result;
	}
	
	public List<JobVO> selectJobList(String agentId){
		MapSqlParameterSource paramSource = new MapSqlParameterSource("agentId",agentId);
		
		String sql = sqlRepo.getProperty("select.config.job.list");
		
		logger.debug("SQL : {}",sql);
		logger.debug("PARAMETERS : {}",paramSource.getValues());
		
		List<JobVO> result = jdbcTemplate.query(
				sql, paramSource, jobRowMapper);
		return result;
	}
	
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
	
	public int insertJob(JobVO job){
		String sql = sqlRepo.getProperty("insert.config.job");
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(job);

		logger.debug("SQL : {}",sql);
		logger.debug("PARAMETERS : {}",job);
		
		int result = jdbcTemplate.update(sql, paramSource);
		return result;
	}
	
	public int updateSchedule(JobVO job){
		String sql = sqlRepo.getProperty("update.config.job");
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(job);

		logger.debug("SQL : {}",sql);
		logger.debug("PARAMETERS : {}",job);
		
		int result = jdbcTemplate.update(sql, paramSource);
		return result;
	}
	
	public int deleteJob(String jobId){
		String sql = sqlRepo.getProperty("delete.config.job");
		MapSqlParameterSource paramSource = new MapSqlParameterSource("jobId",jobId);

		logger.debug("SQL : {}",sql);
		logger.debug("PARAMETERS : {}",jobId);
		
		int result = jdbcTemplate.update(sql, paramSource);
		return result;
	}
	
	public int deleteJobByClientId(String agentId){
		String sql = sqlRepo.getProperty("delete.config.job.byclientid");
		MapSqlParameterSource paramSource = new MapSqlParameterSource("agentId",agentId);

		logger.debug("SQL : {}",sql);
		logger.debug("PARAMETERS : {}",agentId);
		
		int result = jdbcTemplate.update(sql, paramSource);
		return result;
	}
	
	public Map<String,Object> selectJdbcInfo(JobVO job){
		String sql = sqlRepo.getProperty("select.config.job.server.jdbc.one");
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(job);
		Map<String, Object> result = jdbcTemplate.queryForMap(sql, paramSource);
		return result;
	}
}
