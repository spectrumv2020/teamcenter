//==================================================
// 
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.clientx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.teamcenter.schemas.soa._2006_03.exceptions.InternalServerException;
import com.teamcenter.soa.client.ExceptionHandler;
import com.teamcenter.soa.exceptions.CanceledOperationException;

/**
 * Implementation of the ExceptionHandler. For ConnectionExceptions
 * (server temporarily down, etc.) prompts the user to retry the last request.
 *  For other exceptions convert to a RunTime exception.
 */
public class TcWebExceptionHandler implements ExceptionHandler {

    @Override
    public void handleException(InternalServerException ise)
    {
        System.out.println("");
        System.out.println("*****");
        System.out.println("Exception caught in com.teamcenter.clientx.TcWebExceptionHandler.handleException(InternalServerException).");
        System.out.println("\nThe server returned an internal server error.\n"
                         + ise.getMessage()
                         + "\nThis is most likely the result of a programming error."
                         + "\nA RuntimeException will be thrown.");
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String retry = br.readLine();

            // If yes, return to the calling SOA client framework, where the
            // last service request will be resent.
            if (retry.toLowerCase().equals("y") ||
                retry.toLowerCase().equals("yes"))
                return;
        }
        catch (IOException e)
        {
            System.err.println("Failed to read user response.\nA RuntimeException will be thrown.");
        }
    }

    @Override
    public void handleException(CanceledOperationException coe)
    {
        System.out.println("");
        System.out.println("*****");
        System.out.println("Exception caught in com.teamcenter.clientx.AppXExceptionHandler.handleException(CanceledOperationException).");

        // Expecting this from the login tests with bad credentials, and the
        // AnyUserCredentials class not prompting for different credentials
        try
        {
            throw new Exception(coe.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
