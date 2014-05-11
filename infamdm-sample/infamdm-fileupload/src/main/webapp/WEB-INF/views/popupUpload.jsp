<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<form name="uploadForm" id="uploadForm" method="post" enctype="multipart/form-data"></form>
	File : <input type="file" name="file"/><br/>
	Display Name : <input type="text" name="attachment.display_name" /><br/>
	Stored Path : <input type="text" name="attachment.stored_path"/><br/>
	BO Name : <input type="text" name="attachment.bo_name"/><br/>
	BO Rowid : <input type="text" name="attachment.bo_rowid"/><br/>
</body>
</html>