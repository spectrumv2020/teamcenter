//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.wireharness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teamcenter.clientx.WHSession;
import com.teamcenter.services.strong.cad._2008_06.StructureManagement.SaveBOMWindowsResponse;
import com.teamcenter.services.strong.core.StructureManagementService;
import com.teamcenter.services.strong.core._2008_06.StructureManagement.CreateInStructureAssociationResponse;
import com.teamcenter.services.strong.core._2008_06.StructureManagement.InStructureAssociationInfo;
import com.teamcenter.services.strong.wireharness._2008_06.WireHarness.CableData;
import com.teamcenter.services.strong.wireharness._2008_06.WireHarness.ConnectionData;
import com.teamcenter.services.strong.wireharness._2008_06.WireHarness.ConnectorData;
import com.teamcenter.services.strong.wireharness._2008_06.WireHarness.CreateOrUpdateHarnessResponse;
import com.teamcenter.services.strong.wireharness._2008_06.WireHarness.DeviceData;
import com.teamcenter.services.strong.wireharness._2008_06.WireHarness.HarnessData;
import com.teamcenter.services.strong.wireharness._2008_06.WireHarness.HarnessInfo;
import com.teamcenter.services.strong.wireharness._2008_06.WireHarness.HarnessObjectInfo;
import com.teamcenter.services.strong.wireharness._2008_06.WireHarness.ItemElementData;
import com.teamcenter.services.strong.wireharness._2008_06.WireHarness.ShieldData;
import com.teamcenter.services.strong.wireharness._2008_06.WireHarness.WireInfo;
import com.teamcenter.soa.client.Connection;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.client.model.strong.BOMLine;
import com.teamcenter.soa.client.model.strong.BOMWindow;
import com.teamcenter.soa.client.model.strong.Item;
import com.teamcenter.soa.client.model.strong.ItemRevision;
import com.teamcenter.soa.client.model.strong.PSBOMViewRevision;
import com.teamcenter.wireharness.HarnessClientIDInfo.HRNObjInfo;
import com.teamcenter.wireharness.HarnessOccBomMapInfo.HarnessStructureData;


public class CreateHarness {

    private Connection connection = null;
    protected com.teamcenter.services.strong.wireharness.WireHarnessService whService;
    protected StructureManagementService smService;

    protected com.teamcenter.services.strong.cad.StructureManagementService smServiceCad;


    private WireHarnessTestUtils whTestUtils = null;
    private ModelObject route_with_curve = null;
    private ModelObject route_with_segment = null;
    private static final String ROUTED_BY = "RoutedBy";
    private HarnessStructureData hrnStructData = null;
    private HarnessObjects hrn_struct = null;
    private HarnessClientIDInfo hrn_assy_occ_info = null;
    private List<BOMLine> ctr_lines = null;

    /**
     * Constructor
     */
    public CreateHarness()
    {
        connection = WHSession.getConnection();
        whService = com.teamcenter.services.strong.wireharness.WireHarnessService.getService( connection );
        smService = StructureManagementService.getService( connection );

        whTestUtils = new WireHarnessTestUtils();
        ctr_lines = new ArrayList<BOMLine>();

        smServiceCad = com.teamcenter.services.strong.cad.StructureManagementService.getService( connection );
    }

    /**
     * Calls all the necessary internal methods required for creation of
     * max complexity structure and Route info.
     */
    public void createHarnessStructure() throws Exception
    {
        try
        {
            populate_harness_data();
            createMaxComplexityHarnessStructure();
            createRouteInfo();
            associateRoutesWithWires();
        }
        catch ( Exception ex )
        {
            // Rethrow ex up the calling order
            throw ex;
        }
    }

    /**
     * Create Route Objects
     */
    private void createRouteInfo() throws Exception
    {
        try
        {
            ItemRevision rev = (ItemRevision)hrn_struct.hrn_assy_rev;
            PSBOMViewRevision[] bvrs = rev.get_structure_revisions();
            ModelObject context = bvrs[0];
            CreateOrUpdateRouteObjects r_info = new CreateOrUpdateRouteObjects( context );
            route_with_curve = r_info.createRouteUsingCurve();
            route_with_segment = r_info.createRouteDataUsingSegments();
        }
        catch( Exception ex )
        {
            LogFile.write( "createRouteInfo : " + ex.getMessage() );
            // Re-throw the exception
            throw ex;
        }

    }


    /**
     * Create all the hrn objects: assy, harness, ctrs, gdes, connections, wires
     *  cables and shields. Auto generates item ids for all the above objects since,
     *  the client can be run any number of times
     */
    private void populate_harness_data()
    {
        hrn_struct = new HarnessObjects();
        createHrnAssyItem();
        createHrnItem();
        createConnectors();
        ctr1_createConnectionTerminals();
        ctr2_createConnectionTerminals();
        ctr3_createConnectionTerminals();

        ctr4_createConnectionTerminals();
        ctr5_createConnectionTerminals();
        ctr6_createConnectionTerminals();
        ctr7_createConnectionTerminals();
        ctr8_createConnectionTerminals();

        createConnections();
        createDevices();
        createWires();
        createCables();
        createShields();

        LogFile.write( "Harness data populated successfuly" );
    }

    /**
     * Creates the top level harness assembly with name HarnessAssy
     */
    private void createHrnAssyItem()
    {
        try
        {
            List <String> item_ids = new ArrayList<String>();
            List<Item> hrn_assy_item = new ArrayList<Item>();
            List<ItemRevision> revs = new ArrayList<ItemRevision>();
            int num_itms = 1;
            String[] item_names = new String[1];
            item_names[0] = "HarnessAssy";

            whTestUtils.createItems( num_itms, item_names, "Item", item_ids, hrn_assy_item, revs );
            LogFile.write( "Harness Assembly - id:  " + item_ids.get(0) + "   name:" + item_names[0] + "   created successfully." );
            hrn_struct.hrn_assy_rev = revs.get(0);
        }
        catch( Exception ex )
        {
            LogFile.write( ex.getMessage() );
        }

    }

    /**
     * Creates the harness item with name Harness
     */
    private void createHrnItem()
    {
        try
        {
            List <String> item_ids = new ArrayList<String>();
            List<Item> hrn_item = new ArrayList<Item>();
            List<ItemRevision> revs = new ArrayList<ItemRevision>();
            int num_itms = 1;
            String[] item_names = new String[1];
            item_names[0] = "Harness";

            whTestUtils.createItems( num_itms, item_names, "Item", item_ids, hrn_item, revs );
            LogFile.write( "Harness -id:    " + item_ids.get(0) + "   name: " + item_names[0] + "    created successfully." );
            hrn_struct.hrn_rev = revs.get(0);
        }
        catch( Exception ex )
        {
            LogFile.write( ex.getMessage() );
        }

    }

    /**
     * Create 3 connectors with names connector0001, connector0002, connector0003
     */
    private void createConnectors()
    {
        try
        {
            List <String> item_ids = new ArrayList<String>();
            List<Item> ctrs = new ArrayList<Item>();
            int num_ctrs = 8;
            String[] ctr_names = new String[num_ctrs];

            for ( int idx = 0; idx<num_ctrs; idx++ )
            {
                int id = idx+1;
                ctr_names[idx] = "connector000" + id;
            }
            whTestUtils.createItems( num_ctrs, ctr_names, "HRN_ConHousing", item_ids,
                    ctrs, hrn_struct.ctr_revs );
            for(int jdx = 0; jdx<num_ctrs; jdx++)
            {
                LogFile.write( "Connector - id:  " + item_ids.get(jdx) + "   name:  " + ctr_names[jdx] + "   created successfully." );
            }
        }
        catch ( Exception ex )
        {
            LogFile.write( ex.getMessage() );
        }
    }

    /**
     * Create GDEs pinA, pinB
     */
    private void ctr1_createConnectionTerminals()
    {
        int num_gdes = 2;
        String [] gde_names = new String[num_gdes];
        gde_names[0] = "pinA";
        gde_names[1] = "pinB";
        whTestUtils.createItemElements( num_gdes, gde_names,
                "Connection_Terminal", hrn_struct.ctr1_item_elems );
        for(int idx = 0; idx<num_gdes; idx++)
        {
            LogFile.write( "Connection Terminal - name:  " + gde_names[idx] + "   created successfully." );
        }
    }

    /**
     * Create GDEs p1, p2
     */
    private void ctr2_createConnectionTerminals()
    {
        int num_gdes = 2;
        String [] gde_names = new String[num_gdes];
        gde_names[0] = "p1";
        gde_names[1] = "p2";
        whTestUtils.createItemElements( num_gdes, gde_names,
                "Connection_Terminal", hrn_struct.ctr2_item_elems );
        for(int idx = 0; idx<num_gdes; idx++)
        {
            LogFile.write( "Connection Terminal - name:  " + gde_names[idx] + "   created successfully." );
        }
    }
    /**
     * Create GDEs portA, PortB
     */
    private void ctr3_createConnectionTerminals()
    {
        int num_gdes = 2;
        String [] gde_names = new String[num_gdes];
        gde_names[0] = "portA";
        gde_names[1] = "portB";
        whTestUtils.createItemElements( num_gdes, gde_names,
                "Connection_Terminal", hrn_struct.ctr3_item_elems );
        for(int idx = 0; idx<num_gdes; idx++)
        {
            LogFile.write( "Connection Terminal - name:  " + gde_names[idx] + "   created successfully." );
        }
    }

    /**
     * Create GDE's for Connector4
     */
    private void ctr4_createConnectionTerminals()
    {
        int num_gdes = 2;
        String [] gde_names = new String[num_gdes];
        gde_names[0] = "TermA";
        gde_names[1] = "TermB";
        whTestUtils.createItemElements( num_gdes, gde_names,
                "Connection_Terminal", hrn_struct.ctr4_item_elems );
        for(int idx = 0; idx<num_gdes; idx++)
        {
            LogFile.write( "Connection Terminal - name:  " + gde_names[idx] + "   created successfully." );
        }
    }

    /**
     * Create GDE's for Connector5
     */
    private void ctr5_createConnectionTerminals()
    {
        int num_gdes = 2;
        String [] gde_names = new String[num_gdes];
        gde_names[0] = "Term1";
        gde_names[1] = "Term2";
        whTestUtils.createItemElements( num_gdes, gde_names,
                "Connection_Terminal", hrn_struct.ctr5_item_elems );
        for(int idx = 0; idx<num_gdes; idx++)
        {
            LogFile.write( "Connection Terminal - name:  " + gde_names[idx] + "   created successfully." );
        }
    }

    /**
     * Create GDE's for Connector6
     */
    private void ctr6_createConnectionTerminals()
    {
        int num_gdes = 2;
        String [] gde_names = new String[num_gdes];
        gde_names[0] = "TermI";
        gde_names[1] = "TermII";
        whTestUtils.createItemElements( num_gdes, gde_names,
                "Connection_Terminal", hrn_struct.ctr6_item_elems );
        for(int idx = 0; idx<num_gdes; idx++)
        {
            LogFile.write( "Connection Terminal - name:  " + gde_names[idx] + "   created successfully." );
        }
    }

    /**
     * Create GDE's for Connector7
     */
    private void ctr7_createConnectionTerminals()
    {
        int num_gdes = 2;
        String [] gde_names = new String[num_gdes];
        gde_names[0] = "PI";
        gde_names[1] = "PII";
        whTestUtils.createItemElements( num_gdes, gde_names,
                "Connection_Terminal", hrn_struct.ctr7_item_elems );
        for(int idx = 0; idx<num_gdes; idx++)
        {
            LogFile.write( "Connection Terminal - name:  " + gde_names[idx] + "   created successfully." );
        }
    }

    /**
     * Create GDE's for Connector8
     */
    private void ctr8_createConnectionTerminals()
    {
        int num_gdes = 2;
        String [] gde_names = new String[num_gdes];
        gde_names[0] = "PortI";
        gde_names[1] = "PortII";
        whTestUtils.createItemElements( num_gdes, gde_names,
                "Connection_Terminal", hrn_struct.ctr8_item_elems );
        for(int idx = 0; idx<num_gdes; idx++)
        {
            LogFile.write( "Connection Terminal - name:  " + gde_names[idx] + "   created successfully." );
        }
    }

    /**
     * Create 4 connections with names
     * connection0001, connection0002, connection0003
     * connection0004
     */
    private void createConnections()
    {
        List <ModelObject> conns = new ArrayList<ModelObject>();
        List <String> conn_ids = new ArrayList<String>();

        try
        {
            int num_conns = 4;
            String[] conn_names = new String[num_conns];

            for (int idx = 0; idx<num_conns; idx++)
            {
                int id = idx+1;
                conn_names[idx] = "connection000" + id;
            }
            whTestUtils.createConnections( num_conns,  conn_names, "Connection", conn_ids,
                    conns, hrn_struct.connection_revs );
            for (int jdx = 0; jdx<num_conns; jdx++)
            {
                LogFile.write( "Connection -id:  " + conn_ids.get(jdx)+ "  name:  " + conn_names[jdx] + "    created successfully." );
            }
        }
        catch ( Exception ex )
        {
            LogFile.write( ex.getMessage() );
        }
    }

    /**
     * Create 4 devices with names device0001, device0002,
     *  device0003, device0004
     */
    private void createDevices()
    {
        try
        {
            List<Item> devs = new ArrayList<Item>();
            List <String> item_ids = new ArrayList<String>();

            int num_devs = 4;

            String[] dev_names = new String[num_devs];

            for (int idx = 0; idx<num_devs; idx++)
            {
                int id = idx+1;
                dev_names[idx] = "device000" + id;
            }
            whTestUtils.createItems( num_devs, dev_names, "HRN_AssemblyPart", item_ids, devs, hrn_struct.device_revs );
            for(int jdx = 0; jdx<num_devs; jdx++)
            {
                LogFile.write( "Device - id:  " + item_ids.get(jdx) + "    name: " + dev_names[jdx] + "   created successfully." );
            }
        }
        catch ( Exception ex )
        {
            LogFile.write( ex.getMessage() );
        }
    }

    /**
     * Create 4 wires with names Wire0001, Wire0002,
     *      Wire0003, Wire0004
     */
    private void createWires()
    {
        try
        {
            List<Item> wrs = new ArrayList<Item>();
            List <String> item_ids = new ArrayList<String>();

            int num_wrs = 4;
            String[] wr_names = new String[num_wrs];

            for (int idx = 0; idx<num_wrs; idx++)
            {
                int id = idx+1;
                wr_names[idx] = "Wire000" + id;
            }
            whTestUtils.createItems( num_wrs, wr_names, "HRN_GeneralWire", item_ids, wrs, hrn_struct.wire_revs );
            for(int jdx = 0; jdx<num_wrs; jdx++)
            {
                LogFile.write( "Wire - id: " + item_ids.get(jdx) + "   name: " + wr_names[jdx] + "    created successfully." );
            }
        }
        catch ( Exception ex )
        {
            LogFile.write( ex.getMessage() );
        }
    }


    /**
     * Create cables with names  Simple Cable, Complex Cable
     */
    private void createCables()
    {
        try
        {
            List<Item> cables = new ArrayList<Item>();
            List<ItemRevision> cable_revs = new ArrayList<ItemRevision>();
            List <String> item_ids = new ArrayList<String>();
            int num_cables = 2;

            String[] cable_names = new String[num_cables];
            cable_names[0] = "Simple Cable";
            cable_names[1] = "Complex Cable";

            whTestUtils.createItems( num_cables, cable_names, "HRN_Cable", item_ids, cables, cable_revs );
            hrn_struct.simple_cable_rev = cable_revs.get(0);
            hrn_struct.complex_cable_rev = cable_revs.get(1);
            for(int jdx = 0; jdx<num_cables; jdx++)
            {
                LogFile.write( "Cable - id: " + item_ids.get(jdx) + "    name: " + cable_names[jdx] + "    created successfully." );
            }
        }
        catch ( Exception ex )
        {
            LogFile.write( ex.getMessage() );
        }

    }

    /**
     * Create shields with names  Simple Shield, Complex Shield
     */
    private void createShields()
    {
        try
        {
            List<Item> shields = new ArrayList<Item>();
            List<ItemRevision> shield_revs = new ArrayList<ItemRevision>();
            List <String> item_ids = new ArrayList<String>();
            int num_shields = 2;

            String[] shield_names = new String[num_shields];
            shield_names[0] = "Simple Shield";
            shield_names[1] = "Complex Shield";

            whTestUtils.createItems( num_shields, shield_names, "HRN_Shield", item_ids, shields, shield_revs );
            hrn_struct.simple_shield_rev = shield_revs.get(0);
            hrn_struct.complex_shield_rev = shield_revs.get(1);
            for(int jdx = 0; jdx<num_shields; jdx++)
            {
                LogFile.write( "Shield - id:  " + item_ids.get(jdx) + "    name: " + shield_names[jdx] + "    created successfully." );
            }
        }
        catch ( Exception ex )
        {
            LogFile.write( ex.getMessage() );
        }
    }

    /**
     * populates the client id, itemrev (or GDE objecs) into client map.
     */
    private void addModObjToClientMap( String clientId, ModelObject obj )
    {
        HarnessClientIDInfo.HRNObjInfo hrn_obj = hrn_assy_occ_info.clientMap.get( clientId );
        hrn_obj.obj = obj;
        hrn_assy_occ_info.clientMap.put( clientId, hrn_obj );
    }

    /**
     * populate connector 1 terminals
     */
    private void populateCtr1TrmOccs( ConnectorData ctr_occ )
    {
        int num_ports = 2;
        ctr_occ.ports = new ItemElementData[2];
        for(int idx = 0; idx<num_ports; idx++)
        {
            ctr_occ.ports[idx] = new ItemElementData();
            HarnessObjectInfo trm_a = new HarnessObjectInfo();
            trm_a.object = hrn_struct.ctr1_item_elems.get(idx);

            if(idx == 0)
            {
                trm_a.clientId = hrn_assy_occ_info.ctr1_trma_id;
            }
            else if(idx == 1)
            {
                trm_a.clientId = hrn_assy_occ_info.ctr1_trmb_id;
            }
            ctr_occ.ports[idx].itemElement = trm_a;
            addModObjToClientMap(trm_a.clientId, trm_a.object);
        }
    }

    /**
     * populate connector 2 terminals
     */
    private void populateCtr2TrmOccs( ConnectorData ctr_occ )
    {
        int num_ports = 2;
        ctr_occ.ports = new ItemElementData[2];
        for(int idx = 0; idx<num_ports; idx++)
        {
            ctr_occ.ports[idx] = new ItemElementData();
            HarnessObjectInfo trm_a = new HarnessObjectInfo();
            trm_a.object = hrn_struct.ctr2_item_elems.get(idx);
            if(idx == 0)
            {
                trm_a.clientId = hrn_assy_occ_info.ctr2_trm_pin1_id;
            }
            else if(idx == 1)
            {
                trm_a.clientId = hrn_assy_occ_info.ctr2_trm_pin2_id;
            }
            ctr_occ.ports[idx].itemElement = trm_a;
            addModObjToClientMap(trm_a.clientId, trm_a.object);
        }
    }

    /**
     * populate connector 3 terminals
     */
    private void populateCtr3TrmOccs( ConnectorData ctr_occ )
    {
        int num_ports = 2;
        ctr_occ.ports = new ItemElementData[2];
        for(int idx = 0; idx<num_ports; idx++)
        {
            ctr_occ.ports[idx] = new ItemElementData();
            HarnessObjectInfo trm_a = new HarnessObjectInfo();
            if(idx == 0)
            {
                trm_a.clientId = hrn_assy_occ_info.ctr3_trm_p1_id;
            }
            else if(idx == 1)
            {
                trm_a.clientId = hrn_assy_occ_info.ctr3_trm_p2_id;
            }
            trm_a.object = hrn_struct.ctr3_item_elems.get(idx);
            ctr_occ.ports[idx].itemElement = trm_a;
            addModObjToClientMap( trm_a.clientId, trm_a.object );
        }
    }

    /**
     * Populate connector 1 data
     */
    private void populateCtr1Occs( ConnectorData ctr_occ )
    {
        ctr_occ.connector = new HarnessObjectInfo();
        ctr_occ.connector.clientId = hrn_assy_occ_info.ctr1_id;
        ctr_occ.connector.object = hrn_struct.ctr_revs.get(0);
        addModObjToClientMap( hrn_assy_occ_info.ctr1_id, ctr_occ.connector.object );
        populateCtr1TrmOccs(ctr_occ);
    }

    /**
     * Populate connector 2 data
     */
    private void populateCtr2Occs( ConnectorData ctr_occ )
    {
        ctr_occ.connector = new HarnessObjectInfo();
        ctr_occ.connector.clientId = hrn_assy_occ_info.ctr2_id;
        ctr_occ.connector.object = hrn_struct.ctr_revs.get(1);
        addModObjToClientMap( hrn_assy_occ_info.ctr2_id, ctr_occ.connector.object );
        populateCtr2TrmOccs( ctr_occ );
    }

    /**
     * Populate connector 3 data
     */
    private void populateCtr3Occs( ConnectorData ctr_occ )
    {
        ctr_occ.connector = new HarnessObjectInfo();
        ctr_occ.connector.clientId = hrn_assy_occ_info.ctr3_id;
        ctr_occ.connector.object = hrn_struct.ctr_revs.get(2);
        addModObjToClientMap( hrn_assy_occ_info.ctr3_id, ctr_occ.connector.object );
        populateCtr3TrmOccs( ctr_occ );
    }

    /**
     * Populate connector occs under harness
     */
    private void populateCtrOccs( ConnectorData[] ctrs )
    {
        ctrs[0] = new ConnectorData();
        ctrs[1] = new ConnectorData();
        ctrs[2] = new ConnectorData();
        populateCtr1Occs( ctrs[0] );
        populateCtr2Occs( ctrs[1] );
        populateCtr3Occs( ctrs[2] );
    }

    /**
     * Populate connection occs under harness
     */
    private  void populateConnOccs( ConnectionData[] conn_occs )
    {
        // connection1 data
        conn_occs[0] = new ConnectionData();
        conn_occs[0].connection = new HarnessObjectInfo();
        conn_occs[0].connection.clientId = hrn_assy_occ_info.conn1_id;
        conn_occs[0].connection.object = hrn_struct.connection_revs.get(0);
        addModObjToClientMap( conn_occs[0].connection.clientId, conn_occs[0].connection.object );

        // Associated ports for connection1
        conn_occs[0].associatedPorts = new HarnessObjectInfo[2];
        conn_occs[0].associatedPorts[0] = new HarnessObjectInfo();
        conn_occs[0].associatedPorts[0].clientId = hrn_assy_occ_info.ctr1_trma_id;
        conn_occs[0].associatedPorts[0].object = hrn_struct.ctr1_item_elems.get(0);
        conn_occs[0].associatedPorts[1] = new HarnessObjectInfo();
        conn_occs[0].associatedPorts[1].clientId = hrn_assy_occ_info.d1_ctr4_trm_t1_id;
        conn_occs[0].associatedPorts[1].object = hrn_struct.ctr4_item_elems.get(1);

        //Associated wires for connection1
        conn_occs[0].associatedWires = new HarnessObjectInfo[1];
        conn_occs[0].associatedWires[0] = new HarnessObjectInfo();
        conn_occs[0].associatedWires[0].clientId = hrn_assy_occ_info.w1_id;
        conn_occs[0].associatedWires[0].object = hrn_struct.wire_revs.get(0);


        // connection2 data
        conn_occs[1] = new ConnectionData();
        conn_occs[1].connection = new HarnessObjectInfo();
        conn_occs[1].connection.clientId = hrn_assy_occ_info.conn2_id;
        conn_occs[1].connection.object = hrn_struct.connection_revs.get(1);
        addModObjToClientMap( conn_occs[1].connection.clientId, conn_occs[1].connection.object );

        // Associated ports for connection2
        conn_occs[1].associatedPorts = new HarnessObjectInfo[2];
        conn_occs[1].associatedPorts[0] = new HarnessObjectInfo();
        conn_occs[1].associatedPorts[0].clientId = hrn_assy_occ_info.ctr2_trm_pin1_id;
        conn_occs[1].associatedPorts[0].object = hrn_struct.ctr2_item_elems.get(0);

        conn_occs[1].associatedPorts[1] = new HarnessObjectInfo();
        conn_occs[1].associatedPorts[1].clientId = hrn_assy_occ_info.d2_ctr5_trm_t1_id;
        conn_occs[1].associatedPorts[1].object = hrn_struct.ctr5_item_elems.get(1);

        //Associated wires for connection2
        conn_occs[1].associatedWires = new HarnessObjectInfo[1];
        conn_occs[1].associatedWires[0] = new HarnessObjectInfo();
        conn_occs[1].associatedWires[0].clientId = hrn_assy_occ_info.w2_id;
        conn_occs[1].associatedWires[0].object = hrn_struct.wire_revs.get(1);

        // connection3 data
        conn_occs[2] = new ConnectionData();
        conn_occs[2].connection = new HarnessObjectInfo();
        conn_occs[2].connection.clientId = hrn_assy_occ_info.conn3_id;
        conn_occs[2].connection.object = hrn_struct.connection_revs.get(2);
        addModObjToClientMap( conn_occs[2].connection.clientId, conn_occs[2].connection.object );

        // Associated ports for connection3
        conn_occs[2].associatedPorts = new HarnessObjectInfo[2];
        conn_occs[2].associatedPorts[0] = new HarnessObjectInfo();
        conn_occs[2].associatedPorts[0].clientId = hrn_assy_occ_info.ctr3_trm_p1_id;
        conn_occs[2].associatedPorts[0].object = hrn_struct.ctr3_item_elems.get(0);

        conn_occs[2].associatedPorts[1] = new HarnessObjectInfo();
        conn_occs[2].associatedPorts[1].clientId = hrn_assy_occ_info.d3_ctr6_trm_t1_id;
        conn_occs[2].associatedPorts[1].object = hrn_struct.ctr6_item_elems.get(1);

        //Associated wires for connection3
        conn_occs[2].associatedWires = new HarnessObjectInfo[1];
        conn_occs[2].associatedWires[0] = new HarnessObjectInfo();
        conn_occs[2].associatedWires[0].clientId = hrn_assy_occ_info.w3_id;
        conn_occs[2].associatedWires[0].object = hrn_struct.wire_revs.get(2);

    }

    /**
     * Populate wire occs under harness
     */
    private  void populateWireOccs( WireInfo[] wires )
    {
        wires[0] = new WireInfo();

        wires[0].wire = new HarnessObjectInfo();
        wires[0].wire.clientId = hrn_assy_occ_info.w1_id;
        wires[0].wire.object = hrn_struct.wire_revs.get( 0 );
        wires[0].wireOccProps = new HashMap<String,String>();
        wires[0].wireOccProps.put("fabrication", "PSG1");
        wires[0].wireOccProps.put("multiplier", "1.0");
        wires[0].wireOccProps.put("offset", "1.0");
        wires[0].wireOccProps.put("description", "PSG Wire1");
        wires[0].wireOccProps.put("wire_length", "120.32");
        wires[0].wireOccProps.put("connector_id", "conn1");
        addModObjToClientMap( wires[0].wire.clientId, wires[0].wire.object );

        wires[1] = new WireInfo();

        wires[1].wire = new HarnessObjectInfo();
        wires[1].wire.clientId = hrn_assy_occ_info.w2_id;
        wires[1].wire.object = hrn_struct.wire_revs.get( 1 );
        wires[1].wireOccProps = new HashMap<String,String>();
        wires[1].wireOccProps.put("fabrication", "PSG2");
        wires[1].wireOccProps.put("multiplier", "2.0");
        wires[1].wireOccProps.put("offset", "3.0");
        wires[1].wireOccProps.put("description", "PSG Wire2");
        wires[1].wireOccProps.put("wire_length", "220.32");
        wires[1].wireOccProps.put("connector_id", "conn2");
        addModObjToClientMap( wires[1].wire.clientId, wires[1].wire.object );

        wires[2] = new WireInfo();

        wires[2].wire = new HarnessObjectInfo();
        wires[2].wire.clientId = hrn_assy_occ_info.w3_id;
        wires[2].wire.object = hrn_struct.wire_revs.get(2);
        wires[2].wireOccProps = new HashMap<String,String>();
        wires[2].wireOccProps.put("fabrication", "PSG3");
        wires[2].wireOccProps.put("multiplier", "4.0");
        wires[2].wireOccProps.put("offset", "4.0");
        wires[2].wireOccProps.put("description", "PSG Wire3");
        wires[2].wireOccProps.put("wire_length", "320.32");
        wires[2].wireOccProps.put("connector_id", "conn3");
        addModObjToClientMap( wires[2].wire.clientId, wires[2].wire.object );
    }

    /**
     * Populate device 1 data
     */
    private void populateDev1Occs( DeviceData dev )
    {
        // Add device 1
        dev.device = new HarnessObjectInfo();
        dev.device.clientId = hrn_assy_occ_info.device1_id;
        dev.device.object = hrn_struct.device_revs.get(0);
        addModObjToClientMap( dev.device.clientId, dev.device.object );

        // Add 3 connectors to device 1
        dev.connectors = new ConnectorData[1];
        ConnectorData ctr = new ConnectorData();
        ctr.connector = new HarnessObjectInfo();
        ctr.connector.clientId = hrn_assy_occ_info.d1_ctr4_id;
        ctr.connector.object = hrn_struct.ctr_revs.get(3);
        addModObjToClientMap( ctr.connector.clientId, ctr.connector.object );
        ctr.ports = new ItemElementData[2];
        ctr.ports[0] = new ItemElementData();
        ctr.ports[0].itemElement = new HarnessObjectInfo();
        ctr.ports[0].itemElement.clientId = hrn_assy_occ_info.d1_ctr4_trm_t1_id;
        ctr.ports[0].itemElement.object = hrn_struct.ctr4_item_elems.get(0);
        addModObjToClientMap( hrn_assy_occ_info.d1_ctr4_trm_t1_id, ctr.ports[0].itemElement.object );
        ctr.ports[1] = new ItemElementData();
        ctr.ports[1].itemElement = new HarnessObjectInfo();
        ctr.ports[1].itemElement.clientId = hrn_assy_occ_info.d1_ctr4_trm_t2_id;
        ctr.ports[1].itemElement.object = hrn_struct.ctr4_item_elems.get(1);
        addModObjToClientMap( hrn_assy_occ_info.d1_ctr4_trm_t2_id, ctr.ports[1].itemElement.object );
        dev.connectors[0] = ctr;

        HarnessObjectInfo[] assoCtrsArr = new HarnessObjectInfo[1];
        assoCtrsArr[0] = ctr.connector;
        dev.associatedConnectors = assoCtrsArr;
    }

    /**
     * Populate device 2 data
     */
    private void populateDev2Occs( DeviceData dev )
    {
        // Add device 2
        dev.device = new HarnessObjectInfo();
        dev.device.clientId = hrn_assy_occ_info.device2_id;
        dev.device.object = hrn_struct.device_revs.get(1);
        addModObjToClientMap( dev.device.clientId, dev.device.object );

        // Add 3 connectors to device 2
        dev.connectors = new ConnectorData[1];
        ConnectorData ctr = new ConnectorData();
        ctr.connector = new HarnessObjectInfo();
        ctr.connector.clientId = hrn_assy_occ_info.d2_ctr5_id;
        ctr.connector.object = hrn_struct.ctr_revs.get(4);
        addModObjToClientMap( ctr.connector.clientId, ctr.connector.object );
        ctr.ports = new ItemElementData[2];
        ctr.ports[0] = new ItemElementData();
        ctr.ports[0].itemElement = new HarnessObjectInfo();
        ctr.ports[0].itemElement.clientId = hrn_assy_occ_info.d2_ctr5_trm_t1_id;
        ctr.ports[0].itemElement.object = hrn_struct.ctr5_item_elems.get(0);
        addModObjToClientMap( ctr.ports[0].itemElement.clientId,
                                    ctr.ports[0].itemElement.object );
        ctr.ports[1] = new ItemElementData();
        ctr.ports[1].itemElement = new HarnessObjectInfo();
        ctr.ports[1].itemElement.clientId = hrn_assy_occ_info.d2_ctr5_trm_t2_id;
        ctr.ports[1].itemElement.object = hrn_struct.ctr5_item_elems.get(1);
        addModObjToClientMap( ctr.ports[1].itemElement.clientId,
                                    ctr.ports[1].itemElement.object );
        dev.connectors[0] = ctr;

        HarnessObjectInfo[] assoCtrsArr = new HarnessObjectInfo[1];
        assoCtrsArr[0] = ctr.connector;
        dev.associatedConnectors = assoCtrsArr;
    }

    /**
     * Populate device 3 data
     */
    private void populateDev3Occs( DeviceData dev )
    {
        // Add device 3
        dev.device = new HarnessObjectInfo();
        dev.device.clientId = hrn_assy_occ_info.device3_id;
        dev.device.object = hrn_struct.device_revs.get(2);
        addModObjToClientMap( dev.device.clientId, dev.device.object );

        // Add 3 connectors to device 3
        dev.connectors = new ConnectorData[1];
        ConnectorData ctr = new ConnectorData();
        ctr.connector = new HarnessObjectInfo();
        ctr.connector.clientId = hrn_assy_occ_info.d3_ctr6_id;
        ctr.connector.object = hrn_struct.ctr_revs.get(5);
        addModObjToClientMap( ctr.connector.clientId, ctr.connector.object );
        ctr.ports = new ItemElementData[2];
        ctr.ports[0] = new ItemElementData();
        ctr.ports[0].itemElement = new HarnessObjectInfo();
        ctr.ports[0].itemElement.clientId = hrn_assy_occ_info.d3_ctr6_trm_t1_id;
        ctr.ports[0].itemElement.object = hrn_struct.ctr6_item_elems.get(0);
        addModObjToClientMap( ctr.ports[0].itemElement.clientId,
                                    ctr.ports[0].itemElement.object );
        ctr.ports[1] = new ItemElementData();
        ctr.ports[1].itemElement = new HarnessObjectInfo();
        ctr.ports[1].itemElement.clientId = hrn_assy_occ_info.d3_ctr6_trm_t2_id;
        ctr.ports[1].itemElement.object = hrn_struct.ctr6_item_elems.get(1);
        addModObjToClientMap( ctr.ports[1].itemElement.clientId,
                                    ctr.ports[1].itemElement.object );
        dev.connectors[0] = ctr;

        HarnessObjectInfo[] assoCtrsArr = new HarnessObjectInfo[1];
        assoCtrsArr[0] = ctr.connector;
        dev.associatedConnectors = assoCtrsArr;
    }

    /**
     * Populate device occurrences under harness
     */
    private void populateDeviceOccs( DeviceData[] devices )
    {
        devices[0] = new DeviceData();
        populateDev1Occs( devices[0] );

        devices[1] = new DeviceData();
        populateDev2Occs( devices[1] );

        devices[2] = new DeviceData();
        populateDev3Occs( devices[2] );
    }

    /**
     * Populate cable occurrences under harness
     */
    private void populateCableOccs( CableData[] cable_occs )
    {
        //Simple cable info
        cable_occs[0] = new CableData();
        cable_occs[0].cable = new HarnessObjectInfo();
        cable_occs[0].cable.clientId = hrn_assy_occ_info.cb1_id;
        cable_occs[0].cable.object = hrn_struct.simple_cable_rev;
        addModObjToClientMap( cable_occs[0].cable.clientId, cable_occs[0].cable.object );

        //Complex cable info
        cable_occs[1] = new CableData();
        cable_occs[1].cable = new HarnessObjectInfo();
        cable_occs[1].cable.clientId = hrn_assy_occ_info.cb2_id;
        cable_occs[1].cable.object = hrn_struct.complex_cable_rev;
        addModObjToClientMap( cable_occs[1].cable.clientId, cable_occs[1].cable.object );
        cable_occs[1].connectors = new ConnectorData[1];
        ConnectorData ctr = new ConnectorData();
        ctr.connector = new HarnessObjectInfo();
        ctr.connector.clientId = hrn_assy_occ_info.cb2_ctr1_id;
        ctr.connector.object = hrn_struct.ctr_revs.get(0);
        cable_occs[1].connectors[0] = ctr;
        addModObjToClientMap(ctr.connector.clientId, ctr.connector.object);
        cable_occs[1].shields = new ShieldData[1];
        cable_occs[1].shields[0] = new ShieldData();
        cable_occs[1].shields[0].shield = new HarnessObjectInfo();
        cable_occs[1].shields[0].shield.clientId = hrn_assy_occ_info.cb2_sh_id;
        cable_occs[1].shields[0].shield.object = hrn_struct.simple_shield_rev;
        addModObjToClientMap( cable_occs[1].shields[0].shield.clientId,
                                cable_occs[1].shields[0].shield.object );
    }

    /**
     * Populate shield occurrences under harness
     */
    private  void populateShieldOccs( ShieldData[] shields )
    {
        //Simple shield info
        shields[0] = new ShieldData();
        shields[0].shield = new HarnessObjectInfo();
        shields[0].shield.clientId = hrn_assy_occ_info.sh1_id;
        shields[0].shield.object = hrn_struct.simple_shield_rev;
        addModObjToClientMap( shields[0].shield.clientId, shields[0].shield.object );

        //Complex cable info
        shields[1] = new ShieldData();
        shields[1].shield = new HarnessObjectInfo();
        shields[1].shield.clientId = hrn_assy_occ_info.sh2_id;
        shields[1].shield.object = hrn_struct.complex_shield_rev;
        addModObjToClientMap( shields[1].shield.clientId, shields[1].shield.object );
        shields[1].connectors = new ConnectorData[1];
        ConnectorData ctr = new ConnectorData();
        ctr.connector = new HarnessObjectInfo();
        ctr.connector.clientId = hrn_assy_occ_info.sh2_ctr1_id;
        ctr.connector.object = hrn_struct.ctr_revs.get(0);
        shields[1].connectors[0] = ctr;
        addModObjToClientMap( ctr.connector.clientId, ctr.connector.object);
        shields[1].cables = new CableData[1];
        shields[1].cables[0] = new CableData();
        shields[1].cables[0].cable = new HarnessObjectInfo();
        shields[1].cables[0].cable.clientId = hrn_assy_occ_info.sh2_cb_id;
        shields[1].cables[0].cable.object = hrn_struct.simple_cable_rev;
        addModObjToClientMap( shields[1].cables[0].cable.clientId, shields[1].cables[0].cable.object );
    }

    /**
     * Ppopulate the client id map with item revision (item element), occurrence, occurrence
     * thread, bomline
     * This client id map can be used for updating the structure with variants & options
     * or updating structure using bomlines
     */
    private void populateClientOccurrenceMap( HarnessObjectInfo[] occ_info ) throws Exception
    {
        try
        {
            HarnessOccBomMapInfo hrn_res = new HarnessOccBomMapInfo();
            HarnessOccBomMapInfo.HarnessStructureData hrn_occ_data =
                hrn_res.getHarnessStructure( (ItemRevision)hrn_struct.hrn_assy_rev, "ElectricalHarness" );
            this.hrnStructData = hrn_occ_data;

            for(int idx = 0; idx<occ_info.length; idx++)
            {
                String clientId = occ_info[idx].clientId;
                HarnessClientIDInfo.HRNObjInfo hrn_obj = hrn_assy_occ_info.clientMap.get(clientId);

                hrn_obj.occ = occ_info[idx].objectOcc;
                hrn_obj.occThd = occ_info[idx].objectOccThread;

                hrn_obj.bomline = hrn_occ_data.getBOMLine( hrn_obj.occ, hrn_obj.occThd );
                this.hrn_assy_occ_info.clientMap.put( clientId, hrn_obj );
            }
        }
        catch ( Exception ex )
        {
            LogFile.write( "populateClientOccurrenceMap : " + ex.getMessage() );
            throw ex;
        }
    }

    /**
     * This function populates the ctr1 and ctr2 bomlines
     * This will be used for allocations
     */
    public List<BOMLine> populateCtrLines()
    {
        int num_ctr_lines = 2;
        String[] ctr_clientIds = new String[num_ctr_lines];
        ctr_clientIds[0] = hrn_assy_occ_info.ctr1_id;
        ctr_clientIds[1] = hrn_assy_occ_info.ctr2_id;
        for(int idx = 0; idx<num_ctr_lines; idx++)
        {
            HarnessClientIDInfo.HRNObjInfo hrn_obj = hrn_assy_occ_info.clientMap.get(ctr_clientIds[idx]);
            ctr_lines.add(hrn_obj.bomline);
        }
        return ctr_lines;
    }

    /**
     * Associate Routes with Wires
     */
    private void associateRoutesWithWires() throws Exception
    {
        try
        {
            HarnessClientIDInfo.HRNObjInfo w1_obj = hrn_assy_occ_info.clientMap.get( hrn_assy_occ_info.w1_id );
            HarnessClientIDInfo.HRNObjInfo w2_obj = hrn_assy_occ_info.clientMap.get( hrn_assy_occ_info.w2_id );
            HarnessClientIDInfo.HRNObjInfo w3_obj = hrn_assy_occ_info.clientMap.get( hrn_assy_occ_info.w3_id );
            BOMLine w1Line = w1_obj.bomline;
            BOMLine w2Line = w2_obj.bomline;
            BOMLine w3Line = w3_obj.bomline;
            BOMLine topLine = this.hrnStructData.getHarnessTopLine();

            InStructureAssociationInfo[] inStructAssocInfoArr = new InStructureAssociationInfo[3];

            inStructAssocInfoArr[0] = new InStructureAssociationInfo();
            inStructAssocInfoArr[0].primaryBOMLine = w1Line;
            ModelObject[] rArr1 = new ModelObject[1];
            rArr1[0] = this.route_with_curve;
            inStructAssocInfoArr[0].secondaries = rArr1;
            inStructAssocInfoArr[0].contextBOMLine = topLine;
            inStructAssocInfoArr[0].associationType = ROUTED_BY;

            inStructAssocInfoArr[1] = new InStructureAssociationInfo();
            inStructAssocInfoArr[1].primaryBOMLine = w2Line;
            ModelObject[] rArr2 = new ModelObject[1];
            rArr2[0] = this.route_with_curve;
            inStructAssocInfoArr[1].secondaries = rArr2;
            inStructAssocInfoArr[1].contextBOMLine = topLine;
            inStructAssocInfoArr[1].associationType = ROUTED_BY;

            inStructAssocInfoArr[2] = new InStructureAssociationInfo();
            inStructAssocInfoArr[2].primaryBOMLine = w3Line;
            ModelObject[] rArr3 = new ModelObject[1];
            rArr3[0] = this.route_with_segment;
            inStructAssocInfoArr[2].secondaries = rArr3;
            inStructAssocInfoArr[2].contextBOMLine = topLine;
            inStructAssocInfoArr[2].associationType = ROUTED_BY;

            CreateInStructureAssociationResponse inStrucAssocResp =
                smService.createInStructureAssociations( inStructAssocInfoArr );
            if ( ! whTestUtils.handleServiceData( inStrucAssocResp.serviceData, "associateRoutesWithWires" ) )
            {
                return;
            }

            // Fix for PR#6086955
            BOMWindow[] bwArr = new BOMWindow[1];
            bwArr[0] = this.getHarnessStructureData().getHarnessStructureWindow();
            SaveBOMWindowsResponse saveResp =  smServiceCad.saveBOMWindows( bwArr );
            if ( ! whTestUtils.handleServiceData( saveResp.serviceData, "associateRoutesWithWires" ) )
            {
                return;
            }

            LogFile.write( "associateRoutesWithWires : Associated Route - Route0001 with : Wire0001, Wire0002 Successfully" );
            LogFile.write( "associateRoutesWithWires : Associated Route - Route0002 with : Wire0003 Successfully" );
        }
        catch( Exception ex )
        {
            LogFile.write( "associateRoutesWithWires : " + ex.getMessage() );
            // Re-throw the exception
            throw ex;
        }
    }

    /**
     * Returns all the harness objects
     * @return
     */
    public HarnessObjects getHarnessObjects()
    {
        return hrn_struct;
    }

    /**
     * Returns the Client Id to HRNObjInfo Map
     * @return
     */
    public Map<String, HRNObjInfo> getClientOccurrenceMap()
    {
        return hrn_assy_occ_info.clientMap;
    }

    /**
     * Returns the HarnessClientIDInfo object
     * @return
     */
    public HarnessClientIDInfo getHarnessClientIDInfo()
    {
        return hrn_assy_occ_info;
    }

    /**
     * Returns HarnessStructureData object
     * @return
     */
    public HarnessStructureData getHarnessStructureData()
    {
        return hrnStructData;
    }

    /**
     * Returns route with segment
     * @return
     */
    public ModelObject getRouteWithSegment()
    {
        return route_with_segment;
    }

    /**
     * Returns route with Curve
     * @return
     */
    public ModelObject getRouteWithCurve()
    {
        return route_with_curve;
    }

    /**
     * Create the max complexity harness structure
     */
    private void createMaxComplexityHarnessStructure() throws Exception
    {
        try
        {
            hrn_assy_occ_info = new HarnessClientIDInfo();
            HarnessObjectInfo harness_occ_info = new HarnessObjectInfo();
            harness_occ_info.clientId = hrn_assy_occ_info.harness_id;
            harness_occ_info.object = hrn_struct.hrn_rev;
            addModObjToClientMap( harness_occ_info.clientId, hrn_struct.hrn_rev );

            HarnessData hrnData = new HarnessData();
            HarnessData[] hrnDataArr = new HarnessData[1];

            hrnData.devices = new DeviceData[3];
            populateDeviceOccs( hrnData.devices );

            hrnData.connectors = new ConnectorData[3];
            populateCtrOccs( hrnData.connectors );

            hrnData.connections = new ConnectionData[3];
            populateConnOccs( hrnData.connections );

            hrnData.wires = new WireInfo[3];
            populateWireOccs(hrnData.wires);

            hrnData.cables = new CableData[2];
            populateCableOccs(hrnData.cables);

            hrnData.shields = new ShieldData[2];
            populateShieldOccs(hrnData.shields);

            hrnData.harness = harness_occ_info;

            hrnDataArr[0] = hrnData;

            // Now create the harness info for the SOA
            HarnessInfo[] hrnInfoArr = new HarnessInfo[1];
            HarnessInfo hrnInfo = new HarnessInfo();
            hrnInfo.clientId = hrn_assy_occ_info.topllevel_assy_id;
            hrnInfo.harnessData = hrnDataArr;
            hrnInfo.parent = hrn_struct.hrn_assy_rev;
            addModObjToClientMap( hrnInfo.clientId, hrnInfo.parent );

            hrnInfoArr[0] = hrnInfo;
            CreateOrUpdateHarnessResponse harnResp =
                    whService.createOrUpdateHarness( hrnInfoArr, "ElectricalHarness", false );
            if ( ! whTestUtils.handleServiceData( harnResp.serviceData, "createMaxComplexityHarnessStructure" ) )
            {
                return;
            }
            LogFile.write( "Max Complexity Harness Structure created successfully" );
            populateClientOccurrenceMap( harnResp.output );
        }
        catch ( Exception ex )
        {
            LogFile.write( "createMaxComplexityHarnessStructure : " + ex.getMessage() );
            throw ex;
        }
    }
}
