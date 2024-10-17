// Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
// ==================================================
// Copyright 2011.
// Siemens Product Lifecycle Management Software Inc.
// All Rights Reserved.
// ==================================================
// Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.

package com.teamcenter.classification;

import com.teamcenter.clientx.AppXSession;
import com.teamcenter.services.strong.classification.ClassificationService;
import com.teamcenter.services.strong.classification._2007_01.Classification.GetKeyLOVsResponse;
import com.teamcenter.services.strong.classification._2007_01.Classification.KeyLOVDefinition;
import com.teamcenter.schemas.soa._2006_03.exceptions.ServiceException;



import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *  This sample program get the keyLOV definitions for the specified key LOV id's.
 *  
 *  The following data must be created before executing this test.
 *     Create KeyLov with ID=-2894
 *     Add set of key value entries to -2894
 *     
 *  Refer TDoc for classification data creation. 
 *  
 */
public class GetKeyLOVsSample
    
{
        
   /**
   * get the KeyLOV definitions from keyLOV ID's.
   * 
   * @throws ServiceException
   */
    public void testGetKeyLOVs1()throws ServiceException
    {
        String[] keyLOVIds = new String[1];
        keyLOVIds[0] = "-2894";

        System.out.println("Calling the SOA getKeyLOVs");
        ClassificationService clsService = ClassificationService.getService(AppXSession.getConnection());
        GetKeyLOVsResponse response = clsService.getKeyLOVs( keyLOVIds );
        if ( response.data.sizeOfPartialErrors() > 0)
            throw new ServiceException( "ClassificationService.getKeyLOVs returned a partial Error.");


        Map<String, KeyLOVDefinition> keyLOVs = response.keyLOVs;
        System.out.println("*****LOV Details******* ");
        Set<String> keys = keyLOVs.keySet();
        for(Iterator<String> itr = keys.iterator();itr.hasNext();)
        {
            String id = itr.next();
            KeyLOVDefinition lovDef = keyLOVs.get( id );
            System.out.println("KeyLOV ID: " + id.toString());
            System.out.println("KeyLOV Name: "+lovDef.name);
            System.out.println("*************************************************************");

            Map<String,String> keyLovEntries= lovDef.keyValuePairs;
            Set<String> keySet = keyLovEntries.keySet();
            for ( Iterator<String> iter = keySet.iterator();iter.hasNext();)
            {
                String key = iter.next();
                System.out.println("KeyLOV Entry Key     : "+key );
                System.out.println("KeyLOV Entry Value   : "+keyLovEntries.get(key));
                System.out.println("*************************************************************");
            }
            
        }
    }
}
