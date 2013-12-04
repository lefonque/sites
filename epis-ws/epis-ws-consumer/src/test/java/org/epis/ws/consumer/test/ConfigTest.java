package org.epis.ws.consumer.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.epis.ws.common.entity.MapWrapper;
import org.epis.ws.common.entity.RecordMap;
import org.epis.ws.common.entity.RecordMapEntry;
import org.epis.ws.consumer.dao.AgentBizDAO;
import org.epis.ws.consumer.service.core.UnixScheduleRegister;
import org.epis.ws.consumer.service.core.WindowsScheduleRegister;
import org.epis.ws.consumer.util.PropertyEnum;
import org.epis.ws.consumer.util.SqlUtil;
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
public class ConfigTest {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private WindowsScheduleRegister ntSchedule;
	
	@Autowired
	private UnixScheduleRegister unixSchedule;
	
	@Autowired
	private AgentBizDAO bizDao;
	
	@Autowired
	@Qualifier("jobProp")
	private Properties jobProp;
	
	@Autowired
	private SqlUtil sqlUtil;
	
	@Test
	public void testSelect(){
		String jobId = System.getProperty(PropertyEnum.SYS_JOB_NAME.getKey());
		
		String sql = jobProp.getProperty(jobId + PropertyEnum.JOB_SQL_MAIN.getKey());
//		List<MapWrapper> list = bizDao.selectList(sql);
		List<RecordMap> mapList = bizDao.selectListAsRecordMap(sql);
		for(RecordMap wrapper : mapList){
			logger.debug("entry count : {}", wrapper.entry.size());
			for(RecordMapEntry entry : wrapper.entry){
				logger.debug("{} : {}",entry.getKey(),entry.getValue());
			}
		}
	}
	
//	@Test
	public void testSqlUtil(){
		String sql = "INSERT INTO just_table (field1,field2,field3,field4,field5) VALUES('A? or B?', ?, 'get out', ?, CONCAT('P-',?))";
		logger.debug(sqlUtil.convertInsertSQL(sql));
		
		sql = "UPDATE just_table SET field1=?, field2=CONCAT('P-',?), field3='James', field4=? WHERE field5=TRIM(?)";
		logger.debug(sqlUtil.convertNamedParameterUpdateSQL(sql));
	}
	
//	@Test
	public void testGarage(){
		String dumm = "        1   ...                     오후 5:13     E:\temp\test.bat";
		String[] tokens = StringUtils.split(dumm," ");
		for(String token : tokens){
			logger.debug("token : [{}]",token);
		}
		
		String jobName = "job1";
		String template = "cmd /c \"at %1$s /every:M,T,W,Th,F,S,Su cmd /c \"%2$s\\biz.bat %3$s\"\"";
		String result = String.format(template
				,jobProp.getProperty(jobName+".execTime")
				,System.getProperty(PropertyEnum.SYS_ROOT_DIR.getKey())
				,jobName);
		logger.debug("formatted : [{}]", result);
	}
	
	
	//@Test
	public void testConfig(){
		

//		Map<String,String> ntScheduleMap = new HashMap<String,String>();
//		scheduleMap.put("EPIS_AGENT_SYNC","SCHTASKS /Create /TN EPIS_AGENT_SYNC /SC MINUTE /MO 15 /SD 2013-11-14 /ST 10:30 /TR C:\\Sites\\Works\\episWorks\\epis-ws\\epis-ws-consumer\\config.bat");
		
		Map<String,String> unixScheduleMap = new HashMap<String,String>();
		unixScheduleMap.put("EPIS_AGENT_SYNC", "*/2 * * * * /home/exerciser/Agent/test2.sh '/home/exerciser/Agent'");
		
	}
}
