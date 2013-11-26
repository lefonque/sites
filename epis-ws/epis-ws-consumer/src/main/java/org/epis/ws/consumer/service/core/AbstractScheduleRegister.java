package org.epis.ws.consumer.service.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.PreDestroy;

import org.apache.commons.io.IOUtils;
import org.epis.ws.common.utils.ConstEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class AbstractScheduleRegister {


	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 콘솔상에서 명령어 실행 후의 표시되는 문자열 수집처리를 하는 Thread를 실행시키는 Executor
	 */
	protected final ExecutorService execService = Executors.newCachedThreadPool();


	@Autowired
	@Qualifier("jobProp")
	protected Properties jobProp;
	
	@Autowired
	@Qualifier("agentProp")
	protected Properties agentProp;
	
	/**
	 * 어플리케이션이 Shutdown될 때, 본 클래스의 파괴처리를 기술한다.
	 */
	@PreDestroy
	public void destroy(){
		execService.shutdownNow();
		logger.debug("DESTROYED ExecutorService");
	}
	
	public abstract Map<ConstEnum,List<String>> registerSchedule(String[] jobNames) throws Exception;
	
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
	protected Map<ConstEnum,List<String>> processCmd(Runtime rt, String cmd)
			throws IOException, InterruptedException, ExecutionException {
		Process process = null;
		Reader reader = null;
		Reader failReader = null;
		
		Map<ConstEnum,List<String>> result = new HashMap<ConstEnum,List<String>>();
		try {
			process = rt.exec(cmd.toString());
			String charset = agentProp.getProperty("consumer.charset");
			reader = new BufferedReader(new InputStreamReader(process.getInputStream(), charset));
			failReader = new BufferedReader(new InputStreamReader(process.getErrorStream(),charset));
			
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
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(failReader);
			process.destroy();
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
	
	/**
	 * <pre>
	 * 별도의 Thread상에서 문자열 수집을 처리하는 Callable 클래스.
	 * Thread와 동일한 용도이나, 데이터 리턴이 가능함.
	 * </pre>
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


