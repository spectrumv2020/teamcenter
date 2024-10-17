//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================
/**
    @file
*/


package com.teamcenter.clientx;

import java.util.List;
import java.util.Vector;

import com.teamcenter.schemas.soa._2006_03.exceptions.InvalidCredentialsException;
import com.teamcenter.schemas.soa._2006_03.exceptions.ServiceException;
import com.teamcenter.services.strong.core.DataManagementService;
import com.teamcenter.services.strong.core.SessionService;
import com.teamcenter.services.strong.core._2006_03.Session.LoginResponse;
import com.teamcenter.soa.SoaConstants;
import com.teamcenter.soa.client.Connection;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.client.model.strong.User;
import com.teamcenter.soa.client.model.strong.WorkspaceObject;
import com.teamcenter.soa.exceptions.CanceledOperationException;
import com.teamcenter.soa.exceptions.NotLoadedException;


public class WHSession
{
    /**
     * Single instance of the Connection object that is shared throughtout
     * the application. This Connection object is needed whenever a Service
     * stub is instantiated.
     */
    private static Connection           connection;

    /**
     * The credentialManager is used both by the Session class and the Teamcenter
     * Services Framework to get user credentials.
     *
     */
    private static WHAppXCredentialManager credentialManager;

    /**
     * Create an instance of the Session with a connection to the specified
     * server.
     *
     * Add implementations of the ExceptionHandler, PartialErrorListener,
     * ChangeListener, and DeleteListeners.
     *
     * @param host      Address of the host to connect to, http://serverName:port/tc
     */

    public WHSession(String host)
    {
        // Create an instance of the CredentialManager, this is used
        // by the SOA Framework to get the user's credentials when
        // challanged by the server (sesioin timeout on the web tier).
        credentialManager = new WHAppXCredentialManager();

        String protocol = (host.startsWith("http"))? SoaConstants.HTTP: SoaConstants.IIOP;

        // Create the Connection object, no contact is made with the server
        // until a service request is made
        connection = new Connection(host, credentialManager, SoaConstants.REST,
                                    protocol);
        connection.setOption(Connection.OPT_USE_COMPRESSION, "false");
        
        // Add an ExceptionHandler to the Connection, this will handle any
        // InternalServerException, communication errors, xml marshalling errors
        // .etc
        connection.setExceptionHandler(new WHAppXExceptionHandler());

        // While the above ExceptionHandler is required, all of the following
        // Listeners are optional. Client application can add as many or as few Listeners
        // of each type that they want.

        // Add a Partial Error Listener, this will be notified when ever a
        // a service returns partial errors.
        connection.getModelManager().addPartialErrorListener(new WHAppXPartialErrorListener());


        // Add a Change and Delete Listener, this will be notified when ever a
        // a service returns model objects that have been updated.
        connection.getModelManager().addModelEventListener(new WHAppXModelEventListener());

        // Add a Request Listener, this will be notified before and after each
        // service request is sent to the server.
        Connection.addRequestListener( new WHAppXRequestListener() );
    }

    /**
     * Get the single Connection object for the application
     *
     * @return  connection
     */
    public static Connection getConnection()
    {
        return connection;
    }

    /**
     * Login to the Teamcenter Server
     *
     */

    public User login()
    {
        // Get the service stub
        SessionService sessionService = SessionService.getService(connection);

        try
        {
            // Prompt for credentials until they are right, or until user
            // cancels
            String[] credentials = credentialManager.promptForCredentials();
            while (true)
            {
                try
                {
                    // *****************************
                    // Execute the service operation
                    // *****************************
                    LoginResponse out = sessionService.login(credentials[0], credentials[1],
                            credentials[2], credentials[3],"", credentials[4]);

                    return out.user;
                }
                catch (InvalidCredentialsException e)
                {
                    credentials = credentialManager.getCredentials(e);
                }
            }
        }
        // User canceled the operation, don't need to tell him again
        catch (CanceledOperationException e) {}

        // Exit the application
        System.exit(0);
        return null;
    }

    /**
     * Terminate the session with the Teamcenter Server
     *
     */
    public void logout()
    {
        // Get the service stub
        SessionService sessionService = SessionService.getService(connection);
        try
        {
            // *****************************
            // Execute the service operation
            // *****************************
            sessionService.logout();
        }
        catch (ServiceException e){}
    }

    /**
     * Print some basic information for a list of objects
     *
     * @param objects
     */
    public static void printObjects(ModelObject[] objects)
    {
        if(objects == null)
            return;
        // Ensure that the referenced User objects that we will use below are loaded
        getUsers( objects );
    }

    private static void getUsers( ModelObject[] objects )
    {
        if(objects == null)
            return;

        DataManagementService dmService = DataManagementService.getService(WHSession.getConnection());

        List<User> unKnownUsers = new Vector<User>();
        for (int i = 0; i < objects.length; i++)
        {
            if(!(objects[i] instanceof WorkspaceObject ))
                continue;

            WorkspaceObject wo = (WorkspaceObject)objects[i];

            User owner = null;
            try
            {
                owner = (User) wo.get_owning_user();
                owner.get_user_name();
            }
            catch (NotLoadedException e)
            {
                if(owner != null)
                    unKnownUsers.add(owner);
            }
        }
        User[] users = (User[])unKnownUsers.toArray(new User[unKnownUsers.size()]);
        String[] attributes = { "user_name" };

        // *****************************
        // Execute the service operation
        // *****************************
        dmService.getProperties(users, attributes);
    }
}
