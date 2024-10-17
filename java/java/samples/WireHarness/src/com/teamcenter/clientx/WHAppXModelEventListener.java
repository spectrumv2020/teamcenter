//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.clientx;


import com.teamcenter.soa.client.model.ModelEventListener;
import com.teamcenter.soa.client.model.ModelObject;

/**
 * Implementation of the ChangeListener. Print out all objects that have been updated.
 *
 */
public class WHAppXModelEventListener extends ModelEventListener
{

    @Override
    public void localObjectChange(ModelObject[] objects)
    {

    }

    @Override
    public void localObjectDelete(String[] uids)
    {
        if (uids.length == 0)
            return;
    }

}
