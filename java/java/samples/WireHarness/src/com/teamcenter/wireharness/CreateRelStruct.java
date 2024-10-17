//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.wireharness;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import com.teamcenter.clientx.WHSession;
import com.teamcenter.soa.client.model.strong.BOMLine;
import com.teamcenter.soa.client.model.strong.BOMWindow;
import com.teamcenter.soa.client.model.strong.Item;
import com.teamcenter.soa.client.model.strong.ItemRevision;
import com.teamcenter.services.strong.cad._2007_01.StructureManagement.CreateBOMWindowsInfo;
import com.teamcenter.services.strong.cad._2007_01.StructureManagement.CreateBOMWindowsResponse;
import com.teamcenter.services.strong.bom._2008_06.StructureManagement.AddOrUpdateChildrenToParentLineInfo;
import com.teamcenter.services.strong.bom._2008_06.StructureManagement.AddOrUpdateChildrenToParentLineResponse;
import com.teamcenter.services.strong.bom._2008_06.StructureManagement.ItemLineInfo;
import com.teamcenter.services.strong.bom.StructureManagementService;

public class CreateRelStruct
{
    protected com.teamcenter.services.strong.cad.StructureManagementService smService;
    protected StructureManagementService smBomService;

    private BOMWindow bomWindow = null;
    private BOMLine topLine = null;
    private List <BOMLine> ctrLines = null;
    private WireHarnessTestUtils whTestUtils;
    private List <ItemRevision> ctr_revs;
    private List <Item> ctrs;
    private ItemRevision mech_assy_rev;

    public CreateRelStruct()
    {
        whTestUtils = new WireHarnessTestUtils();
        ctr_revs = new ArrayList<ItemRevision>();
        ctrLines = new ArrayList<BOMLine>();
        ctrs = new ArrayList<Item>();
        smService =
            com.teamcenter.services.strong.cad.StructureManagementService.getService( WHSession.getConnection() );
        smBomService = StructureManagementService.getService( WHSession.getConnection() );
    }

    /**
     * Create mechanical assembly to be used to create allocations
     */
    public void createMechStructure() throws Exception
    {
        try
        {
            createToplevelAndCtrItems();
            createBomLines();
        }
        catch( Exception ex )
        {
            throw ex;
        }
    }

    /**
     * Create Top level Item And Connector Items
     */
    private void createToplevelAndCtrItems() throws Exception
    {
        try
        {
            List<Item> relstr_items = new ArrayList<Item>();
            List<ItemRevision> revs = new ArrayList<ItemRevision>();
            List <String> item_ids = new ArrayList<String>();
            int num_itms = 3;
            int num_ctrs = num_itms - 1; // first item is top level
            String[] item_names = new String[3];
            item_names[0] = "mech_assy";
            item_names[1] = "ctr1";
            item_names[2] = "ctr2";

            whTestUtils.createItems( num_itms, item_names, "Item", item_ids, relstr_items, revs );
            LogFile.write( "Mechanical Assembly -id:    " + item_ids.get(0) + "   mech_assy: " + item_names[0] + "    created successfully." );

            mech_assy_rev = revs.get( 0 );
            for( int jdx = 0; jdx<num_ctrs; jdx++ )
            {
                ctr_revs.add( revs.get( jdx+1 ) );
                ctrs.add( relstr_items.get( jdx+1 ) );
                LogFile.write( "Connector (mechanical assembly) - id:  " + item_ids.get( jdx+1 ) + "   name:  " + item_names[jdx+1] + "   created successfully." );
            }
        }
        catch( Exception ex )
        {
            LogFile.write( "createToplevelAndCtrItems : " + ex.getMessage() );
            throw ex;
        }
    }

    /**
     * Create BomLines
     */
    private void createBomLines() throws Exception
    {
        try
        {
            CreateBOMWindowsInfo[] createBOMWindowsInfo = new CreateBOMWindowsInfo[1];
            createBOMWindowsInfo[0] = new CreateBOMWindowsInfo();
            createBOMWindowsInfo[0].itemRev = mech_assy_rev;

            CreateBOMWindowsResponse createBOMWindowsResponse =
                    smService.createBOMWindows( createBOMWindowsInfo );
            if ( ! whTestUtils.handleServiceData( createBOMWindowsResponse.serviceData, "createBomLines" ) )
            {
                return;
            }

            bomWindow = createBOMWindowsResponse.output[0].bomWindow;
            topLine = createBOMWindowsResponse.output[0].bomLine;

            int num_lines = ctrs.size();

            ItemLineInfo[] itmLineInfoArr = new ItemLineInfo[num_lines];

            for(int idx= 0; idx<num_lines; idx++)
            {
                itmLineInfoArr[idx] = new ItemLineInfo();
                itmLineInfoArr[idx].bomline = null;
                itmLineInfoArr[idx].clientId = "Connector Occurrence" + idx;
                itmLineInfoArr[idx].item = (Item)ctrs.get( idx );
                itmLineInfoArr[idx].itemRev = (ItemRevision)ctr_revs.get( idx );
                itmLineInfoArr[idx].occType = "";
                itmLineInfoArr[idx].itemLineProperties = new HashMap<String, String>();
            }

            AddOrUpdateChildrenToParentLineInfo[] addChToParInfoArr =
                                new AddOrUpdateChildrenToParentLineInfo[1];
            AddOrUpdateChildrenToParentLineInfo addChToParInfo =
                                new AddOrUpdateChildrenToParentLineInfo();
            addChToParInfo.items = itmLineInfoArr;
            addChToParInfo.viewType = "view";
            addChToParInfo.parentLine =  topLine;

            addChToParInfoArr[0] = addChToParInfo;

            AddOrUpdateChildrenToParentLineResponse AddUpdChToParResp =
                    smBomService.addOrUpdateChildrenToParentLine( addChToParInfoArr );
            if ( ! whTestUtils.handleServiceData( AddUpdChToParResp.serviceData, "createBomLines" ) )
            {
                return;
            }

            for(int idx = 0; idx<num_lines; idx++)
            {
                ctrLines.add( AddUpdChToParResp.itemLines[idx].bomline );
            }

            BOMWindow[] bomWindows = new BOMWindow[1];
            bomWindows[0] = bomWindow;
            smService.saveBOMWindows( bomWindows );
        }
        catch( Exception ex )
        {
            LogFile.write( "createBomLines : " + ex.getMessage() );
            throw ex;
        }
    }

    /**
     * Get BomWindow
     */
    public BOMWindow getBomWindow()
    {
        return this.bomWindow;
    }

    /**
     * Get Topline
     */
    public BOMLine getTopline()
    {
        return this.topLine;
    }

    /**
     * Get Connector Lines
     */
    public List <BOMLine> getCtrLines()
    {
        return this.ctrLines;
    }
}
