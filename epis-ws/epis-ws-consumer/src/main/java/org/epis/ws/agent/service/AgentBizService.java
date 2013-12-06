package org.epis.ws.agent.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.epis.ws.agent.dao.AgentBizDAO;
import org.epis.ws.agent.util.SqlUtil;
import org.epis.ws.common.entity.MapWrapper;
import org.epis.ws.common.entity.RecordMap;
import org.epis.ws.common.utils.EAIColumnEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AgentBizService {
	
	@Autowired
	private AgentBizDAO dao;
	
	@Autowired
	@Qualifier("jobProp")
	private Properties jobProp;
	
	@Value("#{jobProp['${job.id}.sqlPre']}")
	private String preSQL;
	
	@Value("#{jobProp['${job.id}.sqlMain']}")
	private String mainSQL;
	
	@Value("#{jobProp['${job.id}.sqlPost']}")
	private String postSQL;
	
	@Autowired
	private SqlUtil sqlUtil;
	
	@PostConstruct
	public void init(){
		if(StringUtils.isNotEmpty(postSQL)){
			mainSQL = sqlUtil.convertPagableSelectSQL(mainSQL);	// Convert paginational SQL
		}
	}
	
	/**
	 * 전처리SQL을 실행하여 실행된 결과수를 리턴한다.
	 * 전처리SQL이 없을 경우 -1을 리턴한다.
	 * @return
	 */
	@Transactional(rollbackFor=Throwable.class)
	public int executePreSQL(){
		int result = -1;
		if(StringUtils.isNotEmpty(preSQL)){
			result = dao.modify(preSQL);
		}
		return result;
	}
	
	/**
	 * 후처리SQL을 실행하여 실행된 건수를 리턴한다.
	 * 후처리 SQL이 없을 경우 -1을 리턴한다.
	 * @param rowList	후처리 적용대상 데이터목록
	 * @param eflag		WebService 처리결과값
	 * @return
	 */
	@Transactional(rollbackFor=Throwable.class)
	public int executePostSQL(List<MapWrapper> rowList, String eflag){
		int result = -1;
		if(StringUtils.isEmpty(postSQL)){
			return result;
		}
		
		result = 0;
		Timestamp edate = new Timestamp(System.currentTimeMillis());
		for(MapWrapper wrapper : rowList){
			wrapper.core.put(EAIColumnEnum.EFLAG.getColumnName(),eflag);
			wrapper.core.put(EAIColumnEnum.EDATE.getColumnName(),edate);
			result += dao.modify(postSQL, wrapper.core);
		}
		return result;
	}
	
	public List<MapWrapper> executeMainSQL(){
		List<MapWrapper> result = dao.selectList(mainSQL);
		return result;
	}
	
	public List<RecordMap> executeMainSQLAsRecordMap(){
		return dao.selectListAsRecordMap(mainSQL);
	}
}
