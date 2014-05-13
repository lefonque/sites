package com.mdm.cleanse.unit.qsa;

import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.mdm.cleanse.base.FunctionUnit;
import com.mdm.cleanse.base.LoggerHelper;
import com.siperian.mrm.cleanse.api.ParameterTypes;

/**
 * <pre>
 * ===========================================================
 *	삼성서울병원 차세대시스템 MDM 시스템
 * 
 *	프로그램 구분	: 정리함수
 *	프로그램 이름	: QSA_CN_SSNCHK
 *	작동설명		: 주민번호 유효성 검사
 *	작성일		: 2014.05.12
 * ===========================================================
 * </pre>
 * @author P25
 *
 */
public class QSA_CN_SSNCHK implements FunctionUnit {
	
	private final Logger logger = Logger.getLogger(QSA_CN_SSNCHK.class);

	/**
	 * 입력파라메터 명
	 */
	protected final String[] inputName = {
			"FRRN", "BRRN"
	};
	
	/**
	 * 입력파라메터 타입
	 */
	protected final String[] inputTypes = {
			ParameterTypes.STRING
			,ParameterTypes.STRING
	};
	
	/**
	 * 입력파라메터 설명
	 */
	protected final String[] inputDescriptions = {
			"주민등록번호 앞자리"
			,"주민등록번호 뒷자리"
	};
	
	/**
	 * 출력파라메터 명
	 */
	protected final String[] outputNames = {
			"FRRN"
			,"BRRN"
			,"CHK_RES"
			,"validationStatus"
	};
	
	/**
	 * 출력파라메터 타입
	 */
	protected final String[] outputTypes = {
			ParameterTypes.STRING
			,ParameterTypes.STRING
			,ParameterTypes.BOOLEAN
			,ParameterTypes.STRING
	};
	
	protected final String[] outputDescriptions = {
			"주민등록번호 앞자리"
			,"주민등록번호 뒷자리"
			,"주민번호 정합성 체크결과(true:유효함,false:유효하지 않음)"
			,"솔루션 사용 정합성 결과(에러 메세지 문자열. 유효할 경우 null)"
	};
	
	protected final String funcName = "QSA_CN_SSNCHK";
	
	protected final String funcDesc = "주민번호 정합성 체크";
	
	@Override
	public String[] getInputNames() {
		
		return inputName;
	}

	@Override
	public String[] getInputTypes() {
		return inputTypes;
	}

	@Override
	public String[] getInputDescriptions() {
		return inputDescriptions;
	}

	@Override
	public String[] getOutputNames() {
		return outputNames;
	}

	@Override
	public String[] getOutputTypes() {
		return outputTypes;
	}

	@Override
	public String[] getOutputDescriptions() {
		return outputDescriptions;
	}

	@Override
	public String getFuncName() {
		return funcName;
	}

	@Override
	public String getFuncDesc() {
		return funcDesc;
	}

	@Override
	public void cleanse(Map<String, Object> input, Map<String, Object> output) {
		LoggerHelper.debugLog(logger, "=== Cleanse Function [{}] Start ===",funcName);
		
		StringBuilder ssnNumber = new StringBuilder();
		ssnNumber.append(input.get(inputName[0])).append(input.get(inputName[1]));
		boolean isValid = checkSSN(ssnNumber.toString());
		
		output.put(outputNames[0], input.get(inputName[0]));
		output.put(outputNames[1], input.get(inputName[1]));
		output.put(outputNames[2], isValid);
		
		if(!isValid){
			output.put(outputNames[3], "잘못된 주민등록번호입니다.");
		}
		
		LoggerHelper.debugLog(logger, "=== Cleanse Function [{}] End ===",funcName);
	}
	
	private final int[] ns = {2, 3, 4, 5, 6, 7, 8, 9, 2, 3, 4, 5};
	
	private boolean checkSSN(final String ssnNumber){
		boolean result = false;
		
		//1. regular expression 체크
		final String ssnRegex = "^\\d{6}[1-4]\\d{6}$";
		result = Pattern.matches(ssnRegex, ssnNumber);
		if(result){
			int idx = 0;
			
//			//2. 규칙 검사
//			int[] ssns = new int[ssnNumber.length()];
//			for(char ch : ssnNumber.toCharArray()){
//				ssns[idx++] = ch - '0';
//			}
			
			int total = 0; idx = 0;
//			for(int n : ns){
//				total += ssns[idx] * n;
//			}
//			result = (ssns[ssns.length-1] == ((11 - total % 11) % 10));
			
			for(int n : ns){
				total += ((ssnNumber.charAt(idx++) - '0') * n);
			}
			result = (((int)(ssnNumber.charAt(idx) - '0'))
						== ((11 - total % 11) % 10));
			
		}
		return result;
	}

}
