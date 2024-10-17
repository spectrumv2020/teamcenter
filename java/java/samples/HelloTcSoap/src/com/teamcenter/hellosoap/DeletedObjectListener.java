//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.hellosoap;

import com.teamcenter.schemas.soa._2006_03.base.RefId;
import com.teamcenter.schemas.soa._2006_03.base.ServiceData;




/**
 * Class to check ServiceData for deleted objects
 *
 */
public class DeletedObjectListener
{

    /**
     * Check ServiceData for deleted objects.
     *
     * This is a wrapper to the modelObjectDelete method.
     *
     * @param sd ServiceData object returned from a serivce operation.
     */
    public static void checkForDeletes( ServiceData sd )
    {
        RefId[] ids = sd.getDeletedObjs();
        if(ids != null)
        {
            modelObjectDelete( ids );
        }
    }

    /**
     * WSDL-DIFFERENCE - Delete Listener
     * In the Teamcenter client bindings are integrated with a Delete Listener.
     * This allows the client applications to have an implementation (AppXDeletedObjectListener)
     * of the interface that is automatically notified when any service
     * operation returns one or more Deleted objects
     * In this sample application we have the same AppXDeletedObjectListener class
     * as in the HelloTeamcenter sample, however the application code must
     * call this listener to check for Deleted objects.
     *
     * @param uids  List of UIDs
     */
    private static void modelObjectDelete(RefId[] uids)
    {
        if (uids.length == 0)
            return;

        System.out.println("");
        System.out.println("Deleted Objects handled in com.teamcenter.clientx.AppXDeletedObjectListener.modelObjectDelete");
        System.out.println("The following objects have been deleted from the server:");
        for (int i = 0; i < uids.length; i++)
        {
            System.out.println("    " + uids[i].getUid());
        }

    }

}
