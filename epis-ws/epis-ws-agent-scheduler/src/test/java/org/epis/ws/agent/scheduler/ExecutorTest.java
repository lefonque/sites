package org.epis.ws.agent.scheduler;

import org.epis.ws.agent.scheduler.service.AgentExecutor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:/META-INF/spring/*.xml")
public class ExecutorTest {

	@Autowired
	private AgentExecutor executor;
	
	@Test
	public void testExec(){
		executor.execute("JOB-2");
	}
}
