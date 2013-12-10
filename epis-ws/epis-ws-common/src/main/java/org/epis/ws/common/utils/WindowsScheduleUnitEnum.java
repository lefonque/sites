package org.epis.ws.common.utils;


/**
 * <pre>
 * NT계열의 Schtasks.exe를 이용한 스케쥴링시, Schedule Job 등록시 옵션명칭을
 * 리터럴 상수로 보관한 Enum
 * </pre>
 * @author developer
 *
 */
public enum WindowsScheduleUnitEnum {

	MINUTE,
	HOURLY,
	DAILY,
	MONTHLY,
	WEEKLY,
	;
}
