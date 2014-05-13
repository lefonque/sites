package com.mdm.cleanse.unit.qsb;

import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.mdm.cleanse.base.FunctionUnit;
import com.mdm.cleanse.base.LoggerHelper;
import com.siperian.mrm.cleanse.api.ParameterTypes;

public class QSB_CN_PTNTNO implements FunctionUnit {
	
	private final Logger logger = Logger.getLogger(QSB_CN_PTNTNO.class);

	/**
	 * 입력파라메터 명
	 */
	protected final String[] inputNames = {
			"PTNO"
	};
	
	/**
	 * 입력파라메터 타입
	 */
	protected final String[] inputTypes = {
			ParameterTypes.STRING
	};
	
	/**
	 * 입력파라메터 설명
	 */
	protected final String[] inputDescriptions = {
			"환자고유번호"
	};
	
	/**
	 * 출력파라메터 명
	 */
	protected final String[] outputNames = {
			"PTNO"
			,"CHK_RES"
			,"validationStatus"
	};
	
	/**
	 * 출력파라메터 타입
	 */
	protected final String[] outputTypes = {
			ParameterTypes.STRING
			,ParameterTypes.BOOLEAN
			,ParameterTypes.STRING
	};
	
	protected final String[] outputDescriptions = {
			"환자고유번호"
			,"주민번호 정합성 체크결과(true:유효함,false:유효하지 않음)"
			,"솔루션 사용 정합성 결과(에러 메세지 문자열. 유효할 경우 null)"
	};
	
	protected final String funcName = "QSB_CN_PTNTNO";
	
	protected final String funcDesc = "환자고유번호 정합성 체크";

	@Override
	public String[] getInputNames() {
		return inputNames;
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
		String validationStatus = "";
		
		
		final String ptno = (String)input.get(inputNames[0]);
		boolean isValid = checkPTNO(ptno);
		if(!isValid){
			validationStatus = "유효하지 않은 환자번호입니다.";
			output.put(outputNames[2], validationStatus);
		}

		output.put(outputNames[0], input.get(outputNames[0]));
		output.put(outputNames[1], isValid);
		
		LoggerHelper.debugLog(logger, "=== Cleanse Function [{}] End ===",funcName);
	}
	
	private boolean checkPTNO(final String ptno){
		boolean isValid = false;
		//1. 8자리
		if(ptno.length()==8){
			//2. P : 수탁환자, R : 임상연구 => 년도2자리 + Serial 5자리
			String regex = "^(P|R)\\d{7}$";
			isValid = Pattern.matches(regex, ptno);
			if(!isValid){
				int[] weights = {1,3,7,1,3,7,1};
				int total = 0, idx = 0;
				for(int w : weights){
					total += w * ((int)(ptno.charAt(idx++) - '0'));
				}
				isValid = (((int)(ptno.charAt(idx)-'0'))
								== ((11 - total % 11) % 10));
			}
		}
		return isValid;
	}

}
