<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isErrorPage="true"%>
<% response.setStatus(HttpServletResponse.SC_OK);%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>403 - 权限不足</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
		<style type="text/css">
			
		</style>
	</head>
	<body>
		<div class="container">
			<div class="jumbotron" style="margin-top: 80px;">
				<h1> 403 !</h1>
				<p>您没有权限访问此页面</p>
				<p><a class="btn btn-primary btn-lg" href="javascript:window.history.back();" role="button">返回上一级</a> <a class="btn btn-danger btn-lg" href="<%=request.getContextPath() %>/login.html" role="button">换个账户试试</a></p>
			</div>
		</div>
		<script>
		</script>
	</body>
</html>