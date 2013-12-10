package org.epis.ws.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.PreDestroy;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * <p>Runtime 클래스를 이용하여 Command를 실행하는 클래스</p>
 * 
 * Command를 실행한 결과는 CMDMessageCollector 클래스를 사용하여 출력문자열을 수집한다.
 * 
 * </pre>
 * 
 * @see org.epis.ws.common.utils.RuntimeExecutor.CMDMessageCollector
 * @author developer
 *
 */
@Component
public class RuntimeExecutor {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 콘솔상에서 명령어 실행 후의 표시되는 문자열 수집처리를 하는 Thread를 실행시키는 Executor
	 */
	protected final ExecutorService execService = Executors.newCachedThreadPool();

	/**
	 * 어플리케이션이 Shutdown될 때, 본 클래스의 파괴처리를 기술한다.
	 */
	@PreDestroy
	public void destroy(){
		execService.shutdownNow();
		logger.debug("DESTROYED ExecutorService");
	}
	
	@Value("#{agentProp['consumer.charset']}")
	private String charsetName;
	
	/**
	 * <pre>
	 * <p>NT의 CMD에서 명령어를 실행한다.</p>
	 * 
	 * Process의 waitFor() 으로 실행이 완료되기를 기다리며,
	 * 최종적으로는 Process에서 취득한 stream을 close하며, Process를 파괴한다.
	 * </pre>
	 * @param rt
	 * @param cmd
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public Map<ConstEnum,List<String>> executeCMD(String cmd)
			throws IOException, InterruptedException, ExecutionException {
		Process process = null;
		Reader reader = null;
		Reader failReader = null;
		
		Map<ConstEnum,List<String>> result = new HashMap<ConstEnum,List<String>>();
		try {
			process = Runtime.getRuntime().exec(cmd);
	
			reader = new BufferedReader(new InputStreamReader(process.getInputStream(), charsetName));
			failReader = new BufferedReader(new InputStreamReader(process.getErrorStream(),charsetName));
			
			//콘솔창에 명령어를 실행한 후에 성공처리되어 표시되는 문자열을 수집한다.
			List<String> lines = gatherResultString(reader);
			result.put(ConstEnum.SUCCESS,lines);
			for(String line : lines){
				logger.debug("Rumtime Process SUCCESS : [{}]",line);
			}
			
			//콘솔창에 명령어를 실행한 후에 실패처리되어 표시되는 문자열을 수집한다.
			lines = gatherResultString(failReader);
			result.put(ConstEnum.FAIL,lines);
			for(String line : lines){
				logger.debug("Rumtime Process FAIL : [{}]",line);
			}
			
			process.waitFor();
		} finally {
			if(process!=null){
				process.destroy();	
			}
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(failReader);
		}
		return result;
	}

	/**
	 * <pre>
	 * 콘솔창에 명령어를 실행한 후에 표시되는 문자열을 수집한다.
	 * 문자열 수집처리는 별도의 Thread를 생성하여 취득한다.
	 * </pre>
	 * @param reader
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	protected List<String> gatherResultString(final Reader reader)
			throws UnsupportedEncodingException, InterruptedException, ExecutionException {
	
		CMDMessageCollector collector = new CMDMessageCollector(reader);
		Future<List<String>> future = execService.submit(collector);
		List<String> lines = future.get();
		
		return lines;
	}

	public String getMessageListAsString(List<String> msgList){
		StringBuilder result = new StringBuilder();
		if(CollectionUtils.isNotEmpty(msgList)){
			for(String line : msgList){
				result.append(line).append(IOUtils.LINE_SEPARATOR);
			}
		}
		return result.toString();
	}
	
	/**
	 * <pre>
	 * 별도의 Thread상에서 문자열 수집을 처리하는 Callable 인터페이스 구현체
	 * RuntimeExecutor내 ExecutorService 인스턴스를 이용하여 실행됨
	 * 
	 * Thread와 동일한 용도이나, 데이터 리턴이 가능함.
	 * </pre>
	 * @see org.epis.ws.common.utils.RuntimeExecutor
	 * @author developer
	 *
	 */
	private static final class CMDMessageCollector implements Callable<List<String>> {
		
		Reader reader;
		
		CMDMessageCollector(final Reader reader){
			this.reader = reader;
		}

		@Override
		public List<String> call() throws Exception {
			List<String> result = IOUtils.readLines(reader);
			return result;
		}
	}
}


