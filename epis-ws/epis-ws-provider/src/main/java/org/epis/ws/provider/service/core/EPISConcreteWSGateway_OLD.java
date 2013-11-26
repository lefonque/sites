package org.epis.ws.provider.service.core;

import java.util.HashMap;
import java.util.List;

import javax.jws.WebService;
import javax.xml.namespace.QName;

import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.ws.security.WSConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;


public class EPISConcreteWSGateway_OLD {
//implements EPISWSGateway {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ConfigurationService clientService;
	
	public String processPrimitiveData(HashMap<String,String> paramMap){
		String result = null;
		
		debug();
		
		return result;
	}
	
//	@Override
//	public ClientVO findConfigurationData(String clientId) throws Exception{
//		ClientVO result = clientService.getConfigurationInfo(clientId);
//		logger.debug("CLIENT [{}] FETCHED Configuration Info",clientId);
//		if(result==null){
//			return result;
//		}
//		//TODO Auditing that client retrieved Configuration Info
//		
//		
//		Map<String,String> scheduleMap = new HashMap<String,String>();
//		List<JobVO> scheduleList = clientService.getScheduleList(result.getClientId());
//		for(JobVO schedule : scheduleList){
//			String cmd = StringUtils.defaultString(schedule.getSchedulerExpression());
//			if(StringUtils.isEmpty(cmd)){
//				if(OSEnum.Windows.name().equals(result.getOperatingSystem())){
//					cmd = generateNTScheduleCMD(schedule);
//				}
//				else{
//					cmd = generateLinuxScheduleCMD(schedule);
//				}
//			}
//			scheduleMap.put(schedule.getScheduleType(), cmd);
//		}
//		result.setScheduleMap(scheduleMap);
//		return result;
//	}
//	
//	
//	/**
//	 * <pre>
//	 * <p>NT의 SCHTASKS의 설정을 generating한다.</p>
//	 * 실행프로그램 설정부분(TR 부분)은 client에서 처리하도록 한다.
//	 * </pre>
//	 * @param schedule
//	 * @return
//	 */
//	public String generateNTScheduleCMD(JobVO schedule){
//		StringBuilder builder = new StringBuilder();
//		builder.append("SCHTASKS /Create /TN %s");
//		
//		builder.append(" /SC ").append(schedule.getIntervalUnit());
//		if(schedule.getIntervalValue()!=null || schedule.getIntervalValue()!=0){
//			builder.append(" /MO ").append(schedule.getIntervalValue());
//		}
//		
//		builder.append(" /SD ").append(DateFormatUtils.format(schedule.getExecDatetime(), "yyyy-MM-dd"))
//			.append(" /ST ").append(DateFormatUtils.format(schedule.getExecDatetime(), "HH:mm"));
//		
//		builder.append(" /TR %s");//=>Client에서 처리하도록 함
//		
//		
//		return builder.toString();
//	}
//	
//	/**
//	 * <pre>
//	 * <p>Unix/Linux의 crontab의 설정을 generating한다.</p>
//	 * crontab 실행명령 및 실행프로그램 설정 부분은 client에서 처리하도록 한다.
//	 * </pre>
//	 * @param schedule
//	 * @return
//	 */
//	public String generateLinuxScheduleCMD(JobVO schedule){
//		StringBuilder builder = new StringBuilder();
//		WindowsScheduleUnitEnum unit = WindowsScheduleUnitEnum.valueOf(schedule.getIntervalUnit());
//		
//		String cronExp = "%1$s %2$s %3$s %4$s %5$s";
//		String intervalValue = "*/1";
//		if(schedule.getIntervalValue()!=null
//				&& schedule.getIntervalValue() > 0){
//			intervalValue = "*/" + schedule.getIntervalValue();
//		}
//		
//		Calendar execDateCal = Calendar.getInstance();
//		execDateCal.setTime(schedule.getExecDatetime());
//		switch(unit){
//		case MINUTE:
//			cronExp = String.format(cronExp
//				,intervalValue
//				,"*"
//				,"*"
//				,"*"
//				,"*");
//			break;
//			
//		case HOURLY:
//			cronExp = String.format(cronExp
//				,cronVal(execDateCal.get(Calendar.MINUTE))
//				,intervalValue
//				,"*"
//				,"*"
//				,"*");
//			break;
//			
//		case DAILY:
//			cronExp = String.format(cronExp
//				,cronVal(execDateCal.get(Calendar.MINUTE))
//				,cronVal(execDateCal.get(Calendar.HOUR))
//				,intervalValue
//				,"*"
//				,"*");
//			break;
//			
//		case MONTHLY:
//			cronExp = String.format(cronExp
//				,cronVal(execDateCal.get(Calendar.MINUTE))
//				,cronVal(execDateCal.get(Calendar.HOUR))
//				,cronVal(execDateCal.get(Calendar.DAY_OF_MONTH))
//				,intervalValue
//				,"*");
//			break;
//			
//		case WEEKLY:
//			cronExp = String.format(cronExp
//				,cronVal(execDateCal.get(Calendar.MINUTE))
//				,cronVal(execDateCal.get(Calendar.HOUR))
//				,"*"
//				,"*"
//				,intervalValue);
//			break;
//		}
//		builder.append(cronExp);
//		
//		return builder.toString();
//	}
	
	
	private String cronVal(int value){
		String result = "*";
		if(value!=0){
			result = String.valueOf(value);
		}
		return result;
	}
	
	
	
	private List<Header> getHeaders(){
		
		List<Header> result = null;
		Message msg = PhaseInterceptorChain.getCurrentMessage();
		result = CastUtils.cast((List<?>)msg.get(Header.HEADER_LIST));
		return result;
	}
	
	private void debug(){
		List<Header> headerList = getHeaders();
		for(Header header : headerList){
			QName qName = header.getName();
			if(WSConstants.WSSE_LN.equals(qName.getLocalPart())
				&& (WSConstants.WSSE11_NS.equals(qName.getNamespaceURI())
					|| WSConstants.WSSE_NS.equals(qName.getNamespaceURI())
					|| "http://schemas.xmlsoap.org/ws/2002/04/secext".equals(qName.getNamespaceURI()))){
				Element security = Element.class.cast(header.getObject());
				List<Element> utElem = DOMUtils.findAllElementsByTagNameNS(security, qName.getNamespaceURI(), WSConstants.USERNAME_LN);
				for(Element utSub : utElem){
					logger.debug("Username : [{}]",utSub.getTextContent());
				}
			}
		}
	}
	
}
