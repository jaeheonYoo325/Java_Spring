<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>회원 로그인</h1>
	
	<form:form	modelAttribute="membersVO"
				method="post" 
				action="/HelloSpring/member/login">
		<div>
			<ul>
				<li><form:errors path="email" /></li>
				<li><form:errors path="password" /></li>
			</ul>
		</div>				
		<div>
			<input type="email" name="email" placeholder="Email" />
		</div>
		<div>
			<input type="password" name="password" placeholder="Password" />
		</div>
		<div>
			<input type="submit" value="로그인" />
		</div>
	</form:form>
</body>
</html>