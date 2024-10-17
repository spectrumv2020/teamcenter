<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ page import="com.teamcenter.soa.client.*" %>
<%@ page import="com.teamcenter.soa.client.model.strong.Item" %>
<%@ page import="com.teamcenter.services.strong.core.SessionService" %>
<%@ page import="com.teamcenter.webapp.util.DataManagement" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>HelloTcWeb - Delete Item</title>
</head>
<body>

<%
	String uid = request.getParameter("uid");
	Connection connection = (Connection) session.getAttribute(uid + "connection");
    DataManagement items = (DataManagement) session.getAttribute("dataMgmtItems");
    Item[] createdItems = (Item[])session.getAttribute("createdItems");
    items.deleteItems(createdItems, connection);

    //Remove these attributes from session.
    session.removeAttribute("dataMgmtItems");
    session.removeAttribute("createdItems");
%>


<H4>Item Deleted! Please log out from the Teamcenter Server</H4>

<BR>

<form id="form1" method="POST" action="Logout.jsp?uid=<%=uid%>">
<input type="SUBMIT" name="Logout" value="Logout"/>
</form>
</body>
</html>