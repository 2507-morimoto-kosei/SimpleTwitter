<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>つぶやき編集画面</title>
		<link href="./css/style.css" rel="stylesheet" type="text/css">
	</head>
	<body>
		<div class="header">
			<c:if test="${ not empty loginUser }">
				<a href="./">ホーム</a>
				<a href="setting">設定</a>
				<a href="logout">ログアウト</a>
			</c:if>
		</div>

		<c:if test="${ not empty loginUser }">
			<div class="profile"></div>
			<div class="name"><h2><c:out value="${loginUser.name}"/></h2></div>
			<div class="account">@<c:out value="${loginUser.account}"/></div>
        	<div class="description"><c:out value="${loginUser.description}"/></div>
		</c:if>

		<div class="main-contents">
			<c:if test="${ not empty errorMessages }">
				<div class="errorMessages">
					<ul>
						<c:forEach items="${errorMessages}" var="errorMessage">
							<li><c:out value="${errorMessage}" />
						</c:forEach>
					</ul>
				</div>
			</c:if>

			<!-- ここでのmessageはtop.jspで編集ボタンを押したメッセージ -->
			<div class="edit-area">
				<form action="edit" method="post">
					つぶやき<br>
					<textarea name="editText" cols="100" rows="5" class="tweet-box">${message.text}</textarea><br />
					<input name="editTextId" value="${message.id}" id = "id" type="hidden">
					<input type="submit" value="更新">（140文字まで）
				</form>
			</div>
			<a href="./">戻る</a>
		</div>
		<div class="copyright">Copyright(c)森本晃生</div>
	</body>
</html>