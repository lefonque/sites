package org.epis.ws.manager.util;

import org.epis.ws.common.entity.JDBConnectionVO;
import org.springframework.util.Assert;

/**
 * <pre>
 * <p>다중 DataSource를 사용하기 위한 ContextHolder</p>
 * 
 * 서버단에서 job id에 따라 Connection을 달리 할 경우에 사용할 목적으로
 * 작성한 DynamicDataSource에 연계되는 클래스임.
 * 
 * ThreadLocal에는 DB접속정보 및 job id를 담은 Map을 get/set하고 있으며,
 * DynamicDataSource를 사용하게 될 경우, Job쪽 테이블에서 jdbc접속정보를 추출하여
 * 별도의 테이블로 작성하고, 본 클래스의 Map은 jdbc접속정보 테이블의 VO로 변경하는 것이 좋음
 * (JDBConnectionVO을 이용하여 Table 세팅진행 권장)
 * 
 * </pre>
 * @author developer
 *
 */
public class DBKeyContextHolder {

	private static final ThreadLocal<JDBConnectionVO> holder
		= new ThreadLocal<JDBConnectionVO>();
	
	/**
	 * 
	 * @return
	 */
	public static final JDBConnectionVO get(){
		return holder.get();
	}
	
	public static final void set(JDBConnectionVO key){
		Assert.notNull(key, "JOB NAME cannot be null");
		holder.set(key);
	}
	
	public static final void clear(){
		holder.remove();
	}
}
