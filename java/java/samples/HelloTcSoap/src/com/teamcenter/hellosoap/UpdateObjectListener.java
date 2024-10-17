//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.hellosoap;

import com.teamcenter.schemas.soa._2006_03.base.ModelObject;
import com.teamcenter.schemas.soa._2006_03.base.RefId;
import com.teamcenter.schemas.soa._2006_03.base.ServiceData;


/**
 * Class to check ServiceData for Updated objects
 *
 */
public class UpdateObjectListener
{

    /**
     * Check ServiceData for Updated objects
     *
     * This is a wrapper to the modelObjectChange method.
     *
     * @param sd        ServiceData object returned from a serivce operation.
     * @param message   A message to display if there was a partial error.
     */
    public static void checkForUpdates( ServiceData sd )
    {
        RefId[] ids = sd.getUpdatedObjs();
        if(ids != null)
        {
            ModelObject[] objects =  ClientDataModel.getObjects( ids );
            modelObjectChange( objects );
        }
    }

    /**
     * WSDL-DIFFERENCE - Change Listener
     * In the Teamcenter client bindings are integrated with a Change Listener.
     * This allows the client applications to have an implementation (AppXUpdateObjectListener)
     * of the interface that is automatically notified when any service
     * operation returns one or more Updated objects
     * In this sample application we have the same AppXUpdateObjectListener class
     * as in the HelloTeamcenter sample, however the application code must
     * call this listener to check for Updated objects.
     *
     * @param objects List of updated objects
     */
    private static void modelObjectChange(ModelObject[] objects)
    {
        if (objects.length == 0) return;
        System.out.println("");
        System.out.println("Modified Objects handled in com.teamcenter.clientx.AppXUpdateObjectListener.modelObjectChange");
        System.out.println("The following objects have been updated in the client data model:");
        for (int i = 0; i < objects.length; i++)
        {
            String uid = objects[i].getUid();
            String type = objects[i].getType();
            String name = "";
            ModelObject wo = objects[i];
            try
            {
                 name = ClientDataModel.getPropertyStringValue( wo, "object_string");
            }
            catch (NotLoadedException e) {} // just ignore
            System.out.println("    " + uid + " " + type + " " + name);
        }
    }

}
