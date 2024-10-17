//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.wireharness;

import com.teamcenter.services.strong.wireharness._2008_06.WireHarness.RouteInfo;
import com.teamcenter.soa.client.model.ModelObject;

public class CreateOrUpdateRouteObjects
{
    private ModelObject context = null;
    private RouteTestUtils r_utils = null;

    /**
     * Constructor
     */
    public CreateOrUpdateRouteObjects( ModelObject context )
    {
        this.context = context;
        r_utils = new RouteTestUtils();
    }

    /**
     *  This method creates of Route object with
     *  route nodes and a route curve
     **/
    public ModelObject createRouteUsingCurve()
    {
        RouteInfo[] r_info = new RouteInfo[1];
        ModelObject[] routes = r_utils.createRoutesWithAllDataUsingCurve( r_info, this.context );
        return routes[0];
    }

    /**
     *  This method creates of Route object with
     *  route nodes and 2 route segments
     **/
     public ModelObject createRouteDataUsingSegments()
     {
          RouteInfo[] r_info = new RouteInfo[1];
          ModelObject[] routes = r_utils.createRoutesWithAllDataUsingSegments( r_info, this.context );
          return routes[0];
     }
}
