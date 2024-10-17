// Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
// ==================================================
// Copyright 2011.
// Siemens Product Lifecycle Management Software Inc.
// All Rights Reserved.
// ==================================================
// Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.

package com.teamcenter.classification;

import com.teamcenter.services.strong.classification._2009_10.Classification.KeyLOVDefinition2;
import com.teamcenter.services.strong.classification._2009_10.Classification.KeyLOVEntry;


import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.teamcenter.clientx.AppXSession;
import com.teamcenter.services.strong.classification.ClassificationService;
import com.teamcenter.schemas.soa._2006_03.exceptions.ServiceException;

/**
 *  This sample program get the keyLOV definitions for the specified key LOV id's.
 *  
 *  The following data must be created before executing this test.
 *     Create KeyLov with ID=-2894
 *     Add set of key value entries to -2894
 *     
 *  Refer TDoc for classification data creation
 *  
 */

public class GetKeyLOVs2Sample  
{
      
   /**
   * get the KeyLOV definitions from keyLOV ID's.
   * 
   * @throws ServiceException
   */
    public void testGetKeyLOVs2()throws ServiceException
    {
        ClassificationService clsService = ClassificationService.getService(AppXSession.getConnection());
        String[] keyLOVIds = new String[1];
        keyLOVIds[0] = "-2894";
        System.out.println("Calling the SOA getKeyLOVs2");
        com.teamcenter.services.strong.classification._2009_10.Classification.GetKeyLOVsResponse2 response = clsService.getKeyLOVs2( keyLOVIds );
        if ( response.data.sizeOfPartialErrors() > 0)
           throw new ServiceException( "ClassificationService.getKeyLOVs2 returned a partial Error.");

        Map<String, KeyLOVDefinition2> keyLOVs = response.keyLOVs;
        System.out.println("*****Lov Details******* "+keyLOVs.size());
        Set<String> keys = keyLOVs.keySet();
        for(Iterator<String> itr = keys.iterator();itr.hasNext();)
        {
            String id = itr.next();
            KeyLOVDefinition2 lovDef = keyLOVs.get( id );
            System.out.println("KeyLOV Id             : "+lovDef.id);
            System.out.println("KeyLOV Name           : "+lovDef.name);
            System.out.println("KeyLOV Options        : "+lovDef.options);
            System.out.println("KeyLOV Owning Site    : "+lovDef.owningSite.getUid());
            System.out.println("KeyLOV tag         : "+lovDef.keyLovtag.getUid());
            System.out.println("KeyLOV Share Sites : "+lovDef.sharedSites);
            KeyLOVEntry[] keyLOVEntries =lovDef.keyLovEntries;

            System.out.println("Number of KeyLOV's : "+keyLOVEntries.length);
            System.out.println("*************************************************************");
            for(int i=0;i<keyLOVEntries.length;i++)
            {
                KeyLOVEntry keyLOVEntries1 = lovDef.keyLovEntries[i];
                System.out.println("KeyLOV Entry Key     : "+keyLOVEntries1.lovKey);
                System.out.println("KeyLOV Entry Value   : "+keyLOVEntries1.lovValue);
                System.out.println("Deprecated Status : "+keyLOVEntries1.deprecatedSatus);
                System.out.println("*************************************************************");
            }
        }

    }
}
