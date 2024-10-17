//==================================================
// 
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.clientx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.teamcenter.schemas.soa._2006_03.exceptions.InvalidCredentialsException;
import com.teamcenter.schemas.soa._2006_03.exceptions.InvalidUserException;
import com.teamcenter.soa.client.CredentialManager;
import com.teamcenter.soa.exceptions.CanceledOperationException;

/**
 * The CredentialManager is used by the Teamcenter Services framework to get the
 * user's credentials when challenged by the server. This can occur after a period
 * of inactivity and the server has timed-out the user's session, at which time
 * the client application will need to re-authenticate. The framework will
 * call one of the getCredentials methods (depending on circumstances) and will
 * send the SessionService.login service request. Upon successful completion of
 * the login service request. The last service request (one that caused the challenge)
 * will be resent.
 *
 * The framework will also call the setUserPassword setGroupRole methods when ever
 * these credentials change, thus allowing this implementation of the CredentialManager
 * to cache these values so prompting of the user is not required for re-authentication.
 *
 */
public class TcWebCredentialManager implements CredentialManager
{
    private String name          = null;
    private String password      = null;
    private String group         = "";          // default group
    private String role          = "";          // default role
    private String discriminator = "SoaAppX";   // always connect same user
                                                // to same instance of server

    @Override
    public int getCredentialType()
    {
        return 0;
    }

    @Override
    public String[] getCredentials(InvalidCredentialsException e)
            throws CanceledOperationException
    {
        System.out.println(e.getMessage());
        return PromptForCredentials();
    }

    @Override
    public String[] getCredentials(InvalidUserException arg0)
            throws CanceledOperationException
    {
        // Have not logged in yet, should not happen but just in case
        if (name == null) return PromptForCredentials();

        // Return cached credentials
        String[] tokens = { name, password, group, role, discriminator };
        return tokens;
    }

    @Override
    public void setGroupRole(String group, String role)
    {
        this.group = group;
        this.role = role;
    }

    @Override
    public void setUserPassword(String user, String password, String discriminator)
    {
        this.name = user;
        this.password = password;
        this.discriminator = discriminator;
    }

    public String[] PromptForCredentials()
        throws CanceledOperationException
    {
        try
        {
            System.out.println("Please enter user credentials (return to quit):");
            System.out.println("");
            System.out.print("User Name: ");

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            name = br.readLine();

            if (name.length() == 0)
                throw new CanceledOperationException("");

            System.out.print("Password: ");

            password = br.readLine();

        }
        catch (IOException e)
        {
            String message = "Failed to get the name and password.\n" + e.getMessage();
            System.out.println(message);
            throw new CanceledOperationException(message);
        }

        String[] tokens = { name, password, group, role, discriminator };
        return tokens;
    }

}
