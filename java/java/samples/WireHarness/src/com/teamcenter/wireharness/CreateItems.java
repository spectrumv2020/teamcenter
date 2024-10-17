//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================


package com.teamcenter.wireharness;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.teamcenter.services.strong.core.DataManagementService;
import com.teamcenter.services.strong.core._2006_03.DataManagement.CreateItemsResponse;
import com.teamcenter.services.strong.core._2006_03.DataManagement.GenerateItemIdsAndInitialRevisionIdsProperties;
import com.teamcenter.services.strong.core._2006_03.DataManagement.GenerateItemIdsAndInitialRevisionIdsResponse;
import com.teamcenter.services.strong.core._2006_03.DataManagement.ItemIdsAndInitialRevisionIds;
import com.teamcenter.services.strong.core._2006_03.DataManagement.ItemProperties;
import com.teamcenter.soa.client.Connection;
import com.teamcenter.soa.client.model.strong.Item;
import com.teamcenter.soa.client.model.strong.ItemRevision;

public class CreateItems
{
    private DataManagementService  service;
    private String m_itemType = "";
    private String m_itemName = "";
    private WireHarnessTestUtils whTestUtils = null;

    /**
     * Constructor
     */
    public CreateItems( Connection conn )
    {
        service = DataManagementService.getService(conn);
        whTestUtils = new WireHarnessTestUtils();
    }

    /**
     * Constructor
     */
    public CreateItems( Connection conn, String itemType, String itemName )
    {
        service = DataManagementService.getService(conn);
        m_itemType = itemType;
        m_itemName = itemName;
    }

    /**
     * Generate and Create Ids
     */
    public CreateItemsResponse generateAndCreate( int num_ids, List<Item> items, List<ItemRevision> revs )
    {
        List<String> itemIds = new ArrayList<String>();
        List<String> revIds = new ArrayList<String>();
        generateItemIdsAndRevIds( num_ids, itemIds, revIds );
        ItemProperties[] props =
            populateItemProperties( num_ids, itemIds, revIds );
        CreateItemsResponse response =
            service.createItems( props, null, "" );
        if ( ! whTestUtils.handleServiceData( response.serviceData, "" ) )
        {
            return response;
        }

        for( int idx = 0; idx < response.output.length; idx++ )
        {
            items.add( response.output[idx].item );
            revs.add( response.output[idx].itemRev );
        }
        return response;
    }

    /**
     * populate Item Properties
     */
    private ItemProperties[] populateItemProperties (int num_ids, List<String> itemIds, List<String> revIds)
    {
        ItemProperties[] itemProps = new ItemProperties[num_ids];
        for (int idx = 0; idx<num_ids; idx++)
        {
            ItemProperties itemProperty = new ItemProperties();
            itemProperty.clientId = "TestItem_"+idx;
            itemProperty.itemId = (String)itemIds.get(idx);
            itemProperty.revId = (String)revIds.get(idx);
            itemProperty.type = (m_itemType != "") ? m_itemType  : "Item";
            itemProps[idx] = itemProperty;
            itemProperty.name = (m_itemName != "") ? m_itemName + "_" + (idx + 1) : "";
        }
        return itemProps;
    }

    /**
     * Generate ItemIds And RevIds
     */
    @SuppressWarnings("rawtypes")
    private void generateItemIdsAndRevIds (int noOfIds, List<String> itemIds, List<String> revIds)
    {
        //Generate item and rev ids
        GenerateItemIdsAndInitialRevisionIdsResponse response = null;
        GenerateItemIdsAndInitialRevisionIdsProperties[] props = new GenerateItemIdsAndInitialRevisionIdsProperties[noOfIds];

        for ( int idx=0; idx<noOfIds; idx++ )
        {
            GenerateItemIdsAndInitialRevisionIdsProperties property = new GenerateItemIdsAndInitialRevisionIdsProperties();
            property.count = 1;
            property.itemType = "Item";
            props[idx] = property;
        }
        response = service.generateItemIdsAndInitialRevisionIds(props);
        if ( ! whTestUtils.handleServiceData( response.serviceData, "" ) )
        {
            return;
        }

        //Add the item ids and rev ids to List
        Map itemsIdsAndRevs = response.outputItemIdsAndInitialRevisionIds;

        for (Iterator iterator = itemsIdsAndRevs.entrySet().iterator(); iterator.hasNext();)
        {
             Map.Entry entry = (Map.Entry) iterator.next();
             ItemIdsAndInitialRevisionIds[] theMap = (ItemIdsAndInitialRevisionIds[])entry.getValue();
             for ( int i=0; i<theMap.length ; i++)
             {
                 ItemIdsAndInitialRevisionIds ids= theMap[i];
                 String itemId = ids.newItemId ;
                 itemIds.add( itemId );
                 String revId = ids.newRevId ;
                 revIds.add( revId );
             }
        }
    }
}
