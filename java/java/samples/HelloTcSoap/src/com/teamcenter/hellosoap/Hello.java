//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.hellosoap;

import com.teamcenter.schemas.soa._2006_03.base.ModelObject;



/**
 * This sample client application demonstrates the use of the Teamcenter Service WSDLs
 * with client bindings from the Axis2 SOAP engine and toolkit.
 *
 * This client application performs the following functions:
 * 1. Establishes a session with the Teamcenter server
 * 2. Display the contents of the Home Folder
 * 3. Performs a simple query of the database
 * 4. Create, revise, and delete an Item
 *
 */
public class Hello
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
                System.out.println("usage: java [-Dhost=http://server:port/tc] com.teamcenter.hello.Hello");
                System.exit(0);
            }
        }

        // Get optional host information
        String serverHost = "http://localhost:7001/tc";
        String host = System.getProperty("host");
        if (host != null && host.length() > 0)
        {
            serverHost = host;
        }



        Session   session = new Session(serverHost);
        HomeFolder   home = new HomeFolder();
        Query       query = new Query();
        DataManagement dm = new DataManagement();





        // Establish a session with the Teamcenter Server
        ModelObject user = session.login();

        // Using the User object returned from the login service request
        // display the contents of the Home Folder
        home.listHomeFolder(user);

        // Perform a simple query of the database
        query.queryItems();

        // Perform some basic data management functions
        dm.createReviseAndDelete();

        // Terminate the session with the Teamcenter server
        session.logout();

    }







}
