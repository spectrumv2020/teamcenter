//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.tableproperty;

import com.teamcenter.clientx.AppXSession;




/**
 * This sample client application demonstrates some of the basic features of the
 * Teamcenter Services framework and a few of the services.
 *
 * An instance of the Connection object is created with implementations of the
 * ExceptionHandler, PartialErrorListener, ChangeListener, and DeleteListeners
 * intefaces. This client application performs the following functions:
 * 1. Establishes a session with the Teamcenter server
 * 2. Creates a custom BusinessObject that has TableProperty defined
 * 3. Add 3 table rows
 * 4. Update the values on the created table rows
 * 5. Delete a table row
 * 6. Delete the custom BusinessObject created in Step 1
 *
 */
public class TablePropertyMain
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
                System.out.println("usage: java [-Dhost=http://server:port/tc] com.teamcenter.tableproperty.TableProperty");
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
            TableProperty tp = new TableProperty();

            // Establish a session with the Teamcenter Server
            session.login();
            System.out.println("\nSuccessfully logged into Teamcenter\n");

            // Create a custom Business Object
            tp.createCustomBO();

            // Add table properties to the custom BO
            tp.addTableRows();

            // Update a table row
            tp.retrieveAndUpdateTableRowValues();

            // Delete a table row
            tp.deleteTableRows();

            // Delete the custom BO
            tp.deleteCustomBO();

            // Terminate the session with the Teamcenter server
            session.logout();
        }
        catch( Exception e)
        {
            System.out.println(e.getMessage());
        }

    }







}
