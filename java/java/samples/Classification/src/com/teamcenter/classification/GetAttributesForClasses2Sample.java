// Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
// ==================================================
// Copyright 2011.
// Siemens Product Lifecycle Management Software Inc.
// All Rights Reserved.
// ==================================================
// Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.

package com.teamcenter.classification;


import com.teamcenter.services.strong.classification._2007_01.Classification.ClassAttribute;
import com.teamcenter.services.strong.classification._2007_01.Classification.GetAttributesForClassesResponse;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.teamcenter.clientx.AppXSession;
import com.teamcenter.services.strong.classification.ClassificationService;
import com.teamcenter.schemas.soa._2006_03.exceptions.ServiceException;

/**
 *   This Class get the classification class attributes from the class ID's
 *   
 *   The following data should exist before running this test.
 *        Create a class with ID=ICM4001
 *        Add attributes to ICM4001 from data dictionary.
 *        
 *   Refer TDoc for creating classification data.
 *   
 */
public class GetAttributesForClasses2Sample
    
{
    /**
     * get the classification class attributes from the class ID's.
     * 
     * @throws ServiceException
     */
    public void testgetGetAttributesForClasses()
        throws ServiceException
    {
        ClassificationService clsService = ClassificationService.getService(AppXSession.getConnection());
        String[] classIds = new String[1];
        classIds[0] = "ICM4001";
        System.out.println( "Calling the SOA testgetGetAttributesForClasses..." );
        GetAttributesForClassesResponse response = clsService.getAttributesForClasses( classIds );
        if ( response.data.sizeOfPartialErrors() > 0)
            throw new ServiceException( "ClassificationService.getAttributesForClasses returned a partial Error.");

        ClassAttribute[] clsattr = new ClassAttribute[1];

        Map<String, ClassAttribute[]> attributes = response.attributes;
        System.out.println( "After Call SOA " );

        Set<String> keys = attributes.keySet();
        for( Iterator<String> itr = keys.iterator(); itr.hasNext(); )
        {
            String id = itr.next();
            clsattr = attributes.get(id  );
            
            System.out.println (clsattr);
            for( int i = 0; i < clsattr.length; i++ )
            {

                ClassAttribute clsattrs = clsattr[i];

                System.out.println( "Attr Id: " + clsattrs.id );
                System.out.println( "Name: " + clsattrs.name );
                System.out.println( "shortname: " + clsattrs.shortName );
                System.out.println( "description: " + clsattrs.description );
                System.out.println( "annotation: " + clsattrs.annotation );
                System.out.println( "arraysize: " + clsattrs.arraySize );
                System.out.println( "options: " + clsattrs.options );
                System.out.println( "Attribute Length: " + clsattrs.altFormat.formatLength );
                System.out.println( "Attribute Modifier1: " + clsattrs.altFormat.formatModifier1 );
                System.out.println( "Attribute Modifier2: " + clsattrs.altFormat.formatModifier2 );
                System.out.println( "Attribute Type: " + clsattrs.altFormat.formatType );
                System.out.println( "Annotation: " + clsattrs.annotation );
                System.out.println( "Default Value: " + clsattrs.defaultValue );
                System.out.println( "Config: " + clsattrs.config );
                System.out.println( "Maximum Value: " + clsattrs.maxValue );
                System.out.println( "Minimum Value: " + clsattrs.minValue );
                System.out.println( "Post Config: " + clsattrs.postConfig );
                System.out.println( "Pre Config: " + clsattrs.preConfig );
                System.out.println( "Unit Name: " + clsattrs.unitName );
                System.out.println( "****************************************************" );

            }
            System.out.println( "****************************************************" );

        }

    }
}
