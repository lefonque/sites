package org.epis.ws.provider.test;

import org.epis.ws.common.entity.BizVO;
import org.epis.ws.manager.core.service.LogService;
import org.epis.ws.provider.service.core.EPISConcreteWSGateway;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:/META-INF/spring/*.xml","classpath*:/META-INF/cxf/*.xml"})
public class DAOTest {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private LogService logService;
	
	@Autowired
	private EPISConcreteWSGateway gtway;
	
	
	
	@Test
	public void clientDaoTest(){
		try {
			BizVO bizVO = new BizVO();
			bizVO.setAgentId("AGENT-5");
			bizVO.setJobId("JOB-1");
			
			logService.addLog(bizVO, "S");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
