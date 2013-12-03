package org.epis.ws.provider.test;

import java.util.List;

import org.epis.ws.common.entity.BizVO;
import org.epis.ws.common.entity.MapWrapper;
import org.epis.ws.manager.core.dao.ConfigurationDAO;
import org.epis.ws.provider.service.BizService;
import org.epis.ws.provider.service.core.EPISConcreteWSGateway;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:/META-INF/spring/*.xml")
public class DAOTest {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ConfigurationDAO configDao;
	
	@Autowired
	private BizService service;
	
	@Autowired
	private EPISConcreteWSGateway gtway;
	
	
	
	@Test
	public void clientDaoTest(){
		try {
			BizVO bizVO = new BizVO();
			bizVO.setAgentId("AGENT-5");
			bizVO.setJobId("JOB-1");
			
			String sql = "SELECT * FROM if_src_job1";
			List<MapWrapper> paramList = service.getData(sql);
			bizVO.setDataList(paramList);
			service.addData(bizVO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
