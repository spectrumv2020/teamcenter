//==================================================
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//==================================================


package com.teamcenter.mdomanagement;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lbr0.services.strong.librarymanagement._2014_10.LibraryManagement.CreateHierarchiesResponse;
import com.lbr0.services.strong.librarymanagement._2014_10.LibraryManagement.CreateNodesResponse;
import com.lbr0.services.strong.librarymanagement._2014_10.LibraryUsage.PublishObjectsResponse;
import com.mdo0.services.strong.mdomanagement._2014_10.MultiDisciplinaryDataMgt.DesignArtifactInputsForSearch;
import com.mdo0.services.strong.mdomanagement._2014_10.MultiDisciplinaryDataMgt.DesignInstancesData;
import com.mdo0.services.strong.mdomanagement._2014_10.MultiDisciplinaryDataMgt.LinkedInstances;
import com.mdo0.services.strong.mdomanagement._2014_10.MultiDisciplinaryDataMgt.MDOSearchOutput;
import com.mdo0.services.strong.mdomanagement._2014_10.MultiDisciplinaryDataMgt.SearchForLinkedInstancesResponse;
import com.mdo0.services.strong.mdomanagement._2014_10.MultiDisciplinaryDataMgt.SearchInput;
import com.mdo0.services.strong.mdomanagement._2014_10.MultiDisciplinaryDataMgt.SearchMDOResponse;
import com.mdo0.services.strong.mdomanagement._2015_07.MultiDisciplinaryDataMgt.CreateNotificationOutput;
import com.mdo0.services.strong.mdomanagement._2015_07.MultiDisciplinaryDataMgt.FilterForSearch;
import com.mdo0.services.strong.mdomanagement._2015_07.MultiDisciplinaryDataMgt.MDOInitialNotification;
import com.mdo0.services.strong.mdomanagement._2015_07.MultiDisciplinaryDataMgt.MDONotificationReactionData;
import com.mdo0.services.strong.mdomanagement._2015_07.MultiDisciplinaryDataMgt.MDONotificationUpdateData;
import com.mdo0.services.strong.mdomanagement._2015_07.MultiDisciplinaryDataMgt.MDONotificationUpdateInput;
import com.mdo0.services.strong.mdomanagement._2015_07.MultiDisciplinaryDataMgt.MDOSearchOutput2;
import com.mdo0.services.strong.mdomanagement._2015_07.MultiDisciplinaryDataMgt.ModifyNotificationOutput;
import com.mdo0.services.strong.mdomanagement._2015_07.MultiDisciplinaryDataMgt.NotificationQueryInput;
import com.mdo0.services.strong.mdomanagement._2015_07.MultiDisciplinaryDataMgt.QryInputByDesignOrProject;
import com.mdo0.services.strong.mdomanagement._2015_07.MultiDisciplinaryDataMgt.QryNotificationByDesignResponse;
import com.mdo0.services.strong.mdomanagement._2015_07.MultiDisciplinaryDataMgt.QryNotificationOutputByDesign;
import com.mdo0.services.strong.mdomanagement._2015_07.MultiDisciplinaryDataMgt.QueryNotificationResponse;
import com.mdo0.services.strong.mdomanagement._2015_07.MultiDisciplinaryDataMgt.SearchInput2;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.DesignArtifactInfo2;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.DomainRelevancyInput;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.FilterForDomainRelevancy;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.ImpactedDesignInstanceInfo2;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.InstancesToLinkResponse2;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.MDOOutput3;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.MDOSearchOutput3;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.NeedsValidationLinkInput;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.NeedsValidationLinkResponse;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.RelevancyInformation;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.SearchForLinkedInstances2Response;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.SearchInput3;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.SearchMDO3Response;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.UpdateDomainRelevancyResponse;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.UpdateLinkStatusToValidatedInput;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.UpdateLinksToValidatedResponse;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.UpdateMDInstanceToMDThreadResponse;
import com.mdo0.services.strong.mdomanagement._2018_06.MultiDisciplinaryDataMgt.GetDomainRelevancyInput;
import com.mdo0.services.strong.mdomanagement._2018_06.MultiDisciplinaryDataMgt.GetDomainRelevancyOfAnObjectResp;
import com.mdo0.services.strong.mdomanagement._2018_06.MultiDisciplinaryDataMgt.SplitDesignArtifactsResponse;
import com.mdo0.services.strong.mdomanagement._2018_06.MultiDisciplinaryDataMgt.SplitDesignInstancesResponse;
import com.rlz0.services.strong.realization._2014_10.RealizationManagement.ModelToModelRealizationContentResponse;
import com.teamcenter.clientx.AppXSession;
import com.teamcenter.services.strong.cad._2007_01.StructureManagement.CreateBOMWindowsResponse;
import com.teamcenter.services.strong.core._2006_03.DataManagement.CreateItemsResponse;
import com.teamcenter.services.strong.core._2015_07.DataManagement.GetDomainInput;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.client.model.strong.BusinessObject;
import com.teamcenter.soa.client.model.strong.Cpd0CollaborativeDesign;
import com.teamcenter.soa.client.model.strong.Cpd0DesignElement;
import com.teamcenter.soa.client.model.strong.ImanType;
import com.teamcenter.soa.client.model.strong.Item;
import com.teamcenter.soa.client.model.strong.ItemRevision;
import com.teamcenter.soa.client.model.strong.Lbr0Library;
import com.teamcenter.soa.client.model.strong.Mdl0Baseline;
import com.teamcenter.soa.client.model.strong.Mdl0BaselineDefinition;
import com.teamcenter.soa.client.model.strong.Mdl0BaselineRevision;
import com.teamcenter.soa.client.model.strong.Mdl0ModelElement;
import com.teamcenter.soa.client.model.strong.Mdl0SubsetDefinition;
import com.teamcenter.soa.client.model.strong.POM_object;
import com.teamcenter.soa.client.model.strong.WorkspaceObject;

/**
 * This Class contains the main function for the sample client. It executes the MDO related test cases.
 *
 */
public class MDOManagement
{
    static String pauseStr;
    static int failCnt;
    /**
     * @param args
     */
    public static void main( String[] args )
    {
        AppXSession session = null;
        failCnt = 0;
        try
        {
            if ( args.length > 0 )
            {
                if (args[0].equals( "-help" ) || args[0].equals( "-h" ) )
                {
                    System.out.println( "usage: java [-Dhost=http://server:port/tc] com.teamcenter.mdomanagement.MDOManagement" );
                    System.exit(0);
                }
            }

            // Get optional host information
            String serverHost = "http://localhost:7001/tc";
            String host = System.getProperty( "host" );
            if ( host != null && host.length() > 0 )
            {
                serverHost = host;
            }

            session = new AppXSession(serverHost);

            LogFile.getInstance();
            LogFile.write( "---------------------Login Test-----------------------------------" );

            // Establish a session with the Teamcenter Server
            session.login();
            LogFile.write( "Login successful.");

            // Pause for user response ( Enter )
            pauseStr = System.getProperty( "MDOManagement_Client_Pause_After_Each_UseCase" );
            if (pauseStr==null)
            {
                pauseStr = System.getenv("MDOManagement_Client_Pause_After_Each_UseCase");
            }

            String performanceTestingStr = System.getProperty( "MDOManagement_Client_Performance_Testing" );
            if (performanceTestingStr==null)
            {
                performanceTestingStr = System.getenv("MDOManagement_Client_Performance_Testing");
            }

            if( performanceTestingStr!= null && performanceTestingStr.equals( "true" ) )
            {
                LogFile.write( "------------------------------------------------------------------" );
                LogFile.write( "Running for Performance testing - Started" );

                executePerformanceTesting();

                LogFile.write( "Running for Performance testing - Completed" );
                LogFile.write( "------------------------------------------------------------------" );
                LogFile.write( " ------------------------Logout Test------------------------------" );

                // Terminate the session with the Teamcenter server
                session.logout();

                LogFile.write( "Log out success. Session terminated successfully." );
                if ( failCnt > 0 )
                {
                    LogFile.write( "Fail count = " + failCnt );
                }
                LogFile.write( "------------------------------------------------------------------" );
                LogFile.close();
                return;


            }

            executeMDThreadCases();

            executeQueryNotifications();

            executeMDOSearch2TestCases();

            executeDomainTestCases();

            LogFile.write( " ------------------------Logout Test------------------------------" );

            // Terminate the session with the Teamcenter server
            session.logout();

            LogFile.write( "Log out success. Session terminated successfully." );
            if ( failCnt > 0 )
            {
                LogFile.write( "Fail count = " + failCnt );
            }
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.close();
        }
        catch( Exception ex )
        {
            if( session != null)
            {
                session.logout();
            }
            LogFile.getInstance();
            LogFile.write("MDOManagement usecases execution failed." );
            ex.printStackTrace();
            LogFile.close();
        }
    }

    private static void executeMDThreadCases()
    {
        try
        {
            MDOTestUtils m_mdoTestUtils = new MDOTestUtils();
            MDOManagement mdoObj = new MDOManagement();
            com.teamcenter.soa.client.model.Property[] objNameProp;
            LogFile.write( "==================================================================" );
            LogFile.write( "Executing MDThread tests" );

            mdoObj.pauseBetweenUseCases( pauseStr );
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Usecase 1 - Creation of MDThread with multiple design artifacts" );
            List<ItemRevision> revsOfDAs = m_mdoTestUtils.createDesignArtifacts(4,"DAforCreateTest");
            WorkspaceObject newMDO = m_mdoTestUtils.createMDOWithDARevList(revsOfDAs,"MDOForCreate", "MDThread for Create test");
            LogFile.write( "Completed Usecase 1 - Creation of MDThread with multiple design artifacts");

            // Pause for user input ( Enter )
            mdoObj.pauseBetweenUseCases( pauseStr );
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Usecase 2 - Update of MDThread with add design artifacts, remove design artifacts and properties update" );

            List< ItemRevision > toAddDAList = m_mdoTestUtils.createDesignArtifacts(2, "DAstoAdd");
            List< ItemRevision > toRemoveDAList = new ArrayList<ItemRevision>();
            toRemoveDAList.add(revsOfDAs.get(2));
            toRemoveDAList.add(revsOfDAs.get(3));

            Map<String,String[]> attrs = new HashMap<String,String[]>();
            String[] nameValArr = new String[1];
            nameValArr[0]="MDOAfterUpdate";
            String[] descValArr = new String[1];
            descValArr[0]="MDThread created using sample client is now updated";
            attrs.put("object_name",nameValArr);
            attrs.put("object_desc",descValArr);
            WorkspaceObject updatedMDO = m_mdoTestUtils.updateMDO(newMDO, toAddDAList, toRemoveDAList, attrs);
            if ( updatedMDO == null )
            {
                LogFile.write("MDThread update failed");
                ++failCnt;
            }
            LogFile.write( "Completed Usecase 2 - Update of MDThread with add design artifacts, remove design artifacts and properties update" );
            // Pause for user input ( Enter )
            mdoObj.pauseBetweenUseCases( pauseStr );

            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Usecase 3 - Search for MDThread with design artifacts combination only" );

            List< ItemRevision > DAListForSearch = new ArrayList< ItemRevision >();
            DAListForSearch.add(toAddDAList.get(0));
            DAListForSearch.add(toAddDAList.get(1));
            List<WorkspaceObject> searchResults = m_mdoTestUtils.searchMDOsWithOnlyDesigns(DAListForSearch);
            if ( searchResults.size() == 1 )
            {
                LogFile.write("searchMDOsWithOnlyDesigns succeeded");
                objNameProp = MDOTestUtils.getObjectProperties(searchResults.get(0), new String[] {"object_name"});
                LogFile.write( "MDThread named: " + objNameProp[0].getDisplayableValue() + " uid: " + searchResults.get(0).getUid() + "  is fetched." );
            }
            else
            {
                LogFile.write("searchMDOsWithOnlyDesigns failed");
                ++failCnt;
            }
            LogFile.write( "Completed Usecase 3 - Search for MDThread with design artifacts combination only" );
            // Pause for user input ( Enter )
            mdoObj.pauseBetweenUseCases( pauseStr );

            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Usecase 4 - Search for MDThread with design artifacts combination and MDThread properties" );

            Map<String, String[]> mdoPropertiesCriteria = new HashMap<String, String[]>();
            String propValues[] =  {"*Update"};
            mdoPropertiesCriteria.put("object_name", propValues);
            LogFile.write("MDThread property used is object_name and value provided for search is *Update ");
            searchResults = m_mdoTestUtils.searchMDOsWithDesignsAndMDOProps(DAListForSearch, mdoPropertiesCriteria);
            if ( searchResults.size() == 1 )
            {
                LogFile.write("searchMDOsWithDesignsAndMDOProps succeeded");
                objNameProp = MDOTestUtils.getObjectProperties(searchResults.get(0), new String[] {"object_name"});
                LogFile.write( "MDThread named: " + objNameProp[0].getDisplayableValue() + " uid: " + searchResults.get(0).getUid() + " is fetched." );
            }
            else
            {
                LogFile.write("searchMDOsWithDesignsAndMDOProps failed");
                ++failCnt;
            }
            LogFile.write( "Completed Usecase 4 - Search for MDThread with design artifacts combination MDThread properties" );
            // Pause for user input ( Enter )
            mdoObj.pauseBetweenUseCases( pauseStr );

            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Usecase 5 - Search for MDThread with MDThread properties only" );

            LogFile.write("MDThread property used is object_name and value provided for search is *Update ");

            searchResults = m_mdoTestUtils.searchMDOPropsOnly(mdoPropertiesCriteria);
            if ( searchResults.size() > 0 )
            {
                LogFile.write("searchMDOPropsOnly succeeded");
                LogFile.write( searchResults.size() + " MDThread(s)  are fetched." );
                for(int inx=0 ; inx < searchResults.size() ; inx++)
                {
                    objNameProp = MDOTestUtils.getObjectProperties(searchResults.get(inx), new String[] {"object_name"});
                    LogFile.write( "MDThread named: " + objNameProp[0].getDisplayableValue() + " uid: " + searchResults.get(inx).getUid() + "  is fetched." );
                }
            }
            else
            {
                LogFile.write("searchMDOPropsOnly failed");
                ++failCnt;
            }
            LogFile.write( "Completed Usecase 5 - Search for MDThread with MDThread properties only" );
            // Pause for user input ( Enter )
            mdoObj.pauseBetweenUseCases( pauseStr );

            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Usecase 6a - Consuming DA of MDThread in BVR" );

            List< Item > topItemList = new ArrayList< Item >();
            List< ItemRevision > topItemRevList = new ArrayList< ItemRevision >();

            List< Item > childItemList = new ArrayList< Item >();
            List< ItemRevision > childItemRevList = new ArrayList< ItemRevision >();

            LogFile.write(" Creating Structure ");
            m_mdoTestUtils.createItems( 1, "TopBVRItem", "Item", topItemList, topItemRevList );
            m_mdoTestUtils.createItems( 5, "ChildBVRItem", "Item", childItemList, childItemRevList );
            CreateBOMWindowsResponse bomResp = m_mdoTestUtils.createStructure(topItemRevList.get(0), childItemRevList);


            ModelObject bvrTopItem = (ModelObject)topItemRevList.get(0);
            objNameProp = MDOTestUtils.getObjectProperties(bvrTopItem, new String[] {"object_string"});
            LogFile.write( "BVR assembly details: Top Line item rev name: " + objNameProp[0].getDisplayableValue()+ " uid: " + topItemRevList.get(0).getUid() );

            //Add DA_1 and DA_2 from MDO1 to the bvr of TopBVRItem
            List <ItemRevision> childItemRevAddList = new ArrayList<ItemRevision>();
            childItemRevAddList.add(revsOfDAs.get(0));
            childItemRevAddList.add(revsOfDAs.get(1));

            LogFile.write( "Design Artifact named: " + revsOfDAs.get(0).get_object_string()+ " uid: " + revsOfDAs.get(0).getUid() + " which is part of MDThread is consumed in BVR assembly" );
            LogFile.write( "Design Artifact named: " + revsOfDAs.get(1).get_object_string()+ " uid: " + revsOfDAs.get(1).getUid() + " which is part of MDThread is consumed in BVR assembly" );

            m_mdoTestUtils.addBomlines(bomResp,childItemRevAddList);
            LogFile.write( "Completed Usecase 6a - Consuming DA of MDThread in BVR" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Usecase 6b - Remove DA from its MDThread" );


            List< ItemRevision > toAddDAList2 = new ArrayList<ItemRevision>();
            List< ItemRevision > toRemoveDAList2 = new ArrayList<ItemRevision>();
            toRemoveDAList2.add(revsOfDAs.get(1));


            WorkspaceObject updatedMDO2 = m_mdoTestUtils.updateMDO(updatedMDO, toAddDAList2, toRemoveDAList2, attrs);

            if ( updatedMDO2 != null )
            {
                LogFile.write("Usecase6b succeeded");
            }
            else
            {
                LogFile.write("Usecase6b failed");
                ++failCnt;
            }
            LogFile.write( "Completed Usecase 6b - Remove DA from its MDThread" );
            // Pause for user input ( Enter )
            mdoObj.pauseBetweenUseCases( pauseStr );

            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Usecase 7 - Realize single IR which is part of MDThread" );

            String de1Objstring="";
            ModelObject cdtest = m_mdoTestUtils.createCD("TestCD_1");
            ModelObject de1 = m_mdoTestUtils.createReuseDEWithCD(revsOfDAs.get(0),"ReuseDE1",cdtest);
            if ( de1 == null )
            {
                LogFile.write("Reuse creation failed");
                ++failCnt;
            }
            else
            {
                LogFile.write("Reuse creation succeeded");
                objNameProp = MDOTestUtils.getObjectProperties(de1, new String[] {"object_string"});
                de1Objstring = objNameProp[0].getDisplayableValue();
                LogFile.write( "Design Element named: " + de1Objstring+ " uid: " + de1.getUid() + " is realized." );


            }
            LogFile.write( "Completed Usecase 7 - Realize single IR which is part of MDThread" );
            // Pause for user input ( Enter )
            mdoObj.pauseBetweenUseCases( pauseStr );

            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Usecase 8 - Link design instances with an MDInstance" );

            String de2Objstring="";
            ModelObject cdtest2 = m_mdoTestUtils.createCD("TestCD_2");
            ModelObject de2 = m_mdoTestUtils.createReuseDEWithCD(revsOfDAs.get(1),"ReuseDE2",cdtest2);

            if ( de2 == null )
            {
                LogFile.write("Reuse creation failed for de2");
                ++failCnt;
            }
            else
            {

                objNameProp = MDOTestUtils.getObjectProperties(de2, new String[] {"object_string"});
                de2Objstring =  objNameProp[0].getDisplayableValue();
                LogFile.write( "Design Element named: " + de2Objstring + " uid: " + de2.getUid() + " is realized." );


            }
            if( de1Objstring != null && de1 != null)
            {
                LogFile.write( "Design Element named: " + de1Objstring + " uid: " + de1.getUid() + " is linked imprecisely." );
            }
            if( de2Objstring != null && de2 != null)
            {
                LogFile.write( "Design Element named: " + de2Objstring + " uid: " + de2.getUid() + " is linked precisely." );
            }

            List<DesignInstancesData> didList = new ArrayList<DesignInstancesData>();
            DesignInstancesData did[] = new DesignInstancesData[2];
            did[0] = new DesignInstancesData();
            did[0].designInstance = (POM_object) de1;
            did[0].isPreciseLink = false;
            did[1] = new DesignInstancesData();
            did[1].designInstance = (POM_object) de2;
            did[1].isPreciseLink = true;
            didList.add(did[0]);
            didList.add(did[1]);
            ModelObject[] mdit = m_mdoTestUtils.createInstanceThread(didList);
            if ( mdit[0] == null )
            {
                LogFile.write("createInstanceThread failed");
                ++failCnt;
            }
            else
            {
                LogFile.write("createInstanceThread succeeded");
                LogFile.write( "MDInstance with uid: " + mdit[0].getUid() + " is created." );
            }
            LogFile.write( "Completed Usecase 8 - Link design instances with an MDInstance" );
            // Pause for user input ( Enter )
            mdoObj.pauseBetweenUseCases( pauseStr );

            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Usecase 9a - Find MDInstance from design instances using imprecise option" );
            if( de1Objstring != null && de1 != null)
            {
                LogFile.write( "Design Element named: " + de1Objstring + " uid: " + de1.getUid() + " is used for search" );
            }

            List<DesignInstancesData> didList2 = new ArrayList<DesignInstancesData>();
            DesignInstancesData did2[] = new DesignInstancesData[1];
            did2[0] = new DesignInstancesData();
            did2[0].designInstance = (POM_object) de1;
            did2[0].isPreciseLink = false;
            didList2.add(did2[0]);
            LinkedInstances impactedInstances[] = m_mdoTestUtils.searchForLinkedDesignInstances(didList2);
            if ( impactedInstances == null )
            {
                LogFile.write("searchForLinkedDesignInstances failed");
                ++failCnt;
            }
            else
            {
                LogFile.write("searchForLinkedDesignInstances succeeded");
                LogFile.write( impactedInstances.length + " MDInstance(s) are fetched." );
                for(int inx=0 ; inx < impactedInstances.length ; inx++)

                    LogFile.write( "Design Element named: "+(impactedInstances[inx].instances[0]).get_object_string() + "uid: " + (impactedInstances[inx].instances[0]).getUid() );
            }
            LogFile.write( "Completed Usecase 9a - Find MDInstance from design instances using imprecise option" );

            // Pause for user input ( Enter )
            mdoObj.pauseBetweenUseCases( pauseStr );

            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Usecase 9b - Find MDInstance from design instances using precise option" );
            if( de2Objstring != null && de2 != null)
            {
                LogFile.write( "Design Element named: " + de2Objstring + " uid: " + de2.getUid() + " is used for search" );
            }

            List<DesignInstancesData> didList3 = new ArrayList<DesignInstancesData>();
            DesignInstancesData did3[] = new DesignInstancesData[1];
            did3[0] = new DesignInstancesData();
            did3[0].designInstance = (POM_object) de2;
            did3[0].isPreciseLink = true;
            didList3.add(did3[0]);
            LinkedInstances impactedInstances2[] = m_mdoTestUtils.searchForLinkedDesignInstances(didList3);
            if ( impactedInstances2 == null )
            {
                LogFile.write("searchForLinkedDesignInstances failed");
                ++failCnt;
            }
            else
            {
                LogFile.write("searchForLinkedDesignInstances succeeded");
                LogFile.write( impactedInstances2.length + " MDInstance(s) are fetched." );
                for(int iny=0 ; iny < impactedInstances2.length ; iny++)
                {
                   LogFile.write( "Design Element named: "+(impactedInstances2[iny].instances[0]).get_object_string() + "uid: " + (impactedInstances2[iny].instances[0]).getUid() );
                }
            }
            LogFile.write( "Completed Usecase 9b - Find MDInstance from design instances using precise option" );
            // Pause for user input ( Enter )
            mdoObj.pauseBetweenUseCases( pauseStr );


            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Usecase 10 - Find MDThread from design instances" );

            if( de1Objstring != null && de1 != null)
            {
                LogFile.write( "Design Element named: " + de1Objstring + " uid: " + de1.getUid() + " is used for search for MDThread" );
            }

            MDOSearchOutput mdoInfo[] = m_mdoTestUtils.searchForMDO(didList2);
            if ( mdoInfo.length == 0 || mdoInfo[0].mdoOutputs == null|| mdoInfo[0].mdoOutputs.length == 0)
            {
                LogFile.write("No MDOs found");
            }
            else if (mdoInfo[0].mdoOutputs[0].associatedDesignArtifact.length != 3 )
            {
                LogFile.write("searchForMDO failed, wrong number of associated design artifacts");
                ++failCnt;
            }
            else
            {
                LogFile.write("searchForMDO succeeded");
                LogFile.write( mdoInfo[0].mdoOutputs.length + " MDThread(s) are fetched." );
                for(int inx=0 ; inx < mdoInfo[0].mdoOutputs.length ; inx++)
                    LogFile.write( "MDThread Name: " + (mdoInfo[0].mdoOutputs[inx].mdoObject).get_object_string() + " uid: " + ((ModelObject)(mdoInfo[0].mdoOutputs[inx].mdoObject)).getUid() );
            }
            LogFile.write( "Completed Usecase 10 - Find MDThread from design instances" );
            // Pause for user input ( Enter )
            mdoObj.pauseBetweenUseCases( pauseStr );

            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Usecase 11.1 - Unlink design instances from an MDInstance" );

            int delObjs = m_mdoTestUtils.unlinkFromInstanceThread(didList);
            if ( delObjs >= 3 )
            {
                LogFile.write("unlinkFromInstanceThread succeeded");
                for(int inx=0 ; inx < didList.size() ; inx++)
                    LogFile.write( "Design Instance with uid: " + didList.get(inx).designInstance.getUid() + " is unlinked" );
                LogFile.write( "MDI with Uid:" + mdit[0].getUid() + " is deleted" );
            }
            else
            {
                LogFile.write("unlinkFromInstanceThread failed");
                ++failCnt;
            }
            LogFile.write( "Completed Usecase 11.1 - Unlink design instances from an MDInstance" );
            // Pause for user input ( Enter )
            mdoObj.pauseBetweenUseCases( pauseStr );

            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Usecase 11.2 - Unlinking of a released Design Element from MDInstance" );
            mdit = m_mdoTestUtils.createInstanceThread(didList);
            WorkspaceObject[] deList = new WorkspaceObject[]{ (WorkspaceObject)de1 };
            m_mdoTestUtils.applyReleaseStatus( deList, "TCM Released" );
            delObjs = m_mdoTestUtils.unlinkFromInstanceThread(didList);
            if ( delObjs == 0 )
            {
                LogFile.write("unlinkFromInstanceThread did not unlink released Design Element succeeded");
            }
            else
            {
                LogFile.write("unlinkFromInstanceThread should not unlink released Design Element failed");
            }
            LogFile.write( "Completed Usecase 11.2 - Unlinking of a released Design Element from MDInstance" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Usecase 12 - Realize Design Artifact" );

            // Create 3 Items
            List <ItemRevision> mdoItemRevList = m_mdoTestUtils.createDesignArtifacts( 3, "DAfor4GD" );

            // Create a reuse DE from 1st IR. This will invoke Cpd0Realize_Design_Object event.
            // Since the underlying IR is not part of MDThread association, the condition
            // Mdo0IsDesignElementRealizeNotifyReqd will return false and Mdo0Realize handler
            // is not called. No entry is made to Mdo0InstanceToArtifactMap.

            //Creating Project
            POM_object testProj1 = m_mdoTestUtils.createProject( "Testproj1", "Test Project 1" );
            m_mdoTestUtils.setProjectAsMultiDisciplinary(testProj1);

            ModelObject cdtest3 = m_mdoTestUtils.createCD("TestCD_3");

            ModelObject[] collaborativeDesigns = new ModelObject[1];
            collaborativeDesigns[0] = cdtest3;
            m_mdoTestUtils.assignCDsToProject(collaborativeDesigns, testProj1);

            m_mdoTestUtils.createReuseDEWithCD( mdoItemRevList.get( 0 ),"ReuseDE3",cdtest3 );

            // Associate the created 3 IR's through an MDThread.
            m_mdoTestUtils.createMDOWithDARevList( mdoItemRevList, "MDOfor4GD", "MDThread for 4GD Test" );

            // Create a reuse DE from 2nd IR. This will invoke Cpd0Realize_Design_Object event.
            // Since the underlying IR is part of MDThread association, the condition
            // Mdo0IsDesignElementRealizeNotifyReqd will return true and Mdo0Realize handler
            // is called. Entry is made to Mdo0InstanceToArtifactMap.
            ModelObject cdtest4 = m_mdoTestUtils.createCD("TestCD_4");
            collaborativeDesigns[0] = cdtest4;
            m_mdoTestUtils.assignCDsToProject(collaborativeDesigns, testProj1);

            m_mdoTestUtils.createReuseDEWithCD( mdoItemRevList.get( 1 ),"ReuseDE4",cdtest4 );

            LogFile.write( "Completed Usecase 12 - Realize Design Artifact" );

            mdoObj.pauseBetweenUseCases( pauseStr );
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Usecase 15.1 - Search for MDThread using SearchForArtifactsUsingInstance3" );
            //Search for MDThread using SearchForArtifactsUsingInstance3 SOA
            executeSearchForArtifactsUsingInstance3();
            LogFile.write( "Completed Usecase 15.1 - Search for MDThread using SearchForArtifactsUsingInstance3" );

            mdoObj.pauseBetweenUseCases( pauseStr );
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Usecase 15.2a - Revising a Design Artifact precisely linked with an MDThread" );
            executeReviseDesignArtifact();
            LogFile.write( "Completed Usecase 15.2a - Revising a Design Artifact precisely linked with an MDThread" );

            mdoObj.pauseBetweenUseCases( pauseStr );
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Usecase 15.2b - SaveAs on a Design Artifact precisely linked with an MDThread" );
            executeSaveAsDesignArtifact();
            LogFile.write( "Completed Usecase 15.2b - SaveAs on a Design Artifact precisely linked with an MDThread" );

            mdoObj.pauseBetweenUseCases( pauseStr );
         //   LogFile.write( "------------------------------------------------------------------" );
         //   LogFile.write( "Executing Usecase 15.2c - Revising a Library Element precisely linked with an MDThread" );
         //   executeReviseLibraryElement();
         //   LogFile.write( "Completed Usecase 15.2c - Revising a Library Element precisely linked with an MDThread" );

         //   mdoObj.pauseBetweenUseCases( pauseStr );
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Usecase 15.3 - Tests carryForwardMDT on unrelated Design Artifacts" );
            executeCarryForwardMDTDiffDesignArtifacts();
            LogFile.write( "Completed Usecase 15.3 - Tests carryForwardMDT on unrelated Design Artifacts" );

            mdoObj.pauseBetweenUseCases( pauseStr );
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Usecase 15.4a - Revising a Design Element precisely linked with an MDInstance" );
            executeReviseDesignElement();
            LogFile.write( "Completed Usecase 15.4a - Revising a Design Element precisely linked with an MDInstance" );

            mdoObj.pauseBetweenUseCases( pauseStr );
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Usecase 15.4b - SaveAs on a Design Element precisely linked with an MDInstance" );
            executeSaveAsDesignElement();
            LogFile.write( "Completed Usecase 15.4b - SaveAs on a Design Element precisely linked with an MDInstance" );

            mdoObj.pauseBetweenUseCases( pauseStr );
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Usecase 15.5 - Tests carryForwardMDI on unrelated Design Elements" );
            executeCarryForwardMDIDiffDesignElements();
            LogFile.write( "Completed Usecase 15.5 - Tests carryForwardMDI on unrelated Design Elements" );

            mdoObj.pauseBetweenUseCases( pauseStr );
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Usecase 15.6 - Linking MDInstance with MDThread " );
            executeMDInstanceLinkingWithMDTUseCases();
            LogFile.write( "Completed Usecase 15.6 - Linking MDInstance with MDThread" );

            mdoObj.pauseBetweenUseCases( pauseStr );
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Usecase 15.7 - Query and Update of MDT and MDI associations with needs validation " );
            executeQueryAndUpdateOfMDOAndMDIAssociations();
            LogFile.write( "Completed Usecase 15.7 - Query and Update of MDT and MDI associations with needs validation" );

            mdoObj.pauseBetweenUseCases( pauseStr );
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Usecase 15.8a - updateDomainRelevancy for adding and removing domains" );
            executeUpdateDomainRelevancyAddAndRemove();
            LogFile.write( "Completed Usecase 15.8a - updateDomainRelevancy for adding and removing domains" );

            mdoObj.pauseBetweenUseCases( pauseStr );
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Usecase 15.8b - GetDomainRelevancyDASingle for getting domain relevancy of Design Artifact" );
            executeGetDomainRelevancyDASingle();
            LogFile.write( "Completed Usecase 15.8b - GetDomainRelevancyDASingle for getting domain relevancy of Design Artifact" );

            mdoObj.pauseBetweenUseCases( pauseStr );
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Usecase 15.8c - GetDomainRelevancyDAAll for getting domain relevancy of Design Artifact" );
            executeGetDomainRelevancyDAAll();
            LogFile.write( "Completed Usecase 15.8c - GetDomainRelevancyDAAll for getting domain relevancy of Design Artifact" );

            mdoObj.pauseBetweenUseCases( pauseStr );
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Usecase 15.8d - DomainRelevancyCases various combinations" );
            executeDomainRelevancyCases();
            LogFile.write( "Completed Usecase 15.8d - DomainRelevancyCases various combinations" );

            mdoObj.pauseBetweenUseCases( pauseStr );
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Usecase 15.8e - DomainRelevancyWithECN various combinations" );
            executeDomainRelevancyWithECN();
            LogFile.write( "Completed Usecase 15.8e - DomainRelevancyWithECN various combinations" );

            mdoObj.pauseBetweenUseCases( pauseStr );
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Usecase 15.9a - Splitting a Design Artifact from an MDThread" );
            executeSplitDesignArtifactFromMDTs();
            LogFile.write( "Completed Usecase 15.9a - Splitting a Design Artifact from an MDThread" );

            mdoObj.pauseBetweenUseCases( pauseStr );
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Usecase 15.9b - Splitting a Library Element from an MDThread" );
            executeSplitLibraryElementFromMDTs();
            LogFile.write( "Completed Usecase 15.9b - Splitting a Library Element from an MDThread" );

            mdoObj.pauseBetweenUseCases( pauseStr );
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Usecase 15.10 - Splitting a Design Instance from an MDInstance" );
            executeSplitDesignInstanceFromMDI();
            LogFile.write( "Completed Usecase 15.10 - Splitting a Design Instance from an MDInstance" );

            mdoObj.pauseBetweenUseCases( pauseStr );
            LogFile.write( "------------------------------------------------------------------" );

        }
        catch(Exception ex)
        {
              ex.printStackTrace();
        }
        LogFile.write( "==================================================================" );
    }

    private static void executeMDOSearch2TestCases() throws Exception
    {
        MDOTestUtils m_mdoTestUtils = new MDOTestUtils();
        CreateItemsResponse response;
        LogFile.write( "Usecase 14 : Search MDO2" );
        LogFile.write( "Executing Usecase 14 - Search MDO2" );


        // Create DA1/A - Mechanical (Part)
        response = m_mdoTestUtils.createDesignArtifactsWithType("SearchDA1", "Part", "Description for SearchDA1" );
        Item searchDA1 = response.output[0].item;
        ItemRevision searchDA1Rev = response.output[0].itemRev;

        // Create DA2/A - Electrical (PSSignal)
        response = m_mdoTestUtils.createDesignArtifactsWithType("SearchDA2", "PSSignal", "Description for SearchDA2" );
        Item searchDA2 = response.output[0].item;
        ItemRevision searchDA2Rev = response.output[0].itemRev;

        // Create DA3/A - Electrical (PSSignal)
        response = m_mdoTestUtils.createDesignArtifactsWithType("SearchDA3", "PSSignal", "Description for SearchDA3" );
        Item searchDA3 = response.output[0].item;
        ItemRevision searchDA3Rev = response.output[0].itemRev;

        // Create DA4/A - Automation (PSConnection)
        response = m_mdoTestUtils.createDesignArtifactsWithType("SearchDA4", "PSConnection", "Description for SearchDA4");
        Item searchDA4 = response.output[0].item;
        ItemRevision searchDA4Rev = response.output[0].itemRev;
        // Create DA5/A - Electrical (PSSignal)

        response = m_mdoTestUtils.createDesignArtifactsWithType("SearchDA5", "PSSignal", "Description for SearchDA5" );
        Item searchDA5 = response.output[0].item;
        ItemRevision searchDA5Rev = response.output[0].itemRev;
        // Create DA6/A - Electrical (PSSignal)

        response = m_mdoTestUtils.createDesignArtifactsWithType("SearchDA6", "PSSignal", "Description for SearchDA6" );
        Item searchDA6 = response.output[0].item;
        ItemRevision searchDA6Rev = response.output[0].itemRev;
        // Create DA7/A - Automation (PSConnection)

        response = m_mdoTestUtils.createDesignArtifactsWithType("SearchDA7", "PSConnection", "Description for SearchDA7" );
        Item searchDA7 = response.output[0].item;
        ItemRevision searchDA7Rev = response.output[0].itemRev;

        // Create MDO1 with DA1/A, DA2/A, DA3/A, DA4/A
        List<ItemRevision> revsOfDAsForMdo1 = new ArrayList<ItemRevision>();
        revsOfDAsForMdo1.add(searchDA1Rev);
        revsOfDAsForMdo1.add(searchDA2Rev);
        revsOfDAsForMdo1.add(searchDA3Rev);
        revsOfDAsForMdo1.add(searchDA4Rev);
        WorkspaceObject searchMdo1 = m_mdoTestUtils.createMDOWithDARevList(revsOfDAsForMdo1,"Srch2_MDO1", "MDO1 for MDO Search 2 test");

        // Create MDO2 with  DA2/A, DA3/A
        List<ItemRevision> revsOfDAsForMdo2 = new ArrayList<ItemRevision>();
        revsOfDAsForMdo2.add(searchDA2Rev);
        revsOfDAsForMdo2.add(searchDA3Rev);
        WorkspaceObject searchMdo2 = m_mdoTestUtils.createMDOWithDARevList(revsOfDAsForMdo2,"Srch2_MDO2", "MDO2 for MDO Search 2 test");

        // Create MDO3 with  DA4/A, DA5/A, DA6/A, DA7/A
        List<ItemRevision> revsOfDAsForMdo3 = new ArrayList<ItemRevision>();
        revsOfDAsForMdo3.add(searchDA4Rev);
        revsOfDAsForMdo3.add(searchDA5Rev);
        revsOfDAsForMdo3.add(searchDA6Rev);
        revsOfDAsForMdo3.add(searchDA7Rev);
        WorkspaceObject searchMdo3 = m_mdoTestUtils.createMDOWithDARevList(revsOfDAsForMdo3,"Srch2_MDO3", "MDO3 for MDO Search 2 test");

        LogFile.write( "------------------------------------------------------------------" );
        LogFile.write( "Executing Search MDO2 Test case 14.1 - Search based on MDThread Criteria only" );
        // Test case 1: Search based on MDThread Criteria only
        Map<String, String[]> mdoPropertiesCriteria = new HashMap<String, String[]>();
        String propValues[] =  {"Srch2_MDO2"};
        mdoPropertiesCriteria.put("object_name", propValues);
        SearchInput2[] inputs = new SearchInput2[1];
        inputs[0] = new SearchInput2();
        inputs[0].clientId = "Search";
        inputs[0].mdoCriteria = mdoPropertiesCriteria;
        com.teamcenter.soa.client.model.Property[] objNameProp;
        List<WorkspaceObject> searchResults = m_mdoTestUtils.executeMDOSearch2(inputs);
        if ( searchResults.size() > 0 )
        {
            LogFile.write("Completed Search MDO2 Test case 14.1 - Search based on MDThread Criteria only");
            for(int inx = 0; inx < searchResults.size(); inx++ )
            {
                objNameProp = MDOTestUtils.getObjectProperties(searchResults.get(inx), new String[] {"object_name"});
                LogFile.write( "MDThread named: " + objNameProp[0].getDisplayableValue() + " uid: " + searchResults.get(inx).getUid() + "  is fetched." );
            }
        }
        else
        {
            LogFile.write("Failed Search MDO2 Test case 14.1 - Search based on MDThread Criteria only");
        }
        MDOManagement mdoObj = new MDOManagement();
        mdoObj.pauseBetweenUseCases( pauseStr );

        LogFile.write( "------------------------------------------------------------------" );
        LogFile.write( "Executing Search MDO2 Test case 14.2 - Search based on design artifacts only" );
        // Test case 2: Search based on design artifacts only
        DesignArtifactInputsForSearch  DAInputsForSrch = new DesignArtifactInputsForSearch();
        WorkspaceObject[] DAList = new WorkspaceObject[2];
        DAList[0] = searchDA2Rev;
        DAList[1] = searchDA3Rev;
        DAInputsForSrch.designArtifactsObjects = DAList;
        SearchInput2[] inputs1 = new SearchInput2[1];
        inputs1[0] = new SearchInput2();
        inputs1[0].clientId = "Search";
        inputs1[0].designArtifactInputs = DAInputsForSrch;

        searchResults = m_mdoTestUtils.executeMDOSearch2(inputs1);
        if ( searchResults.size() > 0 )
        {
              LogFile.write("Completed Search MDO2 Test case 14.2 - Search based on design artifacts only");
            for(int inx = 0; inx < searchResults.size(); inx++ )
            {
                 objNameProp = MDOTestUtils.getObjectProperties(searchResults.get(inx), new String[] {"object_name"});
                LogFile.write( "MDThread named: " + objNameProp[0].getDisplayableValue() + " uid: " + searchResults.get(inx).getUid() + "  is fetched." );
            }
        }
        else
        {
            LogFile.write("Failed Search MDO2 Test case 14.2 - Search based on design artifacts only");
        }
        mdoObj.pauseBetweenUseCases( pauseStr );

        LogFile.write( "------------------------------------------------------------------" );
        LogFile.write( "Executing Search MDO2 Test case 14.3 - Search based on design artifacts and mdo criteria without filter" );
        // Test case 3: Search based on design artifacts and mdo criteria without filter
        // Pause for user input ( Enter )
        SearchInput2[] inputs2 = new SearchInput2[1];
        inputs2[0] = new SearchInput2();
        inputs2[0].clientId = "Search";
        DesignArtifactInputsForSearch  DAInputsForSrch2 = new DesignArtifactInputsForSearch();
        WorkspaceObject[] DAList2 = new WorkspaceObject[2];
        DAList2[0] = searchDA2Rev;
        DAList2[1] = searchDA3Rev;
        DAInputsForSrch2.designArtifactsObjects = DAList2;
        inputs2[0].designArtifactInputs = DAInputsForSrch2;
        Map<String, String[]> mdoPropertiesCriteria2 = new HashMap<String, String[]>();
        String propValues2[] =  {"Srch2_MDO2"};
        mdoPropertiesCriteria2.put("object_name", propValues2);
        inputs2[0].mdoCriteria = mdoPropertiesCriteria2;
        searchResults = m_mdoTestUtils.executeMDOSearch2(inputs2);
        if ( searchResults.size() > 0 )
        {
            LogFile.write("Completed Search MDO2 Test case 14.3 - Search based on design artifacts and mdo criteria without filter");
            for(int inx = 0; inx < searchResults.size(); inx++ )
            {
                objNameProp = MDOTestUtils.getObjectProperties(searchResults.get(inx), new String[] {"object_name"});
                LogFile.write( "MDThread named: " + objNameProp[0].getDisplayableValue() + " uid: " + searchResults.get(inx).getUid() + "  is fetched." );
            }
        }
        else
        {
            LogFile.write("Failed Search MDO2 Test case 14.3 - Search based on design artifacts and mdo criteria without filter");
        }

        mdoObj.pauseBetweenUseCases( pauseStr );
        LogFile.write( "------------------------------------------------------------------" );
        LogFile.write( "Executing Search MDO2 Test case 14.4 - Search based on MDThread Criteria and filter - DA imantype ");
        // Test case 4: Search based on MDThread Criteria and filter - DA imantype
        Map<String, String[]> mdoPropertiesCriteria3 = new HashMap<String, String[]>();
        String propValues3[] =  {"Srch2_*"};
        mdoPropertiesCriteria3.put("object_name", propValues3);
        FilterForSearch filterCritera3 = new FilterForSearch();
        ImanType Im1 = new ImanType(searchDA4Rev.getTypeObject(), searchDA4Rev.getTypeObject().getUid());
        filterCritera3.typeObj = Im1;
        SearchInput2[] inputs3 = new SearchInput2[1];
        inputs3[0] = new SearchInput2();
        inputs3[0].clientId = "Search";
        inputs3[0].mdoCriteria = mdoPropertiesCriteria3;
        inputs3[0].filterCriteria = filterCritera3;
        searchResults = m_mdoTestUtils.executeMDOSearch2(inputs3);
        if ( searchResults.size() > 0 )
        {
             LogFile.write("Completed Search MDO2 Test case 14.4 - Search based on MDThread Criteria and filter - DA imantype");
            for(int inx = 0; inx < searchResults.size(); inx++ )
            {
                objNameProp = MDOTestUtils.getObjectProperties(searchResults.get(inx), new String[] {"object_name"});
                LogFile.write( "MDThread named: " + objNameProp[0].getDisplayableValue() + " uid: " + searchResults.get(inx).getUid() + "  is fetched." );
            }
        }
        else
        {
            LogFile.write("Failed Search MDO2 Test case 14.4 - Search based on MDThread Criteria and filter - DA imantype");
        }

        mdoObj.pauseBetweenUseCases( pauseStr );
        LogFile.write( "------------------------------------------------------------------" );
        LogFile.write( "Executing Search MDO2 Test case 14.5 - Search based on MDThread Criteria and filter - design criteria" );
        // Test case 5: Search based on MDThread Criteria and filter - design criteria
        Map<String, String[]> mdoPropertiesCriteria4 = new HashMap<String, String[]>();
        String propValues4[] =  {"Srch2_*"};
        mdoPropertiesCriteria4.put("object_name", propValues4);
        FilterForSearch filterCritera4 = new FilterForSearch();

        Map<String, String[]> designPropertiesCriteria4 = new HashMap<String, String[]>();
        String designPropValues4[] =  {"SearchDA6"};
        designPropertiesCriteria4.put("object_name", designPropValues4);

        filterCritera4.attributeValues = designPropertiesCriteria4;
        SearchInput2[] inputs4 = new SearchInput2[1];
        inputs4[0] = new SearchInput2();
        inputs4[0].clientId = "Search";
        inputs4[0].mdoCriteria = mdoPropertiesCriteria4;
        inputs4[0].filterCriteria = filterCritera4;
        searchResults = m_mdoTestUtils.executeMDOSearch2(inputs4);
        if ( searchResults.size() > 0 )
        {
            LogFile.write("Completed Search MDO2 Test case 14.5 - Search based on MDThread Criteria and filter - design criteria");
            for(int inx = 0; inx < searchResults.size(); inx++ )
            {
                objNameProp = MDOTestUtils.getObjectProperties(searchResults.get(inx), new String[] {"object_name"});
                LogFile.write( "MDThread named: " + objNameProp[0].getDisplayableValue() + " uid: " + searchResults.get(inx).getUid() + "  is fetched." );
            }
        }
        else
        {
            LogFile.write("Failed Search MDO2 Test case 14.5 - Search based on MDThread Criteria and filter - design criteria");
        }

        mdoObj.pauseBetweenUseCases( pauseStr );
        LogFile.write( "------------------------------------------------------------------" );
        LogFile.write( "Executing Search MDO2 Test case 14.6 - Search based on MDThread Criteria, design artifacts and filter - DA imantype and design criteria" );
        // Test case 6: Search based on MDThread Criteria, design artifacts and filter - DA imantype and design criteria
        Map<String, String[]> mdoPropertiesCriteria5 = new HashMap<String, String[]>();
        String propValues5[] =  {"Srch2_*"};
        mdoPropertiesCriteria5.put("object_name", propValues5);
        DesignArtifactInputsForSearch  DAInputsForSrch5 = new DesignArtifactInputsForSearch();
        WorkspaceObject[] DAList5 = new WorkspaceObject[1];
        DAList5[0] = searchDA2Rev;
        DAInputsForSrch5.designArtifactsObjects = DAList5;

        FilterForSearch filterCritera5 = new FilterForSearch();
        Map<String, String[]> designPropertiesCriteria5 = new HashMap<String, String[]>();
        String designPropValues5[] =  {"SearchDA3"};
        designPropertiesCriteria5.put("object_name", designPropValues5);

        filterCritera5.attributeValues = designPropertiesCriteria5;

        ImanType Im5 = new ImanType(searchDA4Rev.getTypeObject(), searchDA5Rev.getTypeObject().getUid());
        filterCritera5.typeObj = Im5;

        SearchInput2[] inputs5 = new SearchInput2[1];
        inputs5[0] = new SearchInput2();
        inputs5[0].clientId = "Search";
        inputs5[0].mdoCriteria = mdoPropertiesCriteria5;
        inputs5[0].filterCriteria = filterCritera5;
        inputs5[0].designArtifactInputs = DAInputsForSrch5;

        searchResults = m_mdoTestUtils.executeMDOSearch2(inputs5);
        if ( searchResults.size() > 0 )
        {
            LogFile.write("Completed Search MDO2 Test case 14.6 - Search based on MDThread Criteria, design artifacts and filter - DA imantype and design criteria");
            for(int inx = 0; inx < searchResults.size(); inx++ )
            {
                objNameProp = MDOTestUtils.getObjectProperties(searchResults.get(inx), new String[] {"object_name"});
                LogFile.write( "MDThread named: " + objNameProp[0].getDisplayableValue() + " uid: " + searchResults.get(inx).getUid() + "  is fetched." );
            }
        }
        else
        {
            LogFile.write("Failed Search MDO2 Test case 14.6 - Search based on MDThread Criteria, design artifacts and filter - DA imantype and design criteria");
        }

        mdoObj.pauseBetweenUseCases( pauseStr );
        LogFile.write( "------------------------------------------------------------------" );
        LogFile.write( "Executing Search MDO2 Test case 14.7 - Search based on MDThread Criteria and filter - Domain Automation" );
        // Test case ?: Search based on MDThread Criteria and filter - Domain Automation
        Map<String, String[]> mdoPropertiesCriteria6 = new HashMap<String, String[]>();
        String propValues6[] =  {"Srch2_*"};
        mdoPropertiesCriteria6.put("object_name", propValues6);
        FilterForSearch filterCritera6 = new FilterForSearch();

        filterCritera6.domain = "Automation";
        SearchInput2[] inputs6 = new SearchInput2[1];
        inputs6[0] = new SearchInput2();
        inputs6[0].clientId = "Search";
        inputs6[0].mdoCriteria = mdoPropertiesCriteria6;
        inputs6[0].filterCriteria = filterCritera6;
        searchResults = m_mdoTestUtils.executeMDOSearch2(inputs6);
        if ( searchResults.size() == 3 )
        {
            LogFile.write("Completed Search MDO2 Test case 14.7 - Search based on MDThread Criteria and filter - Domain Automation");
            for(int inx = 0; inx < searchResults.size(); inx++ )
            {
                objNameProp = MDOTestUtils.getObjectProperties(searchResults.get(inx), new String[] {"object_name"});
                LogFile.write( "MDThread named: " + objNameProp[0].getDisplayableValue() + " uid: " + searchResults.get(inx).getUid() + "  is fetched." );
            }
        }
        else
        {
            for(int inx = 0; inx < searchResults.size(); inx++ )
            {
                objNameProp = MDOTestUtils.getObjectProperties(searchResults.get(inx), new String[] {"object_name"});
                LogFile.write( "MDThread named: " + objNameProp[0].getDisplayableValue() + " uid: " + searchResults.get(inx).getUid() + "  is fetched." );
            }
            LogFile.write("Failed Search MDO2 Test case 14.7 - Search based on MDThread Criteria and filter - Domain Automation");
        }
        LogFile.write( "------------------------------------------------------------------" );
        mdoObj.pauseBetweenUseCases( pauseStr );

        LogFile.write( "Executing Search MDO2 Test case 14.8 - Search based on MDThread Criteria and filter - Domain none" );
        // Test case 14.8: Search based on MDThread Criteria and filter - Domain none
        Map<String, String[]> mdoPropertiesCriteria7 = new HashMap<String, String[]>();
        String propValues7[] =  {"Srch2_*"};
        mdoPropertiesCriteria7.put("object_name", propValues7);
        FilterForSearch filterCritera7 = new FilterForSearch();

        filterCritera7.domain = "";
        SearchInput2[] inputs7 = new SearchInput2[1];
        inputs7[0] = new SearchInput2();
        inputs7[0].clientId = "Search";
        inputs7[0].mdoCriteria = mdoPropertiesCriteria7;
        inputs7[0].filterCriteria = filterCritera7;
        searchResults = m_mdoTestUtils.executeMDOSearch2(inputs7);
        if ( searchResults.size() == 3 )
        {
            LogFile.write("Completed Search MDO2 Test case 14.8 - Search based on MDThread Criteria and filter - Domain none");
            for(int inx = 0; inx < searchResults.size(); inx++ )
            {
                objNameProp = MDOTestUtils.getObjectProperties(searchResults.get(inx), new String[] {"object_name"});
                LogFile.write( "MDThread named: " + objNameProp[0].getDisplayableValue() + " uid: " + searchResults.get(inx).getUid() + "  is fetched." );
            }
        }
        else
        {
            for(int inx = 0; inx < searchResults.size(); inx++ )
            {
                objNameProp = MDOTestUtils.getObjectProperties(searchResults.get(inx), new String[] {"object_name"});
                LogFile.write( "MDThread named: " + objNameProp[0].getDisplayableValue() + " uid: " + searchResults.get(inx).getUid() + "  is fetched." );
            }
            LogFile.write("Failed Search MDO2 Test case 14.8 - Search based on MDThread Criteria and filter - Domain none");
        }
        LogFile.write( "------------------------------------------------------------------" );
        mdoObj.pauseBetweenUseCases( pauseStr );

        LogFile.write( "Executing Search MDO3 Test case 14.9 - Search based on design artifacts only, get validation and relevant info" );
        DomainRelevancyInput[] inputs21 = new DomainRelevancyInput[1];
        inputs21[0] = new DomainRelevancyInput();
        inputs21[0].clientId = "Search3";
        inputs21[0].designArtifact = searchDA2Rev;

        String[] addRelDom1 = new String[1];
        addRelDom1[0] = "Automation";
        String[] addIrrDom1 = new String[1];
        addIrrDom1[0] = "Mechanical";
        RelevancyInformation addInfo1 = new RelevancyInformation();
        addInfo1.relevantDomain = addRelDom1;
        addInfo1.irrelevantDomain = addIrrDom1;
        inputs21[0].addInformation = addInfo1;

        UpdateDomainRelevancyResponse resp1 = m_mdoTestUtils.updateDomainRelevancy( inputs21 );
        if ( resp1.serviceData.sizeOfPartialErrors() > 0 )
        {
            LogFile.write("Creation of Domain relevancy information has failed");
            ++failCnt;
        }

        // Search based on design artifacts only
        DesignArtifactInputsForSearch  DAInputsForSrch21 = new DesignArtifactInputsForSearch();
        WorkspaceObject[] DAList3 = new WorkspaceObject[2];
        DAList3[0] = searchDA2Rev;
        DAList3[1] = searchDA3Rev;
        DAInputsForSrch21.designArtifactsObjects = DAList3;
        SearchInput3[] inputs11 = new SearchInput3[1];
        inputs11[0] = new SearchInput3();
        inputs11[0].clientId = "Search";
        inputs11[0].designArtifactInputs = DAInputsForSrch21;

        SearchMDO3Response resp = m_mdoTestUtils.executeMDOSearch3(inputs11);
        LogFile.write( "Found " + resp.mdoSearchOutput[0].mdoOutput.length + " MDOs." );
        if ( resp.mdoSearchOutput.length > 0 )
        {
            for ( int inx=0; inx < resp.mdoSearchOutput[0].mdoOutput.length; inx++ )
            {
                objNameProp = MDOTestUtils.getObjectProperties(resp.mdoSearchOutput[0].mdoOutput[inx].mdoObject, new String[] {"object_name"});
                LogFile.write( "MDThread named: " + objNameProp[0].getDisplayableValue() );
                for ( int jnx=0; jnx < resp.mdoSearchOutput[0].mdoOutput[inx].associatedDesignArtifact.length; ++jnx )
                {
                    objNameProp = MDOTestUtils.getObjectProperties(resp.mdoSearchOutput[0].mdoOutput[inx].associatedDesignArtifact[jnx].designArtifact, new String[] {"object_name"});
                    LogFile.write( "DA named: " + objNameProp[0].getDisplayableValue() + " is fetched." );
                    if ( resp.mdoSearchOutput[0].mdoOutput[inx].associatedDesignArtifact[jnx].designArtifact == searchDA2Rev )
                    {
                        LogFile.write( "Found original DA " + resp.mdoSearchOutput[0].mdoOutput[inx].associatedDesignArtifact[jnx].designArtifact );
                    }
                    LogFile.write( "    DA needsValidation " + resp.mdoSearchOutput[0].mdoOutput[inx].associatedDesignArtifact[jnx].needsValidation );
                    LogFile.write( "    DA isValidated " + resp.mdoSearchOutput[0].mdoOutput[inx].associatedDesignArtifact[jnx].isValidated );
                    for ( int knx=0; knx < resp.mdoSearchOutput[0].mdoOutput[inx].associatedDesignArtifact[jnx].relevantDomains.length; ++knx )
                    {
                        LogFile.write( "Found Relevant Domain " + resp.mdoSearchOutput[0].mdoOutput[inx].associatedDesignArtifact[jnx].relevantDomains[knx] );
                    }
                    for ( int knx=0; knx < resp.mdoSearchOutput[0].mdoOutput[inx].associatedDesignArtifact[jnx].irrelevantDomains.length; ++knx )
                    {
                        LogFile.write( "Found Irrelevant Domain " + resp.mdoSearchOutput[0].mdoOutput[inx].associatedDesignArtifact[jnx].irrelevantDomains[knx] );
                    }
                }
            }
            LogFile.write("Completed Search MDO3 Test case 14.9 - Search based on design artifacts only, get validation and relevant info");
        }
        else
        {
            LogFile.write("Failed Search MDO3 Test case 14.9 - Search based on design artifacts only, get validation and relevant info");
        }
        LogFile.write( "------------------------------------------------------------------" );


        mdoObj.pauseBetweenUseCases( pauseStr );

        LogFile.write( "Executing Search MDO3 Test case 14.10 - Search based on design artifacts only, get only relevant information" );
        // Search based on design artifacts only, get only relevant information
        DesignArtifactInputsForSearch  DAInputsForSrch22 = new DesignArtifactInputsForSearch();
        WorkspaceObject[] DAList4 = new WorkspaceObject[2];
        DAList4[0] = searchDA2Rev;
        DAList4[1] = searchDA3Rev;
        DAInputsForSrch22.designArtifactsObjects = DAList4;

        FilterForDomainRelevancy filterForDomainRel1 = new FilterForDomainRelevancy();
        filterForDomainRel1.isFilterForRelevantDomain = true; // find relevant only
        filterForDomainRel1.domain = "Automation";

        SearchInput3[] inputs12 = new SearchInput3[1];
        inputs12[0] = new SearchInput3();
        inputs12[0].clientId = "Search";
        inputs12[0].designArtifactInputs = DAInputsForSrch22;
        inputs12[0].filterForDomainRelevancy = filterForDomainRel1;

        SearchMDO3Response resp2 = m_mdoTestUtils.executeMDOSearch3(inputs12);
        LogFile.write( "Found " + resp2.mdoSearchOutput[0].mdoOutput.length + " MDOs." );
        if ( resp2.mdoSearchOutput.length > 0 )
        {
            for ( int inx=0; inx < resp2.mdoSearchOutput[0].mdoOutput.length; inx++ )
            {
                objNameProp = MDOTestUtils.getObjectProperties(resp2.mdoSearchOutput[0].mdoOutput[inx].mdoObject, new String[] {"object_name"});
                LogFile.write( "MDThread named: " + objNameProp[0].getDisplayableValue() );
                for ( int jnx=0; jnx < resp2.mdoSearchOutput[0].mdoOutput[inx].associatedDesignArtifact.length; ++jnx )
                {
                    objNameProp = MDOTestUtils.getObjectProperties(resp2.mdoSearchOutput[0].mdoOutput[inx].associatedDesignArtifact[jnx].designArtifact, new String[] {"object_name"});
                    LogFile.write( "DA named: " + objNameProp[0].getDisplayableValue() + " is fetched." );
                    if ( resp2.mdoSearchOutput[0].mdoOutput[inx].associatedDesignArtifact[jnx].designArtifact == searchDA2Rev )
                    {
                        LogFile.write( "Found original DA " + resp2.mdoSearchOutput[0].mdoOutput[inx].associatedDesignArtifact[jnx].designArtifact );
                    }
                }
            }
            LogFile.write("Completed Search MDO3 Test case 14.10 - Search based on design artifacts only, get only relevant information");
        }
        else
        {
            LogFile.write("Failed Search MDO3 Test case 14.10 - Search based on design artifacts only, get only relevant information");
        }
        LogFile.write( "------------------------------------------------------------------" );


        mdoObj.pauseBetweenUseCases( pauseStr );
        LogFile.write( "Executing Search MDO3 Test case 14.11 - Search based on design artifacts only, get only irrelevant information" );
        // Search based on design artifacts only, get only relevant information
        DesignArtifactInputsForSearch  DAInputsForSrch23 = new DesignArtifactInputsForSearch();
        WorkspaceObject[] DAList6 = new WorkspaceObject[2];
        DAList6[0] = searchDA2Rev;
        DAList6[1] = searchDA3Rev;
        DAInputsForSrch23.designArtifactsObjects = DAList6;

        FilterForDomainRelevancy filterForDomainRel2 = new FilterForDomainRelevancy();
        filterForDomainRel2.isFilterForRelevantDomain = false; // find irrelevant only
        filterForDomainRel2.domain = "Mechanical";

        SearchInput3[] inputs13 = new SearchInput3[1];
        inputs13[0] = new SearchInput3();
        inputs13[0].clientId = "Search";
        inputs13[0].designArtifactInputs = DAInputsForSrch23;
        inputs13[0].filterForDomainRelevancy = filterForDomainRel2;

        SearchMDO3Response resp3 = m_mdoTestUtils.executeMDOSearch3(inputs13);
        LogFile.write( "Found " + resp3.mdoSearchOutput[0].mdoOutput.length + " MDOs." );
        if ( resp3.mdoSearchOutput.length > 0 )
        {
            for ( int inx=0; inx < resp3.mdoSearchOutput[0].mdoOutput.length; inx++ )
            {
                objNameProp = MDOTestUtils.getObjectProperties(resp3.mdoSearchOutput[0].mdoOutput[inx].mdoObject, new String[] {"object_name"});
                LogFile.write( "MDThread named: " + objNameProp[0].getDisplayableValue() );
                for ( int jnx=0; jnx < resp3.mdoSearchOutput[0].mdoOutput[inx].associatedDesignArtifact.length; ++jnx )
                {
                    objNameProp = MDOTestUtils.getObjectProperties(resp3.mdoSearchOutput[0].mdoOutput[inx].associatedDesignArtifact[jnx].designArtifact, new String[] {"object_name"});
                    LogFile.write( "DA named: " + objNameProp[0].getDisplayableValue() + " is fetched." );
                    if ( resp3.mdoSearchOutput[0].mdoOutput[inx].associatedDesignArtifact[jnx].designArtifact == searchDA2Rev )
                    {
                        LogFile.write( "Found original DA " + resp3.mdoSearchOutput[0].mdoOutput[inx].associatedDesignArtifact[jnx].designArtifact );
                    }
                }
            }
            LogFile.write("Completed Search MDO3 Test case 14.11 - Search based on design artifacts only, get only irrelevant information");
        }
        else
        {
            LogFile.write("Failed Search MDO3 Test case 14.11 - Search based on design artifacts only, get only irrelevant information");
        }
        LogFile.write( "------------------------------------------------------------------" );

        mdoObj.pauseBetweenUseCases( pauseStr );
        LogFile.write( "Executing Search MDO3 Test case 14.12 - Search based on design artifacts only, get only irrelevant information(none for this domain)" );
        // Search based on design artifacts only, get only relevant information(none for this domain)
        DesignArtifactInputsForSearch  DAInputsForSrch24 = new DesignArtifactInputsForSearch();
        WorkspaceObject[] DAList7 = new WorkspaceObject[2];
        DAList7[0] = searchDA2Rev;
        DAList7[1] = searchDA3Rev;
        DAInputsForSrch24.designArtifactsObjects = DAList7;

        FilterForDomainRelevancy filterForDomainRel3 = new FilterForDomainRelevancy();
        filterForDomainRel3.isFilterForRelevantDomain = false; // find irrelevant only
        filterForDomainRel3.domain = "Automation";

        SearchInput3[] inputs14 = new SearchInput3[1];
        inputs14[0] = new SearchInput3();
        inputs14[0].clientId = "Search";
        inputs14[0].designArtifactInputs = DAInputsForSrch24;
        inputs14[0].filterForDomainRelevancy = filterForDomainRel3;

        SearchMDO3Response resp4 = m_mdoTestUtils.executeMDOSearch3(inputs14);
        LogFile.write( "Found " + resp4.mdoSearchOutput[0].mdoOutput.length + " MDOs." );
        if ( resp4.mdoSearchOutput.length > 0 )
        {
            for ( int inx=0; inx < resp4.mdoSearchOutput[0].mdoOutput.length; inx++ )
            {
                objNameProp = MDOTestUtils.getObjectProperties(resp4.mdoSearchOutput[0].mdoOutput[inx].mdoObject, new String[] {"object_name"});
                LogFile.write( "MDThread named: " + objNameProp[0].getDisplayableValue() );
                for ( int jnx=0; jnx < resp4.mdoSearchOutput[0].mdoOutput[inx].associatedDesignArtifact.length; ++jnx )
                {
                    objNameProp = MDOTestUtils.getObjectProperties(resp4.mdoSearchOutput[0].mdoOutput[inx].associatedDesignArtifact[jnx].designArtifact, new String[] {"object_name"});
                    LogFile.write( "DA named: " + objNameProp[0].getDisplayableValue() + " is fetched." );
                    if ( resp4.mdoSearchOutput[0].mdoOutput[inx].associatedDesignArtifact[jnx].designArtifact == searchDA2Rev )
                    {
                        LogFile.write( "Found original DA " + resp4.mdoSearchOutput[0].mdoOutput[inx].associatedDesignArtifact[jnx].designArtifact );
                    }
                }
            }
            LogFile.write("Completed Search MDO3 Test case 14.12 - Search based on design artifacts only, get only irrelevant information(none for this domain)");
        }
        else
        {
            LogFile.write("Failed Search MDO3 Test case 14.12 - Search based on design artifacts only, get only irrelevant information(none for this domain)");
        }
        LogFile.write( "------------------------------------------------------------------" );

        mdoObj.pauseBetweenUseCases( pauseStr );

        LogFile.write("Deleting data used in search Mdo2 test case");
        ModelObject[] mdoList = new ModelObject[3];
        mdoList[0] = searchMdo1;
        mdoList[1] = searchMdo2;
        mdoList[2] = searchMdo3;
        m_mdoTestUtils.cleanupData(mdoList);
        ModelObject[] itemList = new ModelObject[7];
        itemList[0] = searchDA1;
        itemList[1] = searchDA2;
        itemList[2] = searchDA3;
        itemList[3] = searchDA4;
        itemList[4] = searchDA5;
        itemList[5] = searchDA6;
        itemList[6] = searchDA7;
        m_mdoTestUtils.cleanupData(itemList);

        LogFile.write( "Completed Usecase 14 - Search MDO2" );
    }

    private static void executeQueryNotifications()
    {
        try
        {
            MDOTestUtils m_mdoTestUtils = new MDOTestUtils();
            MDOManagement mdoObj = new MDOManagement();

            LogFile.write( "Executing Usecase 13 - Find Notification objects" );

            // Test case 1.1 - Create 4 Items - DA1(Mechanical), DA2(Electrical), DA3(Electrical), DA4(Automation)
            // and 4 collaboration designs (CD) - CDMech, CDElect1 , CDElect2 and CDAuto
            // Create reuse DA1DE0 in the CDMech.
            // Query for all notification information using the SOA QueryMDONotification with performCreateNotificationQuery as true
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 1.1 - Create 4 Items, 4 CDs, reuse DA1DE0 in CDMech, query for notification" );

            // Create DA1/A - Mechanical (Part)
            CreateItemsResponse response;
            response = m_mdoTestUtils.createDesignArtifactsWithType("DA1", "Part", "Description for DA1");
            ItemRevision DA1Rev = response.output[0].itemRev;

            // Create DA2/A - Electrical (PSSignal)
            response = m_mdoTestUtils.createDesignArtifactsWithType("DA2", "PSSignal", "Description for DA2");
            ItemRevision DA2Rev = response.output[0].itemRev;

            // Create DA3/A - Electrical (PSSignal)
            response = m_mdoTestUtils.createDesignArtifactsWithType("DA3", "PSSignal", "Description for DA3" );
            ItemRevision DA3Rev = response.output[0].itemRev;

            // Create DA4/A - Automation (PSConnection)
            response = m_mdoTestUtils.createDesignArtifactsWithType("DA4", "PSConnection", "Description for DA4");
            ItemRevision DA4Rev = response.output[0].itemRev;
            //Creating Project
            POM_object context2 = m_mdoTestUtils.createProject( "proj2", "Project 2" );
            m_mdoTestUtils.setProjectAsMultiDisciplinary(context2);
            // Create CDMech
            ModelObject CDMech = m_mdoTestUtils.createCD("CDMech");
            // Create CDElect1
            ModelObject CDElect1 = m_mdoTestUtils.createCD("CDElect1");
            // Create CDElect2
            ModelObject CDElect2 = m_mdoTestUtils.createCD("CDElect2");
            // Create CDAuto
            ModelObject CDAuto = m_mdoTestUtils.createCD("CDAuto");

            //Add CDMech, CDElect1 , CDElect2 and CDAuto to proj2
            ModelObject[] collaborativeDesigns = new ModelObject[4];
            collaborativeDesigns[0] = CDMech;
            collaborativeDesigns[1] = CDElect1;
            collaborativeDesigns[2] = CDElect2;
            collaborativeDesigns[3] = CDAuto;
            m_mdoTestUtils.assignCDsToProject( collaborativeDesigns, context2 );

            // Create reuse DE DA1DE0
            ModelObject DA1DE0 = m_mdoTestUtils.createReuseDEWithCD( DA1Rev, "DA1DE0", CDMech );
            m_mdoTestUtils.sleepForNotificationCreation();

            // list all notifications
            NotificationQueryInput[] mdoNotifInputs = new NotificationQueryInput[1];
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].performCreateNotificationQueryAlso = true;
            QueryNotificationResponse notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            LogFile.write( " check for all Notifications" );
            m_mdoTestUtils.printNotifQuery(notifResp);

            LogFile.write( "Completed Test case 1.1" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 1.2 - Associate the 4 Item Revisions - DA1/A, DA2/A, DA3/A and DA4/A through an MDThread - MDO1
            // Create reuse DE- DA1DE1 in CD- CDMech
            // Query for all notification information using the SOA QueryMDONotification with performCreateNotificationQuery as true
            // Create in an ECN
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 1.2 - Create MDThread with 4 Items, reuse DA1DE1 in CDMech, query for notification" );

            //Create ECN
            ModelObject ecnRev1 = m_mdoTestUtils.createECN( "ECN_DomainRel", "ECM desc" );
            m_mdoTestUtils.submitECNToChangeWorkflow(ecnRev1);

            // Create MDO1 with DA1/A, DA2/A, DA3/A, DA4/A
            List<ItemRevision> revsOfDAsForMdo1 = new ArrayList<ItemRevision>();
            revsOfDAsForMdo1.add(DA1Rev);
            revsOfDAsForMdo1.add(DA2Rev);
            revsOfDAsForMdo1.add(DA3Rev);
            revsOfDAsForMdo1.add(DA4Rev);
            m_mdoTestUtils.createMDOWithDARevList(revsOfDAsForMdo1,"MDO1", "MDO1 for search notifications test");

            // Create reuse DE- DA1DE1 in CD- CDMech
            ModelObject DA1DE1 = m_mdoTestUtils.createReuseDEWithCDInECN( DA1Rev, "DA1DE1", CDMech, ecnRev1 );
            m_mdoTestUtils.sleepForNotificationCreation();

            // check that 1 notification was created
            QryInputByDesignOrProject[] mdoQryInputs = new QryInputByDesignOrProject[1];
            mdoQryInputs[0] = new QryInputByDesignOrProject();
            mdoQryInputs[0].clientId = "qryFindNotification";
            mdoQryInputs[0].inputDesign = (POM_object) CDMech;
            QryNotificationByDesignResponse qryResp = m_mdoTestUtils.qryNotificationsByDesignOrProject(mdoQryInputs);
            if ( qryResp.output.length > 0 )
            {
                if ( qryResp.output[0].notificationOutputs.length == 1 )
                {
                    QryNotificationOutputByDesign notificationOutput = qryResp.output[0].notificationOutputs[0];
                    if ( notificationOutput.notificationByDesignOutputs.length == 1 )
                    {
                        if ( notificationOutput.notificationByDesignOutputs[0].designInstance == DA1DE1 )
                        {
                            LogFile.write( " check for Notification succeeded - designInstance is DA1DE1" );
                        }
                        else
                        {
                            LogFile.write( " check for Notification failed - designInstance IS NOT DA1DE1" );
                        }
                    }
                    else
                    {
                        LogFile.write( " check for Notification failed - notificationByDesignOutputs is not 1" );
                    }
                }
                else
                {
                    LogFile.write( " check for Notification failed - notificationOutputs is not 1" );
                }
            }
            else
            {
                LogFile.write( " check for Notification failed - no output" );
            }
            m_mdoTestUtils.printQueryResp(qryResp);

            LogFile.write( "Completed Test case 1.2" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            //Extract DA1DE1 create notification
            POM_object notifObj = null;
            for(int inx= 0; inx < notifResp.output[0].createNotificationOutputs.length; ++inx )
            {
                CreateNotificationOutput createNotificationOutput = notifResp.output[0].createNotificationOutputs[inx];
                // Get Trigger Notification Object
                MDOInitialNotification createTriggerNotification = createNotificationOutput.createTriggerNotification;
                if(createTriggerNotification.designInstance == DA1DE1)
                {
                    notifObj = createTriggerNotification.notificationObject;
                }
            }

            // Test Case 1.3 - Create reuse DEs - DA2DE1 in CDElect1 and DA4DE1 in CDAuto
            // Respond to create notification of DA1DE1 using the SOA UpdateMDONotification
            // for the newly created DEs - DA2DE1 and DA4DE1. Provide input action as create, the create notification object as DA1DE1 and
            // ignoreNotification as false as input to the SOA.
            // Create MDI1 by linking DA1DE1, DA2DE1 and DA4DE1 with context as proj1 for MDI
            // Link again all the DEs DA1DE1, DA2DE1 and DA4DE1 with context proj2.
            // Query for all notification information using the SOA QueryMDONotification with input performCreateNotificationQuery as true
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 1.3 - Reuse DA2DE1 and DA4DE1, respond to notification, link 3 DAs, query for notification" );

            // Create reuse DE DA2DE1
            ModelObject DA2DE1 = m_mdoTestUtils.createReuseDEWithCD( DA2Rev, "DA2DE1", CDElect1 );

            // Create reuse DE DA4DE1
            ModelObject DA4DE1 = m_mdoTestUtils.createReuseDEWithCD( DA4Rev, "DA4DE1", CDAuto );

            m_mdoTestUtils.sleepForNotificationCreation();
            // Respond to create notification of DA1DE1 using the SOA UpdateMDONotification
            MDONotificationReactionData[] reactData = new MDONotificationReactionData[2];
            reactData[0] = new MDONotificationReactionData();
            reactData[0].notificationQualifier = "create";
            reactData[0].reactedObject =  (POM_object) DA2DE1;

            reactData[1] = new MDONotificationReactionData();
            reactData[1].notificationQualifier = "create";
            reactData[1].reactedObject =  (POM_object) DA4DE1;

            MDONotificationUpdateData[] updateData = new MDONotificationUpdateData[1];
            updateData[0] = new MDONotificationUpdateData();
            updateData[0].notificationQualifier = "create";
            updateData[0].ignoreNotification = false;
            updateData[0].notificationObject = (POM_object) notifObj ; // notification object from query
            updateData[0].reactedResponses = new  MDONotificationReactionData[2];
            for (int inx = 0; inx < 2; inx++ )
            {
                updateData[0].reactedResponses[inx] = reactData[inx];
            }
            MDONotificationUpdateInput[] mdoNotifUpdInput = new MDONotificationUpdateInput[1];
            mdoNotifUpdInput[0] = new MDONotificationUpdateInput();
            mdoNotifUpdInput[0].clientId = "updateMDONotifications";
            mdoNotifUpdInput[0].notificationUpdateData = updateData[0];

            m_mdoTestUtils.updateMDONotification(mdoNotifUpdInput);

            // Create MDI1 linking DA1DE1, DA2DE1, DA4DE1 with context proj2
            List<ModelObject> dElemList1 = new ArrayList <ModelObject>();
            dElemList1.add(DA1DE1);
            dElemList1.add(DA2DE1);
            dElemList1.add(DA4DE1);

            //Creating Project
            m_mdoTestUtils.linkDesignInstances( dElemList1, context2, true);

            notifResp = m_mdoTestUtils.queryForMDONotifByImpactedDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);

            LogFile.write( "Completed Test case 1.3" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 1.4 - Create 4 Items - DA7(Automation), DA8(Automation), DA9(Electrical), DA10(Mechanical)
            // Associate the 4 Item Revisions - DA7/A, DA8/A, DA9/A and DA10/A through an MDThread - MDO3
            // Create shape DE DA9DE1, imprecise reuse DE DA11DE1 in the CDElect1.
            // Query for all notification information using the SOA QueryMDONotification with performCreateNotificationQuery as true
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 1.4 - Create MDThread with 4 Item Revisions, create shape, query for notification" );

            // Create DA7/A - Automation (Part)
            response = m_mdoTestUtils.createDesignArtifactsWithType("DA7", "Part", "Description for DA7");
            ItemRevision DA7Rev = response.output[0].itemRev;

            // Create DA8/A - Automation (PSSignal)
            response = m_mdoTestUtils.createDesignArtifactsWithType("DA8", "PSSignal", "Description for DA8");
            ItemRevision DA8Rev = response.output[0].itemRev;

            // Create DA9/A - Electrical (PSSignal)
            response = m_mdoTestUtils.createDesignArtifactsWithType("DA9", "PSSignal", "Description for DA9" );
            ItemRevision DA9Rev = response.output[0].itemRev;

            // Create DA10/A - Mechanical (PSConnection)
            response = m_mdoTestUtils.createDesignArtifactsWithType("DA10", "PSConnection", "Description for DA10");
            ItemRevision DA10Rev = response.output[0].itemRev;

            // Create MDO3 with DA1/A, DA2/A, DA3/A, DA4/A
            List<ItemRevision> revsOfDAsForMdo3 = new ArrayList<ItemRevision>();
            revsOfDAsForMdo3.add(DA7Rev);
            revsOfDAsForMdo3.add(DA8Rev);
            revsOfDAsForMdo3.add(DA9Rev);
            revsOfDAsForMdo3.add(DA10Rev);
            m_mdoTestUtils.createMDOWithDARevList(revsOfDAsForMdo3,"MDO3", "MDO3 for search notifications test");

            // Create shape DE-DA9DE1 in CD-CDElect1
            m_mdoTestUtils.createShapeDE( "DA9DE1", CDElect1 );
            m_mdoTestUtils.sleepForNotificationCreation();

            // list all notifications
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].performCreateNotificationQueryAlso = true;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);

            LogFile.write( "Completed Test case 1.4" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test Case 1.5 - Create promissory DE DA10DE1 in the CDMech.
            // Query for all notification information using the SOA QueryMDONotification with performCreateNotificationQuery as true
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 1.5 - Create promissory DE-DA10DE1 in CD-CDMech, query for notification" );

            // Create promissory DE-DA10DE1 in CD-CDMech
            m_mdoTestUtils.createPromissoryDE( "DA10DE1", CDAuto );
            m_mdoTestUtils.sleepForNotificationCreation();

            // list all notifications
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].performCreateNotificationQueryAlso = true;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);

            LogFile.write( "Completed Test case 1.5" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test Case 1.6 - Create BOM Structure with DA7 as top line and DA8 as child line.
            // Create reuse DE DA7DE1 in CD-CDAuto
            // Query for all notification information using the SOA QueryMDONotification with performCreateNotificationQuery as true
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 1.6 - Create BOM, reuse DA7DE1, query for notification" );

            // create BOM structure
            // DA7Rev
            //    |- DA8Rev
            List< ItemRevision > childItemRevs = new ArrayList< ItemRevision >();
            childItemRevs.add( DA8Rev );
            m_mdoTestUtils.createStructure(DA7Rev, childItemRevs, "");

            // Create reuse DE DA7DE1
            ModelObject DA7DE1 = m_mdoTestUtils.createReuseDEWithCD( DA7Rev, "DA7DE1", CDAuto );
            m_mdoTestUtils.sleepForNotificationCreation();

            // list all notifications
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].performCreateNotificationQueryAlso = true;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);

            LogFile.write( "Completed Test case 1.6" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test Case 1.7 - Create CD-CDAuto1 and copy DA7DE1 under it as instantiation
            // Query for all notification information using the SOA QueryMDONotification with performCreateNotificationQuery as true
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 1.7 - Create CD-CDAuto1 and copy DA7DE1 under it as instantiation, query for notification" );

            ModelObject CDAuto1 = m_mdoTestUtils.createCD("CDAuto1");
            collaborativeDesigns = new ModelObject[1];
            collaborativeDesigns[0] = CDAuto1;
            m_mdoTestUtils.assignCDsToProject( collaborativeDesigns, context2 );

            Mdl0ModelElement des[] = new Mdl0ModelElement[1];
            des[0] = (Mdl0ModelElement) DA7DE1;
            m_mdoTestUtils.createModeltoModelInstantiation( des, CDAuto, CDAuto1);
            m_mdoTestUtils.sleepForNotificationCreation();

            // list all notifications
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].performCreateNotificationQueryAlso = true;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);
            LogFile.write( "Completed Test case 1.7" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test Case 1.8 - Copy DA7DE1 under CD-CDAuto1 as clone
            // Query for all notification information using the SOA QueryMDONotification with performCreateNotificationQuery as true
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 1.8 -  Copy DA7DE1 under CD-CDAuto1 as clone, query for notification" );
            m_mdoTestUtils.cloneModelContent( des, CDAuto, CDAuto1);
            m_mdoTestUtils.sleepForNotificationCreation();

            // list all notifications
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].performCreateNotificationQueryAlso = true;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);

            LogFile.write( "Completed Test case 1.8" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test Case 2.1 - Create Partition P1 under CD-CDMech
            // Query for all notification information using the SOA QueryMDONotification with performCreateNotificationQuery as true
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 2.1 - Create Partition P1 under CD-CDMech, query for notification" );

            ModelObject partScheme = MDOTestUtils.createPartitionScheme( CDMech, "Ptn0SchemeFunctional","TestScheme1", "TestScheme1 Description" );
            ModelObject P1 = MDOTestUtils.createAdhocPartition( CDMech, partScheme, "Ptn0Functional","P1", "Partn_desc","Ptn_ID_P1" );
            m_mdoTestUtils.sleepForNotificationCreation();

            // list all notifications
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "queryPartnNotifs";
            mdoNotifInputs[0].performCreateNotificationQueryAlso = true;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);

            //Extract P1 create notification
            POM_object notifObject = null;
            for(int inx= 0; inx < notifResp.output[0].createNotificationOutputs.length; ++inx )
            {
                CreateNotificationOutput createNotificationOutput = notifResp.output[0].createNotificationOutputs[inx];
                // Get Trigger Notification Object
                MDOInitialNotification createTriggerNotification = createNotificationOutput.createTriggerNotification;
                if(createTriggerNotification.designInstance == P1)
                {
                    notifObject = createTriggerNotification.notificationObject;
                }
            }

            LogFile.write( "Completed Test case 2.1" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test Case 2.2 - Create Partition P2 under CD-CDAuto
            // Respond to create notification of P1 using the SOA UpdateMDONotification
            // Provide input action as create, create notification object as P1 and ignoreNotification as false as input to the SOA.
            // Query for all notification information using the SOA QueryMDONotification with performCreateNotificationQuery as true
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 2.2 - Create Partition P2 under CD-CDAuto, respond, query for notification" );

            ModelObject P2 = MDOTestUtils.createAdhocPartition( CDAuto, partScheme, "Ptn0Functional","P2", "Partn_desc","Ptn_ID_P2" );
            m_mdoTestUtils.sleepForNotificationCreation();

            // Respond to create notification of DA1DE1 using the SOA UpdateMDONotification
            reactData = new MDONotificationReactionData[1];
            reactData[0] = new MDONotificationReactionData();
            reactData[0].notificationQualifier = "create";
            reactData[0].reactedObject =  (POM_object) P2;

            updateData = new MDONotificationUpdateData[1];
            updateData[0] = new MDONotificationUpdateData();
            updateData[0].notificationQualifier = "create";
            updateData[0].ignoreNotification = false;
            updateData[0].notificationObject = (POM_object) notifObject ; // partition notification object from query
            updateData[0].reactedResponses = new  MDONotificationReactionData[1];
            for (int inx = 0; inx < 1; inx++ )
            {
                updateData[0].reactedResponses[inx] = reactData[inx];
            }
            mdoNotifUpdInput = new MDONotificationUpdateInput[1];
            mdoNotifUpdInput[0] = new MDONotificationUpdateInput();
            mdoNotifUpdInput[0].clientId = "updateMDONotifications";
            mdoNotifUpdInput[0].notificationUpdateData = updateData[0];

            m_mdoTestUtils.updateMDONotification(mdoNotifUpdInput);

            // list all notifications
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "queryPartnNotifs";
            mdoNotifInputs[0].performCreateNotificationQueryAlso = true;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);

            LogFile.write( "Completed Test case 2.2" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Replace related test cases
            // Test case 3.1 Create reuse DEs - DA3DE1 in CDElect2, Replace the underlying IR of DA3DE1
            // Query for all notification information using the SOA QueryMDONotification with input design instance as DA3DE1
            // Create reuse DE DA3DE1
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 3.1 - Replace the underlying IR of DA3DE1, query for notification" );

            ModelObject DA3DE1 = m_mdoTestUtils.createReuseDEWithCD( DA3Rev, "DA3DE1", CDElect2 );


            response = m_mdoTestUtils.createDesignArtifactsWithType("DATest", "PSSignal", "Description for DATest" );
            ItemRevision DATestRev = response.output[0].itemRev;

            m_mdoTestUtils.updateReuseDE(DATestRev,DA3DE1);
            m_mdoTestUtils.sleepForNotificationCreation();
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].designInstance = (POM_object) DA3DE1;
            mdoNotifInputs[0].performCreateNotificationQueryAlso = false;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);

            LogFile.write( "Completed Test case 3.1" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 3.2 Replace the underlying IR of DA2DE1
            // Query for all notification information using the SOA QueryMDONotification with input design instance as DA2DE1
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 3.2 - Replace the underlying IR of DA2DE1, query for notification" );

            response = m_mdoTestUtils.createDesignArtifactsWithType("DATest1", "PSSignal", "Description for DATest1");
            ItemRevision DATestRev1 = response.output[0].itemRev;
            m_mdoTestUtils.updateReuseDE(DATestRev1,DA2DE1);
            m_mdoTestUtils.sleepForNotificationCreation();
            // Query for all notification information using the SOA QueryMDONotification with input design instance as DA2DE1
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].designInstance = (POM_object) DA2DE1;
            mdoNotifInputs[0].performCreateNotificationQueryAlso = false;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);

            LogFile.write( "Completed Test case 3.2" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 3.3 Replace the underlying IR of DA2DE1 again
            // Query for all notification information using the SOA QueryMDONotification with input design instance as DA2DE1
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 3.3 - Replace the underlying IR of DA2DE1 again, query for notification" );

            response = m_mdoTestUtils.createDesignArtifactsWithType("DATest2", "PSSignal", "Description for DATest2");
            ItemRevision DATestRev2 = response.output[0].itemRev;
            m_mdoTestUtils.updateReuseDE(DATestRev2,DA2DE1);
            m_mdoTestUtils.sleepForNotificationCreation();
            // Query for all notification information using the SOA QueryMDONotification with input design instance as DA2DE1
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].designInstance = (POM_object) DA2DE1;
            mdoNotifInputs[0].performCreateNotificationQueryAlso = false;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);

            LogFile.write( "Completed Test case 3.3" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 3.4 Replace the underlying IR of DA1DE1
            // Query for all notification information using the SOA QueryMDONotification with input design instance as DA1DE1
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 3.4 - Replace the underlying IR of DA1DE1, query for notification" );
            response = m_mdoTestUtils.createDesignArtifactsWithType("DATest3", "Part", "Description for DATest3");
            ItemRevision DATestRev3 = response.output[0].itemRev;
            m_mdoTestUtils.updateReuseDE(DATestRev3,DA1DE1);
            m_mdoTestUtils.sleepForNotificationCreation();
            // Query for all notification information using the SOA QueryMDONotification with input design instance as DA2DE1
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].designInstance = (POM_object) DA1DE1;
            mdoNotifInputs[0].performCreateNotificationQueryAlso = false;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);

            LogFile.write( "Completed Test case 3.4" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 3.5 Replace DEs - DA2DE1 and DA4DE1
            // Respond to modify notification of DA1DE1 using the SOA UpdateMDONotification for the modified DEs - DA2DE1 and DA4DE1.
            // Provide input action as modify, the modify notification object as DA1DE1 and ignoreNotification as false as input to the SOA.
            // Query for all notification information using the SOA QueryMDONotification with input design instance as DA1DE1

            // Respond to replace notification of DA2DE1 using the SOA UpdateMDONotification for the replace DE - DA1DE1.
            // Provide input action as replace, the replace notification object as DA2DE1 and ignoreNotification as false as input to the SOA.
            // Query for all notification information using the SOA QueryMDONotification with input design instance as DA1DE1

            //Extract DA2DE1 replace notification
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 3.5 - Replace DEs - DA2DE1 and DA4DE1, respond, query for notification" );
            POM_object modifyNotifObj = null;
            for(int inx= 0; inx < notifResp.output[0].modifyNotificationOutputs.length; ++inx )
            {
                ModifyNotificationOutput modifyNotificationOutput = notifResp.output[0].modifyNotificationOutputs[inx];
                // Get Trigger Notification Object
                MDOInitialNotification modifyTriggerNotification = modifyNotificationOutput.modifyTriggerNotifications[0];
                if(modifyTriggerNotification.designInstance == DA1DE1)
                {
                    modifyNotifObj = modifyTriggerNotification.notificationObject;
                }
            }

            // Replace the underlying IR of DA2DE1 and DA4DE1
            response = m_mdoTestUtils.createDesignArtifactsWithType("DATest4", "PSSignal", "Description for DATest4");
            ItemRevision DATestRev4 = response.output[0].itemRev;
            m_mdoTestUtils.updateReuseDE(DATestRev4,DA2DE1);

            response = m_mdoTestUtils.createDesignArtifactsWithType("DATest5", "PSConnection","Description for DATest5" );
            ItemRevision DATestRev5 = response.output[0].itemRev;
            m_mdoTestUtils.updateReuseDE(DATestRev5,DA4DE1);
            m_mdoTestUtils.sleepForNotificationCreation();

            // Respond to replace notification of DA1DE1 using the SOA UpdateMDONotification
            MDONotificationReactionData[] replaceReactData = new MDONotificationReactionData[2];
            replaceReactData[0] = new MDONotificationReactionData();
            replaceReactData[0].notificationQualifier = "replace";
            replaceReactData[0].reactedObject =  (POM_object) DA2DE1;
            replaceReactData[1] = new MDONotificationReactionData();
            replaceReactData[1].notificationQualifier = "replace";
            replaceReactData[1].reactedObject =  (POM_object) DA4DE1;

            MDONotificationUpdateData[] modifyUpdateData = new MDONotificationUpdateData[1];
            modifyUpdateData[0] = new MDONotificationUpdateData();
            modifyUpdateData[0].notificationQualifier = "replace";
            modifyUpdateData[0].ignoreNotification = false;
            modifyUpdateData[0].notificationObject = (POM_object) modifyNotifObj; // notification object from query
            modifyUpdateData[0].reactedResponses = new  MDONotificationReactionData[2];
            for (int inx = 0; inx < 2; inx++ )
            {
                modifyUpdateData[0].reactedResponses[inx] = replaceReactData[inx];
            }
            MDONotificationUpdateInput[] mdoModifNotifUpdInput = new MDONotificationUpdateInput[1];
            mdoModifNotifUpdInput[0] = new MDONotificationUpdateInput();
            mdoModifNotifUpdInput[0].clientId = "replaceMDONotifications";
            mdoModifNotifUpdInput[0].notificationUpdateData = modifyUpdateData[0];
            m_mdoTestUtils.updateMDONotification(mdoModifNotifUpdInput);

            // Query for all notification information using the SOA QueryMDONotification with input design instance as DA1DE1
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].designInstance = (POM_object) DA1DE1;
            mdoNotifInputs[0].performCreateNotificationQueryAlso = false;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);

            LogFile.write( "Completed Test case 3.5" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Querying for notification information related test cases
            // Test case 4.1 Query for all notification information with input as design instance DA1DE1
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 4.1 - Query for notification with design instance DA1DE1" );
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].designInstance = (POM_object) DA1DE1;
            mdoNotifInputs[0].performCreateNotificationQueryAlso = false;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);

            LogFile.write( "Completed Test case 4.1" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 4.2 Create 2 Items - DA5(Mechanical), DA6(Mechanical),
            // Associate the Item Revisions - DA4/A, DA5/A and DA6/A through an MDThread - MDO2
            // Create reuse DEs - DA4DE2 in CDAuto, DA5DE1 and DA6DA1 in CDMech.
            // Create linkage MDI2 with DA4DE2, DA5DE1 and DA6DE1 with context as proj2.
            // Replace underlying IR of DE - DA4DE2 and DA5DE1
            // Query for all notification information with input contextOnMDI as proj2
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 4.2 - Create MDThread and link, replace IR, query for notification with contextOnMDI proj2" );

            // Create DA5/A - Mechanical (Part)
            response = m_mdoTestUtils.createDesignArtifactsWithType("DA5", "Part", "Description for DA5");
            ItemRevision DA5Rev = response.output[0].itemRev;

            // Create DA6/A - Mechanical (Part)
            response = m_mdoTestUtils.createDesignArtifactsWithType("DA6", "Part" , "Description for DA6");
            ItemRevision DA6Rev = response.output[0].itemRev;


            // Create MDO2 with DA4/A, DA5/A, DA6/A
            List<ItemRevision> revsOfDAsForMdo2 = new ArrayList<ItemRevision>();
            revsOfDAsForMdo2.add(DA4Rev);
            revsOfDAsForMdo2.add(DA5Rev);
            revsOfDAsForMdo2.add(DA6Rev);
            m_mdoTestUtils.createMDOWithDARevList(revsOfDAsForMdo2,"MDO2", "Description for MDO2");

            ModelObject DA5DE1 = m_mdoTestUtils.createReuseDEWithCD( DA5Rev, "DA5DE1", CDMech );
            ModelObject DA6DE1 = m_mdoTestUtils.createReuseDEWithCD( DA6Rev, "DA6DE1", CDMech );
            ModelObject DA4DE2 = m_mdoTestUtils.createReuseDEWithCD( DA4Rev, "DA4DE2", CDAuto );

            // Create linkage MDI2 with DA4DE2, DA5DE1 and DA6DE1 with context as proj2.
            List<ModelObject> dElemList2 = new ArrayList <ModelObject>();
            dElemList2.add(DA5DE1);
            dElemList2.add(DA6DE1);
            dElemList2.add(DA4DE2);

            m_mdoTestUtils.linkDesignInstances( dElemList2, context2, true);

            // Replace the underlying IR of DA4DE2 and DA5DE1
            response = m_mdoTestUtils.createDesignArtifactsWithType("DATest6", "PSConnection", "Description for DATest6" );
            ItemRevision DATestRev6 = response.output[0].itemRev;
            m_mdoTestUtils.updateReuseDE(DATestRev6,DA4DE2);

            response = m_mdoTestUtils.createDesignArtifactsWithType("DATest7", "Part", "Description for DATest7" );
            ItemRevision DATestRev7 = response.output[0].itemRev;
            m_mdoTestUtils.updateReuseDE(DATestRev7,DA5DE1);
            m_mdoTestUtils.sleepForNotificationCreation();

            // Query for all notification information with input contextOnMDI as- proj2
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].projectContext = context2;
            mdoNotifInputs[0].performCreateNotificationQueryAlso = false;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);

            LogFile.write( "Completed Test case 4.2" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 4.3 Query for all notification information with input design instance as DA1DE1 and contextOnMDI as proj2
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 4.3 - Query for notification with design instance DA1DE1 and contextOnMDI proj2" );
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].designInstance = (POM_object) DA1DE1;
            mdoNotifInputs[0].projectContext = context2;
            mdoNotifInputs[0].performCreateNotificationQueryAlso = false;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);

            LogFile.write( "Completed Test case 4.3" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 4.4 Query for all notification information with input as design instance - DA4DE2
            // and performCreateNotificationQuery = true
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 4.4 - Query for notification with design instance DA4DE2" );
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].designInstance = (POM_object) DA4DE2;
            mdoNotifInputs[0].performCreateNotificationQueryAlso = true;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);
            LogFile.write( "Completed Test case 4.4" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 4.5 Query for all notification information with input contextOnMDI as proj2 and filter type -Mechanical
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 4.5 - Query for notification with contextOnMDI proj2 and filter type Mechanical" );

            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].projectContext = context2;
            ImanType Im1 = new ImanType(DA1Rev.getTypeObject(), DA1Rev.getTypeObject().getUid());
            mdoNotifInputs[0].filterModifyTriggerNotificationByType = Im1;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);
            LogFile.write( "Completed Test case 4.5" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 4.6 Query for all notification information based on filter type Electrical and performCreateNotificationQuery = true
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 4.6 - Query for notification with filter type Electrical" );
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            Im1 = new ImanType(DA2Rev.getTypeObject(), DA2Rev.getTypeObject().getUid());
            mdoNotifInputs[0].filterCompatibleArtifactByType = Im1;
            mdoNotifInputs[0].performCreateNotificationQueryAlso = true;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);
            LogFile.write( "Completed Test case 4.6" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 4.7 Query for all notification information with input contextOnMDI as proj2,
            // filter by design as CDAuto and designFilterIsForInitialNotification = true
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 4.7 - Query for notification with contextOnMDI proj2 and filter by design CDAuto" );
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].projectContext = context2;
            mdoNotifInputs[0].filterByDesign = (POM_object) CDAuto;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);
            LogFile.write( "Completed Test case 4.7" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 4.8 Query for all notification information with input contextOnMDI as proj2,
            // filter by design as CDAuto and designFilterIsForInitialNotification = false
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 4.8 - Query for notification with contextOnMDI proj2 and filter by design CDAuto, not initial notification" );
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].projectContext = context2;
            mdoNotifInputs[0].filterByDesign = (POM_object) CDAuto;
            notifResp = m_mdoTestUtils.queryForMDONotifByImpactedDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);
            LogFile.write( "Completed Test case 4.8" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 4.9 Query for all notification information with filter by design as CDMech,
            // designFilterIsForInitialNotification = true and performCreateNotificationQuery = true
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 4.9 - Query for notification with filter by design CDMech" );
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].performCreateNotificationQueryAlso = true;
            mdoNotifInputs[0].filterByDesign = (POM_object) CDMech;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);
            LogFile.write( "Completed Test case 4.9" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 4.10 Query for all notification information with filter by design as CDElect1,
            // designFilterIsForInitialNotification = false and performCreateNotificationQuery = true
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 4.10 - Query for notification with filter by design CDElect1, not initial notification" );
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].performCreateNotificationQueryAlso = true;
            mdoNotifInputs[0].filterByDesign = (POM_object) CDElect1;
            notifResp = m_mdoTestUtils.queryForMDONotifByImpactedDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);
            LogFile.write( "Completed Test case 4.10" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 4.11 Query for all create and modify MDO notifications for a design
            // and ordered by date and grouped by design
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 4.11 - Query for all create and modify MDO notifications for a design, ordered by date and design" );
            mdoQryInputs = new QryInputByDesignOrProject[1];
            mdoQryInputs[0] = new QryInputByDesignOrProject();
            mdoQryInputs[0].clientId = "qryByDesign";
            mdoQryInputs[0].inputDesign = (POM_object) CDMech;
            Calendar dateYesterday = new GregorianCalendar();
            dateYesterday.add(Calendar.DAY_OF_MONTH, -1);
            Calendar dateNow = new GregorianCalendar();
            mdoQryInputs[0].dateCriteria.fromDate = dateYesterday;
            mdoQryInputs[0].dateCriteria.toDate = dateNow;
            qryResp = m_mdoTestUtils.qryNotificationsByDesignOrProject(mdoQryInputs);
            m_mdoTestUtils.printQueryResp(qryResp);
            LogFile.write( "Completed Test case 4.11" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 4.12 Query for all create and modify MDO notifications for all designs in a project
            // and ordered by date and grouped by design
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 4.12 - Query for all create and modify MDO notifications for a design in a project, ordered by date and design" );
            mdoQryInputs = new QryInputByDesignOrProject[1];
            mdoQryInputs[0] = new QryInputByDesignOrProject();
            mdoQryInputs[0].clientId = "qryByProject";
            mdoQryInputs[0].project = context2;
            mdoQryInputs[0].dateCriteria.fromDate = dateYesterday;
            mdoQryInputs[0].dateCriteria.toDate = dateNow;
            qryResp = m_mdoTestUtils.qryNotificationsByDesignOrProject(mdoQryInputs);
            m_mdoTestUtils.printQueryResp(qryResp);
            LogFile.write( "Completed Test case 4.12" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 5.1 Unrealize the reused DA1DE0
            // Query for all notification information using the SOA QueryMDONotification with input design instance as DA1DE0
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 5.1 - Unrealize the reused DA1DE0, query for notification" );
            ModelObject[] de = new ModelObject[1];
            de[0] = DA1DE0;
            m_mdoTestUtils.unrealizeDE(de);
            m_mdoTestUtils.sleepForNotificationCreation();
            mdoQryInputs[0] = new QryInputByDesignOrProject();
            mdoQryInputs[0].clientId = "querynotifs";
            mdoQryInputs[0].inputDesign = (POM_object) CDMech;
            qryResp = m_mdoTestUtils.qryNotificationsByDesignOrProject(mdoQryInputs);
            m_mdoTestUtils.printQueryResp(qryResp);
            LogFile.write( "Completed Test case 5.1" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 5.2 Unrealize the reused DA1DE1.
            // Query for all notification information using the SOA QueryMDONotification with input design instance as DA2DE1
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 5.2 - Unrealize the reused DA1DE1, query for notification" );
            de[0] = DA1DE1;
            m_mdoTestUtils.unrealizeDE(de);
            m_mdoTestUtils.sleepForNotificationCreation();
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].designInstance = (POM_object) DA2DE1;
            mdoNotifInputs[0].performCreateNotificationQueryAlso = false;
            notifResp = m_mdoTestUtils.queryForMDONotifByImpactedDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);
            LogFile.write( "Completed Test case 5.2" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test Case 6 - Realization container related test cases
            // Test case 6.1 - Create Baseline object BL1 and Create 8 items - DA21, DA22, Src1, Src2, Src3, Src4, DAElect1, DAAuto1
            // Create MDO5 with BL1/A, DA21/A, DA22/A and MDO6 with Src1/A, DAElect1/A, DAAuto1/A
            // Create Tc_project- proj5
            // Create 4 collaboration designs (CD) - CDSource1, CDtarget1 , CDElect11 and CDAuto11
            // Make CDSource1, CDtarget1 , CDElect11 and CDAuto11 part of proj5
            // Create reuse DEs - Src1DE1, Src2DE1, Src3DE1 and Src4DE1 in CDSource1
            // Query for all notification information using the SOA QueryMDONotification with performCreateNotificationQuery as true
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 6.1 - Realization container create Baseline object BL1, 8 items, MDThread, 4 CDs, reuse DEs, query for notification" );

            com.teamcenter.soa.client.model.Property[] objNameProp;

            // Create DA21/A - Mechanical (Part)
            response = m_mdoTestUtils.createDesignArtifactsWithType("DA21", "Item" , "Description for DA21");
            ItemRevision DA21Rev = response.output[0].itemRev;
            // Create DA22/A - Electrical (PSSignal)
            response = m_mdoTestUtils.createDesignArtifactsWithType("DA22", "Item" , "Description for DA22");
            ItemRevision DA22Rev = response.output[0].itemRev;
            // Create Src1/A - Source (Part)
            response = m_mdoTestUtils.createDesignArtifactsWithType("Src1", "Item" , "Description for Src1");
            ItemRevision Src1Rev = response.output[0].itemRev;
            // Create Src2/A - Source (Part)
            response = m_mdoTestUtils.createDesignArtifactsWithType("Src2", "Item" , "Description for Src2");
            ItemRevision Src2Rev = response.output[0].itemRev;
            // Create Src3/A - Source (Part)
            response = m_mdoTestUtils.createDesignArtifactsWithType("Src3", "Item" , "Description for Src3");
            ItemRevision Src3Rev = response.output[0].itemRev;
            // Create Src4/A - Source (Part)
            response = m_mdoTestUtils.createDesignArtifactsWithType("Src4", "Item" , "Description for Src4");
            ItemRevision Src4Rev = response.output[0].itemRev;
            // Create DAElect1/A - Automation (Part)
            response = m_mdoTestUtils.createDesignArtifactsWithType("DAElect1", "Item" , "Description for DAElect1");
            ItemRevision DAElect1Rev = response.output[0].itemRev;
            // Create DAAuto1/A - Automation (Part)
            response = m_mdoTestUtils.createDesignArtifactsWithType("DAAuto1", "Item" , "Description for DAAuto1");
            ItemRevision DAAuto1Rev = response.output[0].itemRev;

            // Create CDSource1
            ModelObject CDSource1 = m_mdoTestUtils.createCD("CDSource1");
            // Create CDElect1
            ModelObject CDTarget1 = m_mdoTestUtils.createCD("CDTarget1");
            // Create CDElect2
            ModelObject CDElect11 = m_mdoTestUtils.createCD("CDElect11");
            // Create CDAuto
            ModelObject CDAuto11 = m_mdoTestUtils.createCD("CDAuto11");

            //Creating Baseline BL1
            Mdl0Baseline BL1 = m_mdoTestUtils.createBaseline(( Cpd0CollaborativeDesign )CDSource1);
            objNameProp = MDOTestUtils.getObjectProperties(BL1, new String[] {"revision_list"});
            ModelObject[] revs = objNameProp[0].getModelObjectArrayValue();
            Mdl0BaselineRevision BL1Rev = (Mdl0BaselineRevision)revs[0];

            // Create MDO5 with BL1/A, DA21/A, DA22/A
            List<ItemRevision> revsOfDAsForMdo5 = new ArrayList<ItemRevision>();
            revsOfDAsForMdo5.add(BL1Rev);
            revsOfDAsForMdo5.add(DA21Rev);
            revsOfDAsForMdo5.add(DA22Rev);
            m_mdoTestUtils.createMDOWithDARevList(revsOfDAsForMdo5,"MDO5", "MDO5 for search notifications test");

            // Create MDO6 with Src1/A, DAElect1/A, DAAuto1/A
            List<ItemRevision> revsOfDAsForMdo6 = new ArrayList<ItemRevision>();
            revsOfDAsForMdo6.add(Src1Rev);
            revsOfDAsForMdo6.add(DAElect1Rev);
            revsOfDAsForMdo6.add(DAAuto1Rev);
             m_mdoTestUtils.createMDOWithDARevList(revsOfDAsForMdo6,"MDO6", "MDO6 for search notifications test");

            //Creating Project
            POM_object proj5 = m_mdoTestUtils.createProject( "proj5", "Project 5" );
            m_mdoTestUtils.setProjectAsMultiDisciplinary(proj5);

            //Add CDSource1, CDTarget1 , CDElect11 and CDAuto11 to proj5
            collaborativeDesigns = new ModelObject[4];
            collaborativeDesigns[0] = CDSource1;
            collaborativeDesigns[1] = CDTarget1;
            collaborativeDesigns[2] = CDElect11;
            collaborativeDesigns[3] = CDAuto11;
            m_mdoTestUtils.assignCDsToProject( collaborativeDesigns, proj5 );

            // Create reuse DE Src4DE1
            ModelObject Src4DE1 = m_mdoTestUtils.createReuseDEWithCD( Src4Rev, "Src4DE1", CDSource1 );
            // Create reuse DE Src2DE1
            ModelObject Src2DE1 = m_mdoTestUtils.createReuseDEWithCD( Src2Rev, "Src2DE1", CDSource1 );
            // Create reuse DE Src3DE1
            ModelObject Src3DE1 = m_mdoTestUtils.createReuseDEWithCD( Src3Rev, "Src3DE1", CDSource1 );
            // Create reuse DE Src1DE1
            ModelObject Src1DE1 = m_mdoTestUtils.createReuseDEWithCD( Src1Rev, "Src1DE1", CDSource1 );
            m_mdoTestUtils.sleepForNotificationCreation();

            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].performCreateNotificationQueryAlso = true;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);
            LogFile.write( "Completed Test case 6.1" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test Case 6.2 - Promote Src1DE1, Src2DE1, Src3DE1 to history. Add Src1DE1, Src2DE1, Src3DE1 to baseline BL1.
            // Create subset definition Subset1 from BL1 and add Src1DE1, Src2DE1, Src3DE1 to Subset
            // Create notification for Realization Container
            // Query for all notification information using the SOA QueryMDONotification with performCreateNotificationQuery as true
            // Create reuse DEs - DAElect1DE1 in CDElect11 and DAAuto11DE1 in CDAuto11
           LogFile.write( "------------------------------------------------------------------" );
           LogFile.write( "Executing Test case 6.2 - Realization container promote DEs, create subset, query for notification" );

            //Promote Src1DE1, Src2DE1, Src3DE1 to history
            WorkspaceObject[] promoteDAList = new WorkspaceObject[]{Src1Rev, Src2Rev, Src3Rev };
            m_mdoTestUtils.applyReleaseStatus( promoteDAList, "TCM Released" );
            ModelObject[] promoteList = new ModelObject[]{Src1DE1, Src2DE1, Src3DE1 };
            m_mdoTestUtils.promote( promoteList );
            for ( int i = 0; i < promoteList.length; ++i )
            {
                objNameProp = MDOTestUtils.getObjectProperties(promoteList[i], new String[] {"revision_number"});
                if( objNameProp[0].getIntValue() == 2 )
                {
                    objNameProp = MDOTestUtils.getObjectProperties(Src1DE1, new String[] {"object_name"});
                    LogFile.write( " Design Element:" + objNameProp[0].getDisplayableValue() + " promoted ");
                }
            }

            // Create a recipe for the baseline which selects all the DEs from the model.
            objNameProp = MDOTestUtils.getObjectProperties(BL1Rev, new String[] {"mdl0BaselineDefinition"});
            Mdl0BaselineDefinition blDefn = ( Mdl0BaselineDefinition )objNameProp[0].getModelObjectValue();
            ArrayList<Cpd0DesignElement> blDefnDeList = new ArrayList<Cpd0DesignElement>();
            blDefnDeList.add( ( Cpd0DesignElement )Src1DE1 );
            blDefnDeList.add( ( Cpd0DesignElement )Src2DE1 );
            blDefnDeList.add( ( Cpd0DesignElement )Src3DE1 );
            m_mdoTestUtils.createRecipe( ( Cpd0CollaborativeDesign )CDSource1, blDefn, blDefnDeList );

            // Create subset definition Subset1 from BL1 and add Src1DE1, Src2DE1, Src3DE1 to Subset
            Mdl0SubsetDefinition Subset1 =
           m_mdoTestUtils.createSubsetDefnFromBaselineDefn( ( Cpd0CollaborativeDesign )CDSource1, "CD_testAddDeToSubsetOfBaseline_", blDefn );

            // Add a recipe to the subset defn that selects a set of DEs that partially intersects the
            // set selected for the baseline.
            ArrayList<Cpd0DesignElement> subsetDefnDeList = new ArrayList<Cpd0DesignElement>();
            subsetDefnDeList.add( ( Cpd0DesignElement )Src1DE1 );
            subsetDefnDeList.add( ( Cpd0DesignElement )Src4DE1 );
            m_mdoTestUtils.createRecipe( ( Cpd0CollaborativeDesign )CDSource1, Subset1, subsetDefnDeList );

            String currentTime = (new java.util.Date()).toString();
            currentTime = currentTime.replace( " ", "_" );

            // Create a subset and append design elements.
            ModelObject [] SrcDEs = new ModelObject [2];
            SrcDEs[0] = Src2DE1;
            SrcDEs[1] = Src3DE1;
            m_mdoTestUtils.createSubset(CDSource1, Subset1, SrcDEs);

            //Call to create realization container
            ModelToModelRealizationContentResponse rlzResponse = m_mdoTestUtils.createRlzContainer(CDSource1, CDTarget1, Subset1 );
           m_mdoTestUtils.sleepForNotificationCreation();

            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].performCreateNotificationQueryAlso = true;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);

            // Fetch realization container RC1
            ModelObject[] RC1 = new ModelObject[1];
            RC1[0] = (ModelObject)rlzResponse.rmeData[0].realizationContainer;

            // Create reuse DE DAElect1DE1
            ModelObject DAElect1DE1 = m_mdoTestUtils.createReuseDEWithCD( DAElect1Rev, "DAElect1DE1", CDElect11 );
            // Create reuse DE DAAuto1DE1
            ModelObject DAAuto1DE1 = m_mdoTestUtils.createReuseDEWithCD( DAAuto1Rev, "DAAuto1DE1", CDAuto11 );
            m_mdoTestUtils.sleepForNotificationCreation();
            LogFile.write( "Completed Test case 6.2" );
            mdoObj.pauseBetweenUseCases( pauseStr );

           // Test Case 6.3 - Link RC1, DAElect1DE1 and DAAuto1DE1 using MDI5.
           // Query for notification information using the SOA QueryMDONotificationByDesignOrProject with design as CDTarget1.
           // Query for notification information using the SOA QueryMDONotificationByDesignOrProject with project as proj5.
           // Delete Realization container RC1.
           // Query for all notification information using the SOA queryForMDONotifByOriginatingDesign with performCreateNotificationQuery as false.
           // Query for notification information using the SOA QueryMDONotificationByDesignOrProject with design as CDTarget1.
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 6.3 - Link RC1, DAElect1DE1 and DAAuto1DE1 using MDI5, query, delete Realization container, query for notification" );

            // Create linkage MDI5 with RC1, DAElect1DE1 and DAAuto1DE1 with context as proj5.
            List<ModelObject> dElemList3 = new ArrayList <ModelObject>();
            dElemList3.add(RC1[0]);
            dElemList3.add(DAElect1DE1);
            dElemList3.add(DAAuto1DE1);

            m_mdoTestUtils.linkDesignInstances( dElemList3,  proj5, true);

            mdoQryInputs[0] = new QryInputByDesignOrProject();
            mdoQryInputs[0].clientId = "qrycreateRlzContainer";
            mdoQryInputs[0].inputDesign = (POM_object) CDTarget1;
            qryResp = m_mdoTestUtils.qryNotificationsByDesignOrProject(mdoQryInputs);
            m_mdoTestUtils.printQueryResp(qryResp); // only RC create notification returned

            // mdoQryInputs = new QryInputByDesignOrProject[1];
            mdoQryInputs[0] = new QryInputByDesignOrProject();
            mdoQryInputs[0].clientId = "qrycreateRlzContainer";
            mdoQryInputs[0].project = proj5;
            qryResp = m_mdoTestUtils.qryNotificationsByDesignOrProject(mdoQryInputs);
            m_mdoTestUtils.printQueryResp(qryResp);// all 3 notifications of CDs assigned to project and 1 RC create notification returned

            // Delete realization container RC1
            m_mdoTestUtils.cleanupData( RC1 );
            m_mdoTestUtils.sleepForNotificationCreation( );

            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "qrydeleteRlzContainer";
            mdoNotifInputs[0].performCreateNotificationQueryAlso = false;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);//0 notifications as per input returned

            mdoQryInputs[0] = new QryInputByDesignOrProject();
            mdoQryInputs[0].clientId = "qrydeleteRlzContainer";
            mdoQryInputs[0].inputDesign = (POM_object) CDTarget1;
            qryResp = m_mdoTestUtils.qryNotificationsByDesignOrProject(mdoQryInputs);
            m_mdoTestUtils.printQueryResp(qryResp);// only delete RC notification returned

            Calendar T1 = new GregorianCalendar();
            T1.add(Calendar.MONTH, +1);

            LogFile.write( "Completed Test case 6.3" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test Case 6.4 - Query for notification information using the SOA QueryMDONotificationByDesignOrProject with project as proj5.
            // Query for notification information using the SOA QueryMDONotificationByDesignOrProject with input design as CDTarget1 and from date as T1
           LogFile.write( "------------------------------------------------------------------" );
           LogFile.write( "Executing Test case 6.4 - Query for notification with project proj5, query with design CDTarget1 and date T1" );
            mdoQryInputs[0] = new QryInputByDesignOrProject();
            mdoQryInputs[0].clientId = "qrydeleteRlzContainer";
            mdoQryInputs[0].project = proj5;
            qryResp = m_mdoTestUtils.qryNotificationsByDesignOrProject(mdoQryInputs);
            m_mdoTestUtils.printQueryResp(qryResp);

            mdoQryInputs[0] = new QryInputByDesignOrProject();
            mdoQryInputs[0].clientId = "qrydeleteDE";
            mdoQryInputs[0].project = proj5;
            mdoQryInputs[0].dateCriteria.fromDate = T1;
            qryResp = m_mdoTestUtils.qryNotificationsByDesignOrProject(mdoQryInputs);
            m_mdoTestUtils.printQueryResp(qryResp);

            LogFile.write( "Completed Test case 6.4" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Setup objects for Test case 6.5" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Commenting out test case 7 - "Attribute attach and detach from a model element in 4GD" as
            // there is no out of the box attribute group type defined.
            // To use the Attribute attach and detach test cases with your own custom Attribute Group object,
            // replace Cpd9TestAttrGroup with your own type name in the file MDOTestUtils.java,
            // and uncomment below code and the methods in MDOTestUtils.java
            // (createAttrGrp, attrachAttrGrp, detachAndDeleteAttrGrp).

            // Test Case 7 Attribute attach and detach from a model element in 4GD
            /*
            // Test case 7.1
            // Create an attribute group AttrGrp1 of type Cpd9TestAttrGroup on CDElec11 using createobjects SOA
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 7.1 - Create an attribute group AttrGrp1 on CDElec11, query for notification" );
            Calendar timeBeforeCreate = new GregorianCalendar();
            ModelObject AttrGrp1 = m_mdoTestUtils.createAttrGrp("AttrGrp1", CDElect11);
            m_mdoTestUtils.sleepForNotificationCreation();

            Calendar timeAfterCreate = new GregorianCalendar();
            QryInputByDesignOrProject[] mdoNotifInputs2 = new QryInputByDesignOrProject[1];
            mdoNotifInputs2[0] = new QryInputByDesignOrProject();
            mdoNotifInputs2[0].clientId = "querynotifs";
            mdoNotifInputs2[0].dateCriteria.fromDate = timeBeforeCreate;
            mdoNotifInputs2[0].dateCriteria.toDate = timeAfterCreate;
            mdoNotifInputs2[0].project = proj5;
            QryNotificationByDesignResponse notifResp2 = m_mdoTestUtils.qryNotificationsByDesignOrProject(mdoNotifInputs2);
            // No MDO notifications are returned
            m_mdoTestUtils.printQueryResp(notifResp2);

            // Attach AttrGrp1 to Model Element DAElect1DE1 using SOA createOrUpdateRelations
            m_mdoTestUtils.attachAttrGrp(AttrGrp1, DAElect1DE1);
            // MDO notification Attach Attribute group N1 is generated for DAElect1DE1
            m_mdoTestUtils.sleepForNotificationCreation();

            timeAfterCreate = new GregorianCalendar();
            mdoNotifInputs2 = new QryInputByDesignOrProject[1];
            mdoNotifInputs2[0] = new QryInputByDesignOrProject();
            mdoNotifInputs2[0].clientId = "querynotifs";
            mdoNotifInputs2[0].dateCriteria.fromDate = timeBeforeCreate;
            mdoNotifInputs2[0].dateCriteria.toDate = timeAfterCreate;
            mdoNotifInputs2[0].project = proj5;
            notifResp2 = m_mdoTestUtils.qryNotificationsByDesignOrProject(mdoNotifInputs2);
            // MDO notification for Attribute Group Attach N1 for DAElect1DE1 is returned
            m_mdoTestUtils.printQueryResp(notifResp2);
            LogFile.write( "Completed Test case 7.1" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 7.2 setup
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 7.2 - Attach an attribute group AttrGrp2 to Tgt4DE1, query for notification" );
            // Create Design Elements on CDTarget1
            ModelObject Tgt4DE1 = m_mdoTestUtils.createReuseDEWithCD( Src4Rev, "Tgt4DE1", CDTarget1 );
            // Create an attribute group AttrGrp2 of type Cpd9TestAttrGroup on CDTarget1 using createobjects SOA
            ModelObject AttrGrp2 = m_mdoTestUtils.createAttrGrp("AttrGrp2", CDTarget1);
            // No MDO notification for Attach Attribute group is generated for Tgt4DE1

            // Attach AttrGrp2 to Model Element Tgt4DE1 using SOA createOrUpdateRelations
            m_mdoTestUtils.attachAttrGrp(AttrGrp2, Tgt4DE1);
            m_mdoTestUtils.sleepForNotificationCreation();

            timeAfterCreate = new GregorianCalendar();
            mdoNotifInputs2 = new QryInputByDesignOrProject[1];
            mdoNotifInputs2[0] = new QryInputByDesignOrProject();
            mdoNotifInputs2[0].clientId = "querynotifs";
            mdoNotifInputs2[0].dateCriteria.fromDate = timeBeforeCreate;
            mdoNotifInputs2[0].dateCriteria.toDate = timeAfterCreate;
            mdoNotifInputs2[0].project = proj5;
            notifResp2 = m_mdoTestUtils.qryNotificationsByDesignOrProject(mdoNotifInputs2);
            // Only 1 MDO notification for Attribute Group Attach for DAElect1DE1 is returned
            m_mdoTestUtils.printQueryResp(notifResp2);
            LogFile.write( "Completed Test case 7.2" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 7.3 setup
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 7.3 - Attach an attribute group AttrGrp3 to DAAuto1DE1, query, respond, query for notification" );
            // Create an attribute group AttrGrp3 of type Cpd9TestAttrGroup on CDAuto11 using createobjects SOA
            ModelObject AttrGrp3 = m_mdoTestUtils.createAttrGrp("AttrGrp3", CDAuto11);

            // Attach AttrGrp3 to Model Element DAAuto1DE1 using SOA createOrUpdateRelations
            m_mdoTestUtils.attachAttrGrp(AttrGrp3, DAAuto1DE1);
            m_mdoTestUtils.sleepForNotificationCreation();

            timeAfterCreate = new GregorianCalendar();
            mdoNotifInputs2 = new QryInputByDesignOrProject[1];
            mdoNotifInputs2[0] = new QryInputByDesignOrProject();
            mdoNotifInputs2[0].clientId = "querynotifs";
            mdoNotifInputs2[0].dateCriteria.fromDate = timeBeforeCreate;
            mdoNotifInputs2[0].dateCriteria.toDate = timeAfterCreate;
            mdoNotifInputs2[0].project = proj5;
            notifResp2 = m_mdoTestUtils.qryNotificationsByDesignOrProject(mdoNotifInputs2);
            // 2 MDO notifications for Attribute Group Attach for DAElect1DE1, DAAuto1DE1 N1, N2 are returned
            m_mdoTestUtils.printQueryResp(notifResp2);
            //Extract N1
            POM_object N1 = null;
            for(int inx= 0; inx < notifResp2.output[0].notificationOutputs.length; ++inx )
            {
                QryNotificationOutputByDesign notificationOutput = notifResp2.output[0].notificationOutputs[inx];
                for(int jnx=0; jnx<notificationOutput.notificationByDesignOutputs.length; ++jnx)
                {
                    if(notificationOutput.notificationByDesignOutputs[jnx].designInstance.getUid().equals(DAAuto1DE1.getUid()))
                    {
                        if (notificationOutput.notificationByDesignOutputs[jnx].notificationQualifier.equals("AttrGrpAttach"))
                        {
                            N1 = notificationOutput.notificationByDesignOutputs[jnx].notificationObject;
                        }
                    }
                }
            }

            // Update MDO notification of N1 saying CDAuto1 reacted for DAAuto1DE1 by attaching attribute group to it

            MDONotificationReactionData[] reactData2 = new MDONotificationReactionData[1];
            reactData2[0] = new MDONotificationReactionData();
            reactData2[0].notificationQualifier = "AttrGrpAttach";
            reactData2[0].reactedObject =  (POM_object) DAAuto1DE1;

            MDONotificationUpdateData[] updateData2 = new MDONotificationUpdateData[1];
            updateData2[0] = new MDONotificationUpdateData();
            updateData2[0].notificationQualifier = "create";
            updateData2[0].ignoreNotification = false;
            updateData2[0].notificationObject = N1 ; // notification object from query
            updateData2[0].reactedResponses = new  MDONotificationReactionData[1];
            updateData2[0].reactedResponses[0] = reactData2[0];

            MDONotificationUpdateInput[] mdoNotifUpdInput2 = new MDONotificationUpdateInput[1];
            mdoNotifUpdInput2[0] = new MDONotificationUpdateInput();
            mdoNotifUpdInput2[0].clientId = "updateMDONotifications";
            mdoNotifUpdInput2[0].notificationUpdateData = updateData2[0];

            UpdateMDONotificationResponse response2 = m_mdoTestUtils.updateMDONotification(mdoNotifUpdInput2);
            m_mdoTestUtils.sleepForNotificationCreation();

            timeAfterCreate = new GregorianCalendar();
            mdoNotifInputs2 = new QryInputByDesignOrProject[1];
            mdoNotifInputs2[0] = new QryInputByDesignOrProject();
            mdoNotifInputs2[0].clientId = "querynotifs";
            mdoNotifInputs2[0].dateCriteria.fromDate = timeBeforeCreate;
            mdoNotifInputs2[0].dateCriteria.toDate = timeAfterCreate;
            mdoNotifInputs2[0].project = proj5;
            notifResp2 = m_mdoTestUtils.qryNotificationsByDesignOrProject(mdoNotifInputs2);
            // Only 1 MDO notification N1 for Attribute group attach to DAElect1DE1 is returned.  It will have 1 accepted response
            m_mdoTestUtils.printQueryResp(notifResp2);
            LogFile.write( "Completed Test case 7.3" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 7.4 setup
            // Detach Attribute group AttrGrp1 from DAElect1DE1 using SOA deleteRelations
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 7.4 - Detach an attribute group AttrGrp1 from DAElect1DE1, query for notification" );
            m_mdoTestUtils.detachAndDeleteAttrGrp(DAElect1DE1, AttrGrp1);
            m_mdoTestUtils.sleepForNotificationCreation();

            timeAfterCreate = new GregorianCalendar();
            mdoNotifInputs2 = new QryInputByDesignOrProject[1];
            mdoNotifInputs2[0] = new QryInputByDesignOrProject();
            mdoNotifInputs2[0].clientId = "querynotifs";
            mdoNotifInputs2[0].dateCriteria.fromDate = timeBeforeCreate;
            mdoNotifInputs2[0].dateCriteria.toDate = timeAfterCreate;
            mdoNotifInputs2[0].project = proj5;
            notifResp2 = m_mdoTestUtils.qryNotificationsByDesignOrProject(mdoNotifInputs2);
            // 1 MDO notification N1 for Attribute group attach to DAElect1DE1 is returned.  It will have 1 accepted response.
            // 1 MDO notification for attribute group detach N3 for DAElect1DE1 will be returned
            m_mdoTestUtils.printQueryResp(notifResp2);
            LogFile.write( "Completed Test case 7.4" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 7.5 setup
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 7.4 - Detach an attribute group AttrGrp1 from Tgt4DE1, query for notification" );
            // Detach Attribute group AttrGrp2 from Tgt4DE1 using SOA deleteRelations
            m_mdoTestUtils.detachAndDeleteAttrGrp(Tgt4DE1, AttrGrp2);
            m_mdoTestUtils.sleepForNotificationCreation();

            timeAfterCreate = new GregorianCalendar();
            mdoNotifInputs2 = new QryInputByDesignOrProject[1];
            mdoNotifInputs2[0] = new QryInputByDesignOrProject();
            mdoNotifInputs2[0].clientId = "querynotifs";
            mdoNotifInputs2[0].dateCriteria.fromDate = timeBeforeCreate;
            mdoNotifInputs2[0].dateCriteria.toDate = timeAfterCreate;
            mdoNotifInputs2[0].project = proj5;
            notifResp2 = m_mdoTestUtils.qryNotificationsByDesignOrProject(mdoNotifInputs2);
            // 1 MDO notification N1 for Attribute group attach to DAElect1DE1 is returned.  It will have 1 accepted response.
            // 1 MDO notification for attribute group detach N3 for DAElect1DE1 will be returned
            m_mdoTestUtils.printQueryResp(notifResp2);
            LogFile.write( "Completed Test case 7.5" );
            mdoObj.pauseBetweenUseCases( pauseStr );
            */

            // Test case 8 Revise
            // Create 3 Items - DA11/A (Electrical), DA12/A(Electrical), DA13/A(Automation) and DA14/A (Mechanical).
            // DA11/A Electrical (PSSignal)
            response = m_mdoTestUtils.createDesignArtifactsWithType("DA11", "PSSignal", "Description for DA11");
            ItemRevision DA11Rev = response.output[0].itemRev;

            // Create DA12/A - Electrical (PSSignal)
            response = m_mdoTestUtils.createDesignArtifactsWithType("DA12", "PSSignal", "Description for DA12");
            ItemRevision DA12Rev = response.output[0].itemRev;

            // Create DA13/A - Automation (PSConnection)
            response = m_mdoTestUtils.createDesignArtifactsWithType("DA13", "PSConnection", "Description for DA13" );
            ItemRevision DA13Rev = response.output[0].itemRev;

            // Create DA14/A - Mechanical (Part)
            response = m_mdoTestUtils.createDesignArtifactsWithType("DA14", "Part", "Description for DA14");
            ItemRevision DA14Rev = response.output[0].itemRev;

            // Associate them through an MDThread- MDO4
            List<ItemRevision> revsOfDAsForMdo4 = new ArrayList<ItemRevision>();
            revsOfDAsForMdo4.add(DA11Rev);
            revsOfDAsForMdo4.add(DA12Rev);
            revsOfDAsForMdo4.add(DA13Rev);
            revsOfDAsForMdo4.add(DA14Rev);
            m_mdoTestUtils.createMDOWithDARevList(revsOfDAsForMdo4,"MDO4", "MDO4 for search notifications test");

            // Create shape DE-DA11DE1 in CD-CDElect1
            ModelObject DA11DE1 = m_mdoTestUtils.createShapeDE( "DA11DE1", CDElect1 );

            // Create reuse DE-DA12DE1 in CD-CDElect2
            ModelObject DA12DE1 = m_mdoTestUtils.createReuseDEWithCD( DA12Rev, "DA12DE1", CDElect2 );

            // Create promissory DE-DA13DE1 in CD-CDAuto
            ModelObject DA13DE1 = m_mdoTestUtils.createPromissoryDE( "DA13DE1", CDAuto );

            // Create promissory DA14DE1 in CDMech
            ModelObject DA14DE1 = m_mdoTestUtils.createPromissoryDE( "DA14DE1", CDMech );

            //Create MDI4 by linking DA11DE1, DA12DE1(imprecise) and DA14DE1 with context proj4.
            List<ModelObject> dElemList4 = new ArrayList <ModelObject>();
            dElemList4.add(DA11DE1);
            dElemList4.add(DA12DE1);
            dElemList4.add(DA14DE1);

            //Creating MDI4
            m_mdoTestUtils.linkDesignInstances( dElemList4,  context2, false);

            // Released shape DE-DA11DE1, reuse DE-DA12DE1, promissory DE-DA13DE1 and promissory DE-DA14DE1
            Cpd0DesignElement[] deObjs = new Cpd0DesignElement[4];
            deObjs[0] = (Cpd0DesignElement) DA11DE1;
            deObjs[1] = (Cpd0DesignElement)DA12DE1;
            deObjs[2] = (Cpd0DesignElement)DA13DE1;
            deObjs[3] = (Cpd0DesignElement)DA14DE1;
            m_mdoTestUtils.applyReleaseStatus( (WorkspaceObject[]) deObjs, "TCM Released" );


            // Test case 8.1 Revise released shape DE-DA11DE1
            // Query for all notification information using the SOA QueryMDONotificationByOriginatingDesign with input design instance as DA11DE1
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 8.1 - Revise released shape DE-DA11DE1, query for notification with design instance DA11DE1" );
            m_mdoTestUtils.reviseDE(DA11DE1);
            m_mdoTestUtils.sleepForNotificationCreation();
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].designInstance = (POM_object) DA11DE1;
            mdoNotifInputs[0].performCreateNotificationQueryAlso = false;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);
            LogFile.write( "Completed Test case 8.1" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 8.2 Revise released reuse DE-DA12DE1
            // Query for all notification information using the SOA QueryMDONotificationByOriginatingDesign with input design instance as DA12DE1
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 8.2 - Revise released reuse DE-DA12DE1, query for notification with design instance DA12DE1" );
            m_mdoTestUtils.reviseDE(DA12DE1);
            m_mdoTestUtils.sleepForNotificationCreation();
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].designInstance = (POM_object) DA12DE1;
            mdoNotifInputs[0].performCreateNotificationQueryAlso = false;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);
            LogFile.write( "Completed Test case 8.2" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 8.3 Revise released promissory DE-DA13DE1
            // Query for all notification information using the SOA QueryMDONotificationByOriginatingDesign with input design instance as DA13DE1
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 8.3 - Revise released promissory DE-DA13DE1, query for notification with design instance DA13DE1" );
            m_mdoTestUtils.reviseDE(DA13DE1);
            m_mdoTestUtils.sleepForNotificationCreation();
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].designInstance = (POM_object) DA13DE1;
            mdoNotifInputs[0].performCreateNotificationQueryAlso = false;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);
            LogFile.write( "Completed Test case 8.3" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 8.4 Revise released promissory DE-DA14DE1
            // Query for all notification information using the SOA QueryMDONotificationByOriginatingDesign with input design instance as DA14DE1
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 8.4 - Revise released promissory DE-DA14DE1, query for notification with design instance DA14DE1" );
            m_mdoTestUtils.reviseDE(DA14DE1);
            m_mdoTestUtils.sleepForNotificationCreation();
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].designInstance = (POM_object) DA14DE1;
            mdoNotifInputs[0].performCreateNotificationQueryAlso = false;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);
            LogFile.write( "Completed Test case 8.4" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 12: Notifications based on TC_Project context

            // Test case 12.6 Realize DE in a CD assigned to multi-disciplinary project
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 12.6 Realize DE in a CD assigned to multi-disciplinary project" );

            // Create Item  DA15/A (Electrical), DA16/A (Electrical) and DA17/A (Automation) Associate them through an MDThread- MDO5.

            response = m_mdoTestUtils.createDesignArtifactsWithType("DA15", "PSSignal", "Description for DA15");
            ItemRevision DA15Rev = response.output[0].itemRev;

            // Create DA12/A - Electrical (PSSignal)
            response = m_mdoTestUtils.createDesignArtifactsWithType("DA16", "PSSignal", "Description for DA16");
            ItemRevision DA16Rev = response.output[0].itemRev;

            // Create DA17/A - Automation (PSConnection)
            response = m_mdoTestUtils.createDesignArtifactsWithType("DA17", "PSConnection", "Description for DA17" );
            ItemRevision DA17Rev = response.output[0].itemRev;

            // Associate them through an MDThread- MDO7
            List<ItemRevision> revsOfDAsForMdo7 = new ArrayList<ItemRevision>();
            revsOfDAsForMdo7.add(DA15Rev);
            revsOfDAsForMdo7.add(DA16Rev);
            revsOfDAsForMdo7.add(DA17Rev);
            m_mdoTestUtils.createMDOWithDARevList(revsOfDAsForMdo7,"MDO7", "MDO7 for search notifications test");

            // Create TC_Project Proj6, such that the property fnd0CollaborationCategories contains the value Multi-Disciplinary.
            //Creating Project
            POM_object context6 = m_mdoTestUtils.createProject( "proj6", "Project 6" );
            m_mdoTestUtils.setProjectAsMultiDisciplinary(context6);

            // Create CD - CDElect3.
            ModelObject CDElect3 = m_mdoTestUtils.createCD("CDElect3");

            // Assign TC_Project proj6 to CDElect3.
            ModelObject[] collDesigns = new ModelObject[1];
            collDesigns[0] = CDElect3;
            m_mdoTestUtils.assignCDsToProject(collDesigns, context6);

            // Create reuse DE-DA15DE1 in CD-CDElect3
            m_mdoTestUtils.createReuseDEWithCD ( DA15Rev, "DA15DE1", CDElect3 );

            m_mdoTestUtils.sleepForNotificationCreation();
            // Query for all notification information using the SOA QueryForMDONotificationsByOriginatingDesign  with
            // performCreateNotificationQueryAlso as true
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].performCreateNotificationQueryAlso = true;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);
            LogFile.write( "Completed Test case 12.6" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 12.7 Realize DE in a CD not assigned to multi-disciplinary project
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 12.7 Realize DE in a CD not assigned to multi-disciplinary project" );

            // Create TC_Project Proj7, such that the property fnd0CollaborationCategories does not contain the value Multi-Disciplinary.
            POM_object context7 = m_mdoTestUtils.createProject( "proj7", "Project 7" );

            // Create CD - CDElect4.
            ModelObject CDElect4 = m_mdoTestUtils.createCD("CDElect4");

            // Assign TC_Project Proj7 to CDElect4.
            collDesigns[0] = CDElect4;
            m_mdoTestUtils.assignCDsToProject(collDesigns, context7);

            // Create reuse DE-DA16DE1 in CD-CDElect4
            m_mdoTestUtils.createReuseDEWithCD(DA16Rev, "DA16DE1", CDElect4 );
            m_mdoTestUtils.sleepForNotificationCreation();

            // Query for all notification information using the SOA QueryForMDONotificationsByOriginatingDesign  with  performCreateNotificationQueryAlso as true
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].performCreateNotificationQueryAlso = true;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);
            LogFile.write( "Completed Test case 12.7" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 12.8 Query for create notifications based on context
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 12.8 Query for create notifications based on context" );
            // Create CD - CDAuto3.
            ModelObject CDAuto3 = m_mdoTestUtils.createCD("CDAuto3");
            // Assign TC_Project Proj6 to CDAuto3.
            collDesigns[0] = CDAuto3;
            m_mdoTestUtils.assignCDsToProject(collDesigns, context6);
            // Create reuse DE-DA16DE2 in CD-CDElect3 and reuse DE - DA17DE1 in CDAuto3
            m_mdoTestUtils.createReuseDEWithCD( DA16Rev, "DA16DE2", CDElect3 );
            m_mdoTestUtils.sleepForNotificationCreation();
            m_mdoTestUtils.createReuseDEWithCD( DA17Rev, "DA17DE1", CDAuto3 );
            m_mdoTestUtils.sleepForNotificationCreation();
            // Query for all notification information using the SOA QueryForMDONotificationsByOriginatingDesign
            // with  performCreateNotificationQueryAlso as true and input  contextOnMDI as Proj6.
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].performCreateNotificationQueryAlso = true;
            mdoNotifInputs[0].projectContext = context6;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);
            LogFile.write( "Completed Test case 12.8" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 12.9 Test creation of create notifications per each Multi-Disciplinary project
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 12.9 Test creation of create notifications per each Multi-Disciplinary project" );

            // Assign TC_Project Proj7 to CDAuto4.
            ModelObject CDAuto4 = m_mdoTestUtils.createCD("CDAuto4");
            collDesigns = new ModelObject[1];
            collDesigns [0] = CDAuto4;
            m_mdoTestUtils.assignCDsToProject(collDesigns, context2);
            m_mdoTestUtils.assignCDsToProject(collDesigns, context6);

            // Create reuse DE - DA17DE2 in CDAuto4
            m_mdoTestUtils.createReuseDEWithCD( DA17Rev, "DA17DE2", CDAuto4 );
            m_mdoTestUtils.sleepForNotificationCreation();
            // Query for all notification information using the SOA QueryForMDONotificationsByOriginatingDesign  with  performCreateNotificationQueryAlso as true
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].performCreateNotificationQueryAlso = true;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);
            LogFile.write( "Completed Test case 12.9" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 17 - Realization of library element in 4GD design
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 17.1 - Realization of library element LE1 in 4GD design" );

            // create data for library creation.
            String[] disciplines = new String[2];
            disciplines[0] = "Mechanical";
            disciplines[1] = "Electrical";

            String[] allowedMemberTypes = new String[2];
            allowedMemberTypes[0]="Item";
            allowedMemberTypes[1]="ItemRevision";

            int numLibrariesToCreate = 1;
            Lbr0Library[] createdLibraries = m_mdoTestUtils.createLbr0Librararies( "ST00Library", disciplines, allowedMemberTypes, numLibrariesToCreate );

            // one Hierarchy and and one General Node
            CreateHierarchiesResponse hierarchyResponse;
            hierarchyResponse = m_mdoTestUtils.createHierarchy(createdLibraries[0], "ST00Hierarchy" );

            CreateNodesResponse nodeResponse;
            nodeResponse = m_mdoTestUtils.createLbrHierarchyGenralNode ( hierarchyResponse.output[0].hierarchy, null, "Lbr0_pub_Node1" );

            // Create 3 Items  DAForLE1, DAForLE2 and DAForLE3
            response = m_mdoTestUtils.createDesignArtifactsWithType("DAForLE1", "Item", "Description for DAForLE1" );
            Item DAForLE1 = response.output[0].item;
            ItemRevision DAForLE1Rev = response.output[0].itemRev;

            response = m_mdoTestUtils.createDesignArtifactsWithType("DAForLE2", "Item", "Description for DAForLE2" );
            ItemRevision DAForLE2Rev = response.output[0].itemRev;

            response = m_mdoTestUtils.createDesignArtifactsWithType("DAForLE3", "Item", "Description for DAForLE3" );
            Item DAForLE3 = response.output[0].item;
            ItemRevision DAForLE3Rev = response.output[0].itemRev;

            BusinessObject[] itemList1 = new BusinessObject[1];
            itemList1[0] = DAForLE1;
            PublishObjectsResponse publishedResponse = m_mdoTestUtils.publishLibraryObjects( nodeResponse.output[0].node, itemList1 , "LE1", 1 );
            ModelObject LE1 = publishedResponse.serviceData.getCreatedObject(0);

            //   Create MDThread - MDOForLE using DAForLE1/A, DAForLE2/A, DAForLE3/A
            List<ItemRevision> revsOfDAsForMdoForLE = new ArrayList<ItemRevision>();
            revsOfDAsForMdoForLE.add(DAForLE1Rev);
            revsOfDAsForMdoForLE.add(DAForLE2Rev);
            revsOfDAsForMdoForLE.add(DAForLE3Rev);
            m_mdoTestUtils.createMDOWithDARevList(revsOfDAsForMdoForLE, "MDOForLE", "MDOForLE for realization of library elements test");

            //   Create Tc_project ProjForLE
            POM_object ProjForLE = m_mdoTestUtils.createProject( "ProjForLE", "Project for LE" );
            m_mdoTestUtils.setProjectAsMultiDisciplinary(ProjForLE);

            //   Create a Collaborative Design CDForLE and make it part of ProjForLE..
            ModelObject CDForLE = m_mdoTestUtils.createCD("CDForLE");
            collDesigns = new ModelObject[1];
            collDesigns[0] = CDForLE;
            m_mdoTestUtils.assignCDsToProject(collDesigns, ProjForLE);

            //  Realize LE1 in CDForLE as DEForLE1
            ModelObject DEForLE1 = m_mdoTestUtils.realizeLibraryElement(LE1, CDForLE, "DEForLE1" );

            m_mdoTestUtils.sleepForNotificationCreation();
            // Query for MDO notification using QueryForMDONotificationsByOriginatingDesign with performCreateNotificationQueryAlso=true
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].performCreateNotificationQueryAlso = true;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);
            LogFile.write( "Completed Test case 17.1" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 17.2
            // Create Item  DAForLE4
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 17.2 - Realization of library element LE2 in 4GD design" );
            response = m_mdoTestUtils.createDesignArtifactsWithType("DAForLE4", "Item", "Description for DAForLE4" );
            Item DAForLE4 = response.output[0].item;


            // Create a Library Element LE2 with underlying IR as DAForLE4/A
            itemList1[0] = DAForLE4;
            publishedResponse = m_mdoTestUtils.publishLibraryObjects( nodeResponse.output[0].node, itemList1 , "LE2", 1 );
            ModelObject LE2 = publishedResponse.serviceData.getCreatedObject(0);

            // Realize LE2 in CDForLE as DEForLE2
            ModelObject DEForLE2 = m_mdoTestUtils.realizeLibraryElement(LE2, CDForLE, "DEForLE2" );
            m_mdoTestUtils.sleepForNotificationCreation();

            // Query for MDO notification using QueryForMDONotificationsByOriginatingDesign with performCreateNotificationQueryAlso=true
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].performCreateNotificationQueryAlso = true;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);
            LogFile.write( "Completed Test case 17.2" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 18 - Unrealization of library element in 4GD design
            // Create a Library Element LE3 with underlying IR as DAForLE3/A
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 18 - Unrealization of library element in 4GD design" );

            itemList1[0] = DAForLE3;
            publishedResponse = m_mdoTestUtils.publishLibraryObjects( nodeResponse.output[0].node, itemList1 , "LE3", 1 );
            ModelObject LE3 = publishedResponse.serviceData.getCreatedObject(0);

            LogFile.write( "Executing Test case 18.1 - Realization of library element LE3 in 4GD design" );
            // Realize LE3 in CDForLE as DEForLE3
            ModelObject DEForLE3 = m_mdoTestUtils.realizeLibraryElement(LE3, CDForLE, "DEForLE3" );
            m_mdoTestUtils.sleepForNotificationCreation();

            // Create MDIForLE by linking DEForLE1 and DEForLE3 with context as ProjForLE
            List<ModelObject> dElemListForLE = new ArrayList <ModelObject>();
            dElemListForLE.add(DEForLE1);
            dElemListForLE.add(DEForLE3);

            m_mdoTestUtils.linkDesignInstances( dElemListForLE, ProjForLE, true);
            // Unrealize the reused DEForLE1
            ModelObject[] deforUnrealize = new ModelObject[1];
            deforUnrealize[0] = DEForLE1;
            m_mdoTestUtils.unrealizeDE(deforUnrealize);
            m_mdoTestUtils.sleepForNotificationCreation();

            // Query for all notification information using the SOA QueryForMDONotificationsByOriginatingDesign with input design instance as DEForLE1
            mdoQryInputs = new QryInputByDesignOrProject[1] ;
            mdoQryInputs [0] = new QryInputByDesignOrProject();
            mdoQryInputs[0].clientId = "querynotifs";
            mdoQryInputs[0].inputDesign = (POM_object) CDForLE;
            qryResp = m_mdoTestUtils.qryNotificationsByDesignOrProject(mdoQryInputs);
            m_mdoTestUtils.printQueryResp(qryResp);
            LogFile.write( "Completed Test case 18.1" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            LogFile.write( "Executing Test case 18.2 - Unrealization of library element LE3 in 4GD design" );

            // Unrealize the reused DEForLE2
            ModelObject[] deforUnrealize1 = new ModelObject[1];
            deforUnrealize1[0] = DEForLE2;
            m_mdoTestUtils.unrealizeDE(deforUnrealize1);
            m_mdoTestUtils.sleepForNotificationCreation();

            mdoQryInputs = new QryInputByDesignOrProject[1] ;
            // Query for all notification information using the SOA QueryForMDONotificationsByOriginatingDesign with input design instance as DEForLE1
            mdoQryInputs [0] = new QryInputByDesignOrProject();
            mdoQryInputs[0].clientId = "querynotifs";
            mdoQryInputs[0].inputDesign = (POM_object) CDForLE;
            qryResp = m_mdoTestUtils.qryNotificationsByDesignOrProject(mdoQryInputs);
            m_mdoTestUtils.printQueryResp(qryResp);
            LogFile.write( "Completed Test case 18.2" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 19 - Notifications for design elements in multiple MDInstances
            // Test case 19.1 - Notifications for revise for design elements in multiple MDInstances
            // Create 3 Items DA18/A (PSConnection), DA19/A (PSConnection), DA20/A (Automation) Associate them through an MDThread- MDO8.

            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 19 -  Notifications for design elements in multiple MDInstances" );
            LogFile.write( "Executing Test case 19.1 -  Notifications for revise for design elements in multiple MDInstances" );

            response = m_mdoTestUtils.createDesignArtifactsWithType("DA18", "PSConnection", "Description for DA18");
            ItemRevision DA18Rev = response.output[0].itemRev;

            // Create DA19/A - Electrical (PSSignal)
            response = m_mdoTestUtils.createDesignArtifactsWithType("DA19", "PSConnection", "Description for DA19");
            ItemRevision DA19Rev = response.output[0].itemRev;

            // Create DA20/A - Automation (PSConnection)
            response = m_mdoTestUtils.createDesignArtifactsWithType("DA20", "PSConnection", "Description for DA20" );
            ItemRevision DA20Rev = response.output[0].itemRev;

            // Associate them through an MDThread- MDO8
            List<ItemRevision> revsOfDAsForMdo8 = new ArrayList<ItemRevision>();
            revsOfDAsForMdo8.add(DA18Rev);
            revsOfDAsForMdo8.add(DA19Rev);
            revsOfDAsForMdo8.add(DA20Rev);
            m_mdoTestUtils.createMDOWithDARevList(revsOfDAsForMdo8,"MDO8", "MDO8 for search notifications test");

            // Create resue DE-DA18DE1 in CD-CDElect1, DA19DE1 in CD-CDElect2 and DA20DE1 in CD-CDAuto1.
            ModelObject DA18DE1 = m_mdoTestUtils.createReuseDEWithCD( DA18Rev, "DA18DE1", CDAuto4 );
            m_mdoTestUtils.sleepForNotificationCreation();
            ModelObject DA19DE1 = m_mdoTestUtils.createReuseDEWithCD( DA19Rev, "DA19DE1", CDAuto4 );
            m_mdoTestUtils.sleepForNotificationCreation();
            ModelObject DA20DE1 = m_mdoTestUtils.createReuseDEWithCD( DA20Rev, "DA20DE1", CDAuto4 );
            m_mdoTestUtils.sleepForNotificationCreation();

            // Create MDI6 by linking DA18DE1, DA19DE1 and DA20DE1 with context Proj1.

            List<ModelObject> dElemList6 = new ArrayList <ModelObject>();
            dElemList6.add(DA18DE1);
            dElemList6.add(DA19DE1);
            dElemList6.add(DA20DE1);
            m_mdoTestUtils.linkDesignInstances( dElemList6,  context2, true);


            // Create MDI7 by linking DA18DE1, DA19DE1 and DA20DE1 with context Proj6.
            List<ModelObject> dElemList7 = new ArrayList <ModelObject>();
            dElemList7.add(DA18DE1);
            dElemList7.add(DA19DE1);
            dElemList7.add(DA20DE1);
            m_mdoTestUtils.linkDesignInstances( dElemList7,  context6, true);

            // Revise released DE-DA18DE1
            // Released shape DE-DA18DE1
            Cpd0DesignElement[] deObjs1 = new Cpd0DesignElement[1];
            deObjs1[0] = (Cpd0DesignElement) DA18DE1;

            m_mdoTestUtils.applyReleaseStatus( (WorkspaceObject[]) deObjs1, "TCM Released" );
            m_mdoTestUtils.reviseDE(DA18DE1);
            m_mdoTestUtils.sleepForNotificationCreation();

            // Query for all notification information using the SOA QueryMDONotificationByOriginatingDesign  with  input design instance as DA18DE1
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].performCreateNotificationQueryAlso = false;
            mdoNotifInputs[0].designInstance = (POM_object) DA18DE1;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);
            LogFile.write( "Completed Test case 19.1" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 19.2 - Notifications for replace for design elements in multiple MDInstances
            // Replace the underlying IR of DA19DE1
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 19.2 -  Notifications for replace for design elements in multiple MDInstances" );

            response = m_mdoTestUtils.createDesignArtifactsWithType("DA19_2", "PSSignal", "Description for DA19_2" );
            ItemRevision DA19_2Rev = response.output[0].itemRev;
            m_mdoTestUtils.updateReuseDE(DA19_2Rev,DA19DE1);
            m_mdoTestUtils.sleepForNotificationCreation();

            // Query for all notification information using the SOA QueryForMDONotificationsByOriginatingDesign  with  input design instance as DA19DE1
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].performCreateNotificationQueryAlso = false;
            mdoNotifInputs[0].designInstance = (POM_object) DA19DE1;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);
            LogFile.write( "Completed Test case 19.2" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Commenting out test case 19.3 and 19.4 as
            // there is no out of the box attribute group type defined.
            // To use the Attribute attach and detach test cases with your own custom Attribute Group object,
            // replace Cpd9TestAttrGroup with your own type name in the file MDOTestUtils.java,
            // and uncomment below code and the methods in MDOTestUtils.java
            // (createAttrGrp, attrachAttrGrp, detachAndDeleteAttrGrp).

            // Test case 19.3 - Notifications for attribute group attach for design elements in multiple MDInstances
            //LogFile.write( "------------------------------------------------------------------" );
            //LogFile.write( "Executing Test case 19.3 -  Notifications for attribute group attach for design elements in multiple MDInstances" );

            // Create an attribute group AttrGrpForMultiMDI of type Cpd9TestAttrGroup on CDAuto4 using createobjects SOA
            /*ModelObject AttrGrpForMultiMDI = m_mdoTestUtils.createAttrGrp("AttrGrpForMultiMDI", CDAuto4);
            // Attach AttrGrpForMultiMDI to Design Element DA20DE1 using SOA createOrUpdateRelations.
            m_mdoTestUtils.attachAttrGrp(AttrGrpForMultiMDI, DA20DE1);
            m_mdoTestUtils.sleepForNotificationCreation();

            // Query for all notification information using the SOA QueryForMDONotificationsByOriginatingDesign  with  input design instance as DA20DE1
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].performCreateNotificationQueryAlso = false;
            mdoNotifInputs[0].designInstance = (POM_object) DA20DE1;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);
            LogFile.write( "Completed Test case 19.3" );
            mdoObj.pauseBetweenUseCases( pauseStr );

            // Test case 19.4 - Notifications for attribute group detach for design elements in multiple MDInstances
            //LogFile.write( "------------------------------------------------------------------" );
            //LogFile.write( "Executing Test case 19.4 -  Notifications for attribute group detach for design elements in multiple MDInstances" );
            // Detach Attribute group AttrGrpForMultiMDI from DA20DE1 using SOA deleteRelations
            m_mdoTestUtils.detachAndDeleteAttrGrp(DA20DE1, AttrGrpForMultiMDI);
            m_mdoTestUtils.sleepForNotificationCreation();

            // Query for all notification information using the SOA QueryForMDONotificationsByOriginatingDesign  with  input design instance as DA20DE1
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querynotifs";
            mdoNotifInputs[0].performCreateNotificationQueryAlso = false;
            mdoNotifInputs[0].designInstance = (POM_object) DA20DE1;
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);
            LogFile.write( "Completed Test case 19.4" );
            mdoObj.pauseBetweenUseCases( pauseStr );*/

            // Test case 19.5 - Notifications for unrealize for design elements in multiple MDInstances
            // Unrealize DE - DA19DE1
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing Test case 19.5 -  Notifications for unrealize for design elements in multiple MDInstances" );
            de [0] = DA19DE1;
            m_mdoTestUtils.unrealizeDE(de);
            m_mdoTestUtils.sleepForNotificationCreation();

            mdoQryInputs = new QryInputByDesignOrProject[1];

            // Query for all notification information using the SOA QueryMDONotificationByOriginatingDesign  with  input design instance as DA19DE1
            mdoQryInputs[0] = new QryInputByDesignOrProject();
            mdoQryInputs[0].clientId = "querynotifs";
            mdoQryInputs[0].inputDesign = (POM_object) CDAuto4;
            qryResp = m_mdoTestUtils.qryNotificationsByDesignOrProject(mdoQryInputs);
            m_mdoTestUtils.printQueryResp(qryResp);
            LogFile.write( "Completed Test case 19.5" );
            mdoObj.pauseBetweenUseCases( pauseStr );
        }
        catch(Exception ex)
        {
              ex.printStackTrace();
        }
        LogFile.write( "Completed Usecase 13 - Find Notification objects" );

    }


    /**
     * Makes the flow to 'pause' between the use-cases
     * @param pauseStr
     * @throws Exception
     */
    public void pauseBetweenUseCases( String pauseStr ) throws Exception
    {
        try
        {
            if( pauseStr != null )
            {
                if( pauseStr.equals( "true" ) )
                {
                    BufferedReader stdin =
                        new BufferedReader( new InputStreamReader( System.in ) );

                    System.out.println( "Press Enter to continue." );
                    stdin.read();
                }
            }
        }
        catch( Exception ex )
        {
            LogFile.write( "pauseBetweenUseCases : " + ex.getMessage() );
            throw ex;
        }
    }




    /**
     * Tests splitting Design Artifacts from multiple MDThreads
     *
     */
    public static void executeSplitDesignArtifactFromMDTs()
    {
        MDOTestUtils mdoTestUtils = new MDOTestUtils();
        try
        {
            List<ItemRevision> revsOfDAs = mdoTestUtils.createDesignArtifacts(4,"SplitDesignArtifact");
            WorkspaceObject newMDT1 = mdoTestUtils.createMDOWithDARevList(revsOfDAs,"MDThread1_Split", "MDT for split");

            if ( newMDT1 == null )
            {
                LogFile.write("Error creating MDThread1_Split");
            }
            mdoTestUtils.createMDOWithDARevList(revsOfDAs,"MDThread2_Split", "MDT for split");

            List<ItemRevision> revOfDAOther = mdoTestUtils.createDesignArtifacts(1,"SplitDAOther");

            ItemRevision revisedDaRev1 = mdoTestUtils.reviseItemRev( revsOfDAs.get(0), "B" );
            LogFile.write("Revised " + revsOfDAs.get(0).get_object_string() + " and got " + revisedDaRev1.get_object_string() );

            ItemRevision revisedDaRev2 = mdoTestUtils.reviseItemRev( revsOfDAs.get(1), "B" );
            LogFile.write("Revised " + revsOfDAs.get(1).get_object_string() + " and got " + revisedDaRev2.get_object_string() );

            boolean testPassed = true;
            // negative case, try to split non-latest revisions
            WorkspaceObject[] nonLatestRevs = new WorkspaceObject[2];
            nonLatestRevs[0] = revsOfDAs.get(0);
            nonLatestRevs[1] = revsOfDAs.get(1);
            SplitDesignArtifactsResponse resp1 = mdoTestUtils.splitDesignArtifacts( nonLatestRevs );
            if ( resp1.serviceData.sizeOfPartialErrors() == 1 ) // MDO_design_artifacts_are_not_latest_rev
            {
                LogFile.write( "splitDesignArtifacts correct error MDO_design_artifacts_are_not_latest_rev" );
            }
            else
            {
                testPassed = false;
            }
            // negative case, try to split non-related revisions
            WorkspaceObject[] nonRelatedRevs = new WorkspaceObject[2];
            nonRelatedRevs[0] = revisedDaRev1;
            nonRelatedRevs[1] = revOfDAOther.get(0);
            SplitDesignArtifactsResponse resp2 = mdoTestUtils.splitDesignArtifacts( nonRelatedRevs );
            if ( resp2.serviceData.sizeOfPartialErrors() == 1 ) // MDO_no_mdthreads_found
            {
                LogFile.write( "splitDesignArtifacts correct error MDO_no_mdthreads_found" );
            }
            else
            {
                testPassed = false;
            }
            // split latest revisions
            WorkspaceObject[] latestRevs = new WorkspaceObject[2];
            latestRevs[0] = revisedDaRev1;
            latestRevs[1] = revisedDaRev2;
            SplitDesignArtifactsResponse resp3 = mdoTestUtils.splitDesignArtifacts( latestRevs );
            if ( resp3.serviceData.sizeOfPartialErrors() == 0 &&
                    resp3.output.length == 1 &&
                    resp3.output[0].isSuccess &&
                    resp3.output[0].newMDTObject != null &&
                    resp3.output[0].updatedMDTObjects.length == 2
                    )
            {
                LogFile.write( "splitDesignArtifacts passed" );
            }
            else
            {
                testPassed = false;
            }
            if ( testPassed )
            {
                LogFile.write( "executeSplitDesignArtifact succeeded" );
            }
            else
            {
                LogFile.write( "executeSplitDesignArtifact failed" );
                ++failCnt;
            }
        }
        catch ( Exception ex )
        {
            LogFile.write( "executeSplitDesignArtifact failed" );
            ++failCnt;
            ex.printStackTrace();
        }
    }

    private static void executePerformanceTesting()
    {
        // Create an MDThread with 2 DesignArtifacts
        // MDOMotor with MechanicalMotor, AutomationMotor
        // Create a MultiDisciplinary project - Project_MD

        // Test following steps for 25, 50, 100, 150, 200 DEs
        // Create 2 CD - CDMechPerf, CDAutoPerf, assign Project_MD to these CDs.
        // Create 25 DEs using MechanicalMotor in CDMechPerf
        // Fire qryNotificationsByOriginDesign note time
        // Create 25 DEs using AutomationMotor in CDAutoPerf
        // Call updateMDONotification to update the 25 DEs of CDAutoPerf to be impacted response for 25 Notification in CDMechPerf
        // Fire qryNotificationsByOriginDesign note time - It returns the notification with response.

        try
        {
            MDOTestUtils m_mdoTestUtils = new MDOTestUtils();
            MDOManagement mdoObj = new MDOManagement();

            //Create an MDThread with 2 DesignArtifacts
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Creation of MDOMotor with MechanicalMotor, AutomationMotor" );

            List<ItemRevision> revsOfDAs1 = m_mdoTestUtils.createDesignArtifacts(1,"MechanicalMotor");
            List<ItemRevision> revsOfDAs2 = m_mdoTestUtils.createDesignArtifacts(1,"AutomationMotor");

            ItemRevision MechanicalMotor = revsOfDAs1.get(0);
            ItemRevision AutomationMotor = revsOfDAs2.get(0);


            List< ItemRevision > revsOfDAs = new ArrayList< ItemRevision >();
            revsOfDAs.add(MechanicalMotor);
            revsOfDAs.add(AutomationMotor);


            WorkspaceObject MDOMotor = m_mdoTestUtils.createMDOWithDARevList(revsOfDAs,"MDOMotor", "MDOMotor for Performance");
            if(MDOMotor != null)
            {
                LogFile.write( "Creation of MDOMotor with MechanicalMotor, AutomationMotor - Completed");
            }
            else
            {
                LogFile.write( "Creation of MDOMotor with MechanicalMotor, AutomationMotor failed");
                ++failCnt;
                return;

            }


         // Create a MultiDisciplinary project - project_MD

            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Creation of MultiDisciplinary project - Project_MD" );

            //Creating Project
            POM_object testProj1 = m_mdoTestUtils.createProject( "Project_MD", "Project created for Performance" );
            m_mdoTestUtils.setProjectAsMultiDisciplinary(testProj1);

            LogFile.write( "Creation of MultiDisciplinary project - Project_MD - Completed" );


            mdoObj.pauseBetweenUseCases( pauseStr );

            {

                LogFile.write( "------------------------------------------------------------------" );
                //Call the test to perform the test for 25 DES
                LogFile.write( "Performance testing for 25 DES - Start" );

                //Create 2 CD - CDMechPerfFor25DE, CDAutoPerfFor25DE, assign Project_MD to these CD.
                 LogFile.write( "Create 2 CD - CDMechPerfFor25DE, CDAutoPerfFor25DE, assign Project_MD to these CDs" );

                ModelObject cdMechPerfFor25DE = m_mdoTestUtils.createCD("CDMechPerfFor25DE");
                ModelObject cdAutoPerfFor25DE = m_mdoTestUtils.createCD("CDAutoPerfFor25DE");

                ModelObject[] collaborativeDesignsFor25DE = new ModelObject[2];
                collaborativeDesignsFor25DE[0] = cdMechPerfFor25DE;
                collaborativeDesignsFor25DE[1] = cdAutoPerfFor25DE;
                m_mdoTestUtils.assignCDsToProject(collaborativeDesignsFor25DE, testProj1);

                LogFile.write( "Create 2 CD - CDMechPerfFor25DE, CDAutoPerfFor25DE, assign Project_MD to these CDs - Completed" );

                m_mdoTestUtils.executePerformanceTestingForInputDECount( 25,  MechanicalMotor,   AutomationMotor,  cdMechPerfFor25DE,  cdAutoPerfFor25DE);

                LogFile.write( "Performance testing for 25 DES - Completed" );
                LogFile.write( "------------------------------------------------------------------" );

                mdoObj.pauseBetweenUseCases( pauseStr );

                LogFile.write( "------------------------------------------------------------------" );

            }

            //Call the test to perform the test for 50 DES
            {
                LogFile.write( "Performance testing for 50 DES - Start" );

                //Create 2 CD - CDMechPerfFor50DE, CDAutoPerfFor50DE, assign Project_MD to these CD.
                 LogFile.write( "Create 2 CD -  CDMechPerfFor50DE, CDAutoPerfFor50DE, assign Project_MD to these CDs" );

                ModelObject cdMechPerfFor50DE = m_mdoTestUtils.createCD("CDMechPerfFor50DE");
                ModelObject cdAutoPerfFor50DE = m_mdoTestUtils.createCD("CDAutoPerfFor50DE");

                ModelObject[] collaborativeDesignsFor50DE = new ModelObject[2];
                collaborativeDesignsFor50DE[0] = cdMechPerfFor50DE;
                collaborativeDesignsFor50DE[1] = cdMechPerfFor50DE;
                m_mdoTestUtils.assignCDsToProject(collaborativeDesignsFor50DE, testProj1);

                LogFile.write( "Create 2 CD -  CDMechPerfFor50DE, CDAutoPerfFor50DE,assign Project_MD to these CDs - Completed" );

                m_mdoTestUtils.executePerformanceTestingForInputDECount( 50,  MechanicalMotor,   AutomationMotor,  cdMechPerfFor50DE,  cdAutoPerfFor50DE);

                LogFile.write( "Performance testing for 50 DES - Completed" );
                LogFile.write( "------------------------------------------------------------------" );

            }
            //Call the test to perform the test for 100 DES
            {
                LogFile.write( "Performance testing for 100 DES - Start" );

                //Create 2 CD - CDMechPerfFor100DE, CDAutoPerfFor100DE, assign Project_MD to these CD.
                LogFile.write( "Create 2 CD - CDMechPerfFor100DE, CDAutoPerfFor100DE, assign Project_MD to these CDs" );

                ModelObject cdMechPerfFor100DE = m_mdoTestUtils.createCD("CDMechPerfFor100DE");
                ModelObject cdAutoPerfFor100DE = m_mdoTestUtils.createCD("CDAutoPerfFor100DE");

                ModelObject[] collaborativeDesignsFor100DE = new ModelObject[2];
                collaborativeDesignsFor100DE[0] = cdMechPerfFor100DE;
                collaborativeDesignsFor100DE[1] = cdAutoPerfFor100DE;
                m_mdoTestUtils.assignCDsToProject(collaborativeDesignsFor100DE, testProj1);

                LogFile.write( "Create 2 CD - CDMechPerfFor100DE, CDAutoPerfFor100DE, assign Project_MD to these CDs - Completed" );

                m_mdoTestUtils.executePerformanceTestingForInputDECount( 100,  MechanicalMotor,   AutomationMotor,  cdMechPerfFor100DE,  cdAutoPerfFor100DE);

                LogFile.write( "Performance testing for 100 DES - Completed" );
                LogFile.write( "------------------------------------------------------------------" );
            }

           //Call the test to perform the test for 150 DES
            {
                LogFile.write( "Performance testing for 150 DES - Start" );

                //Create 2 CD - CDMechPerfFor150DE, CDAutoPerfFor150DE, assign Project_MD to these CD.
                LogFile.write( "Create 2 CD - CDMechPerfFor150DE, CDAutoPerfFor150DE, assign Project_MD to these CDs" );

                ModelObject cdMechPerfFor150DE = m_mdoTestUtils.createCD("CDMechPerfFor150DE");
                ModelObject cdAutoPerfFor150DE = m_mdoTestUtils.createCD("CDAutoPerfFor150DE");

                ModelObject[] collaborativeDesignsFor150DE = new ModelObject[2];
                collaborativeDesignsFor150DE[0] = cdMechPerfFor150DE;
                collaborativeDesignsFor150DE[1] = cdAutoPerfFor150DE;
                m_mdoTestUtils.assignCDsToProject(collaborativeDesignsFor150DE, testProj1);

                LogFile.write( "Create 2 CD - CDMechPerfFor150DE, CDAutoPerfFor150DE, assign Project_MD to these CDs - Completed" );

                m_mdoTestUtils.executePerformanceTestingForInputDECount( 150,  MechanicalMotor,   AutomationMotor,  cdMechPerfFor150DE,  cdAutoPerfFor150DE);

                LogFile.write( "Performance testing for 150 DES - Completed" );
                LogFile.write( "------------------------------------------------------------------" );
            }

            //Call the test to perform the test for 200 DES
            {
                LogFile.write( "Performance testing for 200 DES - Start" );

                //Create 2 CD - CDMechPerfFor200DE, CDAutoPerfFor200DE, assign Project_MD to these CD.
                LogFile.write( "Create 2 CD - CDMechPerfFor200DE, CDAutoPerfFor200DE, assign Project_MD to these CDs" );

                ModelObject CDMechPerfFor200DE = m_mdoTestUtils.createCD("CDMechPerfFor200DE");
                ModelObject CDAutoPerfFor200DE = m_mdoTestUtils.createCD("CDAutoPerfFor200DE");

                ModelObject[] collaborativeDesignsFor200DE = new ModelObject[2];
                collaborativeDesignsFor200DE[0] = CDMechPerfFor200DE;
                collaborativeDesignsFor200DE[1] = CDAutoPerfFor200DE;
                m_mdoTestUtils.assignCDsToProject(collaborativeDesignsFor200DE, testProj1);

                LogFile.write( "Create 2 CD - CDMechPerfFor150DE, CDAutoPerfFor200DE, assign Project_MD to these CDs - Completed" );

                m_mdoTestUtils.executePerformanceTestingForInputDECount( 200,  MechanicalMotor,   AutomationMotor,  CDMechPerfFor200DE,  CDAutoPerfFor200DE);

                LogFile.write( "Performance testing for 200 DES - Completed" );
                LogFile.write( "------------------------------------------------------------------" );
            }
            return;


        }
        catch(Exception ex)
        {
              ex.printStackTrace();
        }
    }

    private static void executeDomainTestCases()
    {
        // Create Item1, Item2, Item3,  Item6, Item7 of Item Type.
        // Create  Item4 (PSConnection), Item5 (PSSignal)

        // Create an MDO1 with Revision of Item1, Item2, Item3.
        // Create an MDO2 with Revision of Item1, Item4, Item5, Item6, Item7.

        try
        {
            MDOManagement mdoObj = new MDOManagement();
            CreateItemsResponse response;
            MDOTestUtils m_mdoTestUtils = new MDOTestUtils();

            LogFile.write( "==================================================================" );
            LogFile.write( "Executing test case 23 - Get Domain of design artifact related test cases" );

            response = m_mdoTestUtils.createDesignArtifactsWithType("DomainTest_DA1", "Item", "Description for DomainTest_DA1");
            ItemRevision DA1Rev = response.output[0].itemRev;

            response = m_mdoTestUtils.createDesignArtifactsWithType("DomainTest_DA2", "Item", "Description for DomainTest_DA2");
            Item DA2 = response.output[0].item;

            response = m_mdoTestUtils.createDesignArtifactsWithType("DomainTest_DA3", "Item", "Description for DomainTest_DA3");
            ItemRevision DA3Rev = response.output[0].itemRev;

            response = m_mdoTestUtils.createDesignArtifactsWithType("DomainTest_DA4", "PSConnection", "Description for DomainTest_DA4");
            ItemRevision DA4Rev = response.output[0].itemRev;

            response = m_mdoTestUtils.createDesignArtifactsWithType("DomainTest_DA5", "PSSignal", "Description for DomainTest_DA5");
            ItemRevision DA5Rev = response.output[0].itemRev;

            response = m_mdoTestUtils.createDesignArtifactsWithType("DomainTest_DA6", "Item", "Description for DomainTest_DA6");
            ItemRevision DA6Rev = response.output[0].itemRev;

            response = m_mdoTestUtils.createDesignArtifactsWithType("DomainTest_DA7", "Item", "Description for DomainTest_DA7");
            ItemRevision DA7Rev = response.output[0].itemRev;

            mdoObj.pauseBetweenUseCases( pauseStr );
            LogFile.write( "------------------------------------------------------------------" );

            LogFile.write( "Executing test case 23.1 - Get Domain for design artifact of type PSConnectionRevision - Start" );

            GetDomainInput[] getDomainInputArray = new GetDomainInput[1];
                        GetDomainInput input1 = new GetDomainInput();
            input1.clientId = "ForObjectGetDomain";
            input1.inputDesignArtifact = (WorkspaceObject) DA4Rev;
            getDomainInputArray[0] = input1;
            com.teamcenter.services.strong.core._2015_07.DataManagement.DomainOfObjectOrTypeResponse  resp=  m_mdoTestUtils.getDomainOfObjectOrType(getDomainInputArray);

            if(resp.output.length > 0)
            {
                com.teamcenter.soa.client.model.Property[] objNameProp;
                objNameProp = MDOTestUtils.getObjectProperties(DA4Rev, new String[] {"object_name"});
                LogFile.write( "Input Client Id: " + resp.output[0].clientId + ", Object Name : " + objNameProp[0].getDisplayableValue() +", uid : " + DA4Rev.getUid()+ " Domain : "+ resp.output[0].domain + " is fetched." );
                LogFile.write( "Get Domain for design artifact type PSConnectionRevision - Completed" );
            }
            mdoObj.pauseBetweenUseCases( pauseStr );
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing test case 23.2 - Get Domain for type PSSignalRevision - Start" );

            GetDomainInput input2 = new GetDomainInput();
            input2.clientId = "ForTypeGetDomain";
            input2.typName = "PSSignalRevision";
            getDomainInputArray[0] = input2;

            com.teamcenter.services.strong.core._2015_07.DataManagement.DomainOfObjectOrTypeResponse  resp1=  m_mdoTestUtils.getDomainOfObjectOrType(getDomainInputArray);
            if(resp1.output.length > 0)
            {

                LogFile.write( "Input Client Id: " + resp1.output[0].clientId + ", Type Name : " + input2.typName+", Domain : "+ resp1.output[0].domain + " is fetched." );
                LogFile.write( "Get Domain for  type PSSignalRevision - Completed" );
            }

            mdoObj.pauseBetweenUseCases( pauseStr );
            LogFile.write( "------------------------------------------------------------------" );

            //Create an MDO1  with Revision of Item1, Item2, Item3.
            LogFile.write( "Executing test case 23.3 - Creation of MDO1 with ItemRev1, Item2, ItemRev3 for Domain test" );

            List< WorkspaceObject > designArtifacts = new ArrayList< WorkspaceObject >();
            designArtifacts.add(DA1Rev);
            designArtifacts.add(DA2);
            designArtifacts.add(DA3Rev);


            WorkspaceObject MDO1 = m_mdoTestUtils.createMDOWithDAList(designArtifacts,"MDO1", "MDO1 for Domain test");
            if(MDO1 != null)
            {
                LogFile.write( "Creation of MDO1 with ItemRev1, Item2, ItemRev3 for Domain test - Completed");
            }
            else
            {
                LogFile.write( "Creation of MDO1 with ItemRev1, Item2, ItemRev3 for Domain test for Domain test failed");
                ++failCnt;
            }
            mdoObj.pauseBetweenUseCases( pauseStr );
            LogFile.write( "------------------------------------------------------------------" );
            //Create an MDO2  with Revision of Item1, Item4, Item5,Item6, Item7.

            LogFile.write( "Executing test case 23.4 - Creation of MDO2 with Revision of Item1, Item4, Item5,Item6, Item7 for Domain test" );

            List< ItemRevision > revsOfDAs2 = new ArrayList< ItemRevision >();
            revsOfDAs2.add(DA1Rev);
            revsOfDAs2.add(DA4Rev);
            revsOfDAs2.add(DA5Rev);
            revsOfDAs2.add(DA6Rev);
            revsOfDAs2.add(DA7Rev);

            WorkspaceObject MDO2 = m_mdoTestUtils.createMDOWithDARevList(revsOfDAs2,"MDO2", "MDO2 for Domain test");
            if(MDO2 != null)
            {
                LogFile.write( "Creation of MDO2 with Revision of Item1, Item4, Item5,Item6, Item7 for Domain test - Completed");
            }
            else
            {
                LogFile.write( "Creation of MDO2 with Revision of Item1, Item4, Item5,Item6, Item7 for Domain test failed");
                ++failCnt;
            }
            mdoObj.pauseBetweenUseCases( pauseStr );
            LogFile.write( "------------------------------------------------------------------" );

            // Search for MDThread using searchMDO2 with MDThread name as MDO2
            LogFile.write( "Executing test case 23.5 - Search for MDThread using searchMDO2 with MDThread name as MDO2" );
            Map<String, String[]> mdoPropertiesCriteria = new HashMap<String, String[]>();
            String propValues[] =  {"MDO2"};
            mdoPropertiesCriteria.put("object_name", propValues);
            SearchInput2[] inputs = new SearchInput2[1];
            inputs[0] = new SearchInput2();
            inputs[0].clientId = "Search";
            inputs[0].mdoCriteria = mdoPropertiesCriteria;
            com.teamcenter.soa.client.model.Property[] objNameProp;
            List<WorkspaceObject> searchResults = m_mdoTestUtils.executeMDOSearch2(inputs);
            if ( searchResults.size() > 0 )
            {
                LogFile.write("Search for MDThread using searchMDO2 with MDThread name as MDO2 succeeded");
                for(int inx = 0; inx < searchResults.size(); inx++ )
                {
                    objNameProp = MDOTestUtils.getObjectProperties(searchResults.get(inx), new String[] {"object_name"});
                    LogFile.write( "MDThread named: " + objNameProp[0].getDisplayableValue() + " uid: " + searchResults.get(inx).getUid() + "  is fetched." );
                }
            }
            else
            {
                LogFile.write("Search for MDThread using searchMDO2 with MDThread name as MDO2 failed");
                ++failCnt;
            }
            mdoObj.pauseBetweenUseCases( pauseStr );
            LogFile.write( "------------------------------------------------------------------" );

            // Search for MDThread using searchMDO2 with Item4Revision as input design artifact and domain as Mechanical
            LogFile.write( "Executing test case 23.6 - Search for MDThread using searchMDO2 with Item4Revision as input design artifact and domain as Mechanical" );

            DesignArtifactInputsForSearch  DAInputsForSrch2 = new DesignArtifactInputsForSearch();
            WorkspaceObject[] DAList2 = new WorkspaceObject[1];
            DAList2[0] = DA4Rev;
            DAInputsForSrch2.designArtifactsObjects = DAList2;

            FilterForSearch filterCritera2 = new FilterForSearch();
            filterCritera2.domain = "Mechanical";

            SearchInput2[] inputs2 = new SearchInput2[1];
            inputs2[0] = new SearchInput2();
            inputs2[0].clientId = "Search";
            inputs2[0].filterCriteria = filterCritera2;
            inputs2[0].designArtifactInputs = DAInputsForSrch2;
            com.teamcenter.soa.client.model.Property[] objNameProp2;
            List<WorkspaceObject> searchResults2 = m_mdoTestUtils.executeMDOSearch2(inputs2);
            if ( searchResults2.size() == 0 )
            {
                LogFile.write("Search for MDThread using searchMDO2 with Item4Revision as input design artifact and domain as Mechanical succeeded");
            }
            else
            {
                LogFile.write("Search for MDThread using searchMDO2 with Item4Revision as input design artifact and domain as Mechanical failed");
                ++failCnt;
                for(int inx = 0; inx < searchResults2.size(); inx++ )
                {
                    objNameProp2 = MDOTestUtils.getObjectProperties(searchResults2.get(inx), new String[] {"object_name"});
                    LogFile.write( "MDThread named: " + objNameProp2[0].getDisplayableValue() + " uid: " + searchResults2.get(inx).getUid() + "  is fetched." );
                }
            }
            mdoObj.pauseBetweenUseCases( pauseStr );
            LogFile.write( "------------------------------------------------------------------" );

            // Search for MDThread using searchMDO2 with Item4Revision as input design artifact and domain as Electrical
            LogFile.write( "Executing test case 23.7 - Search for MDThread using searchMDO2 with Item4Revision as input design artifact and domain as Electrical" );

            DesignArtifactInputsForSearch  DAInputsForSrch3 = new DesignArtifactInputsForSearch();
            WorkspaceObject[] DAList3 = new WorkspaceObject[1];
            DAList3[0] = DA4Rev;
            DAInputsForSrch3.designArtifactsObjects = DAList3;

            FilterForSearch filterCritera3 = new FilterForSearch();
            filterCritera3.domain = "Electrical";

            SearchInput2[] inputs3 = new SearchInput2[1];
            inputs3[0] = new SearchInput2();
            inputs3[0].clientId = "Search";
            inputs3[0].filterCriteria = filterCritera3;
            inputs3[0].designArtifactInputs = DAInputsForSrch3;
            List<WorkspaceObject> searchResults3 = m_mdoTestUtils.executeMDOSearch2(inputs3);
            if ( searchResults3.size() == 0 )
            {
                LogFile.write("Search for MDThread using searchMDO2 with Item4Revision as input design artifact and domain as Electrical succeeded");
            }
            else
            {
                LogFile.write("Search for MDThread using searchMDO2 with Item4Revision as input design artifact and domain as Electrical failed");
                ++failCnt;
            }
            mdoObj.pauseBetweenUseCases( pauseStr );
            LogFile.write( "------------------------------------------------------------------" );

            // Query for create notification using qryNotificationsByOriginDesign with performCreateNotificationQueryAlso = true and filterByDomain = "Automation"
            LogFile.write( "Executing test case 23.8 - Query for create notification using qryNotificationsByOriginDesign - Domain Automation" );
            //Creating Project
            POM_object context2 = m_mdoTestUtils.createProject( "domainProj2", "DomainProject 2" );
            m_mdoTestUtils.setProjectAsMultiDisciplinary(context2);
            // Create CDAuto
            ModelObject CDdomain1 = m_mdoTestUtils.createCD("CDDomain1");

            //Add CDdomain1 to proj2
            ModelObject[] collaborativeDesigns = new ModelObject[1];
            collaborativeDesigns[0] = CDdomain1;
            m_mdoTestUtils.assignCDsToProject( collaborativeDesigns, context2 );

            // Create reuse DE DOMDA1DE0
            m_mdoTestUtils.createReuseDEWithCD( DA1Rev, "DOMDA1DE0", CDdomain1 );
            m_mdoTestUtils.sleepForNotificationCreation();
            // Query for all notification information
            NotificationQueryInput[] mdoNotifInputs = new NotificationQueryInput[1];
            mdoNotifInputs[0] = new NotificationQueryInput();
            mdoNotifInputs[0].clientId = "querydomain";
            mdoNotifInputs[0].performCreateNotificationQueryAlso = true;
            mdoNotifInputs[0].filterByDomain = "Automation";
            QueryNotificationResponse notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp);
            LogFile.write("Query for create notification using qryNotificationsByOriginDesign - Domain Automation - Completed");
            mdoObj.pauseBetweenUseCases( pauseStr );
            LogFile.write( "------------------------------------------------------------------" );

            // Query for create notification using qryNotificationsByImpactedDesign with performCreateNotificationQueryAlso = true and filterByDomain = "Automation"
            LogFile.write( "Executing test case 23.9 - Query for create notification using qryNotificationsByImpactedDesign - Domain Automation" );

            QueryNotificationResponse notifResp2 = m_mdoTestUtils.queryForMDONotifByImpactedDesign(mdoNotifInputs);
            m_mdoTestUtils.printNotifQuery(notifResp2);
            LogFile.write("Query for create notification using qryNotificationsByImpactedDesign - Domain Automation - Completed");
            mdoObj.pauseBetweenUseCases( pauseStr );
            LogFile.write( "------------------------------------------------------------------" );

            // Query for create notification using qryNotificationsByOriginDesign with performCreateNotificationQueryAlso = true and filterByDomain = "Electrical"
            LogFile.write( "Executing test case 23.10 - Query for create notification using qryNotificationsByOriginDesign - Domain Electrical" );

            // Query for all notification information
            NotificationQueryInput[] mdoNotifInputs2 = new NotificationQueryInput[1];
            mdoNotifInputs2[0] = new NotificationQueryInput();
            mdoNotifInputs2[0].clientId = "querydomain";
            mdoNotifInputs2[0].performCreateNotificationQueryAlso = true;
            mdoNotifInputs2[0].filterByDomain = "Electrical";
            notifResp = m_mdoTestUtils.queryForMDONotifByOriginatingDesign(mdoNotifInputs2);
            m_mdoTestUtils.printNotifQuery(notifResp);
            LogFile.write("Query for create notification using qryNotificationsByOriginDesign - Domain Electrical - Completed");
            mdoObj.pauseBetweenUseCases( pauseStr );
            LogFile.write( "------------------------------------------------------------------" );

            // Query for create notification using qryNotificationsByImpactedDesign with performCreateNotificationQueryAlso = true and filterByDomain = "Electrical"
            LogFile.write( "Executing test case 23.11 - Query for create notification using qryNotificationsByImpactedDesign - Domain Electrical" );

            notifResp2 = m_mdoTestUtils.queryForMDONotifByImpactedDesign(mdoNotifInputs2);
            m_mdoTestUtils.printNotifQuery(notifResp2);
            LogFile.write("Query for create notification using qryNotificationsByImpactedDesign - Domain Electrical - Completed");
            mdoObj.pauseBetweenUseCases( pauseStr );
            LogFile.write( "------------------------------------------------------------------" );

            LogFile.write( "Executing test case 23.12 - Find MDThread from design instances with domain" );
            ModelObject cdtest1 = m_mdoTestUtils.createCD("TestCDdomain1");
            ModelObject de4 = m_mdoTestUtils.createReuseDEWithCD(DA4Rev,"ReuseDE4",cdtest1);
            if( de4 != null)
            {
                LogFile.write( "Design Element uid: " + de4.getUid() + " is used for search for MDThread" );
            }
            List<DesignInstancesData> didList2 = new ArrayList<DesignInstancesData>();
            DesignInstancesData did2[] = new DesignInstancesData[1];
            did2[0] = new DesignInstancesData();
            did2[0].designInstance = (POM_object) de4;
            did2[0].isPreciseLink = false;
            didList2.add(did2[0]);
            MDOSearchOutput2 mdoInfo2 = m_mdoTestUtils.searchForMDO2(didList2, "Automation");
            if (  mdoInfo2.mdoOutput == null|| mdoInfo2.mdoOutput.length == 0)
            {
                LogFile.write("No MDOs found");
            }
            else if (mdoInfo2.mdoOutput[0].associatedDesignArtifact.length != 2 )
            {
                LogFile.write("searchForMDO2 failed, wrong number of associated design artifacts");
                ++failCnt;
            }
            else
            {
                LogFile.write("searchForMDO2 succeeded");
                LogFile.write( mdoInfo2.mdoOutput.length + " MDThread(s) are fetched." );
                for(int inx=0 ; inx < mdoInfo2.mdoOutput.length ; inx++)
                    LogFile.write( "MDThread Name: " + (mdoInfo2.mdoOutput[inx].mdoObject).get_object_string() + " uid: " + ((ModelObject)(mdoInfo2.mdoOutput[inx].mdoObject)).getUid() );
            }
            LogFile.write( "Completed Usecase 23.12 - Find MDThread from design instances with domain" );
            // Pause for user input ( Enter )
            mdoObj.pauseBetweenUseCases( pauseStr );

            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "Executing test case 23.13 - Find MDThread from design instances with domain" );
            if( de4 != null)
            {
                LogFile.write( "Design Element uid: " + de4.getUid() + " is used for search for MDThread" );
            }

            MDOSearchOutput2 mdoInfo3 = m_mdoTestUtils.searchForMDO2(didList2, "Electrical");
            if (  mdoInfo3.mdoOutput != null && mdoInfo3.mdoOutput.length > 0)
            {
                LogFile.write("searchForMDO2 failed, wrong number of associated design artifacts");
                ++failCnt;
            }
            else
            {
                LogFile.write("searchForMDO2 succeeded");
                LogFile.write( "Zero MDThread(s) are fetched for this domain." );
            }
            LogFile.write( "Completed Usecase 23.13 - Find MDThread from design instances with domain" );
            // Pause for user input ( Enter )
            mdoObj.pauseBetweenUseCases( pauseStr );
        }
        catch(Exception ex)
        {
              ex.printStackTrace();
        }
        LogFile.write( "Completed test case 23 - Get Domain of design artifact related test cases" );
        LogFile.write( "==================================================================" );
    }

    /**
     * Tests searching of MDO with Item And ItemRevision associated to it as Design Artifacts based on input Design Element
     *
     */

    private static void executeSearchForArtifactsUsingInstance3() throws Exception
    {
        MDOTestUtils m_mdoTestUtils = new MDOTestUtils();
        CreateItemsResponse response;
        try
        {
            //Creating 4 Design Artifacts, create 3 MDOs with  3, 2, 2 DAs respectively. Where DAs are Item or ItemRevision
            //MDO1 has DesignArtifact1, DesignArtifact2/A, DesignArtifact3/A
            //MDO2 has DesignArtifact1/A, DesignArtifact2
            //MDO3 has DesignArtifact2, DesignArtifact3/A, DesignArtifact4
            //Create 2 DE with one DesignArtifact1/A , DesignArtifact2/A
            //Create 1 SearchForDesignArtifactInput 2 DE objects  mentioned above

            // Creating Design Artifacts
            response = m_mdoTestUtils.createDesignArtifactsWithType("DesignArtifact1", "Item", " DesignArtifact1");
            ItemRevision DA1Rev = response.output[0].itemRev;
            Item DA1 = response.output[0].item;

            response = m_mdoTestUtils.createDesignArtifactsWithType("DesignArtifact2", "Item", " DesignArtifact2");
            ItemRevision DA2Rev = response.output[0].itemRev;
            Item DA2 = response.output[0].item;

            response = m_mdoTestUtils.createDesignArtifactsWithType("DesignArtifact3", "Item", " DesignArtifact3");
            ItemRevision DA3Rev = response.output[0].itemRev;


            response = m_mdoTestUtils.createDesignArtifactsWithType("DesignArtifact4", "Item", " DesignArtifact4");
            Item DA4 = response.output[0].item;


           // Create MDO1 . Has DesignArtifact1, DesignArtifact2/A, DesignArtifact3/A
            List<WorkspaceObject> revsOfDAsForMdo1 = new ArrayList<WorkspaceObject>();
            revsOfDAsForMdo1.add(DA1);
            revsOfDAsForMdo1.add(DA2Rev);
            revsOfDAsForMdo1.add(DA3Rev);
            m_mdoTestUtils.createMDOWithDAList(revsOfDAsForMdo1,"MDO1", "MDO1 for search test");


            // Create MDO2 . Has  DesignArtifact1/A, DesignArtifact2
            List<WorkspaceObject> revsOfDAsForMdo2 = new ArrayList<WorkspaceObject>();
            revsOfDAsForMdo2.add(DA1Rev);
            revsOfDAsForMdo2.add(DA2);
            m_mdoTestUtils.createMDOWithDAList(revsOfDAsForMdo2,"MDO2", "MDO2 for search test");

            // Create MDO3 . Has DesignArtifact2, DesignArtifact3/A, DesignArtifact4
            List<WorkspaceObject> revsOfDAsForMdo3 = new ArrayList<WorkspaceObject>();
            revsOfDAsForMdo3.add(DA2);
            revsOfDAsForMdo3.add(DA3Rev);
            revsOfDAsForMdo3.add(DA4);

            m_mdoTestUtils.createMDOWithDAList(revsOfDAsForMdo3,"MDO3", "MDO3 for search test");

            //Creating Collaborative design
            ModelObject cdObject = m_mdoTestUtils.createCD("CDTest");


            List<WorkspaceObject> dElem = new ArrayList<WorkspaceObject>();

            //Creating Design Elements
            dElem.add((WorkspaceObject)m_mdoTestUtils.createReuseDEWithCD( DA1Rev,"InstReuseDE_1",cdObject ));

            //Creating Design Elements
            dElem.add((WorkspaceObject)m_mdoTestUtils.createReuseDEWithCD( DA2Rev,"InstReuseDE_2",cdObject ));

            //Add domain relevancy information

            /// ----------------------------------------

            // Add DA1 Item, DA2 Rev, DA3 Rev , DA4 Item as relevant for Automation and irrelevant for Mechanical

            DomainRelevancyInput[] inputs1 = new DomainRelevancyInput[4];

            String[] addRelDom = new String[1];
            addRelDom[0] = "Automation";

            String[] addIrRelDom = new String[1];
            addIrRelDom[0] = "Mechanical";


            RelevancyInformation addInfo  = new RelevancyInformation();
            addInfo.relevantDomain = addRelDom;
            addInfo.irrelevantDomain = addIrRelDom;

            //Input 1

            inputs1[0] = new DomainRelevancyInput();
            inputs1[0].clientId = "AddRemoveDomainRelForInput1";
            inputs1[0].designArtifact = DA1;
            inputs1[0].addInformation = addInfo;

            //Input 2

            inputs1[1] = new DomainRelevancyInput();
            inputs1[1].clientId = "AddRemoveDomainRelForInput2";
            inputs1[1].designArtifact = DA2Rev;
            inputs1[1].addInformation = addInfo;

            //Input 3

            inputs1[2] = new DomainRelevancyInput();
            inputs1[2].clientId = "AddRemoveDomainRelForInput3";
            inputs1[2].designArtifact = DA3Rev;
            inputs1[2].addInformation = addInfo;

            //Input 4

            inputs1[3] = new DomainRelevancyInput();
            inputs1[3].clientId = "AddRemoveDomainRelForInput3";
            inputs1[3].designArtifact = DA4;
            inputs1[3].addInformation = addInfo;

            m_mdoTestUtils.updateDomainRelevancy(  inputs1 );



           //Search without filter for Domain Relevancy
            LogFile.write( "executeSearchForArtifactsUsingInstance3  without filter for Domain Relevancy - Start" );

            MDOSearchOutput3[]  result = m_mdoTestUtils.searchForMDOUsingDIs(dElem, "", false);


            if (result!=null && result.length > 0 )
            {
                com.teamcenter.soa.client.model.Property[] objNameProp2;
                LogFile.write("Search of MDO with Item And ItemRevision associated to it as Design Artifact succeeded");
                for(int inx = 0; inx < result.length; inx++ )
                {
                    MDOOutput3[] output = result[inx].mdoOutput;
                    for(int x = 0; x < output.length; x++ )
                    {
                        objNameProp2 = MDOTestUtils.getObjectProperties(output[x].mdoObject, new String[] {"object_name"});
                        LogFile.write( "MDThread named: " + objNameProp2[0].getDisplayableValue() + " uid: " + output[x].mdoObject.getUid() + " is fetched." );
                        LogFile.write("Associated Design Artifact Details:\n{");
                        DesignArtifactInfo2[] das = output[x].associatedDesignArtifact;
                        for(int y = 0; y < das.length; y++ )
                        {
                            objNameProp2 = MDOTestUtils.getObjectProperties(das[y].designArtifact, new String[] {"object_name"});
                            LogFile.write( "Name: " + objNameProp2[0].getDisplayableValue() + " uid: " + das[y].designArtifact.getUid() + ", Needs Validation status: "+das[y].needsValidation +", Is Validated status: "+ das[y].isValidated );
                            LogFile.write( " Relevant Domains:");
                            for(int z = 0; z < das[y].relevantDomains.length; z++ )
                            {
                                LogFile.write( "  " + das[y].relevantDomains[z] );

                            }

                            LogFile.write( " Irrelevant Domains:");
                            for(int z = 0; z < das[y].irrelevantDomains.length; z++ )
                            {
                                LogFile.write( "  " + das[y].irrelevantDomains[z]);

                            }
                        }
                        LogFile.write("}");

                    }
                }
            }

            LogFile.write( "executeSearchForArtifactsUsingInstance3  without filter for Domain Relevancy - End" );

            //Search with filter for Relevant Domain as Automation
            LogFile.write( "executeSearchForArtifactsUsingInstance3  with filter for Relevant Domain as Automation - Start" );

            result = m_mdoTestUtils.searchForMDOUsingDIs(dElem, "Automation", true);


            if (result!=null && result.length > 0 )
            {
                com.teamcenter.soa.client.model.Property[] objNameProp2;
                for(int inx = 0; inx < result.length; inx++ )
                {
                    MDOOutput3[] output = result[inx].mdoOutput;
                    for(int x = 0; x < output.length; x++ )
                    {
                        objNameProp2 = MDOTestUtils.getObjectProperties(output[x].mdoObject, new String[] {"object_name"});
                        LogFile.write( "MDThread named: " + objNameProp2[0].getDisplayableValue() + " uid: " + output[x].mdoObject.getUid() + " is fetched." );
                        LogFile.write("Associated Design Artifact Details:\n{");
                        DesignArtifactInfo2[] das = output[x].associatedDesignArtifact;
                        for(int y = 0; y < das.length; y++ )
                        {
                            objNameProp2 = MDOTestUtils.getObjectProperties(das[y].designArtifact, new String[] {"object_name"});
                            LogFile.write( "Name: " + objNameProp2[0].getDisplayableValue() + " uid: " + das[y].designArtifact.getUid() + ", Needs Validation status: "+das[y].needsValidation +", Is Validated status: "+ das[y].isValidated );
                        }
                        LogFile.write("}");

                    }
                }
            }

            LogFile.write( "executeSearchForArtifactsUsingInstance3  with filter for Relevant Domain as Automation - End" );

            //Search with filter for Irrelevant Domain as Mechanical
            LogFile.write( "executeSearchForArtifactsUsingInstance3  with filter for Irrelevant Domain  as Mechanical - Start " );

            result = m_mdoTestUtils.searchForMDOUsingDIs(dElem, "Mechanical", false);


            if (result!=null && result.length > 0 )
            {
                com.teamcenter.soa.client.model.Property[] objNameProp2;
                LogFile.write("Search of MDO with Item And ItemRevision associated to it as Design Artifact succeeded");
                for(int inx = 0; inx < result.length; inx++ )
                {
                    MDOOutput3[] output = result[inx].mdoOutput;
                    for(int x = 0; x < output.length; x++ )
                    {
                        objNameProp2 = MDOTestUtils.getObjectProperties(output[x].mdoObject, new String[] {"object_name"});
                        LogFile.write( "MDThread named: " + objNameProp2[0].getDisplayableValue() + " uid: " + output[x].mdoObject.getUid() + "  is fetched." );
                        LogFile.write("Associated Design Artifact Details:\n{");
                        DesignArtifactInfo2[] das = output[x].associatedDesignArtifact;
                        for(int y = 0; y < das.length; y++ )
                        {
                            objNameProp2 = MDOTestUtils.getObjectProperties(das[y].designArtifact, new String[] {"object_name"});
                            LogFile.write( "Name: " + objNameProp2[0].getDisplayableValue() + " uid: " + das[y].designArtifact.getUid() + ", Needs Validation status: "+das[y].needsValidation +", Is Validated status: "+ das[y].isValidated );
                        }
                        LogFile.write("}");

                    }
                }
            }

            LogFile.write( "executeSearchForArtifactsUsingInstance3  with filter for Irrelevant Domain  as Mechanical - End" );

            //Search with filter for Relevant Domain as Mechanical
            LogFile.write( "executeSearchForArtifactsUsingInstance3  with filter for Relevant Domain as Mechanical - Start" );

            result = m_mdoTestUtils.searchForMDOUsingDIs(dElem, "Mechanical", true);


            if (result!=null && result.length > 0 )
            {
                com.teamcenter.soa.client.model.Property[] objNameProp2;
                LogFile.write("Search of MDO with Item And ItemRevision associated to it as Design Artifact succeeded");
                for(int inx = 0; inx < result.length; inx++ )
                {
                    MDOOutput3[] output = result[inx].mdoOutput;
                    for(int x = 0; x < output.length; x++ )
                    {
                        objNameProp2 = MDOTestUtils.getObjectProperties(output[x].mdoObject, new String[] {"object_name"});
                        LogFile.write( "MDThread named: " + objNameProp2[0].getDisplayableValue() + " uid: " + output[x].mdoObject.getUid() + " is fetched." );
                        LogFile.write("Associated Design Artifact Details:\n{");
                        DesignArtifactInfo2[] das = output[x].associatedDesignArtifact;
                        for(int y = 0; y < das.length; y++ )
                        {
                            objNameProp2 = MDOTestUtils.getObjectProperties(das[y].designArtifact, new String[] {"object_name"});
                            LogFile.write( "Name: " + objNameProp2[0].getDisplayableValue() + " uid: " + das[y].designArtifact.getUid() + ", Needs Validation status: "+das[y].needsValidation +", Is Validated status: "+ das[y].isValidated );
                        }
                        LogFile.write("}");

                    }
                }
            }

            LogFile.write( "executeSearchForArtifactsUsingInstance3  with filter for Relevant Domain as Mechanical - End" );

            //Search with filter for Irrelevant Domain as Automation
            LogFile.write( "executeSearchForArtifactsUsingInstance3  with filter for Irrelevant Domain  as Automation - Start" );

            result = m_mdoTestUtils.searchForMDOUsingDIs(dElem, "Automation", false);


            if (result!=null && result.length > 0 )
            {
                com.teamcenter.soa.client.model.Property[] objNameProp2;
                LogFile.write("Search of MDO with Item And ItemRevision associated to it as Design Artifact succeeded");
                for(int inx = 0; inx < result.length; inx++ )
                {
                    MDOOutput3[] output = result[inx].mdoOutput;
                    for(int x = 0; x < output.length; x++ )
                    {
                        objNameProp2 = MDOTestUtils.getObjectProperties(output[x].mdoObject, new String[] {"object_name"});
                        LogFile.write( "MDThread named: " + objNameProp2[0].getDisplayableValue() + " uid: " + output[x].mdoObject.getUid() + "  is fetched." );
                        LogFile.write("Associated Design Artifact Details:\n{");
                        DesignArtifactInfo2[] das = output[x].associatedDesignArtifact;
                        for(int y = 0; y < das.length; y++ )
                        {
                            objNameProp2 = MDOTestUtils.getObjectProperties(das[y].designArtifact, new String[] {"object_name"});
                            LogFile.write( "Name: " + objNameProp2[0].getDisplayableValue() + " uid: " + das[y].designArtifact.getUid() + ", Needs Validation status: "+das[y].needsValidation +", Is Validated status: "+ das[y].isValidated );
                        }
                        LogFile.write("}");

                    }
                }
            }
            LogFile.write( "executeSearchForArtifactsUsingInstance3  with filter for Irrelevant Domain  as Automation - End" );
            LogFile.write( "executeSearchForArtifactsUsingInstance3 succeeded" );
        }
        catch ( Exception ex )
        {
            LogFile.write( "executeSearchForArtifactsUsingInstance3 failed" );
            ++failCnt;
            ex.printStackTrace();

        }
    }

    /**
     * Tests revising a Design Artifact precisely linked with an MDThread
     *
     */
    public static void executeReviseDesignArtifact()
    {
        MDOTestUtils m_mdoTestUtils = new MDOTestUtils();
        try
        {
            List<ItemRevision> revsOfDAs = m_mdoTestUtils.createDesignArtifacts(2,"RDesignArtifact");
            WorkspaceObject newMDT = m_mdoTestUtils.createMDOWithDARevList(revsOfDAs,"MDThread1_Revise", "MDT for revise");

            if ( newMDT == null )
            {
                LogFile.write("Error creating MDThread1_Revise");
            }

            //Add domain relevancy information

            /// ----------------------------------------
            // Add DA1 Rev as  relevant for Automation

            DomainRelevancyInput[] inputs1 = new DomainRelevancyInput[1];

            //Input 1 for DA1 Rev
            inputs1[0] = new DomainRelevancyInput();
            inputs1[0].clientId = "AddRemoveDomainRelForInput1";
            inputs1[0].designArtifact = revsOfDAs.get(0);

            String[] addRelDom1 = new String[1];
            addRelDom1[0] = "Automation";

            RelevancyInformation addInfo1 = new RelevancyInformation();
            addInfo1.relevantDomain = addRelDom1;

            inputs1[0].addInformation = addInfo1;

            // update domain relevancy on designs
            m_mdoTestUtils.updateDomainRelevancy( inputs1 );

            SearchInput[] inputs2 = new SearchInput[1];
            inputs2[0] = new SearchInput();
            inputs2[0].clientId = "SearchForDAs";
            inputs2[0].designArtifactInputs = new DesignArtifactInputsForSearch();
            inputs2[0].designArtifactInputs.designArtifactsObjects = new WorkspaceObject[2];
            inputs2[0].designArtifactInputs.designArtifactsObjects[0] = revsOfDAs.get(0);
            inputs2[0].designArtifactInputs.designArtifactsObjects[1] = revsOfDAs.get(1);
            List<WorkspaceObject> searchResults = m_mdoTestUtils.executeMDODASearch(inputs2);
            if ( searchResults.size() == 2 )
            {
                LogFile.write("Found 2 DAs");
            }
            else
            {
                LogFile.write("Did not find 2 DAs");
            }

            ItemRevision revisedDaRev1 = m_mdoTestUtils.reviseItemRev( revsOfDAs.get(0), "B" );
            LogFile.write("Revised " + revsOfDAs.get(0).get_object_string() + " and got " + revisedDaRev1.get_object_string() );

            ItemRevision revisedDaRev2 = m_mdoTestUtils.reviseItemRev( revsOfDAs.get(0), "C" );
            LogFile.write("Revised " + revsOfDAs.get(0).get_object_string() + " and got " + revisedDaRev2.get_object_string() );

            // check for new revision under current MDT (4 DAs)
            List<WorkspaceObject> searchResults2 = m_mdoTestUtils.executeMDODASearch(inputs2);
            if ( searchResults2.size() == 4 )
            {
                LogFile.write("Found 4 DAs");
                LogFile.write( "executeReviseDesignArtifact succeeded" );
            }
            else
            {
                LogFile.write("Did not find 4 DAs");
                LogFile.write( "executeReviseDesignArtifact failed" );
                ++failCnt;
            }

            LogFile.write( "------------------------------------------------------------------------" );
            LogFile.write( "Query for MDT Association with Needs Validation set as true using MDThread as input - start" );

            WorkspaceObject mdoObject =  newMDT;

            NeedsValidationLinkInput[] qryInputs = new NeedsValidationLinkInput[1];
            NeedsValidationLinkInput input1 = new NeedsValidationLinkInput();
            input1.clientId = "QueryNeedsValidation";
            input1.inputMDO = mdoObject;
            qryInputs[0] = input1;
            NeedsValidationLinkResponse qryResp = m_mdoTestUtils.queryNeedsValidation(qryInputs);
            m_mdoTestUtils.printQueryNeedsValidationResults (qryResp);

            LogFile.write( "Query for MDT Association with NeedsValidation set as true using MDThread as input - End" );

            LogFile.write( "------------------------------------------------------------------------" );

            LogFile.write( "Query for MDT Association with NeedsValidation set as true using Design Artifact as input - start" );

            NeedsValidationLinkInput[] qryInputs2 = new NeedsValidationLinkInput[1];
            NeedsValidationLinkInput input2 = new NeedsValidationLinkInput();
            input2.clientId = "QueryNeedsValidation";
            input2.inputDesignArtifact = revisedDaRev1;
            qryInputs2[0] = input2;

            NeedsValidationLinkResponse qryResp2 = m_mdoTestUtils.queryNeedsValidation(qryInputs2);
            m_mdoTestUtils.printQueryNeedsValidationResults (qryResp2);

            LogFile.write( "Query for MDT Association with NeedsValidation set as true using Design Artifact as input - End" );

            LogFile.write( "------------------------------------------------------------------------" );
            // check Automation relevancy for revised DA1 Rev
            GetDomainRelevancyInput[] inputs3 = new GetDomainRelevancyInput[1];
            inputs3[0] = new GetDomainRelevancyInput();
            inputs3[0].clientId = "GetDomainRelSingle";
            inputs3[0].inputObject = revisedDaRev1;
            inputs3[0].domain = "Automation";

            GetDomainRelevancyOfAnObjectResp response2 = m_mdoTestUtils.getDomainRelevancy( inputs3 );
            if ( response2.serviceData.sizeOfPartialErrors() == 0 &&
                 response2.output.length == 1 &&
                 response2.output[0].relevancyValue.equals( "relevant" ) &&
                 response2.output[0].isDesignArtifactBased
                 )
            {
                LogFile.write( "getDomainRelevancy relevant succeeded" );
            }
            else
            {
                LogFile.write( "getDomainRelevancy relevant failed" );
                ++failCnt;
            }


        }
        catch ( Exception ex )
        {
            LogFile.write( "executeReviseDesignArtifact failed" );
            ++failCnt;
            ex.printStackTrace();
        }
    }

    /**
     * Tests SaveAs on a Design Artifact
     *
     */
    public static void executeSaveAsDesignArtifact()
    {
        MDOTestUtils m_mdoTestUtils = new MDOTestUtils();
        try
        {
            List<ItemRevision> revsOfDAs = m_mdoTestUtils.createDesignArtifacts(2,"RDesignArtifact");
            com.teamcenter.soa.client.model.Property[] objNameProp = MDOTestUtils.getObjectProperties(revsOfDAs.get(0), new String[] {"item_id"});
            String da1Id = new String( objNameProp[0].getDisplayableValue());

            //Add domain relevancy information

            /// ----------------------------------------
            // Add DA1 Rev as  relevant for Automation

            DomainRelevancyInput[] inputs1 = new DomainRelevancyInput[1];

            //Input 1 for DA1 Rev
            inputs1[0] = new DomainRelevancyInput();
            inputs1[0].clientId = "AddRemoveDomainRelForInput1";
            inputs1[0].designArtifact = revsOfDAs.get(0);

            String[] addRelDom1 = new String[1];
            addRelDom1[0] = "Automation";

            RelevancyInformation addInfo1 = new RelevancyInformation();
            addInfo1.relevantDomain = addRelDom1;

            inputs1[0].addInformation = addInfo1;

            // update domain relevancy on designs
            m_mdoTestUtils.updateDomainRelevancy( inputs1 );

            // SaveAs the daRev1
            ItemRevision saveAsDaRev1 = m_mdoTestUtils.saveAsItemRev( revsOfDAs.get(0), da1Id+"SaveAs" );
            LogFile.write("SaveAs " + revsOfDAs.get(0).get_object_string() + " and got " + saveAsDaRev1.get_object_string() );

            // check Automation relevancy for SaveAs DA1 Rev
            GetDomainRelevancyInput[] inputs3 = new GetDomainRelevancyInput[1];
            inputs3[0] = new GetDomainRelevancyInput();
            inputs3[0].clientId = "GetDomainRelSingle";
            inputs3[0].inputObject = saveAsDaRev1;
            inputs3[0].domain = "Automation";

            GetDomainRelevancyOfAnObjectResp response2 = m_mdoTestUtils.getDomainRelevancy( inputs3 );
            if ( response2.serviceData.sizeOfPartialErrors() == 0 &&
                 response2.output.length == 1 &&
                 response2.output[0].relevancyValue.equals( "relevant" ) &&
                 response2.output[0].isDesignArtifactBased
                 )
            {
                LogFile.write( "getDomainRelevancy relevant succeeded" );
            }
            else
            {
                LogFile.write( "getDomainRelevancy relevant failed" );
                ++failCnt;
            }
        }
        catch ( Exception ex )
        {
            LogFile.write( "executeSaveAsDesignArtifact failed" );
            ++failCnt;
            ex.printStackTrace();
        }
    }

    /**
     * Tests revising a Library Element precisely linked with an MDThread
     *
     */
    public static void executeReviseLibraryElement()
    {
        /* LibraryElement currently cannot be revised
        MDOTestUtils mdoTestUtils = new MDOTestUtils();
        try
        {
            // create data for library creation.
            String[] disciplines = new String[2];
            disciplines[0] = "Mechanical";
            disciplines[1] = "Electrical";

            String[] allowedMemberTypes = new String[2];
            allowedMemberTypes[0]="Item";
            allowedMemberTypes[1]="ItemRevision";

            int numLibrariesToCreate = 1;
            Lbr0Library[] createdLibraries = mdoTestUtils.createLbr0Librararies( "ST01Library", disciplines, allowedMemberTypes, numLibrariesToCreate );

            // one Hierarchy and and one General Node
            CreateHierarchiesResponse hierarchyResponse;
            hierarchyResponse = mdoTestUtils.createHierarchy(createdLibraries[0], "ST01Hierarchy" );

            CreateNodesResponse nodeResponse;
            nodeResponse = mdoTestUtils.createLbrHierarchyGenralNode ( hierarchyResponse.output[0].hierarchy, null, "Lbr0_pub_Node2" );

            // Create 2 Items  daForLE1, daForLE2
            CreateItemsResponse response = mdoTestUtils.createDesignArtifactsWithType("daForLE1", "Item", "Description for daForLE1" );
            ItemRevision daForLE1Rev = response.output[0].itemRev;

            response = mdoTestUtils.createDesignArtifactsWithType("daForLE2", "Item", "Description for daForLE2" );
            ItemRevision daForLE2Rev = response.output[0].itemRev;

            BusinessObject[] itemList1 = new BusinessObject[1];
            itemList1[0] = daForLE1Rev;
            PublishObjectsResponse publishedResponse = mdoTestUtils.publishLibraryObjects( nodeResponse.output[0].node, itemList1 , "le1", 1 );
            WorkspaceObject le1 = (WorkspaceObject) publishedResponse.serviceData.getCreatedObject(0);

            BusinessObject[] itemList2 = new BusinessObject[1];
            itemList2[0] = daForLE2Rev;
            PublishObjectsResponse publishedResponse2 = mdoTestUtils.publishLibraryObjects( nodeResponse.output[0].node, itemList2 , "le2", 1 );
            WorkspaceObject le2 = (WorkspaceObject) publishedResponse2.serviceData.getCreatedObject(0);

            //   Create MDThread - MDOForLE using library element
            List<WorkspaceObject> leForMdo = new ArrayList<WorkspaceObject>();
            leForMdo.add(le1);
            leForMdo.add(le2);
            WorkspaceObject mdoForLE = mdoTestUtils.createMDOWithDAList(leForMdo, "mdoForLE", "mdoForLE for revise of library elements test");
            if ( mdoForLE == null )
            {
                LogFile.write("Error creating mdoForLE");
            }

            SearchInput[] inputs = new SearchInput[1];
            inputs[0] = new SearchInput();
            inputs[0].clientId = "SearchForDAs";
            inputs[0].designArtifactInputs = new DesignArtifactInputsForSearch();
            inputs[0].designArtifactInputs.designArtifactsObjects = new WorkspaceObject[2];
            inputs[0].designArtifactInputs.designArtifactsObjects[0] = le1;
            inputs[0].designArtifactInputs.designArtifactsObjects[1] = le2;
            mdoTestUtils.executeMDODASearch(inputs);

            //Release le1
            WorkspaceObject[] deObjs = new WorkspaceObject[1];
            deObjs[0] = (WorkspaceObject) le1;
            mdoTestUtils.applyReleaseStatus( deObjs, "TCM Released" );


            ModelObject revisedLe1 = mdoTestUtils.reviseDE( le1 );

            LogFile.write( "------------------------------------------------------------------------" );
            LogFile.write( "Query for MDT Association with Needs Validation set as true using MDThread as input - start" );

            WorkspaceObject mdoObject =  newMDT;

            NeedsValidationLinkInput[] qryInputs = new NeedsValidationLinkInput[1];
            NeedsValidationLinkInput input1 = new NeedsValidationLinkInput();
            input1.clientId = "QueryNeedsValidation";
            input1.inputMDO = mdoObject;
            qryInputs[0] = input1;
            NeedsValidationLinkResponse qryResp = mdoTestUtils.queryNeedsValidation(qryInputs);
            mdoTestUtils.printQueryNeedsValidationResults (qryResp);

            LogFile.write( "Query for MDT Association with NeedsValidation set as true using MDThread as input - End" );

            LogFile.write( "------------------------------------------------------------------------" );

            LogFile.write( "Query for MDT Association with NeedsValidation set as true using Design Artifact as input - start" );

            NeedsValidationLinkInput[] qryInputs2 = new NeedsValidationLinkInput[1];
            NeedsValidationLinkInput input2 = new NeedsValidationLinkInput();
            input2.clientId = "QueryNeedsValidation";
            input2.inputDesignArtifact = revisedDaRev1;
            qryInputs2[0] = input2;

            NeedsValidationLinkResponse qryResp2 = mdoTestUtils.queryNeedsValidation(qryInputs2);
            mdoTestUtils.printQueryNeedsValidationResults (qryResp2);

            LogFile.write( "Query for MDT Association with NeedsValidation set as true using Design Artifact as input - End" );

            LogFile.write( "------------------------------------------------------------------------" );


        }
        catch ( Exception ex )
        {
            LogFile.write( "executeReviseLibraryElement failed" );
            ++failCnt;
            ex.printStackTrace();
        }
        */
    }

    /**
     * Tests carryForwardMDT on unrelated Design Artifacts
     *
     */
    public static void executeCarryForwardMDTDiffDesignArtifacts()
    {
        MDOTestUtils m_mdoTestUtils = new MDOTestUtils();
        try
        {
            List<ItemRevision> revsOfDAs = m_mdoTestUtils.createDesignArtifacts(2,"RDesignArtifact");

            // Carry forward MDT
            SearchMDOResponse carryResp = m_mdoTestUtils.carryForwardMDT( revsOfDAs.get(0), revsOfDAs.get(1), true );
            if ( carryResp.serviceData.sizeOfPartialErrors() == 1 )
            {
                SearchMDOResponse carryResp2 = m_mdoTestUtils.carryForwardMDT( revsOfDAs.get(0), revsOfDAs.get(1), false );
                if ( carryResp2.serviceData.sizeOfPartialErrors() == 1 )
                {
                    LogFile.write("carryForwardMDT caught invalid item revisions");
                    LogFile.write( "executecarryForwardMDTDiffDesignArtifacts succeeded" );
                }
                else
                {
                    LogFile.write("carryForwardMDT did not catch invalid item revisions");
                    LogFile.write( "executecarryForwardMDTDiffDesignArtifacts failed-B" );
                    ++failCnt;
                }
            }
            else
            {
                LogFile.write("carryForwardMDT did not catch invalid item revisions");
                LogFile.write( "executecarryForwardMDTDiffDesignArtifacts failed-A" );
                ++failCnt;
            }
        }
        catch ( Exception ex )
        {
            LogFile.write( "executecarryForwardMDTDiffDesignArtifacts failed" );
            ++failCnt;
            ex.printStackTrace();
        }
    }

    /**
     * Tests revising a Design Element precisely linked with an MDInstance
     *
     */
    public static void executeReviseDesignElement()
    {
        MDOTestUtils mdoTestUtils = new MDOTestUtils();
        List< Item > itemList = new ArrayList< Item >();
        List< ItemRevision > revList = new ArrayList< ItemRevision >();
        List<ModelObject> dElem = new ArrayList <ModelObject>();

        try
        {
            //Creating Items for Design Elements
            mdoTestUtils.createItems( 2, "InstLinking", "Item", itemList, revList );
            if ( itemList.size() != 2 )
            {
                LogFile.write("Error creating 2 InstLinking items");
            }
            //Creating Project
            POM_object context = mdoTestUtils.createProject( "Project_ReviseDE", "mdoProjectDesc" );
            mdoTestUtils.setProjectAsMultiDisciplinary(context);
            //Creating Collaborative design
            ModelObject cdObject = mdoTestUtils.createCD("CDTest");
            ModelObject[] collaborativeDesigns = new ModelObject[1];
            collaborativeDesigns[0] = cdObject;
            mdoTestUtils.assignCDsToProject(collaborativeDesigns, context);
            //Creating Design Elements
            dElem.add( mdoTestUtils.createReuseDEWithCD( revList.get( 0 ), "InstReuseDE_30", cdObject ) );
            dElem.add( mdoTestUtils.createReuseDEWithCD( revList.get( 1 ), "InstReuseDE_31", cdObject ) );
            //Creating MDInstance
            POM_object[] newMDI = mdoTestUtils.linkDesignInstances( dElem, context, true );
            if ( newMDI.length != 1 )
            {
                LogFile.write("Error linkDesignInstances");
            }
            else
            {
                LogFile.write("The 2 DEs are linked with MDI " + newMDI[0].toString());
            }

            //Release reuse DE dElem[0]
            WorkspaceObject[] deObjs = new WorkspaceObject[1];
            deObjs[0] = (WorkspaceObject) dElem.get(0);
            mdoTestUtils.applyReleaseStatus( deObjs, "TCM Released" );

            //Add domain relevancy information

            /// ----------------------------------------
            // Add DA1 Rev as  relevant for Automation
            // With DE1   as irrelevant for Automation

            DomainRelevancyInput[] inputs1 = new DomainRelevancyInput[2];

            //Input 1 for DA1 Rev
            inputs1[0] = new DomainRelevancyInput();
            inputs1[0].clientId = "AddRemoveDomainRelForInput1";
            inputs1[0].designArtifact = revList.get(0);

            String[] addRelDom1 = new String[1];
            addRelDom1[0] = "Automation";

            RelevancyInformation addInfo1 = new RelevancyInformation();
            addInfo1.relevantDomain = addRelDom1;

            inputs1[0].addInformation = addInfo1;

            //Input 2 for DE1

            inputs1[1] = new DomainRelevancyInput();
            inputs1[1].clientId = "AddRemoveDomainRelForInput2";
            inputs1[1].designArtifact = (WorkspaceObject) dElem.get(0);

            String[] addIrrDom2 = new String[1];
            addIrrDom2[0] = "Automation";

            RelevancyInformation addInfo2 = new RelevancyInformation();
            addInfo2.irrelevantDomain = addIrrDom2;

            inputs1[1].addInformation = addInfo2;

            // update domain relevancy on designs
            mdoTestUtils.updateDomainRelevancy( inputs1 );

            // Revise released reuse DE dElem[0]
            POM_object revisedDElem1 = (POM_object)mdoTestUtils.reviseDE(dElem.get(0));
            LogFile.write("New revision " + revisedDElem1.get_object_string() );

            //Release reuse DE dElem[1]
            WorkspaceObject[] deObjs2 = new WorkspaceObject[1];
            deObjs2[0] = (WorkspaceObject) dElem.get(1);
            mdoTestUtils.applyReleaseStatus( deObjs2, "TCM Released" );

            // Revise released reuse DE dElem[1]
            POM_object revisedDElem2 = (POM_object)mdoTestUtils.reviseDE(dElem.get(1));
            LogFile.write("New revision " + revisedDElem2.get_object_string() );

            // check Automation relevancy for revised DE1
            GetDomainRelevancyInput[] inputs = new GetDomainRelevancyInput[1];
            inputs[0] = new GetDomainRelevancyInput();
            inputs[0].clientId = "GetDomainRelSingle";
            inputs[0].inputObject = (WorkspaceObject) revisedDElem1;
            inputs[0].domain = "Automation";

            GetDomainRelevancyOfAnObjectResp response2 = mdoTestUtils.getDomainRelevancy( inputs );
            if ( response2.serviceData.sizeOfPartialErrors() == 0 &&
                 response2.output.length == 1 &&
                 response2.output[0].relevancyValue.equals( "irrelevant" ) &&
                 response2.output[0].isDesignArtifactBased == false
                 )
            {
                LogFile.write( "getDomainRelevancy irrelevant succeeded" );
            }
            else
            {
                LogFile.write( "getDomainRelevancy irrelevant failed" );
                ++failCnt;
            }

            List<DesignInstancesData> didList2 = new ArrayList<DesignInstancesData>();
            DesignInstancesData did2[] = new DesignInstancesData[1];
            did2[0] = new DesignInstancesData();
            did2[0].designInstance = (POM_object) dElem.get(0);
            did2[0].isPreciseLink = true;
            didList2.add(did2[0]);
            SearchForLinkedInstances2Response resp1 = mdoTestUtils.searchForLinkedDesignInstances2(didList2);
            mdoTestUtils.printSearchForLinkedInstances2Response(resp1);

            if (resp1!=null && resp1.output.length > 0 && resp1.output[0].linkedInstances.length > 0 )
            {
                ImpactedDesignInstanceInfo2[] diInfos = resp1.output[0].linkedInstances[0].instancesInfo;
                boolean revisionIsFound = false;
                if(diInfos.length > 0)
                {
                     for(int inx=0 ; inx < diInfos.length ; inx++)
                     {
                        if ( diInfos[inx].designInstance == revisedDElem1 )
                        {
                            revisionIsFound  = true;
                        }
                    }
                }
                if (revisionIsFound)
                {
                    LogFile.write("Revised Design Element is found");
                    LogFile.write( "executeReviseDesignElement succeeded" );
                }
                else
                {
                    LogFile.write("Revised Design Element is not found");
                    LogFile.write( "executeReviseDesignElement failed-A" );
                    ++failCnt;
                }
            }
            else
            {
                LogFile.write("searchForLinkedDesignInstances failed");
                ++failCnt;
            }

            LogFile.write( "------------------------------------------------------------------------" );

            LogFile.write( "Query for MDI Association with NeedsValidation set as true using Design Instance as input - start" );

            NeedsValidationLinkInput[] qryInputs = new NeedsValidationLinkInput[1];
            NeedsValidationLinkInput input1 = new NeedsValidationLinkInput();
            input1.clientId = "QueryNeedsValidation";
            input1.inputDesignInstance = revisedDElem1;
            qryInputs[0] = input1;

            NeedsValidationLinkResponse qryResp = mdoTestUtils.queryNeedsValidation(qryInputs);
            mdoTestUtils.printQueryNeedsValidationResults (qryResp);


            LogFile.write( "Query for MDI Association with NeedsValidation set as true using Design Instance as input - end" );

            LogFile.write( "------------------------------------------------------------------------" );

        }
        catch ( Exception ex )
        {
            LogFile.write( "executeReviseDesignElement failed" );
            ++failCnt;
            ex.printStackTrace();
        }
    }

    /**
     * Tests SaveAs on a Design Element precisely linked with an MDInstance
     *
     */
    public static void executeSaveAsDesignElement()
    {
        MDOTestUtils mdoTestUtils = new MDOTestUtils();
        List< Item > itemList = new ArrayList< Item >();
        List< ItemRevision > revList = new ArrayList< ItemRevision >();
        List<ModelObject> dElem = new ArrayList <ModelObject>();

        try
        {
            //Creating Items for Design Elements
            mdoTestUtils.createItems( 2, "InstLinking", "Item", itemList, revList );
            if ( itemList.size() != 2 )
            {
                LogFile.write("Error creating 2 InstLinking items");
            }
            //Creating Project
            POM_object context = mdoTestUtils.createProject( "Project_SaveAsDE", "mdoProjectDesc" );
            mdoTestUtils.setProjectAsMultiDisciplinary(context);
            //Creating Collaborative design
            ModelObject cdObject = mdoTestUtils.createCD("CDTest");
            ModelObject[] collaborativeDesigns = new ModelObject[1];
            collaborativeDesigns[0] = cdObject;
            mdoTestUtils.assignCDsToProject(collaborativeDesigns, context);
            //Creating Design Elements
            dElem.add( mdoTestUtils.createReuseDEWithCD( revList.get( 0 ), "InstReuseDE_32", cdObject ) );
            dElem.add( mdoTestUtils.createReuseDEWithCD( revList.get( 1 ), "InstReuseDE_33", cdObject ) );

            //Release reuse DE dElem[0]
            WorkspaceObject[] deObjs = new WorkspaceObject[1];
            deObjs[0] = (WorkspaceObject) dElem.get(0);
            mdoTestUtils.applyReleaseStatus( deObjs, "TCM Released" );

            //Add domain relevancy information

            /// ----------------------------------------
            // Add DA1 Rev as  relevant for Automation
            // With DE1   as irrelevant for Automation

            DomainRelevancyInput[] inputs1 = new DomainRelevancyInput[2];

            //Input 1 for DA1 Rev
            inputs1[0] = new DomainRelevancyInput();
            inputs1[0].clientId = "AddRemoveDomainRelForInput1";
            inputs1[0].designArtifact = revList.get(0);

            String[] addRelDom1 = new String[1];
            addRelDom1[0] = "Automation";

            RelevancyInformation addInfo1 = new RelevancyInformation();
            addInfo1.relevantDomain = addRelDom1;

            inputs1[0].addInformation = addInfo1;

            //Input 2 for DE1

            inputs1[1] = new DomainRelevancyInput();
            inputs1[1].clientId = "AddRemoveDomainRelForInput2";
            inputs1[1].designArtifact = (WorkspaceObject) dElem.get(0);

            String[] addIrrDom2 = new String[1];
            addIrrDom2[0] = "Automation";

            RelevancyInformation addInfo2 = new RelevancyInformation();
            addInfo2.irrelevantDomain = addIrrDom2;

            inputs1[1].addInformation = addInfo2;

            // update domain relevancy on designs
            mdoTestUtils.updateDomainRelevancy( inputs1 );

            // SaveAs released reuse DE dElem[0]
            WorkspaceObject saveAsDElem1 = (WorkspaceObject)mdoTestUtils.saveAsDE(dElem.get(0));
            LogFile.write("New revision " + saveAsDElem1.get_object_string() );

            // check Automation relevancy for SaveAs DE1
            GetDomainRelevancyInput[] inputs = new GetDomainRelevancyInput[1];
            inputs[0] = new GetDomainRelevancyInput();
            inputs[0].clientId = "GetDomainRelSingle";
            inputs[0].inputObject = saveAsDElem1;
            inputs[0].domain = "Automation";

            GetDomainRelevancyOfAnObjectResp response2 = mdoTestUtils.getDomainRelevancy( inputs );
            if ( response2.serviceData.sizeOfPartialErrors() == 0 &&
                 response2.output.length == 1 &&
                 response2.output[0].relevancyValue.equals( "irrelevant" ) &&
                 response2.output[0].isDesignArtifactBased == false
                 )
            {
                LogFile.write( "getDomainRelevancy irrelevant succeeded" );
            }
            else
            {
                LogFile.write( "getDomainRelevancy irrelevant failed" );
                ++failCnt;
            }

            LogFile.write( "executeSaveAsDesignElement succeeded" );
        }
        catch ( Exception ex )
        {
            LogFile.write( "executeSaveAsDesignElement failed" );
            ++failCnt;
            ex.printStackTrace();
        }
    }

    /**
     * Tests carryForwardMDI on unrelated Design Elements
     *
     */
    public static void executeCarryForwardMDIDiffDesignElements()
    {
        MDOTestUtils mdoTestUtils = new MDOTestUtils();
        List< Item > itemList = new ArrayList< Item >();
        List< ItemRevision > revList = new ArrayList< ItemRevision >();
        List<ModelObject> dElem = new ArrayList <ModelObject>();

        try
        {
            //Creating Items for Design Elements
            mdoTestUtils.createItems( 2, "InstLinking", "Item", itemList, revList );
            if ( itemList.size() != 2 )
            {
                LogFile.write("Error creating 2 InstLinking items");
            }
            //Creating Project
            POM_object context = mdoTestUtils.createProject( "Project_ReviseDE", "mdoProjectDesc" );
            mdoTestUtils.setProjectAsMultiDisciplinary(context);
            //Creating Collaborative design
            ModelObject cdObject = mdoTestUtils.createCD("CDTest");
            ModelObject[] collaborativeDesigns = new ModelObject[1];
            collaborativeDesigns[0] = cdObject;
            mdoTestUtils.assignCDsToProject(collaborativeDesigns, context);
            //Creating Design Elements
            dElem.add( mdoTestUtils.createReuseDEWithCD( revList.get( 0 ), "InstReuseDE_30", cdObject ) );
            dElem.add( mdoTestUtils.createReuseDEWithCD( revList.get( 1 ), "InstReuseDE_31", cdObject ) );
            //Creating MDInstance

            // Carry forward MDI
            SearchForLinkedInstancesResponse carryResp = mdoTestUtils.carryForwardMDI( dElem.get(0), dElem.get(1), true );
            if ( carryResp.serviceData.sizeOfPartialErrors() == 1 )
            {
                // Carry forward MDI
                SearchForLinkedInstancesResponse carryResp2 = mdoTestUtils.carryForwardMDI( dElem.get(0), dElem.get(1), false );
                if ( carryResp2.serviceData.sizeOfPartialErrors() == 1 )
                {
                    LogFile.write( "CarryForwardMDIDiffDesignElements succeeded" );
                }
                else
                {
                    LogFile.write( "CarryForwardMDIDiffDesignElements failed-B" );
                    ++failCnt;
                }
            }
            else
            {
                LogFile.write( "CarryForwardMDIDiffDesignElements failed-A" );
                ++failCnt;
            }
        }
        catch ( Exception ex )
        {
            LogFile.write( "CarryForwardMDIDiffDesignElements failed" );
            ++failCnt;
            ex.printStackTrace();
        }
    }

    /**
     * Tests Linking of MDT with MDI use cases
     *
     */

    private static void executeMDInstanceLinkingWithMDTUseCases() throws Exception
    {
        MDOTestUtils m_mdoTestUtils = new MDOTestUtils();
        CreateItemsResponse response;
        try
        {
            //Creating 5 Design Artifacts, create 1 MDOs with 5 DAs respectively. Where DAs are ItemRevision
            //MDO4 has DesignArtifact6/A, DesignArtifact7/A, DesignArtifact8/A, DesignArtifact9/A, DesignArtifact10/A


            // Creating Design Artifacts
            response = m_mdoTestUtils.createDesignArtifactsWithType("DesignArtifact6", "Item", " DesignArtifact6");
            ItemRevision DA1Rev = response.output[0].itemRev;

            response = m_mdoTestUtils.createDesignArtifactsWithType("DesignArtifact7", "Item", " DesignArtifact7");
            ItemRevision DA2Rev = response.output[0].itemRev;
            Item DA2 = response.output[0].item;

            response = m_mdoTestUtils.createDesignArtifactsWithType("DesignArtifact8", "Item", " DesignArtifact8");
            ItemRevision DA3Rev = response.output[0].itemRev;

            response = m_mdoTestUtils.createDesignArtifactsWithType("DesignArtifact9", "Item", " DesignArtifact9");
            ItemRevision DA4Rev = response.output[0].itemRev;

            response = m_mdoTestUtils.createDesignArtifactsWithType("DesignArtifact10", "Item", " DesignArtifact10");
            ItemRevision DA5Rev = response.output[0].itemRev;

           // Create MDO4. Has DesignArtifact6/A, DesignArtifact7/A, DesignArtifact8/A, DesignArtifact9/A, DesignArtifact10/A
            List<WorkspaceObject> revsOfDAsForMdo1 = new ArrayList<WorkspaceObject>();
            revsOfDAsForMdo1.add(DA1Rev);
            revsOfDAsForMdo1.add(DA2Rev);
            revsOfDAsForMdo1.add(DA3Rev);
            revsOfDAsForMdo1.add(DA4Rev);
            revsOfDAsForMdo1.add(DA5Rev);

            WorkspaceObject mdo1 = m_mdoTestUtils.createMDOWithDAList(revsOfDAsForMdo1,"MDO4", "MDO4 for search test");

            //Creating Collaborative design
            ModelObject cdObject = m_mdoTestUtils.createCD("CDTest");

            List<WorkspaceObject> dElem = new ArrayList<WorkspaceObject>();
            //Creating Design Elements
            dElem.add((WorkspaceObject)m_mdoTestUtils.createReuseDEWithCD( DA1Rev,"DA1DE1",cdObject ));
            dElem.add((WorkspaceObject)m_mdoTestUtils.createReuseDEWithCD( DA2Rev,"DA2DE1",cdObject ));
            dElem.add((WorkspaceObject)m_mdoTestUtils.createReuseDEWithCD( DA3Rev,"DA3DE1",cdObject ));
            dElem.add((WorkspaceObject)m_mdoTestUtils.createReuseDEWithCD( DA4Rev,"DA4DE1",cdObject ));
            dElem.add((WorkspaceObject)m_mdoTestUtils.createReuseDEWithCD( DA5Rev,"DA5DE1",cdObject ));

            //Creating Project
            POM_object context2 = m_mdoTestUtils.createProject( "proj2", "Project 2" );
            m_mdoTestUtils.setProjectAsMultiDisciplinary(context2);

            ModelObject[] collDesigns = new ModelObject[1];
            collDesigns[0] = cdObject;
            m_mdoTestUtils.assignCDsToProject(collDesigns, context2);


            // Create MDI1 linking DEs in context proj2 with MDO MDO4

            InstancesToLinkResponse2 resp = m_mdoTestUtils.linkDesignInstances2( dElem, context2, true, mdo1);


            if (resp!=null && resp.output.length > 0 )
            {

                LogFile.write("Create MDI1 linking DA1DE1, DA2DE1, DA3DE1, DA4DE1, DA5DE1 with context proj2 with MDO MDO4 succeeded");
                com.teamcenter.soa.client.model.Property[] objNameProp2;
                WorkspaceObject mdoObject = resp.output[0].instanceLinkingObjectData[0].mdoObject;
                objNameProp2 = MDOTestUtils.getObjectProperties(mdoObject, new String[] {"object_name"});
                LogFile.write( "MDThread named: " + objNameProp2[0].getDisplayableValue() + " uid: " + mdoObject.getUid() + "  is Linked to MDI." );

            }

            //Add domain relevancy information

            /// ----------------------------------------
            // Add DA1 Rev as  relevant for Automation, Mechanical
            // Add DA2 Item as relevant for Automation, Mechanical
            // Add DA3 Rev as  relevant for Automation and irrelevant for Mechanical
            // Add DA4 Rev as  relevant for Automation
            // With DE4 as relevant for Mechanical

            DomainRelevancyInput[] inputs1 = new DomainRelevancyInput[6];

            //Input 1 for DA1 Rev
            inputs1[0] = new DomainRelevancyInput();
            inputs1[0].clientId = "AddRemoveDomainRelForInput1";
            inputs1[0].designArtifact = DA1Rev;

            String[] addRelDom1 = new String[2];
            addRelDom1[0] = "Automation";
            addRelDom1[1] = "Mechanical";


            RelevancyInformation addInfo1 = new RelevancyInformation();
            addInfo1.relevantDomain = addRelDom1;

            inputs1[0].addInformation = addInfo1;

            //Input 2 for DA2 Item

            inputs1[1] = new DomainRelevancyInput();
            inputs1[1].clientId = "AddRemoveDomainRelForInput2";
            inputs1[1].designArtifact = DA2;

            String[] addRelDom2 = new String[2];
            addRelDom2[0] = "Automation";
            addRelDom2[1] = "Mechanical";

            RelevancyInformation addInfo2 = new RelevancyInformation();
            addInfo2.relevantDomain = addRelDom2;

            inputs1[1].addInformation = addInfo2;

            //Input 3 for DA3 Rev

            inputs1[2] = new DomainRelevancyInput();
            inputs1[2].clientId = "AddRemoveDomainRelForInput3";
            inputs1[2].designArtifact = DA3Rev;

            String[] addRelDom3 = new String[1];
            addRelDom3[0] = "Automation";

            String[] addIrRelDom3 = new String[1];
            addIrRelDom3[0] = "Mechanical";

            RelevancyInformation addInfo3 = new RelevancyInformation();
            addInfo3.relevantDomain = addRelDom3;
            addInfo3.irrelevantDomain = addIrRelDom3;

            inputs1[2].addInformation = addInfo3;

            //Input 4 for DA4 Rev

            inputs1[3] = new DomainRelevancyInput();
            inputs1[3].clientId = "AddRemoveDomainRelForInput4";
            inputs1[3].designArtifact = DA4Rev;

            String[] addRelDom4 = new String[1];
            addRelDom4[0] = "Automation";

            RelevancyInformation addInfo4 = new RelevancyInformation();
            addInfo4.relevantDomain = addRelDom4;

            inputs1[3].addInformation = addInfo4;

            //Input 5 for DE4

            inputs1[4] = new DomainRelevancyInput();
            inputs1[4].clientId = "AddRemoveDomainRelForInput5";
            inputs1[4].designArtifact = (WorkspaceObject) dElem.get(3);

            String[] addRelDom5 = new String[1];
            addRelDom5[0] = "Mechanical";

            RelevancyInformation addInfo5 = new RelevancyInformation();
            addInfo5.relevantDomain = addRelDom5;

            inputs1[4].addInformation = addInfo5;

            //Input 6 for DE5

            inputs1[5] = new DomainRelevancyInput();
            inputs1[5].clientId = "AddRemoveDomainRelForInput6";
            inputs1[5].designArtifact = (WorkspaceObject) dElem.get(4);

            String[] addRelDom6 = new String[1];
            addRelDom6[0] = "Mechanical";

            RelevancyInformation addInfo6 = new RelevancyInformation();
            addInfo6.relevantDomain = addRelDom6;

            inputs1[5].addInformation = addInfo6;

            // update domain relevancy on designs
            m_mdoTestUtils.updateDomainRelevancy( inputs1 );

            //Search for Linked Design instances using searchForLinkedDesignInstances2
            List<DesignInstancesData> didList2 = new ArrayList<DesignInstancesData>();
            DesignInstancesData did2[] = new DesignInstancesData[1];
            did2[0] = new DesignInstancesData();
            did2[0].designInstance = (POM_object) dElem.get(2);
            did2[0].isPreciseLink = true;
            didList2.add(did2[0]);
            SearchForLinkedInstances2Response resp1 = m_mdoTestUtils.searchForLinkedDesignInstances2(didList2);
            m_mdoTestUtils.printSearchForLinkedInstances2Response(resp1);
            // Validate the returned domains
            if ( resp1.serviceData.sizeOfPartialErrors() == 0 &&
                 resp1.output[0].linkedInstances[0].mdoObjectsLinkedToMDI.length == 1 &&
                 resp1.output[0].linkedInstances[0].instancesInfo.length == 4 )
            {
                for ( int inx = 0; inx < resp1.output[0].linkedInstances[0].instancesInfo.length; ++inx )
                {
                    if ( resp1.output[0].linkedInstances[0].instancesInfo[inx].designInstance == dElem.get(0) ||
                         resp1.output[0].linkedInstances[0].instancesInfo[inx].designInstance == dElem.get(1) ||
                         resp1.output[0].linkedInstances[0].instancesInfo[inx].designInstance == dElem.get(3) )
                    {
                        if ( resp1.output[0].linkedInstances[0].instancesInfo[inx].relevantDomains.length == 2 )
                        {
                            if ( resp1.output[0].linkedInstances[0].instancesInfo[inx].relevantDomains[0] !=
                                 resp1.output[0].linkedInstances[0].instancesInfo[inx].relevantDomains[1] &&
                                 ( resp1.output[0].linkedInstances[0].instancesInfo[inx].relevantDomains[0].equals( "Automation" ) ||
                                   resp1.output[0].linkedInstances[0].instancesInfo[inx].relevantDomains[0].equals( "Mechanical" ) ) &&
                                 ( resp1.output[0].linkedInstances[0].instancesInfo[inx].relevantDomains[1].equals( "Automation" ) ||
                                   resp1.output[0].linkedInstances[0].instancesInfo[inx].relevantDomains[1].equals( "Mechanical" ) ) )
                            {
                                LogFile.write("These domains are found: " +
                                    resp1.output[0].linkedInstances[0].instancesInfo[inx].relevantDomains[0] + ", " +
                                    resp1.output[0].linkedInstances[0].instancesInfo[inx].relevantDomains[1] );
                            }
                            else
                            {
                                LogFile.write("These domains should be found: Automation, Mechanical; but these are found instead: " +
                                    resp1.output[0].linkedInstances[0].instancesInfo[inx].relevantDomains[0] + ", " +
                                    resp1.output[0].linkedInstances[0].instancesInfo[inx].relevantDomains[1] );
                            }
                        }
                        else
                        {
                            LogFile.write("Should be 2 relevant domains found, not " + resp1.output[0].linkedInstances[0].instancesInfo[inx].relevantDomains.length );
                        }
                    }
                    else if ( resp1.output[0].linkedInstances[0].instancesInfo[inx].designInstance == dElem.get(4) )
                    {
                        if ( resp1.output[0].linkedInstances[0].instancesInfo[inx].relevantDomains.length == 1 )
                        {
                            if ( resp1.output[0].linkedInstances[0].instancesInfo[inx].relevantDomains[0].equals( "Mechanical" ) )
                            {
                                LogFile.write("This domain is found: " +
                                    resp1.output[0].linkedInstances[0].instancesInfo[inx].relevantDomains[0] );
                            }
                            else
                            {
                                LogFile.write("This domain should be found: Mechanical; but this is found instead: " +
                                    resp1.output[0].linkedInstances[0].instancesInfo[inx].relevantDomains[0] );
                            }
                        }
                        else
                        {
                            LogFile.write("Should be 1 relevant domain found, not " + resp1.output[0].linkedInstances[0].instancesInfo[inx].relevantDomains.length );
                        }
                    }
                    else
                    {
                        LogFile.write("Search is not returning all of the expected design elements");
                    }
                }
            }

            // Remove MDT on MDI

            ModelObject mdiObject = resp1.output[0].linkedInstances[0].mdiObject;
            WorkspaceObject mdoObject = resp1.output[0].linkedInstances[0].mdoObjectsLinkedToMDI[0];

            UpdateMDInstanceToMDThreadResponse resp3 = m_mdoTestUtils.updateMDTonMDI (mdiObject, null, mdoObject);
            if(resp3 != null && resp3.linkOutput.length > 0 &&  resp3.linkOutput[0].linkedMDOObjects.length == 0)
            {
                LogFile.write("Removing MDT from MDI using updateMDInstanceToMDThreadLink succeeded");
            }

            //Search for Linked Design instances using searchForLinkedDesignInstances2 after removing MDT from MDI

            resp1 = m_mdoTestUtils.searchForLinkedDesignInstances2(didList2);
            m_mdoTestUtils.printSearchForLinkedInstances2Response(resp1);

           // Add MDT on MDI

            resp3 = m_mdoTestUtils.updateMDTonMDI (mdiObject, mdoObject, null);
            if(resp3 != null && resp3.linkOutput.length > 0 && resp3.linkOutput[0].linkedMDOObjects.length > 0 )
            {
                LogFile.write("Adding MDT from MDI using updateMDInstanceToMDThreadLink succeeded");

                com.teamcenter.soa.client.model.Property[] objNameProp2;

                for(int inx=0 ; inx < resp3.linkOutput[0].linkedMDOObjects.length ; inx++)
                {
                    WorkspaceObject mdoObject1 = resp3.linkOutput[0].linkedMDOObjects[inx];
                    objNameProp2 = MDOTestUtils.getObjectProperties(mdoObject1, new String[] {"object_name"});
                    LogFile.write( "MDThread named: " + objNameProp2[0].getDisplayableValue() + " uid: " + mdoObject1.getUid() + "  is Linked to MDI." );
                }
            }

            //Test delete MDThread object linked to MDI

            ModelObject[] objToBeDeleted = new ModelObject[1];
            objToBeDeleted[0] = mdoObject;
            m_mdoTestUtils.cleanupData(objToBeDeleted);

            LogFile.write("Delete of MDThread linked to MDI succeeded");

            //Search for Linked Design instances using searchForLinkedDesignInstances2 after deleting MDT which is linked to MDI

            resp1 = m_mdoTestUtils.searchForLinkedDesignInstances2(didList2);
            if (resp1!=null && resp1.output.length > 0 )
            {
                LogFile.write("Search for Linked Design instances using searchForLinkedDesignInstances2 succeeded");
                if( resp1.output[0].linkedInstances.length > 0 && resp1.output[0].linkedInstances[0].mdoObjectsLinkedToMDI.length == 0)
                {
                    LogFile.write( "No MDThread is linked to MDI." );
                }
            }
            m_mdoTestUtils.printSearchForLinkedInstances2Response(resp1);

            LogFile.write( "executeMDInstanceLinkingWithMDTUseCases succeeded" );
        }
        catch ( Exception ex )
        {
            LogFile.write( "executeMDInstanceLinkingWithMDTUseCases failed" );
            ++failCnt;
            ex.printStackTrace();

        }
    }

    /**
     * Tests query and updates for NeedsValidation links for MDO and MDI association
     *
     */

    private static void executeQueryAndUpdateOfMDOAndMDIAssociations() throws Exception
    {
        MDOTestUtils mdoTestUtils = new MDOTestUtils();
        try
        {
            LogFile.write( "------------------------------------------------------------------------" );
            LogFile.write( "Query for MDT Association with Needs Validation set as true using flag queryAllMDOLinks as true - Start" );


            NeedsValidationLinkInput[] qryInputs = new NeedsValidationLinkInput[1];
            NeedsValidationLinkInput input1 = new NeedsValidationLinkInput();
            input1.clientId = "QueryNeedsValidation";
            input1.queryAllMDOLinks = true;
            qryInputs[0] = input1;

            NeedsValidationLinkResponse qryResp = mdoTestUtils.queryNeedsValidation(qryInputs);
            mdoTestUtils.printQueryNeedsValidationResults (qryResp);

            LogFile.write( "Query for MDT Association with Needs Validation set as true using flag queryAllMDOLinks as true - End" );
            LogFile.write( "------------------------------------------------------------------------" );

            LogFile.write( "Update ALL MDT Association with Needs Validation set as true - Start" );


            if(qryResp.linkQueryOutput.length > 0 && qryResp.linkQueryOutput[0].mdoLinkOutput.length > 0)
            {


                int arrylen = qryResp.linkQueryOutput[0].mdoLinkOutput.length;
                UpdateLinkStatusToValidatedInput[] updateInputs = new UpdateLinkStatusToValidatedInput[arrylen];

                for(int i = 0; i<arrylen; ++i )
                {
                    UpdateLinkStatusToValidatedInput updInput1 = new UpdateLinkStatusToValidatedInput();
                    updInput1.clientId = "UpdateNeedsValidationForMDO"+i;
                    updInput1.mdoDataForUpdate = qryResp.linkQueryOutput[0].mdoLinkOutput[i];
                    updateInputs[i] = updInput1;
                }

                UpdateLinksToValidatedResponse updateResp = mdoTestUtils.updateNeedsValidation(updateInputs);
                com.teamcenter.soa.client.model.Property[] objNameProp2;
                for( int i= 0; i< updateResp.output.length ; ++i)
                {
                    WorkspaceObject mdoObjectUpdated = updateResp.output[i].mdoObject;

                     objNameProp2 = MDOTestUtils.getObjectProperties(mdoObjectUpdated, new String[] {"object_name"});
                     LogFile.write( "Association for MDThread named: " + objNameProp2[0].getDisplayableValue() + " uid: " + mdoObjectUpdated.getUid() + "  is updated." );
                }
            }

            LogFile.write( "Update ALL MDT Association with Needs Validation set as true - End" );

            LogFile.write( "Query for MDT Association with Needs Validation set as true using flag queryAllMDOLinks as true after update - Start" );

            qryResp = mdoTestUtils.queryNeedsValidation(qryInputs);
            mdoTestUtils.printQueryNeedsValidationResults (qryResp);

            LogFile.write( "Query for MDT Association with Needs Validation set as true using flag queryAllMDOLinks as true after update - End" );


            LogFile.write( "------------------------------------------------------------------------" );
            LogFile.write( "Query for MDI Association with Needs Validation set as true using flag queryAllMDILinks as true - Start" );
            NeedsValidationLinkInput[] qryInputs2 = new NeedsValidationLinkInput[1];
            NeedsValidationLinkInput input2 = new NeedsValidationLinkInput();
            input2.clientId = "QueryNeedsValidation";
            input2.queryAllMDILinks = true;
            qryInputs2[0] = input2;

            NeedsValidationLinkResponse qryResp2 = mdoTestUtils.queryNeedsValidation(qryInputs2);
            mdoTestUtils.printQueryNeedsValidationResults (qryResp2);

            LogFile.write( "Query for MDI Association with Needs Validation set as true using flag queryAllMDILinks as true - End" );

            LogFile.write( "------------------------------------------------------------------------" );

            LogFile.write( "Update ALL MDI Association with Needs Validation set as true - Start" );


            if(qryResp2.linkQueryOutput.length > 0 && qryResp2.linkQueryOutput[0].mdiLinkOutput.length > 0)
            {
                int arrylen = qryResp2.linkQueryOutput[0].mdiLinkOutput.length;
                UpdateLinkStatusToValidatedInput[] updateInputs = new UpdateLinkStatusToValidatedInput[arrylen];

                for(int i = 0; i<arrylen; ++i )
                {
                    UpdateLinkStatusToValidatedInput updInput1 = new UpdateLinkStatusToValidatedInput();
                    updInput1.clientId = "UpdateNeedsValidationForMDI"+i;
                    updInput1.mdiDataForUpdate = qryResp2.linkQueryOutput[0].mdiLinkOutput[i];
                    updateInputs[i] = updInput1;
                }

                UpdateLinksToValidatedResponse updateResp = mdoTestUtils.updateNeedsValidation(updateInputs);

                for( int i= 0; i< updateResp.output.length ; ++i)
                {

                     LogFile.write( "Association for MDInstance  uid: " + updateResp.output[i].mdiObject.getUid() + "  is updated." );
                }

            }

            LogFile.write( "Update ALL MDI Association with Needs Validation set as true - End" );
            LogFile.write( "------------------------------------------------------------------------" );

            LogFile.write( "Query for MDI Association with Needs Validation set as true using flag queryAllMDILinks as true after update - Start" );

            qryResp2 = mdoTestUtils.queryNeedsValidation(qryInputs2);
            mdoTestUtils.printQueryNeedsValidationResults (qryResp2);

            LogFile.write( "Query for MDI Association with Needs Validation set as true using flag queryAllMDILinks as true after update - End" );
            LogFile.write( "------------------------------------------------------------------------" );


            LogFile.write(  "Test for query and update of needs validation for MDO and MDI succeeded" );

        }
        catch ( Exception ex )
        {
            LogFile.write( "executeQueryAndUpdateOfMDOAndMDIAssociations failed" );
            ++failCnt;
            ex.printStackTrace();

        }
    }

    private static void executeUpdateDomainRelevancyAddAndRemove()
    {
        try
        {
            MDOTestUtils mdoTestUtils = new MDOTestUtils();
            List< Item > itemsList = new ArrayList< Item >();
            List< ItemRevision > revsList = new ArrayList< ItemRevision >();
            CreateItemsResponse crItemsResp =
                    mdoTestUtils.createItems( 1, "DA1", "Item", itemsList, revsList );

            crItemsResp = mdoTestUtils.createDesignArtifactsWithType("DA2", "Item", " DA2");
            Item DA2 = crItemsResp.output[0].item;

            WorkspaceObject[] designArtifacts = new WorkspaceObject[2];
            designArtifacts[0] = revsList.get(0);
            designArtifacts[1] = DA2;

            // ----------------------------------------
            // Add    DA1 as relevant   for Automation
            // Add    DA1 as relevant   for UnknownDomain - error
            // Add    DA1 as irrelevant for Mechanical
            // Add    DA1 as irrelevant for UnkDomain - error
            DomainRelevancyInput[] inputs1 = new DomainRelevancyInput[2];
            inputs1[0] = new DomainRelevancyInput();
            inputs1[0].clientId = "AddRemoveDomainRel";
            inputs1[0].designArtifact = designArtifacts[0];
            inputs1[1] = new DomainRelevancyInput();
            inputs1[1].clientId = "AddRemoveDomainRel";
            inputs1[1].designArtifact = designArtifacts[1];

            String[] addRelDom1 = new String[2];
            addRelDom1[0] = "Automation";
            addRelDom1[1] = "UnknownDomain";
            String[] addIrrDom1 = new String[2];
            addIrrDom1[0] = "Mechanical";
            addIrrDom1[1] = "UnkDomain";
            RelevancyInformation addInfo1 = new RelevancyInformation();
            addInfo1.relevantDomain = addRelDom1;
            addInfo1.irrelevantDomain = addIrrDom1;
            inputs1[0].addInformation = addInfo1;

            UpdateDomainRelevancyResponse resp1 = mdoTestUtils.updateDomainRelevancy( inputs1 );

            LogFile.write( "updateDomainRelevancy Partial Errors = " + resp1.serviceData.sizeOfPartialErrors() );
            if ( resp1.serviceData.sizeOfPartialErrors() == 1 &&
                resp1.output.length == 2 &&
                resp1.output[0].domainRelevancy.relevantDomain.length == 1 &&
                resp1.output[0].domainRelevancy.relevantDomain[0].equals( "Automation" ) &&
                resp1.output[0].domainRelevancy.irrelevantDomain.length == 1 &&
                resp1.output[0].domainRelevancy.irrelevantDomain[0].equals( "Mechanical" )
                )
            {
                LogFile.write( "updateDomainRelevancy adds with error succeeded" );
            }
            else
            {
                LogFile.write( "updateDomainRelevancy adds with error failed" );
                ++failCnt;
            }

            // ----------------------------------------
            // Remove DA1 as relevant   for Automation
            // Remove DA1 as relevant   for UnknownDomain - error
            // Remove DA1 as irrelevant for Mechanical
            // Remove DA1 as irrelevant for UnkDomain - error
            DomainRelevancyInput[] inputs2 = new DomainRelevancyInput[2];
            inputs2[0] = new DomainRelevancyInput();
            inputs2[0].clientId = "AddRemoveDomainRel";
            inputs2[0].designArtifact = designArtifacts[0];
            inputs2[1] = new DomainRelevancyInput();
            inputs2[1].clientId = "AddRemoveDomainRel";
            inputs2[1].designArtifact = designArtifacts[1];

            String[] remRelDom2 = new String[2];
            remRelDom2[0] = "Automation";
            remRelDom2[1] = "UnknownDomain";
            String[] remIrrDom2 = new String[2];
            remIrrDom2[0] = "Mechanical";
            remIrrDom2[1] = "UnkDomain";
            RelevancyInformation remInfo2 = new RelevancyInformation();
            remInfo2.relevantDomain = remRelDom2;
            remInfo2.irrelevantDomain = remIrrDom2;
            inputs2[0].removeInformation = remInfo2;

            UpdateDomainRelevancyResponse resp2 = mdoTestUtils.updateDomainRelevancy( inputs2 );
            LogFile.write( "updateDomainRelevancy Partial Errors = " + resp2.serviceData.sizeOfPartialErrors() );
            if ( resp2.serviceData.sizeOfPartialErrors() == 1 &&
                resp2.output.length == 2 &&
                resp2.output[0].domainRelevancy.relevantDomain.length == 0 &&
                resp2.output[0].domainRelevancy.irrelevantDomain.length == 0
                )
            {
                LogFile.write( "updateDomainRelevancy removes with error succeeded" );
            }
            else
            {
                LogFile.write( "updateDomainRelevancy removes with error failed" );
                ++failCnt;
            }

            // ----------------------------------------
            // Remove DA1 as relevant   for Mechanical - skip
            // Add    DA1 as relevant   for Automation
            // Add    DA1 as irrelevant for Mechanical
            DomainRelevancyInput[] inputs3 = new DomainRelevancyInput[1];
            inputs3[0] = new DomainRelevancyInput();
            inputs3[0].clientId = "AddRemoveDomainRel";
            inputs3[0].designArtifact = designArtifacts[0];

            String[] remRelDom3 = new String[1];
            remRelDom3[0] = "Mechanical";
            RelevancyInformation remInfo3 = new RelevancyInformation();
            remInfo3.relevantDomain = remRelDom3;
            inputs3[0].removeInformation = remInfo3;

            String[] addRelDom3 = new String[1];
            addRelDom3[0] = "Automation";
            String[] addIrrDom3 = new String[1];
            addIrrDom3[0] = "Mechanical";
            RelevancyInformation addInfo3 = new RelevancyInformation();
            addInfo3.relevantDomain = addRelDom3;
            addInfo3.irrelevantDomain = addIrrDom3;
            inputs3[0].addInformation = addInfo3;

            UpdateDomainRelevancyResponse resp3 = mdoTestUtils.updateDomainRelevancy( inputs3 );
            LogFile.write( "updateDomainRelevancy Partial Errors = " + resp3.serviceData.sizeOfPartialErrors() );
            if ( resp3.serviceData.sizeOfPartialErrors() == 0 &&
                resp3.output.length == 1 &&
                resp3.output[0].domainRelevancy.relevantDomain.length == 1 &&
                resp3.output[0].domainRelevancy.relevantDomain[0].equals( "Automation" ) &&
                resp3.output[0].domainRelevancy.irrelevantDomain.length == 1 &&
                resp3.output[0].domainRelevancy.irrelevantDomain[0].equals( "Mechanical" )
                )
            {
                LogFile.write( "updateDomainRelevancy adds with invalid succeeded" );
            }
            else
            {
                LogFile.write( "updateDomainRelevancy adds with invalid failed" );
                ++failCnt;
            }

            // ----------------------------------------
            // Remove DA1 as relevant   for Automation
            // Remove DA1 as irrelevant for Mechanical
            DomainRelevancyInput[] inputs4 = new DomainRelevancyInput[1];
            inputs4[0] = new DomainRelevancyInput();
            inputs4[0].clientId = "AddRemoveDomainRel";
            inputs4[0].designArtifact = designArtifacts[0];

            String[] remRelDom4 = new String[1];
            remRelDom4[0] = "Mechanical";
            String[] remIrrDom4 = new String[1];
            remIrrDom4[0] = "Automation";
            RelevancyInformation remInfo4 = new RelevancyInformation();
            remInfo4.relevantDomain = remRelDom4;
            remInfo4.irrelevantDomain = remIrrDom4;
            inputs4[0].removeInformation = remInfo4;

            UpdateDomainRelevancyResponse resp4 = mdoTestUtils.updateDomainRelevancy( inputs4 );
            LogFile.write( "updateDomainRelevancy Partial Errors = " + resp4.serviceData.sizeOfPartialErrors() );
            if ( resp4.serviceData.sizeOfPartialErrors() == 0 &&
                resp4.output.length == 1 &&
                resp4.output[0].domainRelevancy.relevantDomain.length == 0 &&
                resp4.output[0].domainRelevancy.irrelevantDomain.length == 0
                )
            {
                LogFile.write( "updateDomainRelevancy removes succeeded" );
            }
            else
            {
                LogFile.write( "updateDomainRelevancy removes failed" );
                ++failCnt;
            }
            LogFile.write( "executeUpdateDomainRelevancyAddAndRemove succeeded" );
        }
        catch ( Exception ex )
        {
            LogFile.write( "executeUpdateDomainRelevancyAddAndRemove failed" );
            ++failCnt;
            ex.printStackTrace();
        }

    }

    private static void executeGetDomainRelevancyDASingle()
    {
        try
        {
            MDOTestUtils mdoTestUtils = new MDOTestUtils();
            List< Item > itemsList = new ArrayList< Item >();
            List< ItemRevision > revsList = new ArrayList< ItemRevision >();
            CreateItemsResponse crItemsResp =
                    mdoTestUtils.createItems( 1, "DA1", "Item", itemsList, revsList );

            crItemsResp = mdoTestUtils.createDesignArtifactsWithType("DA2", "Item", " DA2");
            Item DA2 = crItemsResp.output[0].item;

            // create data for library creation.
            String[] disciplines = new String[2];
            disciplines[0] = "Mechanical";
            disciplines[1] = "Electrical";

            String[] allowedMemberTypes = new String[2];
            allowedMemberTypes[0]="Item";
            allowedMemberTypes[1]="ItemRevision";

            int numLibrariesToCreate = 1;
            Lbr0Library[] createdLibraries = mdoTestUtils.createLbr0Librararies( "ST04Library", disciplines, allowedMemberTypes, numLibrariesToCreate );

            // one Hierarchy and and one General Node
            CreateHierarchiesResponse hierarchyResponse;
            hierarchyResponse = mdoTestUtils.createHierarchy(createdLibraries[0], "ST04Hierarchy" );

            CreateNodesResponse nodeResponse;
            nodeResponse = mdoTestUtils.createLbrHierarchyGenralNode ( hierarchyResponse.output[0].hierarchy, null, "Lbr0_pub_Node2" );

            // Create Item daForLE1
            CreateItemsResponse response = mdoTestUtils.createDesignArtifactsWithType("daForLE1", "Item", "Description for daForLE1" );
            ItemRevision daForLE1Rev = response.output[0].itemRev;

            BusinessObject[] itemList1 = new BusinessObject[1];
            itemList1[0] = daForLE1Rev;
            PublishObjectsResponse publishedResponse = mdoTestUtils.publishLibraryObjects( nodeResponse.output[0].node, itemList1 , "le1", 1 );
            WorkspaceObject le1 = (WorkspaceObject) publishedResponse.serviceData.getCreatedObject(0);

            // ----------------------------------------
            // Add    DA1 as relevant   for Automation
            // Add    DA1 as irrelevant for Mechanical
            // Add    LE1 as relevant   for Mechanical
            DomainRelevancyInput[] inputs1 = new DomainRelevancyInput[2];
            inputs1[0] = new DomainRelevancyInput();
            inputs1[0].clientId = "AddRemoveDomainRel";
            inputs1[0].designArtifact = revsList.get(0);

            String[] addRelDom1 = new String[1];
            addRelDom1[0] = "Automation";
            String[] addIrrDom1 = new String[1];
            addIrrDom1[0] = "Mechanical";
            RelevancyInformation addInfo1 = new RelevancyInformation();
            addInfo1.relevantDomain = addRelDom1;
            addInfo1.irrelevantDomain = addIrrDom1;
            inputs1[0].addInformation = addInfo1;

            inputs1[1] = new DomainRelevancyInput();
            inputs1[1].clientId = "AddRemoveDomainRel";
            inputs1[1].designArtifact = le1;

            String[] addRelDom2 = new String[1];
            addRelDom2[0] = "Mechanical";
            RelevancyInformation addInfo2 = new RelevancyInformation();
            addInfo2.relevantDomain = addRelDom2;
            inputs1[1].addInformation = addInfo2;

            mdoTestUtils.updateDomainRelevancy( inputs1 );

            // check Automation relevancy for DA1Rev
            GetDomainRelevancyInput[] inputs = new GetDomainRelevancyInput[1];
            inputs[0] = new GetDomainRelevancyInput();
            inputs[0].clientId = "GetDomainRelSingle";
            inputs[0].inputObject = revsList.get(0);
            inputs[0].domain = "Automation";

            GetDomainRelevancyOfAnObjectResp response1 = mdoTestUtils.getDomainRelevancy( inputs );
            if ( response1.serviceData.sizeOfPartialErrors() == 0 &&
                 response1.output.length == 1 &&
                 response1.output[0].relevancyValue.equals( "relevant" )
                 )
            {
                LogFile.write( "getDomainRelevancy relevant succeeded" );
            }
            else
            {
                LogFile.write( "getDomainRelevancy relevant failed" );
                ++failCnt;
            }

            // check Mechanical relevancy for DA1Rev
            inputs[0].domain = "Mechanical";

            GetDomainRelevancyOfAnObjectResp response2 = mdoTestUtils.getDomainRelevancy( inputs );
            if ( response2.serviceData.sizeOfPartialErrors() == 0 &&
                 response2.output.length == 1 &&
                 response2.output[0].relevancyValue.equals( "irrelevant" )
                 )
            {
                LogFile.write( "getDomainRelevancy irrelevant succeeded" );
            }
            else
            {
                LogFile.write( "getDomainRelevancy irrelevant failed" );
                ++failCnt;
            }

            // check Mechanical relevancy for DA2
            inputs[0].inputObject = DA2;
            inputs[0].domain = "Mechanical";

            GetDomainRelevancyOfAnObjectResp response3 = mdoTestUtils.getDomainRelevancy( inputs );
            if ( response3.serviceData.sizeOfPartialErrors() == 0 &&
                 response3.output.length == 1 &&
                 response3.output[0].relevancyValue.equals( "" )
                 )
            {
                LogFile.write( "getDomainRelevancy blank succeeded" );
            }
            else
            {
                LogFile.write( "getDomainRelevancy blank failed" );
                ++failCnt;
            }

            // check Mechanical relevancy for LE1
            inputs[0].inputObject = le1;
            inputs[0].domain = "Mechanical";

            GetDomainRelevancyOfAnObjectResp response4 = mdoTestUtils.getDomainRelevancy( inputs );
            if ( response4.serviceData.sizeOfPartialErrors() == 0 &&
                 response4.output.length == 1 &&
                 response4.output[0].relevancyValue.equals( "relevant" ) &&
                 response4.output[0].isDesignArtifactBased
                 )
            {
                LogFile.write( "getDomainRelevancy Mechanical relevant for LE1 succeeded" );
            }
            else
            {
                LogFile.write( "getDomainRelevancy Mechanical relevant for LE1 failed" );
                ++failCnt;
            }

            LogFile.write( "executeGetDomainRelevancyDASingle succeeded" );
        }
        catch ( Exception ex )
        {
            LogFile.write( "executeGetDomainRelevancyDASingle failed" );
            ++failCnt;
            ex.printStackTrace();
        }

    }

    private static void executeGetDomainRelevancyDAAll()
    {
        try
        {
            MDOTestUtils mdoTestUtils = new MDOTestUtils();
            List< Item > itemsList = new ArrayList< Item >();
            List< ItemRevision > revsList = new ArrayList< ItemRevision >();
            CreateItemsResponse crItemsResp =
                    mdoTestUtils.createItems( 1, "DA1", "Item", itemsList, revsList );

            crItemsResp = mdoTestUtils.createDesignArtifactsWithType("DA2", "Item", " DA2");
            Item DA2 = crItemsResp.output[0].item;

            WorkspaceObject[] designArtifacts = new WorkspaceObject[2];
            designArtifacts[0] = revsList.get(0);
            designArtifacts[1] = DA2;

            // ----------------------------------------
            // Add    DA1 as relevant   for Automation
            // Add    DA1 as irrelevant for Mechanical
            DomainRelevancyInput[] inputs1 = new DomainRelevancyInput[1];
            inputs1[0] = new DomainRelevancyInput();
            inputs1[0].clientId = "AddRemoveDomainRel";
            inputs1[0].designArtifact = designArtifacts[0];

            String[] addRelDom1 = new String[1];
            addRelDom1[0] = "Automation";
            String[] addIrrDom1 = new String[1];
            addIrrDom1[0] = "Mechanical";
            RelevancyInformation addInfo1 = new RelevancyInformation();
            addInfo1.relevantDomain = addRelDom1;
            addInfo1.irrelevantDomain = addIrrDom1;
            inputs1[0].addInformation = addInfo1;

            mdoTestUtils.updateDomainRelevancy( inputs1 );

            // check all relevancy for DA1Rev
            GetDomainRelevancyInput[] inputs = new GetDomainRelevancyInput[1];
            inputs[0] = new GetDomainRelevancyInput();
            inputs[0].clientId = "GetDomainRelSingle";
            inputs[0].inputObject = revsList.get(0);
            inputs[0].domain = "";

            GetDomainRelevancyOfAnObjectResp response1 = mdoTestUtils.getDomainRelevancy( inputs );
            if ( response1.serviceData.sizeOfPartialErrors() == 0 &&
                 response1.output.length == 1 &&
                 response1.output[0].domainRelevancy.relevantDomain.length == 1 &&
                 response1.output[0].domainRelevancy.relevantDomain[0].equals("Automation") &&
                 response1.output[0].domainRelevancy.irrelevantDomain.length == 1 &&
                 response1.output[0].domainRelevancy.irrelevantDomain[0].equals("Mechanical")
                 )
            {
                LogFile.write( "getDomainRelevancy blank succeeded" );
            }
            else
            {
                LogFile.write( "getDomainRelevancy blank failed" );
                ++failCnt;
            }

            // check all relevancy for DA2
            inputs[0].inputObject = DA2;
            inputs[0].domain = "";

            GetDomainRelevancyOfAnObjectResp response3 = mdoTestUtils.getDomainRelevancy( inputs );
            if ( response3.serviceData.sizeOfPartialErrors() == 0 &&
                 response3.output.length == 1 &&
                 response3.output[0].relevancyValue.equals( "" )
                 )
            {
                LogFile.write( "getDomainRelevancy blank succeeded" );
            }
            else
            {
                LogFile.write( "getDomainRelevancy blank failed" );
                ++failCnt;
            }

            LogFile.write( "executeGetDomainRelevancyDAAll succeeded" );
        }
        catch ( Exception ex )
        {
            LogFile.write( "executeGetDomainRelevancyDAAll failed" );
            ++failCnt;
            ex.printStackTrace();
        }

    }

    /**
     * Tests splitting Design Instances from an MDInstance
     *
     */
    public static void executeSplitDesignInstanceFromMDI()
    {
        MDOTestUtils mdoTestUtils = new MDOTestUtils();
        List< Item > itemList = new ArrayList< Item >();
        List< ItemRevision > revList = new ArrayList< ItemRevision >();
        List<ModelObject> dElem = new ArrayList <ModelObject>();
        List<ModelObject> dElemOther = new ArrayList <ModelObject>();

        try
        {
            //Creating Items for Design Elements
            mdoTestUtils.createItems( 6, "Splitting", "Item", itemList, revList );
            if ( itemList.size() != 6 )
            {
                LogFile.write("Error creating 6 InstLinking items");
            }
            //Creating Project
            POM_object context = mdoTestUtils.createProject( "Project1_SplitDE", "mdoProjectDesc" );
            mdoTestUtils.setProjectAsMultiDisciplinary(context);
            POM_object context2 = mdoTestUtils.createProject( "Project2_SplitDE", "mdoProjectDesc" );
            //Creating Collaborative design
            ModelObject cdObject = mdoTestUtils.createCD("CDTest");
            ModelObject[] collaborativeDesigns = new ModelObject[1];
            collaborativeDesigns[0] = cdObject;
            mdoTestUtils.assignCDsToProject(collaborativeDesigns, context);
            mdoTestUtils.assignCDsToProject(collaborativeDesigns, context2);
            //Creating Design Elements
            dElem.add( mdoTestUtils.createReuseDEWithCD( revList.get( 0 ), "InstSplitDE_50", cdObject ) );
            dElem.add( mdoTestUtils.createReuseDEWithCD( revList.get( 1 ), "InstSplitDE_51", cdObject ) );
            dElem.add( mdoTestUtils.createReuseDEWithCD( revList.get( 2 ), "InstSplitDE_52", cdObject ) );
            dElem.add( mdoTestUtils.createReuseDEWithCD( revList.get( 3 ), "InstSplitDE_53", cdObject ) );
            dElemOther.add( mdoTestUtils.createReuseDEWithCD( revList.get( 4 ), "InstSplitDE_Other", cdObject ) );
            dElemOther.add( mdoTestUtils.createReuseDEWithCD( revList.get( 5 ), "InstSplitDE_Extra", cdObject ) );
            //Creating MDInstance
            POM_object[] newMDI = mdoTestUtils.linkDesignInstances( dElem, context, true );
            if ( newMDI.length != 1 )
            {
                LogFile.write("Error linkDesignInstances");
            }
            else
            {
                LogFile.write("The 4 DEs are linked with MDI1 " + newMDI[0].toString());
            }
            POM_object[] newMDI2 = mdoTestUtils.linkDesignInstances( dElem, context2, true );
            if ( newMDI2.length != 1 )
            {
                LogFile.write("Error linkDesignInstances");
            }
            else
            {
                LogFile.write("The 4 DEs are linked with MDI2 " + newMDI2[0].toString());
            }
            POM_object[] newMDI3 = mdoTestUtils.linkDesignInstances( dElemOther, context2, true );
            if ( newMDI3.length != 1 )
            {
                LogFile.write("Error linkDesignInstances");
            }
            else
            {
                LogFile.write("The 2 other DEs are linked with MDI3 " + newMDI3[0].toString());
            }

            //Release reuse DE dElem[0]
            WorkspaceObject[] deObjs = new WorkspaceObject[1];
            deObjs[0] = (WorkspaceObject) dElem.get(0);
            mdoTestUtils.applyReleaseStatus( deObjs, "TCM Released" );

            // Revise released reuse DE dElem[0]
            POM_object revisedDElem1 = (POM_object)mdoTestUtils.reviseDE(dElem.get(0));
            LogFile.write("New revision " + revisedDElem1.get_object_string() );

            //Release reuse DE dElem[1]
            WorkspaceObject[] deObjs2 = new WorkspaceObject[1];
            deObjs2[0] = (WorkspaceObject) dElem.get(1);
            mdoTestUtils.applyReleaseStatus( deObjs2, "TCM Released" );

            // Revise released reuse DE dElem[1]
            POM_object revisedDElem2 = (POM_object)mdoTestUtils.reviseDE(dElem.get(1));
            LogFile.write("New revision " + revisedDElem2.get_object_string() );

            List<DesignInstancesData> didList2 = new ArrayList<DesignInstancesData>();
            DesignInstancesData did2[] = new DesignInstancesData[1];
            did2[0] = new DesignInstancesData();
            did2[0].designInstance = (POM_object) dElem.get(0);
            did2[0].isPreciseLink = true;
            didList2.add(did2[0]);
            SearchForLinkedInstances2Response resp0 = mdoTestUtils.searchForLinkedDesignInstances2(didList2);

            if (resp0!=null && resp0.output.length > 0 && resp0.output[0].linkedInstances.length > 0 )
            {
                ImpactedDesignInstanceInfo2[] diInfos = resp0.output[0].linkedInstances[0].instancesInfo;
                if ( diInfos.length == 5 )
                {
                    LogFile.write("Revised Design Instances are linked to the original MDI");
                }
                else
                {
                    LogFile.write("Revised Design Instances are not linked to the original MDI - failed");
                    ++failCnt;
                }
            }
            else
            {
                LogFile.write("searchForLinkedDesignInstances failed");
                ++failCnt;
            }
            boolean testPassed = true;
            // negative case, try to split non-latest revisions
            WorkspaceObject[] nonLatestRevs = new WorkspaceObject[2];
            nonLatestRevs[0] = (WorkspaceObject) dElem.get(0);
            nonLatestRevs[1] = (WorkspaceObject) dElem.get(1);
            SplitDesignInstancesResponse resp1 = mdoTestUtils.splitDesignInstances( nonLatestRevs, context );
            if ( resp1.serviceData.sizeOfPartialErrors() == 1 ) // MDO_design_instances_are_not_latest_rev
            {
                LogFile.write( "splitDesignInstances correct error MDO_design_instances_are_not_latest_rev" );
            }
            else
            {
                testPassed = false;
            }
            // negative case, try to split non-related revisions
            WorkspaceObject[] nonRelatedRevs = new WorkspaceObject[2];
            nonRelatedRevs[0] = (WorkspaceObject) revisedDElem1;
            nonRelatedRevs[1] = (WorkspaceObject) dElemOther.get(0);
            SplitDesignInstancesResponse resp2 = mdoTestUtils.splitDesignInstances( nonRelatedRevs, context );
            if ( resp2.serviceData.sizeOfPartialErrors() == 1 ) // MDO_design_instances_not_all_linked_same_mdis
            {
                LogFile.write( "splitDesignInstances correct error MDO_design_instances_not_all_linked_same_mdis" );
            }
            else
            {
                testPassed = false;
            }
            // split latest revisions
            WorkspaceObject[] latestRevs = new WorkspaceObject[2];
            latestRevs[0] = (WorkspaceObject) revisedDElem1;
            latestRevs[1] = (WorkspaceObject) revisedDElem2;
            SplitDesignInstancesResponse resp3 = mdoTestUtils.splitDesignInstances( latestRevs, context );
            if ( resp3.serviceData.sizeOfPartialErrors() == 0 &&
                    resp3.output.length == 1 &&
                    resp3.output[0].isSuccess &&
                    resp3.output[0].newMDIObject != null &&
                    resp3.output[0].updatedMDIObjects.length == 1
                    )
            {
                LogFile.write( "splitDesignInstances passed" );
            }
            else
            {
                testPassed = false;
            }
            // should have only 4 DIs on original MDI
            SearchForLinkedInstances2Response resp4 = mdoTestUtils.searchForLinkedDesignInstances2(didList2);
            mdoTestUtils.printSearchForLinkedInstances2Response(resp4);

            if (resp4!=null && resp4.output.length > 0 )
            {
                ImpactedDesignInstanceInfo2[] diInfos = resp4.output[0].linkedInstances[0].instancesInfo;
                if ( diInfos.length == 3 )
                {
                    LogFile.write(" Revised Design Instances are unlinked from the original MDI");
                }
                else
                {
                    LogFile.write(" Revised Design Instances are not unlinked from the original MDI - failed");
                    ++failCnt;
                }
            }
            else
            {
                LogFile.write(" searchForLinkedDesignInstances failed");
                ++failCnt;
            }
            // should have 2 DIs on new MDI in context, 6 DIs on MDI in context2
            List<DesignInstancesData> didList3 = new ArrayList<DesignInstancesData>();
            DesignInstancesData did3[] = new DesignInstancesData[1];
            did3[0] = new DesignInstancesData();
            did3[0].designInstance = revisedDElem1;
            did3[0].isPreciseLink = true;
            didList3.add(did3[0]);
            SearchForLinkedInstances2Response resp5 = mdoTestUtils.searchForLinkedDesignInstances2(didList3);
            mdoTestUtils.printSearchForLinkedInstances2Response(resp5);

            if (resp5!=null && resp5.output.length > 0 && resp5.output[0].linkedInstances.length > 1 )
            {
                int diCntA = resp5.output[0].linkedInstances[0].instancesInfo.length;
                int diCntB = resp5.output[0].linkedInstances[1].instancesInfo.length;
                if ( (diCntA == 1 && diCntB == 5) || (diCntA == 5 && diCntB == 1) )
                {
                    LogFile.write(" Revised Design Instances are linked to the new MDI");
                }
                else
                {
                    LogFile.write(" Revised Design Instances are not linked to the new MDI - failed");
                    ++failCnt;
                }
            }
            else
            {
                LogFile.write(" searchForLinkedDesignInstances failed");
                ++failCnt;
            }
            if ( testPassed )
            {
                LogFile.write( "executeSplitDesignInstance succeeded" );
            }
            else
            {
                LogFile.write( "executeSplitDesignInstance failed" );
                ++failCnt;
            }
        }
        catch ( Exception ex )
        {
            LogFile.write( "executeSplitDesignInstance failed" );
            ++failCnt;
            ex.printStackTrace();
        }
    }

    /**
     * Tests splitting Library Element from multiple MDThreads
     *
     */
    public static void executeSplitLibraryElementFromMDTs()
    {
        MDOTestUtils mdoTestUtils = new MDOTestUtils();
        try
        {
            // create data for library creation.
            String[] disciplines = new String[2];
            disciplines[0] = "Mechanical";
            disciplines[1] = "Electrical";

            String[] allowedMemberTypes = new String[2];
            allowedMemberTypes[0]="Item";
            allowedMemberTypes[1]="ItemRevision";

            int numLibrariesToCreate = 1;
            Lbr0Library[] createdLibraries = mdoTestUtils.createLbr0Librararies( "ST02Library", disciplines, allowedMemberTypes, numLibrariesToCreate );

            // one Hierarchy and and one General Node
            CreateHierarchiesResponse hierarchyResponse;
            hierarchyResponse = mdoTestUtils.createHierarchy(createdLibraries[0], "ST02Hierarchy" );

            CreateNodesResponse nodeResponse;
            nodeResponse = mdoTestUtils.createLbrHierarchyGenralNode ( hierarchyResponse.output[0].hierarchy, null, "Lbr0_pub_Node2" );

            // Create 4 Items  daForLE1-4
            CreateItemsResponse response = mdoTestUtils.createDesignArtifactsWithType("daForLE1", "Item", "Description for daForLE1" );
            ItemRevision daForLE1Rev = response.output[0].itemRev;

            response = mdoTestUtils.createDesignArtifactsWithType("daForLE2", "Item", "Description for daForLE2" );
            ItemRevision daForLE2Rev = response.output[0].itemRev;

            response = mdoTestUtils.createDesignArtifactsWithType("daForLE3", "Item", "Description for daForLE3" );
            ItemRevision daForLE3Rev = response.output[0].itemRev;

            response = mdoTestUtils.createDesignArtifactsWithType("daForLE4", "Item", "Description for daForLE4" );
            ItemRevision daForLE4Rev = response.output[0].itemRev;

            response = mdoTestUtils.createDesignArtifactsWithType("daForOtherLE", "Item", "Description for daForOtherLE" );
            ItemRevision daForOtherLERev = response.output[0].itemRev;

            BusinessObject[] itemList1 = new BusinessObject[1];
            itemList1[0] = daForLE1Rev;
            PublishObjectsResponse publishedResponse = mdoTestUtils.publishLibraryObjects( nodeResponse.output[0].node, itemList1 , "le1", 1 );
            WorkspaceObject le1 = (WorkspaceObject) publishedResponse.serviceData.getCreatedObject(0);

            BusinessObject[] itemList2 = new BusinessObject[1];
            itemList2[0] = daForLE2Rev;
            PublishObjectsResponse publishedResponse2 = mdoTestUtils.publishLibraryObjects( nodeResponse.output[0].node, itemList2 , "le2", 1 );
            WorkspaceObject le2 = (WorkspaceObject) publishedResponse2.serviceData.getCreatedObject(0);

            BusinessObject[] itemList3 = new BusinessObject[1];
            itemList3[0] = daForLE3Rev;
            PublishObjectsResponse publishedResponse3 = mdoTestUtils.publishLibraryObjects( nodeResponse.output[0].node, itemList3 , "le3", 1 );
            publishedResponse3.serviceData.getCreatedObject(0);

            BusinessObject[] itemList4 = new BusinessObject[1];
            itemList4[0] = daForLE4Rev;
            PublishObjectsResponse publishedResponse4 = mdoTestUtils.publishLibraryObjects( nodeResponse.output[0].node, itemList4 , "le4", 1 );
            publishedResponse4.serviceData.getCreatedObject(0);

            BusinessObject[] itemList5 = new BusinessObject[1];
            itemList5[0] = daForOtherLERev;
            mdoTestUtils.publishLibraryObjects( nodeResponse.output[0].node, itemList5 , "otherLE", 1 );
            WorkspaceObject otherLE = (WorkspaceObject) publishedResponse4.serviceData.getCreatedObject(0);

            //   Create MDThread - MDOForLE using library element
            List<WorkspaceObject> leForMdo = new ArrayList<WorkspaceObject>();
            leForMdo.add(le1);
            leForMdo.add(le2);
            WorkspaceObject mdoForLE = mdoTestUtils.createMDOWithDAList(leForMdo, "mdoForLE", "mdoForLE for revise of library elements test");
            if ( mdoForLE == null )
            {
                LogFile.write("Error creating mdoForLE");
            }
            WorkspaceObject mdoForLE2 = mdoTestUtils.createMDOWithDAList(leForMdo, "mdoForLE2", "mdoForLE2 for revise of library elements test");
            if ( mdoForLE2 == null )
            {
                LogFile.write("Error creating mdoForLE2");
            }

            // TODO revise le1 and le2


            boolean testPassed = true;
            // negative case, try to split non-latest revisions
            /* TODO after the above revise is working uncomment this code
            WorkspaceObject[] nonLatestRevs = new WorkspaceObject[2];
            nonLatestRevs[0] = le1;
            nonLatestRevs[1] = le2;
            SplitDesignArtifactsResponse resp1 = mdoTestUtils.splitDesignArtifacts( nonLatestRevs );
            if ( resp1.serviceData.sizeOfPartialErrors() == 1 ) // MDO_design_artifacts_are_not_latest_rev
            {
                LogFile.write( "splitLibraryElements has correct error MDO_design_artifacts_are_not_latest_rev" );
            }
            else
            {
                testPassed = false;
                LogFile.write( "splitLibraryElements did not error with MDO_design_artifacts_are_not_latest_rev" );
            }
            */
            // negative case, try to split non-related revisions
            WorkspaceObject[] nonRelatedRevs = new WorkspaceObject[2];
            nonRelatedRevs[0] = le1; // TODO should be revisedLe1
            nonRelatedRevs[1] = otherLE;
            SplitDesignArtifactsResponse resp2 = mdoTestUtils.splitDesignArtifacts( nonRelatedRevs );
            if ( resp2.serviceData.sizeOfPartialErrors() == 1 ) // MDO_no_mdthreads_found
            {
                LogFile.write( "splitLibraryElements has correct error MDO_no_mdthreads_found" );
            }
            else
            {
                testPassed = false;
                LogFile.write( "splitLibraryElements did not error with MDO_no_mdthreads_found" );
            }
            // split latest revisions
            /* TODO
            WorkspaceObject[] latestRevs = new WorkspaceObject[2];
            latestRevs[0] = le1; // TODO should be revisedLe1
            latestRevs[1] = le2; // TODO should be revisedLe2
            SplitDesignArtifactsResponse resp3 = mdoTestUtils.splitDesignArtifacts( latestRevs );
            if ( resp3.serviceData.sizeOfPartialErrors() == 0 &&
                    resp3.output.length == 1 &&
                    resp3.output[0].isSuccess &&
                    resp3.output[0].newMDTObject != null &&
                    resp3.output[0].updatedMDTObjects.length == 2
                    )
            {
                LogFile.write( "splitLibraryElements passed" );
            }
            else
            {
                testPassed = false;
            }
            */
            if ( testPassed )
            {
                LogFile.write( "executeSplitLibraryElement succeeded" );
            }
            else
            {
                LogFile.write( "executeSplitLibraryElement failed" );
                ++failCnt;
            }
        }
        catch ( Exception ex )
        {
            LogFile.write( "executeSplitLibraryElement failed" );
            ++failCnt;
            ex.printStackTrace();
        }
    }
    public static void executeDomainRelevancyCases()
    {
        MDOTestUtils mdoTestUtils = new MDOTestUtils();
        List< Item > itemList = new ArrayList< Item >();
        List< ItemRevision > revList = new ArrayList< ItemRevision >();
        List< WorkspaceObject > dElem = new ArrayList< WorkspaceObject > ();


        try
        {
            mdoTestUtils.createItems( 4, "DomainRel", "Item", itemList, revList );

            //Creating Project
            POM_object context = mdoTestUtils.createProject( "DomainProj3", "mdoProjectDesc" );
            mdoTestUtils.setProjectAsMultiDisciplinary(context);

            //Creating Collaborative design
            ModelObject cdObject = mdoTestUtils.createCD("CDTest");
            ModelObject[] collaborativeDesigns = new ModelObject[1];
            collaborativeDesigns[0] = cdObject;
            mdoTestUtils.assignCDsToProject(collaborativeDesigns, context);
            //Creating Design Elements
            dElem.add( (WorkspaceObject) mdoTestUtils.createReuseDEWithCD( revList.get( 0 ), "DomainRelDE_1", cdObject ) );
            dElem.add( (WorkspaceObject) mdoTestUtils.createReuseDEWithCD( revList.get( 1 ), "DomainRelDE_2", cdObject ) );
            dElem.add( (WorkspaceObject) mdoTestUtils.createReuseDEWithCD( revList.get( 2 ), "DomainRelDE_3", cdObject ) );
            dElem.add( (WorkspaceObject) mdoTestUtils.createReuseDEWithCD( revList.get( 3 ), "DomainRelDE_4", cdObject ) );

            //Add domain relevancy information

            /// ----------------------------------------
            // Add DA1 Rev as  relevant for Automation
            // With DE1   as irrelevant for Automation
            // Add DA2 Item as relevant for Automation, Mechanical
            // With DE2   as irrelevant for Mechanical
            // Add DA3 Rev as  relevant for Automation and irrelevant for Mechanical
            // Add DA4 Rev as  relevant for Automation
            // With DE4     as relevant for Mechanical

            DomainRelevancyInput[] inputs1 = new DomainRelevancyInput[7];

            //Input 1 for DA1 Rev
            inputs1[0] = new DomainRelevancyInput();
            inputs1[0].clientId = "AddRemoveDomainRelForInput1";
            inputs1[0].designArtifact = revList.get( 0 );

            String[] addRelDom1 = new String[1];
            addRelDom1[0] = "Automation";

            RelevancyInformation addInfo1 = new RelevancyInformation();
            addInfo1.relevantDomain = addRelDom1;

            inputs1[0].addInformation = addInfo1;

            //Input 2 for DA2 Item

            inputs1[1] = new DomainRelevancyInput();
            inputs1[1].clientId = "AddRemoveDomainRelForInput2";
            inputs1[1].designArtifact = itemList.get(1);

            String[] addRelDom2 = new String[2];
            addRelDom2[0] = "Automation";
            addRelDom2[1] = "Mechanical";

            RelevancyInformation addInfo2 = new RelevancyInformation();
            addInfo2.relevantDomain = addRelDom2;

            inputs1[1].addInformation = addInfo2;

            //Input 3 for DA3 Rev

            inputs1[2] = new DomainRelevancyInput();
            inputs1[2].clientId = "AddRemoveDomainRelForInput3";
            inputs1[2].designArtifact = revList.get( 2 );

            String[] addRelDom3 = new String[1];
            addRelDom3[0] = "Automation";

            String[] addIrRelDom3 = new String[1];
            addIrRelDom3[0] = "Mechanical";

            RelevancyInformation addInfo3 = new RelevancyInformation();
            addInfo3.relevantDomain = addRelDom3;
            addInfo3.irrelevantDomain = addIrRelDom3;

            inputs1[2].addInformation = addInfo3;

            //Input 4 for DA4 Rev

            inputs1[3] = new DomainRelevancyInput();
            inputs1[3].clientId = "AddRemoveDomainRelForInput4";
            inputs1[3].designArtifact = revList.get( 3 );

            String[] addRelDom4 = new String[1];
            addRelDom4[0] = "Automation";

            RelevancyInformation addInfo4 = new RelevancyInformation();
            addInfo4.relevantDomain = addRelDom4;

            inputs1[3].addInformation = addInfo4;

            //Input 5 for DE4

            inputs1[4] = new DomainRelevancyInput();
            inputs1[4].clientId = "AddRemoveDomainRelForInput5";
            inputs1[4].designArtifact = (WorkspaceObject) dElem.get( 3 );

            String[] addRelDom5 = new String[1];
            addRelDom5[0] = "Mechanical";

            RelevancyInformation addInfo5 = new RelevancyInformation();
            addInfo5.relevantDomain = addRelDom5;

            inputs1[4].addInformation = addInfo5;

            //Input 6 for DE1

            inputs1[5] = new DomainRelevancyInput();
            inputs1[5].clientId = "AddRemoveDomainRelForInput6";
            inputs1[5].designArtifact = (WorkspaceObject) dElem.get(0);

            String[] addIrrDom6 = new String[1];
            addIrrDom6[0] = "Automation";

            RelevancyInformation addInfo6 = new RelevancyInformation();
            addInfo6.irrelevantDomain = addIrrDom6;

            inputs1[5].addInformation = addInfo6;

            //Input 7 for DE2

            inputs1[6] = new DomainRelevancyInput();
            inputs1[6].clientId = "AddRemoveDomainRelForInput7";
            inputs1[6].designArtifact = (WorkspaceObject) dElem.get(1);

            String[] addIrrDom7 = new String[1];
            addIrrDom7[0] = "Mechanical";

            RelevancyInformation addInfo7 = new RelevancyInformation();
            addInfo7.irrelevantDomain = addIrrDom7;

            inputs1[6].addInformation = addInfo7;

            // update domain relevancy on designs
            mdoTestUtils.updateDomainRelevancy( inputs1 );

            GetDomainRelevancyInput[] inputs = new GetDomainRelevancyInput[1];
            inputs[0] = new GetDomainRelevancyInput();

            // check Automation relevancy for DE1 instance
            inputs[0].clientId = "GetDomainRelSingle";
            inputs[0].inputObject =(WorkspaceObject) dElem.get(0);
            inputs[0].domain = "Automation";

            GetDomainRelevancyOfAnObjectResp response0 = mdoTestUtils.getDomainRelevancy( inputs );
            if ( response0.serviceData.sizeOfPartialErrors() == 0 &&
                 response0.output.length == 1 &&
                 response0.output[0].relevancyValue.equals("irrelevant") &&
                 response0.output[0].isDesignArtifactBased == false
                )
            {
                LogFile.write( "getDomainRelevancy Automation is irrelevant for DE1 succeeded" );
            }
            else
            {
                LogFile.write( "getDomainRelevancy Automation is irrelevant for DE1 failed" );
                ++failCnt;
            }

            // check Automation relevancy for DA3 Rev
            inputs[0].inputObject = revList.get( 2 );
            inputs[0].domain = "Automation";

            GetDomainRelevancyOfAnObjectResp response1 = mdoTestUtils.getDomainRelevancy( inputs );
            if ( response1.serviceData.sizeOfPartialErrors() == 0 &&
                 response1.output.length == 1 &&
                 response1.output[0].relevancyValue.equals("relevant") &&
                 response1.output[0].isDesignArtifactBased
                )
            {
                LogFile.write( "getDomainRelevancy Automation is relevant for DA3 Rev succeeded" );
            }
            else
            {
                LogFile.write( "getDomainRelevancy Automation is relevant for DA3 Rev failed" );
                ++failCnt;
            }

            // check Automation relevancy for DA4 Rev
            inputs[0].inputObject = revList.get( 3 );
            inputs[0].domain = "Automation";

            GetDomainRelevancyOfAnObjectResp response2 = mdoTestUtils.getDomainRelevancy( inputs );
            if ( response2.serviceData.sizeOfPartialErrors() == 0 &&
                 response2.output.length == 1 &&
                 response2.output[0].relevancyValue.equals("relevant") &&
                 response2.output[0].isDesignArtifactBased
                )
            {
                LogFile.write( "getDomainRelevancy Automation is relevant for DA4 Rev succeeded" );
            }
            else
            {
                LogFile.write( "getDomainRelevancy Automation is relevant for DA4 Rev failed" );
                ++failCnt;
            }

            // check Mechanical relevancy for DE4 instance
            inputs[0].inputObject = (WorkspaceObject) dElem.get(3);
            inputs[0].domain = "Mechanical";

            GetDomainRelevancyOfAnObjectResp response3 = mdoTestUtils.getDomainRelevancy( inputs );
            if ( response3.serviceData.sizeOfPartialErrors() == 0 &&
                 response3.output.length == 1 &&
                 response3.output[0].relevancyValue.equals("relevant") &&
                 response3.output[0].isDesignArtifactBased == false
                )
            {
                LogFile.write( "getDomainRelevancy Mechanical is relevant for DE4 succeeded" );
            }
            else
            {
                LogFile.write( "getDomainRelevancy Mechanical is relevant for DE4 failed" );
                ++failCnt;
            }

            // check all relevancy for DA4 Rev
            inputs[0].inputObject = revList.get( 3 );
            inputs[0].domain = "";

            GetDomainRelevancyOfAnObjectResp response4 = mdoTestUtils.getDomainRelevancy( inputs );
            if ( response4.serviceData.sizeOfPartialErrors() == 0 &&
                 response4.output.length == 1 &&
                 response4.output[0].domainRelevancy.relevantDomain.length == 1 &&
                 response4.output[0].domainRelevancy.relevantDomain[0].equals("Automation") &&
                 response4.output[0].domainRelevancy.irrelevantDomain.length == 0
                )
            {
                LogFile.write( "getDomainRelevancy all relevancy for DA4 Rev succeeded" );
            }
            else
            {
                LogFile.write( "getDomainRelevancy all relevancy for DA4 Rev failed" );
                ++failCnt;
            }

            // check all relevancy for DE4 instance
            inputs[0].inputObject = (WorkspaceObject) dElem.get(3);
            inputs[0].domain = "";

            GetDomainRelevancyOfAnObjectResp response5 = mdoTestUtils.getDomainRelevancy( inputs );
            if ( response5.serviceData.sizeOfPartialErrors() == 0 &&
                 response5.output.length == 1 &&
                 response5.output[0].domainRelevancy.relevantDomain.length == 2 &&
                 ( response5.output[0].domainRelevancy.relevantDomain[0].equals("Automation") &&
                     response5.output[0].domainRelevancy.relevantDomain[1].equals("Mechanical") ||
                   response5.output[0].domainRelevancy.relevantDomain[0].equals("Mechanical") &&
                     response5.output[0].domainRelevancy.relevantDomain[1].equals("Automation") ) &&
                 response5.output[0].domainRelevancy.irrelevantDomain.length == 0
                )
            {
                LogFile.write( "getDomainRelevancy all relevancy for DE4 succeeded" );
            }
            else
            {
                LogFile.write( "getDomainRelevancy all relevancy for DE4 failed" );
                ++failCnt;
            }

            // check Mechanical relevancy for DE1 instance
            inputs[0].clientId = "GetDomainRelSingle";
            inputs[0].inputObject = (WorkspaceObject) dElem.get(0);
            inputs[0].domain = "Mechanical";

            GetDomainRelevancyOfAnObjectResp response6 = mdoTestUtils.getDomainRelevancy( inputs );
            if ( response6.serviceData.sizeOfPartialErrors() == 0 &&
                 response6.output.length == 1 &&
                 response6.output[0].relevancyValue.equals("")
                )
            {
                LogFile.write( "getDomainRelevancy Mechanical is relevant for DE4 succeeded" );
            }
            else
            {
                LogFile.write( "getDomainRelevancy Mechanical is relevant for DE4 failed" );
                ++failCnt;
            }

            // check all relevancy for DE1 instance
            inputs[0].inputObject = (WorkspaceObject) dElem.get(0);
            inputs[0].domain = "";

            GetDomainRelevancyOfAnObjectResp response7 = mdoTestUtils.getDomainRelevancy( inputs );
            if ( response7.serviceData.sizeOfPartialErrors() == 0 &&
                 response7.output.length == 1 &&
                 response7.output[0].domainRelevancy.irrelevantDomain.length == 1 &&
                 response7.output[0].domainRelevancy.irrelevantDomain[0].equals("Automation")
                )
            {
                LogFile.write( "getDomainRelevancy all relevancy for DE1 succeeded" );
            }
            else
            {
                LogFile.write( "getDomainRelevancy all relevancy for DE1 failed" );
                ++failCnt;
            }

            // INPUT instance, domain A ->   relevant for domain A + Is output from design artifact or instance
            // INPUT instance, domain C -> irrelevant for domain C + Is output from design artifact or instance
            // INPUT instance, no specific domain -> relevant for domain A, irrelevant for domain C (All domain relevancy is returned)
            // Domain A = Automation, Domain C = Mechanical, instance = DE1, artifact = DA1

            // check Automation relevancy for DE2 instance
            inputs[0].clientId = "GetDomainRelSingle";
            inputs[0].inputObject = (WorkspaceObject) dElem.get(1);
            inputs[0].domain = "Automation";

            GetDomainRelevancyOfAnObjectResp response8 = mdoTestUtils.getDomainRelevancy( inputs );
            if ( response8.serviceData.sizeOfPartialErrors() == 0 &&
                 response8.output.length == 1 &&
                 response8.output[0].relevancyValue.equals("relevant") &&
                 response8.output[0].isDesignArtifactBased
                )
            {
                LogFile.write( "getDomainRelevancy check Automation is relevant for DE2 succeeded" );
            }
            else
            {
                LogFile.write( "getDomainRelevancy check Automation is relevant for DE2 failed" );
                ++failCnt;
            }

            // check Mechanical relevancy for DE2 instance
            inputs[0].clientId = "GetDomainRelSingle";
            inputs[0].inputObject = (WorkspaceObject) dElem.get(1);
            inputs[0].domain = "Mechanical";

            GetDomainRelevancyOfAnObjectResp response9 = mdoTestUtils.getDomainRelevancy( inputs );
            if ( response9.serviceData.sizeOfPartialErrors() == 0 &&
                 response9.output.length == 1 &&
                 response9.output[0].relevancyValue.equals("irrelevant") &&
                 response9.output[0].isDesignArtifactBased == false
                )
            {
                LogFile.write( "getDomainRelevancy check Mechanical is irrelevant for DE2 succeeded" );
            }
            else
            {
                LogFile.write( "getDomainRelevancy check Mechanical is irrelevant for DE2 failed" );
                ++failCnt;
            }

            // check all relevancy for DE2 instance
            inputs[0].inputObject = (WorkspaceObject) dElem.get(1);
            inputs[0].domain = "";

            GetDomainRelevancyOfAnObjectResp resp10 = mdoTestUtils.getDomainRelevancy( inputs );
            if ( resp10.serviceData.sizeOfPartialErrors() == 0 &&
                 resp10.output.length == 1 &&
                 resp10.output[0].domainRelevancy.relevantDomain.length == 1 &&
                 resp10.output[0].domainRelevancy.relevantDomain[0].equals("Automation") &&
                 resp10.output[0].domainRelevancy.irrelevantDomain.length == 1 &&
                 resp10.output[0].domainRelevancy.irrelevantDomain[0].equals("Mechanical")
                )
            {
                LogFile.write( "getDomainRelevancy check Automation/Mechanical is relevant/irrelevant for DE2 succeeded" );
            }
            else
            {
                LogFile.write( "getDomainRelevancy check Automation/Mechanical is relevant/irrelevant for DE2 failed" );
                ++failCnt;
            }

            LogFile.write( "executeDomainRelevancyCases succeeded" );
        }
        catch ( Exception ex )
        {
            LogFile.write( "executeDomainRelevancyCases failed" );
            ++failCnt;
            ex.printStackTrace();
        }
    }

    public static void executeDomainRelevancyWithECN()
    {
        MDOTestUtils mdoTestUtils = new MDOTestUtils();
        List< Item > itemList = new ArrayList< Item >();
        List< ItemRevision > revList = new ArrayList< ItemRevision >();
        List< WorkspaceObject > dElem = new ArrayList< WorkspaceObject > ();


        try
        {
            //Create ECN
            ModelObject ecnRev1 = mdoTestUtils.createECN( "ECN_DomainRel", "ECM desc" );
            mdoTestUtils.submitECNToChangeWorkflow(ecnRev1);

            mdoTestUtils.createItems( 1, "DomainRel", "Item", itemList, revList );

            //Creating Project
            POM_object context = mdoTestUtils.createProject( "DomainProj3", "mdoProjectDesc" );
            mdoTestUtils.setProjectAsMultiDisciplinary(context);

            //Creating Collaborative design
            ModelObject cdObject = mdoTestUtils.createCD("CDTest");
            ModelObject[] collaborativeDesigns = new ModelObject[1];
            collaborativeDesigns[0] = cdObject;
            mdoTestUtils.assignCDsToProject(collaborativeDesigns, context);
            //Creating Design Elements
            dElem.add( (WorkspaceObject) mdoTestUtils.createReuseDEWithCDInECN( revList.get( 0 ), "DomainRelDE_1", cdObject, ecnRev1 ) );

            //Add domain relevancy information

            /// ----------------------------------------
            // Add DA1 Rev as  relevant for Automation
            // Add DE1   as irrelevant for Automation

            DomainRelevancyInput[] inputs1 = new DomainRelevancyInput[2];

            //Input 1 for DA1 Rev
            inputs1[0] = new DomainRelevancyInput();
            inputs1[0].clientId = "AddRemoveDomainRelForInput1";
            inputs1[0].designArtifact = revList.get( 0 );

            String[] addRelDom1 = new String[1];
            addRelDom1[0] = "Automation";

            RelevancyInformation addInfo1 = new RelevancyInformation();
            addInfo1.relevantDomain = addRelDom1;

            inputs1[0].addInformation = addInfo1;

            //Input 2 for DE1

            inputs1[1] = new DomainRelevancyInput();
            inputs1[1].clientId = "AddRemoveDomainRelForInput6";
            inputs1[1].designArtifact = (WorkspaceObject) dElem.get(0);

            String[] addIrrDom6 = new String[1];
            addIrrDom6[0] = "Automation";

            RelevancyInformation addInfo6 = new RelevancyInformation();
            addInfo6.irrelevantDomain = addIrrDom6;

            inputs1[1].addInformation = addInfo6;

            // update domain relevancy on designs
            mdoTestUtils.updateDomainRelevancy( inputs1 );

            GetDomainRelevancyInput[] inputs = new GetDomainRelevancyInput[1];
            inputs[0] = new GetDomainRelevancyInput();

            // check Automation relevancy for DE1 instance with ECN
            inputs[0].clientId = "GetDomainRelSingle";
            inputs[0].inputObject =(WorkspaceObject) dElem.get(0);
            inputs[0].domain = "Automation";

            GetDomainRelevancyOfAnObjectResp response0 = mdoTestUtils.getDomainRelevancy( inputs );
            if ( response0.serviceData.sizeOfPartialErrors() == 0 &&
                 response0.output.length == 1 &&
                 response0.output[0].relevancyValue.equals("irrelevant") &&
                 response0.output[0].isDesignArtifactBased == false
                )
            {
                LogFile.write( "getDomainRelevancy Automation is irrelevant for DE1 with ECN passed" );
            }
            else
            {
                LogFile.write( "getDomainRelevancy Automation is irrelevant for DE1 with ECN failed" );
                ++failCnt;
            }


            // check Mechanical relevancy for DE1 instance
            inputs[0].clientId = "GetDomainRelSingle";
            inputs[0].inputObject = (WorkspaceObject) dElem.get(0);
            inputs[0].domain = "Mechanical";

            GetDomainRelevancyOfAnObjectResp response6 = mdoTestUtils.getDomainRelevancy( inputs );
            if ( response6.serviceData.sizeOfPartialErrors() == 0 &&
                 response6.output.length == 1 &&
                 response6.output[0].relevancyValue.equals("")
                )
            {
                LogFile.write( "getDomainRelevancy Mechanical is relevant for DE1 succeeded" );
            }
            else
            {
                LogFile.write( "getDomainRelevancy Mechanical is relevant for DE1 failed" );
                ++failCnt;
            }

            // check all relevancy for DE1 instance
            inputs[0].inputObject = (WorkspaceObject) dElem.get(0);
            inputs[0].domain = "";

            GetDomainRelevancyOfAnObjectResp response7 = mdoTestUtils.getDomainRelevancy( inputs );
            if ( response7.serviceData.sizeOfPartialErrors() == 0 &&
                 response7.output.length == 1 &&
                 response7.output[0].domainRelevancy.irrelevantDomain.length == 1 &&
                 response7.output[0].domainRelevancy.irrelevantDomain[0].equals("Automation")
                )
            {
                LogFile.write( "getDomainRelevancy all relevancy for DE1 with ECN passed" );
            }
            else
            {
                LogFile.write( "getDomainRelevancy all relevancy for DE1 with ECN failed" );
                ++failCnt;
            }

            //Remove domain relevancy information

            /// ----------------------------------------
            // Remove DA1 Rev as  relevant for Automation
            // Remove DE1   as irrelevant for Automation

            DomainRelevancyInput[] inputs2 = new DomainRelevancyInput[2];

            //Input 1 for DA1 Rev
            inputs2[0] = new DomainRelevancyInput();
            inputs2[0].clientId = "AddRemoveDomainRelForInput1";
            inputs2[0].designArtifact = revList.get( 0 );

            String[] remRelDom1 = new String[1];
            remRelDom1[0] = "Automation";

            RelevancyInformation remInfo1 = new RelevancyInformation();
            remInfo1.relevantDomain = remRelDom1;

            inputs2[0].removeInformation = remInfo1;

            //Input 2 for DE1

            inputs2[1] = new DomainRelevancyInput();
            inputs2[1].clientId = "AddRemoveDomainRelForInput6";
            inputs2[1].designArtifact = (WorkspaceObject) dElem.get(0);

            String[] remIrrDom6 = new String[1];
            remIrrDom6[0] = "Automation";

            RelevancyInformation remInfo6 = new RelevancyInformation();
            remInfo6.irrelevantDomain = remIrrDom6;

            inputs2[1].removeInformation = remInfo6;

            // update domain relevancy on designs
            mdoTestUtils.updateDomainRelevancy( inputs2 );

            GetDomainRelevancyInput[] inputs3 = new GetDomainRelevancyInput[1];
            inputs3[0] = new GetDomainRelevancyInput();

            // check all relevancy for DE1 instance
            inputs3[0].inputObject = (WorkspaceObject) dElem.get(0);
            inputs3[0].domain = "";

            GetDomainRelevancyOfAnObjectResp response8 = mdoTestUtils.getDomainRelevancy( inputs3 );
            if ( response8.serviceData.sizeOfPartialErrors() == 0 &&
                 response8.output.length == 1 &&
                 response8.output[0].domainRelevancy.irrelevantDomain.length == 0 &&
                 response8.output[0].domainRelevancy.relevantDomain.length == 0 )
            {
                LogFile.write( "getDomainRelevancy remove all relevancy for DE1 with ECN passed" );
            }
            else
            {
                LogFile.write( "getDomainRelevancy remove all relevancy for DE1 with ECN failed" );
                ++failCnt;
            }

            LogFile.write( "executeDomainRelevancyWithECN succeeded" );
        }
        catch ( Exception ex )
        {
            LogFile.write( "executeDomainRelevancyWithECN failed" );
            ++failCnt;
            ex.printStackTrace();
        }
    }

}
