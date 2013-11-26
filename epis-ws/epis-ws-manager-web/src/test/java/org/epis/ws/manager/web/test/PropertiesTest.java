package org.epis.ws.manager.web.test;

import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:/META-INF/spring/*.xml")
public class PropertiesTest {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	@Qualifier("jdbc")
	private Properties jdbcProp;
	
	@Autowired
	@Qualifier("dataSource")
	private BasicDataSource dataSource;
	
	@Test
	public void test(){
		logger.debug("db.hsqldb.username : {}",jdbcProp.getProperty("db.hsqldb.username"));
		logger.debug("db.hsqldb.url : {}",jdbcProp.getProperty("db.hsqldb.url"));
		
		try {
			logger.debug("connection : {}",(dataSource.getConnection()==null));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
