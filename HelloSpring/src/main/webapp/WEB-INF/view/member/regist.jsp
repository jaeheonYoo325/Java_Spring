<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="/HelloSpring/js/jquery-3.3.1.min.js" type="text/javascript"></script>
<script type="text/javascript">
	$().ready(function() {
		$("#email").keyup(function() {
			
			// Ajax 요청
			//$.post("URL", 요청 파라미터, function(response) {})
			$.post("/HelloSpring/member/check/duplicate" // URL
					, {									 // Request Parameter
						"email": $(this).val()
					}
					, function(response) {				 // Response Call back
						if ( response.duplicated ) {
							// 중복이 된다면
							$("#email-error").slideDown(100);
						}
						else {
							// 중복이 안된다면
							$("#email-error").slideUp(100);
						}
					})
		})
	})
	
</script>
</head>
<body>
	<h1>회원 등록하기</h1>
	
	<form:form 	modelAttribute="membersVO"
				autocomplete="off"
				method="post" 
				action="/HelloSpring/member/regist">
		<div>
			<ul>
				<li><form:errors path="email" /></li>
				<li><form:errors path="name" /></li>
				<li><form:errors path="password" /></li>
			</ul>
		</div>
		<div>
			<input type="email" name="email" id="email" placeholder="Email"/>
			<div id="email-error" style="display: none;">
				이 이메일은 사용할 수 없습니다.
			</div>
		</div>
		
		<div>
			<input type="text" name="name" placeholder="Name"/>
		</div>
		<div>
			<input type="password" name="password" placeholder="Password"/>
		</div>
		<div>
			<input type="submit" value="등록" />
		</div>
	</form:form>
</body>
</html>