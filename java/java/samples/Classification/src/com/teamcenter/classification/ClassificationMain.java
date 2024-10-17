// Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
// ==================================================
// Copyright 2011.
// Siemens Product Lifecycle Management Software Inc.
// All Rights Reserved.
// ==================================================
// Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.

package com.teamcenter.classification;

import com.teamcenter.clientx.AppXSession;


/**
 * This sample client application demonstrates some of the basic features of the
 * Teamcenter classification services.
 *
 * An instance of the Connection object is created with implementations of the
 * ExceptionHandler, PartialErrorListener, ChangeListener, and DeleteListeners
 * interfaces. This client application performs the following functions:
 * 1. Establishes a session with the Teamcenter server
 * 2. Perform the following tasks.
 *      Create or Update ICO objects.
 *      Create or Update keyLOV definitions.
 *      Get class attributes from the class ID.
 *      Get the class descriptions from the class ID.
 *      Get the KeyLOV definitions from the key LOV Id.
 *
 */
public class ClassificationMain
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

        try 
        {     
            // Establish a session with the Teamcenter Server
            session.login();
            // testCreateAndUpdateICO demonstrate creating and updating stand alone ICO objects
            CreateOrUpdateICOSample creOrUpdateICO = new CreateOrUpdateICOSample();
            creOrUpdateICO.testCreateAndUpdateICO();
        
            // CreateOrUpdateKeyLOVSample demonstrate creating , obtaining , 
            CreateOrUpdateKeyLOVSample creOrUpdateKeyLov = new CreateOrUpdateKeyLOVSample();
            creOrUpdateKeyLov.testCreateKeyLOVs();       
            creOrUpdateKeyLov.testGetKeyLOVs2();       
            creOrUpdateKeyLov.testInsertKeyLOVEntry();      
            creOrUpdateKeyLov.testRemoveKeyLOVEntry();      
            creOrUpdateKeyLov.testUpdateKeyLOV();
            
            // Get Attributes from a class ID
            GetAttributesForClasses2Sample  getAttrForClasses2 = new GetAttributesForClasses2Sample();
            getAttrForClasses2.testgetGetAttributesForClasses();
        
            // Get class descriptions from a class ID
            GetClassDescriptionsSample  getClassDesc =new  GetClassDescriptionsSample() ;
            getClassDesc.testGetClassDescription();
        
            //Get KeyLov definitions from KeyLOV id.
            GetKeyLOVs2Sample  getKeyLovs2 = new GetKeyLOVs2Sample() ;
            getKeyLovs2.testGetKeyLOVs2();
        
            // Get KeyLOV definitions from KeyLOV id.
            GetKeyLOVsSample  getKeyLovs = new GetKeyLOVsSample ();
            getKeyLovs.testGetKeyLOVs1();
              
        }catch (Exception ex)
        {
           System.out.println( ex.getMessage());
           return ;
           
        }
        // Terminate the session with the Teamcenter server
        session.logout();

    }
}
