// Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
// ==================================================
// Copyright 2018.
// Siemens Product Lifecycle Management Software Inc.
// All Rights Reserved.
// ==================================================
// Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.

package com.teamcenter.cpdsearch;

import java.util.ArrayList;

import com.teamcenter.clientx.AppXSession;
import com.cpd0.services.strong.cpdcore._2011_06.DataManagement.CompoundCreateInfo;
import com.cpd0.services.strong.cpdcore._2011_06.DataManagement.CompoundCreateInput;
import com.cpd0.services.strong.cpdcore._2011_06.DataManagement.CreateOrUpdateDesignElementsInfo;
import com.cpd0.services.strong.cpdcore._2011_06.DataManagement.CreateOrUpdateDesignElementsResponse;
import com.cpd0.services.strong.cpdcore._2011_06.DataManagement.PromissoryDesignElementInfo;
import com.cpd0.services.strong.cpdcore._2011_06.DataManagement.ShapeDesignElementInfo;
import com.mdl0.services.strong.modelcore.SearchService;
import com.mdl0.services.strong.modelcore._2011_06.Search.BoxZoneExpression;
import com.mdl0.services.strong.modelcore._2011_06.Search.BoxZoneExpressionInput;
import com.mdl0.services.strong.modelcore._2011_06.Search.ExpressionResponse;
import com.mdl0.services.strong.modelcore._2011_06.Search.RecipeData;
import com.mdl0.services.strong.modelcore._2011_06.Search.SavedQueryExpression;
import com.mdl0.services.strong.modelcore._2011_06.Search.SavedQueryExpressionInput;
import com.mdl0.services.strong.modelcore._2011_06.Search.SearchBox;
import com.mdl0.services.strong.modelcore._2011_06.Search.SearchExpression;
import com.mdl0.services.strong.modelcore._2011_06.Search.SearchExpressionInput;
import com.mdl0.services.strong.modelcore._2011_06.Search.SearchExpressionResponse;
import com.mdl0.services.strong.modelcore._2011_06.Search.SearchExpressionSet;
import com.mdl0.services.strong.modelcore._2011_06.Search.SearchOptions;
import com.mdl0.services.strong.modelcore._2011_06.Search.SearchRecipe;
import com.mdl0.services.strong.modelcore._2011_06.Search.SearchResponse;
import com.mdl0.services.strong.modelcore._2011_06.Search.SearchScope;
import com.ptn0.services.strong.partition.PartitionManagementService;
import com.ptn0.services.strong.partition._2011_06.PartitionManagement.UpdateMembersInputInfo;
import com.ptn0.services.strong.partition._2011_06.PartitionManagement.UpdateMembersResponse;
import com.ptn0.services.strong.partition._2012_09.Search.PartitionQueryExpression;
import com.ptn0.services.strong.partition._2012_09.Search.PartitionSearchExpressionInput;
import com.teamcenter.schemas.soa._2006_03.exceptions.ServiceException;
import com.teamcenter.services.strong.query.SavedQueryService;
import com.teamcenter.services.strong.query._2006_03.SavedQuery.GetSavedQueriesResponse;
import com.teamcenter.services.strong.core._2008_06.DataManagement.CreateIn;
import com.teamcenter.services.strong.core._2008_06.DataManagement.CreateInput;
import com.teamcenter.services.strong.core._2008_06.DataManagement.CreateResponse;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.client.model.ServiceData;
import com.teamcenter.soa.client.model.strong.Cpd0CollaborativeDesign;
import com.teamcenter.soa.client.model.strong.ImanQuery;
import com.teamcenter.soa.client.model.strong.ImanType;
import com.teamcenter.soa.client.model.strong.Mdl0ModelElement;
import com.teamcenter.soa.client.model.strong.Mdl0SearchDef;
import com.teamcenter.soa.client.model.strong.Cpd0DesignElement;
import com.teamcenter.soa.client.model.strong.Ptn0Partition;
import com.teamcenter.soa.client.model.strong.Ptn0PartitionScheme;
import com.teamcenter.soa.client.model.strong.User;
import com.teamcenter.soa.exceptions.NotLoadedException;

/**
 * This sample client application demonstrates some of the basic features of the
 * Teamcenter classification services.
 *
 * An instance of the Connection object is created with implementations of the
 * ExceptionHandler, PartialErrorListener, ChangeListener, and DeleteListeners
 * interfaces. This client application performs the following functions:
 * 1. Establishes a session with the Teamcenter server
 * 2. Perform the following tasks.
 *      Create 4GD Objects, CollaborativeDesign, Partitions Schemes, Partitions, DesignElements.
 *      Create SavedQuery & Boxzone Search Expressions & Search Definition.
 *      Perform Search with SavedQuery and BoxZone Search Expression.
 *
 */
public class PartitionSearchServiceSOASampleTest
{
    // Services
    private static SearchService m_searchService = null;
    private static com.ptn0.services.strong.partition.SearchService m_partitionSearchService = null;
    private static SavedQueryService m_savedQueryService = null;
    private static com.teamcenter.services.strong.core.DataManagementService m_coreDmService = null;
    private static com.cpd0.services.strong.cpdcore.DataManagementService m_cpdDmMgmtService = null;
    private static PartitionManagementService m_ptnMgmtService = null; 

    // Data
    // Is Data Created
    private static boolean dataCreated = false;
    
    private static Cpd0CollaborativeDesign m_cd = null;
    
    private static Ptn0PartitionScheme m_partitionScheme = null;
    private static Ptn0Partition m_partitionRecipeContainer = null;
    private static ArrayList<Ptn0Partition> m_inclPartitions = new ArrayList<Ptn0Partition>();
    
    private static ArrayList<Cpd0DesignElement> m_des = new ArrayList<Cpd0DesignElement>();    
    private static ArrayList<Cpd0DesignElement> m_inclDEs = new ArrayList<Cpd0DesignElement>();
    private static ArrayList<Cpd0DesignElement> m_member1DEs = new ArrayList<Cpd0DesignElement>();
    private static ArrayList<Cpd0DesignElement> m_member2DEs = new ArrayList<Cpd0DesignElement>();
    private static ArrayList<Cpd0DesignElement> m_member3DEs = new ArrayList<Cpd0DesignElement>();

    private static User m_loginUser = null;
    private static AppXSession   m_session = null;
    
    /**
     * @param args   -help or -h will print out a Usage statement
     */
    public static void main(String[] args)
    {
        if (args.length > 0)
        {
            if (args[0].equals("-help") || args[0].equals("-h"))
            {
                System.out.println("usage: java [-Dhost=http://server:port/tc] com.teamcenter.clientX.AppX");
                System.exit(0);
            }
        }
        
        // Get optional host information
        String serverHost = "http://localhost:7001/tc";
        String host = System.getProperty("host");
        if (host != null && host.length() > 0)
        {
            serverHost = host;
        }
        
        m_session = new AppXSession(serverHost);
        
        try 
        {     
            m_session.login();
            // Establish a session with the Teamcenter Server
            m_searchService = SearchService.getService(AppXSession.getConnection());
            m_savedQueryService = SavedQueryService.getService(AppXSession.getConnection());
            m_partitionSearchService = com.ptn0.services.strong.partition.SearchService.getService(AppXSession.getConnection());
            m_coreDmService = com.teamcenter.services.strong.core.DataManagementService.getService(AppXSession.getConnection());
            
            // Create Data
            createSampleData();
            
            executePartitionSearchExpressionWithBoxAndSavedQry( m_partitionRecipeContainer );
            
        }catch (Exception ex)
        {
           System.out.println( ex.getMessage());
           return ;
           
        }
        // Terminate the session with the Teamcenter server
        m_session.logout();

    }
    
    public static void createSampleData()
    {
        String clientId = "SampleSearch";
        m_cd = createCD( clientId, "SampleCD", m_coreDmService);
        try {
            createDEs(m_coreDmService, m_cpdDmMgmtService, m_ptnMgmtService );
        } catch (ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static Cpd0CollaborativeDesign createCD( String cliendId, String cdName, com.teamcenter.services.strong.core.DataManagementService coreDMService)
    {
        try
        {
            CreateIn createIn = new CreateIn();
            createIn.clientId = cliendId;
            createIn.data.boName = "Cpd0CollaborativeDesign";
            createIn.data.stringProps.put("object_name", cdName);
            String cdId = "Sample_CD";
            createIn.data.stringProps.put("mdl0model_id", cdId);
            CreateResponse resp = coreDMService.createObjects(new CreateIn[]{createIn});
            System.out.println("Cpd0CollaborativeDesign created : " + cdId );
            return (Cpd0CollaborativeDesign) resp.output[0].objects[0];
        }
        catch( Exception ex )
        {
            ex.printStackTrace();
        }
        return null;
    }
    
    public static ModelObject createPartitionScheme( ModelObject mdlObj,String schemeType, String objName, String objDesc, com.teamcenter.services.strong.core.DataManagementService dataManagementService) {
        CreateIn[] inputs = new CreateIn[1];
        inputs[0] = new CreateIn();
        inputs[0].data = new CreateInput();
        String uid = mdlObj.getUid().toString();
        inputs[0].data.boName = schemeType;
        inputs[0].data.tagProps.put("mdl0model_object", mdlObj);
        inputs[0].data.stringProps.put("object_name", objName);
        inputs[0].data.stringProps.put("object_desc", objDesc);
        CreateResponse response = null;
        try {
            response = dataManagementService.createObjects(inputs);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        System.out.println("END:");
        return response.output[0].objects[0];
    }
    
    public static void createDEs( com.teamcenter.services.strong.core.DataManagementService coreDMService, com.cpd0.services.strong.cpdcore.DataManagementService dataManagementService, PartitionManagementService ptnService  ) throws ServiceException 
    {
        if ( !dataCreated )
        { 
            m_cd = createCD("SampleTest", "SampleTestCD", coreDMService);
            
            m_partitionScheme = (Ptn0PartitionScheme) createPartitionScheme(m_cd, "Ptn0SchemeFunctional",
                            "TestScheme1_A37.1", "TestScheme1_A37.1 Description",
                            coreDMService);
            Ptn0Partition partition1 = (Ptn0Partition) createAdhocPartition(m_cd, m_partitionScheme,
                            "Ptn0Functional", "ParentPartnTest_A37",
                            "ParentPartnTest_A37 Desc", "Partition001",
                            coreDMService);
            Ptn0Partition partition2 = (Ptn0Partition) createAdhocPartition(m_cd, m_partitionScheme,
                            "Ptn0Functional", "ParentPartnTest_A38",
                            "ParentPartnTest_A38 Desc", "Partition002",
                            coreDMService);

            Ptn0Partition partition3 = (Ptn0Partition) createAdhocPartition(m_cd, m_partitionScheme,
                            "Ptn0Functional", "ParentPartnTest_A39",
                            "ParentPartnTest_A39 Desc", "Partition003",
                            coreDMService);

            Ptn0Partition partition4 = (Ptn0Partition) createAdhocPartition(m_cd, m_partitionScheme,
                            "Ptn0Functional", "ParentPartnTest_A39",
                            "ParentPartnTest_A39 Desc", "Partition004",
                            coreDMService);
            
    
            m_partitionRecipeContainer = partition3;
        
            // Need these to test partition recipe with List of Partitions
            m_inclPartitions.add( partition1 );
            m_inclPartitions.add( partition2 );
            m_inclPartitions.add( partition3 );

            String incluPrefix = "INCLDE00";
            int prtnAsnFlag = 0;
            for (int i = 0; i < 20; i++) 
            {
                String curTime = new Long(System.currentTimeMillis()).toString();
                System.out.println(curTime);
    
                String objName = incluPrefix + new Integer(i).toString();
                Cpd0DesignElement de = createPromissoryDE(dataManagementService, objName, m_cd);
                m_inclDEs.add(de);
                if (prtnAsnFlag == 0) 
                {
                    m_member1DEs.add(de); // INCLDE000, INCLDE003, INCLDE006,
                                        // INCLDE009, INCLDE0012, INCLDE0015,
                                        // INCLDE0018
                    prtnAsnFlag++;
                } 
                else if (prtnAsnFlag == 1) 
                {
                    m_member2DEs.add(de);
                    prtnAsnFlag++;
                } 
                else if (prtnAsnFlag == 2) 
                {
                    m_member3DEs.add(de);
                    prtnAsnFlag = 0;
                }
            }

            UpdateMembersInputInfo[] ipm1 = new UpdateMembersInputInfo[1];
            ipm1[0] = new UpdateMembersInputInfo();
            ipm1[0].contentPersistenceMode = "STATIC_MODE";
            ipm1[0].inputPartition = partition1;
            ipm1[0].opCode = "ADD";
            ipm1[0].configurationContext = null;
            ipm1[0].membersList = new Cpd0DesignElement[m_member1DEs.size()];
            for (int inx = 0; inx < m_member1DEs.size(); inx++) 
            {
                ipm1[0].membersList[inx] = (Cpd0DesignElement) m_member1DEs.get(inx);
            }
            UpdateMembersResponse resp = ptnService.updateMembers(ipm1);
            UpdateMembersInputInfo[] ipm2 = new UpdateMembersInputInfo[1];
            ipm2[0] = new UpdateMembersInputInfo();
            ipm2[0].contentPersistenceMode = "STATIC_MODE";
            ipm2[0].inputPartition = partition2;
            ipm2[0].opCode = "ADD";
            ipm2[0].configurationContext = null;
            ipm2[0].membersList = new Cpd0DesignElement[m_member2DEs.size()];
            for (int inx = 0; inx < m_member2DEs.size(); inx++) 
            {
                ipm2[0].membersList[inx] = (Cpd0DesignElement) m_member2DEs.get(inx);
            }
            resp = ptnService.updateMembers(ipm2);
            UpdateMembersInputInfo[] ipm3 = new UpdateMembersInputInfo[1];
            ipm3[0] = new UpdateMembersInputInfo();
            ipm3[0].contentPersistenceMode = "STATIC_MODE";
            ipm3[0].inputPartition = partition3;
            ipm3[0].opCode = "ADD";
            ipm3[0].configurationContext = null;
            ipm3[0].membersList = new Cpd0DesignElement[m_member3DEs.size()];
            for (int inx = 0; inx < m_member3DEs.size(); inx++) 
            {
                ipm3[0].membersList[inx] = (Cpd0DesignElement) m_member3DEs.get(inx);
            }
            resp = ptnService.updateMembers(ipm3);

            UpdateMembersInputInfo[] ipm4 = new UpdateMembersInputInfo[1];
            ipm4[0] = new UpdateMembersInputInfo();
            ipm4[0].contentPersistenceMode = "STATIC_MODE";
            ipm4[0].inputPartition = partition4;
            ipm4[0].opCode = "ADD";
            ipm4[0].configurationContext = null;
            ipm4[0].membersList = new Cpd0DesignElement[1];
            ipm4[0].membersList[0] = (Cpd0DesignElement)m_member1DEs.get( 0 );
            resp = ptnService.updateMembers(ipm4);
        
            // Position Data created - projected to XY axis
            //  y /\
            //    |
            //  6-|
            //    |
            //  5-|         __________________________
            //    |         |                        |
            //  4-|         |        ELE002          |
            //    |         |                        |
            //  3-|    _____|________________________|_____    ____________
            //    |    |         |              |         |    |          |
            //  2-|    | ELE001  |              | ELE003  |    | ELE005   |
            //    |    |       ____________________       |    |          |
            //  1-|    |_______|_|              |_|_______|    |__________|
            //    |            |     ELE004       |
            //  0-|------------|------------------|----------------------------------------------------------------------------------->
            //    |    1    2  | 3    4    5    6 |  7    8    9    10   11   12   13   14
            // -1-|            |                  |
            //    |            |__________________|
            // -2-|
            //    |
            // -3-|
            //    |
            //    \/
            //
            //  ______________________________________________________________
            //  |  Part Name | X-Min | Y-Min | Z-Min | X-Max | Y-Max | Z-Max |
            //  |____________|_______|_______|_______|_______|_______|_______|
            //  |            |       |       |       |       |       |       |
            //  |  ELE001    |  1    |   1   |   1   |   3   |   3   |   3   |
            //  |            |       |       |       |       |       |       |
            //  |  ELE002    |  2    |   3   |   1   |   7   |   5   |   3   | 
            //  |            |       |       |       |       |       |       |
            //  |  ELE003    |  6    |   1   |   1   |   8   |   3   |   3   |
            //  |            |       |       |       |       |       |       |
            //  |  ELE004    |  2.5  | -1.5  |   1   |  6.5  |  1.5  |   3   |
            //  |            |       |       |       |       |       |       |
            //  |  ELE005    |  9    |   1   |   1   |  11   |   3   |   3   |
            //  |            |       |       |       |       |       |       |
            //  |____________|_______|_______|_______|_______|_______|_______|
            //
    
            String elemPrefix = "ELE00";
            for (int i = 1; i < 6; i++) 
            {
                String curTime = new Long(System.currentTimeMillis()).toString();
                System.out.println(curTime);
    
                String objName = elemPrefix + new Integer(i).toString();
                if (i == 1) 
                {
                    double[] bbox = { 1.0, 1.0, 1.0, 3.0, 3.0, 3.0 };
                    Cpd0DesignElement de = createShapeDE( objName, curTime, m_cd, bbox, coreDMService, dataManagementService );
                    m_des.add(de);
                } 
                else if (i == 2) 
                {
                    double[] bbox = { 2.0, 3.0, 1.0, 7.0, 5.0, 3.0 };
                    Cpd0DesignElement de = createShapeDE( objName, curTime, m_cd, bbox, coreDMService, dataManagementService );
                    m_des.add(de);
                } 
                else if (i == 3) 
                {
                    double[] bbox = { 6.0, 1.0, 1.0, 8.0, 3.0, 3.0 };
                    Cpd0DesignElement de = createShapeDE( objName, curTime, m_cd, bbox, coreDMService, dataManagementService );
                    m_des.add(de);
                } 
                else if (i == 4) 
                {
                    double[] bbox = { 2.5, -1.5, 1.0, 6.5, 1.5, 3.0 };
                    Cpd0DesignElement de = createShapeDE( objName, curTime, m_cd, bbox, coreDMService, dataManagementService );
                    //SearchExpressionUtils.attachEffectivityCondition( de, "[Teamcenter::]Unit = 1" );
                    //objName = objName + "_Mod"; // force a save
                    //updateShapeDE(de, objName);
                    m_des.add(de);
                } 
                else if (i == 5) 
                {
                    double[] bbox = { 9.0, 1.0, 1.0, 11.0, 3.0, 3.0 };
                    Cpd0DesignElement de = createShapeDE( objName, curTime, m_cd, bbox, coreDMService, dataManagementService );
                    m_des.add(de);
                }
            }
            dataCreated = true;
        }
    }
    
    public static ModelObject createAdhocPartition( ModelObject mdlObj,ModelObject schemeObj,
            String partitionType, String objName, String objDesc, String ptn_id, com.teamcenter.services.strong.core.DataManagementService dataManagementService) {
        CreateIn[] inputs = new CreateIn[1];
        inputs[0] = new CreateIn();
        inputs[0].data = new CreateInput();
        inputs[0].data.boName = partitionType;
        inputs[0].data.tagProps.put("mdl0model_object", mdlObj);
        ModelObject ptnObj = null;

        try {
            dataManagementService.getProperties(new ModelObject[] { schemeObj }, new String[] { "object_type" });

            if (schemeObj.getPropertyObject("object_type") != null) {
                ImanType Im1 = new ImanType(schemeObj.getTypeObject(),
                        schemeObj.getTypeObject().getUid());
                inputs[0].data.tagProps.put("ptn0partition_scheme_type", Im1);
            }
        } catch (NotLoadedException e1) {
            e1.printStackTrace();
        }
        inputs[0].data.stringProps.put("object_name", objName);
        inputs[0].data.stringProps.put("object_desc", objDesc);
        inputs[0].data.stringProps.put("ptn0partition_id", ptn_id);
        inputs[0].data.tagProps.put("ptn0partition_recipe", null);
        inputs[0].data.tagProps.put("ptn0source_object", null);
        CreateResponse response = null;
        try {
            response = dataManagementService.createObjects(inputs);
            //check for Partition object
            for( ModelObject object : response.output[0].objects )
            {
                    if( object instanceof Ptn0Partition )
                    {
                        ptnObj = object;
                        break;
                    }
            }
        }
        catch (ServiceException e)
        {
            e.printStackTrace();
        }
        return ptnObj;
    }
    
    public static Cpd0DesignElement createShapeDE( String objName, String itemId, Cpd0CollaborativeDesign cd, double[] bbox, com.teamcenter.services.strong.core.DataManagementService coreDMService, com.cpd0.services.strong.cpdcore.DataManagementService dataManagementService ) 
    {
        Cpd0DesignElement de = null;
        try 
        {
            ShapeDesignElementInfo shapeDE = new ShapeDesignElementInfo();
            shapeDE.boType = "Cpd0DesignElement";
            shapeDE.clientId = "1000";
            shapeDE.attrInfo.put("object_name", new String[] { objName });
            shapeDE.modelObject = cd;

            CompoundCreateInput shapeDesign = new CompoundCreateInput();
            shapeDesign.createInfo.boType = "Cpd0ShapeDesign";
            shapeDesign.createInfo.attrInfo.put("item_id", new String[] { itemId });
            shapeDesign.createInfo.attrInfo.put("object_name", new String[] { "shape_name" });
            m_loginUser = m_session.getUser();
            coreDMService.getProperties(new ModelObject[] { m_loginUser }, new String[] { "home_folder" });

            shapeDesign.folder = m_loginUser.get_home_folder();
            CompoundCreateInfo shapeRevision = new CompoundCreateInfo();
            shapeRevision.boType = "Cpd0ShapeDesignRevision";
            shapeRevision.attrInfo.put("item_revision_id", new String[] { "itemRevisionID" });
            CompoundCreateInfo[] compounds = new CompoundCreateInfo[1];
            compounds[0] = shapeRevision;
            shapeDesign.createInfo.compoundInfo.put("revision", compounds);
            shapeDE.compoundCreateInput = shapeDesign;

            // positioned geometry
            shapeDE.boundingBox = bbox;

            CreateOrUpdateDesignElementsInfo info = new CreateOrUpdateDesignElementsInfo();
            info.shapeDesignElements = new ShapeDesignElementInfo[] { shapeDE };

            CreateOrUpdateDesignElementsResponse resp = dataManagementService.createOrUpdateDesignElements(info);
            de = (Cpd0DesignElement) resp.clientIDMap.get(shapeDE.clientId);
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }
        return de;
    }
    
    public static Cpd0DesignElement createPromissoryDE(com.cpd0.services.strong.cpdcore.DataManagementService dataManagementService, String objName, Cpd0CollaborativeDesign cd) 
    {
        Cpd0DesignElement de = null;
        try 
        {
            PromissoryDesignElementInfo promissoryDE = new PromissoryDesignElementInfo();
            promissoryDE.boType = "Cpd0DesignElement";
            promissoryDE.clientId = "5";
            promissoryDE.attrInfo.put("object_name", new String[] { objName });
            promissoryDE.modelObject = cd;
            CreateOrUpdateDesignElementsInfo info = new CreateOrUpdateDesignElementsInfo();
            info.promissoryDesignElements = new PromissoryDesignElementInfo[] { promissoryDE };

            CreateOrUpdateDesignElementsResponse resp = dataManagementService.createOrUpdateDesignElements(info);
            de = (Cpd0DesignElement) resp.clientIDMap.get(promissoryDE.clientId);
        }
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }
        return de;
    }

    public static Mdl0SearchDef createSavedQueryExpression(
            String clientId, SearchService searchService, SavedQueryService savedQueryService,
            String queryName, String[] entries, String[] values) 
    {
        Mdl0SearchDef searchDef = null;
        SavedQueryExpression sqe = new SavedQueryExpression();
        ImanQuery query = null;
        try 
        {
            // Step 1: Find the "Item ID" query.
            GetSavedQueriesResponse resp = savedQueryService.getSavedQueries();
            int i;
            for (i = 0; i < resp.queries.length; i++) 
            {
                if (resp.queries[i].name.equals(queryName)) 
                {
                    query = resp.queries[i].query;
                    break;
                }
            }
            // this makes sure that we find the saved query
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        sqe.savedQuery = query;
        sqe.entries = entries;
        sqe.values = values;

        SavedQueryExpressionInput sqei = new SavedQueryExpressionInput();
        sqei.savedQueryExpression = sqe;
        sqei.clientid = clientId;
        SearchExpressionInput seInput = new SearchExpressionInput();
        seInput.savedQueryExpressions = new SavedQueryExpressionInput[1];
        seInput.savedQueryExpressions[0] = sqei;

        // create a SOA expression and add to it.
        SearchExpressionResponse serverResponse = searchService
                .createSearchExpressions(seInput);
        ServiceData serviceData = serverResponse.serviceData;
        ExpressionResponse[] responses = serverResponse.expressions;

        for (int i = 0; i < responses.length; i++) 
        {
            ExpressionResponse resp = responses[i];
            if (resp.clientid.compareTo(clientId) == 0) 
            {
                // this is the response for the request
                searchDef = resp.searchDef;
            }
        }
        return searchDef;
    }
    
    public static Mdl0SearchDef createBoxZoneExpressionWithInsideOperator(
            String clientId,
            ArrayList<Cpd0DesignElement> designElementList,
            SearchService searchService) 
    {
        Mdl0SearchDef searchDef = null;
        com.mdl0.services.strong.modelcore._2011_06.Search.BoxZoneExpressionInput bzei = new com.mdl0.services.strong.modelcore._2011_06.Search.BoxZoneExpressionInput();
        bzei.clientid = clientId;

        BoxZoneExpression bze = new BoxZoneExpression();
        SearchBox sb = new SearchBox();
        sb.xmax = 0.978;
        sb.xmin = -0.75;
        sb.ymax = 1.0;
        sb.ymin = -0.60;
        sb.zmax = 0.595;
        sb.zmin = -0.62;

        bze.searchBoxes = new SearchBox[1];
        bze.searchBoxes[0] = sb;
        bze.boxOperator = "Inside";
        bzei.boxZoneExpression = bze;

        SearchExpressionInput seInput = new SearchExpressionInput();
        seInput.boxZoneExpressions = new BoxZoneExpressionInput[1];
        seInput.boxZoneExpressions[0] = bzei;

        SearchExpressionResponse serverResponse = searchService
                .createSearchExpressions(seInput);
        ServiceData serviceData = serverResponse.serviceData;
        ExpressionResponse[] responses = serverResponse.expressions;

        for (int i = 0; i < responses.length; i++) 
        {
            ExpressionResponse resp = responses[i];
            if (resp.clientid.compareTo(clientId) == 0) 
            {
                // this is the response for the request
                searchDef = resp.searchDef;
            }
        }
        return searchDef;
    }
    
    public static Mdl0SearchDef createPartitionExpression(
            PartitionSearchExpressionInput psei, String clientId,
            com.ptn0.services.strong.partition.SearchService searchService) 
    {
        Mdl0SearchDef searchDef = null;

        com.ptn0.services.strong.partition._2012_09.Search.SearchExpressionResponse serverResponse = searchService
                .createPartitionExpressions(psei);

        ServiceData serviceData = serverResponse.serviceData;
        com.ptn0.services.strong.partition._2012_09.Search.ExpressionResponse[] responses = serverResponse.expressions;
        for (int i = 0; i < responses.length; i++) 
        {
            com.ptn0.services.strong.partition._2012_09.Search.ExpressionResponse resp = responses[i];
            if (resp.clientid.compareTo(clientId) == 0) 
            {
                // this is the response for the request
                searchDef = resp.searchDef;
            }
        }
        return searchDef;
    }
    
    private static SearchExpressionSet buildRecipeGroupUsingSavedQueryANDBoxZone()
    {
        // Position Data created - projected to XY axis
        //  y /\
        //    |
        //  6-|
        //    |
        //  5-|         __________________________
        //    |         |                        |
        //  4-|         |         ELE002         |
        //    |         | ___________            |
        //  3-|    _____|_|_________|____________|_____    ____________
        //    |    |      |  |      |       |         |    |          |
        //  2-|    |ELE001|  | TEST |       | ELE003  |    | ELE005   |
        //    |    |      |_________|__________       |    |          |
        //  1-|    |______||_|      |       |_|_______|    |__________|
        //    |           |_________| ELE004  |
        //  0-|------------|------------------|----------------------------------------------------------------------------------->
        //    |    1    2  | 3    4    5    6 |  7    8    9    10   11   12   13   14
        // -1-|            |                  |
        //    |            |__________________|
        // -2-|
        //    |
        // -3-|
        //    |
        //    \/
        //
        //  ______________________________________________________________
        //  |  Part Name | X-Min | Y-Min | Z-Min | X-Max | Y-Max | Z-Max |
        //  |____________|_______|_______|_______|_______|_______|_______|
        //  |            |       |       |       |       |       |       |
        //  |  TEST      |  2.2  |  0.5  |   1   |  4.5  |  3.5  |   3   |
        //  |            |       |       |       |       |       |       |
        //  |____________|_______|_______|_______|_______|_______|_______|
        //
        //  Expected Results for Inside (which is really InsideOrIntersects) => ELE001, ELE002 and ELE004
        //  Combining with the Saved Query Expected results is ELE002

        String savedQueryName = "Design Element";
        String[] entries = new String[1];
        entries[0] = "Design Element Name";
        String[] values = new String[1];
        values[0] = "ELE*2";
        Mdl0SearchDef savedQuerySearchDef = createSavedQueryExpression(
                "Cli1" + "SavedQueryExpression", m_searchService, m_savedQueryService, savedQueryName, entries, values);
        SearchExpressionSet seSet1 = new SearchExpressionSet();
        seSet1.searchOperator = "And";
        SearchExpression searchExpression1 = new SearchExpression();
        searchExpression1.searchExpressions = new Mdl0SearchDef[1];
        searchExpression1.searchExpressions[0] = savedQuerySearchDef;
        seSet1.searchExpression = searchExpression1;

        Mdl0SearchDef boxZoneSearchDef = createBoxZoneExpressionWithInsideOperator(
                "Cli1"  + "BoxZoneExpression", m_des, m_searchService);

        SearchExpressionSet seSet2 = new SearchExpressionSet();
        seSet2.searchOperator = new String("And");

        SearchExpression searchExpression2 = new SearchExpression();
        searchExpression2.searchExpressions = new Mdl0SearchDef[1];
        searchExpression2.searchExpressions[0] = boxZoneSearchDef;
        seSet2.searchExpression = searchExpression2;

        SearchExpressionSet seSetGroup = new SearchExpressionSet();
        seSetGroup.searchOperator = "And";
        seSetGroup.searchExpressionSets = new SearchExpressionSet[2];
        seSetGroup.searchExpressionSets[0] = seSet1;
        seSetGroup.searchExpressionSets[1] = seSet2;

        return seSetGroup;
    }
    
    private static void executePartitionSearchExpressionWithBoxAndSavedQry( Ptn0Partition partitionRecipeContainer )
    {
        RecipeData[] recipedata = new RecipeData[1];
        recipedata[0] = new RecipeData();
        recipedata[0].recipeContainer = partitionRecipeContainer
        SearchRecipe recipe = new SearchRecipe();
        SearchScope scope = new SearchScope();
        scope.model = m_cd;
        recipe.scope = scope;
        recipe.searchExpression = buildRecipeGroupUsingSavedQueryANDBoxZone();
        recipedata[0].recipe = recipe;
        ServiceData response = m_searchService.setRecipes(recipedata);
        
        com.mdl0.services.strong.modelcore._2011_06.Search.SearchOptions searchOptions = new SearchOptions();
        searchOptions.defaultLoadCount = 100;
        searchOptions.sortOrder = "Ascending";
        ArrayList<Mdl0ModelElement> responses = new ArrayList<Mdl0ModelElement>();
        SearchResponse execSrhResponse = m_searchService.executeSearch(recipe, searchOptions);
        System.out.println("Completed the search execution - going for the results");
    }
}

