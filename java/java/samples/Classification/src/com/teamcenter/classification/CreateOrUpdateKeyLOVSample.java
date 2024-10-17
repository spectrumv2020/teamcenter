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
import com.teamcenter.services.strong.classification.ClassificationService;
import com.teamcenter.soa.client.model.ServiceData;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


import com.teamcenter.clientx.AppXSession;
import com.teamcenter.schemas.soa._2006_03.exceptions.ServiceException;


/**
 *  This class contain API's that create or update the KeyLov definitions.
 * 
 */
public class CreateOrUpdateKeyLOVSample  
{
    
    ClassificationService clsService = ClassificationService.getService(AppXSession.getConnection());

    /**
     * This test create specified keyLov definitions.
     * 
     * @throws ServiceException
     */

  public void testCreateKeyLOVs()throws ServiceException
    {
        System.out.println("Calling the SOA createOrUpdateKeyLOVs");
        KeyLOVDefinition2[] lovDefnitions = new KeyLOVDefinition2[3];
        KeyLOVEntry[] keyLOVEntries = new KeyLOVEntry[3];

        keyLOVEntries[0] =new KeyLOVEntry();
        keyLOVEntries[0].lovKey="6";
        keyLOVEntries[0].lovValue="600";
        keyLOVEntries[0].deprecatedSatus=true;

        keyLOVEntries[1] =new KeyLOVEntry();
        keyLOVEntries[1].lovKey="7";
        keyLOVEntries[1].lovValue="700";
        keyLOVEntries[1].deprecatedSatus=false;

        keyLOVEntries[2] =new KeyLOVEntry();
        keyLOVEntries[2].lovKey="8";
        keyLOVEntries[2].lovValue="800";
        keyLOVEntries[2].deprecatedSatus=false;

        lovDefnitions[0] = new KeyLOVDefinition2();
        lovDefnitions[0].id = "-2894";
        lovDefnitions[0].name = "Test2894";
        lovDefnitions[0].options = 0;
        lovDefnitions[0].keyLovEntries = keyLOVEntries;

        lovDefnitions[1] = new KeyLOVDefinition2();
        lovDefnitions[1].id = "-1000";
        lovDefnitions[1].name = "Test1000";
        lovDefnitions[1].options = 0;
        lovDefnitions[1].keyLovEntries = keyLOVEntries;

        lovDefnitions[2] = new KeyLOVDefinition2();
        lovDefnitions[2].id = "-1234";
        lovDefnitions[2].name = "Test1234";
        lovDefnitions[2].options = 0;
        lovDefnitions[2].keyLovEntries = keyLOVEntries;

        ServiceData data = clsService.createOrUpdateKeyLOVs( lovDefnitions );
        if ( data.sizeOfPartialErrors() > 0 )
            throw new ServiceException( "ClassificationService.createOrUpdateKeyLOVs returned a partial Error.");

        System.out.println("***************************************************");
        System.out.println("********************Key Lov Created****************");
        System.out.println("***************************************************");

     }

  /**
   * This test insert keyLOV entries for the specified KeyLOV definition
   * 
   * @throws ServiceException
   */

  public void testInsertKeyLOVEntry()throws ServiceException
  {
      System.out.println("Calling the SOA createOrUpdateKeyLOVs");
      KeyLOVDefinition2[] lovDefnitions = new KeyLOVDefinition2[3];
      KeyLOVEntry[] keyLOVEntries = new KeyLOVEntry[4];

      keyLOVEntries[0] =new KeyLOVEntry();
      keyLOVEntries[0].lovKey="6";
      keyLOVEntries[0].lovValue="600";
      keyLOVEntries[0].deprecatedSatus=true;

      keyLOVEntries[1] =new KeyLOVEntry();
      keyLOVEntries[1].lovKey="7";
      keyLOVEntries[1].lovValue="700";
      keyLOVEntries[1].deprecatedSatus=false;

      keyLOVEntries[2] =new KeyLOVEntry();
      keyLOVEntries[2].lovKey="8";
      keyLOVEntries[2].lovValue="800";
      keyLOVEntries[2].deprecatedSatus=false;

      keyLOVEntries[3] =new KeyLOVEntry();
      keyLOVEntries[3].lovKey="9";
      keyLOVEntries[3].lovValue="900";
      keyLOVEntries[3].deprecatedSatus=false;

      lovDefnitions[0] = new KeyLOVDefinition2();
      lovDefnitions[0].id = "-2894";
      lovDefnitions[0].name = "Test2894";
      lovDefnitions[0].options = 0;
      lovDefnitions[0].keyLovEntries = keyLOVEntries;

      lovDefnitions[1] = new KeyLOVDefinition2();
      lovDefnitions[1].id = "-1000";
      lovDefnitions[1].name = "Test1000";
      lovDefnitions[1].options = 0;
      lovDefnitions[1].keyLovEntries = keyLOVEntries;

      lovDefnitions[2] = new KeyLOVDefinition2();
      lovDefnitions[2].id = "-1234";
      lovDefnitions[2].name = "Test1234";
      lovDefnitions[2].options = 0;
      lovDefnitions[2].keyLovEntries = keyLOVEntries;

      ServiceData data = clsService.createOrUpdateKeyLOVs( lovDefnitions );
      if ( data.sizeOfPartialErrors() > 0 )
          throw new ServiceException( "ClassificationService.createOrUpdateKeyLOVs returned a partial Error.");

      System.out.println("***************************************************");
      System.out.println("********************Key Entry Inserted****************");
      System.out.println("***************************************************");

  }

  /**
   * This test remove the keyLOV entries for the specified KeyLOV definition
   * 
   * @throws ServiceException
   */
  public void testRemoveKeyLOVEntry()throws ServiceException
  {
      System.out.println("Calling the SOA createOrUpdateKeyLOVs");
      KeyLOVDefinition2[] lovDefnitions = new KeyLOVDefinition2[3];
      KeyLOVEntry[] keyLOVEntries = new KeyLOVEntry[3];

      keyLOVEntries[0] =new KeyLOVEntry();
      keyLOVEntries[0].lovKey="6";
      keyLOVEntries[0].lovValue="600";
      keyLOVEntries[0].deprecatedSatus=true;

      keyLOVEntries[1] =new KeyLOVEntry();
      keyLOVEntries[1].lovKey="7";
      keyLOVEntries[1].lovValue="700";
      keyLOVEntries[1].deprecatedSatus=false;

      keyLOVEntries[2] =new KeyLOVEntry();
      keyLOVEntries[2].lovKey="9";
      keyLOVEntries[2].lovValue="900";
      keyLOVEntries[2].deprecatedSatus=false;

      lovDefnitions[0] = new KeyLOVDefinition2();
      lovDefnitions[0].id = "-2894";
      lovDefnitions[0].name = "Test2894";
      lovDefnitions[0].options = 0;
      lovDefnitions[0].keyLovEntries = keyLOVEntries;

      lovDefnitions[1] = new KeyLOVDefinition2();
      lovDefnitions[1].id = "-1000";
      lovDefnitions[1].name = "Test1000";
      lovDefnitions[1].options = 0;
      lovDefnitions[1].keyLovEntries = keyLOVEntries;

      lovDefnitions[2] = new KeyLOVDefinition2();
      lovDefnitions[2].id = "-1234";
      lovDefnitions[2].name = "Test1234";
      lovDefnitions[2].options = 0;
      lovDefnitions[2].keyLovEntries = keyLOVEntries;

      ServiceData data = clsService.createOrUpdateKeyLOVs( lovDefnitions );
      if ( data.sizeOfPartialErrors() > 0 )
          throw new ServiceException( "ClassificationService.createOrUpdateKeyLOVs returned a partial Error.");

      System.out.println("***************************************************");
      System.out.println("********************Key Lov Entry Removed****************");
      System.out.println("***************************************************");

  }
  /**
   * This test update keyLOV entries for the specified KeyLOV definition
   * 
   * @throws ServiceException
   */
  public void testUpdateKeyLOV()throws ServiceException
  {
      System.out.println("Calling the SOA createOrUpdateKeyLOVs");
      KeyLOVDefinition2[] lovDefnitions = new KeyLOVDefinition2[3];
      KeyLOVEntry[] keyLOVEntries = new KeyLOVEntry[3];

      keyLOVEntries[0] =new KeyLOVEntry();
      keyLOVEntries[0].lovKey="6";
      keyLOVEntries[0].lovValue="60";
      keyLOVEntries[0].deprecatedSatus=true;

      keyLOVEntries[1] =new KeyLOVEntry();
      keyLOVEntries[1].lovKey="7";
      keyLOVEntries[1].lovValue="70";
      keyLOVEntries[1].deprecatedSatus=false;

      keyLOVEntries[2] =new KeyLOVEntry();
      keyLOVEntries[2].lovKey="9";
      keyLOVEntries[2].lovValue="90";
      keyLOVEntries[2].deprecatedSatus=false;

      lovDefnitions[0] = new KeyLOVDefinition2();
      lovDefnitions[0].id = "-2894";
      lovDefnitions[0].name = "Test2894";
      lovDefnitions[0].options = 0;
      lovDefnitions[0].keyLovEntries = keyLOVEntries;

      lovDefnitions[1] = new KeyLOVDefinition2();
      lovDefnitions[1].id = "-1000";
      lovDefnitions[1].name = "Test1000";
      lovDefnitions[1].options = 0;
      lovDefnitions[1].keyLovEntries = keyLOVEntries;

      lovDefnitions[2] = new KeyLOVDefinition2();
      lovDefnitions[2].id = "-1234";
      lovDefnitions[2].name = "Test1234";
      lovDefnitions[2].options = 0;
      lovDefnitions[2].keyLovEntries = keyLOVEntries;

      ServiceData data = clsService.createOrUpdateKeyLOVs( lovDefnitions );
      if ( data.sizeOfPartialErrors() > 0 )
         throw new ServiceException( "ClassificationService.createOrUpdateKeyLOVs returned a partial Error.");

      System.out.println("***************************************************");
      System.out.println("********************Key Lov Updated****************");
      System.out.println("***************************************************");

  }


/**
 * This test get the keyLOV definitions for the specified KeyLOV id's
 * 
 * @throws ServiceException
 */
  public void testGetKeyLOVs2()throws ServiceException
  {
      String[] keyLOVIds = new String[1];
      keyLOVIds[0] = "-2894";
      System.out.println("Calling the SOA getKeyLOVs1");
      com.teamcenter.services.strong.classification._2009_10.Classification.GetKeyLOVsResponse2 response = clsService.getKeyLOVs2( keyLOVIds );
      if ( response.data.sizeOfPartialErrors() > 0 )
         throw new ServiceException( "ClassificationService.getKeyLOVs2 returned a partial Error.");

      Map<String, KeyLOVDefinition2> keyLOVs = response.keyLOVs;
      System.out.println("*****Lov Details*******"+keyLOVs.size());
      Set<String> keys = keyLOVs.keySet();
      for(Iterator<String> itr = keys.iterator();itr.hasNext();)
      {
          String id = itr.next();
          KeyLOVDefinition2 lovDef = keyLOVs.get( id );
          System.out.println("LOV Id          : "+lovDef.id);
          System.out.println("LOV Name        : "+lovDef.name);
          System.out.println("LOV Options     : "+lovDef.options );
          System.out.println("LOV Owning Site : "+lovDef.owningSite.getUid());
          System.out.println("keyLOV Tag      : "+lovDef.keyLovtag.getUid() );
          System.out.println("keyLOV Share Sites : "+lovDef.sharedSites.length);
          KeyLOVEntry[] keyLOVEntries =lovDef.keyLovEntries;

          System.out.println("Size "+keyLOVEntries.length);
          for(int i=0;i<keyLOVEntries.length;i++)
          {
              KeyLOVEntry keyLOVEntries1 = lovDef.keyLovEntries[i];

              System.out.println("LOV Entry Key       : "+keyLOVEntries1.lovKey);
              System.out.println("LOV Entry Value     : "+keyLOVEntries1.lovValue);
              System.out.println("Depricated Status   : "+keyLOVEntries1.deprecatedSatus);

              System.out.println("************************************* ");
              System.out.println("****************Key End************** ");
              System.out.println("************************************* ");

          }
      }

  }

 }
