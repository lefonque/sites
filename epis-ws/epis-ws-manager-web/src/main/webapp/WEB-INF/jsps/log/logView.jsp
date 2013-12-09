<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href='<c:url value="/resources/css/ui-lightness/jquery-ui-1.10.3.custom.css"/>'>
<link rel="stylesheet" type="text/css" media="screen" href='<c:url value="/resources/css/ui.jqgrid.css"/>' />
<link rel="stylesheet" type="text/css" media="screen" href='<c:url value="/resources/css/sample/screen.css"/>' />


<script type="text/javascript" src='<c:url value="/resources/js/jquery-1.10.2.js"/>'></script>
<script type="text/javascript" src='<c:url value="/resources/js/jquery-ui-1.10.3.custom.js"/>'></script>
<script type="text/javascript" src='<c:url value="/resources/js/i18n/grid.locale-en.js"/>'></script>
<script type="text/javascript" src='<c:url value="/resources/js/jquery.jqGrid.src.js"/>'></script>
<script type="text/javascript" src='<c:url value="/resources/js/jquery.validate.js"/>'></script>
<script type="text/javascript" src='<c:url value="/resources/js/additional-methods.js"/>'></script>
<script type="text/javascript" src='<c:url value="/resources/js/date.format.js"/>'></script>
<!-- 
<script type="text/javascript" src='<c:url value="/resources/js/i18n/message_ko.js"/>'></script>
 -->
<script type="text/javascript">


var intervalId = null;
$(window).unload(function(){
	if(intervalId!=null){
		window.clearInterval(intervalId);
	}
});


var agentValidator = null, agentJobValidator = null;
$(document).ready(function() {
	initGrid();
});





/**
 * Configuration Grid 및 Schedule Grid를 설정함.
 */
function initGrid() {
	//=================================================================
	//	Configuration List표시용 Grid
	//=================================================================
	jQuery("#gridLog").jqGrid(
	{
		url : '<c:url value="/config/logList"/>'
		,mtype : 'POST'
		,datatype : "json"
		,colNames : [ 'Log ID', 'Agent ID','Job ID', '작업명', 'Row수', 'Result Flag', '처리결과', '처리일자']
		,colModel : [ 
			{ name : 'logId', index : 'log_id', width : 120 }
			,{ name : 'agentId', index : 'agent_id', width : 120 }
			,{ name : 'jobId', index : 'job_id', width : 66 }
			,{ name : 'jobName', index : 'job_name', width : 100}
			,{ name : 'rowCount', index : 'row_count', width : 100 }
			,{ name : 'resultFlag', index : 'result_flag', width : 70 }
			,{ name : 'resultFlagText', index : 'result_flag_text', width : 70 }
			,{ name : 'createDate', index : 'create_date'
				,formatter : datetimeFormatter, width : 100 }
		]
		,rowNum : 10
		,rowList : [ 10, 20, 30 ]
		,pager : '#pagerLogGrid'
		,sortname : 'create_date'
		,viewrecords : true
		,sortorder : "desc"
		,caption : "Log 리스트"
		,jsonReader : {
			root: "root"
			,repeatitems: false
			,_search: "search"
		}
		,loadComplete: handleLoadComplete_GridLog
	});
	jQuery("#gridLog").jqGrid('navGrid', '#pagerLogGrid', {
		edit : false
		,add : false
		,del : false
		,search: true
		,refresh: true
		//,addfunc:function(){
		//	$('#dialogAgent').dialog("open");
		//}
	});
	jQuery("#gridLog").jqGrid('hideCol', ["resultFlag"]);
}

/**
 * Grid 내 날짜표시컬럼에서의 날짜값 표시처리
 *
 *	=> 	서버에서 java.util.Date의 값을 millisec으로 보내기 때문에 
 *		그에 대한 처리가 필요함.
 *		서버에서 보낸 값을 변경하지 않기 위해, 서버에서 보낸 값이 bind된 컬럼은 hide하고,
 *		별도의 컬럼을 추가하여 화면표시 용도로 사용한다. 
 */
function datetimeFormatter(cellValue,option,rowObject){
	 var result = '';
	 if(cellValue!=null){
		 result = new Date(cellValue).format('yyyy-mm-dd HH:MM');
	 }
	return result;
}

/**
 * Log Data를 Polling한다.
 */
function handleLoadComplete_GridLog(){
	if(intervalId!=null){
		window.clearInterval(intervalId);
	}
	intervalId = setInterval(function(){
		$("#gridLog").trigger("reloadGrid");
	}, 30000);
}

</script>
<title>Configuration List</title>
</head>
<body>
	<!-- ======================================================================================== -->
	<!-- Log List 표시부 : Grid -->
	<!-- ======================================================================================== -->
	<div id="divLog">
		<h1>Log 목록</h1>
		<table id="gridLog"></table>
		<div id="pagerLogGrid"></div>
	</div>
	
</body>
</html>