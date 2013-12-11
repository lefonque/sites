package org.epis.ws.agent.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;

import org.epis.ws.common.entity.MapWrapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 * <pre>
 * <p>Select된 결과를 MapWrapp에 담기 위한 RowMapper 구현체</p>
 * 
 * </pre>
 * @author developer
 *
 */
public class MapWrapperRowMapper implements RowMapper<MapWrapper> {

	public MapWrapper mapRow(ResultSet rs, int rowNum) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		LinkedHashMap<String, Object> mapOfColValues = createColumnMap(columnCount);
		for (int i = 1; i <= columnCount; i++) {
			String key = getColumnKey(JdbcUtils.lookupColumnName(rsmd, i));
			Object obj = getColumnValue(rs, i);
			mapOfColValues.put(key, obj);
		}
		MapWrapper result = new MapWrapper(mapOfColValues);
		return result;
	}

	/**
	 * Create a Map instance to be used as column map.
	 * <p>By default, a linked case-insensitive Map will be created.
	 * @param columnCount the column count, to be used as initial
	 * capacity for the Map
	 * @return the new Map instance
	 * @see org.springframework.util.LinkedCaseInsensitiveMap
	 */
	protected LinkedHashMap<String, Object> createColumnMap(int columnCount) {
		return new LinkedCaseInsensitiveMap<Object>(columnCount);
	}

	/**
	 * Determine the key to use for the given column in the column Map.
	 * @param columnName the column name as returned by the ResultSet
	 * @return the column key to use
	 * @see java.sql.ResultSetMetaData#getColumnName
	 */
	protected String getColumnKey(String columnName) {
		return columnName;
	}

	/**
	 * Retrieve a JDBC object value for the specified column.
	 * <p>The default implementation uses the <code>getObject</code> method.
	 * Additionally, this implementation includes a "hack" to get around Oracle
	 * returning a non standard object for their TIMESTAMP datatype.
	 * @param rs is the ResultSet holding the data
	 * @param index is the column index
	 * @return the Object returned
	 * @see org.springframework.jdbc.support.JdbcUtils#getResultSetValue
	 */
	protected Object getColumnValue(ResultSet rs, int index) throws SQLException {
		Object result = JdbcUtils.getResultSetValue(rs, index);
		
		//CXF에서 XML Escape를 하기 때문에 처리상에 문제 발생함.
		//Object 타입 필드는 MOXy를 써도 CDATA wrapping이 안됨
//		if(result instanceof String){
//			result = "<![CDATA[" + result + "]]>";
//		}
		
		boolean oddByte = false;
		if(result instanceof String){
			String str = String.class.cast(result);
			byte[] temp = str.getBytes();
			for(int i = 0; i < temp.length; i++){
				if(temp[i]==0){
					oddByte = true;
					temp[i] = 32;
				}
			}
			if(oddByte){
				str = new String(temp);
			}
			result = str;
		}
		return result;
	}

}
