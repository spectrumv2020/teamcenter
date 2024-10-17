//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.wireharness;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teamcenter.clientx.WHSession;
import com.teamcenter.schemas.soa._2006_03.exceptions.ServiceException;
import com.teamcenter.services.strong.allocations.AllocationService;
import com.teamcenter.services.strong.allocations._2007_01.Allocation.AllocationContextInput;
import com.teamcenter.services.strong.allocations._2007_01.Allocation.AllocationLineInfo;
import com.teamcenter.services.strong.allocations._2007_01.Allocation.AllocationWindowInfo;
import com.teamcenter.services.strong.allocations._2007_01.Allocation.GetAllocationWindowResponse;
import com.teamcenter.services.strong.core.SessionService;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.client.model.ServiceData;
import com.teamcenter.soa.client.model.strong.AllocationLine;
import com.teamcenter.soa.client.model.strong.AllocationMap;
import com.teamcenter.soa.client.model.strong.AllocationMapRevision;
import com.teamcenter.soa.client.model.strong.AllocationWindow;
import com.teamcenter.soa.client.model.strong.BOMLine;
import com.teamcenter.soa.client.model.strong.BOMView;
import com.teamcenter.soa.client.model.strong.BOMWindow;

public class Allocations
{
    protected AllocationService allocService = null;
    protected AllocationWindow allocationWindow = null;
    private Map<BOMLine, AllocationLine> fromBLAllocLineMap;
    private Map<BOMLine, AllocationLine> toBLAllocLineMap;
    private WireHarnessTestUtils whTestUtils = null;

    private class AllocatingBOMLines
    {
        public AllocatingBOMLines( ) {}
        public BOMLine[] fromBOMLines;
        public BOMLine[] toBOMLines;
    }

    public Allocations( )
    {
        allocService = AllocationService.getService(WHSession.getConnection());
        fromBLAllocLineMap = new HashMap<BOMLine, AllocationLine>();
        toBLAllocLineMap = new HashMap<BOMLine, AllocationLine>();
        whTestUtils = new WireHarnessTestUtils();

        setObjectPropPolicy();
    }

    /**
     * Set Object Property Policy
     */
    private void setObjectPropPolicy()
    {
        SessionService sessionService  = SessionService.getService( WHSession.getConnection() );
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
     * Call internal methods to load allocation data and create allocations
     * @param bomWindows
     * @param fromBLs
     * @param toBLs
     * @throws Exception
     */
    public void loadAndCreateAllocations( BOMWindow[] bomWindows,
                                            List<BOMLine> fromBLs,
                                            List<BOMLine> toBLs ) throws Exception
    {
        try
        {
            loadAllocations( bomWindows );
            loadAllocatingBomlines( fromBLs, toBLs );

            //Fix for PR#6086964
            //Save the allocations after creation
            ServiceData serviceData = allocService.saveAllocationWindow( allocationWindow );
            whTestUtils.handleServiceData( serviceData, "loadAndCreateAllocations" );
        }
        catch( Exception ex )
        {
            throw ex;
        }
    }

    /**
     * Load Allocating Bomlines info and create the allocations
     * @param fromBLs
     * @param toBLs
     * @throws Exception
     */
    private void loadAllocatingBomlines( List<BOMLine> fromBLs,
                                            List<BOMLine> toBLs ) throws Exception
    {
        try
        {
            for ( int idx = 0; idx < fromBLs.size(); idx++ )
            {
                AllocatingBOMLines[] allocBLsArr = new AllocatingBOMLines[1];
                allocBLsArr[0] = new AllocatingBOMLines();
                allocBLsArr[0].fromBOMLines = new BOMLine[1];
                allocBLsArr[0].fromBOMLines[0] = (BOMLine)fromBLs.get( idx );
                allocBLsArr[0].toBOMLines = new BOMLine[1];
                allocBLsArr[0].toBOMLines[0] = (BOMLine)toBLs.get( idx );

                //Create allocation
                this.allocateBOMLines( allocBLsArr );
            }
        }
        catch( Exception ex )
        {
            LogFile.write( "loadAllocatingBomlines : " + ex.getMessage() );
            throw ex;
        }
    }

    /**
     * Save and close allocation window
     *
     */
    protected void finalize()
    {
        ServiceData serviceData = allocService.saveAllocationWindow(allocationWindow);
        whTestUtils.handleServiceData( serviceData, "finalize" );

        serviceData = allocService.closeAllocationWindow( allocationWindow, true );
        whTestUtils.handleServiceData( serviceData, "finalize" );
    }

    /**
     * Loads allocations
     * @param bomWindows
     * @throws Exception
     */
    private void loadAllocations( BOMWindow[] bomWindows ) throws Exception
    {
        try
        {
            BOMView[] bomViews = new BOMView[bomWindows.length];
            for( int inx = 0; inx < bomWindows.length; inx++ )
            {
                BOMLine topLine = (BOMLine)bomWindows[inx].get_top_line();
                bomViews[inx] = (BOMView)topLine.get_bl_bomview();
            }

            ServiceData serviceData = allocService.findAllocationContexts( bomViews );
            if ( ! whTestUtils.handleServiceData( serviceData, "loadAllocations" ) )
            {
                return;
            }

            AllocationMap allocationMap = null;
            if( serviceData.sizeOfPlainObjects() > 0 )
            {
                allocationMap = ( AllocationMap )serviceData.getPlainObject( 0 );

                // Fix for PR#6020247 : Displaying Allocations info
                if( allocationMap != null )
                {
                    LogFile.write( "Allocation Map id :  " + allocationMap.get_current_id() );
                    LogFile.write( "Allocation Map name :  " + allocationMap.get_current_name() );
                }
            }

            GetAllocationWindowResponse allocationWindowResp;
            if( allocationMap == null )
            {
                AllocationContextInput allocationContextInput = new AllocationContextInput();
                allocationContextInput.id = "";
                allocationContextInput.name = "";
                allocationContextInput.revision = "";
                allocationContextInput.type = "";
                allocationContextInput.openedBOMWindows = bomWindows;
                allocationWindowResp = allocService.createAllocationContext( allocationContextInput );

                if ( ! whTestUtils.handleServiceData( allocationWindowResp.serviceData, "loadAllocations" ) )
                {
                    return;
                }

                // Fix for PR#6020247 : Displaying Allocations info
                AllocationMap allocMap = (AllocationMap)allocationWindowResp.serviceData.getCreatedObject( 0 );
                if( allocMap != null )
                {
                    LogFile.write( "Allocation Map id :  " + allocMap.get_current_id() );
                    LogFile.write( "Allocation Map name :  " + allocMap.get_current_name() );
                }
            }
            else
            {
                AllocationWindowInfo allocationWindowInfo = new AllocationWindowInfo();
                allocationWindowInfo.allocationContext = (AllocationMapRevision)allocationMap.get_item_revision();
                allocationWindowInfo.allocationRule = null;
                allocationWindowInfo.openedBOMWindows = bomWindows;

                allocationWindowResp = allocService.openAllocationWindow( allocationWindowInfo, null );
            }

            if ( ! whTestUtils.handleServiceData( allocationWindowResp.serviceData, "loadAllocations" ) )
            {
                return;
            }

            allocationWindow = allocationWindowResp.allocationWindow;

            for(int inc = 0; inc < allocationWindowResp.allocationLines.length; inc++)
            {
                populateAllocationsInfo( allocationWindowResp.allocationLines[inc] );
            }
        }
        catch ( Exception ex )
        {
            LogFile.write( ex.getMessage() );
            throw ex;
        }
    }

    /**
     * Create allocations
     * @param allocatingBOMLines
     * @throws Exception
     */
    public void allocateBOMLines( AllocatingBOMLines[] allocatingBOMLines ) throws Exception
    {
        try
        {
            AllocationLineInfo[] allocationLineInfo = new AllocationLineInfo[allocatingBOMLines.length];
            for(int inx = 0; inx < allocatingBOMLines.length; inx++)
            {
                allocationLineInfo[inx] = new AllocationLineInfo();
                allocationLineInfo[inx].name = "";
                allocationLineInfo[inx].type = "";
                allocationLineInfo[inx].reason = "";
                allocationLineInfo[inx].fromBOMLines = allocatingBOMLines[inx].fromBOMLines;
                allocationLineInfo[inx].toBOMLines = allocatingBOMLines[inx].toBOMLines;
            }

            ServiceData serviceData = allocService.addAllocationLines( allocationWindow, allocationLineInfo );
            if ( ! whTestUtils.handleServiceData( serviceData, "allocateBOMLines" ) )
            {
                return;
            }

            for(int inc = 0; inc < serviceData.sizeOfCreatedObjects(); inc++)
            {
                // Fix for PR#6020247 : Displaying Allocations info
                AllocationLine allocLine =  (AllocationLine)serviceData.getCreatedObject( inc );
                LogFile.write( "Added Allocation : " + allocLine.get_allocation_line_name() );

                populateAllocationsInfo( (AllocationLine)serviceData.getCreatedObject( inc ) );
            }
        }
        catch( Exception ex )
        {
            LogFile.write( "allocateBOMLines : " + ex.getMessage() );
            throw ex;
        }
    }

    /**
     * Remove Allocations
     *
     */
    public void unAllocateBOMLines( BOMLine[] fromBOMLines ) throws Exception
    {
        try
        {
            AllocationLine[] allocationLines = new AllocationLine[fromBOMLines.length];
            for( int inx = 0; inx < fromBOMLines.length; inx++ )
            {
                if( fromBLAllocLineMap.containsKey( fromBOMLines[inx]) )
                {
                    allocationLines[inx] = fromBLAllocLineMap.get( fromBOMLines[inx] );
                }
            }

            if( allocationLines.length > 0 )
            {
                ServiceData serviceData = allocService.deleteAllocationLines( allocationWindow, allocationLines );
                if ( ! whTestUtils.handleServiceData( serviceData, "unAllocateBOMLines" ) )
                {
                    return;
                }

                // Fix for PR#6020247 : Displaying Allocations info
                for( int inx = 0; inx < serviceData.sizeOfDeletedObjects(); inx++ )
                {
                    AllocationLine removedAllocLine =  (AllocationLine)allocationLines[ inx ];
                    LogFile.write( "Removed Allocation : " + removedAllocLine.get_allocation_line_name() );
                }
            }
        }
        catch( Exception ex )
        {
            LogFile.write( "unAllocateBOMLines : " + ex.getMessage() );
            throw ex;
        }
    }

    /**
     * Get 'Allocated From' info
     */
    public BOMLine[] getAllocatedFromBOMLines( BOMLine toBOMLine )
    {
        if( toBLAllocLineMap.containsKey( toBOMLine ) )
        {
            try
            {
                return (BOMLine[])toBLAllocLineMap.get(toBOMLine).get_allocation_line_sources();
            }
            catch ( Exception e )
            {
                LogFile.write( e.getMessage() );
            }
        }
        return null;
    }

    /**
     * Get 'Allocated To' info
     */
    public BOMLine[] getAllocatedToBOMLines( BOMLine fromBOMLine )
    {
        if( fromBLAllocLineMap.containsKey( fromBOMLine ) )
        {
            try
            {
                return (BOMLine[])fromBLAllocLineMap.get(fromBOMLine).get_allocation_line_targets();
            }
            catch ( Exception e )
            {
                LogFile.write( e.getMessage() );
            }
        }
        return null;
    }

    /**
     * Populate (cache) the allocations info for further use
     * @param allocLine
     * @throws Exception
     */
    private void populateAllocationsInfo( AllocationLine allocLine )
                                                        throws Exception
    {
        try
        {
            ModelObject[] fromBOMLines = allocLine.get_allocation_line_sources();
            for( int inx = 0; inx < fromBOMLines.length; inx++ )
            {
                fromBLAllocLineMap.put( (BOMLine)fromBOMLines[inx], allocLine );
            }

            ModelObject[] toBOMLines = allocLine.get_allocation_line_targets();
            for( int inx = 0; inx < toBOMLines.length; inx++ )
            {
                toBLAllocLineMap.put( (BOMLine)toBOMLines[inx], allocLine );
            }
        }
        catch ( Exception ex )
        {
            LogFile.write( ex.getMessage() );
            throw ex;
        }
    }
}
