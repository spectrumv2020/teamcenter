//==================================================
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//==================================================


package com.teamcenter.mdomanagement;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.cpd0.services.strong.cpdcore._2011_06.DataManagement.CompoundCreateInfo;
import com.cpd0.services.strong.cpdcore._2011_06.DataManagement.CompoundCreateInput;
import com.cpd0.services.strong.cpdcore._2011_06.DataManagement.CreateOrUpdateDesignElementsInfo;
import com.cpd0.services.strong.cpdcore._2011_06.DataManagement.CreateOrUpdateDesignElementsResponse;
import com.cpd0.services.strong.cpdcore._2011_06.DataManagement.CreateOrUpdateDesignSubsetElementsResponse;
import com.cpd0.services.strong.cpdcore._2011_06.DataManagement.DesignSubsetElementInfo;
import com.cpd0.services.strong.cpdcore._2011_06.DataManagement.PromissoryDesignElementInfo;
import com.cpd0.services.strong.cpdcore._2011_06.DataManagement.ReuseDesignElementInfo;
import com.cpd0.services.strong.cpdcore._2011_06.DataManagement.ShapeDesignElementInfo;
import com.cpd0.services.strong.cpdcore._2014_10.DataManagement.InstantiationObjectInfo;
import com.cpd0.services.strong.cpdcore._2014_10.DataManagement.ModelToModelInstantiationInfo;
import com.cpd0.services.strong.cpdcore._2014_10.DataManagement.ModelToModelInstantiationResponse;
import com.lbr0.services.strong.librarymanagement.LibraryManagementService;
import com.lbr0.services.strong.librarymanagement.LibraryUsageService;
import com.lbr0.services.strong.librarymanagement._2014_10.LibraryManagement.CreateHierarchiesIn;
import com.lbr0.services.strong.librarymanagement._2014_10.LibraryManagement.CreateHierarchiesResponse;
import com.lbr0.services.strong.librarymanagement._2014_10.LibraryManagement.CreateLibrariesIn;
import com.lbr0.services.strong.librarymanagement._2014_10.LibraryManagement.CreateLibrariesResponse;
import com.lbr0.services.strong.librarymanagement._2014_10.LibraryManagement.CreateNodesIn;
import com.lbr0.services.strong.librarymanagement._2014_10.LibraryManagement.CreateNodesResponse;
import com.lbr0.services.strong.librarymanagement._2014_10.LibraryUsage.ObjectToPublishInfo;
import com.lbr0.services.strong.librarymanagement._2014_10.LibraryUsage.PublishObjectsIn;
import com.lbr0.services.strong.librarymanagement._2014_10.LibraryUsage.PublishObjectsResponse;
import com.mdl0.services.strong.modelcore.IdManagementService;
import com.mdl0.services.strong.modelcore.SearchService;
import com.mdl0.services.strong.modelcore._2011_06.DataManagement.DeepCopyData;
import com.mdl0.services.strong.modelcore._2011_06.DataManagement.SaveAsDescResponse;
import com.mdl0.services.strong.modelcore._2011_06.DataManagement.SaveAsIn;
import com.mdl0.services.strong.modelcore._2011_06.DataManagement.SaveAsObjectsResponse;
import com.mdl0.services.strong.modelcore._2011_06.IdManagement.AssignInput;
import com.mdl0.services.strong.modelcore._2011_06.IdManagement.AssignResponse;
import com.mdl0.services.strong.modelcore._2011_06.Search.ConfigResponse;
import com.mdl0.services.strong.modelcore._2011_06.Search.ConfigurationData;
import com.mdl0.services.strong.modelcore._2011_06.Search.ConfigurationDetail;
import com.mdl0.services.strong.modelcore._2011_06.Search.ExpressionResponse;
import com.mdl0.services.strong.modelcore._2011_06.Search.ModelElementInput;
import com.mdl0.services.strong.modelcore._2011_06.Search.RecipeData;
import com.mdl0.services.strong.modelcore._2011_06.Search.SearchExpression;
import com.mdl0.services.strong.modelcore._2011_06.Search.SearchExpressionInput;
import com.mdl0.services.strong.modelcore._2011_06.Search.SearchExpressionResponse;
import com.mdl0.services.strong.modelcore._2011_06.Search.SearchExpressionSet;
import com.mdl0.services.strong.modelcore._2011_06.Search.SearchRecipe;
import com.mdl0.services.strong.modelcore._2011_06.Search.SearchScope;
import com.mdl0.services.strong.modelcore._2014_10.DataManagement.CloneModelContentInfo;
import com.mdl0.services.strong.modelcore._2014_10.DataManagement.CloneModelContentResponse;
import com.mdl0.services.strong.modelcore._2014_10.DataManagement.SourceTargetContentInfo;
import com.mdo0.services.strong.mdomanagement._2018_06.MultiDisciplinaryDataMgt.GetDomainRelevancyInput;
import com.mdo0.services.strong.mdomanagement._2018_06.MultiDisciplinaryDataMgt.GetDomainRelevancyOfAnObjectResp;
import com.mdo0.services.strong.mdomanagement._2018_06.MultiDisciplinaryDataMgt.SplitDesignArtifactInput;
import com.mdo0.services.strong.mdomanagement._2018_06.MultiDisciplinaryDataMgt.SplitDesignArtifactsResponse;
import com.mdo0.services.strong.mdomanagement._2018_06.MultiDisciplinaryDataMgt.SplitDesignInstanceInput;
import com.mdo0.services.strong.mdomanagement._2018_06.MultiDisciplinaryDataMgt.SplitDesignInstancesResponse;
import com.mdo0.services.strong.mdomanagement.MultiDisciplinaryDataMgtService;
import com.mdo0.services.strong.mdomanagement._2014_10.MultiDisciplinaryDataMgt.CreateInput;
import com.mdo0.services.strong.mdomanagement._2014_10.MultiDisciplinaryDataMgt.CreateOrUpdateMDOResponse;
import com.mdo0.services.strong.mdomanagement._2014_10.MultiDisciplinaryDataMgt.DesignArtifactInputsForSearch;
import com.mdo0.services.strong.mdomanagement._2014_10.MultiDisciplinaryDataMgt.DesignInstancesData;
import com.mdo0.services.strong.mdomanagement._2014_10.MultiDisciplinaryDataMgt.DesignInstancesInput;
import com.mdo0.services.strong.mdomanagement._2014_10.MultiDisciplinaryDataMgt.InstancesToLinkResponse;
import com.mdo0.services.strong.mdomanagement._2014_10.MultiDisciplinaryDataMgt.InstancesToUnlinkResponse;
import com.mdo0.services.strong.mdomanagement._2014_10.MultiDisciplinaryDataMgt.LinkedInstances;
import com.mdo0.services.strong.mdomanagement._2014_10.MultiDisciplinaryDataMgt.MDOInput;
import com.mdo0.services.strong.mdomanagement._2014_10.MultiDisciplinaryDataMgt.MDOOutput;
import com.mdo0.services.strong.mdomanagement._2014_10.MultiDisciplinaryDataMgt.MDOSearchOutput;
import com.mdo0.services.strong.mdomanagement._2014_10.MultiDisciplinaryDataMgt.SearchForDesignArtifactInput;
import com.mdo0.services.strong.mdomanagement._2014_10.MultiDisciplinaryDataMgt.SearchForLinkedInstancesInput;
import com.mdo0.services.strong.mdomanagement._2014_10.MultiDisciplinaryDataMgt.SearchForLinkedInstancesResponse;
import com.mdo0.services.strong.mdomanagement._2014_10.MultiDisciplinaryDataMgt.SearchInput;
import com.mdo0.services.strong.mdomanagement._2014_10.MultiDisciplinaryDataMgt.SearchMDOResponse;
import com.mdo0.services.strong.mdomanagement._2015_07.MultiDisciplinaryDataMgt.CreateNotificationOutput;
import com.mdo0.services.strong.mdomanagement._2015_07.MultiDisciplinaryDataMgt.MDOOutput2;
import com.mdo0.services.strong.mdomanagement._2015_07.MultiDisciplinaryDataMgt.MDOInitialNotification;
import com.mdo0.services.strong.mdomanagement._2015_07.MultiDisciplinaryDataMgt.MDONotificationDetails;
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
import com.mdo0.services.strong.mdomanagement._2015_07.MultiDisciplinaryDataMgt.SearchForDesignArtifactInput2;
import com.mdo0.services.strong.mdomanagement._2015_07.MultiDisciplinaryDataMgt.SearchInput2;
import com.mdo0.services.strong.mdomanagement._2015_07.MultiDisciplinaryDataMgt.SearchMDO2Response;
import com.mdo0.services.strong.mdomanagement._2015_07.MultiDisciplinaryDataMgt.UpdateMDONotificationResponse;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.DesignInstancesInput2;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.DomainRelevancyInput;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.FilterForDomainRelevancy;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.ImpactedDesignInstanceInfo2;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.InstancesToLinkResponse2;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.MDInstanceData;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.MDInstanceLinkCarryFwdInput;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.MDInstanceToMDThreadInput;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.MDOSearchOutput3;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.MDThreadLinkCarryFwdInput;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.NeedsValidationLinkInput;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.NeedsValidationLinkResponse;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.SearchForLinkedInstances2Response;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.SearchInput3;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.SearchMDO3Response;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.SearchForDesignArtifactInput3;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.UpdateDomainRelevancyResponse;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.UpdateLinkStatusToValidatedInput;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.UpdateLinksToValidatedResponse;
import com.mdo0.services.strong.mdomanagement._2017_05.MultiDisciplinaryDataMgt.UpdateMDInstanceToMDThreadResponse;
import com.rlz0.services.strong.realization.RealizationManagementService;
import com.rlz0.services.strong.realization._2014_10.RealizationManagement.ModelToModelRealizationContentResponse;
import com.rlz0.services.strong.realization._2014_10.RealizationManagement.RealizationContentInfo;
import com.rlz0.services.strong.realization._2014_10.RealizationManagement.RealizeModelContentInfo;
import com.rlz0.services.strong.realization._2014_10.RealizationManagement.SourceModelInfo;
import com.teamcenter.clientx.AppXSession;
import com.teamcenter.schemas.soa._2006_03.exceptions.ServiceException;
import com.teamcenter.services.strong.cad.StructureManagementService;
import com.teamcenter.services.strong.cad._2007_01.StructureManagement.CreateBOMWindowsInfo;
import com.teamcenter.services.strong.cad._2007_01.StructureManagement.CreateBOMWindowsResponse;
import com.teamcenter.services.strong.cad._2007_01.StructureManagement.CreateOrUpdateRelativeStructureResponse;
import com.teamcenter.services.strong.cad._2007_01.StructureManagement.GetRevisionRulesResponse;
import com.teamcenter.services.strong.cad._2007_01.StructureManagement.RevisionRuleInfo;
import com.teamcenter.services.strong.cad._2008_06.StructureManagement.CreateOrUpdateRelativeStructureInfo3;
import com.teamcenter.services.strong.cad._2008_06.StructureManagement.CreateOrUpdateRelativeStructurePref3;
import com.teamcenter.services.strong.cad._2008_06.StructureManagement.RelativeStructureChildInfo2;
import com.teamcenter.services.strong.core.DataManagementService;
import com.teamcenter.services.strong.core.ProjectLevelSecurityService;
import com.teamcenter.services.strong.core.ReservationService;
import com.teamcenter.services.strong.core._2006_03.DataManagement.CreateItemsResponse;
import com.teamcenter.services.strong.core._2006_03.DataManagement.GenerateItemIdsAndInitialRevisionIdsProperties;
import com.teamcenter.services.strong.core._2006_03.DataManagement.GenerateItemIdsAndInitialRevisionIdsResponse;
import com.teamcenter.services.strong.core._2006_03.DataManagement.ItemIdsAndInitialRevisionIds;
import com.teamcenter.services.strong.core._2006_03.DataManagement.ItemProperties;
import com.teamcenter.services.strong.core._2007_09.ProjectLevelSecurity.AssignedOrRemovedObjects;
import com.teamcenter.services.strong.core._2008_06.DataManagement.CreateIn;
import com.teamcenter.services.strong.core._2008_06.DataManagement.CreateResponse;
import com.teamcenter.services.strong.core._2008_06.DataManagement.ReviseInfo;
import com.teamcenter.services.strong.core._2008_06.DataManagement.ReviseResponse2;
import com.teamcenter.services.strong.core._2008_06.DataManagement.SaveAsNewItemInfo;
import com.teamcenter.services.strong.core._2008_06.DataManagement.SaveAsNewItemOutput2;
import com.teamcenter.services.strong.core._2008_06.DataManagement.SaveAsNewItemResponse2;
import com.teamcenter.services.strong.core._2012_09.DataManagement.RelateInfoIn;
import com.teamcenter.services.strong.core._2012_09.ProjectLevelSecurity.ProjectInformation;
import com.teamcenter.services.strong.core._2012_09.ProjectLevelSecurity.ProjectOpsResponse;
import com.teamcenter.services.strong.core._2012_09.ProjectLevelSecurity.TeamMemberInfo;
import com.teamcenter.services.strong.core._2014_10.DataManagement.DeepCopyDataInput;
import com.teamcenter.services.strong.core._2014_10.DataManagement.GetDeepCopyDataResponse;
import com.teamcenter.services.strong.core._2015_07.DataManagement.GetDomainInput;
import com.teamcenter.services.strong.query.SavedQueryService;
import com.teamcenter.services.strong.query._2006_03.SavedQuery.GetSavedQueriesResponse;
import com.teamcenter.services.strong.query._2006_03.SavedQuery.SavedQueryObject;
import com.teamcenter.services.strong.query._2007_06.SavedQuery.ExecuteSavedQueriesResponse;
import com.teamcenter.services.strong.query._2007_06.SavedQuery.SavedQueryInput;
import com.teamcenter.services.strong.query._2007_06.SavedQuery.SavedQueryResults;
import com.teamcenter.services.strong.structuremanagement.StructureService;
import com.teamcenter.services.strong.structuremanagement._2012_09.Structure.AddInformation;
import com.teamcenter.services.strong.structuremanagement._2012_09.Structure.AddParam;
import com.teamcenter.services.strong.structuremanagement._2012_09.Structure.AddResponse;
import com.teamcenter.services.strong.structuremanagement._2012_10.Structure.CutItemParam;
import com.teamcenter.services.strong.workflow.WorkflowService;
import com.teamcenter.services.strong.workflow._2007_06.Workflow.ReleaseStatusInput;
import com.teamcenter.services.strong.workflow._2007_06.Workflow.ReleaseStatusOption;
import com.teamcenter.services.strong.workflow._2008_06.Workflow.ContextData;
import com.teamcenter.services.strong.workflow._2008_06.Workflow.InstanceInfo;
import com.teamcenter.soa.client.Connection;
import com.teamcenter.soa.client.model.ErrorStack;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.client.model.Property;
import com.teamcenter.soa.client.model.ServiceData;
import com.teamcenter.soa.client.model.strong.BOMWindow;
import com.teamcenter.soa.client.model.strong.BusinessObject;
import com.teamcenter.soa.client.model.strong.ConfigurationContext;
import com.teamcenter.soa.client.model.strong.Cpd0CollaborativeDesign;
import com.teamcenter.soa.client.model.strong.Cpd0DesignElement;
import com.teamcenter.soa.client.model.strong.Cpd0DesignSubsetElement;
import com.teamcenter.soa.client.model.strong.Cpd0WorksetRevision;
import com.teamcenter.soa.client.model.strong.GroupMember;
import com.teamcenter.soa.client.model.strong.ImanType;
import com.teamcenter.soa.client.model.strong.Item;
import com.teamcenter.soa.client.model.strong.ItemRevision;
import com.teamcenter.soa.client.model.strong.Lbr0Hierarchy;
import com.teamcenter.soa.client.model.strong.Lbr0HierarchyNode;
import com.teamcenter.soa.client.model.strong.Lbr0Library;
import com.teamcenter.soa.client.model.strong.Mdl0ApplicationModel;
import com.teamcenter.soa.client.model.strong.Mdl0Baseline;
import com.teamcenter.soa.client.model.strong.Mdl0BaselineDefinition;
import com.teamcenter.soa.client.model.strong.Mdl0ModelElement;
import com.teamcenter.soa.client.model.strong.Mdl0PositionedModelElement;
import com.teamcenter.soa.client.model.strong.Mdl0SearchDef;
import com.teamcenter.soa.client.model.strong.Mdl0SubsetDefinition;
import com.teamcenter.soa.client.model.strong.POM_object;
import com.teamcenter.soa.client.model.strong.RevisionRule;
import com.teamcenter.soa.client.model.strong.TC_Project;
import com.teamcenter.soa.client.model.strong.User;
import com.teamcenter.soa.client.model.strong.WorkspaceObject;
import com.teamcenter.soa.exceptions.NotLoadedException;

public class MDOTestUtils
{
    private static Connection m_connection = null;
    private static MultiDisciplinaryDataMgtService m_mdoService = null;
    private static ReservationService m_resService = null;
    private static StructureManagementService m_smService;
    private static DataManagementService m_coreDMService = null;
    private static com.cpd0.services.strong.cpdcore.DataManagementService m_dataMgtService;
    private static com.mdl0.services.strong.modelcore.DataManagementService m_mdlDataMgtService;
    private static ProjectLevelSecurityService m_prjLevSecService = null;
    private static WorkflowService m_workflowService = null;
    private String m_itemNameSuffix = "_";
    private int m_suffixIncrVal = 0;
    static Random generator = new Random();
    private Map<String, SavedQueryObject> m_savedQueryObjectMap;
    private static final String clientId = "testCreateOrUpdateDesignSubsetElements";
    private static com.teamcenter.services.strong.core.DataManagementService m_dmService;
    private static com.teamcenter.services.internal.strong.core.DataManagementService m_IntDMService;
    private static SearchService m_searchService;
    private static RealizationManagementService m_rlzDMService;
    private static LibraryManagementService m_libraryManagementService = null;
    private static LibraryUsageService m_libraryUsageService = null;
    /**
     * Constructor
     * @param connection
     */
    public MDOTestUtils()
    {
        com.teamcenter.soa.client.model.StrongObjectFactoryCpd.init();
        com.teamcenter.soa.client.model.StrongObjectFactoryAppmodel.init();
        com.teamcenter.soa.client.model.StrongObjectFactory.init();
        com.teamcenter.soa.client.model.StrongObjectFactoryLibrarymgmt.init();
        m_connection = AppXSession.getConnection();
        m_dataMgtService = com.cpd0.services.strong.cpdcore.DataManagementService.getService(m_connection);
        m_coreDMService = DataManagementService.getService( m_connection );
        m_IntDMService = com.teamcenter.services.internal.strong.core.DataManagementService.getService( m_connection );
        m_resService = ReservationService.getService( m_connection );
        m_mdoService = MultiDisciplinaryDataMgtService.getService( m_connection );
        m_smService  = com.teamcenter.services.strong.cad.StructureManagementService.getService(m_connection);
        m_prjLevSecService = com.teamcenter.services.strong.core.ProjectLevelSecurityService.getService( m_connection );
        m_workflowService = WorkflowService.getService( m_connection );
        m_mdlDataMgtService = com.mdl0.services.strong.modelcore.DataManagementService.getService(m_connection);
        m_dmService = com.teamcenter.services.strong.core.DataManagementService.getService(m_connection);
        m_searchService = SearchService.getService(m_connection);
        m_rlzDMService = RealizationManagementService.getService(m_connection);
        m_libraryManagementService  = LibraryManagementService.getService(m_connection);
        m_libraryUsageService  = LibraryUsageService.getService(m_connection);
    }

    /**
     * Use this helper method to create multiple items at one go
     * @param nItems
     * @param itemName
     * @param itemType : If specified as "", then default will be taken, i.e., "Item".
     * @param itemsList : List of Items created
     * @param revsList : List of Revisions created
     * @return
     */
    public CreateItemsResponse createItems (int nItems, String itemName,
                                    String itemType, List<Item> itemsList,
                                    List<ItemRevision> revsList)
    {
        try
        {
            List<String> items = new ArrayList<String>();
            List<String> revs = new ArrayList<String>();
            generateItemIdsAndRevIds(nItems, itemType, items, revs);

            itemName = itemName + m_itemNameSuffix + m_suffixIncrVal;
            ItemProperties[] props =
                populateItemProperties(nItems, itemName, itemType, "", items, revs);
            m_suffixIncrVal = m_suffixIncrVal+1;
            CreateItemsResponse response =
                    m_coreDMService.createItems(props, null, "" );

            for( int idx = 0; idx < response.output.length; ++idx )
            {
                itemsList.add( response.output[idx].item );
                revsList.add( response.output[idx].itemRev );
            }
            if ( ! handleServiceData( response.serviceData, "" ) )
            {
                LogFile.write( "createItems: Failed" );
            }
            return response;
        }
        catch(Exception ex)
        {
            LogFile.write("Item creation failed");
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Revise an item revision
     * @param itemRev
     * @param newRevId
     * @return
     * @throws Exception
     */
    public ItemRevision reviseItemRev (ItemRevision itemRev, String newRevId) throws Exception
    {
        ReviseInfo[] reviseInfos = new ReviseInfo[1];
        reviseInfos[0] = new ReviseInfo();
        reviseInfos[0].clientId = "ReviseItemRevision";
        reviseInfos[0].baseItemRevision = itemRev;
        //reviseInfos[0].name = "";
        reviseInfos[0].newRevId = newRevId;
        //reviseInfos[0].description = "";

        // Call the reviseObject operation
        ReviseResponse2 revResponse = m_coreDMService.revise2( reviseInfos );

        Map<String,com.teamcenter.services.strong.core._2008_06.DataManagement.ReviseOutput> reviseOutputMap = revResponse.reviseOutputMap;
        Iterator<Entry<String,com.teamcenter.services.strong.core._2008_06.DataManagement.ReviseOutput>> iterator = reviseOutputMap.entrySet().iterator();
        Map.Entry<String,com.teamcenter.services.strong.core._2008_06.DataManagement.ReviseOutput> entry = iterator.next();

        //new revision was created
        com.teamcenter.services.strong.core._2008_06.DataManagement.ReviseOutput reviseOutput = entry.getValue();
        return reviseOutput.newItemRev;
    }
    
    /**
     * SaveAs an item revision
     * @param itemRev
     * @param newItemId
     * @return
     * @throws Exception
     */
    public ItemRevision saveAsItemRev (ItemRevision itemRevision, String newItemId) throws Exception
    {
        SaveAsNewItemResponse2 saveAsResp;

        SaveAsNewItemInfo[] saveAsNewItemInfos = new SaveAsNewItemInfo[1];
        saveAsNewItemInfos[0] = new SaveAsNewItemInfo();
        saveAsNewItemInfos[0].clientId = "DataMgmtSaveAsServiceClientID";
        saveAsNewItemInfos[0].baseItemRevision = itemRevision;
        saveAsNewItemInfos[0].name = "DataMSOASaveAsItemNew";
        String itemIDNew = newItemId;
        saveAsNewItemInfos[0].newItemId = itemIDNew;
        saveAsNewItemInfos[0].newRevId = "A";
        saveAsNewItemInfos[0].description = "";

        saveAsResp = m_coreDMService.saveAsNewItem2( saveAsNewItemInfos );

        Map<String,SaveAsNewItemOutput2> saveAsOutputMap = saveAsResp.saveAsOutputMap;
        Iterator<Map.Entry<String,SaveAsNewItemOutput2>> iterator = saveAsOutputMap.entrySet().iterator();
        Map.Entry<String,SaveAsNewItemOutput2> entry = iterator.next();


        SaveAsNewItemOutput2 saveAsNewItemOutput2 = entry.getValue();
        ItemRevision newItemRev = saveAsNewItemOutput2.newItemRev;

        return newItemRev;
    }

    /**
     * Generate ItemIds And RevIds
     * @param noOfIds
     * @param itemIds
     * @param revIds
     */
    public GenerateItemIdsAndInitialRevisionIdsResponse generateItemIdsAndRevIds (int noOfIds,
            String itemType, List<String> itemIds, List<String> revIds)
    {
        if(itemType.equals(""))
        {
            itemType = "Item";
        }

        //Generate item and rev ids
        GenerateItemIdsAndInitialRevisionIdsResponse response = null;
        GenerateItemIdsAndInitialRevisionIdsProperties[] props = new GenerateItemIdsAndInitialRevisionIdsProperties[noOfIds];

        for ( int idx=0; idx<noOfIds; ++idx )
        {
            GenerateItemIdsAndInitialRevisionIdsProperties property = new GenerateItemIdsAndInitialRevisionIdsProperties();
            property.count = 1;
            property.itemType = itemType;
            props[idx] = property;
        }
        response = m_coreDMService.generateItemIdsAndInitialRevisionIds(props);


        //Add the item ids and rev ids to List
        Map<BigInteger,ItemIdsAndInitialRevisionIds[]> itemsIdsAndRevs =
            response.outputItemIdsAndInitialRevisionIds;

        for (Iterator<Map.Entry<BigInteger,ItemIdsAndInitialRevisionIds[]>> iterator = itemsIdsAndRevs.entrySet().iterator(); iterator.hasNext();)
        {
            Map.Entry<BigInteger,ItemIdsAndInitialRevisionIds[]> entry = iterator.next();
            ItemIdsAndInitialRevisionIds[] theMap = (ItemIdsAndInitialRevisionIds[])entry.getValue();
            for ( int i=0; i<theMap.length ; ++i)
            {
                ItemIdsAndInitialRevisionIds ids= theMap[i];
                itemIds.add(ids.newItemId);
                revIds.add(ids.newRevId);
            }
        }
        if ( ! handleServiceData( response.serviceData, "" ) )
        {
            LogFile.write( "generateItemIdsAndRevIds: Failed" );
        }
        return response;
    }


    /**
     * Populate Item Properties
     * @param num_ids
     * @param itemName
     * @param itemType
     * @param desc
     * @param itemIds
     * @param revIds
     * @return
     */
    private ItemProperties[] populateItemProperties (int num_ids, String itemName, String itemType, String desc, List<String> itemIds, List<String> revIds)
    {
        ItemProperties[] itemProps = new ItemProperties[num_ids];
        for (int idx = 0; idx<num_ids; ++idx)
        {
            ItemProperties itemProperty = new ItemProperties();
            itemProperty.clientId = "TestItem_" + m_suffixIncrVal ;
            itemProperty.itemId = (String)itemIds.get(idx);
            itemProperty.revId = (String)revIds.get(idx);
            itemProperty.type = itemType != "" ? itemType  : "Item";
            itemProps[idx] = itemProperty;
            itemProperty.name = itemName;
        }
        return itemProps;
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
            for( int i=0; i < noPartErrors; ++i )
            {
                ErrorStack errorStack = sData.getPartialError(i);
                String[] messages = errorStack.getMessages();
                for(int j=0; messages != null && j < messages.length; ++j )
                {
                    errorMessage = errorMessage + messages[j] + "\n";
                }
            }
            if( errorMessage != "" )
            {
                if( !methodName.equals("") )
                {
                    LogFile.write( methodName + ": " + errorMessage );
                }
                else
                {
                    LogFile.write( "handleServiceData: " + errorMessage );
                }
            }
            return false;
        }
        return true;
    }



    /**
     * Retrieves the revision rule as per input value. It looks for revision
     * rule from all available revision rules in system.
     * @param revRuleName Revision Rule name. If null or empty string passed,
     *  first value from all revision rules is returned.
     * @return Revision Rule object
     * @throws Exception
     */
    public static RevisionRule getRevisionRuleByName( String revRuleName ) throws Exception
    {
        RevisionRule revRule = null;
        StructureManagementService m_smService = StructureManagementService.getService(m_connection);
        GetRevisionRulesResponse ruleResp = m_smService.getRevisionRules();

        RevisionRule retRevRule = null;

        // If an empty or null string passed, return first value
        if(revRuleName == null || revRuleName.length() == 0)
        {
            retRevRule = ruleResp.output.length > 0 ? ruleResp.output[0].revRule : null;
            return retRevRule;
        }

        for(RevisionRuleInfo revRuleInfo : ruleResp.output)
        {
            revRule = revRuleInfo.revRule;

            com.teamcenter.soa.client.model.Property[] objNameProp = getObjectProperties(revRule, new String[] {"object_name"});
            if(objNameProp[0].getDisplayableValue().equals(revRuleName))
            {
                retRevRule = revRule;
                break;
            }
        }

        return retRevRule;
    }


    /**
     * This function loads the property explicitly which are not part of property policy
     * @param object
     * @param properties
     * @return
     * @throws Exception
     */
    public static Property[] getObjectProperties (ModelObject object, String[] properties) throws Exception
    {
        Property[] retVal = new Property[properties.length];

        com.teamcenter.services.strong.core.DataManagementService m_dmService = com.teamcenter.services.strong.core.DataManagementService.getService(m_connection);
        ServiceData sd = m_dmService.getProperties(new ModelObject[]{object}, properties);
        ModelObject modelObject = sd.getPlainObject(0);

        int i = 0;
        for(String prop : properties)
        {
            retVal[i++] = modelObject.getPropertyObject(prop);
        }
        return retVal;
    }

    /**
     * Creates design artifacts
     * @param nCount number of DAs to be created
     * @param itemName name prefix of DAs to be created
     * @return list of newly created DAs
     */
    public List< ItemRevision > createDesignArtifacts(int nCount, String itemName)
    {
        List< Item > itemList = new ArrayList< Item >();
        List< ItemRevision > revList = new ArrayList< ItemRevision >();
        try
        {
            createItems( nCount, itemName, "Item", itemList, revList );


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return revList;

    }

    /**
     * Creates design artifacts with specified type
     * @param nCount number of DAs to be created
     * @param itemName name prefix of DAs to be created
     * @return list of newly created DAs
     */
    public CreateItemsResponse  createDesignArtifactsWithType( String itemName, String itemType, String desc)
    {
        CreateItemsResponse response = null;
        try
        {
          //  createItems( nCount, itemName, itemtype, itemList, revList );
            List<String> items = new ArrayList<String>();
            List<String> revs = new ArrayList<String>();
            generateItemIdsAndRevIds(1, itemType, items, revs);
            ItemProperties[] itemProps =
                    populateItemProperties(1, itemName, itemType, desc, items, revs);

            response = m_coreDMService.createItems(itemProps, null, "" );
            if ( ! handleServiceData( response.serviceData, "" ) )
            {
                LogFile.write( "createItems: Failed" );
            }
        }
        catch(Exception ex)
        {
            LogFile.write("Item creation failed");
            ex.printStackTrace();
        }
        return response;
    }

    /**
     * Creates MDO with the DA list
     * @param revList revisions of DAs
     * @param mdoName Name of MDO
     * @param mdoDesc Description of MDO
     * @return newly created MDO
     */
    public WorkspaceObject createMDOWithDARevList( List< ItemRevision > revList, String mdoName, String mdoDesc )
    {
        WorkspaceObject mdoCreated = null;
        try
        {
             CreateInput mdoCreateInput = new CreateInput();
             mdoCreateInput.boName = "Mdo0MDThread";
             mdoCreateInput.stringProps.put( "object_name", mdoName );
             mdoCreateInput.stringProps.put( "object_desc", mdoDesc );
             MDOInput mdoInput = new MDOInput();
             mdoInput.clientId = "JAVASampleClientMDO";
             mdoInput.mdoCreInput = mdoCreateInput;

             WorkspaceObject[] designArtifacts = new WorkspaceObject[revList.size()];
             ModelObject[] modelObjs = new ModelObject[revList.size()];

             for( int i = 0; i < revList.size(); ++i )
             {
                 modelObjs[i]= (ModelObject)revList.get(i);
             }

             m_coreDMService.getProperties(modelObjs,new String[] {"object_string"});

             for( int inx = 0; inx < revList.size(); ++inx )
             {
                 designArtifacts[inx] = revList.get(inx);
                 LogFile.write( "Design Artifact named: " + designArtifacts[inx].get_object_string()+ " uid:" + revList.get(inx).getUid() + "  is associated to MDO" );
             }

             mdoInput.artifactInput.addDesignArtifact = designArtifacts;
             MDOInput[] mdoInputArray = new MDOInput[1];
             mdoInputArray[0] = mdoInput;

             CreateOrUpdateMDOResponse response = m_mdoService.createOrUpdateMDO( mdoInputArray );
             if( response != null)
             {
                 if ( ! handleServiceData( response.serviceData, "CreateMDOWithDesignArtifacts" ) )
                 {
                     LogFile.write( "CreateMDOWithDesignArtifacts failed" );
                     return null;
                 }
             }

             if(response!= null && response.mdoOutput != null)
             {
                 mdoCreated = response.mdoOutput[0].mdoObject;
             }

             LogFile.write( "CreateMDOWithDesignArtifacts succeeded" );
             if(mdoName != null && mdoCreated != null)
             {
                 LogFile.write( "MDO named: " + mdoName + " MDO Description: "+ mdoCreated.get_object_desc() +  " uid:" + mdoCreated.getUid() + "  is created" );
             }
        }
        catch(NotLoadedException e)
        {
            e.printStackTrace();
        }
        return mdoCreated;
    }

    /**
     * Creates MDO with the DA list
     * @param daList DAs
     * @param mdoName Name of MDO
     * @param mdoDesc Description of MDO
     * @return newly created MDO
     */
    public WorkspaceObject createMDOWithDAList( List< WorkspaceObject > daList, String mdoName, String mdoDesc )
    {
        WorkspaceObject mdoCreated = null;
        try
        {
             CreateInput mdoCreateInput = new CreateInput();
             mdoCreateInput.boName = "Mdo0MDThread";
             mdoCreateInput.stringProps.put( "object_name", mdoName );
             mdoCreateInput.stringProps.put( "object_desc", mdoDesc );
             MDOInput mdoInput = new MDOInput();
             mdoInput.clientId = "JAVASampleClientMDO";
             mdoInput.mdoCreInput = mdoCreateInput;

             WorkspaceObject[] designArtifacts = new WorkspaceObject[daList.size()];
             ModelObject[] modelObjs = new ModelObject[daList.size()];

             for( int i = 0; i < daList.size(); ++i )
             {
                 modelObjs[i]= (ModelObject)daList.get(i);
             }

             m_coreDMService.getProperties(modelObjs,new String[] {"object_string"});

             for( int inx = 0; inx < daList.size(); ++inx )
             {
                 designArtifacts[inx] = daList.get(inx);
                 LogFile.write( "Design Artifact named: " + designArtifacts[inx].get_object_string()+ " uid:" + daList.get(inx).getUid() + "  is associated to MDO" );
             }

             mdoInput.artifactInput.addDesignArtifact = designArtifacts;
             MDOInput[] mdoInputArray = new MDOInput[1];
             mdoInputArray[0] = mdoInput;

             CreateOrUpdateMDOResponse response = m_mdoService.createOrUpdateMDO( mdoInputArray );
             if( response != null)
             {
                 if ( ! handleServiceData( response.serviceData, "createMDOWithDAList" ) )
                 {
                     LogFile.write( "createMDOWithDAList failed" );
                     return null;
                 }
             }

             if(response!= null && response.mdoOutput != null)
             {
                 mdoCreated = response.mdoOutput[0].mdoObject;
             }

             LogFile.write( "createMDOWithDAList succeeded" );
             if(mdoName != null && mdoCreated != null)
             {
                 LogFile.write( "MDO named: " + mdoName + " MDO Description: "+ mdoCreated.get_object_desc() +  " uid:" + mdoCreated.getUid() + "  is created" );
             }
        }
        catch(NotLoadedException e)
        {
            e.printStackTrace();
        }
        return mdoCreated;
    }


    /**
     * Updates the MDO by adding,removing DAs and modifying mdo attributes
     * @param mdoObjToBeUpdated MDO to be updated
     * @param toAddDAList DAs to be addded
     * @param toRemoveDAList DAs to be removed
     * @param attrs MDO attributes to be updated
     * @return modified MDO
     */
    public WorkspaceObject updateMDO( WorkspaceObject mdoObjToBeUpdated, List< ItemRevision > toAddDAList, List< ItemRevision > toRemoveDAList,Map<String, String[]> attrs )
    {
        WorkspaceObject updatedMDO = null;
        //Checkout the MDO object
        ModelObject[] objToBeCheckedout = new ModelObject[1];
        objToBeCheckedout[0]=mdoObjToBeUpdated;


        ServiceData sData = m_resService.checkout( objToBeCheckedout, "", "" );

        if ( ! handleServiceData( sData, "updateMDO" ) )
        {
            System.out.println( "Checkout failed\n" );
            return null;
        }



        ModelObject[] modelObjs = new ModelObject[toAddDAList.size()+toRemoveDAList.size()];

        for( int i = 0; i < toAddDAList.size(); ++i )
        {
            modelObjs[i]= (ModelObject)toAddDAList.get(i);

        }
        for( int j = 0; j < toAddDAList.size(); ++j )
        {
            modelObjs[toAddDAList.size()+j]= (ModelObject)toAddDAList.get(j);
        }

        m_coreDMService.getProperties(modelObjs,new String[] {"object_string"});

        MDOInput mdoInput2 = new MDOInput();
        mdoInput2.clientId = "MDOUpdate";
        mdoInput2.mdoObject = mdoObjToBeUpdated;

        WorkspaceObject[] addDesignArtifacts = new WorkspaceObject[toAddDAList.size()];
        for(int inx = 0; inx < toAddDAList.size(); ++inx)
        {
            addDesignArtifacts[inx] = toAddDAList.get(inx);

            try
            {

                LogFile.write( "Design Artifact named: " + toAddDAList.get(inx).get_object_string()+ " uid:" + toAddDAList.get(inx).getUid() + "  is attempted to associate to MDO" );
            }
            catch (NotLoadedException e)
            {
                e.printStackTrace();
            }
        }

        WorkspaceObject[] removeDesignArtifacts = new WorkspaceObject[toRemoveDAList.size()];
        for(int inx = 0; inx < toRemoveDAList.size(); ++inx)
        {
            removeDesignArtifacts[inx] = toRemoveDAList.get(inx);
            try
            {
                LogFile.write( "Design Artifact named: " + toRemoveDAList.get(inx).get_object_string()+ " uid:" + toRemoveDAList.get(inx).getUid() + "  is attempted to remove from MDO" );
            }
            catch (NotLoadedException e)
            {
                e.printStackTrace();
            }
        }

        mdoInput2.artifactInput.addDesignArtifact = addDesignArtifacts;
        mdoInput2.artifactInput.removeDesignArtifact = removeDesignArtifacts;
        mdoInput2.attributeValues = attrs;

        MDOInput[] mdoInputArray2 = new MDOInput[1];
        mdoInputArray2[0] = mdoInput2;
        CreateOrUpdateMDOResponse resp2 = m_mdoService.createOrUpdateMDO( mdoInputArray2 );
        if( resp2 != null)
        {
            if ( ! handleServiceData( resp2.serviceData, "updateMDO" ) )
            {
                LogFile.write( "updateMDO failed" );
                sData = m_resService.checkin( objToBeCheckedout );
                if ( ! handleServiceData( sData, "updateMDO" ) )
                {
                    LogFile.write( "Checkin failed" );
                }
                return null;
            }
        }

        if(resp2!= null && resp2.mdoOutput != null)
        {
            updatedMDO =  resp2.mdoOutput[0].mdoObject;
        }

        sData = m_resService.checkin( objToBeCheckedout );

        if ( ! handleServiceData( sData, "updateMDO" ) )
        {
            LogFile.write( "Checkin failed" );
            return null;
        }
        LogFile.write( "updateMDO succeeded" );
        try
        {
            LogFile.write( "MDO named: " + (attrs.get("object_name"))[0] + " MDO Description: "+ updatedMDO.get_object_desc()+" uid:" + updatedMDO.getUid() + "  is updated." );
        }
        catch (NotLoadedException e)
        {
            e.printStackTrace();
        }
        return updatedMDO;
    }

    /**
     * Tests creation of MDO
     * @return newly created MDO
     */
    public WorkspaceObject testCreateMDO()
    {
        List<ItemRevision> revsOfDAs = createDesignArtifacts(3,"DAforCreateTest");
        return createMDOWithDARevList(revsOfDAs,"MDOForCreate", "MDO for Create test");
    }

    /**
     * Tests modification of MDO
     * @return Updated MDO
     */
    public WorkspaceObject testUpdateMDO()
    {
        List<ItemRevision> revsOfDAs = createDesignArtifacts(4,"DAforUpdateTest");
        WorkspaceObject mdoCreated = createMDOWithDARevList(revsOfDAs,"MDOForUpdate", "MDO for Update test");

        List< ItemRevision > toAddDAList = createDesignArtifacts(2, "DAstoAdd");
        List< ItemRevision > toRemoveDAList = new ArrayList<ItemRevision>();
        toRemoveDAList.add(revsOfDAs.get(2));
        toRemoveDAList.add(revsOfDAs.get(3));

        Map<String,String[]> attrs = new HashMap<String,String[]>();
        String[] nameValArr = new String[1];
        nameValArr[0]="MDOAfterUpdate";
        String[] descValArr = new String[1];
        descValArr[0]="MDO created using sample client is now updated";
        attrs.put("object_name",nameValArr);
        attrs.put("object_desc",descValArr);
        return updateMDO(mdoCreated,toAddDAList,toRemoveDAList,attrs);
    }

    /**
     * Searches for MDOs containing exact combination of input DAs
     * @param DAListForSearch DA List
     * @return List of MDOs returned by the search results
     */
    public List<WorkspaceObject> searchMDOsWithOnlyDesigns(List<ItemRevision> DAListForSearch)
    {
        SearchInput[] inputs = new SearchInput[1];
        inputs[0] = new SearchInput();
        inputs[0].clientId = "Search";

        DesignArtifactInputsForSearch designArtifactInput = new DesignArtifactInputsForSearch();

        WorkspaceObject[] designArtifacts = new WorkspaceObject[DAListForSearch.size()];
        for(int inx=0; inx < DAListForSearch.size(); ++inx)
        {
            designArtifacts[inx] = DAListForSearch.get(inx);
            try
            {
                LogFile.write( "Design Artifact named: " + designArtifacts[inx].get_object_string()+ " uid:" + designArtifacts[inx].getUid() + "  is used for MDO search" );
            }
            catch (NotLoadedException e)
            {
                e.printStackTrace();
            }
        }

        designArtifactInput.designArtifactsObjects = designArtifacts;
        inputs[0].designArtifactInputs= designArtifactInput;
        return executeMDOSearch(inputs);
    }

    /**
     * Searches for MDOs containing exact combination of input DAs and property values
     * @param DAListForSearch DA List
     * @param mdoPropertiesCriteria MDO property value map
     * @return List of MDOs returned by the search results
     */
    public List<WorkspaceObject> searchMDOsWithDesignsAndMDOProps(
            List<ItemRevision> DAListForSearch,
            Map<String, String[]> mdoPropertiesCriteria)
    {
        SearchInput[] inputs = new SearchInput[1];

        inputs[0] = new SearchInput();
        inputs[0].clientId = "Search";
        inputs[0].mdoCriteria = mdoPropertiesCriteria;

        DesignArtifactInputsForSearch designArtifactInput = new DesignArtifactInputsForSearch();

        WorkspaceObject[] designArtifacts = new WorkspaceObject[DAListForSearch.size()];
        for(int inx=0; inx < DAListForSearch.size(); ++inx)
        {
            designArtifacts[inx] = DAListForSearch.get(inx);
            try
            {
                LogFile.write( "Design Artifact named: " + designArtifacts[inx].get_object_string()+ " uid:" + designArtifacts[inx].getUid() + "  is used for MDO search" );
            }
            catch (NotLoadedException e)
            {
                e.printStackTrace();
            }
        }
        designArtifactInput.designArtifactsObjects = designArtifacts;
        inputs[0].designArtifactInputs= designArtifactInput;
        return executeMDOSearch(inputs);
    }


    /**
     * Searches for MDOs based on input property values
     * @param mdoPropertiesCriteria MDO property value map
     * @return List of MDOs returned by the search results
     */public List<WorkspaceObject> searchMDOPropsOnly(Map<String, String[]> mdoPropertiesCriteria)
    {
        SearchInput[] inputs = new SearchInput[1];
        inputs[0] = new SearchInput();
        inputs[0].clientId = "Search";
        inputs[0].mdoCriteria = mdoPropertiesCriteria;
        return executeMDOSearch(inputs);
    }

    /**
     * Executes the MDO search
     * @param inputs search input
     * @return  List of MDOs returned by the search results
     */
    private  List<WorkspaceObject> executeMDOSearch(SearchInput[] inputs)
    {
        List<WorkspaceObject> mdoList = new ArrayList<WorkspaceObject>();
        SearchMDOResponse response = m_mdoService.searchMDO( inputs );
        for(int inx= 0; inx < response.mdoSearchOutput[0].mdoOutputs.length; ++inx )
        {
            mdoList.add( response.mdoSearchOutput[0].mdoOutputs[inx].mdoObject );
        }
        return mdoList;
    }

    /**
     * Executes the search MDO 2
     * @param inputs search input
     * @return  List of MDOs returned by the search results
     */
    public List<WorkspaceObject> executeMDOSearch2(SearchInput2[] inputs)
    {
        List<WorkspaceObject> mdoList = new ArrayList<WorkspaceObject>();
        SearchMDO2Response response = m_mdoService.searchMDO2( inputs );

        if(response!= null )
        {
            for(int inx= 0; inx < response.mdoSearchOutput.mdoOutput.length; ++inx )
            {
                mdoList.add( response.mdoSearchOutput.mdoOutput[inx].mdoObject );
            }
        }
        return mdoList;
    }

    /**
     * Executes the search MDO 3
     * @param inputs search input
     * @return response with the search results
     */
    public SearchMDO3Response executeMDOSearch3(SearchInput3[] inputs)
    {
        SearchMDO3Response response = m_mdoService.searchMDO3( inputs );
        return response;
    }

    /**
     * Executes the MDO search
     * @param inputs search input
     * @return  List of the MDOs DAs returned by the search results
     */
    List<WorkspaceObject> executeMDODASearch(SearchInput[] inputs)
    {
        List<WorkspaceObject> daList = new ArrayList<WorkspaceObject>();
        SearchMDOResponse response = m_mdoService.searchMDO( inputs );
        for(int inx= 0; inx < response.mdoSearchOutput[0].mdoOutputs.length; ++inx )
        {
            for (int jnx=0; jnx < response.mdoSearchOutput[0].mdoOutputs[inx].associatedDesignArtifact.length; ++jnx)
            {
                daList.add( response.mdoSearchOutput[0].mdoOutputs[inx].associatedDesignArtifact[jnx] );
            }

        }
        return daList;
    }

    /**
     * Creates a reuse DE by realizing the input object
     * @param object1 the input object
     * @return reuse DE
     */
    public ModelObject createReuseDEWithCDInECN(ModelObject object1, String reuseDeName, ModelObject cd, ModelObject ecnRev )
    {
        ReuseDesignElementInfo reuseDE = new ReuseDesignElementInfo();
        reuseDE.boType = "Cpd0DesignElement";
        reuseDE.clientId = "3";
        reuseDE.attrInfo.put("object_name", new String[] {reuseDeName} );
        reuseDE.attrInfo.put("fnd0ContextProvider", new String[] { ecnRev.getUid() } );

        reuseDE.modelObject = (WorkspaceObject) cd;
        reuseDE.sourceObject = object1;

        CreateOrUpdateDesignElementsInfo info = new CreateOrUpdateDesignElementsInfo();
        info.reuseDesignElements = new ReuseDesignElementInfo[] { reuseDE };

        CreateOrUpdateDesignElementsResponse response = m_dataMgtService.createOrUpdateDesignElements( info );
        if ( ! handleServiceData( response.serviceData, "createReuseDE" ) )
        {
            LogFile.write( "createReuseDE failed" );
            return null;
        }
        ModelObject de = (ModelObject) response.clientIDMap.get( reuseDE.clientId );
        return de;

    }
    /**
     * Creates a reuse DE by realizing the input object
     * @param object1 the input object
     * @return reuse DE
     */
    public ModelObject createReuseDEWithCD(ModelObject object1, String reuseDeName, ModelObject cd )
    {
        ReuseDesignElementInfo reuseDE = new ReuseDesignElementInfo();
        reuseDE.boType = "Cpd0DesignElement";
        reuseDE.clientId = "3";
        reuseDE.attrInfo.put("object_name", new String[] {reuseDeName} );

        reuseDE.modelObject = (WorkspaceObject) cd;
        reuseDE.sourceObject = object1;

        CreateOrUpdateDesignElementsInfo info = new CreateOrUpdateDesignElementsInfo();
        info.reuseDesignElements = new ReuseDesignElementInfo[] { reuseDE };

        CreateOrUpdateDesignElementsResponse response = m_dataMgtService.createOrUpdateDesignElements( info );
        if ( ! handleServiceData( response.serviceData, "createReuseDE" ) )
        {
            LogFile.write( "createReuseDE failed" );
            return null;
        }
        ModelObject de = (ModelObject) response.clientIDMap.get( reuseDE.clientId );
        return de;

    }

    /**
     * Creates a reuse DE by realizing the input object
     * @param count the number of DEs to be created
     * @param object1 the input object
     * @param reuseDeName the name
     * @param cd the application model to which the DE has to be created
     * @return boolean sucess or failure of this method DE
     */
    public CreateOrUpdateDesignElementsResponse createReuseDEWithCDForPerFormance( int count, ModelObject object1, String reuseDeName, ModelObject cd )
    {
        ReuseDesignElementInfo[] reuseDEs = new ReuseDesignElementInfo[count];
        for(int i=0; i < count; ++i)
        {
             ReuseDesignElementInfo reuseDE = new ReuseDesignElementInfo();
             reuseDE.boType = "Cpd0DesignElement";
             reuseDE.clientId = reuseDeName+i;
             reuseDE.attrInfo.put("object_name", new String[] {reuseDeName+i} );

             reuseDE.modelObject = (WorkspaceObject) cd;
             reuseDE.sourceObject = object1;
             reuseDEs[i] = reuseDE;
        }

         CreateOrUpdateDesignElementsInfo info = new CreateOrUpdateDesignElementsInfo();
         info.reuseDesignElements = reuseDEs;

         CreateOrUpdateDesignElementsResponse response = m_dataMgtService.createOrUpdateDesignElements( info );
         if ( ! handleServiceData( response.serviceData, "createReuseDEWithCDForPerFormance" ) )
         {
             LogFile.write( "createReuseDEWithCDForPerFormance failed" );
             return null;
         }

        return response;

    }

    public ModelObject createPromissoryDE( String promDeName, ModelObject cd)
    {
        ModelObject de = null;
        try
        {
            PromissoryDesignElementInfo promissoryDE = new PromissoryDesignElementInfo();
            promissoryDE.boType = "Cpd0DesignElement";
            promissoryDE.clientId = "5";
            promissoryDE.attrInfo.put("object_name", new String[] { promDeName });
            promissoryDE.modelObject = (WorkspaceObject) cd;
            CreateOrUpdateDesignElementsInfo info = new CreateOrUpdateDesignElementsInfo();
            info.promissoryDesignElements = new PromissoryDesignElementInfo[] { promissoryDE };

            CreateOrUpdateDesignElementsResponse resp = m_dataMgtService.createOrUpdateDesignElements(info);
            de = (Cpd0DesignElement) resp.clientIDMap.get(promissoryDE.clientId);

        }
               catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return de;
    }

    public ModelObject createShapeDE( String objName, ModelObject cd )
    {
        ModelObject de = null;
        try
        {
            ShapeDesignElementInfo shapeDE = new ShapeDesignElementInfo();
            shapeDE.boType = "Cpd0DesignElement";
            shapeDE.clientId = "1000";
            shapeDE.attrInfo.put("object_name", new String[] { objName });
            shapeDE.modelObject = (WorkspaceObject) cd;

            CompoundCreateInput shapeDesign = new CompoundCreateInput();
            shapeDesign.createInfo.boType = "Cpd0ShapeDesign";
            shapeDesign.createInfo.attrInfo.put("object_name", new String[] { "shape_name" });
            User m_loginUser = (User) m_connection.getTcSessionInfo().user;;
            m_coreDMService.getProperties(new ModelObject[] { m_loginUser }, new String[] { "home_folder" });
            shapeDesign.folder = m_loginUser.get_home_folder();
            CompoundCreateInfo shapeRevision = new CompoundCreateInfo();
            shapeRevision.boType = "Cpd0ShapeDesignRevision";
            shapeRevision.attrInfo.put("item_revision_id", new String[] { "itemRevisionID" });
            CompoundCreateInfo[] compounds = new CompoundCreateInfo[1];
            compounds[0] = shapeRevision;
            shapeDesign.createInfo.compoundInfo.put("revision", compounds);
            shapeDE.compoundCreateInput = shapeDesign;

            // positioned geometry
            double[] transforms = new double [16]; for ( int i = 0; i < 16;i++ )
            {
                if ( i % 5 == 0 ) transforms[ i ] = 1.0 * i;
            }
            shapeDE.transform = transforms;;

            CreateOrUpdateDesignElementsInfo info = new CreateOrUpdateDesignElementsInfo();
            info.shapeDesignElements = new ShapeDesignElementInfo[] { shapeDE };

            CreateOrUpdateDesignElementsResponse resp = m_dataMgtService.createOrUpdateDesignElements(info);

            de = (Cpd0DesignElement) resp.clientIDMap.get(shapeDE.clientId);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return de;
    }

    /**
     * Creates a Collaboration Design object
     * @param CDName Name of CD object
     * @return Newly created CD object
     */
    public ModelObject createCD(String CDName)
    {
        try
        {
            CreateIn createIn = new CreateIn();
            createIn.clientId = "A";
            createIn.data.boName = "Cpd0CollaborativeDesign";
            createIn.data.stringProps.put("object_name", CDName);
            String cdId = getCpd0CollaborativeDesignId();
            createIn.data.stringProps.put("mdl0model_id", cdId);
            CreateResponse response = m_coreDMService.createObjects(new CreateIn[]{createIn});
            if ( ! handleServiceData( response.serviceData, "createCD" ) )
            {
                LogFile.write( "createCD failed" );
                return null;
            }
            LogFile.write("Cpd0CollaborativeDesign created: " + cdId );
            return  response.output[0].objects[0];
        }
        catch( Exception ex )
        {
            ex.printStackTrace();
        }
        return null;
    }


    /**
     * Returns auto-generated unique id for Cpd0CollaborativeDesign
     * @return auto-generated collaborative design id
     */
    public String getCpd0CollaborativeDesignId()
    {
        AssignInput createIn = new AssignInput();
        createIn.data.clientID = "getCpd0CollaborativeDesignId";
        createIn.data.boType = "Cpd0CollaborativeDesign";
        createIn.operation = "CreateOperation";

        IdManagementService service = IdManagementService.getService(m_connection);

        AssignResponse response = service.autoAssignValues(new AssignInput[]{createIn});
        if ( ! handleServiceData( response.serviceData, "getCpd0CollaborativeDesignId" ) )
        {
            LogFile.write( "getCpd0CollaborativeDesignId failed" );
            return null;
        }
        return (String)(response.output[0].stringProps.get("mdl0model_id"));
    }

    /**
     * Creates a BOMWindow
     * @param itemRev Item Revision
     * @return CreateBOMWindowsResponse for newly created bomwindow
     */
    public CreateBOMWindowsResponse createBomWindow( ItemRevision itemRev )
    {
        //Now create BOMWindow
        CreateBOMWindowsInfo[] createBOMWindowsInfo = new CreateBOMWindowsInfo[1];
        createBOMWindowsInfo[0] = new CreateBOMWindowsInfo();
        createBOMWindowsInfo[0].itemRev = itemRev;

        CreateBOMWindowsResponse createBOMWindowsResponse =
                m_smService.createBOMWindows(createBOMWindowsInfo);
        return createBOMWindowsResponse;
    }

    /**
     * Adds list of Item revisions to BVR
     * @param bomResp CreateBOMWindowsResponse
     * @param childItemRevList List of Item revisions
     * @return AddResponse
     */
    public AddResponse addBomlines(CreateBOMWindowsResponse bomResp, List<ItemRevision> childItemRevList)
    {
        AddParam addBomLineParams[] = new AddParam[1];
        AddParam addInfo = new AddParam();
        addInfo.parent = bomResp.output[0].bomLine;
        addInfo.flags = 0;
        addInfo.toBeAdded = new AddInformation[childItemRevList.size()];
        BOMWindow[] bomWindowArr = new BOMWindow[1];
        bomWindowArr[0] =  bomResp.output[0].bomWindow;

        if( bomWindowArr[0]  != null )
        {
            for(int inx=0; inx < childItemRevList.size(); ++inx)
            {

                addInfo.toBeAdded[inx] = new AddInformation();
                addInfo.toBeAdded[inx].initialValues = new HashMap<String, String>();
                addInfo.toBeAdded[inx].bomView = null;
                addInfo.toBeAdded[inx].element = null;
                addInfo.toBeAdded[inx].line = null;
                addInfo.toBeAdded[inx].itemRev = childItemRevList.get(inx);
            }
            addBomLineParams[0] = addInfo;
            StructureService service = StructureService.getService(m_connection );
            AddResponse response = service.add( addBomLineParams );

            if ( ! handleServiceData( response.serviceData, "addBomlines" ) )
            {
                LogFile.write( "addBomlines failed" );
            }
            else
            {
                LogFile.write( "addBomlines succeeded" );
                m_smService.saveBOMWindows( bomWindowArr );
            }

            return response;
        }
        return null;
    }

    /**
     * Removes bomlines for given Item revisions.
     * @unpublished
     */
    public ServiceData removeBomlines(CreateBOMWindowsResponse bomResp, List<ItemRevision> childItemRevList)
    {
        BOMWindow[] bomWindows = new BOMWindow[1];
        try
        {

            bomWindows[0] = bomResp.output[0].bomWindow;

            CutItemParam input[] = new CutItemParam[1];
            input[0]= new CutItemParam();
            input[0].parent = bomWindows[0];
            input[0].objs = new Item[childItemRevList.size()];
            for(int inx=0; inx < childItemRevList.size(); ++inx)
            {
                input[0].objs[inx]= childItemRevList.get(inx).get_items_tag();
            }

            // Test cutItems
            StructureService service = StructureService.getService(m_connection );
            ServiceData response = service.cutItems( input );
            if ( ! handleServiceData( response, "removeBomlines" ) )
            {
                LogFile.write( "removeBomlines failed" );
            }
            return response;
        }
        catch (Exception exception)
        {
            exception.printStackTrace();

        }
        return null;
    }


    /**
     * Created BVR structure
     * @param topItemRevision Top Item revision
     * @param childItemRevList List of child Item revisions
     * @return
     */
    public CreateBOMWindowsResponse createStructure(ItemRevision topItemRevision,
            List<ItemRevision> childItemRevList)
    {
        CreateBOMWindowsResponse createBOMWindowsResponse = createBomWindow(topItemRevision);
        addBomlines(createBOMWindowsResponse, childItemRevList);
        return createBOMWindowsResponse;

    }

    /**
     * Creates an MDInstanceThread and associates the design instances to it.
     * @param dis list of design instances
     * @return MDInstanceThread object
     */
    public POM_object[] createInstanceThread(List<DesignInstancesData> dis)
    {
        int diCnt = dis.size();
        DesignInstancesInput[] designInstances = new DesignInstancesInput[1];
        designInstances[0] = new DesignInstancesInput();
        designInstances[0].clientId = "MDIThreadCreate";
        designInstances[0].designInstancesData = new DesignInstancesData[diCnt];
        for (int inx=0; inx < diCnt; ++inx )
        {
            designInstances[0].designInstancesData[inx] = dis.get(inx);
        }
        InstancesToLinkResponse response = m_mdoService.linkDesignInstances( designInstances );
        if ( ! handleServiceData( response.serviceData, "createInstanceThread" ) )
        {
            LogFile.write( "createInstanceThread failed" );
            return null;
        }
        return response.output[0].instanceLinkingObject;

    }

    /**
     * Searches for impacted design instances given some design instances.
     * @param dis list of design instances
     * @return impacted design instances
     */
    public LinkedInstances[] searchForLinkedDesignInstances(List<DesignInstancesData> dis)
    {
        int diCnt = dis.size();
        SearchForLinkedInstancesInput[] designInstances = new SearchForLinkedInstancesInput[1];
        designInstances[0] = new SearchForLinkedInstancesInput();
        designInstances[0].clientId = "MDIThreadSearch";
        designInstances[0].designInstanceInput = new DesignInstancesInput();
        designInstances[0].designInstanceInput.designInstancesData = new DesignInstancesData[diCnt];
        for (int inx=0; inx < diCnt; ++inx )
        {
            designInstances[0].designInstanceInput.designInstancesData[inx] = dis.get(inx);
        }
        SearchForLinkedInstancesResponse response = m_mdoService.searchForLinkedDesignInstances( designInstances );
        if ( ! handleServiceData( response.serviceData, "createInstanceThread" ) )
        {
            LogFile.write( "searchImpactedDesignInstances failed" );
            return null;
        }
        return response.output[0].linkedInstances;
    }

    /**
     * Searches for impacted design instances given some design instances.
     * @param dis list of design instances
     * @return impacted design instances along with MDT and association status
     */
    public SearchForLinkedInstances2Response searchForLinkedDesignInstances2(List<DesignInstancesData> dis)
    {
        int diCnt = dis.size();
        SearchForLinkedInstancesInput[] designInstances = new SearchForLinkedInstancesInput[1];
        designInstances[0] = new SearchForLinkedInstancesInput();
        designInstances[0].clientId = "LinkedInstancesSearch";
        designInstances[0].designInstanceInput = new DesignInstancesInput();
        designInstances[0].designInstanceInput.designInstancesData = new DesignInstancesData[diCnt];
        for (int inx=0; inx < diCnt; ++inx )
        {
            designInstances[0].designInstanceInput.designInstancesData[inx] = dis.get(inx);
        }
        SearchForLinkedInstances2Response response = m_mdoService.searchForLinkedDesignInstances2( designInstances );
        if ( ! handleServiceData( response.serviceData, "searchForLinkedDesignInstances2" ) )
        {
            LogFile.write( "searchForLinkedDesignInstances2 failed" );
            return null;
        }
        return response;
    }

     /**
     * Prints the SearchForLinkedInstances2Response
     * @param resp1 the SearchForLinkedInstances2Response object to be printed
     * @return
     */
     public void printSearchForLinkedInstances2Response(SearchForLinkedInstances2Response resp1)
     {
         try
         {
            if (resp1!=null && resp1.output.length > 0 )
            {
                LogFile.write("Response output count: " + resp1.output.length );

                com.teamcenter.soa.client.model.Property[] objNameProp2;
                if( resp1.output[0].linkedInstances.length > 0 && resp1.output[0].linkedInstances[0].mdoObjectsLinkedToMDI.length > 0)
                {
                    WorkspaceObject mdoObject = resp1.output[0].linkedInstances[0].mdoObjectsLinkedToMDI[0];
                    objNameProp2 = MDOTestUtils.getObjectProperties(mdoObject, new String[] {"object_name"});
                    LogFile.write( "MDThread named: " + objNameProp2[0].getDisplayableValue() + " uid: " + mdoObject.getUid() + "  is Linked to MDI." );
                }
                if ( resp1.output[0].linkedInstances.length > 0 )
                {
                    ImpactedDesignInstanceInfo2[] diInfos = resp1.output[0].linkedInstances[0].instancesInfo;
                    if(diInfos.length > 0)
                    {
                         LogFile.write( "No of linked Design Instances are: "+diInfos.length );
                         LogFile.write("Associated Design Instances Details:\n{");
                         for(int inx=0 ; inx < diInfos.length ; inx++)
                         {
                             LogFile.write( "    Design Instance named: "+diInfos[inx].designInstance.get_object_string() + " uid: " + diInfos[inx].designInstance.getUid()  + " Needs Validation status: "+diInfos[inx].needsValidation +" Is Validated status: "+ diInfos[inx].isValidated );
                             LogFile.write( "    Relevant Domains:");
                             for(int z = 0; z < diInfos[inx].relevantDomains.length; z++ )
                             {
                                 LogFile.write( "        " + diInfos[inx].relevantDomains[z] );
    
                             }
    
                             LogFile.write( "    Irrelevant Domains:");
                             for(int z = 0; z < diInfos[inx].irrelevantDomains.length; z++ )
                             {
                                 LogFile.write( "        " + diInfos[inx].irrelevantDomains[z] );
    
                             }
                         }
                         LogFile.write("}");
                    }
                }
            }
         }
         catch (Exception ex)
         {
             ex.printStackTrace();
         }

     }


    /**
     * Searches for impacted design instances given some design instances.
     * @param dis list of design instances
     * @return impacted design instances along with MDT and association status
     */
    public UpdateMDInstanceToMDThreadResponse updateMDTonMDI(ModelObject mdiObject, WorkspaceObject mdoToAdd, WorkspaceObject mdoToRemove )
    {
         MDInstanceToMDThreadInput[] input1 = new MDInstanceToMDThreadInput [1];
         input1[0] = new  MDInstanceToMDThreadInput();

         input1[0].clientId = "TestUpdateMDTonMDI";
         input1[0].mdiObject = (POM_object) mdiObject;
         if(mdoToRemove != null)
         {
              WorkspaceObject[] mdosToRemove = new WorkspaceObject[1];
              mdosToRemove[0] =  mdoToRemove;
              input1[0].removeMDOObjects = mdosToRemove;
         }

         if(mdoToAdd != null)
         {
              WorkspaceObject[] mdosToAdd = new WorkspaceObject[1];
              mdosToAdd[0] =  mdoToAdd;
              input1[0].addMDOObjects = mdosToAdd;
         }

         UpdateMDInstanceToMDThreadResponse resp3 = m_mdoService.updateMDInstanceToMDThreadLink (input1);
         if ( ! handleServiceData( resp3.serviceData, "UpdateMDTonMDI" ) )
         {
             LogFile.write( "UpdateMDTonMDI failed" );
             return null;
         }
        return resp3;
    }



    /**
     * Searches for associated MDOs given a list of design instances.
     * @param dis list of design instances
     * @return MDOs
     */
    public MDOSearchOutput[] searchForMDO(List<DesignInstancesData> dis)
    {
        int diCnt = dis.size();
        SearchForDesignArtifactInput[] searchInstances = new SearchForDesignArtifactInput[1];
        searchInstances[0] = new SearchForDesignArtifactInput();
        searchInstances[0].clientId = "MDOSearchDI";
        searchInstances[0].designInstances = new POM_object[diCnt];
        for (int inx=0; inx < diCnt; ++inx )
        {
            searchInstances[0].designInstances[inx] = dis.get(inx).designInstance;
        }
        SearchMDOResponse response = m_mdoService.searchForArtifactsUsingInstances( searchInstances );
        if ( ! handleServiceData( response.serviceData, "createInstanceThread" ) )
        {
            LogFile.write( "searchImpactedDesignInstances failed" );
            return null;
        }
        return response.mdoSearchOutput;
    }

    /**
     * Searches for associated MDOs given a list of design instances and filter for Domain.
     * @param dis list of design instances
     * @return MDOs
     */
    public MDOSearchOutput2 searchForMDO2(List<DesignInstancesData> dis, String filterByDomain)
    {
        int diCnt = dis.size();
        SearchForDesignArtifactInput2[] searchInstances = new SearchForDesignArtifactInput2[1];
        searchInstances[0] = new SearchForDesignArtifactInput2();
        searchInstances[0].clientId = "MDOSearchDIDomain";
        searchInstances[0].filterByDomain = filterByDomain;
        searchInstances[0].designInstances = new POM_object[diCnt];
        for (int inx=0; inx < diCnt; ++inx )
        {
            searchInstances[0].designInstances[inx] = dis.get(inx).designInstance;
        }
        SearchMDO2Response response = m_mdoService.searchForArtifactsUsingInstances2( searchInstances );
        if ( ! handleServiceData( response.serviceData, "createInstanceThread" ) )
        {
            LogFile.write( "searchImpactedDesignInstances failed" );
            return null;
        }
        return response.mdoSearchOutput;
    }

    /**
     * Searches for associated MDOs given a list of design instances along with additional information on relation and relevant and irrelevant domains .
     * @param dis list of design instances
     * @param domain domain for which filter is to be applied
     * @param filterForRelDomain flag to indicate it is for relevant or irrelevant domain
     * @return MDOs
     */
    public MDOSearchOutput3[] searchForMDOUsingDIs(List<WorkspaceObject> dis, String domain, boolean filterForRelDomain )
    {
         int diCnt = dis.size();
         SearchForDesignArtifactInput3[] srchDesignArtInput = new SearchForDesignArtifactInput3[1];
         srchDesignArtInput[0] = new SearchForDesignArtifactInput3();
         srchDesignArtInput[0].clientId = "SRCH_DA_01";
         srchDesignArtInput[0].designInstances = new POM_object[diCnt];
         for (int inx=0; inx < diCnt; ++inx )
         {
             srchDesignArtInput[0].designInstances[inx] = dis.get(inx);
         }
         if(!domain.isEmpty())
         {
             FilterForDomainRelevancy filterForRelavancy = new FilterForDomainRelevancy();
             filterForRelavancy.domain = domain;
             filterForRelavancy.isFilterForRelevantDomain = filterForRelDomain;
             srchDesignArtInput[0].filterForDomainRelevancy = filterForRelavancy;
         }
         SearchMDO3Response searchResponse = m_mdoService.searchForArtifactsUsingInstances3( srchDesignArtInput );

         if ( ! handleServiceData( searchResponse.serviceData, "searchForMDOUsingDIs" ) )
         {
             LogFile.write( "Search for MDO using searchForArtifactsUsingInstances3 failed" );
             return null;
         }
         return searchResponse.mdoSearchOutput;

    }


    /**
     * Unlink design instances from an MDInstanceThread.
     * @param dis list of design instances
     * @return MDInstanceThread object
     */
    public int unlinkFromInstanceThread(List<DesignInstancesData> dis)
    {
        int diCnt = dis.size();
        DesignInstancesInput[] designInstances = new DesignInstancesInput[1];
        designInstances[0] = new DesignInstancesInput();
        designInstances[0].clientId = "MDIThreadUnlink";
        designInstances[0].designInstancesData = new DesignInstancesData[diCnt];
        for (int inx=0; inx < diCnt; ++inx )
        {
            designInstances[0].designInstancesData[inx] = dis.get(inx);
        }
        InstancesToUnlinkResponse response = m_mdoService.unlinkDesignInstances( designInstances );
        if ( ! handleServiceData( response.serviceData, "unlinkFromInstanceThread" ) )
        {
            LogFile.write( "unlinkFromInstanceThread errored" );
            return 0;
        }
        return response.serviceData.sizeOfDeletedObjects();

    }



    /**
     * Creates a Project/Context object
     * @param prjName Name of project
     * @param prjDesc Description of project
     * @param prjId Id of project
     * @return Newly created project/context object
     */
    public POM_object createProject(String prjName , String prjDesc )
    {
        try
        {
            String prjId = "Proj"+ generator.nextInt();
            User[] users = getUsers();
            GroupMember[] grpMembers = getGroupMembers();
            TeamMemberInfo[] teamMemInfo = new TeamMemberInfo[3];
            teamMemInfo[0] = new TeamMemberInfo();
            teamMemInfo[0].teamMember = grpMembers[0];
            teamMemInfo[0].teamMemberType = 0;

            teamMemInfo[1] = new TeamMemberInfo();
            teamMemInfo[1].teamMember = grpMembers[1];
            teamMemInfo[1].teamMemberType = 0;

            teamMemInfo[2] = new TeamMemberInfo();
            teamMemInfo[2].teamMember = users[0];
            teamMemInfo[2].teamMemberType = 2;

            ProjectInformation[] prjInfo = new ProjectInformation[1];
            prjInfo[0] = new ProjectInformation();
            prjInfo[0].clientId = "2";
            prjInfo[0].projectDescription = prjDesc;
            prjInfo[0].projectId = prjId;
            prjInfo[0].projectName = prjName + prjId;
            prjInfo[0].teamMembers =  teamMemInfo;
            prjInfo[0].visible = true;
            prjInfo[0].active = true;
            prjInfo[0].useProgramContext = false;
            ProjectOpsResponse resp =  m_prjLevSecService.createProjects( prjInfo );
            return  resp.projectOpsOutputs[0].project;
        }
        catch( Exception ex )
        {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Find the users
     * @return user array
     */
    private User[] getUsers()
    {
        User[] users  = new User[1];
        try
        {
            users[0] = findUser("tcadmin" );

        }
        catch( Exception e )
        {
            e.printStackTrace();
        }

        return users;
    }

    /**
     * Find the group Members
     * @return group member array
     */
    private GroupMember[] getGroupMembers()
    {
        GroupMember[] grpMembs = new GroupMember[2];
        try
        {
            grpMembs[0] = findGroupMember( "dba", "DBA", "tcadmin" );
            grpMembs[1] = findGroupMember( "dba", "DBA", "infodba" );
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }

        return grpMembs;
    }

    /**
     * Find the group Member
     * @param group: group name
     * @param role: role
     * @param userId: user name
     * @return Group member
     */
    public GroupMember findGroupMember(String group, String role, String userId) throws Exception
    {
        GroupMember groupMember = null;
        SavedQueryObject queryObject = getSavedQuery("__EINT_group_members");

        SavedQueryService savedQueryService = SavedQueryService.getService(MDOTestUtils.m_connection);
        SavedQueryInput[] inputs = new SavedQueryInput[1];

        inputs[0] = new SavedQueryInput();
        inputs[0].query = queryObject.query;
        inputs[0].entries = new String[3];
        inputs[0].values = new String[3];
        inputs[0].entries[0] = "Group";
        inputs[0].values[0] = group;
        inputs[0].entries[1] = "Role";
        inputs[0].values[1] = role;
        inputs[0].entries[2] = "User";
        inputs[0].values[2] = userId;

        inputs[0].maxNumToInflate = -1;
        ExecuteSavedQueriesResponse response = savedQueryService.executeSavedQueries(inputs);

        groupMember = (GroupMember ) response.arrayOfResults[0].objects[0];
        m_coreDMService.loadObjects( new String[] {groupMember.getUid()});

        return groupMember;
    }

    /**
     * Find the User
     * @param userId: user name
     * @return Group member
     */
    public User findUser(String userId) throws Exception
    {
        User user = null;
        SavedQueryObject queryObject = getSavedQuery("__WEB_find_user");


        SavedQueryService savedQueryService = SavedQueryService.getService(MDOTestUtils.m_connection);
        SavedQueryInput[] inputs = new SavedQueryInput[1];

        inputs[0] = new SavedQueryInput();
        inputs[0].query = queryObject.query;
        inputs[0].entries = new String[1];
        inputs[0].values = new String[1];
        inputs[0].entries[0] = "User ID";
        inputs[0].limitList = new ModelObject[0];
        inputs[0].values[0] = userId;
        inputs[0].limitListCount = 0;
        inputs[0].maxNumToInflate = 1;
        SavedQueryResults queryResultResp = null;
        ExecuteSavedQueriesResponse savedQueryResult = null;
        try
        {
            savedQueryResult = savedQueryService.executeSavedQueries(inputs);

            if (savedQueryResult.arrayOfResults.length > 0)
            {
                queryResultResp = savedQueryResult.arrayOfResults[0];
            }
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
        }
        if( queryResultResp != null )
        {
            user = (User) queryResultResp.objects[0];
        }
        return user;
    }

    /**
     * Fetch Saved Query object
     * @return Saved query object
     */
    private SavedQueryObject getSavedQuery(String queryName) throws Exception
    {
        if(m_savedQueryObjectMap == null)
        {
            m_savedQueryObjectMap = new HashMap<String, SavedQueryObject>();
            SavedQueryService savedQueryService = SavedQueryService
                    .getService(MDOTestUtils.m_connection);
            GetSavedQueriesResponse response = savedQueryService
                    .getSavedQueries();
            for( int i = 0; i < response.queries.length; i++ )
            {
                m_savedQueryObjectMap.put(response.queries[i].name, response.queries[i]);
            }
        }

        return m_savedQueryObjectMap.get(queryName);
    }

    /**
     * Creates precise/imprecise linking of input list of designElements for given context.
     * @param designElements
     * @param context
     * @param isPrecise
     * @return
     */
    public POM_object[] linkDesignInstances( List<ModelObject> designElements, POM_object context, boolean isPrecise)
    {
        DesignInstancesInput[] DIinputs = new DesignInstancesInput[1];
        DIinputs[0] = new DesignInstancesInput();
        DIinputs[0].clientId="Link";

        DesignInstancesData[] DIinputData = new DesignInstancesData[designElements.size()];
        for(int inx = 0; inx < designElements.size(); ++inx)
        {
            DIinputData[inx] = new DesignInstancesData();
            DIinputData[inx].designInstance = (POM_object) designElements.get(inx);
            DIinputData[inx].isPreciseLink = isPrecise;
        }

        DIinputs[0].context = context;
        DIinputs[0].designInstancesData = DIinputData;
        InstancesToLinkResponse respLink = m_mdoService.linkDesignInstances(DIinputs);
        return respLink.output[0].instanceLinkingObject;
    }


    /**
     * Creates precise/imprecise linking of input list of designElements for given context with MDO input.
     * @param designElements
     * @param context
     * @param isPrecise
     * @return
     */
    public InstancesToLinkResponse2 linkDesignInstances2( List<WorkspaceObject> designElements, POM_object context, boolean isPrecise, WorkspaceObject mdo)
    {
        DesignInstancesInput2[] DIinputs = new DesignInstancesInput2[1];
        DIinputs[0] = new DesignInstancesInput2();
        DIinputs[0].clientId="Link";

        DesignInstancesData[] DIinputData = new DesignInstancesData[designElements.size()];
        for(int inx = 0; inx < designElements.size(); ++inx)
        {
            DIinputData[inx] = new DesignInstancesData();
            DIinputData[inx].designInstance = (POM_object) designElements.get(inx);
            DIinputData[inx].isPreciseLink = isPrecise;
        }

        DIinputs[0].context = context;
        DIinputs[0].designInstancesData = DIinputData;
        DIinputs[0].mdoObject = mdo;
        InstancesToLinkResponse2 resp = m_mdoService.linkDesignInstances2(DIinputs);

        if ( ! handleServiceData( resp.serviceData, "linkDesignInstances2" ) )
        {
            LogFile.write( "linkDesignInstances2 failed" );
            return null;
        }

        return resp;
    }

    /**
    * Executes the Notifications query
    * @param mdoNotifInputs the array of QryInputByDesignOrProject
    * @return response the QryNotificationByDesignResponse returned by the query
    */
    public QryNotificationByDesignResponse qryNotificationsByDesignOrProject(QryInputByDesignOrProject[] mdoNotifInputs)
    {
        QryNotificationByDesignResponse response =  m_mdoService.qryNotificationsByDesignOrProject(mdoNotifInputs);
        return response;
    }


    /**
    * Executes the Notification query
    * @param mdoNotifInputs the array of MDONotificationQueryInput
    * @return response the QueryMDONotificationResponse returned by the query
    */
    public QueryNotificationResponse queryForMDONotifByOriginatingDesign(NotificationQueryInput[] mdoNotifInputs)
    {
        QueryNotificationResponse response =  m_mdoService.qryNotificationsByOriginDesign(mdoNotifInputs);
        return response;
    }

    /**
    * Executes the Notification query
    * @param mdoNotifInputs the array of MDONotificationQueryInput
    * @return response the QueryMDONotificationResponse returned by the query
    */
    public QueryNotificationResponse queryForMDONotifByImpactedDesign(NotificationQueryInput[] mdoNotifInputs)
    {
        QueryNotificationResponse response =  m_mdoService.qryNotificationsByImpactedDesign(mdoNotifInputs);
        return response;
    }

    /**
     * Prints the output of the Notification query
     * @param response the QueryMDONotificationResponse returned by the query that is to be printed
     * @return
     */
     public void printNotifQuery(QueryNotificationResponse response)
     {
         try
         {
             if(response.output.length > 0)
             {
                 int curNotifCnt = 0;
                 LogFile.write("{" );
                 LogFile.write ( " Create Notifications found = " + response.output[0].createNotificationOutputs.length );
                 curNotifCnt += response.output[0].createNotificationOutputs.length;
                 if ( response.output[0].createNotificationOutputs.length > 0 )
                 {
                     LogFile.write(" {");
                 }
                 for(int inx= 0; inx < response.output[0].createNotificationOutputs.length; ++inx )
                 {
                     CreateNotificationOutput createNotificationOutput = response.output[0].createNotificationOutputs[inx];
                     // Get Trigger Notification Object
                     MDOInitialNotification createTriggerNotification = createNotificationOutput.createTriggerNotification;
                     printMDOInitialNotification( createTriggerNotification );

                     //Get instance Thread
                     if(createNotificationOutput.instanceThread != null)
                     {
                         LogFile.write("  Instance Thread: " + createNotificationOutput.instanceThread.getUid());
                     }

                     //Get linked Instances
                     LogFile.write("  Linked Instances = " + createNotificationOutput.linkedInstances.length );
                     if ( createNotificationOutput.linkedInstances.length > 0 )
                     {
                         LogFile.write("  {");
                     }
                     for(int jnx = 0 ; jnx < createNotificationOutput.linkedInstances.length; ++jnx)
                     {
                         if(createNotificationOutput.linkedInstances[jnx] != null)
                         {
                             LogFile.write("   Linked instance: " + createNotificationOutput.linkedInstances[jnx].getUid());
                         }
                     }
                     if ( createNotificationOutput.linkedInstances.length > 0 )
                     {
                         LogFile.write("  }");
                     }

                     //Get MDO Output
                     LogFile.write("  MDO Output = " + createNotificationOutput.mdoOutput.length );
                     if ( createNotificationOutput.mdoOutput.length > 0 )
                     {
                         LogFile.write("  {");
                     }
                     ModelObject[] modelObjs = new ModelObject[1];
                     for(int jnx = 0 ; jnx < createNotificationOutput.mdoOutput.length; ++jnx)
                     {
                         if( createNotificationOutput.mdoOutput[jnx] != null)
                         {
                             MDOOutput2 mdoOutput2 = createNotificationOutput.mdoOutput[jnx];
                             modelObjs[0]= (ModelObject) mdoOutput2.mdoObject;
                             m_coreDMService.getProperties(modelObjs,new String[] {"object_string"});
                             LogFile.write("   MDO: "+  mdoOutput2.mdoObject.getUid() + "::" + mdoOutput2.mdoObject.get_object_string());
                             // Print associated DAs
                             for(int knx = 0 ; knx <mdoOutput2.associatedDesignArtifact.length; ++knx)
                             {
                                 if( mdoOutput2.associatedDesignArtifact[knx] != null)
                                 {
                                     modelObjs[0]= (ModelObject) mdoOutput2.associatedDesignArtifact[knx].designArtifact;
                                     m_coreDMService.getProperties(modelObjs,new String[] {"object_string"});
                                     LogFile.write("   Associated DA: " + mdoOutput2.associatedDesignArtifact[knx].designArtifact.getUid() + "::" + mdoOutput2.associatedDesignArtifact[knx].designArtifact.get_object_string());
                                     LogFile.write("   DA Domain: " + mdoOutput2.associatedDesignArtifact[knx].domain);
                                 }
                             }
                         }
                     }
                     if ( createNotificationOutput.mdoOutput.length > 0 )
                     {
                         LogFile.write("  }");
                     }
                 }
                 if ( response.output[0].createNotificationOutputs.length > 0 )
                 {
                     LogFile.write(" }");
                 }

                 LogFile.write ( " Modify Notifications found = " + response.output[0].modifyNotificationOutputs.length );
                 curNotifCnt += response.output[0].modifyNotificationOutputs.length;
                 if ( response.output[0].modifyNotificationOutputs.length > 0 )
                 {
                     LogFile.write(" {");
                 }
                 for(int inx= 0; inx < response.output[0].modifyNotificationOutputs.length; ++inx )
                 {
                     ModifyNotificationOutput modifyNotificationOutput = response.output[0].modifyNotificationOutputs[inx];
                     // Get Trigger Notification Object
                     LogFile.write("  MDO Modify Trigger Objects = " + modifyNotificationOutput.modifyTriggerNotifications.length );
                     for(int jnx = 0 ; jnx < modifyNotificationOutput.modifyTriggerNotifications.length; ++jnx)
                     {
                         MDOInitialNotification modifyTriggerNotification = modifyNotificationOutput.modifyTriggerNotifications[jnx];
                         printMDOInitialNotification( modifyTriggerNotification );
                     }


                     //Get instance Thread
                     if( modifyNotificationOutput.instanceThread !=null )
                     {
                         LogFile.write("  Instance Thread: " + modifyNotificationOutput.instanceThread.getUid());
                     }

                     //Get linked Instances
                     LogFile.write("  Linked Instances = " + modifyNotificationOutput.linkedInstances.length );
                     for(int jnx = 0 ; jnx < modifyNotificationOutput.linkedInstances.length; ++jnx)
                     {
                         if( modifyNotificationOutput.linkedInstances[jnx] !=null )
                         {
                             LogFile.write("    Linked instance: " + modifyNotificationOutput.linkedInstances[jnx].getUid());
                         }
                     }
                 }
                 if ( response.output[0].modifyNotificationOutputs.length > 0 )
                 {
                     LogFile.write(" }");
                 }
                 LogFile.write("}" );
                 LogFile.write("Total New Notifications=" + curNotifCnt);
             }
             else
             {
                 LogFile.write("No Search results returned");
             }
         }
         catch (Exception ex)
         {
             ex.printStackTrace();
         }
     }
     /**
     * Prints the output of the qryNotificationsByDesignOrProject query
     * @param response the QueryMDONotificationResponse returned by the query that is to be printed
     * @return
     */
     public void printQueryResp(QryNotificationByDesignResponse response)
     {
         try
         {
             if(response.output.length > 0)
             {
                 int curNotifCnt = 0;
                 LogFile.write ( "Notifications found = " + response.output[0].notificationOutputs.length );
                 curNotifCnt = response.output[0].notificationOutputs.length;
                 if ( response.output[0].notificationOutputs.length > 0 )
                 {
                     LogFile.write("{");
                 }
                 for(int inx= 0; inx < response.output[0].notificationOutputs.length; ++inx )
                 {
                     QryNotificationOutputByDesign notificationOutput = response.output[0].notificationOutputs[inx];

                     for(int jnx=0; jnx < notificationOutput.notificationByDesignOutputs.length; ++jnx)
                     {
                         MDOInitialNotification aNotification = notificationOutput.notificationByDesignOutputs[jnx];
                         printMDOInitialNotification( aNotification );
                     }
                     if(notificationOutput.designObject != null)
                     {
                         ModelObject[] modelObjs = new ModelObject[1];
                         modelObjs[0]= (ModelObject) notificationOutput.designObject;
                         m_coreDMService.getProperties(modelObjs,new String[] {"object_string"});
                         LogFile.write("  Design Object: " + notificationOutput.designObject.getUid() +"::" + notificationOutput.designObject.get_object_string());
                     }
                 }
                 if ( response.output[0].notificationOutputs.length > 0 )
                 {
                     LogFile.write("}");
                 }
                 LogFile.write("Total New Notifications=" + curNotifCnt);
             }
             else
             {
                 LogFile.write("No Search results returned");
             }
         }
         catch (Exception ex)
         {
             ex.printStackTrace();
         }
     }

     /**
     * Prints the MDO Initial Notification details
     * @param createTriggerNotification the MDOInitialNotification object to be printed
     * @return
     */
     private void printMDOInitialNotification(MDOInitialNotification createTriggerNotification)
     {
         try
         {
             LogFile.write("  Initial Notification" );
             LogFile.write("  {" );
             // Get notification object
             if(createTriggerNotification.notificationObject != null)
             {
                 LogFile.write("    Notification object: " + createTriggerNotification.notificationObject.getUid());

             }

             // Get InitialActionHappened
             LogFile.write("    NotificationQualifier: " + createTriggerNotification.notificationQualifier);

             // Get designInstance
             if(createTriggerNotification.designInstance != null)
             {
                 ModelObject[] modelObjs = new ModelObject[1];
                 modelObjs[0]= (ModelObject) createTriggerNotification.designInstance;
                 m_coreDMService.getProperties(modelObjs,new String[] {"object_string"});
                 LogFile.write("    Design Instance: " + createTriggerNotification.designInstance.getUid() +"::" + createTriggerNotification.designInstance.get_object_string());
             }

             //Get Accepted Responses
             LogFile.write("    Accepted Responses = " + createTriggerNotification.acceptedResponses.length );
             for(int jnx = 0 ; jnx < createTriggerNotification.acceptedResponses.length; ++jnx)
             {
                 MDONotificationDetails mdoNotifactionDetails = createTriggerNotification.acceptedResponses[jnx];
                 printMDONotificationDetails(mdoNotifactionDetails);
             }

             //Get Rejected Responses
             LogFile.write("    Rejected Responses = " + createTriggerNotification.rejectedResponses.length );
             for(int jnx = 0 ; jnx < createTriggerNotification.rejectedResponses.length; ++jnx)
             {
                 MDONotificationDetails mdoNotifactionDetails = createTriggerNotification.rejectedResponses[jnx];
                 printMDONotificationDetails(mdoNotifactionDetails);
             }
             //Get Ignored Responses
             LogFile.write("    Ignored Responses = " + createTriggerNotification.ignoredResponses.length );
             for(int jnx = 0 ; jnx < createTriggerNotification.ignoredResponses.length; ++jnx)
             {
                 MDONotificationDetails mdoNotifactionDetails = createTriggerNotification.ignoredResponses[jnx];
                 printMDONotificationDetails(mdoNotifactionDetails);
             }
             LogFile.write("  }" );
         }
         catch (Exception ex)
         {
             ex.printStackTrace();
         }

     }

     /**
     * Prints the MDO Notification details
     * @param mdoNotifactionDetails the MDONotificationDetails object to be printed
     * @return
     */
     private void printMDONotificationDetails(MDONotificationDetails mdoNotifactionDetails)
     {
         try
         {
             LogFile.write("      {" );
             // Get action Happened
             LogFile.write("        Notification Qualifier: " + mdoNotifactionDetails.notificationQualifier);

             // Get Triggerring Component
             if(  mdoNotifactionDetails.triggeringComponent !=null )
             {
                 ModelObject[] modelObjs = new ModelObject[1];
                 modelObjs[0]= (ModelObject) mdoNotifactionDetails.triggeringComponent;
                 m_coreDMService.getProperties(modelObjs,new String[] {"object_string"});
                 LogFile.write("        Triggering Component: " + mdoNotifactionDetails.triggeringComponent.getUid() + "::" + mdoNotifactionDetails.triggeringComponent.get_object_string());
             }

             // Get Model
             if( mdoNotifactionDetails.model !=null )
             {
                 ModelObject[] modelObjs = new ModelObject[1];
                 modelObjs[0]= (ModelObject) mdoNotifactionDetails.model;
                 m_coreDMService.getProperties(modelObjs,new String[] {"object_string"});
                 LogFile.write("        Model: " + mdoNotifactionDetails.model.getUid() + "::" + mdoNotifactionDetails.model.get_object_string());
             }


             // Get design
             if( mdoNotifactionDetails.objectName !=null )
             {
                 LogFile.write("        Object Name: " + mdoNotifactionDetails.objectName);
             }
             LogFile.write("      }" );
         }
         catch (Exception ex)
         {
             ex.printStackTrace();
         }

     }

    /**
    * Calls the  updateMDONotification SOA
    * @param mdoNotifUpdInput the array of MDONotificationUpdateInput
    * @return updatdResp response object
    */
    public UpdateMDONotificationResponse updateMDONotification(MDONotificationUpdateInput[] mdoNotifUpdInput)
    {
         UpdateMDONotificationResponse updatdResp = m_mdoService.updateMDONotification( mdoNotifUpdInput );
         return updatdResp;

    }

    /**
    * Updates property of input objects
    * @param objects objects to be updated
    * @param propMap map of attribute name and value
    * @return
    */
    public void setPropertiesOfObject( ModelObject[] objects, Map<String, String> propMap )
    {
        m_coreDMService.setDisplayProperties( objects, propMap );
    }

    /**
    * Unrealizes the specified design element
    * @param deToBeUnrealized The design element to be unrealized
    * @return
    */
    public void unrealizeDE(ModelObject[] deToBeUnrealized)
    {
        m_coreDMService.deleteObjects(deToBeUnrealized);
    }

    /**
    * Creates and attaches an attribute group to a design element
    * @param attrGrpName the attribute group name
    * @param de the design element
    * @param cd the collaborative design
    * @return ag the created attribute group
    */
    public void assignToProject(ModelObject itemRevision, POM_object proj5)
    {
        AssignedOrRemovedObjects[] objs = new AssignedOrRemovedObjects[1];
        objs[0] = new AssignedOrRemovedObjects();

        ModelObject[] itemRevs = new ModelObject[1];
        itemRevs[0] = itemRevision;
        objs[0].objectToAssign = itemRevs;

        TC_Project[] projects = new TC_Project[1];
        projects[0] = (TC_Project) proj5;
        objs[0].projects = projects;
        m_prjLevSecService.assignOrRemoveObjects( objs );
    }
    // Commenting out below code
    // there is no out of the box attribute group type defined.
    // To use the Attribute attach and detach test cases with your own custom Attribute Group object,
    // replace Cpd9TestAttrGroup with your own type name in the file MDOTestUtils.java,
    // and uncomment below methods and the code in MDOManagement.java
    /**
    * Creates an attribute group to a design element
    * @param attrGrpName the attribute group name
    * @param cd the collaborative design
    * @return ag the created attribute group
    */
/*  public ModelObject createAttrGrp(String attrGrpName, ModelObject cd)
    {
        CreateIn[] agInput = new CreateIn[1];
        agInput[0] = new CreateIn();
        agInput[0].clientId = "createAttrGroup";
        agInput[0].data = new DataManagement.CreateInput();
        agInput[0].data.boName = "Cpd9TestAttrGroup";
        agInput[0].data.stringProps.put("object_name", attrGrpName);
        agInput[0].data.tagProps.put("mdl0model_object", cd);
        CreateResponse response = null;
        try
        {
            response = m_coreDMService.createObjects(agInput);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        if( response!=null && response.output!=null && response.output.length != 0 )
        {
            return response.output[0].objects[0];
        }
        return null;
    }
*/

    /**
     * Attaches an attribute group to a design element
     * @param ag the attribute group
     * @param de the design element
     */
 /*  public void attachAttrGrp(ModelObject ag, ModelObject de)
     {
         Relationship[] relInput = new Relationship[1];
         relInput[0] = new Relationship();
         relInput[0].clientId = "attrGroup";
         relInput[0].primaryObject = de;
         relInput[0].relationType = "Mdl0AttachAttrGroup";
         relInput[0].secondaryObject = ag;
         m_coreDMService.createRelations(relInput);
     }

    public void detachAndDeleteAttrGrp(ModelObject de, ModelObject ag)
    {
        Relationship[] relInput = new Relationship[1];
        relInput[0] = new Relationship();
        relInput[0].clientId = "attrGroup";
        relInput[0].primaryObject = de;
        relInput[0].relationType = "Mdl0AttachAttrGroup";
        relInput[0].secondaryObject = ag;
        m_coreDMService.deleteRelations(relInput);
        // Delete attribute group (ag) too.
        ModelObject[] mos = new ModelObject[1];
        mos[0] = ag;
        m_coreDMService.deleteObjects(mos);
    }
*/
    /**
    * Updates the design element with specified source object
    * @param dATestRev the new source object
    * @param deObj the design element
    * @return
    */
    public void updateReuseDE(ItemRevision dATestRev, ModelObject deObj)
    {
        ReuseDesignElementInfo updateReuseDE = new ReuseDesignElementInfo();
        updateReuseDE.clientId = "updateReuseDE";
        updateReuseDE.element = (Cpd0DesignElement)deObj;
        updateReuseDE.sourceObject =  dATestRev;
        updateReuseDE.updateSubordinates = true;

        CreateOrUpdateDesignElementsInfo updatedInfo = new CreateOrUpdateDesignElementsInfo();
        updatedInfo.reuseDesignElements = new ReuseDesignElementInfo[] {updateReuseDE };
        m_dataMgtService.createOrUpdateDesignElements( updatedInfo );
    }

    /* Performs cleanup by deleting the list of objects
     * @param objsToBeDeleted the list of objects to be deleted
     */
    public void cleanupData(ModelObject[] objsToBeDeleted)
    {
        ServiceData sData = m_coreDMService.deleteObjects(objsToBeDeleted);
        if ( ! handleServiceData( sData, "cleanupData" ) )
        {
            LogFile.write( "cleanupData failed" );
            return ;
        }
        return;
    }

    /**
     * Sets the release status
     * @param objects on which release status is to be applied
     * @param releaseStatusName the name of release status
     * @throws Exception
     */
    public void applyReleaseStatus(WorkspaceObject[] objects, String releaseStatusName) throws Exception
    {

        // build option to "Append"
        ReleaseStatusOption optionAppend = new ReleaseStatusOption();
        optionAppend.operation = "Append"; // $NON-NLS-1$
        optionAppend.newReleaseStatusTypeName = releaseStatusName; // $NON-NLS-1$
        optionAppend.existingreleaseStatusTypeName = ""; // $NON-NLS-1$

        // build input
        ReleaseStatusInput resInput = new ReleaseStatusInput();
        resInput.objects = objects;
        resInput.operations = new ReleaseStatusOption[]{optionAppend};

        m_workflowService.setReleaseStatus( new ReleaseStatusInput[] { resInput } );
    }

    /**
     * Revises the model element
     * @param de Model Element
     * @return new revision of model element
     */
    public ModelObject reviseDE( ModelObject de)
    {
        try
        {
            ModelObject[] mObj = new ModelObject[1];
            mObj[0] = de;

            DeepCopyData[] dcdata;
            dcdata = getDeepcopyData ( mObj );

            SaveAsIn [] saveAsIn = new SaveAsIn[1];
            saveAsIn [0] = new SaveAsIn();
            saveAsIn [0].saveAsInput.boName = "Cpd0DesignElement";
            saveAsIn[0].targetObject = de;
            saveAsIn[0].deepCopyDatas= dcdata;

            SaveAsObjectsResponse reviseResp = m_mdlDataMgtService.reviseObjects( saveAsIn );
            return  reviseResp.output[0].objects[0];
        }
        catch( Exception ex )
        {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * SaveAs on the model element
     * @param designElement Model Element
     * @return new copy of model element
     */
    public ModelObject saveAsDE( ModelObject designElement )
    {
        try
        {
            com.teamcenter.services.internal.strong.core._2016_10.DataManagement.SaveAsIn[] saveAsIn1 = new com.teamcenter.services.internal.strong.core._2016_10.DataManagement.SaveAsIn[1];
            saveAsIn1[0] = new com.teamcenter.services.internal.strong.core._2016_10.DataManagement.SaveAsIn();
            saveAsIn1[0].deepCopyDatas = getDeepCopyData_2014_10( designElement );
            saveAsIn1[0].targetObject = designElement;

            RelateInfoIn[] relateInfo = new RelateInfoIn[] { new RelateInfoIn() };
            relateInfo[0].relate = false;

            com.teamcenter.services.strong.core._2011_06.DataManagement.SaveAsObjectsResponse resp1 = m_IntDMService.saveAsObjectsInBulkAndRelate( saveAsIn1, relateInfo );

            return resp1.output[0].objects[0];
        }
        catch( Exception ex )
        {
            ex.printStackTrace();
        }
        return null;
    }

    /*
     * Get Deep Copy Data
     */
    private DeepCopyData[] getDeepcopyData ( ModelObject[] mObj )
    {
        DeepCopyData [] deepcopydata = null ;

        try
        {
            // Call the getReviseDesc operation
            SaveAsDescResponse resp = m_mdlDataMgtService.getReviseDesc( mObj );

            // Print the service response

            Map<ModelObject,DeepCopyData[]> deepcopydatamap = resp.deepCopyInfoMap;



            for (Iterator<Map.Entry<ModelObject,DeepCopyData[]>> dcd = deepcopydatamap.entrySet().iterator(); dcd.hasNext();)
            {
                Map.Entry<ModelObject,DeepCopyData[]> dcdentry = dcd.next();

                deepcopydata = (DeepCopyData[]) dcdentry.getValue(); // get the Deepcopydata array

            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();

        }

        return deepcopydata;
    }

    private com.teamcenter.services.strong.core._2014_10.DataManagement.DeepCopyData[] getDeepCopyData_2014_10(
            ModelObject sourceObject )
    {
        DeepCopyDataInput dcdInput = new DeepCopyDataInput();
        dcdInput.businessObject = sourceObject;
        dcdInput.operation = "SaveAs";
        GetDeepCopyDataResponse dcdResp = m_coreDMService.getDeepCopyData( new DeepCopyDataInput[]{ dcdInput } );

        com.teamcenter.services.strong.core._2014_10.DataManagement.DeepCopyData[] deepCopyData =
                (com.teamcenter.services.strong.core._2014_10.DataManagement.DeepCopyData[])dcdResp.deepCopyInfoMap.get( sourceObject );
        return deepCopyData;
    }

    /**
    * Creates new baseline object
    * @param cd: input collaborative design
    * @return
    */
    public Mdl0Baseline createBaseline( Cpd0CollaborativeDesign cd ) throws ServiceException
    {
        CreateIn createBaselineInput = new CreateIn();
        createBaselineInput.clientId = clientId;
        createBaselineInput.data.boName = "Mdl0Baseline";
        createBaselineInput.data.tagProps.put( "mdl0ModelObject", cd );
        CreateResponse createBaselineResponse = m_coreDMService.createObjects( new CreateIn[]{ createBaselineInput } );

        if( createBaselineResponse.output.length > 0 )
        {
            System.out.println( " Baseline Created :" +  createBaselineResponse.output[0]);
        }
        return (Mdl0Baseline)createBaselineResponse.output[0].objects[0];
    }
    /**
    * Assigns collaborative designs to a project
    * @param collaborativeDesigns: array of collaborative designs
    * @param project: project to which CD to be assigned
    * @return
    */
    public void assignCDsToProject(ModelObject[] collaborativeDesigns, POM_object project)
    {
        AssignedOrRemovedObjects[] objs = new AssignedOrRemovedObjects[collaborativeDesigns.length];

        for(int inx = 0 ; inx < collaborativeDesigns.length; ++inx)
        {
            objs[inx] = new AssignedOrRemovedObjects();
            ModelObject[] collaborativeDesign = new ModelObject[1];
            collaborativeDesign[0] = collaborativeDesigns[inx];
            objs[inx].objectToAssign = collaborativeDesign;

            TC_Project[] projects = new TC_Project[1];
            projects[0] = ( TC_Project )project;
            objs[inx].projects = projects;
        }
        m_prjLevSecService.assignOrRemoveObjects( objs );
    }
    /**
    * Promotes the target objects
    * @param targets: array of input objects
    * @return
    */
    public void promote( ModelObject[] targets )
    {
        if ( targets.length == 0 )
        {
            return;
        }
        ContextData contextData = new ContextData();
        contextData.processTemplate = "TC Promote to History Process";
        contextData.attachments = new String[targets.length];
        contextData.attachmentTypes = new int[targets.length];
        contextData.attachmentCount = targets.length;
        for ( int i = 0; i < targets.length; ++i )
        {
            contextData.attachments[i] = targets[i].getUid();
            contextData.attachmentTypes[i] = 1;  // == EPM_target_attachment
        }
        m_workflowService.createInstance( false, "", "MDOTestUtils.promote", "", "", contextData );
    }

    /**
    * Creates subset definition from baseline definition
    * @param cd: collaborative design object
    * @param name: name of subset definition
    * @param blDefn: baseline definition object
    * @return : subset definition object
    */
    public Mdl0SubsetDefinition createSubsetDefnFromBaselineDefn(
            Cpd0CollaborativeDesign cd, String name, Mdl0BaselineDefinition blDefn )
                    throws ServiceException
    {
        // Create a subset definition from the baseline (i.e. by making the outer recipe of the
        // subset definition point to the baseline definition).
        CreateIn createSubsetDefnInput = new CreateIn();
        createSubsetDefnInput.clientId = clientId;
        createSubsetDefnInput.data.boName = "Mdl0SubsetDefinition";
        createSubsetDefnInput.data.tagProps.put( "mdl0model_object", cd );
        createSubsetDefnInput.data.stringProps.put( "object_name", name );
        createSubsetDefnInput.data.tagProps.put( "fnd0OuterRecipe", blDefn );
        CreateResponse createSubsetDefnResponse = m_dmService.createObjects( new CreateIn[]{ createSubsetDefnInput } );
        if ( ! handleServiceData( createSubsetDefnResponse.serviceData, "createSubsetDefnFromBaselineDefn" ) )
        {
            LogFile.write( "createSubsetDefnFromBaselineDefn failed" );
            return null;
        }
        return (Mdl0SubsetDefinition)createSubsetDefnResponse.output[0].objects[0];
    }

    /**
    * Creates new Workset object
    * @param currentTime: current time
    * @return : Workset object
    */
    public Cpd0WorksetRevision createWorkset(String currentTime) throws Exception
    {
        //Create Workset
        ItemProperties wsCreateIn = new ItemProperties();
        wsCreateIn.clientId = clientId;
        wsCreateIn.name = "Cpd0Workset: " + currentTime;
        wsCreateIn.type="Cpd0Workset";
        CreateItemsResponse wsResponse = m_dmService.createItems( new ItemProperties[] { wsCreateIn }, null, "" );
        if ( ! handleServiceData( wsResponse.serviceData, "createWorkset" ) )
        {
            LogFile.write( "createWorkset failed" );
            return null;
        }
        Property[] props2 = getObjectProperties(wsResponse.output[0].item, new String[]{"item_id"});
        String itemIdStr = props2[0].getDisplayableValue();
        System.out.println("+++Cpd0Workset with ItemId " + itemIdStr + " created.");

        return (Cpd0WorksetRevision) wsResponse.output[0].itemRev;
    }

    /**
    * Creates search definition required in creating recipe object
    * @param seInput: search expression input
    * @param clientId: recipe container object
    * @param designElementList: array of design element
    * @param searchService: search service
    * @return searchDef: search definition
    */
    public static Mdl0SearchDef testCreateIncludeElements(
            SearchExpressionInput seInput, String clientId,
            ArrayList<Cpd0DesignElement> designElementList,
            SearchService searchService)
    {
        Mdl0SearchDef searchDef = null;
        ModelElementInput mei = new ModelElementInput();
        mei.modelElements = new Mdl0ModelElement[designElementList.size()];
        for (int inx = 0; inx < designElementList.size(); inx++)
        {
            mei.modelElements[inx] = designElementList.get(inx);
        }
        mei.clientid = clientId;

        seInput.includeElements = new ModelElementInput[1];
        seInput.includeElements[0] = mei;

        SearchExpressionResponse serverResponse = searchService
                .createSearchExpressions(seInput);

        ServiceData serviceData = serverResponse.serviceData;
        processServiceData(serviceData);
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

    /**
    * Creates build configuration details
    * @param configurationFor: name of configuration detail
    * @param configContext: configContext object
    * @return configDetails: array of ConfigurationDetail object
    */
    public static ConfigurationDetail[] buildConfigurationDetails( String configurationFor, ConfigurationContext configContext )
    {
        ConfigurationDetail detail = buildConfigurationDetail( configurationFor, configContext );
        ConfigurationDetail[] configDetails = new ConfigurationDetail[]{detail};
        return configDetails;
    }

    /**
    * Creates configuration context with  Any Status; Working revision rule
    * @param
    * @return cfgContext: configuration context
    */
    public static ConfigurationContext createConfigCtxtWithAnyStatusWorkingRule() throws Exception
    {
        String clientId = "001011919729873324-createConfigCtxtWithAnyStatusWorkingRule";

        RevisionRule contextRevRule = getRevisionRuleByName("Any Status; Working");

        ConfigurationData configData = new ConfigurationData();
        ConfigurationContext cfgCtxt = new ConfigurationContext(null, null);
        /** The configuration context */
        configData.configContext = cfgCtxt;
        configData.revisionRule = contextRevRule;
        configData.clientid = clientId;

        ConfigResponse response = m_searchService.createOrUpdateConfigurations(new ConfigurationData[] { configData });
        processServiceData(response.serviceData);
        ConfigurationContext cfgContext = response.configContext[0].configContext;

        return cfgContext;
    }

    /**
    * Process service data
    * @param serviceData: servic eData
    * @return
    */
    public static void processServiceData(ServiceData serviceData)
    {
        // Loop through one or more of the arrays contained within ServiceData.
        // Service documentation should make it clear which arrays may have data
        for (int i = 0; i < serviceData.sizeOfPartialErrors(); i++)
        {
            ErrorStack errorStack = serviceData.getPartialError(i);
            String[] messages = errorStack.getMessages();
            for (int j = 0; j < messages.length; j++)
            {
                System.out.println(messages[j]);
            }
        }
    }

    /**
    * Creates build configuration detail
    * @param configurationFor: name of configuration detail
    * @param configContext: configContext object
    * @return detail: ConfigurationDetail object
    */
    public static ConfigurationDetail buildConfigurationDetail( String configurationFor, ConfigurationContext configContext )
    {
        ConfigurationDetail detail = new ConfigurationDetail();
        detail.configurationFor = configurationFor;
        detail.configContext = configContext;

        return detail;
    }

    /**
    * Creates new recipe object
    * @param cd: collaborative design object
    * @param recipeContainer: recipe container object
    * @param designElementList: array of design element
    * @return subsetElement: subset object
    */
    public void createRecipe(
            Cpd0CollaborativeDesign cd, ModelObject recipeContainer, ArrayList<Cpd0DesignElement> designElementList )
                    throws Exception
    {
        // Creates a recipe specifying a discrete list of DEs.
        Mdl0SearchDef includeElementsDef = testCreateIncludeElements(
                new SearchExpressionInput(), clientId, designElementList, m_searchService );

        SearchExpression searchExpr = new SearchExpression();
        searchExpr.searchExpressions = new Mdl0SearchDef[]{ includeElementsDef };

        SearchExpressionSet searchExprSet = new SearchExpressionSet();
        searchExprSet.searchExpressionSets = new SearchExpressionSet[0];
        searchExprSet.searchExpression = searchExpr;
        searchExprSet.searchOperator = new String( "Or" );

        SearchScope scope = new SearchScope();
        scope.model = cd;

        SearchRecipe recipe = new SearchRecipe();
        recipe.searchExpression = searchExprSet;
        recipe.scope = scope;
        recipe.configDetails = buildConfigurationDetails("Universal", createConfigCtxtWithAnyStatusWorkingRule() );

        RecipeData recipeData = new RecipeData();
        recipeData.recipe = recipe;
        recipeData.recipeContainer = recipeContainer;

        ServiceData setRecipeResponse = m_searchService.setRecipes( new RecipeData[]{ recipeData } );
        if ( ( setRecipeResponse.sizeOfPartialErrors() != 0 ) )
        {
            LogFile.write( "createRecipe failed" );
        }
    }

    /**
    * Creates new Subset object
    * @param CDSource: source collaborative design object
    * @param SubsetDef: subset definition object
    * @param SrcDEs: source elements to be appended to subset element
    * @return subsetElement: subset object
    */
    public Cpd0DesignSubsetElement createSubset( ModelObject CDSource, Mdl0SubsetDefinition SubsetDef, ModelObject[] SrcDEs)
    {
        Cpd0DesignSubsetElement subsetElement = null;
        try
        {
            String currentTime = (new java.util.Date()).toString();
            currentTime = currentTime.replace( " ", "_" );
            Cpd0WorksetRevision wsrRev = createWorkset( currentTime );
            DesignSubsetElementInfo subsetInfo = new DesignSubsetElementInfo();
            subsetInfo.sourceModel = ( Cpd0CollaborativeDesign )CDSource;// can be a subset or CD
            subsetInfo.modelObject = wsrRev;
            subsetInfo.boType = "Cpd0DesignSubsetElement";
            subsetInfo.clientId = "subsetCreation";
            subsetInfo.subset = SubsetDef;
            CreateOrUpdateDesignSubsetElementsResponse createSubsetResp =
                    m_dataMgtService.createOrUpdateDesignSubsetElements( new DesignSubsetElementInfo[]{ subsetInfo } );
            if( createSubsetResp.dseData != null &&  createSubsetResp.dseData.length == 1 &&  createSubsetResp.dseData[0].subsetElement != null)
            {
                subsetElement = createSubsetResp.dseData[0].subsetElement;
            }
            // Add the DEs to the subset created
            subsetInfo = new DesignSubsetElementInfo();
            subsetInfo.updateRecipeForAppendsAndRemoves = true;
            subsetInfo.element = subsetElement;
            Mdl0PositionedModelElement[] sourceElements = new Mdl0PositionedModelElement[ SrcDEs.length ];
            for ( int inx=0; inx < SrcDEs.length; ++inx)
            {
                sourceElements[inx] = (Cpd0DesignElement)SrcDEs[inx];
            }
            subsetInfo.appendSourceElements = sourceElements;
            
            createSubsetResp = m_dataMgtService.createOrUpdateDesignSubsetElements( new DesignSubsetElementInfo[]{ subsetInfo } );
            if ( ! handleServiceData( createSubsetResp.serviceData, "createSubset" ) )
            {
                LogFile.write( "createSubset failed" );
                return null;
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return subsetElement;
    }

    /**
     * Creates new Subset object
     * @param CDSource: source collaborative design object
     * @param CDTarget: source collaborative design object
     * @return rlzResponse: subset object
     */
     public ModelToModelRealizationContentResponse createRlzContainer( ModelObject CDSource, ModelObject CDTarget, Mdl0SubsetDefinition SubsetDefn )
     {
         ModelToModelRealizationContentResponse rlzResponse = null;
         try
         {
             //populate the SourceModelInfo structure.
             SourceModelInfo sourceInfo = new SourceModelInfo();
             sourceInfo.sourceModel = (Mdl0ApplicationModel) CDSource;
             sourceInfo.configContext =  createConfigCtxtWithAnyStatusWorkingRule();
             sourceInfo.subsetDef = SubsetDefn;

             //populate the RealizationContentInfo structure.
             RealizationContentInfo[] info = new RealizationContentInfo[1];
             info[0] = new RealizationContentInfo();
             info[0].boType = "Rlz0RealizationContainer";
             info[0].realizationContainerName = "RlzContainerObjectName_Aspect";
             info[0].sourceModelInfo = sourceInfo;
             info[0].targetModel = (Mdl0ApplicationModel) CDTarget;

             //populate the RealizeModelContentInfo structure.
             RealizeModelContentInfo createinfo = new RealizeModelContentInfo();

             createinfo.createInfo = info;

             //Call to create realization container
             rlzResponse = m_rlzDMService.createOrUpdateModelToModelRealization( createinfo );
              if ( ! handleServiceData( rlzResponse.serviceData, "createRlzContainer" ) )
              {
                  LogFile.write( "createRlzContainer failed" );
                  return null;
              }
         }
          catch(Exception ex)
          {
              ex.printStackTrace();
          }

         return rlzResponse;
     }


    public CreateOrUpdateRelativeStructureResponse createStructure( ItemRevision parent, List<ItemRevision> listOfChildrenRevIds, String bomViewTypeName )
     {
         CreateOrUpdateRelativeStructureResponse response = null;
         int num_occs = listOfChildrenRevIds.size();
         try
         {
             CreateOrUpdateRelativeStructureInfo3[] inputs = new CreateOrUpdateRelativeStructureInfo3[1];
             inputs[0] = new CreateOrUpdateRelativeStructureInfo3();
             inputs[0].parent = parent;
             inputs[0].precise = true;
             inputs[0].childInfo = new RelativeStructureChildInfo2 [num_occs];
             for( int idx = 0; idx < num_occs ; idx++ )
             {
                 inputs[0].childInfo[idx] = new RelativeStructureChildInfo2();
                 inputs[0].childInfo[idx].child = (ItemRevision) listOfChildrenRevIds.get( idx );
             }
             CreateOrUpdateRelativeStructurePref3 pref = new CreateOrUpdateRelativeStructurePref3();
             //If bomViewTypeName is "", then default will be taken
             response = m_smService.createOrUpdateRelativeStructure( inputs, bomViewTypeName, false, pref );
         }
         catch (Exception ex)
         {
             ex.printStackTrace();
         }
         return response;
     }

    public CloneModelContentResponse cloneModelContent( Mdl0ModelElement des[], ModelObject cdSource, ModelObject cdTarget )
    {
        CloneModelContentResponse responseObject = null;
        try
        {
            com.mdl0.services.strong.modelcore._2014_10.DataManagement.SourceModelInfo sInfo = null;
            sInfo = new com.mdl0.services.strong.modelcore._2014_10.DataManagement.SourceModelInfo();
            sInfo.includeSourceElements = des;
            sInfo.configContext = createConfigCtxtWithAnyStatusWorkingRule();

            SourceTargetContentInfo[] sourceInfos = null;
            sourceInfos = new SourceTargetContentInfo[1];
            sourceInfos[0] = new SourceTargetContentInfo();
            sourceInfos[0].sourceModelInfo = sInfo;
            sourceInfos[0].sourceModelInfo.sourceModelObject = (Mdl0ApplicationModel) cdSource;
            sourceInfos[0].targetModel = (Mdl0ApplicationModel) cdTarget;

            CloneModelContentInfo cloneInfo = null;
            cloneInfo = new CloneModelContentInfo();
            cloneInfo.createInfo = sourceInfos;

            responseObject = m_mdlDataMgtService.cloneModelContent(cloneInfo);
            if ( ! handleServiceData( responseObject.serviceData, "cloneModelContent" ) )
            {
                LogFile.write( "cloneModelContent failed" );
                return null;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return responseObject;
    }

    public ModelToModelInstantiationResponse createModeltoModelInstantiation( Mdl0ModelElement des[], ModelObject cdsource, ModelObject cdTarget )
    {
        ModelToModelInstantiationResponse responseObject = null;
        try
        {
            //populate the RealizationContentInfo structure.
            Mdl0ModelElement[] newlyAddedDEs = new Mdl0ModelElement[5];
            newlyAddedDEs = des;

            com.cpd0.services.strong.cpdcore._2014_10.DataManagement.SourceModelInfo sourceInfo =
                    new  com.cpd0.services.strong.cpdcore._2014_10.DataManagement.SourceModelInfo();
            sourceInfo.sourceModel = (Mdl0ApplicationModel) cdsource;
            sourceInfo.configContext = createConfigCtxtWithAnyStatusWorkingRule();
            //For reply recipe subset definition will not present
            sourceInfo.subsetDefinition = null;
            sourceInfo.includeSourceElements = newlyAddedDEs;


            String desc = Calendar.getInstance().getTime().toString().replaceAll(" ", "").replaceAll(":","");
            String objName = "ModelReuseDE_Up"+ desc;
            InstantiationObjectInfo[] info = new InstantiationObjectInfo[1];
            info[0]=new InstantiationObjectInfo();
            info[0].boType = "Cpd0DesignElement";
            info[0].attrInfo.put("object_name", new String[] { objName });
            info[0].attrInfo.put("object_desc", new String[] { desc });
            info[0].sourceModelData = sourceInfo;
            info[0].targetModel= (Mdl0ApplicationModel) cdTarget;


            ModelToModelInstantiationInfo creInfo = new ModelToModelInstantiationInfo();
            creInfo.clientId = "createM2MInstantiation";
            creInfo.objInfo = info;

            responseObject = m_dataMgtService.createOrUpdateModelToModelInstantiation( creInfo );
            if ( ! handleServiceData( responseObject.serviceData, "createModeltoModelInstantiation" ) )
            {
                LogFile.write( "createModeltoModelInstantiation failed" );
                return null;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return responseObject;
    }

    public static ModelObject createPartitionScheme( ModelObject mdlObj, String schemeType, String objName, String objDesc )
    {
        CreateIn[] inputs = new CreateIn[1];
        inputs[0] = new CreateIn();
        inputs[0].data = new com.teamcenter.services.strong.core._2008_06.DataManagement.CreateInput();
        inputs[0].data.boName = schemeType;
        inputs[0].data.tagProps.put("mdl0model_object", mdlObj);
        inputs[0].data.stringProps.put("object_name", objName);
        inputs[0].data.stringProps.put("object_desc", objDesc);
        CreateResponse response = null;
        try
        {
            response = m_dmService.createObjects( inputs );
        }
        catch (ServiceException e)
        {
            e.printStackTrace();
        }
        if( response!=null && response.output!=null && response.output.length != 0 )
        {
            return response.output[0].objects[0];
        }
        else
        {
            return null;
        }
    }
    public static ModelObject createAdhocPartition( ModelObject mdlObj, ModelObject schemeObj, String partitionType, String objName, String objDesc, String ptn_id )
    {
        CreateIn[] inputs = new CreateIn[1];
        inputs[0] = new CreateIn();
        inputs[0].data = new com.teamcenter.services.strong.core._2008_06.DataManagement.CreateInput();
        inputs[0].data.boName=partitionType;
        inputs[0].data.tagProps.put("mdl0model_object", mdlObj);

        try
        {
            if ( schemeObj.getPropertyObject("object_type") != null )
            {
                ImanType Im1= new ImanType(schemeObj.getTypeObject(),schemeObj.getTypeObject().getUid());
                inputs[0].data.tagProps.put("ptn0partition_scheme_type", Im1);
            }
        }
        catch (NotLoadedException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        inputs[0].data.stringProps.put("object_name", objName);
        inputs[0].data.stringProps.put("object_desc", objDesc);
        inputs[0].data.stringProps.put("ptn0partition_id", ptn_id );
        inputs[0].data.tagProps.put("ptn0partition_recipe",null);
        inputs[0].data.tagProps.put("ptn0source_object",null);
        CreateResponse response = null;
        try
        {
            response = m_dmService.createObjects( inputs );
        }
        catch (ServiceException e)
        {
            e.printStackTrace();
        }

        if( response!=null && response.output!=null && response.output.length != 0 && response.output[0].objects!=null && response.output[0].objects.length != 0 )
        {
            return response.output[0].objects[0];
        }
        else
        {
            return null;
        }
    }
    public PublishObjectsResponse publishLibraryObjects( Lbr0HierarchyNode libraryNode, BusinessObject[] objectsToPublish, String elementID, int numObjects )
    {
        PublishObjectsResponse pubObjResponse = null;

        if( numObjects > 0 )
        {
            ObjectToPublishInfo[] objPublishInfo = new ObjectToPublishInfo[numObjects];

            for( int iElements = 0; iElements< numObjects; ++ iElements )
            {
                String baseName = elementID + "_" +  iElements + generator.nextInt();

                objPublishInfo[iElements] = new ObjectToPublishInfo();
                objPublishInfo[iElements].object      = objectsToPublish[iElements];

                // Library element attributes.
                String[] valuesLbr = new String[1];
                valuesLbr[0] = baseName;
                objPublishInfo[iElements].elementProperties .put( "lbr0ElementId", valuesLbr );

                String[] valuesLbr1 = new String[1];
                valuesLbr1[0] = baseName;
                objPublishInfo[iElements].elementProperties.put( "object_name", valuesLbr1 );

                String[] valuesLbr2 = new String[1];
                valuesLbr2[0] = baseName + " Description Library Element";
                objPublishInfo[iElements].elementProperties.put( "object_desc", valuesLbr2 );

                // classification attributes
                String[] values = new String[1];
                values[0] = baseName;
                objPublishInfo[iElements].properties.put( "cls0object_id", values );

                String[] values1 = new String[1];
                values1[0] =  baseName + " Description Test";
                objPublishInfo[iElements].properties.put( "object_name", values1 );

                // Int attributes
                String[] values2 = new String[1];
                values2[0] =  Integer.toString( iElements + 10 );
                objPublishInfo[iElements].properties.put( "sml088104", values2 );

                // Double attribute
                String[] values3 = new String[1];
                values3[0] =  Double.toString( iElements + 100.1 );
                objPublishInfo[iElements].properties.put( "sml088106", values3 );

                // string attribute
                String[] values4 = new String[1];
                values4[0] =  "Thread Type " + iElements;
                objPublishInfo[iElements].properties.put( "sml088105", values4 );
            }

            PublishObjectsIn[] publishObj1 = new PublishObjectsIn[1];
            publishObj1[0] = new PublishObjectsIn();
            publishObj1[0].libraryNode = libraryNode;
            publishObj1[0].objectsToPublish = objPublishInfo;
            publishObj1[0].doBulkPublish = false;

            pubObjResponse = new PublishObjectsResponse();
            pubObjResponse = m_libraryUsageService.publishObjectsToLibrary( publishObj1 );
        }

        return pubObjResponse;
    }

    public Lbr0Library[] createLbr0Librararies( String lbrName, String[] disciplines, String[] allowedMemberTypes, int numObject )
    {
        Lbr0Library[] libraries = null;

        if ( numObject > 0 )
        {
            CreateLibrariesIn[] librariesIn = new CreateLibrariesIn[numObject];

            for( int iLibrary = 0;  iLibrary < numObject; ++iLibrary )
            {

                String baseName = lbrName + "_" + iLibrary;
                // create Library1
                librariesIn[iLibrary] = new CreateLibrariesIn();

                librariesIn[iLibrary].clientID = baseName;
                librariesIn[iLibrary].boName   = "";
                librariesIn[iLibrary].id       = baseName + generator.nextInt();
                librariesIn[iLibrary].name     = baseName;
                librariesIn[iLibrary].description = "Creatting library ... " + baseName;

                librariesIn[iLibrary].libraryType = "Project";

                librariesIn[iLibrary].disciplines = disciplines;

                // TO DO Administrators
                librariesIn[iLibrary].allowedMemberTypes = allowedMemberTypes;

            }

            CreateLibrariesResponse libraryResponse = new CreateLibrariesResponse();
            libraryResponse = m_libraryManagementService.createLibraries( librariesIn );

            int numCreatedLibraries = libraryResponse.output.length;

            if( numCreatedLibraries > 0 )
            {
                libraries = new Lbr0Library[numCreatedLibraries];

                for( int iCreatedLibrary = 0; iCreatedLibrary < numCreatedLibraries; ++iCreatedLibrary )
                {
                    libraries[iCreatedLibrary] = libraryResponse.output[iCreatedLibrary].library;
                }
            }
        }

        return libraries;
    }

    public CreateHierarchiesResponse createHierarchy( Lbr0Library library, String libraryName )
    {
        CreateHierarchiesResponse hierarchyResponse = new CreateHierarchiesResponse();

        if( library!= null )
        {
            CreateHierarchiesIn[] hierachy = new CreateHierarchiesIn[1];

            hierachy[0] = new CreateHierarchiesIn();

            String baseName = libraryName;

            hierachy[0].clientID = baseName;
            hierachy[0].boName   = "";
            hierachy[0].id       = baseName  + generator.nextInt();
            hierachy[0].name     = baseName;
            hierachy[0].description = "Hierarchy created" + hierachy[0].id;

            hierachy[0].library = library;

            String[] allowedMemberTypes = new String[1];
            allowedMemberTypes[0]="Item";
            hierachy[0].allowedMemberTypes = allowedMemberTypes;

            // TO DO additional Properties

            System.out.println("Calling LibraryManagement::createHierarchies()");
            hierarchyResponse = m_libraryManagementService.createHierarchies( hierachy );
        }

        return hierarchyResponse;
     }

    public CreateNodesResponse createLbrHierarchyGenralNode ( Lbr0Hierarchy hierarchy, Lbr0HierarchyNode parentNode, String nodeName )
    {
        CreateNodesResponse nodeResponse = new CreateNodesResponse();

        if( hierarchy != null )
        {
              CreateNodesIn[] node = new CreateNodesIn[1];
              node[0] = new CreateNodesIn();

              String baseName = nodeName;

              node[0].clientID = "NodeCID_" + baseName;
              node[0].boName   = "Lbr0HierarchyNode";
              node[0].id       =  baseName + generator.nextInt();
              node[0].name     =  baseName;
              node[0].description = " Created Node " + baseName;

              String[] allowedMemberTypes = new String[2];
              allowedMemberTypes[0]="ItemRevision";
              allowedMemberTypes[1]="Item";
              node[0].allowedMemberTypes = allowedMemberTypes;

              node[0].hierarchy  = hierarchy;
              node[0].parentNode = parentNode;

              node[0].replicateClsNodes = false;

              // TO DO Additional properties.

              System.out.println("Calling LibraryManagement::createNodes()");
              nodeResponse = m_libraryManagementService.createNodes( node );

        }
        return nodeResponse;
    }

    public ModelObject realizeLibraryElement( ModelObject libraryElement, ModelObject CDObj, String designElementName ) throws Exception
    {
        String[] propertyArray = new String []{"lbr0LibraryObject"};
        Property[] props = getObjectProperties(libraryElement, propertyArray);
        ModelObject obj = props[0].getModelObjectValue();

//        if( obj instanceof ItemRevision )
//        {
//            System.out.println("\n It is item revision");
//        }

        ReuseDesignElementInfo reuseDE = new ReuseDesignElementInfo();
        reuseDE.boType = "Cpd0DesignElement";
        reuseDE.clientId = designElementName + "_CID";
        reuseDE.attrInfo.put( "object_name", new String[] {designElementName });
        // Next line creates the link between the Design Element, Realization and Library Element
        reuseDE.attrInfo.put( "lbr0SourceLibraryElement", new String[] {libraryElement.getUid() } );

        reuseDE.modelObject = (WorkspaceObject) CDObj;
        reuseDE.sourceObject = obj;
        // Get a revision rule which configures assembly as precise one
        reuseDE.revRule = getRevisionRuleByName("Latest Working");
        // Item realization need released source object
        applyReleaseStatus(new WorkspaceObject[]{(WorkspaceObject) reuseDE.sourceObject}, "TCM Released");

        double[] transforms = new double [16];
        for ( int i = 0; i < 16; i++ )
        {
            if ( i % 5 == 0 )
            {
                transforms[ i ] = 1.0;
            }
        }
        reuseDE.transform = transforms;

        CreateOrUpdateDesignElementsInfo info = new CreateOrUpdateDesignElementsInfo();
        info.reuseDesignElements = new ReuseDesignElementInfo[] { reuseDE };

        CreateOrUpdateDesignElementsResponse resp = m_dataMgtService.createOrUpdateDesignElements( info );



        return resp.serviceData.getCreatedObject(0);

    }

    /**
     * Set Multi-Discciplinary property of project
     */
     public void setProjectAsMultiDisciplinary( ModelObject project)
     {
         ModelObject[] projectObjects = new ModelObject[1];
         projectObjects[0] = project;
         Map<String, String> propertyMap = new HashMap<String, String>();

         propertyMap.put("fnd0CollaborationCategories", "Multi-Disciplinary");

         setPropertiesOfObject( projectObjects, propertyMap );
     }


     /**
      * Wait for the subscriptionmgrd to create any required notifications.
      * Assuming the delay is set to 1 minute (60000 milliseconds).
      * @throws Exception
      */
     public void sleepForNotificationCreation( ) throws Exception
     {
         LogFile.write( "Wait for notification creation" );
         Thread.currentThread();
         Thread.sleep(65000);
     }

     /**
      * Executes the performance testing for given count of DEs
      * @param DECount Number of DEs used for performance testing
      * @param MechanicalMotor The Mechanical design artifact to be used in source collaborative design
      * @param AutomationMotor The Automation design artifact to be used in source collaborative design
      * @param cdSrc source collaborative design
      * @param cdTarget target collaborative design
      */
     public  void executePerformanceTestingForInputDECount( int DECount, ItemRevision MechanicalMotor,   ItemRevision AutomationMotor, ModelObject cdSrc, ModelObject cdTarget)
     {

        // Create DECount DEs using MechanicalMotor in CDMechPerf
        // Fire qryNotificationsByOriginDesign note time
        // Create DECount DEs using AutomationMotor in CDAutoPerf
        // Call updateMDONotification to update the DECount DEs of CDAutoPerf to be impacted response for DECount Notification in CDMechPerf
        // Fire qryNotificationsByOriginDesign note time - It returns the notification with response.

         ModelObject cdMechPerf= cdSrc;
         ModelObject cdAutoPerf = cdTarget;
         // Create DEs using MechanicalMotor in CDMechPerf

         LogFile.write( "Create DEs using MechanicalMotor in CDMechPerf" );

         CreateOrUpdateDesignElementsResponse DEsOfCDMechPerf= createReuseDEWithCDForPerFormance(DECount+20, MechanicalMotor,"DEForMechanicalMotor",cdMechPerf );

         if(DEsOfCDMechPerf != null)
         {
            LogFile.write( "Create DEs using MechanicalMotor in CDMechPerf complete" );
         }
         else
         {
              LogFile.write( "Create DEs using MechanicalMotor in CDMechPerf failed" );
         }


         try
         {
            sleepForNotificationCreation();
         }
         catch (Exception e)
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }

         Calendar dateBefore = new GregorianCalendar();
         LogFile.write( "----------------------------------" );
         // LogFile.write( " Time before firing the query for DEs with out notfication response. Time Start: Hour = "+dateBefore.HOUR_OF_DAY + "Minute ="+dateBefore.MINUTE+"Second ="+ dateBefore.SECOND + " and Time in milli secs = "+dateBefore.getTimeInMillis() );
         LogFile.write( "Time before firing the query for DEs with out notfication response. Time Start: Current Time in milli secs = "+dateBefore.getTimeInMillis() );

         NotificationQueryInput[] mdoNotifInputsForCDMechPerf = new NotificationQueryInput[1];
         mdoNotifInputsForCDMechPerf[0] = new NotificationQueryInput();
         mdoNotifInputsForCDMechPerf[0].clientId = "QueryNotificationForDEswithoutResp";
         mdoNotifInputsForCDMechPerf[0].performCreateNotificationQueryAlso = true;
         mdoNotifInputsForCDMechPerf[0].filterByDesign = (POM_object)cdMechPerf;
         QueryNotificationResponse notifRespforCDMechPerf1 = queryForMDONotifByOriginatingDesign(mdoNotifInputsForCDMechPerf);

         Calendar dateAfter = new GregorianCalendar();

         LogFile.write( "Time after firing the query for DEs with out notfication response. Time End: Current Time in milli secs = "+dateAfter.getTimeInMillis() );
         LogFile.write( "Time consumed for queryForMDONotifByOriginatingDesign in milli secs = "+ (dateAfter.getTimeInMillis()-dateBefore.getTimeInMillis()));
         LogFile.write( "----------------------------------" );
         // Create DEs using AutomationMotor in CDAutoPerf
         LogFile.write( "Create  DEs using AutomationMotor in CDAutoPerf" );

         CreateOrUpdateDesignElementsResponse DEsOfCDAutoPerf = createReuseDEWithCDForPerFormance(DECount+20, AutomationMotor,"DEForAutomationMotor",cdAutoPerf );
         if(DEsOfCDAutoPerf != null)
         {
            LogFile.write( "Create  DEs using MechanicalMotor in CDAutoPerf complete" );
         }
         else
         {
              LogFile.write( "Create  DEs using MechanicalMotor in CDAutoPerf failed" );
         }

         //To generate notification
         try
         {
            sleepForNotificationCreation();
         }
         catch (Exception e)
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }

        // Call updateMDONotification to update the  DEs of CDAutoPerf to be impacted response for Notification in CDMechPerf
         MDONotificationUpdateInput[] mdoNotifUpdInputForCDMechPerf = new MDONotificationUpdateInput[DECount];
         for(int inx= 0; inx < DECount; ++inx )
         {
            // Get Trigger Notification Object
             CreateNotificationOutput createNotificationOutput = notifRespforCDMechPerf1.output[0].createNotificationOutputs[inx];
             MDOInitialNotification createTriggerNotification = createNotificationOutput.createTriggerNotification;
             POM_object notifObj = createTriggerNotification.notificationObject;

             //Get the  DE to be updated as impacted response
             ModelObject deForCDAutoPerf = (ModelObject) DEsOfCDAutoPerf.clientIDMap.get( "DEForAutomationMotor"+inx );

             MDONotificationReactionData[] reactData1 = new MDONotificationReactionData[1];
             reactData1[0] = new MDONotificationReactionData();
             reactData1[0].notificationQualifier = "create";
             reactData1[0].reactedObject =  (POM_object) deForCDAutoPerf;

             MDONotificationUpdateData[] updateData1 = new MDONotificationUpdateData[1];
             updateData1[0] = new MDONotificationUpdateData();
             updateData1[0].notificationQualifier = "create";
             updateData1[0].ignoreNotification = false;
             updateData1[0].notificationObject = notifObj ; // notification object from query
             updateData1[0].reactedResponses = new  MDONotificationReactionData[1];
             updateData1[0].reactedResponses[0] = reactData1[0];

             mdoNotifUpdInputForCDMechPerf[inx] = new MDONotificationUpdateInput();
             mdoNotifUpdInputForCDMechPerf[inx].clientId = "updateMDONotificationsForDEForCDMechPerf"+inx;
             mdoNotifUpdInputForCDMechPerf[inx].notificationUpdateData = updateData1[0];
         }

         dateBefore = new GregorianCalendar();
         LogFile.write( "----------------------------------" );
         LogFile.write( "Time before firing the update for DEs with notfication response. Time Start: Current Time in milli secs = "+dateBefore.getTimeInMillis() );

         UpdateMDONotificationResponse respForUpdateMDONotificationsForDEForCDMechPerf = updateMDONotification(mdoNotifUpdInputForCDMechPerf);
         if ( ! handleServiceData( respForUpdateMDONotificationsForDEForCDMechPerf.serviceData, "UpdateMDONotificationsForDEForCDMechPerf" ) )
         {
            LogFile.write( "updateMDONotification to update the DEs of CDAutoPerf to be impacted response for Notification in CDMechPerf failed" );
         }
         else
         {
            LogFile.write( "updateMDONotification to update the DEs of CDAutoPerf to be impacted response for Notification in CDMechPerf complete" );
         }

         dateAfter = new GregorianCalendar();
         LogFile.write( "Time after firing the update for DEs with notfication response. Time End : Current Time in milli secs = "+dateAfter.getTimeInMillis() );
         LogFile.write( "Time consumed for updateMDONotification in milli secs = "+ (dateAfter.getTimeInMillis()-dateBefore.getTimeInMillis()));
         LogFile.write( "----------------------------------" );
         // Fire qryNotificationsByOriginDesign note time - It returns the notification with response.
         NotificationQueryInput[] mdoNotifInputsForCDMechPerf1 = new NotificationQueryInput[1];
         mdoNotifInputsForCDMechPerf1[0] = new NotificationQueryInput();
         mdoNotifInputsForCDMechPerf1[0].clientId = "QueryNotificationForDEswithtResp";
         mdoNotifInputsForCDMechPerf1[0].performCreateNotificationQueryAlso = true;
         mdoNotifInputsForCDMechPerf1[0].filterByDesign = (POM_object)cdMechPerf;

         dateBefore = new GregorianCalendar();
         LogFile.write( "----------------------------------" );
         LogFile.write( "Time before firing the query for DEs with notfication response. Time Start: Current Time in milli secs = "+dateBefore.getTimeInMillis() );
         notifRespforCDMechPerf1 = queryForMDONotifByOriginatingDesign(mdoNotifInputsForCDMechPerf1);
         dateAfter = new GregorianCalendar();
         LogFile.write( "Time after firing the query for DEs with notfication response.Time End : Current Time in milli secs = "+dateAfter.getTimeInMillis() );
         LogFile.write( "Time consumed for queryForMDONotifByOriginatingDesign query in milli secs = "+ (dateAfter.getTimeInMillis()-dateBefore.getTimeInMillis()));
         LogFile.write( "----------------------------------" );

     }

     /**
      * calls the SOA to get the Domain of object or type
      * @param getDomainInputArray Input array to get the domain information

      */
     public com.teamcenter.services.strong.core._2015_07.DataManagement.DomainOfObjectOrTypeResponse  getDomainOfObjectOrType( GetDomainInput[] getDomainInputArray)
     {

           com.teamcenter.services.strong.core._2015_07.DataManagement.DomainOfObjectOrTypeResponse  resp =  m_dmService.getDomainOfObjectOrType( getDomainInputArray );
           if ( ! handleServiceData( resp.serviceData, "getDomainOfObjectOrType" ) )
           {
               LogFile.write( "getDomainOfObjectOrType: Failed" );
           }
         return resp;

     }

     /**
      * Carries forward the MDI on the DE revision
      * @param origDesignInstance Original Design Instance
      * @param targetDesignInstance Target Design Instance
      * @return newly created MDO
      */
     public SearchForLinkedInstancesResponse carryForwardMDI( ModelObject origDesignInstance, ModelObject targetDesignInstance, boolean setValidationRequired )
     {
         MDInstanceLinkCarryFwdInput[] inputs = new MDInstanceLinkCarryFwdInput[1];
         inputs[0] = new MDInstanceLinkCarryFwdInput();
         inputs[0].clientId = "reviseDE";
         inputs[0].originalDesignInstance = (POM_object)origDesignInstance;
         inputs[0].targetDesignInstance = (POM_object)targetDesignInstance;
         inputs[0].setValidationRequired = setValidationRequired;

         SearchForLinkedInstancesResponse response = m_mdoService.carryForwardMDInstanceAssociation( inputs );
         return response;
     }

     /**
      * Carries forward the MDT on the DA revision
      * @param origDesignArtifact Original Design Artifact
      * @param targetDesignArtifact Target Design Artifact
      * @return newly created MDO
      */
     public SearchMDOResponse carryForwardMDT( ModelObject origDesignArtifact, ModelObject targetDesignArtifact, boolean setValidationRequired )
     {
         MDThreadLinkCarryFwdInput[] inputs = new MDThreadLinkCarryFwdInput[1];
         inputs[0] = new MDThreadLinkCarryFwdInput();
         inputs[0].clientId = "reviseDA";
         inputs[0].originalDesignArtifact = (WorkspaceObject)origDesignArtifact;
         inputs[0].targetDesignArtifact = (WorkspaceObject)targetDesignArtifact;
         inputs[0].setValidationRequired = setValidationRequired;

         SearchMDOResponse response = m_mdoService.carryForwardMDThreadAssociation( inputs );
         return response;
     }

     /**
      * Queries needs Validation Links.
      * @param qryInputs Input query
      * @return Response for the SOA
      */
     public NeedsValidationLinkResponse queryNeedsValidation( NeedsValidationLinkInput[] qryInputs )
     {
         NeedsValidationLinkResponse qryResp = m_mdoService.queryNeedsValidationLink(qryInputs);
         if ( ! handleServiceData( qryResp.serviceData, "queryNeedsValidation" ) )
         {
             LogFile.write( "queryNeedsValidation: Failed" );
         }

         return qryResp;
     }

     /**
      * prints Queries results for needs Validation Links.
      * @param qryResp Input to print the reults
      * @return void
      */
     public void printQueryNeedsValidationResults( NeedsValidationLinkResponse qryResp )
     {
         try
         {
             if(qryResp.linkQueryOutput.length > 0)
             {
                for(int i = 0; i < qryResp.linkQueryOutput.length ; ++i)
                {
                    MDOOutput[] mdoOutputs = qryResp.linkQueryOutput[i].mdoLinkOutput;
                    MDInstanceData[] mdiOutputs = qryResp.linkQueryOutput[i].mdiLinkOutput;
                    if(mdoOutputs != null && mdoOutputs.length > 0)
                    {
                        LogFile.write("MDO Associations with Needs validation status captured on it:");
                        com.teamcenter.soa.client.model.Property[] objNameProp2;

                        for(int x = 0; x < mdoOutputs.length; x++ )
                         {
                             objNameProp2 = getObjectProperties(mdoOutputs[x].mdoObject, new String[] {"object_name"});
                             LogFile.write( "MDThread named: " + objNameProp2[0].getDisplayableValue() + " uid: " + mdoOutputs[x].mdoObject.getUid() + "  is fetched." );
                             LogFile.write("Associated Design Artifact Details with mdo0NeedsValidation as true:\n{");
                             WorkspaceObject[] das = mdoOutputs[x].associatedDesignArtifact;
                             for(int y = 0; y < das.length; y++ )
                             {
                                 objNameProp2 = MDOTestUtils.getObjectProperties(das[y], new String[] {"object_name"});
                                 LogFile.write( "    Name: " + objNameProp2[0].getDisplayableValue() + " uid: " + das[y].getUid()  );
                             }
                             LogFile.write("}");

                         }
                    }//mdooutput

                    if(mdiOutputs != null && mdiOutputs.length > 0 )
                    {
                         LogFile.write("MDI Associations with Needs validation status captured on it:");
                             for(int x = 0; x < mdiOutputs.length; x++ )
                             {
                                ModelObject mdiObject = mdiOutputs[i].mdiObject;
                                 LogFile.write( "MDInstance uid: " + mdiObject.getUid() + "  is fetched." );
                                 LogFile.write("Associated Design Instances Details with mdo0NeedsValidation as true:\n{");
                                 for(int inx=0 ; inx < mdiOutputs[i].instances.length ; inx++)
                                  {
                                      LogFile.write( "    Design Instance named: "+mdiOutputs[i].instances[inx].get_object_string() + " uid: " + mdiOutputs[i].instances[inx].getUid()  );
                                  }
                                  LogFile.write("}");
                             }
                     }

                 }//For
             }//If for response
         }
         catch ( Exception ex )
         {
             LogFile.write( "printQueryNeedsValidationResults failed" );
             ex.printStackTrace();
         }

         return ;
     }

     /**
      * Updates needs Validation Links.
      * @param qryInputs Input for update
      * @return Response for the SOA
      */
    public UpdateLinksToValidatedResponse updateNeedsValidation( UpdateLinkStatusToValidatedInput[] updateInputs )
    {
        UpdateLinksToValidatedResponse resp = m_mdoService.updateLinksToValidated(updateInputs);
        if ( ! handleServiceData( resp.serviceData, "updateNeedsValidation" ) )
        {
            LogFile.write( "updateNeedsValidation: Failed" );
        }

        return resp;
    }

    /**
     * Update the domain relevancy
     * @param inputs Input structure
     * @return UpdateDomainRelevancyResponse
     */
    public UpdateDomainRelevancyResponse updateDomainRelevancy( DomainRelevancyInput[] inputs )
    {
        UpdateDomainRelevancyResponse response = m_mdoService.updateDomainRelevancy( inputs );
        if ( ! handleServiceData( response.serviceData, "updateDomainRelevancy" ) )
        {
            LogFile.write( "updateDomainRelevancy: Failed" );
        }
        return response;
    }

    /**
     * Get the domain relevancy
     * @param inputs Input structure
     * @return GetDomainRelevancyOfAnObjectResp
     */
    public GetDomainRelevancyOfAnObjectResp getDomainRelevancy( GetDomainRelevancyInput[] inputs )
    {
        GetDomainRelevancyOfAnObjectResp response = m_mdoService.getDomainRelevancyOfAnObject( inputs );
        if ( ! handleServiceData( response.serviceData, "getDomainRelevancy" ) )
        {
            LogFile.write( "getDomainRelevancy: Failed" );
        }
        return response;
    }

    /**
     * Split design artifacts from MDThread
     * @param daList List of design artifacts to be split from an MDThread and added to a new MDThread.
     * @return SplitDesignArtifacts Response
     */
    public SplitDesignArtifactsResponse splitDesignArtifacts( WorkspaceObject[] daList )
    {
        SplitDesignArtifactInput[] splitInputs = new SplitDesignArtifactInput[1];
        splitInputs[0] = new SplitDesignArtifactInput();
        splitInputs[0].clientId = "SplitDA";
        splitInputs[0].designArtifacts = daList;
        SplitDesignArtifactsResponse response = m_mdoService.splitDesignArtifactsOfMDThread( splitInputs );
        if ( ! handleServiceData( response.serviceData, "splitDesignArtifactsOfMDThread" ) )
        {
            LogFile.write( "splitDesignArtifactsOfMDThread: errored" );
        }
        return response;
    }

    /**
     * Split design instances from MDInstance
     * @param diList List of design instances to be split from an MDInstance and added to a new MDInstance.
     * @return SplitDesignInstances Response
     */
    public SplitDesignInstancesResponse splitDesignInstances( POM_object[] diList, POM_object context )
    {
        SplitDesignInstanceInput[] splitInputs = new SplitDesignInstanceInput[1];
        splitInputs[0] = new SplitDesignInstanceInput();
        splitInputs[0].clientId = "SplitDI";
        splitInputs[0].designInstances = diList;
        splitInputs[0].context = context;
        SplitDesignInstancesResponse response = m_mdoService.splitDesignInstancesOfMDInstance( splitInputs );
        if ( ! handleServiceData( response.serviceData, "splitDesignInstancesOfMDInstance" ) )
        {
            LogFile.write( "splitDesignArtifactsOfMDInstance: errored" );
        }
        return response;
    }
    /**
     * Creates a Change Notice
     * @param ecnName Name of the ECN
     * @param ecnDesc Description of the ECN
     * @return Newly created ECN object
     */
    public ModelObject createECN( String ecnName, String ecnDesc )
    {
        ModelObject ecnRev = null;
        try
        {
            CreateIn[] createIn = new CreateIn[1];
            createIn[0] = new CreateIn();
            createIn[0].clientId = "E2";
            createIn[0].data.boName = "ChangeNotice";
            createIn[0].data.stringProps.put( "object_name", ecnName );
            createIn[0].data.stringProps.put( "object_desc", ecnDesc );
            
            com.teamcenter.services.strong.core._2008_06.DataManagement.CreateInput compoundcreateInput = new com.teamcenter.services.strong.core._2008_06.DataManagement.CreateInput();
            compoundcreateInput.boName = "ChangeNoticeRevision";
            HashMap<String, com.teamcenter.services.strong.core._2008_06.DataManagement.CreateInput[]> compoundCreateInputMap =
                new HashMap<String, com.teamcenter.services.strong.core._2008_06.DataManagement.CreateInput[]>();
            compoundCreateInputMap.put("revision",
                    new com.teamcenter.services.strong.core._2008_06.DataManagement.CreateInput[] { compoundcreateInput });

            createIn[0].data.compoundCreateInput = compoundCreateInputMap;

            CreateResponse resp = MDOTestUtils.m_dmService.createObjects( createIn );
            ecnRev = resp.output[0].objects[2];
            m_dmService.getProperties(new ModelObject[] { ecnRev }, new String[] { "item_id" });
            System.out.println( "createECN created : " + ecnRev.getPropertyDisplayableValue("item_id") + "-" + ecnName );
        }
        catch( Exception ex )
        {
            ex.printStackTrace();
        }
        return ecnRev;
    }

    /**
     * Submits an ECN to workflow
     */
    public InstanceInfo submitECNToChangeWorkflow(ModelObject ecnRev)
    {
        InstanceInfo response = null;
        try {
            ContextData contextData = new ContextData();
            contextData.processTemplate = "ChangeNoticeRevisionDefaultWorkflowTemplate";
            contextData.attachments = new String[1];
            contextData.attachmentTypes = new int[1];
            contextData.attachmentCount = 1;
            contextData.attachments[0] = ecnRev.getUid();
            contextData.attachmentTypes[0] = 1;

            m_dmService.getProperties(new ModelObject[] { ecnRev }, new String[] { "item_id" });

            response = m_workflowService.createInstance(false, "",
                    ecnRev.getPropertyDisplayableValue("item_id"), "", "",
                    contextData);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return response;
    }
}
