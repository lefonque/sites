package org.epis.ws.agent.test;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:/META-INF/spring/*.xml")
public class InvalidCharacterTest {
	
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	@Qualifier("dataSource")
	public void setDataSource(DataSource dataSource){
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	@Test
	public void testCharset(){
		String sql = "SELECT * FROM COM_SEED WHERE IDX=431";
		List<Map<String,Object>> list = jdbcTemplate.getJdbcOperations().queryForList(sql);
		
		String val = null;
		String literal = "맛이 좋아 소비자가 선호합니다.";
		Map<String,Object> map = list.get(0);
		Object obj = null;
		obj = map.get("ITEM_SPEC");
		val = String.class.cast(obj);
		logger.debug("ITEM_SPEC : [{}]",obj);
		logger.debug("CAUTION : [{}]",map.get("CAUTION"));
		
		int idx = val.indexOf(literal);
		val = val.substring(idx);
		val = val.substring(literal.length());
		byte[] bt = val.getBytes();
		int length = 4;
		byte[] t = new byte[length];
		System.arraycopy(bt, 0, t, 0, length);
		logger.debug("Hidden : [{}]", new String(t));
		logger.debug("Test : [{}]", new String(new byte[]{-21, -95, -100}));
		String ro = " 로";
		logger.debug("normal : [{}]", ro.getBytes());
		
		for(int i = 0; i < bt.length; i++){
			if(bt[i]==0){
				byte[] temp = new byte[5];
				System.arraycopy(bt, i, temp, 0, 5);
				logger.debug("odd char : [{}]",new String(temp));
				i += 3;
			}
		}
		logger.debug("String : [{}]", bt);
	}
}
