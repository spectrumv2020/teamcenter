//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.hellosoap;



import java.math.BigInteger;
import java.rmi.RemoteException;

import com.teamcenter.schemas.query._2006_03.savedquery.GetSavedQueriesInput;
import com.teamcenter.schemas.query._2006_03.savedquery.GetSavedQueriesResponse;
import com.teamcenter.schemas.query._2006_03.savedquery.SavedQueryObject;
import com.teamcenter.schemas.query._2007_06.savedquery.ExecuteSavedQueriesInput;
import com.teamcenter.schemas.query._2007_06.savedquery.ExecuteSavedQueriesResponse;
import com.teamcenter.schemas.query._2007_06.savedquery.SavedQueryInput;
import com.teamcenter.schemas.query._2007_06.savedquery.SavedQueryResults;
import com.teamcenter.schemas.soa._2006_03.base.ModelObject;
import com.teamcenter.services.query._2006_03.InternalServerFaultFault;
import com.teamcenter.services.query._2006_03.InvalidUserFaultFault;
import com.teamcenter.services.query._2006_03.Query0603SavedQueryService;
import com.teamcenter.services.query._2006_03.ServiceFaultFault;
import com.teamcenter.services.query._2007_06.Query0706SavedQueryService;





public class Query
{

    /**
     * Perform a simple query of the database
     *
     */
    public void queryItems()
    {

        ModelObject query = null;
        
        // Get the service stub
        Query0603SavedQueryService queryService = (Query0603SavedQueryService)Session.createServiceStub( Query0603SavedQueryService.class );
        // Get the service stub
        Query0706SavedQueryService query0706Service = (Query0706SavedQueryService)Session.createServiceStub( Query0706SavedQueryService.class );
     

        try
        {
            GetSavedQueriesInput in = new GetSavedQueriesInput();

            // *****************************
            // Execute the service operation
            // *****************************
            GetSavedQueriesResponse savedQueries = queryService.getSavedQueries(in);

            ClientDataModel.addObjects( savedQueries.getServiceData() );

            SavedQueryObject[] queries = savedQueries.getQueries();
            if (queries.length == 0)
            {
                System.out.println("There are no saved queries in the system.");
                return;
            }

            // Find one called 'Item Name'
            for (int i = 0; i < queries.length; i++)
            {

                if (queries[i].getName().equals("Item Name"))
                {
                    query = queries[i].getQuery();
                    break;
                }
            }
        }
        catch (ServiceFaultFault e)         { System.out.println( e.getFaultMessage().getServiceFault().getMessages()[0]); return;}
        catch (InternalServerFaultFault e)  { System.out.println( e.getFaultMessage().getInternalServerFault().getMessages()[0]); return;}
        catch (InvalidUserFaultFault e)     { System.out.println( e.getFaultMessage().getInvalidUserFault().getMessage()); return;}
        catch( RemoteException e )          { System.out.println( e.getMessage()); return;}


        if (query == null)
        {
            System.out.println("There is not an 'Item Name' query.");
            return;
        }

        try
        {
            

            // WSDL-DIFFERENCE - Doc/Literal
            // The WSDL are defined in the document/literal style, this means
            // the every service operation will have one input argument and one
            // return argument.
            // Search for all Items, returning a maximum of 25 objects
            BigInteger limit = new BigInteger("25");
            SavedQueryInput savedQueryInput[] = new SavedQueryInput[1];
            savedQueryInput[0] = new SavedQueryInput();
            savedQueryInput[0].setQuery(query);
            savedQueryInput[0].setMaxNumToReturn(limit);
            savedQueryInput[0].setLimitListCount(limit);
            savedQueryInput[0].setLimitList(new ModelObject[0]);
            savedQueryInput[0].setEntries(new String[]{"Item Name" });
            savedQueryInput[0].setValues(new String[1]);
            savedQueryInput[0].addValues("*");
            savedQueryInput[0].setMaxNumToInflate(limit);
            ExecuteSavedQueriesInput input = new ExecuteSavedQueriesInput();
            input.setInput(savedQueryInput);

            // *****************************
            // Execute the service operation
            // *****************************
            ExecuteSavedQueriesResponse savedQueryResult;
            savedQueryResult = query0706Service.executeSavedQueries(input);
            ClientDataModel.addObjects( savedQueryResult.getServiceData() );
            SavedQueryResults [] found = savedQueryResult.getArrayOfResults(); 
          
            System.out.println("");
            System.out.println("Found Items:");
            Session.printObjects( found[0].getObjects() );
        }
        catch( RemoteException e )
        {
            System.out.println( e.getMessage()); return;
        }
        catch (com.teamcenter.services.query._2007_06.InvalidUserFaultFault e)
        {
            System.out.println( e.getMessage());    
        }
        catch (com.teamcenter.services.query._2007_06.InternalServerFaultFault e)
        {
            System.out.println( e.getMessage());
        }
   
    }

}
