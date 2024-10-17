//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.tableproperty;

import com.teamcenter.clientx.AppXSession;
import com.teamcenter.schemas.soa._2006_03.exceptions.ServiceException;

// Include the Data Management Service Interface
import com.teamcenter.services.strong.core.DataManagementService;

import com.teamcenter.services.strong.core._2007_01.DataManagement.VecStruct;
// Input and output structures for the service operations
import com.teamcenter.services.strong.core._2008_06.DataManagement.CreateResponse;
import com.teamcenter.services.strong.core._2015_07.DataManagement.CreateIn2;
import com.teamcenter.services.strong.core._2015_07.DataManagement.CreateInput2;
import com.teamcenter.services.strong.core._2006_03.DataManagement.CreateItemsOutput;
import com.teamcenter.services.strong.core._2006_03.DataManagement.CreateItemsResponse;
import com.teamcenter.services.strong.core._2006_03.DataManagement.ItemProperties;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.client.model.Property;
import com.teamcenter.soa.client.model.ServiceData;
import com.teamcenter.soa.exceptions.NotLoadedException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Perform different operations in the DataManagementService and verify the TableProperty updates
 *
 */
public class TableProperty
{
    /*
     * Track the Item Model object 
     */
    ModelObject itemWithTableProperty;
    

    /**
     *Create an item of type A2CustomItem 
     */
    public void createCustomBO() throws ServiceException
    {
         // Get the service stub
        DataManagementService dmService = DataManagementService.getService(AppXSession.getConnection());
        Logger.getLogger( TableProperty.class ).info("\nCreating an object of type A2CustomItem \n" );

        ItemProperties itemProperty = new ItemProperties();
        itemProperty.clientId = "sampeTablePropertyClient" ;
        itemProperty.itemId = "";
        itemProperty.revId = "";
        itemProperty.type =  "A2CustomItem";
        itemProperty.name = "ItemWithTableProperty";
        itemProperty.description = "Test Item for the SOA AppX sample application.";
        itemProperty.uom = "";
        
        CreateItemsResponse response = dmService.createItems(new ItemProperties[]{ itemProperty }, null, "" );
        
        // The AppXPartialErrorListener is logging the partial errors returned
        // In this example if any partial errors occur we will throw a
        // ServiceException
        if (response.serviceData.sizeOfPartialErrors() > 0)
        {
            throw new ServiceException( "DataManagementService.createItems returned a partial error.");
        }
        
        if ( response.output != null && response.output.length > 0)
        {
            Logger.getLogger( TableProperty.class ).info("\n Item created successfully" );
            CreateItemsOutput[] crItemOutput = response.output;
            
            // We have created only one item, so get the item at index zero
            itemWithTableProperty = crItemOutput[0].item;
        }
        else
        {
            Logger.getLogger( TableProperty.class ).info("\n Item creation failed" );
        }
    }
    
    
    /*
     * Add three table rows to the custom item
     */
    public void addTableRows() throws ServiceException
    {
         DataManagementService dmService = DataManagementService.getService(AppXSession.getConnection());
         
         // Create input for creation of table properties
         CreateIn2[] createIn = new CreateIn2[3];

         //Create three structures for table properties
         // Input structure for first table row
         createIn[0] = new CreateIn2();
         createIn[0].clientId = "testCreateTableProperties_1";
         createIn[0].targetObject = itemWithTableProperty;
         createIn[0].pasteProp = "a2SpecProp";
         CreateInput2 tablePropertiesInput_1 = new CreateInput2();
         tablePropertiesInput_1.boName = "A2SpecificationsBO";  // BO for the custom TableRow object 
         tablePropertiesInput_1.propertyNameValues.put( "a2Color", new String[]{"Green"} );
         tablePropertiesInput_1.propertyNameValues.put( "a2Model", new String[]{"VXD"} );
         tablePropertiesInput_1.propertyNameValues.put( "a2Year", new String[]{"2014"} );
         createIn[0].createData = tablePropertiesInput_1;
         
         // Input structure for second table row
         createIn[1] = new CreateIn2();
         createIn[1].clientId = "testCreateTableProperties_2";
         createIn[1].targetObject = itemWithTableProperty;
         createIn[1].pasteProp = "a2SpecProp";
         CreateInput2 tablePropertiesInput_2 = new CreateInput2();
         tablePropertiesInput_2.boName = "A2SpecificationsBO";  // BO for the custom TableRow object 
         tablePropertiesInput_2.propertyNameValues.put( "a2Color", new String[]{"Blue"} );
         tablePropertiesInput_2.propertyNameValues.put( "a2Model", new String[]{"VD"} );
         tablePropertiesInput_2.propertyNameValues.put( "a2Year", new String[]{"2013"} );
         createIn[1].createData = tablePropertiesInput_2;
 
         // Input structure for third table row
         createIn[2] = new CreateIn2();
         createIn[2].clientId = "testCreateTableProperties_2";
         createIn[2].targetObject = itemWithTableProperty;
         createIn[2].pasteProp = "a2SpecProp";
         CreateInput2 tablePropertiesInput_3 = new CreateInput2();
         tablePropertiesInput_3.boName = "A2SpecificationsBO";  // BO for the custom TableRow object 
         tablePropertiesInput_3.propertyNameValues.put( "a2Color", new String[]{"Grey"} );
         tablePropertiesInput_3.propertyNameValues.put( "a2Model", new String[]{"GD"} );
         tablePropertiesInput_3.propertyNameValues.put( "a2Year", new String[]{"2012"} );
         createIn[2].createData = tablePropertiesInput_3;

         // Call the createObjects operation
         CreateResponse createObjResponse;
         createObjResponse = dmService.createRelateAndSubmitObjects2( createIn );
         
         if (createObjResponse.serviceData.sizeOfPartialErrors() > 0)
         {
             throw new ServiceException( "DataManagementService.createRelateAndSubmitObjects2 returned a partial error.");
         }
         
         if ( createObjResponse.output != null && createObjResponse.output.length == 3 )
         {
             Logger.getLogger( TableProperty.class ).info("\n Table rows created successfully" );
         }
         else
         {
             Logger.getLogger( TableProperty.class ).info("\n Creation of Table rows failed" );
         }
    }
    
    /*
     * Retrieve the table rows added to the custom item
     * Update table column values for individual rows
     */
    public void retrieveAndUpdateTableRowValues() throws ServiceException
    {
        DataManagementService dmService = DataManagementService.getService(AppXSession.getConnection());
        
        // Get the table rows stored against the table property
        ModelObject[] objects = new ModelObject[1];
        objects[0] = itemWithTableProperty;
        String[] attrNames = {"a2SpecProp"};
        
        // Call getProperties SOA to retrieve table property values
        dmService.getProperties(objects, attrNames );
        Property tableProp = null;
        try 
        {
            tableProp = objects[0].getPropertyObject("a2SpecProp");
        } 
        catch (NotLoadedException ex )
        {
            Logger.getLogger( TableProperty.class ).error( ex );
            ex.printStackTrace();
        }

        
        // Verify that Table Property has 3 table rows
        if ( tableProp != null )
        {
            ModelObject[] tableRows = tableProp.getModelObjectArrayValue();
            if ( tableRows != null && tableRows.length == 3 )
            {
                // Update the Color and Model attribute on the table row at index 1
                Map<String,VecStruct>  mapAttrVal = new  HashMap<String,VecStruct> ();
                VecStruct  colorValue = new  VecStruct();
                colorValue.stringVec = new  String[] { "Electric Blue" };
                VecStruct modelValue = new VecStruct();
                modelValue.stringVec = new String[] {"G"};
                mapAttrVal.put( "a2Color", colorValue );
                mapAttrVal.put( "a2Model", modelValue );

                // Call the SOA API to set the properties on the table row
                ServiceData svcData = dmService.setProperties(new ModelObject[]{tableRows[1]}, mapAttrVal );
                if (svcData.sizeOfPartialErrors() > 0)
                {
                    throw new ServiceException( "DataManagementService.setProperties returned a partial error.");
                }
            }
            else
            {
                Logger.getLogger( TableProperty.class ).error("\n Error in retrieving table rows on Item ");
            }
        }
    }
    
    /*
     * Delete specified table rows
     */
    public void deleteTableRows() throws ServiceException
    {
        DataManagementService dmService = DataManagementService.getService(AppXSession.getConnection());
        
        // Get the table rows stored against the table property
        ModelObject[] objects = new ModelObject[1];
        objects[0] = itemWithTableProperty;
        String[] attrNames = {"a2SpecProp"};
        
        dmService.getProperties(objects, attrNames );
        Property tableProp = null;
        try 
        {
            tableProp = objects[0].getPropertyObject("a2SpecProp");
        } 
        catch (NotLoadedException ex )
        {
            Logger.getLogger( TableProperty.class ).error( ex );
            ex.printStackTrace();
        }
        
        // Verify that Table Property has 3 table rows
        if ( tableProp != null )
        {
            ModelObject[] tableRows = tableProp.getModelObjectArrayValue();
            if ( tableRows != null && tableRows.length == 3 )
            {
                // Call the SOA API to delete table rows at index 0 and 1
                ServiceData svcData = dmService.deleteObjects( new ModelObject[]{tableRows[0],tableRows[1]} );
                if (svcData.sizeOfPartialErrors() > 0)
                {
                    throw new ServiceException( "DataManagementService.deleteObjects returned a partial error.");
                }
                 
            }
            else
            {
                Logger.getLogger( TableProperty.class ).error("\n Error in retrieving table rows on Item ");
            }
        }
    }
    
    /*
     * Cleanup
     * Delete the custom item
     */
    public void deleteCustomBO() throws ServiceException
    {
        DataManagementService dmService = DataManagementService.getService(AppXSession.getConnection());
        ServiceData svcData = dmService.deleteObjects( new ModelObject[]{itemWithTableProperty} );
        if (svcData.sizeOfPartialErrors() > 0)
        {
            throw new ServiceException( "DataManagementService.deleteObjects returned a partial error.");
        }
        
    
    }

}
