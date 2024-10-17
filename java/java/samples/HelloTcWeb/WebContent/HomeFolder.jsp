<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.teamcenter.soa.client.model.strong.WorkspaceObject" %>
<%@ page import="com.teamcenter.webapp.util.*" %>
<%@ page import="com.teamcenter.soa.client.*" %>
<%@ page import="com.teamcenter.soa.client.model.ModelObject" %>
<%@ page import="com.teamcenter.soa.client.model.strong.User" %>
<%@ page import="java.util.*" %>
<%@ page import="com.teamcenter.soa.exceptions.NotLoadedException" %>
<%@ page import="com.teamcenter.services.strong.core.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>HelloTcWeb - Home Folder</title>
</head>
<body>

<%
    HomeFolder folder = new HomeFolder();
    WorkspaceObject[] contents = null;
    String uid = request.getParameter("uid");

    Connection connection = (Connection) session.getAttribute(uid + "connection");

    //As no listenrs are get serialized as a part of Connection object
    //serialization. Calling code needs to set the credential back.
    connection.setCredentialManager(new SoaCredentialManager());

    String userName = (String) session.getAttribute(uid + "LoginUser");
    String password = (String) session.getAttribute(uid + "password");
    String discriminator = (String) session.getAttribute(uid + "discriminator");

    connection.getCredentialManager().setUserPassword(userName, password, discriminator);

    if ( uid == null )
    {
%>
       <jsp:forward page="Login.jsp"/>
<%
    }
    else
    {
        contents = folder.listHomeFolder(uid, connection);
        DisplayHomeFolder(contents, connection);
    }

%>

<%!
	String[] TableColumns = { "Name", "Owner", "Last Modified" };
    ArrayList<String[]> TableRows = new ArrayList<String[]>();
    String errorMsg;

	public void DisplayHomeFolder(ModelObject[] objects, Connection connection)
	{
	    if ( objects == null )
	        return;

	    // Ensure that the referenced User objects that we will use below are loaded
	    getUsers(objects, connection);

	    for ( int i = 0; i < objects.length; i++ )
	    {
	        if ( !(objects[i] instanceof WorkspaceObject) )
	            continue;

	        WorkspaceObject wo = (WorkspaceObject)objects[i];
	        try
	        {
	            String name = wo.get_object_string();
	            User owner = (User)wo.get_owning_user();
	            Calendar lastModified = wo.get_last_mod_date();

	            String[] row = { name, owner.get_user_name(), String.format("%1$tm %1$te,%1$tY", lastModified) };
	            TableRows.add(row);
	        }
	        catch (NotLoadedException e)
	        {
	            // Could do a DataManagementService.getProperties call at this point
	        }
	    }
	}

	private void getUsers( ModelObject[] objects, Connection connection )
	{
	    if ( objects == null )
	        return;

	    ArrayList<User> unKnownUsers = new ArrayList<User>();
	    for ( int i = 0; i < objects.length; i++ )
	    {
	        if ( !(objects[i] instanceof WorkspaceObject) )
	            continue;

	        WorkspaceObject wo = (WorkspaceObject)objects[i];

	        User owner = null;
	        try
	        {
	            owner = (User) wo.get_owning_user();
	            String userName = owner.get_user_name();
	        }
	        catch ( NotLoadedException e )
	        {
	            if ( owner != null )
	                unKnownUsers.add(owner);
	        }
	    }
	    User[] users = new User[unKnownUsers.size()];
	    unKnownUsers.toArray(users);
	    String[] attributes = { "user_name" };

	    // *****************************
	    // Execute the service operation
	    // *****************************
        DataManagementService dmService = DataManagementService.getService(connection);
	    dmService.getProperties( users, attributes );
	}
%>

<h3>List Home Folder</h3>
<table width ="40%" border="2" style="left: 36px; position: relative">
<tr>
<%
    for ( int i=0; i < TableColumns.length; i++ )
    {
%>
    <th><%=TableColumns[i]%></th>
<%
    }
%>
</tr>
<%
	Iterator<String[]> iter = TableRows.iterator();

    for (String[] row : TableRows)
    {
%>
<tr>
	<td><%=row[0]%></td>
	<td><%=row[1]%></td>
	<td><%=row[2]%></td>
</tr>
<%
    }
%>
</table>
<form id="form1" method="POST" action="QueryItem.jsp?uid=<%=uid%>">
<input type="SUBMIT" name="Query Items" value="Query Items"/>
</form>
</body>
</html>