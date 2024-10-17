<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ page import="com.teamcenter.soa.client.*" %>
<%@ page import="com.teamcenter.services.strong.core.SessionService" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>HelloTcWeb - Logout</title>
</head>
<body>

<%
	String uid = request.getParameter("uid");
	Connection connection = (Connection) session.getAttribute(uid + "connection");
	SessionService service = SessionService.getService(connection);
	service.logout();
%>


<H1>Logout Successful</H1>

<form id="form1" method="POST" action="Login.jsp">
<input type="SUBMIT" name="Login Again" value="Login Again"/>
</form>

</body>
</html>