package org.epis.ws.provider.test;

import org.epis.ws.common.entity.AgentVO;
import org.epis.ws.provider.dao.core.ConfigurationDAO;
import org.epis.ws.provider.service.core.EPISConcreteWSGateway_OLD;
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
	private ConfigurationDAO clientDao;
	
	@Autowired
	private EPISConcreteWSGateway_OLD gtway;
	
	
	
	@Test
	public void clientDaoTest(){
		try {
			AgentVO client = clientDao.selectAgentInfo("CLIENT#3");
			logger.debug("CLIENT : {}",client.getAgentId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
