//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.wireharness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.teamcenter.clientx.WHSession;
import com.teamcenter.services.strong.bom._2008_06.StructureManagement.AddOrUpdateChildrenToParentLineInfo;
import com.teamcenter.services.strong.bom._2008_06.StructureManagement.AddOrUpdateChildrenToParentLineResponse;
import com.teamcenter.services.strong.bom._2008_06.StructureManagement.ItemElementLineInfo;
import com.teamcenter.services.strong.bom._2008_06.StructureManagement.ItemLineInfo;
import com.teamcenter.services.strong.cad.StructureManagementService;
import com.teamcenter.services.strong.cad._2007_01.StructureManagement.CreateBOMWindowsInfo;
import com.teamcenter.services.strong.cad._2007_01.StructureManagement.CreateBOMWindowsResponse;
import com.teamcenter.services.strong.core.DataManagementService;
import com.teamcenter.services.strong.core._2006_03.DataManagement.CreateItemsResponse;
import com.teamcenter.services.strong.core._2006_03.DataManagement.ExtendedAttributes;
import com.teamcenter.services.strong.core._2006_03.DataManagement.GenerateItemIdsAndInitialRevisionIdsProperties;
import com.teamcenter.services.strong.core._2006_03.DataManagement.GenerateItemIdsAndInitialRevisionIdsResponse;
import com.teamcenter.services.strong.core._2006_03.DataManagement.ItemIdsAndInitialRevisionIds;
import com.teamcenter.services.strong.core._2006_03.DataManagement.ItemProperties;
import com.teamcenter.services.strong.core._2008_06.DataManagement.ConnectionProperties;
import com.teamcenter.services.strong.core._2008_06.DataManagement.CreateConnectionsResponse;
import com.teamcenter.services.strong.core._2008_06.DataManagement.CreateOrUpdateItemElementsResponse;
import com.teamcenter.services.strong.core._2008_06.DataManagement.ItemElementProperties;
import com.teamcenter.soa.client.Connection;
import com.teamcenter.soa.client.model.ErrorStack;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.client.model.ServiceData;
import com.teamcenter.soa.client.model.strong.BOMLine;
import com.teamcenter.soa.client.model.strong.GeneralDesignElement;
import com.teamcenter.soa.client.model.strong.Item;
import com.teamcenter.soa.client.model.strong.ItemRevision;

public class WireHarnessTestUtils
{
    private Connection m_connection = null;
    private DataManagementService  dmService;
    private com.teamcenter.services.strong.core.DataManagementService dmService2008_03;
    private StructureManagementService smService = null;
    private com.teamcenter.services.strong.bom.StructureManagementService  smBomService;

    /**
     * Constructor
     * @param connection
     */
    public WireHarnessTestUtils()
    {
        m_connection = WHSession.getConnection();
        dmService = DataManagementService.getService( m_connection );
        dmService2008_03 = com.teamcenter.services.strong.core.DataManagementService.getService( m_connection );
        smService = StructureManagementService.getService( WHSession.getConnection() );
        smBomService = com.teamcenter.services.strong.bom.StructureManagementService.getService( m_connection );
    }

    /**
     * This function auto generates the item ids and item rev ids. This can be also used to create auto generate ids of other classes
     * like connections
     * Input:
     *      int noOfIds         Numer of ids need to be generated
     *      String objectType   objectType (if null item type is assumed)
     * Output
     *      List itemids        itemids
     *      List revIds         revision ids
     *
     */

    @SuppressWarnings({"rawtypes" })
    public GenerateItemIdsAndInitialRevisionIdsResponse generateItemIdsAndRevIds (int noOfIds,
            String objectType, List<String> itemIds, List<String> revIds)
    {
        if(objectType.length() == 0)
        {
            objectType = "Item";
        }

        //Generate item and rev ids
        GenerateItemIdsAndInitialRevisionIdsResponse response = null;
        GenerateItemIdsAndInitialRevisionIdsProperties[] props = new GenerateItemIdsAndInitialRevisionIdsProperties[noOfIds];

        for ( int idx=0; idx<noOfIds; idx++ )
        {
            GenerateItemIdsAndInitialRevisionIdsProperties property = new GenerateItemIdsAndInitialRevisionIdsProperties();
            property.count = 1;
            property.itemType = objectType;
            props[idx] = property;
        }
        response = dmService.generateItemIdsAndInitialRevisionIds(props);
        if ( ! handleServiceData( response.serviceData, "" ) )
        {
            return null;
        }

        //Add the item ids and rev ids to List


        Map itemsIdsAndRevs =
            response.outputItemIdsAndInitialRevisionIds;

        for (Iterator iterator = itemsIdsAndRevs.entrySet().iterator(); iterator.hasNext();)
        {
            Map.Entry entry = (Map.Entry) iterator.next();
            ItemIdsAndInitialRevisionIds[] theMap = (ItemIdsAndInitialRevisionIds[])entry.getValue();
            for ( int i=0; i<theMap.length ; i++)
            {
                ItemIdsAndInitialRevisionIds ids= theMap[i];
                itemIds.add(ids.newItemId);
                revIds.add(ids.newRevId);
            }
        }
        return response;
    }

    /**
     * Create Items
     */
    public CreateItemsResponse createItems (int nItems, String[] itemNames,
            String itemType, List <String> itemids, List<Item> itemsList,
            List<ItemRevision> revsList)  throws Exception
    {
        List<String> item_ids = new ArrayList<String>();
        List<String> item_rev_ids = new ArrayList<String>();
        generateItemIdsAndRevIds(nItems, itemType, item_ids, item_rev_ids);

        ItemProperties[] props =
            populateItemProperties(nItems, itemNames, itemType, item_ids, item_rev_ids);
        CreateItemsResponse response =
            dmService.createItems(props, null, "" );
        if ( ! handleServiceData( response.serviceData, "" ) )
        {
            return null;
        }

        for( int idx = 0; idx < response.output.length; idx++ )
        {
            itemsList.add( response.output[idx].item );
            revsList.add( response.output[idx].itemRev );
            itemids.add((String)item_ids.get(idx));

        }
        return response;
    }

    /**
     * Populate Item Properties
     */
    private ItemProperties[] populateItemProperties (int num_ids, String[] itemNames, String itemType, List<String> itemIds, List<String> revIds )
    {
        ItemProperties[] itemProps = new ItemProperties[num_ids];
        for (int idx = 0; idx<num_ids; idx++)
        {
            ItemProperties itemProperty = new ItemProperties();
            itemProperty.clientId = itemNames[idx] + idx ;
            itemProperty.itemId = (String)itemIds.get(idx);
            itemProperty.revId = (String)revIds.get(idx);
            itemProperty.type =  itemType;
            itemProperty.name = itemNames[idx] ;

            // We need to populate the Wire Object properties
            if( itemType == "HRN_GeneralWire" )
            {
                // Create a map of Attribute name value pairs
                Map<String, String> attrMap = new HashMap<String, String>();
                attrMap.put( "bend_radius", "1.0" );
                attrMap.put( "gauge", "TXL" );
                attrMap.put( "wirespec", "TXL" );
                attrMap.put( "outside_dia", "3.8" );
                attrMap.put( "height", "2.0" );
                attrMap.put( "width", "2.0" );
                attrMap.put( "min_length", "200.00" );
                attrMap.put( "max_length", "400.00" );
                attrMap.put( "cross_section", "50.00" );

                // Create ExtendedAttributes object
                ExtendedAttributes extMFAtts = new ExtendedAttributes();
                extMFAtts.attributes = attrMap;
                extMFAtts.objectType = "HRN_GeneralWire Master";

                ExtendedAttributes[] extMFAttsArr = new ExtendedAttributes[1];
                extMFAttsArr[0] = extMFAtts;

                // Set the Wire Object properties
                itemProperty.extendedAttributes = extMFAttsArr;
            }
            itemProps[idx] = itemProperty;
        }
        return itemProps;
    }

    /**
     * Populate Connection Properties
     */
    public ConnectionProperties[] populateConnectionProps(
            int numObjs,
            String connType,
            String [] conn_names,
            List<String> connIds, List<String> connRevIds)
    {
        ConnectionProperties[] connPropsArr = new ConnectionProperties[numObjs];
        for( int inx=0; inx < numObjs; inx++ )
        {

            ConnectionProperties connPropsObj = new ConnectionProperties();
            connPropsObj.clientId = conn_names[inx] + inx;
            connPropsObj.connId = (String)connIds.get(inx);
            connPropsObj.name = conn_names[inx];
            connPropsObj.type = connType;
            connPropsObj.description = conn_names[inx];
            connPropsObj.revId = (String)connRevIds.get(inx);
            connPropsObj.extendedAttributes = new ExtendedAttributes[0];

            connPropsArr[inx] = connPropsObj;

        }
        return connPropsArr;
    }

    /**
     * Set direction on GDE object
     */
    public Boolean setDirectionOnItemElement(GeneralDesignElement gdeObj, String directionvalue)
    {
        Map<String,com.teamcenter.services.strong.core._2007_01.DataManagement.VecStruct> attr = new HashMap<String,com.teamcenter.services.strong.core._2007_01.DataManagement.VecStruct>();
        String propName = "fnd0Direction";
        String[] propertyStringArray = {directionvalue};
        com.teamcenter.services.strong.core._2007_01.DataManagement.VecStruct vecStruct =
                                                     new com.teamcenter.services.strong.core._2007_01.DataManagement.VecStruct();
        vecStruct.stringVec = propertyStringArray;
        attr.put(propName, vecStruct);
        ModelObject[] objects = {gdeObj};   

        ServiceData sd  = dmService2008_03.setProperties(objects, attr);
        return handleServiceData( sd, "setDirectionOnItemElement"); 
    }

    /**
     * Create ItemElements (GDEs)
     */
    public CreateOrUpdateItemElementsResponse createItemElements( int noOfGDEs, String[] gde_names, String gdeType,
            List <ModelObject> itemelems )
    {
        ItemElementProperties[] itemElemPropsArr = new ItemElementProperties[ noOfGDEs ];

        for( int inx=0; inx< noOfGDEs ; inx++ )
        {
            ItemElementProperties itemElemPropsObj = new ItemElementProperties();
            itemElemPropsObj.clientId = gde_names[inx] + inx;
            itemElemPropsObj.description = gde_names[inx];
            itemElemPropsObj.name = gde_names[inx];
            itemElemPropsObj.type = gdeType;

            itemElemPropsArr[inx] = itemElemPropsObj;
        }

        CreateOrUpdateItemElementsResponse createItemElemResp = dmService.createOrUpdateItemElements( itemElemPropsArr );
        if ( ! handleServiceData( createItemElemResp.serviceData, "" ) )
        {
            return null;
        }

        for( int idx = 0; idx < createItemElemResp.output.length; idx++ )
        {
            itemelems.add( createItemElemResp.output[idx].itemElem );
        }
        return createItemElemResp;
    }

    /**
     * Create Connections
     */
    public CreateConnectionsResponse createConnections(int noOfConnObjs, String[] conn_names,
            String ConnectionType, List<String> ids, List <ModelObject> conns, List <ModelObject> conn_revs)
    {
        CreateConnectionsResponse createConnResp = null;
        List<String> conn_ids = new ArrayList<String>();
        List<String> conn_rev_ids = new ArrayList<String>();
        try
        {
            generateItemIdsAndRevIds( noOfConnObjs, ConnectionType, conn_ids, conn_rev_ids );
            ConnectionProperties[] connPropsArr = this.populateConnectionProps(noOfConnObjs, ConnectionType, conn_names,  conn_ids, conn_rev_ids);

            ModelObject[] whereToCreateArr = new ModelObject[1];
            //create Connection objects
            createConnResp =
                dmService2008_03.createConnections(connPropsArr, whereToCreateArr[0], "contents" );
            if ( ! handleServiceData( createConnResp.serviceData, "" ) )
            {
                return null;
            }
            for( int idx = 0; idx < createConnResp.output.length; idx++ )
            {
                conns.add( createConnResp.output[idx].connection );
                conn_revs.add( createConnResp.output[idx].connectionRev );
                ids.add((String)conn_ids.get(idx));
            }
        }
        catch ( Exception ex )
        {
            LogFile.write( "createConnections : " + ex.getMessage() );
        }
        return createConnResp;
    }

    /**
     * Handle and post errors
     * @param sData ServiceData to handle
     * @param methodName If user doesn't want to print the method name, he should pass as ""
     * @return true if no errors
     * @unpublished
     */
    public boolean handleServiceData( final ServiceData sData, String methodName )
    {
        int noPartErrors = sData.sizeOfPartialErrors();
        if( noPartErrors > 0 )
        {
            String errorMessage = "";
            for( int i=0; i < noPartErrors; i++ )
            {
                ErrorStack errorStack = sData.getPartialError(i);
                String[] messages    = errorStack.getMessages();
                for(int j=0; messages != null && j < messages.length; j++ )
                {
                    errorMessage = errorMessage + messages[j] + "\n";
                }
            }
            if( errorMessage != "" )
            {
                if( methodName != "" )
                {
                    LogFile.write( methodName + " : " + errorMessage );
                }
                else
                {
                    LogFile.write( "handleServiceData + : " + errorMessage );
                }
            }
            return false;
        }
        return true;
    }

    /* Creates a BOMWindow
    * @param itemRev
    * @return
    */
   public CreateBOMWindowsResponse createBomWindow( ItemRevision itemRev )
   {
       //Now create BOMWindow
       CreateBOMWindowsInfo[] createBOMWindowsInfo = new CreateBOMWindowsInfo[1];
       createBOMWindowsInfo[0] = new CreateBOMWindowsInfo();
       createBOMWindowsInfo[0].itemRev = itemRev;


       CreateBOMWindowsResponse createBOMWindowsResponse =
               smService.createBOMWindows(createBOMWindowsInfo);
       return createBOMWindowsResponse;
   }

   /**
     * Adds given lines to the parentline specified.
     *  1) If CreateItemsResponse and CreateConnectionsResponse both are null, then only ItemElement
     *      will be added.
     *  2) If CreateItemsResponse and CreateConnectionsResponse both are populated, then all three
     *      will be added (GDE, PSConnection and Item).
     *  3) If only one of CreateItemsResponse  and CreateConnectionsResponse is populated,
     *      then that will be used
     *  NOTE : this method is generalized only to handle one object of each of above types.
     *
     * @param itemElemResp This should either be null or fully populated
     * @param crItemsResp This can either be null or fully populated
     * @param createConnResp This can either be null or fully populated
     * @param viewType
     * @param topLine
     * @return
     */
    public AddOrUpdateChildrenToParentLineResponse
                addOrUpdateChildrenToParentLine(    CreateOrUpdateItemElementsResponse crItemElemResp,
                                                    CreateItemsResponse crItemsResp,
                                                    CreateConnectionsResponse crConnResp,
                                                    String viewType,
                                                    BOMLine topLine
                                                )
    {
        AddOrUpdateChildrenToParentLineResponse AddUpdChToParResp = null;

        try
        {
            if(viewType == "")
            {
                viewType = "view";
            }
            if(     crItemElemResp == null &&
                    crItemsResp == null &&
                    crConnResp == null
                )
            {
                return AddUpdChToParResp;
            }
            //First process Item Element info
            ItemElementLineInfo[] itmElemLineInfoArr;
            if(crItemElemResp != null)
            {
                itmElemLineInfoArr = new ItemElementLineInfo[1];

                ItemElementLineInfo itmElemLineInfo = new ItemElementLineInfo();
                itmElemLineInfo.clientId = crItemElemResp.output[0].clientId;
                itmElemLineInfo.itemElement = crItemElemResp.output[0].itemElem;
                itmElemLineInfo.itemElementline =  null;
                itmElemLineInfo.occType = "";
                itmElemLineInfo.itemElementLineProperties = new HashMap<String, String>();

                itmElemLineInfoArr[0] = itmElemLineInfo;

            }
            else
            {
                itmElemLineInfoArr = new ItemElementLineInfo[0];
            }

            //Now process Item info
            ItemLineInfo[] itmLineInfoArr;
            if(crItemsResp != null && crConnResp == null)   //Only item
            {
                itmLineInfoArr = new ItemLineInfo[1];
                ItemLineInfo itmLineInfo = new ItemLineInfo();
                itmLineInfo.bomline = null;
                itmLineInfo.clientId = crItemsResp.output[0].clientId;
                itmLineInfo.item = crItemsResp.output[0].item;
                itmLineInfo.itemRev = crItemsResp.output[0].itemRev;
                itmLineInfo.occType = "";
                itmLineInfo.itemLineProperties = new HashMap<String, String>();

                itmLineInfoArr[0] = itmLineInfo;

            }
            else if(crConnResp != null && crItemsResp == null ) //only connection
            {
                itmLineInfoArr = new ItemLineInfo[1];
                ItemLineInfo itmLineInfo = new ItemLineInfo();
                itmLineInfo.bomline = null;
                itmLineInfo.clientId = crConnResp.output[0].clientId;
                itmLineInfo.item = crConnResp.output[0].connection;
                itmLineInfo.itemRev = crConnResp.output[0].connectionRev;
                itmLineInfo.occType = "";
                itmLineInfo.itemLineProperties = new HashMap<String, String>();

                itmLineInfoArr[0] = itmLineInfo;
            }
            else if (crConnResp != null && crItemsResp != null )//both item and connection
            {
                itmLineInfoArr = new ItemLineInfo[2];

                ItemLineInfo itmLineInfo = new ItemLineInfo();
                itmLineInfo.bomline = null;
                itmLineInfo.clientId = crItemsResp.output[0].clientId;
                itmLineInfo.item = crItemsResp.output[0].item;
                itmLineInfo.itemRev = crItemsResp.output[0].itemRev;
                itmLineInfo.occType = "";
                Map<String, String> itemLineProps = new HashMap<String, String>();
                itmLineInfo.itemLineProperties = itemLineProps;

                ItemLineInfo connLineInfo = new ItemLineInfo();
                connLineInfo.bomline = null;
                connLineInfo.clientId = crConnResp.output[0].clientId;
                connLineInfo.item = crConnResp.output[0].connection;
                connLineInfo.itemRev = crConnResp.output[0].connectionRev;
                connLineInfo.occType = "";
                Map<String, String> connLineProps = new HashMap<String, String>();
                connLineInfo.itemLineProperties = connLineProps;

                itmLineInfoArr[0] = itmLineInfo;
                itmLineInfoArr[1] = connLineInfo;

            }
            else    //both null
            {
                itmLineInfoArr = new ItemLineInfo[0];
            }

            AddOrUpdateChildrenToParentLineInfo[] addChToParInfoArr =
                                new AddOrUpdateChildrenToParentLineInfo[1];

            AddOrUpdateChildrenToParentLineInfo addChToParInfo =
                                new AddOrUpdateChildrenToParentLineInfo();

            addChToParInfo.itemElements = itmElemLineInfoArr;
            addChToParInfo.items = itmLineInfoArr;
            addChToParInfo.viewType = viewType;
            addChToParInfo.parentLine =  topLine;

            addChToParInfoArr[0] = addChToParInfo;

            // Add the lines
            AddUpdChToParResp =
                smBomService.addOrUpdateChildrenToParentLine( addChToParInfoArr );

        }
        catch( Exception ex )
        {
            System.out.println( "addOrUpdateChildrenToParentLine : " + ex.getMessage() );
        }
        return AddUpdChToParResp;
    }

    /**
     * Helper method for cleanup (deletion) of created Data
     * @param object
     * @throws Exception
     */
    public ServiceData deleteObjects(ModelObject[] object)
    {
        // Delete given objects
        return dmService.deleteObjects(object);
    }
}
