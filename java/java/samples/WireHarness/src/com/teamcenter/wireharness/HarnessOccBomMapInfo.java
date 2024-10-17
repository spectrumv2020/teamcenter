//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================


package com.teamcenter.wireharness;

import java.util.HashMap;
import java.util.Map;
import com.teamcenter.clientx.WHSession;

import com.teamcenter.schemas.soa._2006_03.exceptions.ServiceException;
import com.teamcenter.services.strong.cad.StructureManagementService;
import com.teamcenter.services.strong.cad._2007_01.StructureManagement.CreateBOMWindowsInfo;
import com.teamcenter.services.strong.cad._2007_01.StructureManagement.CreateBOMWindowsResponse;
import com.teamcenter.services.strong.cad._2008_06.StructureManagement.ExpandPSAllLevelsOutput;
import com.teamcenter.services.strong.cad._2008_06.StructureManagement.ExpandPSAllLevelsResponse2;
import com.teamcenter.services.strong.cad._2008_06.StructureManagement.ExpandPSAllLevelsInfo;
import com.teamcenter.services.strong.cad._2008_06.StructureManagement.ExpandPSAllLevelsPref;
import com.teamcenter.services.strong.cad._2008_06.StructureManagement.RelatedObjectTypeAndNamedRefs;
import com.teamcenter.services.strong.cad._2008_06.StructureManagement.RelationAndTypesFilter;

import com.teamcenter.services.strong.core.DataManagementService;
import com.teamcenter.services.strong.core.SessionService;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.client.model.strong.BOMLine;
import com.teamcenter.soa.client.model.strong.BOMWindow;
import com.teamcenter.soa.client.model.strong.ItemRevision;

public class HarnessOccBomMapInfo
{
    protected StructureManagementService smService = null;
    protected DataManagementService dmService = null;

    protected BOMWindow bomWindow = null;
    protected BOMLine topLine = null;
    private WireHarnessTestUtils whTestUtils = null;

    /**
     * Constructor
     */
    public HarnessOccBomMapInfo()
    {
        dmService = DataManagementService.getService( WHSession.getConnection() );
        smService = StructureManagementService.getService( WHSession.getConnection() );

        whTestUtils = new WireHarnessTestUtils();
        setObjectPropPolicy();
    }

    /**
     * Set Object Property Policy
     */
    private void setObjectPropPolicy()
    {
        SessionService sessionService = SessionService.getService( WHSession.getConnection() );
        try
        {
            sessionService.setObjectPropertyPolicy( "WireHarnessPolicy" );
        }
        catch ( ServiceException ex )
        {
            LogFile.write( ex.getMessage() );
        }
    }

    /**
     * Get Harness structure
     */
    public HarnessStructureData getHarnessStructure( ItemRevision itemRev, String view ) throws Exception
    {
        try
        {
            return getHarnessStructure( itemRev,null, view );
        }
        catch( Exception ex )
        {
            throw ex;
        }
    }

    /**
     * Get Harness structure
     */
    public HarnessStructureData getHarnessStructure( BOMWindow bomWindow ) throws Exception
    {
        try
        {
            return getHarnessStructure( null, bomWindow, null );
        }
        catch( Exception ex )
        {
            throw ex;
        }
    }

    /**
     * Get Harness structure
     */
    private HarnessStructureData getHarnessStructure( ItemRevision itemRev,
                                    BOMWindow bomWindow, String view ) throws Exception
    {
        try
        {
            if( bomWindow == null )
            {
                //Create the bomWindow
                CreateBOMWindowsInfo[] bomInfoArr = populateBOMWindowInfo( itemRev );
                CreateBOMWindowsResponse resp = smService.createBOMWindows( bomInfoArr );
                if ( ! whTestUtils.handleServiceData( resp.serviceData, "" ) )
                {
                    return null;
                }
                topLine = resp.output[0].bomLine;
                bomWindow = resp.output[0].bomWindow;
            }
            else
            {
                topLine = (BOMLine) bomWindow.get_top_line();
            }

            ExpandPSAllLevelsInfo expandAllInfo = new ExpandPSAllLevelsInfo();
            ExpandPSAllLevelsPref allPref = new ExpandPSAllLevelsPref();
            allPref.info = new RelationAndTypesFilter[1];
            RelationAndTypesFilter relAndTypefilter = new RelationAndTypesFilter();
            relAndTypefilter.relationName = "IMAN_specification";

            relAndTypefilter.relatedObjAndNamedRefs = new RelatedObjectTypeAndNamedRefs[2];
            relAndTypefilter.relatedObjAndNamedRefs[0] = new RelatedObjectTypeAndNamedRefs();
            relAndTypefilter.relatedObjAndNamedRefs[0].objectTypeName = "UGMASTER";
            relAndTypefilter.relatedObjAndNamedRefs[1] = new RelatedObjectTypeAndNamedRefs();
            relAndTypefilter.relatedObjAndNamedRefs[1].objectTypeName = "UGPART";
            relAndTypefilter.namedRefHandler = "NoNamedRefs";

            allPref.info[0] = relAndTypefilter;
            allPref.expItemRev = true;

            expandAllInfo.parentBomLines = new BOMLine[1];

            expandAllInfo.parentBomLines[0] = topLine;
            expandAllInfo.excludeFilter = "None2";


            ExpandPSAllLevelsResponse2 expAllResp = smService.expandPSAllLevels( expandAllInfo, allPref );
            ExpandPSAllLevelsOutput[] allLevelsOutput = expAllResp.output;
            int noOfLevels = allLevelsOutput.length;

            Map<ModelObject,BOMLine> occurrenceThreadBOMLineMap = new HashMap<ModelObject,BOMLine>();
            Map<ModelObject,BOMLine> occurrenceBOMLineMap = new HashMap<ModelObject,BOMLine>();

            for(int i=0; i< noOfLevels; i++)
            {
                BOMLine bomLine = allLevelsOutput[i].parent.bomLine;
                // Fill occurrence info
                ModelObject childOcc = bomLine.get_bl_real_occurrence();
                occurrenceBOMLineMap.put( childOcc, bomLine );

                // Fill occurrence thread info
                ModelObject childThreadOcc = bomLine.get_bl_occurrence();
                occurrenceThreadBOMLineMap.put( childThreadOcc, bomLine );
            }

            HarnessStructureData harnessStructureData = new HarnessStructureData();
            harnessStructureData.occThreadBOMLineMap = occurrenceThreadBOMLineMap;
            harnessStructureData.occBOMLineMap = occurrenceBOMLineMap;
            harnessStructureData.itemRev = itemRev;
            harnessStructureData.viewType = view;
            harnessStructureData.topLine = topLine;
            harnessStructureData.noOfLinesInHarness = noOfLevels;
            harnessStructureData.bomWindow = bomWindow;

            return harnessStructureData;
        }
        catch( Exception ex )
        {
            LogFile.write( "getHarnessStructure : " + ex.getMessage() );
            throw ex;
        }
    }

    /**
     * Populate BOMWindow Information
     */
    private CreateBOMWindowsInfo[] populateBOMWindowInfo( ItemRevision itemRev )
    {
        CreateBOMWindowsInfo[] bomInfo = new CreateBOMWindowsInfo[1];
        bomInfo[0] = new CreateBOMWindowsInfo();
        bomInfo[0].itemRev = itemRev;
        return bomInfo;
    }

    public class HarnessStructureData
    {
        private ItemRevision itemRev = null;
        private String viewType = null;
        private BOMWindow bomWindow = null;

        private Map<ModelObject, BOMLine> occThreadBOMLineMap;
        private Map<ModelObject, BOMLine> occBOMLineMap;
        private BOMLine topLine = null;
        private int noOfLinesInHarness = 0;

        /**
         * Obtain Bomline from Occurrence or OccurrenceThread
         */
        public BOMLine getBOMLine( ModelObject occ, ModelObject occThread )
        {
            BOMLine bomLine = null;
            if( occThread != null && occThreadBOMLineMap.containsKey( occThread ) )
            {
                bomLine = occThreadBOMLineMap.get( occThread );

                if( bomLine != null &&  occ != null && occBOMLineMap.containsKey( occ ) )
                {
                    BOMLine occBomLine = occBOMLineMap.get( occ );
                    if( occBomLine != bomLine )
                    {
                        bomLine = null;
                    }
                }
            }
            return bomLine;
        }

        /**
         * Get Harness Revision
         */
        public ItemRevision getHarnessRevision()
        {
            return itemRev;
        }

        /**
         * Get Harness View Type
         */
        public String getHarnessViewType()
        {
            return viewType;
        }

        /**
         * Get Harness Structure Window
         */
        public BOMWindow getHarnessStructureWindow()
        {
            return bomWindow;
        }

        /**
         */
        public BOMLine getHarnessTopLine()
        {
            return topLine;
        }

        /**
         * Get Occ Bomline Map
         */
        public Map<ModelObject, BOMLine> getOccBomlineMap()
        {
            return occBOMLineMap;
        }

        /**
         * Get Occ Thread Bomline Map
         */
        public Map<ModelObject, BOMLine> getOccThreadBomlineMap()
        {
            return occThreadBOMLineMap;
        }

        /**
         * Get No Of Configured Lines
         */
        public int getNoOfConfiguredLines()
        {
            return noOfLinesInHarness;
        }
    }
}

