//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.vendormanagement;


import com.teamcenter.clientx.AppXSession;





/**
 * This sample client application demonstrates some of the basic features of the
 * Teamcenter Services framework and a few of the services.
 *
 * An instance of the Connection object is created with implementations of the
 * ExceptionHandler, PartialErrorListener, ChangeListener, and DeleteListeners
 * intefaces. This client application performs the following functions:
 * 1. Establishes a session with the Teamcenter server
 * 2. Display the contents of the Home Folder
 * 3. Performs a simple query of the database
 * 4. Create, revise, and delete an Item
 *
 */
public class VendorManagementMain
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
                System.out.println("usage: java [-Dhost=http://server:port/tc] com.teamcenter.clientX.AppX");
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


        VendorManagement vm = new VendorManagement();


        // Establish a session with the Teamcenter Server
        session.login();


        // VendorManagement Services

        vm.createVendors();

        vm.createBidPackages();

        vm.createLineItems();

        vm.createVendorCommercialParts();

        vm.deleteVendorRoles();

        vm.deleteVendors();

        // Terminate the session with the Teamcenter server
        session.logout();

    }







}
