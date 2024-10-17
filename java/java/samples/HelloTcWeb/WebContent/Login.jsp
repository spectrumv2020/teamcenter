<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>HelloTcWeb - Login</title>
</head>
<body>

<fieldset style="display:inline;margin-left:auto;margin-right:auto">
<legend>Login </legend>
<form method="POST" action="Action.jsp">
<table BORDER="0">
<tr>
	<td>User name :</td>
	<td><INPUT TYPE="TEXT" NAME="USERNAME" SIZE="20"></td>
</tr>
<tr>
	<td>Password :</TD>
	<td><INPUT TYPE="PASSWORD" NAME="PASSWORD" SIZE="20"></td>
</tr>
<tr>
	<td COLSPAN=2><INPUT TYPE="SUBMIT" VALUE="LOGIN" NAME="SUBMIT"></td>
</tr>
</table>
</form>
</fieldset>

<br></br>
<% 
    String errorMsg = (String) request.getAttribute("loginError");
    if (errorMsg != null && errorMsg.length() > 0)
    {
%>
<b style="color:red"><%= errorMsg %></b>
<%
    }
%>

</body>
</html>