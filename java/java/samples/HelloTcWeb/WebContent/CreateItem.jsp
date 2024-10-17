<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*" %>
<%@ page import="com.teamcenter.webapp.util.DataManagement" %>
<%@ page import="com.teamcenter.soa.client.*" %>
<%@ page import="com.teamcenter.services.strong.core.*" %>
<%@ page import="com.teamcenter.soa.client.model.*" %>
<%@ page import="com.teamcenter.services.strong.core.DataManagementService" %>
<%@ page import="com.teamcenter.services.strong.core._2006_03.DataManagement.*" %>
<%@ page import="com.teamcenter.clientx.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.teamcenter.services.strong.core._2006_03.DataManagement.CreateItemsOutput"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>HelloTcWeb - Create Item</title>
</head>
<body>
<%
	DataManagement items = new DataManagement();
	String uid = request.getParameter("uid");

	Connection connection = (Connection) session.getAttribute(uid + "connection");
	DataManagementService dmService = DataManagementService.getService(connection);

	//As no listeners are get serialized as a part of Connection object
    //serialization. Calling code needs to set the credential back.
    connection.setCredentialManager(new TcWebCredentialManager());
    String userName = (String) session.getAttribute(uid + "LoginUser");
    String password = (String) session.getAttribute(uid + "password");
    String discriminator = (String) session.getAttribute(uid + "discriminator");

    connection.getCredentialManager().setUserPassword(userName, password, discriminator);

    if (uid == null)
    {
%>
       <jsp:forward page="Login.jsp"/>
<%
    }
    else
    {
    	CreateItemsOutput[] newItems = items.createItems(1, "Item", connection);
        if (newItems != null)
        {
            DisplayItem(newItems);
        }
        else
        {
        %>
            alert("Either No Query Exists or there is error while executing service");
    	<%
        }
    }

%>

<%!
	String[] TableColumns = { "Item     Uid", "ItemRev  Uid" };
	ArrayList<String[]> TableRows = new ArrayList<String[]>();
    com.teamcenter.soa.client.model.strong.Item[] createdItems = null ;

	private void DisplayItem(CreateItemsOutput [] newItems)
    {
        createdItems = new com.teamcenter.soa.client.model.strong.Item[newItems.length];

        for (int i = 0; i < createdItems.length; i++)
        {
            createdItems[i] = newItems[i].item;
            String[] row = { newItems[i].item.getUid(), newItems[i].itemRev.getUid() };
            TableRows.add(row);
        }
    }
%>

<%
	//Set these objects as request attributes for next jsp page.
	session.setAttribute("dataMgmtItems", items);
	session.setAttribute("createdItems", createdItems);
%>

<h3>Create Item</h3>
<table width ="40%" border="2" style="left: 36px; position: relative">
<tr>
<%
    for (int i=0; i < TableColumns.length; i++ )
    {
%>
    <th><%=TableColumns[i]%></th>
<%
    }
%>
</tr>
<%
	for (String[] row : TableRows)
    {
%>
<tr>
	<td><%=row[0]%></td>
	<td><%=row[1]%></td>
</tr>
<%
    }
%>
</table>

<SCRIPT type="text/javascript">

function submitAction(actionName, activeUid)
{
  if ( actionName == "logout" )
  {
     document.forms[0].action="Logout.jsp?uid=" + activeUid;
  }

  if ( actionName == "deleteItem" )
  {
     document.forms[0].action="Delete.jsp?uid=" + activeUid;
  }
  document.forms[0].submit();
}

</SCRIPT>

<form id="theForm" method="POST">
<input type="SUBMIT" name="Delete Item" value="Delete Item" onClick="submitAction('deleteItem','<%=uid%>')"/>
<input type="SUBMIT" name="Logout" value="Logout" onClick="submitAction('logout','<%=uid%>')"/>
</form>
</body>
</html>