//==================================================
// 
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.hellosoap;

import java.math.BigInteger;
import java.rmi.RemoteException;

import com.teamcenter.schemas.core._2006_03.datamanagement.AttributeNameValueMap;
import com.teamcenter.schemas.core._2006_03.datamanagement.CreateItemsInput;
import com.teamcenter.schemas.core._2006_03.datamanagement.CreateItemsOutput;
import com.teamcenter.schemas.core._2006_03.datamanagement.CreateItemsResponse;
import com.teamcenter.schemas.core._2006_03.datamanagement.DeleteObjectsInput;
import com.teamcenter.schemas.core._2006_03.datamanagement.ExtendedAttributes;
import com.teamcenter.schemas.core._2006_03.datamanagement.GenerateItemIdsAndInitialRevisionIdsInput;
import com.teamcenter.schemas.core._2006_03.datamanagement.GenerateItemIdsAndInitialRevisionIdsProperties;
import com.teamcenter.schemas.core._2006_03.datamanagement.GenerateItemIdsAndInitialRevisionIdsResponse;
import com.teamcenter.schemas.core._2006_03.datamanagement.GenerateRevisionIdsInput;
import com.teamcenter.schemas.core._2006_03.datamanagement.GenerateRevisionIdsProperties;
import com.teamcenter.schemas.core._2006_03.datamanagement.GenerateRevisionIdsResponse;
import com.teamcenter.schemas.core._2006_03.datamanagement.GetPropertiesInput;
import com.teamcenter.schemas.core._2006_03.datamanagement.IndexToIdMap;
import com.teamcenter.schemas.core._2006_03.datamanagement.IndexToRevIdMap;
import com.teamcenter.schemas.core._2006_03.datamanagement.ItemIdsAndInitialRevisionIds;
import com.teamcenter.schemas.core._2006_03.datamanagement.ItemProperties;
import com.teamcenter.schemas.core._2006_03.datamanagement.ItemRevMap;
import com.teamcenter.schemas.core._2006_03.datamanagement.ItemRevPropertyMap;
import com.teamcenter.schemas.core._2006_03.datamanagement.ReviseInput;
import com.teamcenter.schemas.core._2006_03.datamanagement.ReviseProperties;
import com.teamcenter.schemas.core._2006_03.datamanagement.ReviseResponse;
import com.teamcenter.schemas.core._2006_03.datamanagement.RevisionIds;
import com.teamcenter.schemas.core._2007_01.datamanagement.CreateFormsOutput;
import com.teamcenter.schemas.core._2007_01.datamanagement.CreateOrUpdateFormsInput;
import com.teamcenter.schemas.core._2007_01.datamanagement.CreateOrUpdateFormsResponse;
import com.teamcenter.schemas.core._2007_01.datamanagement.FormAttributesInfo;
import com.teamcenter.schemas.core._2007_01.datamanagement.FormInfo;
import com.teamcenter.schemas.core._2007_01.datamanagement.GetItemCreationRelatedInfoInput;
import com.teamcenter.schemas.core._2007_01.datamanagement.GetItemCreationRelatedInfoResponse;
import com.teamcenter.schemas.soa._2006_03.base.ModelObject;
import com.teamcenter.schemas.soa._2006_03.base.ServiceData;
import com.teamcenter.schemas.soa._2006_03.exceptions.ServiceException;
import com.teamcenter.services.core._2006_03.Core0603DataManagementService;
import com.teamcenter.services.core._2007_01.Core0701DataManagementService;



/**
 * Perform different operations in the DataManagementService
 *
 */
public class DataManagement
{
    // WSDL-DIFFERENCE - Client Data Model
    // In the Teamcenter client bindings, client applications are allowed
    // to pass null for a ModelObject input argument, the bindings will convert
    // those nulls to a ModelObject with the reserved UID of "AAAAAAAAAAAAAA".
    // Applications using the WSDL bindings must
    static final ModelObject NULL_OBJECT = new ModelObject();
    static
    {
        NULL_OBJECT.setUid("AAAAAAAAAAAAAA");
    }


    /**
     * Perform a sequence of data management operations: Create Items, Revise
     * the Items, and Delete the Items
     *
     */
    public void createReviseAndDelete()
    {
        try
        {
            int numberOfItems = 3;

            // Reserve Item IDs and Create Items with those IDs
            ItemIdsAndInitialRevisionIds[] itemIds = generateItemIds(numberOfItems, "Item");
            CreateItemsOutput[] newItems = createItems(itemIds, "Item");

            // Copy the Item and ItemRevision to separate arrays for further
            // processing
            ModelObject[] items    = new ModelObject[newItems.length];
            ModelObject[] itemRevs = new ModelObject[newItems.length];
            for (int i = 0; i < items.length; i++)
            {
                items[i] = newItems[i].getItem();
                itemRevs[i] = newItems[i].getItemRev();
            }

            // Reserve revision IDs and revise the Items
            IndexToRevIdMap[] allRevIds = generateRevisionIds(items);
            reviseItems(allRevIds, itemRevs);

            // Delete all objects created
            deleteItems(items);
        }
        catch (com.teamcenter.services.core._2006_03.ServiceFaultFault e)         { System.out.println( e.getFaultMessage().getServiceFault().getMessages()[0]);}
        catch (com.teamcenter.services.core._2006_03.InvalidUserFaultFault e)     { System.out.println( e.getFaultMessage().getInvalidUserFault().getMessage());}
        catch (com.teamcenter.services.core._2006_03.InternalServerFaultFault e)  { System.out.println( e.getFaultMessage().getInternalServerFault().getMessages()[0]);}
        catch (com.teamcenter.services.core._2007_01.ServiceFaultFault e)         { System.out.println( e.getFaultMessage().getServiceFault().getMessages()[0]);}
        catch (com.teamcenter.services.core._2007_01.InvalidUserFaultFault e)     { System.out.println( e.getFaultMessage().getInvalidUserFault().getMessage());}
        catch (com.teamcenter.services.core._2007_01.InternalServerFaultFault e)  { System.out.println( e.getFaultMessage().getInternalServerFault().getMessages()[0]);}
        catch( RemoteException e )                                                { System.out.println( e.getMessage());}

    }

    /**
     * Reserve a number Item and Revision Ids
     *
     * @param numberOfIds      Number of IDs to generate
     * @param type             Type of IDs to generate
     *
     * @return An array of Item and Revision IDs. The size of the array is equal
     *         to the input numberOfIds
     *
     * @throws ServiceException   If any partial errors are returned
     */
    public ItemIdsAndInitialRevisionIds[] generateItemIds(int numberOfIds, String type)
      throws com.teamcenter.services.core._2006_03.ServiceFaultFault,
             com.teamcenter.services.core._2006_03.InvalidUserFaultFault,
             com.teamcenter.services.core._2006_03.InternalServerFaultFault,
             RemoteException
    {
        // Get the service stub
        Core0603DataManagementService dmService = (Core0603DataManagementService)Session.createServiceStub( Core0603DataManagementService.class );

        GenerateItemIdsAndInitialRevisionIdsProperties[] properties = new GenerateItemIdsAndInitialRevisionIdsProperties[1];
        GenerateItemIdsAndInitialRevisionIdsProperties property = new GenerateItemIdsAndInitialRevisionIdsProperties();

        property.setCount( new BigInteger( String.valueOf(numberOfIds)));
        property.setItemType( type );
        property.setItem( NULL_OBJECT ); // Not used
        properties[0] = property;
        GenerateItemIdsAndInitialRevisionIdsInput in = new GenerateItemIdsAndInitialRevisionIdsInput();
        in.setInput(properties);

        // *****************************
        // Execute the service operation
        // *****************************
        GenerateItemIdsAndInitialRevisionIdsResponse response = dmService.generateItemIdsAndInitialRevisionIds(in);
        ClientDataModel.addObjects( response.getServiceData() );

        // WSDL-DIFFERENCE - Partial Error Listener
        // In the Teamcenter client bindings are integrated with a Partial Error Listener.
        // This allows the client applications to have an implementation (AppXPartialErrorListener)
        // of the interface that is automatically notified when any service
        // operation returns one or more Partial Errors
        // In this sample application we have the same AppXPartialErrorListener class
        // as in the HelloTeamcenter sample, however the application code must
        // call this listener to check for partial errors.
        PartialErrorListener.checkForErrors( response.getServiceData(), "DataManagementService.generateItemIdsAndInitialRevisionIds returned a partial error.");

        // The return is a map of ItemIdsAndInitialRevisionIds keyed on the
        // 0-based index of requested IDs. Since we only asked for IDs for one
        // data type, the map key is '0'
        // WSDL-DIFFERENCE - HashMaps
        // The Teamcenter client bindings make extensive use of the HashMap class
        // in operation input and output data structures. WSDL and XSD do not define
        // a data type equivalent to a HashMap. In the WSDL client bindings these
        // types are represented as Arrays of Key/Value pairs. So where the
        // applications based on Teamcenter client bindings can fast random access
        // to the Key/Values, WSDL based application must loop through the Array to
        // find the desired Key/Value.
        BigInteger bIkey = new BigInteger("0");
        IndexToIdMap[] allNewIds = response.getOutputItemIdsAndInitialRevisionIds();
        ItemIdsAndInitialRevisionIds[] myNewIds = null;
        for (int i = 0; i < allNewIds.length; i++)
        {
            if(allNewIds[i].getKey().equals(bIkey))
            {
                myNewIds = allNewIds[i].getValue();
            }

        }

        return myNewIds;
    }




    /**
     * Create Items
     *
     * @param itemIds        Array of Item and Revision IDs
     * @param itemType       Type of item to create
     *
     * @return Set of Items and ItemRevisions
     *
     * @throws ServiceException  If any partial errors are returned
     */
    public CreateItemsOutput[] createItems(ItemIdsAndInitialRevisionIds[] itemIds, String itemType)
        throws com.teamcenter.services.core._2006_03.ServiceFaultFault,
               com.teamcenter.services.core._2006_03.InvalidUserFaultFault,
               com.teamcenter.services.core._2006_03.InternalServerFaultFault,
               com.teamcenter.services.core._2007_01.ServiceFaultFault,
               com.teamcenter.services.core._2007_01.InvalidUserFaultFault,
               com.teamcenter.services.core._2007_01.InternalServerFaultFault,
              RemoteException
    {

        // Get the service stub
        // WSDL-DIFFERENCE - Single Service Interface
        // In the Teamcenter client bindings all service operations are exposed
        // through a single interface (DataManagementService) while the WSDL bindings
        // have a separate interface (Core0603DataManagementService, Core0701DataManagementService)
        // for each version of the service
        Core0603DataManagementService dm0603Service = (Core0603DataManagementService)Session.createServiceStub( Core0603DataManagementService.class );
        Core0701DataManagementService dm0701Service = (Core0701DataManagementService)Session.createServiceStub( Core0701DataManagementService.class );


        GetItemCreationRelatedInfoInput in = new GetItemCreationRelatedInfoInput();
        in.setTypeName(itemType);
        in.setParentObject(NULL_OBJECT);

        // Populate form type
        GetItemCreationRelatedInfoResponse relatedResponse = dm0701Service.getItemCreationRelatedInfo(in);
        ClientDataModel.addObjects( relatedResponse.getServiceData() );

        String[] formTypes = new String[0];
        PartialErrorListener.checkForErrors( relatedResponse.getServiceData(), "DataManagementService.getItemCretionRelatedInfo returned a partial error.");

        FormAttributesInfo[] formAttrs = relatedResponse.getFormAttrs();
        formTypes = new String[formAttrs.length];
        for ( int i = 0; i < formAttrs.length; i++ )
        {
            FormAttributesInfo attrInfo = formAttrs[i];
            formTypes[i] = attrInfo.getFormType();
        }

        ItemProperties[] itemProps = new ItemProperties[itemIds.length];
        for (int i = 0; i < itemIds.length; i++)
        {
            // Create form in cache for form property population
            ModelObject[] forms = createForms(itemIds[i].getNewItemId(), formTypes[0],
                                              itemIds[i].getNewRevId(), formTypes[1],
                                              NULL_OBJECT, false);
            ItemProperties itemProperty = new ItemProperties();

            itemProperty.setClientId( "AppX-Test");
            itemProperty.setItemId ( itemIds[i].getNewItemId());
            itemProperty.setRevId ( itemIds[i].getNewRevId());
            itemProperty.setName ( "AppX-Test");
            itemProperty.setType ( itemType);
            itemProperty.setDescription ( "Test Item for the SOA AppX sample application.");
            itemProperty.setUom ( "");

            GetPropertiesInput propIn = new GetPropertiesInput();
            propIn.setObjects(forms);
            propIn.setAttributes(new String[]{"project_id"});

            // Retrieve one of form attribute value from Item master form.
            ServiceData serviceData = dm0603Service.getProperties( propIn  );
            ClientDataModel.addObjects( serviceData );

            PartialErrorListener.checkForErrors(serviceData, "DataManagementService.getProperties returned a partial error.");

            String property = null;
            try
            {
                forms[0] = ClientDataModel.getObjectWithProperties(forms[0]);
                property = ClientDataModel.getPropertyStringValue(forms[0],"project_id");
            }
            catch ( NotLoadedException ex){}


            // Only if value is null, we set new value
            if ( property == null || property.length() == 0)
            {
                ExtendedAttributes[] extendedAttributes = new ExtendedAttributes[1];

                ExtendedAttributes theExtendedAttr = new ExtendedAttributes();

                AttributeNameValueMap[] map = new AttributeNameValueMap[1];
                map[0] = new AttributeNameValueMap();
                map[0].setKey("project_id");
                map[0].setValue("project_id");
                theExtendedAttr.setAttributes(map);
                theExtendedAttr.setObjectType( formTypes[0] );
                extendedAttributes[0] = theExtendedAttr;
                itemProperty.setExtendedAttributes( extendedAttributes );
            }
            itemProps[i] = itemProperty;
        }

        CreateItemsInput ciInput = new CreateItemsInput();
        ciInput.setProperties( itemProps );
        ciInput.setContainer( NULL_OBJECT);
        ciInput.setRelationType("");

        // *****************************
        // Execute the service operation
        // *****************************
        CreateItemsResponse response = dm0603Service.createItems(ciInput );
        ClientDataModel.addObjects( response.getServiceData() );

        // before control is returned the ChangedHandler will be called with
        // newly created Item and ItemRevisions
        UpdateObjectListener.checkForUpdates( response.getServiceData());


        // The AppXPartialErrorListener is logging the partial errors returned
        // In this simple example if any partial errors occur we will throw a
        // ServiceException
        PartialErrorListener.checkForErrors( response.getServiceData(), "DataManagementService.createItems returned a partial error.");

        return response.getOutput();
    }

    /**
     * Reserve Revision IDs
     *
     * @param items       Array of Items to reserve Ids for
     *
     * @return Map of RevisionIds
     *
     * @throws ServiceException  If any partial errors are returned
     */
    public IndexToRevIdMap[] generateRevisionIds(ModelObject[] items)
     throws com.teamcenter.services.core._2006_03.ServiceFaultFault,
            com.teamcenter.services.core._2006_03.InvalidUserFaultFault,
            com.teamcenter.services.core._2006_03.InternalServerFaultFault,
            RemoteException
    {
        // Get the service stub
        Core0603DataManagementService dmService = (Core0603DataManagementService)Session.createServiceStub( Core0603DataManagementService.class );


        GenerateRevisionIdsResponse response = null;
        GenerateRevisionIdsProperties[] input = null;
        input = new GenerateRevisionIdsProperties[items.length];
        for (int i = 0; i < items.length; i++)
        {
            GenerateRevisionIdsProperties property = new GenerateRevisionIdsProperties();
            property.setItem( items[i] );
            property.setItemType( "" );
            input[i] = property;
        }

        GenerateRevisionIdsInput in = new GenerateRevisionIdsInput();
        in.setInput(input);

        // *****************************
        // Execute the service operation
        // *****************************
        response = dmService.generateRevisionIds(in);
        ClientDataModel.addObjects( response.getServiceData() );

        // The AppXPartialErrorListener is logging the partial errors returned
        // In this simple example if any partial errors occur we will throw a
        // ServiceException
        PartialErrorListener.checkForErrors( response.getServiceData(), "DataManagementService.generateRevisionIds returned a partial error.");

        return response.getOutputRevisionIds();
    }

    /**
     * Revise Items
     *
     * @param revisionIds     Map of Revsion IDs
     * @param itemRevs        Array of ItemRevisons
     *
     * @return Map of Old ItemRevsion(key) to new ItemRevision(value)
     *
     * @throws ServiceException         If any partial errors are returned
     */
    public ItemRevMap[] reviseItems(IndexToRevIdMap[] revisionIds, ModelObject[] itemRevs)
     throws com.teamcenter.services.core._2006_03.ServiceFaultFault,
            com.teamcenter.services.core._2006_03.InvalidUserFaultFault,
            com.teamcenter.services.core._2006_03.InternalServerFaultFault,
            RemoteException
    {
        // Get the service stub
        Core0603DataManagementService dmService = (Core0603DataManagementService)Session.createServiceStub( Core0603DataManagementService.class );

        ItemRevPropertyMap[] revs = new ItemRevPropertyMap[itemRevs.length];
        for (int i = 0; i < itemRevs.length; i++)
        {
            RevisionIds rev = (RevisionIds) revisionIds[i].getValue();

            ReviseProperties revProps = new ReviseProperties();

            revProps.setRevId( rev.getNewRevId() );
            revProps.setName( "testRevise" );
            revProps.setDescription( "describe testRevise");

            AttributeNameValueMap[] attrs = new AttributeNameValueMap[1];
            attrs[0] = new AttributeNameValueMap();
            attrs[0].setKey("project_id");
            attrs[0].setValue("project_id_val");
            revProps.setExtendedAttributes( attrs );

            revs[0] = new ItemRevPropertyMap();
            revs[0].setKey(itemRevs[i]);
            revs[0].setValue(revProps);
        }
        ReviseInput in = new ReviseInput();
        in.setInput(revs);

        // *****************************
        // Execute the service operation
        // *****************************
        ReviseResponse revised = dmService.revise(in);
        ClientDataModel.addObjects( revised.getServiceData() );

        // WSDL-DIFFERENCE - Change Listener
        // In the Teamcenter client bindings are integrated with a Change Listener.
        // This allows the client applications to have an implementation (AppXUpdateObjectListener)
        // of the interface that is automatically notified when any service
        // operation returns one or more Updated objects
        // In this sample application we have the same AppXUpdateObjectListener class
        // as in the HelloTeamcenter sample, however the application code must
        // call this listener to check for Updated objects.
        UpdateObjectListener.checkForUpdates( revised.getServiceData());


        // The AppXPartialErrorListener is logging the partial errors returned
        // In this simple example if any partial errors occur we will throw a
        // ServiceException
        PartialErrorListener.checkForErrors( revised.getServiceData(), "DataManagementService.revise returned a partial error.");

        return revised.getOldItemRevToNewItemRev();

    }

    /**
     * Delete the Items
     *
     * @param items     Array of Items to delete
     *
     * @throws ServiceException    If any partial errors are returned
     */
    public void deleteItems(ModelObject[] items)
     throws com.teamcenter.services.core._2006_03.ServiceFaultFault,
            com.teamcenter.services.core._2006_03.InvalidUserFaultFault,
            com.teamcenter.services.core._2006_03.InternalServerFaultFault,
            RemoteException
    {
        // Get the service stub
        Core0603DataManagementService dmService = (Core0603DataManagementService)Session.createServiceStub( Core0603DataManagementService.class );

        DeleteObjectsInput in = new DeleteObjectsInput();
        in.setObjects(items);

        // *****************************
        // Execute the service operation
        // *****************************
        ServiceData serviceData = dmService.deleteObjects(in);
        ClientDataModel.addObjects( serviceData );


        // WSDL-DIFFERENCE - Delete Listener
        // In the Teamcenter client bindings are integrated with a Delete Listener.
        // This allows the client applications to have an implementation (AppXDeletedObjectListener)
        // of the interface that is automatically notified when any service
        // operation returns one or more Deleted objects
        // In this sample application we have the same AppXDeletedObjectListener class
        // as in the HelloTeamcenter sample, however the application code must
        // call this listener to check for Deleted objects.
        DeletedObjectListener.checkForDeletes(serviceData);

        // The AppXPartialErrorListener is logging the partial errors returned
        // In this simple example if any partial errors occur we will throw a
        // ServiceException
        PartialErrorListener.checkForErrors( serviceData,  "DataManagementService.deleteObjects returned a partial error.");

    }

    /**
     * Create ItemMasterForm and ItemRevisionMasterForm
     *
     * @param IMFormName      Name of ItemMasterForm
     * @param IMFormType      Type of ItemMasterForm
     * @param IRMFormName     Name of ItemRevisionMasterForm
     * @param IRMFormType     Type of ItemRevisionMasterForm
     * @param parent          The container object that two
     *                        newly-created forms will be added into.
     * @return ModelObject[]  Array of forms
     *
     * @throws ServiceException         If any partial errors are returned
     */
    public ModelObject[] createForms ( String IMFormName, String IMFormType,
                                String IRMFormName, String IRMFormType,
                                ModelObject parent, boolean saveDB )
     throws com.teamcenter.services.core._2006_03.ServiceFaultFault,
            com.teamcenter.services.core._2007_01.InvalidUserFaultFault,
            com.teamcenter.services.core._2007_01.InternalServerFaultFault,
            RemoteException
    {
        // Get the service stub
        Core0701DataManagementService dmService = (Core0701DataManagementService)Session.createServiceStub( Core0701DataManagementService.class );


        FormInfo[] inputs = new FormInfo[2];
        inputs[0] = new FormInfo();
        inputs[0].setClientId( "1" );
        inputs[0].setDescription("");
        inputs[0].setName ( IMFormName);
        inputs[0].setFormType(IMFormType);
        inputs[0].setSaveDB ( saveDB);
        inputs[0].setParentObject( parent) ;
        inputs[0].setRelationName("");
        inputs[0].setFormObject( NULL_OBJECT );
        inputs[1] = new FormInfo();
        inputs[1].setClientId( "2");
        inputs[1].setDescription("");
        inputs[1].setName (IRMFormName);
        inputs[1].setFormType(IRMFormType);
        inputs[1].setSaveDB ( saveDB);
        inputs[1].setParentObject ( parent);
        inputs[1].setRelationName("");
        inputs[1].setFormObject( NULL_OBJECT );
        CreateOrUpdateFormsInput in = new CreateOrUpdateFormsInput();
        in.setInfo(inputs);

        CreateOrUpdateFormsResponse response = dmService.createOrUpdateForms(in);
        ClientDataModel.addObjects( response.getServiceData() );

        PartialErrorListener.checkForErrors( response.getServiceData(),  "DataManagementService.createForms returned a partial error.");

        ModelObject[] forms = new ModelObject [inputs.length];
        CreateFormsOutput[] outputs = response.getOutputs();
        for (int i=0; i<inputs.length; ++i)
        {
            forms[i] = outputs[i].getForm();
        }
        return forms;
    }

}
