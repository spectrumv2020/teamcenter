//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.hellosoap;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import com.teamcenter.schemas.core._2006_03.session.LoginInput;
import com.teamcenter.services.core._2006_03.InvalidCredentialsFaultFault;


/**
 * This class prompts the user for credentials.
 *
 */
public class CredentialManager
{
    private LoginInput  credentials;


    public CredentialManager()
    {
        credentials = new LoginInput();
        credentials.setUsername( null);
        credentials.setPassword( null);
        credentials.setGroup("");    // default group
        credentials.setRole("");    // default role
        credentials.setSessionDiscriminator("SoaSOAP"); // always connect same user
                                                        // to same instance of serve
    }


    /**
     * Prompt's the user for credentials.
     *
     */
    public LoginInput getCredentials(InvalidCredentialsFaultFault e)
    {
        System.out.println(e.getFaultMessage().getInvalidCredentialsFault().getMessage());
        return promptForCredentials();
    }








    public LoginInput promptForCredentials()
    {
        try
        {
            LineNumberReader reader = new LineNumberReader(new InputStreamReader(System.in));
            System.out.println("Please enter user credentials (return to quit):");
            System.out.print("User Name: ");
            credentials.setUsername( reader.readLine());

            if (credentials.getUsername().length() == 0)
                System.exit(0);

            System.out.print("Password:  ");
            credentials.setPassword( reader.readLine());
        }
        catch (IOException e)
        {
            String message = "Failed to get the name and password.\n" + e.getMessage();
            System.out.println(message);
            System.exit(0);
        }


        return credentials;
    }

}
