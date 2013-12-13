package org.epis.ws.provider.dao;

import java.util.Properties;

import javax.sql.DataSource;

import org.epis.ws.provider.entity.SMSQueueRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class SMSQueueDAO {

	@Autowired
	@Qualifier("sql")
	private Properties sqlRepo;

	@Autowired
	@Qualifier("smsDataSource")
	public void setDataSource(DataSource dataSource){
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	public int insertSMSQueueRecord(SMSQueueRecord record){
		String sql = sqlRepo.getProperty("insert.sms");
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(record);
		int result = jdbcTemplate.update(sql, paramSource);
		
		return result;
	}
}
