<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isErrorPage="true"%>
<% response.setStatus(HttpServletResponse.SC_OK);%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>404 - 资源不存在</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
		<style type="text/css">
			
		</style>
	</head>
	<body>
		<div class="container">
			<div class="jumbotron" style="margin-top: 80px;">
				<h1> 404 !</h1>
				<p>资源不存在</p>
				<p><a class="btn btn-primary btn-lg" href="javascript:window.history.back();" role="button">返回上一级</a></p>
			</div>
		</div>
		<script>
		</script>
	</body>
</html>