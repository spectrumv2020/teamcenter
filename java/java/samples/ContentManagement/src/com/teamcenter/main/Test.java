//==================================================
//
//  Copyright 2015 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.main;

import com.teamcenter.clientx.AppXSession;


/**
 * This sample client application demonstrates some of the basic features of the
 * Teamcenter Services framework and a few of the services.
 *
 *
 */
public class Test
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
                System.out.println("usage: java [-Dhost=http://server:port/tc] com.teamcenter.main.Test");
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

        AppXSession   session = new AppXSession(serverHost);

        ContentManagement cm = new ContentManagement();

        // Establish a session with the Teamcenter Server
        session.login();        
        
        cm.preComposeCallbackTest(false);
        
        cm.postComposeCallbackTest(false);
        
        // Terminate the session with the Teamcenter server
        session.logout();
    }
}
