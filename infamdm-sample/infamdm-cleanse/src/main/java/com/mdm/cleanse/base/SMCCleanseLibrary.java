package com.mdm.cleanse.base;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.siperian.common.util.TypedProps;
import com.siperian.mrm.cleanse.api.CleanseException;
import com.siperian.mrm.cleanse.api.CleanseFunction;
import com.siperian.mrm.cleanse.api.CleanseLibrary;
import com.siperian.mrm.cleanse.api.CleanseLibraryDescriptor;

public class SMCCleanseLibrary implements CleanseLibrary {

	private final ConcurrentHashMap<String, UnifiedCleanseFunction> mapFunction;
	private final FuncMetaHelper metaHelper;
	private final CleanseLibraryDescriptor descriptor;
	
	private static final String PROP_PATH = "/META-INF/cleanse.properties";
	private static final String PROP_KEY_LIB_NAME = "library.name";
	private static final String PROP_KEY_LIB_DESC = "library.desc";
	private static final String PROP_KEY_LIB_MAJOR_VER = "library.major.ver";
	private static final String PROP_KEY_LIB_MINOR_VER = "library.minor.ver";
	private static final String PROP_KEY_FUNC_PKG = "functionUnit.package";
	
	public SMCCleanseLibrary() {
		mapFunction = new ConcurrentHashMap<String, UnifiedCleanseFunction>();
		metaHelper = new FuncMetaHelper();
		descriptor = new CleanseLibraryDescriptor();
	}

	@Override
	public CleanseFunction getCleanseFunction(String funcName) {
		
		return mapFunction.get(funcName);
	}

	@Override
	public CleanseLibraryDescriptor getLibraryDescriptor() {
		
		return descriptor;
	}

	@Override
	public void initialize(Properties prop) throws CleanseException {
		
		Properties config = new Properties();
		InputStream inStream = null;
		inStream = getClass().getResourceAsStream(PROP_PATH);
		try {
			config.load(inStream);
		} catch (IOException e) {
			throw new RuntimeException("Cannot Load cleanse.properties", e);
		}
		
		//Config Library Description
		TypedProps libConf = new TypedProps(config);
		descriptor.setName(libConf.getString(PROP_KEY_LIB_NAME));
		descriptor.setDescription(libConf.getString(PROP_KEY_LIB_DESC));
		descriptor.setDynamic(true);
		descriptor.setMajorVersion(libConf.getStringInt(PROP_KEY_LIB_MAJOR_VER,1));
		descriptor.setMinorVersion(libConf.getStringInt(PROP_KEY_LIB_MINOR_VER,0));
		final String packageName = libConf.getString(PROP_KEY_FUNC_PKG);
		FunctionUnit unit = null;
		try {
			for(Class<? extends FunctionUnit> unitClass : metaHelper.getFuncUnitClasses(packageName)){
				unit = unitClass.newInstance();
				mapFunction.put(unit.getFuncName(),new UnifiedCleanseFunction(unit));
				descriptor.addFunctionDescriptor(metaHelper.getFunctionDescriptor(unit));
			}
		} catch (Exception e) {
			throw new RuntimeException("Initialization Failed",e);
		}
	}

	@Override
	public void shutdown() throws CleanseException {
		// TODO Auto-generated method stub

	}

}
