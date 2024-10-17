//==================================================
// 
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.webapp.util;

import com.teamcenter.schemas.soa._2006_03.exceptions.ServiceException;

//Include the Saved Query Service Interface
import com.teamcenter.services.strong.query.SavedQueryService;

// Input and output structures for the service operations
// Note: the different namespace from the service interface
import com.teamcenter.services.strong.query._2006_03.SavedQuery.GetSavedQueriesResponse;
import com.teamcenter.services.strong.query._2007_06.SavedQuery.ExecuteSavedQueriesResponse;
import com.teamcenter.services.strong.query._2007_06.SavedQuery.SavedQueryInput;
import com.teamcenter.services.strong.query._2007_06.SavedQuery.SavedQueryResults;

import com.teamcenter.soa.client.Connection;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.client.model.strong.ImanQuery;

public class Query
{
    /**
     * Perform a simple query for items in the database
     *
     * @param connection  SOA connection to use
     * @return results of the item query
     */
    public SavedQueryResults queryItems(Connection connection)
    {
        ImanQuery query = null;

        // Get the service stub
        SavedQueryService queryService = SavedQueryService.getService(connection);
        try
        {
            // *****************************
            // Execute the service operation
            // *****************************
            GetSavedQueriesResponse savedQueries = queryService.getSavedQueries();

            if (savedQueries.queries.length == 0)
            {
                 return null;
            }

            // Find one called 'Item Name'
            for (int i = 0; i < savedQueries.queries.length; i++)
            {
                if (savedQueries.queries[i].name.equals("Item Name"))
                {
                    query = savedQueries.queries[i].query;
                    break;
                }
            }
            if (query == null)
            {
               return null;
            }

            //Search for all items, returning a maximum of 25 objects
            SavedQueryInput savedQueryInput[] = new SavedQueryInput[1];
            savedQueryInput[0] = new SavedQueryInput();
            savedQueryInput[0].query = query;
            savedQueryInput[0].maxNumToReturn = 25;
            savedQueryInput[0].limitListCount = 0;
            savedQueryInput[0].limitList = new ModelObject[0];
            savedQueryInput[0].entries = new String[]{"Item Name" };
            savedQueryInput[0].values = new String[1];
            savedQueryInput[0].values[0] = "*";
            savedQueryInput[0].maxNumToInflate = 25;

            //*****************************
            //Execute the service operation
            //*****************************
            ExecuteSavedQueriesResponse savedQueryResult = queryService.executeSavedQueries(savedQueryInput);

            SavedQueryResults found = savedQueryResult.arrayOfResults[0];

            return found;
        }
        catch (ServiceException e)
        {
           return null;
        }
     }
  }
