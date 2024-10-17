//==================================================
// 
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.webapp.util;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import com.teamcenter.schemas.soa._2006_03.exceptions.ServiceException;
import com.teamcenter.services.strong.core.DataManagementService;
import com.teamcenter.services.strong.core._2006_03.DataManagement.CreateItemsOutput;
import com.teamcenter.services.strong.core._2006_03.DataManagement.CreateItemsResponse;
import com.teamcenter.services.strong.core._2006_03.DataManagement.ExtendedAttributes;
import com.teamcenter.services.strong.core._2006_03.DataManagement.GenerateItemIdsAndInitialRevisionIdsProperties;
import com.teamcenter.services.strong.core._2006_03.DataManagement.GenerateItemIdsAndInitialRevisionIdsResponse;
import com.teamcenter.services.strong.core._2006_03.DataManagement.ItemIdsAndInitialRevisionIds;
import com.teamcenter.services.strong.core._2006_03.DataManagement.ItemProperties;
import com.teamcenter.soa.client.Connection;
import com.teamcenter.soa.client.model.strong.Item;

/**
 * Perform different operations in the DataManagementService
 *
 */
public class DataManagement
{
    /**
     * Reserve a number Item and Revision Ids
     *
     * @param numberOfIds Number of IDs to generate
     * @param type        Type of IDs to generate
     * @param connection  SOA connection to use
     *
     * @return An array of Item and Revision IDs. The size of the array is equal
     *         to the input numberOfIds
     */
    public ItemIdsAndInitialRevisionIds[] generateItemIds(int numberOfIds, String type, Connection connection)
    {
        // Get the service stub
        DataManagementService dmService = DataManagementService.getService(connection);
        GenerateItemIdsAndInitialRevisionIdsProperties[] properties = new GenerateItemIdsAndInitialRevisionIdsProperties[1];
        GenerateItemIdsAndInitialRevisionIdsProperties property = new GenerateItemIdsAndInitialRevisionIdsProperties();

        property.count = numberOfIds;
        property.itemType = type;
        property.item = null; // Not used
        properties[0] = property;

        // *****************************
        // Execute the service operation
        // *****************************
        GenerateItemIdsAndInitialRevisionIdsResponse response = dmService.generateItemIdsAndInitialRevisionIds(properties);

        // The return is a map of ItemIdsAndInitialRevisionIds keyed on the
        // 0-based index of requested IDs. Since we only asked for IDs for one
        // data type, the map key is '0'
        BigInteger bIkey = new BigInteger("0");
        Map allNewIds = response.outputItemIdsAndInitialRevisionIds;
        ItemIdsAndInitialRevisionIds[] myNewIds = (ItemIdsAndInitialRevisionIds[]) allNewIds.get(bIkey);

        return myNewIds;
    }

    /**
     * Create a set of items
     *
     * @param numOfItems  Number of items to create
     * @param itemType    Type of item to create
     * @param connection  SOA connection to use
     * @return array of created Items and ItemRevisions
     * @throws ServiceException
     */
    public CreateItemsOutput[] createItems(int numOfItems, String itemType, Connection connection)
        throws ServiceException
    {
        ItemIdsAndInitialRevisionIds[] itemIds = generateItemIds(numOfItems, itemType, connection);

        // Get the service stub
        DataManagementService dmService = DataManagementService.getService(connection);

        ItemProperties[] itemProps = new ItemProperties[itemIds.length];
        for (int i = 0; i < itemIds.length; i++)
        {
            ItemProperties itemProperty = new ItemProperties();

            itemProperty.clientId = "AppX-Test";
            itemProperty.itemId = itemIds[i].newItemId;
            itemProperty.revId = itemIds[i].newRevId;
            itemProperty.name = "AppX-Test";
            itemProperty.type = itemType;
            itemProperty.description = "Test Item for the SOA AppX sample application.";
            itemProperty.uom = "";

            itemProperty.extendedAttributes = new ExtendedAttributes[1];
            ExtendedAttributes theExtendedAttr = new ExtendedAttributes();
            theExtendedAttr.attributes = new HashMap();
            theExtendedAttr.objectType = itemType;
            itemProperty.extendedAttributes[0] = theExtendedAttr;
            itemProps[i] = itemProperty;
        }

        // *****************************
        // Execute the service operation
        // *****************************
        CreateItemsResponse response = dmService.createItems(itemProps, null, "");

        return response.output;
    }

    /**
     * Delete the Items
     * @param items       array of Items to delete
     * @param connection  SOA connection to use
     */
    public void deleteItems(Item[] items, Connection connection)
    {
        // Get the service stub
        DataManagementService dmService = DataManagementService.getService(connection);

        // *****************************
        // Execute the service operation
        // *****************************
        dmService.deleteObjects(items);
    }

}
