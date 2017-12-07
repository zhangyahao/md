<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

您的账号激活成功，3秒后将会跳转到登录页，如果未跳转，
<a href="${path }/home/login.html">点击这里去登录页</a>
<script type="text/javascript">
window.setTimeout(function(){
	window.location.href='${path }/home/login.html';
},3000);

</script>
</body>
</html>