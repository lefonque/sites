package org.epis.ws.provider.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.epis.ws.common.entity.BizVO;
import org.epis.ws.manager.core.service.LogService;
import org.epis.ws.provider.dao.BizDAO;
import org.epis.ws.provider.service.core.EPISConcreteWSGateway;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:/META-INF/spring/*.xml","classpath*:/META-INF/cxf/*.xml"})
public class DAOTest {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private LogService logService;
	
	@Autowired
	private BizDAO bizDAO;
	
	@Autowired
	private EPISConcreteWSGateway gtway;
	
	@Test
	public void clientDaoTest(){
		String sql = "SELECT * FROM ifs_if_mutong_temp where rownum=1";
		List<Map<String,Object>> row = bizDAO.select(sql);
		ObjectMapper mapper = new ObjectMapper();
		String json = null;
		try {
			json = mapper.writeValueAsString(row);
			BizVO bizVO = new BizVO();
			bizVO.setJsonData(json);
			bizVO.setAgentId("AGENT-1");
			bizVO.setJobId("JOB-3");
			
			gtway.processPrimitiveData(bizVO);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void jsonDeserializationTest(){
		Map<String, Object> element = new HashMap<String, Object>();
		element.put("FIELD-A", "abcdefg");
		element.put("FIELD-B", Calendar.getInstance().getTime());
		element.put("FIELD-C", 100l);
		
		List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
		data.add(element);
		
		element = new HashMap<String,Object>();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -2);
		element.put("FIELD-A", "yyz");
		element.put("FIELD-B", cal.getTime());
		element.put("FIELD-C", 33l);
		data.add(element);
		
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			String str = mapper.writeValueAsString(data);
			logger.debug("json : [{}]",str);
			
			List<Map<String,Object>> converted = mapper.readValue(str, new TypeReference<List<Map<String,Object>>>() {});
			Date date = (Date)converted.get(0).get("FIELD-B");
			logger.debug("Date[{}]:[{}]", new Object[]{date.getClass(),date});
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
