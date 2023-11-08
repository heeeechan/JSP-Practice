<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<%
  pageContext.setAttribute("result", "hello");
%>
<body>
  <!-- request에 있는 result 값을 꺼냄 -->
  <%=request.getAttribute("result") %>입니다!
  ${requestScope.result} <br /> <!-- request -->
  ${names[1]} <br />
  ${notice.title} <br />
  ${result} <br /><!-- page -->
  
  ${param.n > 3} <br />
  ${param.n gt 3} <br />
  ${empty param.n} <br /> <!--  "" || null 이면 true / not empty면 "" 아니고 null도 아님 -->
  ${empty param.n ? "값이 비어 있습니다." : param.n} <br />
  ${param.n / 2} <br />
  
  ${header.accept}
</body>
</html>
