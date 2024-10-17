//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.wireharness;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.client.model.strong.BOMLine;

public class HarnessClientIDInfo
{
    // Toplevel harness item
    public String topllevel_assy_id = "TOPLEVEL_ASSY1";

    // Harness id
    public String harness_id = "HARNESS1";

    // device1 information
    public String device1_id = "DEVICE1";
    public String d1_ctr1_id = "DEVICE1_CONNECTOR1";
    public String d1_ctr1_trma_id = "DEVICE1_CONNECTOR1_TRMA";
    public String d1_ctr1_trmb_id = "DEVICE1_CONNECTOR1_TRMB";

    public String d1_ctr4_id ="DEVICE1_CONNECTOR4";
    public String d1_ctr4_trm_t1_id = "DEVICE1_CONNECTOR4_TRM_T1";
    public String d1_ctr4_trm_t2_id = "DEVICE1_CONNECTOR4_TRM_T2";

    // device2 information
    public String device2_id = "DEVICE2";
    public String d2_ctr2_id = "DEVICE2_CONNECTOR2";
    public String d2_ctr2_trm_pin1_id = "DEVICE2_CONNECTOR2_TRM_PIN1";
    public String d2_ctr2_trm_pin2_id = "DEVICE2_CONNECTOR2_TRM_PIN2";

    public String d2_ctr5_id = "DEVICE2_CONNECTOR5";
    public String d2_ctr5_trm_t1_id = "DEVICE2_CONNECTOR5_TRM_T1";
    public String d2_ctr5_trm_t2_id = "DEVICE2_CONNECTOR5_TRM_T2";

    // device3 information
    public String device3_id = "DEVICE3_ID";
    public String d3_ctr3_id = "DEVICE3_CONNECTOR3_ID";
    public String d3_ctr3_trm_p1_id = "DEVICE3_CONNECTOR3_TRM_P1";
    public String d3_ctr3_trm_p2_id = "DEVICE3_CONNECTOR3_TRM_P2";

    public String d3_ctr6_id = "DEVICE3_CONNECTOR6_ID";
    public String d3_ctr6_trm_t1_id = "DEVICE3_CONNECTOR6_TRM_T1";
    public String d3_ctr6_trm_t2_id = "DEVICE3_CONNECTOR6_TRM_T2";

    // device4 information
    public String device4_id = "DEVICE4_ID";
    public String d4_ctr4_id = "DEVICE4_CONNECTOR4_ID";
    public String d4_ctr4_trm_p1_id = "DEVICE4_CONNECTOR4_TRM_P1";
    public String d4_ctr4_trm_p2_id = "DEVICE4_CONNECTOR4_TRM_P2";

    public String d4_ctr8_id = "DEVICE4_CONNECTOR8_ID";
    public String d4_ctr8_trm_t1_id = "DEVICE4_CONNECTOR8_TRM_T1";
    public String d4_ctr8_trm_t2_id = "DEVICE4_CONNECTOR8_TRM_T2";

    // connector1 information
    public String ctr1_id = "CONNECTOR1";
    public String ctr1_trma_id = "CONNECTOR1_TRMA";
    public String ctr1_trmb_id = "CONNECTOR1_TRMB";

    // connector2 information
    public String ctr2_id = "CONNECTOR2";
    public String ctr2_trm_pin1_id = "CONNECTOR2_TRM_PIN1";
    public String ctr2_trm_pin2_id = "CONNECTOR2_TRM_PIN2";

    // connector3 information
    public String ctr3_id = "CONNECTOR3";
    public String ctr3_trm_p1_id = "CONNECTOR3_TRM_P1";
    public String ctr3_trm_p2_id = "CONNECTOR3_TRM_P2";

    // connector4 information
    public String ctr4_id = "CONNECTOR4";
    public String ctr4_trm_p1_id = "CONNECTOR4_TRM_PA";
    public String ctr4_trm_p2_id = "CONNECTOR4_TRM_PB";

    // connector5 information
    public String ctr5_id  = "CONNECTOR5";
    public String ctr5_trm_t1_id  = "CONNECTOR5_TRM_T1";
    public String ctr5_trm_t2_id  = "CONNECTOR5_TRM_T2";

    // connector6 information
    public String ctr6_id = "CONNECTOR6";
    public String ctr6_trm_t1_id  = "CONNECTOR6_TRM_T1";
    public String ctr6_trm_t2_id  = "CONNECTOR6_TRM_T2";

    // connector7 information
    public String ctr7_id = "CONNECTOR7";
    public String ctr7_trm_t1_id  = "CONNECTOR7_TRM_T1";
    public String ctr7_trm_t2_id  = "CONNECTOR7_TRM_T2";

    // connector8 information
    public String ctr8_id = "CONNECTOR8";
    public String ctr8_trm_t1_id  = "CONNECTOR8_TRM_T1";
    public String ctr8_trm_t2_id  = "CONNECTOR8_TRM_T2";

    // connection information
    public String conn1_id = "CONNECTION1";
    public String conn2_id = "CONNECTION2";
    public String conn3_id = "CONNECTION3";
    public String conn4_id = "CONNECTION4";

    // Wire information
    public String w1_id = "WIRE1";
    public String w2_id = "WIRE2";
    public String w3_id = "WIRE3";
    public String w4_id = "WIRE4";

    //Cable1 information
    public String cb1_id = "SIMPLE_CABLE1";

    //Cable2 information
    public String cb2_id = "COMPLEX_CABLE1";
    public String cb2_ctr1_id = "COMPLEX_CABLE1_CONNECTOR1";
    public String cb2_ctr1_trma_id = "COMPLEX_CABLE1_CONNECTOR1_TRMA";
    public String cb2_ctr1_trmb_id = "COMPLEX_CABLE1_CONNECTOR1_TRMB";
    public String cb2_sh_id = "COMPLEX_CABLE1_SHIELD";

    // Shield1 information
    public String sh1_id = "SIMPLE_SHIELD1";

    // Shield2 information
    public String sh2_id = "COMPLEX_SHIELD1";
    public String sh2_ctr1_id = "COMPLEX_SHIELD1_CONNECTOR1";
    public String sh2_ctr1_trma_id = "COMPLEX_SHIELD1_CONNECTOR1_TRMA";
    public String sh2_ctr1_trmb_id = "COMPLEX_SHIELD1_CONNECTOR1_TRMB";
    public String sh2_cb_id = "COMPLEX_SHIELD1_CABLE";

    public List <String> clientIds = null;
    // ClientId - HRNObjInfo
    public Map<String, HRNObjInfo> clientMap;

    /**
     * Constructor
     */
    public HarnessClientIDInfo()
    {
        clientIds = new ArrayList<String>();
        populateClientIds();
        clientMap = new HashMap<String, HRNObjInfo>();
        int cnt = clientIds.size();
        for (int idx = 0; idx<cnt; idx++)
        {
            HRNObjInfo hrnobj = new HRNObjInfo();
            clientMap.put(clientIds.get(idx), hrnobj);
        }
    }

    /**
     * Populate the Client Ids
     */
    public void populateClientIds()
    {
        clientIds.add( topllevel_assy_id );
        clientIds.add( harness_id );
        clientIds.add( device1_id );
        clientIds.add( d1_ctr1_id );
        clientIds.add( d1_ctr1_trma_id );
        clientIds.add( d1_ctr1_trmb_id );

        clientIds.add( d1_ctr4_id );
        clientIds.add( d1_ctr4_trm_t1_id );
        clientIds.add( d1_ctr4_trm_t2_id );

        clientIds.add( device2_id );
        clientIds.add( d2_ctr2_id );
        clientIds.add( d2_ctr2_trm_pin1_id );
        clientIds.add( d2_ctr2_trm_pin2_id );

        clientIds.add( d2_ctr5_id) ;
        clientIds.add( d2_ctr5_trm_t1_id) ;
        clientIds.add( d2_ctr5_trm_t2_id) ;

        clientIds.add( device3_id );
        clientIds.add( d3_ctr3_id );
        clientIds.add( d3_ctr3_trm_p1_id );
        clientIds.add( d3_ctr3_trm_p2_id );

        clientIds.add( d3_ctr6_id );
        clientIds.add( d3_ctr6_trm_t1_id );
        clientIds.add( d3_ctr6_trm_t2_id );

        clientIds.add( device4_id );
        clientIds.add( d4_ctr4_id );
        clientIds.add( d4_ctr4_trm_p1_id );
        clientIds.add( d4_ctr4_trm_p2_id );

        clientIds.add( d4_ctr8_id );
        clientIds.add( d4_ctr8_trm_t1_id );
        clientIds.add( d4_ctr8_trm_t2_id );

        clientIds.add( ctr1_id );
        clientIds.add( ctr1_trma_id );
        clientIds.add( ctr1_trmb_id );
        clientIds.add( ctr2_id );
        clientIds.add( ctr2_trm_pin1_id );
        clientIds.add( ctr2_trm_pin2_id );
        clientIds.add( ctr3_id );
        clientIds.add( ctr3_trm_p1_id );
        clientIds.add( ctr3_trm_p2_id );
        clientIds.add( ctr4_id );
        clientIds.add( ctr4_trm_p1_id );
        clientIds.add( ctr4_trm_p2_id );

        clientIds.add( ctr5_id) ;
        clientIds.add( ctr5_trm_t1_id) ;
        clientIds.add( ctr5_trm_t2_id) ;
        clientIds.add( ctr6_id) ;
        clientIds.add( ctr6_trm_t1_id) ;
        clientIds.add( ctr6_trm_t2_id) ;

        clientIds.add( ctr7_id) ;
        clientIds.add( ctr7_trm_t1_id) ;
        clientIds.add( ctr7_trm_t2_id) ;

        clientIds.add( ctr8_id ) ;
        clientIds.add( ctr8_trm_t1_id ) ;
        clientIds.add( ctr8_trm_t2_id ) ;

        clientIds.add( conn1_id );
        clientIds.add( conn2_id );
        clientIds.add( conn3_id );
        clientIds.add( conn4_id );
        clientIds.add( w1_id );
        clientIds.add( w2_id );
        clientIds.add( w3_id );
        clientIds.add( w4_id );
        clientIds.add( cb1_id );
        clientIds.add( cb2_id );
        clientIds.add( cb2_ctr1_id );
        clientIds.add( cb2_ctr1_trma_id );
        clientIds.add( cb2_ctr1_trmb_id );
        clientIds.add( cb2_sh_id );
        clientIds.add( sh1_id );
        clientIds.add( sh2_id );
        clientIds.add( sh2_ctr1_id );
        clientIds.add( sh2_ctr1_trma_id );
        clientIds.add( sh2_ctr1_trmb_id );
        clientIds.add( sh2_cb_id );
    }

    public class HRNObjInfo
    {
        // This can be item revision or item element
        public ModelObject obj = null;  // item revision or item element
        public ModelObject occ = null;  // Occurrence
        public ModelObject occThd = null; // Occurrence Thread
        public BOMLine bomline = null;  // BOMLine or GDELine
    }
}
