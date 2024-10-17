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
import com.teamcenter.services.strong.bom._2008_06.StructureManagement.RemoveChildrenFromParentLineResponse;
import com.teamcenter.services.strong.cad._2008_06.StructureManagement.SaveBOMWindowsResponse;
import com.teamcenter.services.strong.core.SessionService;
import com.teamcenter.services.strong.core.StructureManagementService;
import com.teamcenter.services.strong.core._2008_06.StructureManagement.RemoveInStructureAssociationsInfo;
import com.teamcenter.services.strong.core._2008_06.StructureManagement.RemoveInStructureAssociationsResponse;
import com.teamcenter.services.strong.wireharness._2008_06.WireHarness.ConnectionData;
import com.teamcenter.services.strong.wireharness._2008_06.WireHarness.ConnectorData;
import com.teamcenter.services.strong.wireharness._2008_06.WireHarness.CreateOrUpdateHarnessResponse;
import com.teamcenter.services.strong.wireharness._2008_06.WireHarness.DeviceData;
import com.teamcenter.services.strong.wireharness._2008_06.WireHarness.HarnessData;
import com.teamcenter.services.strong.wireharness._2008_06.WireHarness.HarnessInfo;
import com.teamcenter.services.strong.wireharness._2008_06.WireHarness.HarnessObjectInfo;
import com.teamcenter.services.strong.wireharness._2008_06.WireHarness.ItemElementData;
import com.teamcenter.services.strong.wireharness._2008_06.WireHarness.WireInfo;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.client.model.strong.BOMLine;
import com.teamcenter.soa.client.model.strong.BOMWindow;
import com.teamcenter.wireharness.HarnessClientIDInfo.HRNObjInfo;

public class UpdateHarness
{
    protected com.teamcenter.services.strong.wireharness.WireHarnessService whService;
    protected StructureManagementService smService;
    protected com.teamcenter.services.strong.bom.StructureManagementService smBomService;
    protected com.teamcenter.services.strong.cad.StructureManagementService smCadService;

    private CreateHarness createHarnessObj = null;
    private HarnessObjects hrn_struct = null;
    private Map<String, HRNObjInfo> clientOccurrenceMap = null;
    private HarnessClientIDInfo hrnAssyOccInfo = null;
    private static final String CONNECT_TO = "ConnectTo";
    private static final String DEVICE_TO_CONNECTOR = "DeviceToConnector";
    private static final String IMPLEMENTED_BY = "ImplementedBy";
    private static final String ROUTED_BY="RoutedBy";
    private WireHarnessTestUtils whTestUtils = null;

    /**
     * Constructor
     */
    public UpdateHarness( CreateHarness createHarnObj )
    {
        smService = StructureManagementService.getService( WHSession.getConnection() );
        whService = com.teamcenter.services.strong.wireharness.WireHarnessService.getService( WHSession.getConnection() );
        smBomService = com.teamcenter.services.strong.bom.StructureManagementService.getService( WHSession.getConnection() );
        smCadService = com.teamcenter.services.strong.cad.StructureManagementService.getService( WHSession.getConnection() );

        createHarnessObj = createHarnObj;
        clientOccurrenceMap = createHarnessObj.getClientOccurrenceMap();
        hrnAssyOccInfo = createHarnessObj.getHarnessClientIDInfo();
        hrn_struct = createHarnessObj.getHarnessObjects();
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
     * Update the harness structure
     */
    public void updateHarnessStructure() throws Exception
    {
        try
        {
            removeFromHarnessStructure();
            addToHarnessStructure();
        }
        catch( Exception ex )
        {
            throw ex;
        }
    }

    /**
     * We'll remove Following BOMLines from the Harness structure (created by
     * CreateHarness class )
     *      Connector3
     *      Connection3
     *      Device3
     *      Wire3
     *      Connector6
     * Relations involving these lines will also be removed
     */
    private void removeFromHarnessStructure() throws Exception
    {
        try
        {
            // First, get all the BOMLine we want to remove.
            BOMLine ctr3Line = (BOMLine)clientOccurrenceMap.get( hrnAssyOccInfo.ctr3_id ).bomline;
            BOMLine conn3Line = (BOMLine)clientOccurrenceMap.get( hrnAssyOccInfo.conn3_id ).bomline;
            BOMLine dev3Line = (BOMLine)clientOccurrenceMap.get( hrnAssyOccInfo.device3_id ).bomline;
            BOMLine wire3Line = (BOMLine)clientOccurrenceMap.get( hrnAssyOccInfo.w3_id ).bomline;
            BOMLine ctr6Line = (BOMLine)clientOccurrenceMap.get( hrnAssyOccInfo.d3_ctr6_id ).bomline;

            // First lets remove the relations
            RemoveInStructureAssociationsInfo[] remStructInfoArr =
                                new RemoveInStructureAssociationsInfo[4];

            // For ConnectTo relations
            ModelObject[] secondariesArr0 = new ModelObject[0];

            remStructInfoArr[0] = new RemoveInStructureAssociationsInfo();
            remStructInfoArr[0].contextBOMLine = null;
            remStructInfoArr[0].associationType = CONNECT_TO;
            remStructInfoArr[0].primaryBOMLine = conn3Line;
            remStructInfoArr[0].secondaries = secondariesArr0;  //PortA and TermI

            // For ImplementedBy relation
            ModelObject[] secondariesArr1 = new ModelObject[1];
            secondariesArr1[0] = wire3Line;

            remStructInfoArr[1] = new RemoveInStructureAssociationsInfo();
            remStructInfoArr[1].contextBOMLine = null;
            remStructInfoArr[1].associationType = IMPLEMENTED_BY;
            remStructInfoArr[1].primaryBOMLine = conn3Line;
            remStructInfoArr[1].secondaries = secondariesArr1;  //wire3Line

            // For Device to Connector relation
            ModelObject[] secondariesArr2 = new ModelObject[1];
            secondariesArr2[0] = ctr6Line;

            remStructInfoArr[2] = new RemoveInStructureAssociationsInfo();
            remStructInfoArr[2].contextBOMLine = null;
            remStructInfoArr[2].associationType = DEVICE_TO_CONNECTOR;
            remStructInfoArr[2].primaryBOMLine = dev3Line;
            remStructInfoArr[2].secondaries = secondariesArr2;  //ctr6Line

            // For Routed By Relation
            ModelObject[] secondariesArr3 = new ModelObject[1];
            secondariesArr3[0] = createHarnessObj.getRouteWithSegment();

            remStructInfoArr[3] = new RemoveInStructureAssociationsInfo();
            remStructInfoArr[3].contextBOMLine = null;
            remStructInfoArr[3].associationType = ROUTED_BY;
            remStructInfoArr[3].primaryBOMLine = wire3Line;
            remStructInfoArr[3].secondaries = secondariesArr3;

            RemoveInStructureAssociationsResponse remStructResp =
                smService.removeInStructureAssociations( remStructInfoArr );
            if ( ! whTestUtils.handleServiceData( remStructResp.serviceData, "" ) )
            {
                LogFile.write( "removeFromHarnessStructure : Failed to Remove Associations" );
                return;
            }

            // Now lets remove the BOMLines
            BOMLine[] linesToBeDeletedArr = new BOMLine[4];
            linesToBeDeletedArr[0] = ctr3Line;
            linesToBeDeletedArr[1] = dev3Line;
            linesToBeDeletedArr[2] = conn3Line;
            linesToBeDeletedArr[3] = wire3Line;

            RemoveChildrenFromParentLineResponse remChFromParLineResp =
                smBomService.removeChildrenFromParentLine( linesToBeDeletedArr );
            if ( ! whTestUtils.handleServiceData( remChFromParLineResp.serviceData, "" ) )
            {
                return;
            }

            if ( remChFromParLineResp.serviceData.sizeOfPartialErrors() == 0 )
            {
                // Also Set the entries from map to null for removed lines
                clientOccurrenceMap.get( hrnAssyOccInfo.ctr6_id ).bomline = null;
                clientOccurrenceMap.get( hrnAssyOccInfo.ctr6_id ).occ = null;
                clientOccurrenceMap.get( hrnAssyOccInfo.ctr6_id ).occThd = null;

                clientOccurrenceMap.get( hrnAssyOccInfo.device3_id ).bomline = null;
                clientOccurrenceMap.get( hrnAssyOccInfo.device3_id ).occ = null;
                clientOccurrenceMap.get( hrnAssyOccInfo.device3_id ).occThd = null;

                clientOccurrenceMap.get( hrnAssyOccInfo.conn3_id ).bomline = null;
                clientOccurrenceMap.get( hrnAssyOccInfo.conn3_id ).occ = null;
                clientOccurrenceMap.get( hrnAssyOccInfo.conn3_id ).occThd = null;

                clientOccurrenceMap.get( hrnAssyOccInfo.w3_id ).bomline = null;
                clientOccurrenceMap.get( hrnAssyOccInfo.w3_id ).occ = null;
                clientOccurrenceMap.get( hrnAssyOccInfo.w3_id ).occThd = null;
            }
            else
            {
                LogFile.write( "removeFromHarnessStructure : Failed to Remove BOMLines" );
            }

            // mariappa - Fix for PR6486858
            // Save the bomwindow after relations and lines are removed

            BOMWindow  bomWindow = null;
            BOMLine conn2Line = (BOMLine) clientOccurrenceMap.get( hrnAssyOccInfo.conn2_id).bomline;
            bomWindow = (BOMWindow)conn2Line.get_bl_window();
            BOMWindow [] bomWindowArr = new BOMWindow[1];
            bomWindowArr[0] = bomWindow;

            SaveBOMWindowsResponse saveBomWinResp = smCadService.saveBOMWindows(bomWindowArr);
            if ( ! whTestUtils.handleServiceData( saveBomWinResp.serviceData, "" ) )
            {
                return;
            }

        }
        catch( Exception ex )
        {
            LogFile.write( "removeFromHarnessStructure : " + ex.getMessage() );
            throw ex;
        }
    }

    /**
     * Adds new lines to the structure
     */
    private void addToHarnessStructure() throws Exception
    {
        try
        {
            HRNObjInfo hrn_obj = clientOccurrenceMap.get( hrnAssyOccInfo.harness_id );

            HarnessObjectInfo harness_occ_info = new HarnessObjectInfo();
            harness_occ_info.clientId = "";
            harness_occ_info.object = null;
            harness_occ_info.objectOcc = hrn_obj.occ;
            harness_occ_info.objectOccThread = hrn_obj.occThd;

            HarnessData hrnData = new HarnessData();
            HarnessData[] hrnDataArr = new HarnessData[1];

            hrnData.devices = new DeviceData[1];
            populateDeviceOccs( hrnData.devices );

            hrnData.connectors = new ConnectorData[1];
            populateCtrOccs( hrnData.connectors );

            hrnData.connections = new ConnectionData[1];
            populateConnOccs( hrnData.connections );

            hrnData.wires = new WireInfo[1];
            populateWireOccs( hrnData.wires );

            hrnData.harness = harness_occ_info;
            Map<String, String> attrMap = new HashMap<String, String>();
            hrnData.harnessOccProps = attrMap;

            hrnDataArr[0] = hrnData;

            // Now Update the harness info for the SOA
            HarnessInfo[] hrnInfoArr = new HarnessInfo[1];
            HarnessInfo hrnInfo = new HarnessInfo();
            hrnInfo.clientId = "";
            hrnInfo.harnessData = hrnDataArr;
            hrnInfo.parent = hrn_struct.hrn_assy_rev;
            hrnInfoArr[0] = hrnInfo;

            CreateOrUpdateHarnessResponse harnResp =
                whService.createOrUpdateHarness( hrnInfoArr, "ElectricalHarness", false );
            if ( ! whTestUtils.handleServiceData( harnResp.serviceData, "" ) )
            {
                return;
            }
        }
        catch ( Exception ex )
        {
            LogFile.write( "addToHarnessStructure : " + ex.toString() );
            throw ex;
        }
    }

    /**
     * populates the client id, itemrev (or GDE objecs) into client map.
     */
    private void addModObjToClientMap( String clientId, ModelObject obj )
    {
        HarnessClientIDInfo.HRNObjInfo hrn_obj = hrnAssyOccInfo.clientMap.get( clientId );
        hrn_obj.obj = obj;
        hrnAssyOccInfo.clientMap.put( clientId, hrn_obj );
    }

    /**
     * Populate device 4 data
     */
    private void populateDev4Occs( DeviceData dev )
    {
        // Add device 4
        dev.device = new HarnessObjectInfo();
        dev.device.clientId = hrnAssyOccInfo.device4_id;
        dev.device.object = hrn_struct.device_revs.get(3);
        addModObjToClientMap( dev.device.clientId, dev.device.object );

        // Add 8 connectors to device 4
        dev.connectors = new ConnectorData[1];
        ConnectorData ctr = new ConnectorData();
        ctr.connector.clientId = hrnAssyOccInfo.d4_ctr8_id;
        ctr.connector.object = hrn_struct.ctr_revs.get(7);
        addModObjToClientMap( ctr.connector.clientId, ctr.connector.object );

        ctr.ports = new ItemElementData[2];
        ctr.ports[0] = new ItemElementData();
        ctr.ports[0].itemElement = new HarnessObjectInfo();
        ctr.ports[0].itemElement.clientId = hrnAssyOccInfo.d4_ctr8_trm_t1_id;
        ctr.ports[0].itemElement.object = hrn_struct.ctr8_item_elems.get(0);
        addModObjToClientMap( ctr.ports[0].itemElement.clientId,
                                    ctr.ports[0].itemElement.object );
        ctr.ports[1] = new ItemElementData();
        ctr.ports[1].itemElement = new HarnessObjectInfo();
        ctr.ports[1].itemElement.clientId = hrnAssyOccInfo.d4_ctr8_trm_t2_id;
        ctr.ports[1].itemElement.object = hrn_struct.ctr8_item_elems.get(1);
        addModObjToClientMap( ctr.ports[1].itemElement.clientId,
                                    ctr.ports[1].itemElement.object );
        dev.connectors[0] = ctr;

        HarnessObjectInfo[] assoCtrsArr = new HarnessObjectInfo[1];
        assoCtrsArr[0] = ctr.connector;
        dev.associatedConnectors = assoCtrsArr;
    }


    /**
     * Populate connector 7 data
     */
    private void populateCtrOccs( ConnectorData[] ctrs )
    {
        ctrs[0] = new ConnectorData();
        ctrs[0].connector.clientId = hrnAssyOccInfo.ctr7_id;
        ctrs[0].connector.object = hrn_struct.ctr_revs.get(6);
        addModObjToClientMap( ctrs[0].connector.clientId, ctrs[0].connector.object );
        populateCtr7TrmOccs( ctrs[0] );
    }

    /**
     * Populate connector 7 terminals
     */
    private void populateCtr7TrmOccs( ConnectorData ctr_occ )
    {
        int num_ports = 2;
        ctr_occ.ports = new ItemElementData[2];
        for(int idx = 0; idx<num_ports; idx++)
        {
            ctr_occ.ports[idx] = new ItemElementData();
            HarnessObjectInfo trm_a = new HarnessObjectInfo();
            trm_a.object = hrn_struct.ctr7_item_elems.get(idx);

            if(idx == 0)
            {
                trm_a.clientId = hrnAssyOccInfo.ctr7_trm_t1_id;
            }
            else if(idx == 1)
            {
                trm_a.clientId = hrnAssyOccInfo.ctr7_trm_t2_id;
            }
            ctr_occ.ports[idx].itemElement = trm_a;
            addModObjToClientMap( trm_a.clientId, trm_a.object );
        }
    }

    /**
     * Populate connection occs under harness
     */
    private void populateConnOccs( ConnectionData[] conn_occs )
    {
        // connection4 data
        conn_occs[0] = new ConnectionData();
        conn_occs[0].connection = new HarnessObjectInfo();
        conn_occs[0].connection.clientId = hrnAssyOccInfo.conn4_id;
        conn_occs[0].connection.object = hrn_struct.connection_revs.get(3);
        addModObjToClientMap( conn_occs[0].connection.clientId, conn_occs[0].connection.object );

        // Associated ports for connection4
        conn_occs[0].associatedPorts = new HarnessObjectInfo[2];
        conn_occs[0].associatedPorts[0] = new HarnessObjectInfo();
        conn_occs[0].associatedPorts[0].clientId = hrnAssyOccInfo.ctr7_trm_t1_id;
        conn_occs[0].associatedPorts[0].object = hrn_struct.ctr7_item_elems.get(0);
        conn_occs[0].associatedPorts[1] = new HarnessObjectInfo();
        conn_occs[0].associatedPorts[1].clientId = hrnAssyOccInfo.d4_ctr8_trm_t1_id;
        conn_occs[0].associatedPorts[1].object = hrn_struct.ctr8_item_elems.get(1);

        //Associated wires for connection4
        conn_occs[0].associatedWires = new HarnessObjectInfo[1];
        conn_occs[0].associatedWires[0] = new HarnessObjectInfo();
        conn_occs[0].associatedWires[0].clientId = hrnAssyOccInfo.w4_id;
        conn_occs[0].associatedWires[0].object = hrn_struct.wire_revs.get(3);
    }

    /**
     * Populate wire occs under harness
     */
    private void populateWireOccs( WireInfo[] wires )
    {
        wires[0] = new WireInfo();
        wires[0].wire = new HarnessObjectInfo();
        wires[0].wire.clientId = hrnAssyOccInfo.w4_id;
        wires[0].wire.object = hrn_struct.wire_revs.get( 3 );
        wires[0].wireOccProps = new HashMap<String,String>();
        wires[0].wireOccProps.put( "fabrication", "PSG4" );
        wires[0].wireOccProps.put( "multiplier", "4.0" );
        wires[0].wireOccProps.put( "offset", "4.0" );
        wires[0].wireOccProps.put( "description", "PSG Wire4" );
        wires[0].wireOccProps.put( "wire_length", "420.32" );
        wires[0].wireOccProps.put("connector_id", "conn4" );

        addModObjToClientMap( wires[0].wire.clientId, wires[0].wire.object );
    }

    /**
     * Populate device occurrences under harness
     */
    private void populateDeviceOccs( DeviceData[] devices )
    {
        devices[0] = new DeviceData();
        populateDev4Occs( devices[0] );
    }
}
