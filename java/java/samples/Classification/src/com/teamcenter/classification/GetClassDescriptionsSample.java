// Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
// ==================================================
// Copyright 2011.
// Siemens Product Lifecycle Management Software Inc.
// All Rights Reserved.
// ==================================================
// Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.

package com.teamcenter.classification;

import com.teamcenter.services.strong.classification._2007_01.Classification.ClassDef;
import com.teamcenter.services.strong.classification._2007_01.Classification.ClassificationObject;
import com.teamcenter.services.strong.classification._2007_01.Classification.GetClassDescriptionsResponse;
import com.teamcenter.services.strong.classification._2007_01.Classification.SearchForClassesResponse;



import java.util.Map;

import com.teamcenter.clientx.AppXSession;
import com.teamcenter.services.strong.classification.ClassificationService;
import com.teamcenter.schemas.soa._2006_03.exceptions.ServiceException;

/**
 *   This Class get the classification class descriptions from the class ID's
 *   
 *   The following data should exist before running this test.
 *        Create a class with ID=ICM4001
 *        Add attributes to ICM4001 from data dictionary.
 *        
 *   Refer TDoc for creating classification data.
 *   
 */
public class GetClassDescriptionsSample
    
{
    
    ClassificationObject[]                  newICOs             = new ClassificationObject[1];
    ClassificationObject[]                  foundICOs           = new ClassificationObject[1];
    SearchForClassesResponse                searchClassResponse;
    GetClassDescriptionsResponse            classDescResponse;
    ClassDef                                classDescription;

   
  

    /**
     * @throws java.lang.ServiceException
     * 
     */
    public void testGetClassDescription()throws ServiceException
    {
        // Get Class Descriptions -------------------------------------------------------------------
        String [] classIDs = new String[2];
        classIDs[0] = "ICM";
        classIDs[1] = "ICM4001";

        ClassificationService clsService = ClassificationService.getService(AppXSession.getConnection());
        System.out.println("\t Calling Classification::getClassDescriptions() ...");       
        classDescResponse = clsService.getClassDescriptions( classIDs );
        if ( classDescResponse.data.sizeOfPartialErrors() > 0)
            throw new ServiceException( "ClassificationService.getClassDescriptions returned a partial Error.");

        System.out.println("\t ... completed Classification::getClassDescriptions()");


        Map<String, ClassDef> classDescriptionsMap = classDescResponse.descriptions;
        

        System.out.println("\t Class Details -");
        for( String key : classDescriptionsMap.keySet())
         {
            ClassDef clsattr = classDescriptionsMap.get(key);           

            System.out.println("****** Details for " + key + " ******");
            System.out.println( "Attr Id       : " + clsattr.id );
            System.out.println( "Name          : " + clsattr.name );
            System.out.println( "Short Name    : " + clsattr.shortName );
            System.out.println( "Description   : " + clsattr.description );

            System.out.println( "Is Abstract?  : " + clsattr.options.isAbstract );
            System.out.println( "Is Assembly?  : " + clsattr.options.isAssembly );
            System.out.println( "Is Group?     : " + clsattr.options.isGroup );
            System.out.println( "Is Valid?     : " + clsattr.options.isValid );
            System.out.println( "Attr Format   : " + clsattr.unitBase );
            System.out.println( "User Data     : " + clsattr.userData1 );
            System.out.println( "User Data     : " + clsattr.userData2 );
            System.out.println( "Child Count   : " + clsattr.childCount );
            System.out.println( "Instance Count: " + clsattr.instanceCount );
            System.out.println( "Parent        : " + clsattr.parent );
            System.out.println( "View Count    : " + clsattr.viewCount );
        }

        System.out.println("\t ... completed testGetClassDescription()");
    }

}
