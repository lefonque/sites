package org.epis.ws.manager.entity;

import java.util.List;
/*_search=true
nd=1386307226951
rows=10
page=1
sidx=create_date
sord=desc
searchField=job_id
searchString=JOB-2
searchOper=eq
filters=
 * 
 */
public class JQGridVO {
	private int page;//페이지
	private int rows;//한페이지에 표시될 레코드수
	private String sidx;//sort할 컬럼
	private String sord;//asc, desc
	private long nd;//서버에 요청한 time
	private boolean _search;//조건검색을 하였는가
	
	private String searchField;
	private String searchString;
	private String searchOper;
	
	private List<?> root;
	private int total;
	private int records;
	
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public String getSidx() {
		return sidx;
	}
	public void setSidx(String sidx) {
		this.sidx = sidx;
	}
	public String getSord() {
		return sord;
	}
	public void setSord(String sord) {
		this.sord = sord;
	}
	public long getNd() {
		return nd;
	}
	public void setNd(long nd) {
		this.nd = nd;
	}
	public boolean is_search() {
		return _search;
	}
	public void set_search(boolean _search) {
		this._search = _search;
	}
	public String getSearchField() {
		return searchField;
	}
	public String getSearchString() {
		return searchString;
	}
	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
	public String getSearchOper() {
		return searchOper;
	}
	public void setSearchOper(String searchOper) {
		this.searchOper = searchOper;
	}
	public List<?> getRoot() {
		return root;
	}
	public int getTotal() {
		return total;
	}
	public int getRecords() {
		return records;
	}
	public void setRoot(List<?> root) {
		this.root = root;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public void setRecords(int records) {
		this.records = records;
	}
	
}
