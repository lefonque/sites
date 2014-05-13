package com.mdm.cleanse.unit.qsa;

import java.util.Map;
import java.util.regex.Pattern;

import com.mdm.cleanse.base.FunctionUnit;
import com.siperian.mrm.cleanse.api.ParameterTypes;

public class QSA_CN_URLCHK implements FunctionUnit {
	
	protected final String funcName = this.getClass().getSimpleName();
	
	protected final String funcDesc = "URL형식의 유효성 체크용 Cleanse Function";
	
	/**
	 * 입력파라메터 명
	 */
	protected final String[] inputNames = {
			"INPUT_URL"
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
			"입력된 URL 문자열"
	};
	
	/**
	 * 출력파라메터 명
	 */
	protected final String[] outputNames = {
			"INPUT_URL"
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
			"입력된 URL 문자열"
			,"URL형식 유효성 검증 결과"
			,"솔루션에서 Reject처리를 위해 필요한 파라메터(유효하지 않을 경우 출력할 메세지를 설정하며, 유효할 경우 빈값 (\"\" 또는 null)을 설정한다."
	};

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
		final String urlRegex = "(http|https)://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
		final String inputURL = (String)input.get(inputNames[0]);
		boolean isValid = false;
		if(inputURL!=null){
			isValid = Pattern.matches(urlRegex, inputURL);
		}
		
		output.put(outputNames[0], inputURL);
		output.put(outputNames[1], isValid);
		if(!isValid){
			output.put(outputNames[2], "유효하지 않은 URL입니다.");
		}
	}

}
