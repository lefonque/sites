package org.epis.ws.agent.util;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 현재 convertPagableSelectSQL(String) 메서드만 사용가능함.
 * @author developer
 *
 */
@Component
public class SqlUtil {
	
	@Autowired
	@Qualifier("jobProp")
	private Properties jobProp;
	
	@Value("#{systemProperties['job.id']}")
	private String jobId;
	
	/**
	 * <pre>
	 * <p>select SQL을 paging 가능한 형태로 변환한다.</p>
	 * 
	 * 
	 * </pre>
	 * @param sql
	 * @return
	 */
	public String convertPagableSelectSQL(String sql){
		
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM (SELECT ROWNUM rnum, temp.* FROM (")
			.append(sql)
			.append(") temp) WHERE rnum BETWEEN 1 AND ")
			.append(jobProp.getProperty(jobId + PropertyEnum.JOB_SUFFIX_BATCH_SIZE.getKey()));
		return builder.toString();
	}
	
	protected String convertNamedParameterUpdateSQL(String sql){
		String keyword = "set";
		int pairStartIndex = StringUtils.indexOfIgnoreCase(sql, keyword) + keyword.length();
		String pairPart = sql.substring(pairStartIndex).trim();
		pairPart = replaceForUpdate(pairPart);
		
		StringBuilder builder = new StringBuilder();
		builder.append(sql.substring(0, pairStartIndex)).append(" ").append(pairPart);
		
		return builder.toString();
	}
	
	protected String replaceForUpdate(String sqlPairPart){
		
		char ch = 0;
		String fieldName = null;
		boolean parenthesesOpen = false, readField = true, startKeyword=false;
		
		int fieldStartIndex = 0;
		
		StringBuilder result = new StringBuilder();
		for(int cursor=0; cursor < sqlPairPart.length(); cursor++){
			ch = sqlPairPart.charAt(cursor);
			switch(ch){
			case '\'':
				result.append(sqlPairPart.substring(cursor, sqlPairPart.indexOf("'", cursor+1) + 1));
				cursor = sqlPairPart.indexOf("'", cursor+1);
				break;
				
			case '(':
				parenthesesOpen = true;
				result.append(ch);
				break;
				
			case ')':
				parenthesesOpen = false;
				result.append(ch);
				break;
				
			case 'w':
			case 'W':
				startKeyword = "where".equalsIgnoreCase(sqlPairPart.substring(cursor,cursor+5));
				result.append(ch);
				break;
			case 'a':
			case 'A':
				startKeyword = "and".equalsIgnoreCase(sqlPairPart.substring(cursor,cursor+3));
				result.append(ch);
				break;
				
			case 'o':
			case 'O':
				startKeyword = "or".equalsIgnoreCase(sqlPairPart.substring(cursor,cursor+2));
				result.append(ch);
				break;
				
			case ' ':
				if(startKeyword){
					readField = true;
					fieldStartIndex = cursor+1;
				}
				result.append(ch);
				break;
				
			case '=':
				if(!parenthesesOpen && readField){
					fieldName = sqlPairPart.substring(fieldStartIndex,cursor).trim();
					if(StringUtils.contains(fieldName, '(') && StringUtils.contains(fieldName, ')')){
						fieldName = fieldName.substring(fieldName.indexOf('('), fieldName.indexOf(')'));
					}
					readField = false;
					startKeyword = false;
				}
				result.append(ch);
				break;
				
			case ',':
				if(!parenthesesOpen){
					readField = true;
					fieldStartIndex = cursor+1;
				}
				result.append(ch);
				break;
				
			case '?':
				result.append(":").append(fieldName);
				break;
				
			default:
				result.append(ch);
				break;
			}
		}
		
		return result.toString();
	}
	
	/**
	 * <pre>
	 * <p>
	 * INSERT sql 에서 VALUES 이하부분에 ? 를 사용할 경우에 ?를
	 * ':필드명'으로 변환하는 메서드
	 * <p>
	 * 본메서드를 사용하게 될 경우 함수의 인자 부분에  
	 * ','가 있으면 문제가 발생되므로, 이러한 경우에는 
	 * INSERT sql기재시 반드시 ?대신 ':필드명' 으로 기재하도록 한다.
	 * </p>
	 * @param sql
	 * @return
	 */
	protected String convertInsertSQL(String sql){
		
		int startIdx = sql.indexOf("(") + 1;
		int endIdx = StringUtils.indexOfIgnoreCase(sql, "values");
		if(endIdx == -1){
			throw new RuntimeException("Invalid INSERT SQL");
		}
		String columnPart = sql.substring(startIdx,endIdx);
		columnPart = columnPart.substring(0, columnPart.lastIndexOf(")"));
		String[] columns = StringUtils.split(columnPart, ",");
		
		if(sql.indexOf("?",endIdx) < 0){
			return sql;
		}
		
		startIdx = sql.indexOf("(",endIdx)+1;
		endIdx = sql.lastIndexOf(")");
		String valuePart = sql.substring(startIdx,endIdx);
		valuePart = replaceForInsert(valuePart, columns);
		
		StringBuilder result = new StringBuilder(sql);
		result.replace(startIdx, endIdx, valuePart);
		return result.toString();
		
	}
	
	
	private String replaceForInsert(String sqlValuePart, String[] columns){
		StringBuilder builder = new StringBuilder();
		int columnIndex = 0;
		boolean repeatReplace = false;
		boolean countUp = false;
		for(int cursor = 0; cursor < sqlValuePart.length(); cursor++){
			char ch = sqlValuePart.charAt(cursor);
			//' 가 존재하면 닫히는 '까지 건너뛴다.
			switch(ch){
			case '\'':
				builder.append(sqlValuePart.substring(cursor, (sqlValuePart.indexOf('\'',cursor+1) + 1) ));
				cursor = sqlValuePart.indexOf('\'',cursor+1);
				break;
				
			case '(':
				repeatReplace = true;
				builder.append(ch);
				break;
				
			case ')':
				repeatReplace = false;
				builder.append(ch);
				break;
				
			case '?':
				countUp = true;
				builder.append(":").append(columns[columnIndex]);
				break;
				
			default:
				builder.append(ch);
				break;
			}
			if(!repeatReplace && countUp){
				columnIndex++;
				countUp = false;
			}
		}
		return builder.toString();
	}
	
}
