<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.teamcenter.soa.client.*" %>
<%@ page import="com.teamcenter.clientx.*" %>
<%@ page import="com.teamcenter.webapp.util.*" %>
<%@ page import="com.teamcenter.schemas.soa._2006_03.exceptions.InvalidCredentialsException" %>
<%@ page import="com.teamcenter.schemas.soa._2006_03.exceptions.InvalidUserException" %>
<%@ page import="com.teamcenter.soa.client.model.strong.User" %>
<%@ page import="org.apache.commons.httpclient.HttpState" %>
<%@ page import="com.teamcenter.services.strong.core.SessionService" %>
<%@ page import="com.teamcenter.services.strong.core._2006_03.Session.LoginResponse" %>
<%@ page import="com.teamcenter.services.strong.core._2007_01.Session" %>
<%@ page import="com.teamcenter.soa.exceptions.CanceledOperationException" %>
<%@ page import="java.util.Random" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>HelloTcWeb - Login Action</title>
</head>
<body>

<%! 
	String username, password, host, port, app_name, errorMsg;
    String partialErrorMsg = null;
	SessionService service;
	SoaCredentialManager defaultCredentialMgr;
	User user;
%>

<%	        
    username = request.getParameter("USERNAME");
	password = request.getParameter("PASSWORD");
    host =  application.getInitParameter("host");
    port = application.getInitParameter("port");
	app_name = application.getInitParameter("app_name");
%>

<%!
    private Connection CreateConnection()
	{ 
	   defaultCredentialMgr = new SoaCredentialManager(username ,password);
	   String serverHost = "http://" + host + ":" + port + "/" + app_name;
		   
	   //Initialize the credential Manager.
	   //AppXCredentialManager defaultCredentialManager = new AppXCredentialManager();
	   
	   // Create the Connection object, no contact is made with the server
	   // until a service request is made
	   Connection connection = new Connection(serverHost, new HttpState() , defaultCredentialMgr, "REST",
	                                "HTTP", false);

	    // Add an ExceptionHandler to the Connection, this will handle any
	    // InternalServerException, communication errors, xml marshalling errors, etc
	    TcWebExceptionHandler defaultExceptionHandler = new TcWebExceptionHandler();
	    connection.setExceptionHandler(defaultExceptionHandler);
	    	   
	    // The above ExceptionHandler is required. 
	    // Client application can add as many or as few Listeners of each type 
	    // that they want.

	    // Add a Partial Error Listener, this will be notified when ever a
	    // a service returns partial errors.
	    TcWebPartialErrorListener defaultPartialErrorListener = new TcWebPartialErrorListener();
	    connection.getModelManager().addPartialErrorListener(defaultPartialErrorListener);

	    return connection;
	}

	private User LoginToServer(Connection connection, String sessionDisriminator)
	{
	    try
	    {
	        SessionService sessionSvc = SessionService.getService(connection);
	        String locale = "";
	        InvalidUserException iue = new InvalidUserException("");
            String[] credentials = defaultCredentialMgr.getCredentials(iue);

            //This is used to set the unique session discriminator
            Random random = new Random();
            Integer num = random.nextInt(1000);
            credentials[4] = num.toString();
	        	        
	        LoginResponse logIn = sessionSvc.login(credentials[0], credentials[1], credentials[2],
                    credentials[3], locale, credentials[4]);

	        int parErrCt = logIn.serviceData.sizeOfPartialErrors();
	        if ( parErrCt != 0 )
	        {
	        	for (int i=0; i<parErrCt; i++)
	        	{
	        		partialErrorMsg = partialErrorMsg + logIn.serviceData.getPartialError(i);
	        	}
	        }
	        user = logIn.user;
	        
	        return user; 
	    }
	    catch (InvalidCredentialsException e)
	    {
	    	errorMsg = "Invalid Credentials.Please Enter Again";%>
	    	alert(<%= errorMsg%>);
		    <%!
	    } 
	    catch (Exception e)
	    {
	        errorMsg = e.getMessage();%>
	        alert(<%= errorMsg%>);
	    <%!
	    }
	    return null;
	}%>

<% 

if ( (username.length() > 0 && password.length() > 0) )
{      
    Connection connection = CreateConnection();
    service = SessionService.getService(connection);

    //This is used to set the unique session discriminator
    Random random = new Random();
    int discriminator = random.nextInt(1000);
    String sessionDiscriminator = random.toString();

    user = LoginToServer(connection, sessionDiscriminator);

    if (user != null)
    {
        //Serialize the connection object and uid for further use on
        //next pages.
    	session.setAttribute(user.getUid() + "user_name", user.get_user_name());
    	session.setAttribute(user.getUid() + "connection", connection);
    	session.setAttribute(user.getUid() + "LoginUser", username);
    	session.setAttribute(user.getUid() + "password", password);
    	session.setAttribute(user.getUid() + "discriminator", sessionDiscriminator);

%>
<jsp:forward page="Welcome.jsp">
	<jsp:param name="activeUsername" value="<%=username%>"/>
	<jsp:param name="activeUid" value="<%=user.getUid()%>"/>
</jsp:forward>
<%
    }
}

if (errorMsg != null && errorMsg.length() > 0)
{
	request.setAttribute("loginError", errorMsg);
}
if (partialErrorMsg != null && partialErrorMsg.length() > 0 )
{
	request.setAttribute("PartialError", partialErrorMsg);
}
%>
<jsp:forward page="Login.jsp"/> 

</body>
</html>