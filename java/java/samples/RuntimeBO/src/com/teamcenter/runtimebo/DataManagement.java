//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.runtimebo;

import com.teamcenter.clientx.AppXSession;
import com.teamcenter.schemas.soa._2006_03.exceptions.ServiceException;

// Include the Data Management Service Interface
import com.teamcenter.services.strong.core.DataManagementService;

// Input and output structures for the service operations
import com.teamcenter.services.strong.core._2008_06.DataManagement.CreateIn;
import com.teamcenter.services.strong.core._2008_06.DataManagement.CreateResponse;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.client.model.Property;
import com.teamcenter.soa.exceptions.NotLoadedException;

/**
 * Perform different operations in the DataManagementService
 *
 */
public class DataManagement
{

    /**
     *Create an instance of a Runtime Business object
     *Set properties and retrieve the values
     *Exit
     */
    public void createRuntimeBO()
    {
        try
        {
            // Get the service stub
            DataManagementService dmService = DataManagementService.getService(AppXSession.getConnection());
            System.out.println("\nCreating an object of type SRB9Runtimebo1\n");
            
            CreateIn createInput = new CreateIn();

            createInput.clientId = "SampleRuntimeBOclient";
            createInput.data.boName = "SRB9runtimebo1";
            createInput.data.stringProps.put( "srb9StringProp", "MyStringPropVal" );

            CreateResponse createObjResponse = dmService.createObjects( new CreateIn[] { createInput } );
            if ( createObjResponse.output != null && createObjResponse.output.length > 0)
            {
                System.out.println("\nSRB9Runtimebo1 created successfully\n" );
                ModelObject[] objects = new ModelObject[1];
                objects[0] = createObjResponse.output[0].objects[0];
                String[] attrNames = {"srb9StringProp"};
                dmService.getProperties(objects, attrNames );

                Property stringProp = objects[0].getPropertyObject("srb9StringProp");
                if ( stringProp != null  )
                {
                    System.out.println("\nsrb9StringProp value: "+stringProp.getStringValue() );

                }
                else
                {

                    System.out.println("\nUnable to get value for srb9StringProp\n");
                }
            }
            else
            {
                System.out.println("\nCreate failed.\n" );
            }

        }
        catch( NotLoadedException e)
        {
            System.out.println(e.getMessage());
        }
        catch( ServiceException e)
        {
            System.out.println(e.getMessage());
        }
        catch( Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

}
