//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================


package com.teamcenter.hellosoap;


public class NotLoadedException extends Exception
{

    private static final long serialVersionUID = 1L;

    public NotLoadedException( String propertyName )
    {
        super("The property "+propertyName+" was not downloaded with the last service reqeust.");
    }
}

