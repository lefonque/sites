package com.mdm.cleanse.base;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import com.siperian.mrm.cleanse.api.CleanseException;
import com.siperian.mrm.cleanse.api.CleanseFunction;

public class UnifiedCleanseFunction implements CleanseFunction {

	private final AtomicReference<FunctionUnit> unitRef;
	
	public UnifiedCleanseFunction(final FunctionUnit unit) {
		unitRef = new AtomicReference<FunctionUnit>();
		unitRef.set(unit);
	}
	
	@Override
	@SuppressWarnings({"rawtypes","unchecked"})
	public void cleanse(Map context, Map input, Map output) throws CleanseException {
		unitRef.get().cleanse(input, output);
	}
	
	public FunctionUnit getFunctionUnit(){
		return unitRef.get();
	}
}
