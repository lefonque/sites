package org.epis.ws.agent.service;

import java.sql.Timestamp;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.epis.ws.agent.dao.AgentBizDAO;
import org.epis.ws.agent.util.SqlUtil;
import org.epis.ws.common.entity.MapWrapper;
import org.epis.ws.common.utils.EAIColumnEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>SQL을 이용한 업무처리를 수행하는 서비스 클래스</p>
 * @author developer
 *
 */
@Service
public class AgentBizService {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private AgentBizDAO dao;
	
	@Value("#{jobProp['${job.id}.sqlPre']}")
	private String preSQL;
	
	@Value("#{jobProp['${job.id}.sqlMain']}")
	private String mainSQL;
	
	@Value("#{jobProp['${job.id}.sqlPost']}")
	private String postSQL;
	
	@Autowired
	private SqlUtil sqlUtil;
	
	/**
	 * <pre>
	 * Bean 생성시에 postSQL이 존재할 경우,
	 * mainSQL은 select시에 paging이 가능하도록
	 * sql 앞뒤에 페이징 관련 구문을 추가한다.
	 * </pre>
	 */
	@PostConstruct
	public void init(){
		if(StringUtils.isNotEmpty(postSQL)){
			mainSQL = sqlUtil.convertPagableSelectSQL(mainSQL);	// Convert paginational SQL
		}
	}
	
	/**
	 * <pre>
	 * 전처리SQL을 실행하여 실행된 결과수를 리턴한다.
	 * 전처리SQL이 없을 경우 -1을 리턴한다.
	 * 
	 * 트랜잭션 적용됨.(Exception발생시 rollback됨)
	 * </pre>
	 * @return
	 */
	@Transactional(value="transactionManager",rollbackFor=Throwable.class)
	public int executePreSQL(){
		int result = -1;
		if(StringUtils.isNotEmpty(preSQL)){
			result = dao.modify(preSQL);
		}
		return result;
	}
	
	/**
	 * <pre>
	 * 후처리SQL을 실행하여 실행된 건수를 리턴한다.
	 * 후처리 SQL이 없을 경우 -1을 리턴한다.
	 * 
	 * 트랜잭션 적용됨.(Exception발생시 rollback됨)
	 * </pre>
	 * @param rowList	후처리 적용대상 데이터목록
	 * @param eflag		WebService 처리결과값
	 * @return
	 */
	@Transactional(value="transactionManager",rollbackFor=Throwable.class)
	public int executePostSQL(List<MapWrapper> rowList, String eflag){
		int result = -1;
		if(StringUtils.isEmpty(postSQL)){
			return result;
		}
		
		Timestamp edate = new Timestamp(System.currentTimeMillis());
		MapSqlParameterSource[] batchArgs = new MapSqlParameterSource[rowList.size()];
		int loopIdx = 0;
		for(MapWrapper wrapper : rowList){
			wrapper.core.put(EAIColumnEnum.EFLAG.getColumnName(),eflag);
			wrapper.core.put(EAIColumnEnum.SND_EDATE.getColumnName(),edate);
			batchArgs[loopIdx++] = new MapSqlParameterSource(wrapper.core);
		}
		
		result = 0;
		long startMillisec = System.currentTimeMillis(); 
		int[] resultArray = dao.batchModify(postSQL, batchArgs);
		long endMillisec = System.currentTimeMillis();
		logger.debug("Elapsed Time : [{}]",(endMillisec-startMillisec)/1000);
		result = resultArray.length;
		return result;
	}
	
	/**
	 * <pre>
	 * Main SQL을 실행하여 Select된 결과를 리턴한다.
	 * Main SQL은 반드시 Select 구문만 가능한다.
	 * </pre>
	 * @return
	 */
	public List<MapWrapper> executeMainSQL(){
		List<MapWrapper> result = dao.selectList(mainSQL);
		return result;
	}
	
//	public List<RecordMap> executeMainSQLAsRecordMap(){
//		return dao.selectListAsRecordMap(mainSQL);
//	}
}
