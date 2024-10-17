//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.hellosoap;

import com.teamcenter.schemas.soa._2006_03.base.ErrorStack;
import com.teamcenter.schemas.soa._2006_03.base.ErrorValue;
import com.teamcenter.schemas.soa._2006_03.base.ServiceData;
import com.teamcenter.services.core._2006_03.ServiceFaultFault;


/**
 * Class to check ServiceData for Partial Errors
 *
 */
public class PartialErrorListener
{



    /**
     * Check ServiceData for Partial Errors.
     *
     * This is a wrapper to the handlePartialError method.
     *
     * @param sd        ServiceData object returned from a service operation.
     * @param message   A message to display if there was a partial error.
     *
     * @throws ServiceFaultFault    If there are any partial errors
     */
    public static void checkForErrors( ServiceData sd, String message )
        throws ServiceFaultFault
    {
        ErrorStack[] stacks = sd.getPartialErrors();
        if(stacks != null)
        {
            handlePartialError( stacks );
            throw new ServiceFaultFault( message );
        }
    }

    /**
     * WSDL-DIFFERENCE - Partial Error Listener
     * In the Teamcenter client bindings are integrated with a Partial Error Listener.
     * This allows the client applications to have an implementation (AppXPartialErrorListener)
     * of the interface that is automatically notified when any service
     * operation returns one or more Partial Errors
     * In this sample application we have the same AppXPartialErrorListener class
     * as in the HelloTeamcenter sample, however the application code must
     * call this listener to check for partial errors.
     *
     * @param stacks List of Partial Errors
     *
     */
    private static void handlePartialError(ErrorStack[] stacks)
    {
        if (stacks.length == 0) return;

        System.out.println("");
        System.out.println("*****");
        System.out.println("Partial Errors caught in com.teamcenter.clientx.AppXPartialErrorListener.");


        for (int i = 0; i < stacks.length; i++)
        {
            ErrorValue[] errors = stacks[i].getErrorValues();
            System.out.print("Partial Error for ");

            // The different service implementation may optionally associate
            // an ModelObject, client ID, or nothing, with each partial error
            if (stacks[i].getUid() != null)
            {
                System.out.println( "object " + stacks[i].getUid()  );
            }
            else if (stacks[i].getClientId() != null)
            {
                System.out.println( "client id " + stacks[i].getClientId()  );
            }
            else if (stacks[i].getClientIndex() != null)
                System.out.println( "client index " + stacks[i].getClientIndex().toString()  );


            // Each Partial Error will have one or more contributing error messages
            for (int j = 0; j < errors.length; j++)
            {
                System.out.println("    Code: " + errors[j].getCode() + "\tSeverity: "
                        + errors[j].getLevel() + "\t" + errors[j].getMessage());
            }
        }

    }

}
