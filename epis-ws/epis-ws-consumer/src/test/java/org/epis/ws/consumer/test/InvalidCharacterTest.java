package org.epis.ws.consumer.test;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.epis.ws.common.entity.MapWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedCaseInsensitiveMap;

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
		List<MapWrapper> list = jdbcTemplate.getJdbcOperations().query(sql, new ColumnHashMapRowMapper());
		
		String val = null;
		String literal = "맛이 좋아 소비자가 선호합니다.";
		Map<String,Object> map = list.get(0).core;
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

	class ColumnHashMapRowMapper implements RowMapper<MapWrapper> {
		public MapWrapper mapRow(ResultSet rs, int rowNum) throws SQLException {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			HashMap<String, Object> mapOfColValues = createColumnMap(columnCount);
			for (int i = 1; i <= columnCount; i++) {
				String key = getColumnKey(JdbcUtils.lookupColumnName(rsmd, i));
				Object obj = getColumnValue(rs, i);
				if(key.equalsIgnoreCase("ITEM_SPEC")){
					logger.debug("ITEM_SPEC : [{}]",obj);
				}
				mapOfColValues.put(key, obj);
			}
			MapWrapper result = new MapWrapper(mapOfColValues);
			return result;
		}
		
		protected HashMap<String, Object> createColumnMap(int columnCount) {
			return new LinkedCaseInsensitiveMap<Object>(columnCount);
		}
		
		protected String getColumnKey(String columnName) {
			return columnName;
		}
		
		protected Object getColumnValue(ResultSet rs, int index) throws SQLException {
			Object result = JdbcUtils.getResultSetValue(rs, index);
			//CXF에서 XML Escape를 하기 때문에 처리상에 문제 발생함.
			//CDATA를 이용하려면 MOXy를 이용하는 방법밖에 없음.
//			if(result instanceof String){
//				result = "<![CDATA[" + result + "]]>";
//			}
			
			boolean oddByte = false;
			if(result instanceof String){
				String str = String.class.cast(result);
//				str = str.trim();
				byte[] temp = str.getBytes();
				for(int i = 0; i < temp.length; i++){
					if(temp[i]==0){
						oddByte = true;
						temp[i] = 32;
					}
				}
				if(oddByte){
					str = new String(temp);
				}
				result = str;
			}
			return result;
		}
	
	}
	
}
