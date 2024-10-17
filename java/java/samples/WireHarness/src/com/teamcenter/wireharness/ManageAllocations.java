//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================


package com.teamcenter.wireharness;

import java.util.List;

import com.teamcenter.clientx.WHSession;
import com.teamcenter.soa.client.model.strong.BOMLine;
import com.teamcenter.soa.client.model.strong.BOMWindow;

public class ManageAllocations
{
    protected com.teamcenter.services.strong.cad.StructureManagementService smService2007_01;
    private BOMWindow [] bwindows = null;
    private CreateHarness hrn_assy = null;
    private CreateRelStruct mech_assy = null;
    private Allocations alloc = null;

    /**
     * Constructor
     */
    public ManageAllocations( CreateHarness hrn_assy, CreateRelStruct mech_assy )
    {
        smService2007_01 =
            com.teamcenter.services.strong.cad.StructureManagementService.getService( WHSession.getConnection() );
        this.mech_assy = mech_assy;
        this.hrn_assy = hrn_assy;

        bwindows = new BOMWindow[2];
        bwindows[0] = mech_assy.getBomWindow();
        bwindows[1] = hrn_assy.getHarnessStructureData().getHarnessStructureWindow();

        alloc = new Allocations();
    }

    /**
     * Creates Allocations
     * @throws Exception
     */
    public void createAllocations() throws Exception
    {
        try
        {
            List<BOMLine> mech_ctr_lines = mech_assy.getCtrLines();
            List<BOMLine> hrn_ctr_lines = hrn_assy.populateCtrLines();
            alloc.loadAndCreateAllocations( bwindows, mech_ctr_lines, hrn_ctr_lines );
        }
        catch ( Exception ex )
        {
            throw ex;
        }
    }

    /**
     * Removes allocations for given BOMLines
     * @throws Exception
     */
    public void removeAllocations() throws Exception
    {
        try
        {
            BOMLine[] blsToBeUnAllocated = new BOMLine[1];
            blsToBeUnAllocated[0] = mech_assy.getCtrLines().get( 0 );
            alloc.unAllocateBOMLines( blsToBeUnAllocated );
            alloc.finalize();
        }
        catch ( Exception ex )
        {
            throw ex;
        }
    }

    /**
     * This method closes the mechanical assembly, harness assembly windows
     */
    public void closeBomWindows() throws Exception
    {
        try
        {
            //Close the BOMWindow
            smService2007_01.closeBOMWindows( bwindows );
        }
        catch( Exception ex )
        {
            LogFile.write("closeBomWindows : " + ex.getMessage() );
            throw ex;
        }
    }
}
