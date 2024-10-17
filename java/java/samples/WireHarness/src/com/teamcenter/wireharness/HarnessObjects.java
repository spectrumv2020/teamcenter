//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.wireharness;

import java.util.ArrayList;
import java.util.List;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.client.model.strong.ItemRevision;

public class HarnessObjects {

    public ModelObject hrn_assy_rev = null;
    public ModelObject hrn_rev = null;
    public List <ItemRevision> ctr_revs;
    public List <ItemRevision> device_revs;
    public List <ItemRevision> wire_revs;
    public ItemRevision simple_cable_rev;
    public ItemRevision complex_cable_rev;
    public ItemRevision simple_shield_rev;
    public ItemRevision complex_shield_rev;
    public List<ModelObject> connection_revs;
    public List <ModelObject> ctr1_item_elems;
    public List <ModelObject> ctr2_item_elems;
    public List <ModelObject> ctr3_item_elems;
    public List <ModelObject> ctr4_item_elems;
    public List <ModelObject> ctr5_item_elems;
    public List <ModelObject> ctr6_item_elems;
    public List <ModelObject> ctr7_item_elems;
    public List <ModelObject> ctr8_item_elems;

    /**
     * Constructor
     */
    public HarnessObjects ()
    {
        ctr_revs = new ArrayList<ItemRevision>();
        device_revs = new ArrayList<ItemRevision>();
        wire_revs = new ArrayList<ItemRevision>();
        connection_revs = new ArrayList<ModelObject>();
        ctr1_item_elems = new ArrayList<ModelObject>();
        ctr2_item_elems = new ArrayList<ModelObject>();
        ctr3_item_elems = new ArrayList<ModelObject>();

        ctr4_item_elems = new ArrayList<ModelObject>();
        ctr5_item_elems = new ArrayList<ModelObject>();
        ctr6_item_elems = new ArrayList<ModelObject>();
        ctr7_item_elems = new ArrayList<ModelObject>();
        ctr8_item_elems = new ArrayList<ModelObject>();
    }
}
