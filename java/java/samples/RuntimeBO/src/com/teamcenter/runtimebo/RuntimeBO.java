//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.runtimebo;

import com.teamcenter.clientx.AppXSession;




/**
 * This sample client application demonstrates some of the basic features of the
 * Teamcenter Services framework and a few of the services.
 *
 * An instance of the Connection object is created with implementations of the
 * ExceptionHandler, PartialErrorListener, ChangeListener, and DeleteListeners
 * intefaces. This client application performs the following functions:
 * 1. Establishes a session with the Teamcenter server
 * 2. Creates a RuntimeBusiness object
 * 3. Sets properties on the new RuntimeBO
 *
 */
public class RuntimeBO
{

    /**
     * @param args   -help or -h will print out a Usage statement
     */
    public static void main(String[] args)
    {
        if (args.length > 0)
        {
            if (args[0].equals("-help") || args[0].equals("-h"))
            {
                System.out.println("usage: java [-Dhost=http://server:port/tc] com.teamcenter.runtimebo.RuntimeBO");
                System.exit(0);
            }
        }

        try
        {
            

            // Get optional host information
            String serverHost = "http://localhost:7001/tc";
            String host = System.getProperty("host");
            if (host != null && host.length() > 0)
            {
                serverHost = host;
            }

            AppXSession   session = new AppXSession(serverHost);
            DataManagement dm = new DataManagement();

            // Establish a session with the Teamcenter Server
            session.login();
            System.out.println("\nSuccessfully logged into Teamcenter\n");
            // Create a Runtime Business Object
            dm.createRuntimeBO();
            // Terminate the session with the Teamcenter server
            session.logout();
        }
        catch( Exception e)
        {
            System.out.println(e.getMessage());
        }

    }







}
