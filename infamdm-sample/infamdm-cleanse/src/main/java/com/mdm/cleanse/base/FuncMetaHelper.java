package com.mdm.cleanse.base;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;

import com.siperian.mrm.cleanse.api.CleanseFunctionDescriptor;
import com.siperian.mrm.cleanse.api.Parameter;

public class FuncMetaHelper {
	
	private static final Logger logger = Logger.getLogger(FuncMetaHelper.class);

	public CleanseFunctionDescriptor getFunctionDescriptor(FunctionUnit unit){
		final CleanseFunctionDescriptor descriptor
			= new CleanseFunctionDescriptor();
		
		final String[] inputNames = unit.getInputNames();
		final String[] inputTypes = unit.getInputTypes();
		final String[] inputDescriptions = unit.getInputDescriptions();
		Parameter param = null;
		for(int i = 0; i < inputNames.length; i++){
			param = new Parameter(inputNames[i],inputTypes[i],inputDescriptions[i]);
			descriptor.addInput(param);
		}
		
		final String[] outputNames = unit.getOutputNames();
		final String[] outputTypes=  unit.getOutputTypes();
		final String[] outputDescriptions = unit.getOutputDescriptions();
		for(int i = 0; i < outputNames.length; i++){
			param = new Parameter(outputNames[i],outputTypes[i],outputDescriptions[i]);
			descriptor.addOutput(param);
		}
		
		descriptor.setName(unit.getFuncName());
		descriptor.setDescription(unit.getFuncDesc());
		return descriptor;
	}
	
	public Set<Class<? extends FunctionUnit>> getFuncUnitClasses(String packageName) throws IOException, ClassNotFoundException, URISyntaxException{
		final Set<Class<? extends FunctionUnit>> result = new HashSet<Class<? extends FunctionUnit>>();
		
		final String packagePath = packageName.replace('.', '/');
		
		URL url = null;
		URI uri = null;
		String scheme = null;
		for(final Enumeration<URL> resources = getClass().getClassLoader().getResources(packagePath);
				resources.hasMoreElements(); ){
			url = resources.nextElement();
			if(url==null){
				continue;
			}
			
			uri = url.toURI();
			scheme = uri.getScheme();
			if("jar".equalsIgnoreCase(scheme)){
				result.addAll(getFuncUnitClassesInJar(
						JarURLConnection.class.cast(url.openConnection()).getJarFile(), packagePath));
			}
			else if("file".equalsIgnoreCase(scheme)){
				
				//앙되요
//				Path dirPath = Paths.get(URLDecoder.decode(url.getPath(), "UTF-8"));
//				Path dirPath = FileSystems.getDefault().getPath(url.getFile());
				
				Path dirPath = null;
				for(FileSystemProvider provider : FileSystemProvider.installedProviders()){
					if(provider.getScheme().equals(uri.getScheme())){
						dirPath = provider.getPath(uri);
						break;
					}
				}
				if(!Files.exists(dirPath) || !Files.isDirectory(dirPath)){
					continue;
				}
				result.addAll(getFuncUnitClassesInDir(Files.newDirectoryStream(dirPath),packageName));
			}
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private Set<Class<? extends FunctionUnit>> getFuncUnitClassesInJar(final JarFile jarFile, final String packagePath) throws ClassNotFoundException{
		final Set<Class<? extends FunctionUnit>> result = new HashSet<Class<? extends FunctionUnit>>();
		
		final String suffix = ".class";
		final ClassLoader classLoader = getClass().getClassLoader();
		
		int endIndex = 0;
		JarEntry entry = null;
		String className = null;
		for(final Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();){
			entry = entries.nextElement();
			if(entry==null){
				continue;
			}
			className = entry.getName();
			endIndex = className.lastIndexOf(suffix);
			if(!className.startsWith(packagePath) || endIndex == -1){
				continue;
			}
			className = className.substring(0,endIndex).replace('/', '.');
			result.add((Class<? extends FunctionUnit>)classLoader.loadClass(className));
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private Set<Class<? extends FunctionUnit>> getFuncUnitClassesInDir(DirectoryStream<Path> dir, final String packageName) throws IOException, ClassNotFoundException{
		final Set<Class<? extends FunctionUnit>> result = new HashSet<Class<? extends FunctionUnit>>();
		
		final StringBuilder builder = new StringBuilder(packageName).append('.');
		final int initLength = builder.length();
		final String suffix = ".class";
		final ClassLoader classLoader = getClass().getClassLoader();
		for(Path path : dir){
			if(Files.isDirectory(path)){
				builder.append(path.getFileName().toString());
				result.addAll(getFuncUnitClassesInDir(Files.newDirectoryStream(path), builder.toString()));
			}
			else{
				if(logger.isDebugEnabled()){
					logger.debug("Path's FileName : " + path.getFileName());
				}
				builder.append(path.getFileName().toString()).delete(builder.lastIndexOf(suffix), builder.length());
				result.add((Class<? extends FunctionUnit>)classLoader.loadClass(builder.toString()));
			}
			builder.setLength(initLength);
		}
		
		return result;
	}
}
