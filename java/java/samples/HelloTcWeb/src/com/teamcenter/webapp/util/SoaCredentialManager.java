//==================================================
// 
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.webapp.util;

import com.teamcenter.schemas.soa._2006_03.exceptions.InvalidCredentialsException;
import com.teamcenter.schemas.soa._2006_03.exceptions.InvalidUserException;
import com.teamcenter.soa.client.CredentialManager;
import com.teamcenter.soa.exceptions.CanceledOperationException;

public class SoaCredentialManager implements CredentialManager
{
    private String name;
    private String password;
    private String group;
    private String role;
    private String discriminator = "Net-Utest";

    public SoaCredentialManager()
    {
    }

    public SoaCredentialManager(String username, String pass)
    {
        name        = username;
        password    = pass;
        group       = "";
        role        = "";
    }

    @Override
    public int getCredentialType()
    {
        return 0;
    }

    @Override
    public String[] getCredentials(InvalidCredentialsException invalidCred)
            throws CanceledOperationException
    {
        CanceledOperationException ex = new CanceledOperationException(invalidCred.getMessage());
        throw ex;
    }

    @Override
    public String[] getCredentials(InvalidUserException arg0)
            throws CanceledOperationException
    {
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

}
