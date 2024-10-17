// Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
// ==================================================
// Copyright 2011.
// Siemens Product Lifecycle Management Software Inc.
// All Rights Reserved.
// ==================================================
// Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.

package com.teamcenter.classification;


import com.teamcenter.services.strong.classification._2007_01.Classification.ClassificationObject;
import com.teamcenter.services.strong.classification._2007_01.Classification.ClassificationProperty;
import com.teamcenter.services.strong.classification._2007_01.Classification.ClassificationPropertyValue;
import com.teamcenter.services.strong.classification._2007_01.Classification.CreateClassificationObjectsResponse;
import com.teamcenter.services.strong.classification._2007_01.Classification.UpdateClassificationObjectsResponse;
import com.teamcenter.services.strong.classification.ClassificationService;

import com.teamcenter.clientx.AppXSession;
import com.teamcenter.schemas.soa._2006_03.exceptions.ServiceException;


/**
 *   This sample program create and update the standalone ICO.
 *
 *   The following data must be created before executing the test.
 *      Create a string attribute with ID=4001
 *      Create Class with ID=ICM4001
 *      Add the attribute 4001 to the class ICM4001.
 *      
 *   Refer TDoc for creating classification data.
 */
public class CreateOrUpdateICOSample
    
{
    ClassificationObject[]                  newICOs             = new ClassificationObject[1];
    CreateClassificationObjectsResponse     createICOResponse;
    UpdateClassificationObjectsResponse     updateICOResponse;

    /**
     *  This API create and update the standalone ICO object.
     * @throws ServiceException
     */
    public void testCreateAndUpdateICO()throws ServiceException
    {
        ClassificationService clsService = ClassificationService.getService(AppXSession.getConnection());
        System.out.println("Calling Classification::createClassificationObjects() ...");

        ClassificationPropertyValue[] ico_prop_values1 = new ClassificationPropertyValue[1];
        ClassificationProperty[] ico_props = new ClassificationProperty[1];
        ClassificationObject[] icos = new ClassificationObject[1];

        ico_prop_values1[0] = new ClassificationPropertyValue();
        ico_prop_values1[0].dbValue = "abc";

        ico_props[0] = new ClassificationProperty();
        ico_props[0].attributeId = 4001;
        ico_props[0].values = ico_prop_values1;

        icos[0] = new ClassificationObject();
        icos[0].classId = "ICM4001";
        icos[0].clsObjTag = null;
        icos[0].instanceId = "ICO4007";
        icos[0].properties = ico_props;
        icos[0].unitBase = "METRIC";
        // wsoId is null for standalone ICO.  
        icos[0].wsoId = null;

        // Create the ICO object
        createICOResponse = clsService.createClassificationObjects( icos );
        
        if ( createICOResponse.data.sizeOfPartialErrors() > 0)
            throw new ServiceException( "ClassificationService.createClassificationObjects returned a partial Error.");

        System.out.println("... completed Classification::createClassificationObjects()");
      
        if ( createICOResponse.clsObjs.length > 0 )
        {
            // Set the data on classification property value for updating on ICO
            icos[0].properties[0].values[0].dbValue = "defgh" ;
            // Set the classification object tag to be updated.
            icos[0].clsObjTag = createICOResponse.clsObjs[0].clsObjTag;
    
            updateICOResponse = clsService.updateClassificationObjects( icos );
            System.out.println("... completed Classification::updateClassificationObjects()");
            
            if ( updateICOResponse.data.sizeOfPartialErrors() > 0)
                throw new ServiceException( "ClassificationService.updateClassificationObjects returned a partial Error.");
            

            int objCount = updateICOResponse.clsObjs.length ;
            if ( objCount <= 0 )
            {
                System.out.println("Nothing was done");
            }
            else
            {               
                ClassificationObject[]  updatedICOs = new ClassificationObject[objCount];
                for ( int jnx = 0 ;jnx < objCount ; jnx++ )
                {
                    updatedICOs[jnx] = updateICOResponse.clsObjs[jnx];

                    System.out.println( "Class ID: " + updatedICOs[jnx].classId );
                    System.out.println( "ICO ID: " + updatedICOs[jnx].instanceId );
                    System.out.println( "Unit Base: " + updatedICOs[jnx].unitBase );
                    System.out.println( "WSO Id: " + updatedICOs[jnx].wsoId );
                    for( int attrIndex=0; attrIndex < updatedICOs[jnx].properties.length; attrIndex++)
                    {
                        if( updatedICOs[jnx].properties[attrIndex].attributeId == ico_props[0].attributeId )
                        {
                            System.out.println( " Updated Attr ID: " + ico_props[0].attributeId );
                            System.out.println( "  Attr value: " + updatedICOs[jnx].properties[attrIndex].values[0].dbValue );
                        }
                    }
                 }
            }
                   
        }
    }
}
