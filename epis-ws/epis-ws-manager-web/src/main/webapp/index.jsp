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
<style type="text/css">
.no-close .ui-dialog-titlebar-close {display: none }
</style>

<script type="text/javascript" src='<c:url value="/resources/js/jquery-1.10.2.js"/>'></script>
<script type="text/javascript" src='<c:url value="/resources/js/jquery-ui-1.10.3.custom.js"/>'></script>
<script type="text/javascript" src='<c:url value="/resources/js/jquery.validate.js"/>'></script>
<script type="text/javascript" src='<c:url value="/resources/js/additional-methods.js"/>'></script>
<script type="text/javascript" src='<c:url value="/resources/js/date.format.js"/>'></script>
<script type="text/javascript">
var validator = null;
$(document).ready(function(){
	
	validator = $('#formLogin').validate();
	
	
	$('form.cmxform').css({'width':'310px'});
	$('#dialogLogin').dialog({
		height: 210
		,width: 350
		,modal: true
		,dialogClass: 'no-close'
		,show: {
			effect:"blind"
			,duration:500
		}
		,hide:{
			effect:"explode"
			,duration:500
		}
		,buttons: {
			//추가 or 수정 버튼
			"Login": handler_DialogLoginSubmit
		}
	});
	
	$('#dialogAlert').dialog({
		autoOpen: false
		,modal: true
		,buttons: {
			Ok: function() {
				$( this ).dialog( "close" );
			}
		}
		,close: function(){
		}
	});
	<c:if test="${not empty message}">
	$('#alertMessage').text('${message}');
	$('#dialogAlert').dialog('open');
	</c:if>

});

function handler_DialogLoginSubmit(event){
	if(validator.form()){
		$('form').attr('action','<c:url value="/config/login"/>');
		$('form').submit();
	}
}
</script>
<title>WebService Configuration</title>
</head>
<body>
<div id="dialogLogin" title="Login">
	<form id="formLogin" name="formLogin" method="post" class="cmxform">
		<fieldset>
			<p>
				<label for="loginUsername">User ID</label>
				<input type="text" name="loginUsername" id="loginUsername" class="required">
			</p>
			<p>
				<label for="loginPassword">Password</label>
				<input type="password" name="loginPassword" id="loginPassword" class="required">
			</p>
		</fieldset>
	</form>
</div>

<!-- ======================================================================================== -->
<!-- 안내문구 및 경고 메세지 표시용 Dialog -->
<!-- ======================================================================================== -->
<div id="dialogAlert" title="Login Failed">
	<p id="alertMessage"></p>
</div>
</body>
</html>