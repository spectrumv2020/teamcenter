//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================


package com.teamcenter.wireharness;

import java.util.Vector;

import com.teamcenter.clientx.WHSession;
import com.teamcenter.services.strong.wireharness._2008_06.WireHarness.CreateOrUpdateRouteObjectsResponse;
import com.teamcenter.services.strong.wireharness._2008_06.WireHarness.CurveData;
import com.teamcenter.services.strong.wireharness._2008_06.WireHarness.DisplayData;
import com.teamcenter.services.strong.wireharness._2008_06.WireHarness.NodeData;
import com.teamcenter.services.strong.wireharness._2008_06.WireHarness.RouteData;
import com.teamcenter.services.strong.wireharness._2008_06.WireHarness.RouteInfo;
import com.teamcenter.services.strong.wireharness._2008_06.WireHarness.SegmentData;
import com.teamcenter.soa.client.Connection;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.client.model.strong.ItemRevision;
import com.teamcenter.soa.client.model.strong.PSBOMViewRevision;

public class RouteTestUtils
{
    private com.teamcenter.services.strong.wireharness.WireHarnessService whService = null;
    private WireHarnessTestUtils whTestUtils = null;
    private int nodesCount = 0;
    private int curvesCount = 0;
    private int segmentsCount = 0;
    private int routesCount = 0;

    /**
     * Constructor
     */
    public RouteTestUtils()
    {
        Connection connection = WHSession.getConnection();
        whService = com.teamcenter.services.strong.wireharness.WireHarnessService.getService(connection);
        whTestUtils = new WireHarnessTestUtils();
    }

    /**
     * Creates a simple structure and sets the topline and returns PSBVR
     * of topline as context
     */
    public ModelObject setContext(ItemRevision rev)
    {
        ModelObject context = null;

        try
        {
            // Get the bvr of the toplevel item revision
            PSBOMViewRevision[] bvrs = rev.get_structure_revisions();
            context = bvrs[0];
            if(context == null)
            {
                throw new Exception("Context is null. Item revision has no bvr.");
            }
        }
        catch ( Exception ex )
        {
            LogFile.write(ex.getMessage());
        }
        return context;

    }

    /**
     * Function to create route nodes. Number is equal to length of RouteInfo array.
     * In context is specified, it will be used. If null, creates a context
     */
    public ModelObject[] createRouteNodes(RouteInfo[] r_info, ModelObject context)
    {
        int len = r_info.length;
        ModelObject[] nodes = new ModelObject[len];
        try
        {
            for(int idx = 0; idx<len; idx++)
            {

                String x_cord = new String("0.0");
                x_cord += idx;
                r_info[idx].nodeData.nodeAttributes.put("x", x_cord);
                r_info[idx].nodeData.nodeAttributes.put("y", "0.0");
                r_info[idx].nodeData.nodeAttributes.put("z", "0.0");
                r_info[idx].nodeData.nodeAttributes.put("position", x_cord);
                r_info[idx].context = context;
            }

            CreateOrUpdateRouteObjectsResponse resp = whService.createOrUpdateRouteObjects(r_info);
            if ( ! whTestUtils.handleServiceData( resp.serviceData, "" ) )
            {
                return null;
            }

            for(int idx = 0; idx<nodes.length; idx++)
            {
                if(resp.output[idx].node.clientId.equals(r_info[idx].nodeData.clientId))
                {
                    nodes[idx] = resp.output[idx].node.object;
                    LogFile.write("Route node with clientId: " + r_info[idx].nodeData.clientId+" name : " + r_info[idx].nodeData.name+  " created successfully.");

                }
                else
                {
                    throw new Exception("Route node " + r_info[idx].nodeData.clientId + " creation failed.\n");
                }
            }
        }
        catch ( Exception ex )
        {
            LogFile.write(ex.getMessage() + "\n");
            LogFile.write("SOA Service: createOrUpdateRouteObjects Failed." + "\n");
        }
        return nodes;
    }


    /**
     * Function to create route curves. Number is equal to length of RouteInfo array.
     * In context is specified, it will be used. If null, creates a context
     */
    public ModelObject[] createRouteCurves(RouteInfo[] r_info, ModelObject context)
    {
        int len = r_info.length;
        ModelObject[] curves = new ModelObject[len];
        try
        {
            for(int idx = 0; idx<len; idx++)
            {
                // route curve info
                r_info[idx].context = context;
            }

            CreateOrUpdateRouteObjectsResponse resp = whService.createOrUpdateRouteObjects(r_info);
            if ( ! whTestUtils.handleServiceData( resp.serviceData, "" ) )
            {
                return null;
            }

            for (int idx = 0; idx<len; idx++)
            {
                if(resp.output[idx].curve.clientId.equals(r_info[idx].curveData.clientId))
                {
                    curves[idx] = resp.output[idx].curve.object;
                    LogFile.write("Route curve with clientId: " + r_info[idx].curveData.clientId +" name : " +r_info[idx].curveData.name +" created successfully.");
                }
                else
                {
                    throw new Exception ("Route curve " + r_info[idx].curveData.clientId + " creation failed.\n");
                }
            }
        }
        catch ( Exception ex )
        {
            LogFile.write(ex.getMessage() + "\n");
            LogFile.write("SOA Service: createOrUpdateRouteObjects Failed." + "\n");
        }
        return curves;
    }

    /**
     * Function to create route segments. Number is equal to length of RouteInfo array.
     * In context is specified, it will be used. If null, creates a context
     */
    public ModelObject[] createRouteSegments(RouteInfo[] r_info, ModelObject context)
    {
        int len = r_info.length;
        ModelObject[] segments = new ModelObject[len];
        try
        {
            for(int idx = 0; idx<len; idx++)
            {
                // Route segment info
                r_info[idx].context = context;
            }
            CreateOrUpdateRouteObjectsResponse resp = whService.createOrUpdateRouteObjects(r_info);
            if ( ! whTestUtils.handleServiceData( resp.serviceData, "" ) )
            {
                return null;
            }

            for(int idx = 0;idx<segments.length; idx++)
            {
                if(resp.output[idx].segment.segment.clientId.equals(r_info[idx].segmentData.clientId))
                {
                    segments[idx] = resp.output[idx].segment.segment.object;
                    LogFile.write("Route segment with clientId" + r_info[idx].segmentData.clientId + "name : "+ r_info[idx].segmentData.name + " created successfully.");
                }
                else
                {
                    throw new Exception ("Route segment " + r_info[idx].segmentData.clientId + " creation failed.\n");
                }
            }
        }
        catch ( Exception ex )
        {
            LogFile.write(ex.getMessage() + "\n");
            LogFile.write("SOA Service: createOrUpdateRouteObjects Failed." + "\n");
        }
        return segments;
    }

    /**
     * Function to create routes. Number is equal to length of RouteInfo array.
     * In context is specified, it will be used. If null, creates a context
     * This function creates route objects with start node, endnode and a route curve
     * start node, endnode and route curve will be created.
     */
    public ModelObject[] createRoutesWithAllDataUsingCurve(RouteInfo[] r_info, ModelObject context)
    {
        int len = r_info.length;
        ModelObject[] routes = new ModelObject[len];

        Vector<String> routeDetails = new Vector<String>();

        try
        {
            for(int idx = 0; idx<len; idx++)
            {
                String routeDetail = "";
                // Route info
                routesCount++;
                r_info[idx] = new RouteInfo();
                r_info[idx].routeData = new RouteData();
                r_info[idx].routeData.clientId =  "Route000" +  routesCount;
                r_info[idx].routeData.name = "Route000" +  routesCount;
                r_info[idx].context = context;

                /* route curve info */
                r_info[idx].routeData.curveData = new CurveData();
                RouteInfo[] curve_info = new RouteInfo[1];
                ModelObject[] curves = new ModelObject[1];


                curvesCount++;
                curve_info[0] = new RouteInfo();
                curve_info[0].curveData = new CurveData();
                curve_info[0].curveData.clientId = "Curve000"+ curvesCount;
                curve_info[0].curveData.name = "Curve000"+ curvesCount;

                curves = this.createRouteCurves(curve_info, context);
                r_info[idx].routeData.curveData.curve = curves[0];

                /* Route nodes info */
                r_info[idx].routeData.nodeData = new NodeData[2];
                RouteInfo[] node_info = new RouteInfo[2];
                ModelObject[] nodes = new ModelObject[2];

                nodesCount++;
                node_info[0] = new RouteInfo();
                node_info[0].nodeData = new NodeData();
                node_info[0].nodeData.clientId = "Node000"+nodesCount;
                node_info[0].nodeData.name = "Node000"+nodesCount;

                nodesCount++;
                node_info[1] = new RouteInfo();
                node_info[1].nodeData = new NodeData();
                node_info[1].nodeData.clientId = "Node000"+nodesCount;
                node_info[1].nodeData.name = "Node000"+nodesCount;

                nodes = this.createRouteNodes(node_info, context);
                r_info[idx].routeData.nodeData[0] = new NodeData();
                r_info[idx].routeData.nodeData[0].node = nodes[0];
                r_info[idx].routeData.nodeData[1] = new NodeData();
                r_info[idx].routeData.nodeData[1].node = nodes[1];

                for( int i = 0;i<r_info[idx].routeData.nodeData.length; i++)
                {
                    routeDetail = routeDetail+r_info[idx].routeData.clientId +" has node with name: " + node_info[i].nodeData.clientId +"\n" ;
                }

                for( int i = 0;i<curve_info.length; i++)
                {
                    routeDetail = routeDetail+r_info[idx].routeData.clientId + " has curve with name: " + curve_info[i].curveData.clientId +"\n";
                }
                routeDetails.add(routeDetail);


            }
            CreateOrUpdateRouteObjectsResponse resp = whService.createOrUpdateRouteObjects(r_info);
            if ( ! whTestUtils.handleServiceData( resp.serviceData, "" ) )
            {
                return null;
            }

            for(int idx = 0;idx<routes.length; idx++)
            {
                if(resp.output[idx].route.route.clientId.equals(r_info[idx].routeData.clientId))
                {
                    routes[idx] = resp.output[idx].route.route.object;
                    LogFile.write("Route with clientId: " + r_info[idx].routeData.clientId + " name : "+r_info[idx].routeData.name + " created successfully.");
                    LogFile.write(routeDetails.elementAt(idx));
                }
                else
                {
                    throw new Exception ("Route " + r_info[idx].routeData.clientId + " creation failed.\n");
                }
            }
        }
        catch ( Exception ex )
        {
            LogFile.write(ex.getMessage() + "\n");
            LogFile.write("SOA Service: createOrUpdateRouteObjects Failed." + "\n");
        }
        return routes;
    }

    /**
     * Function to create routes. Number is equal to length of RouteInfo array.
     * In context is specified, it will be used. If null, creates a context
     * This function creates route objects with start node, endnode and a
     * route curve and 2 route segments.
     * start node, endnode, route segments and route curve will be created.
     */
    public ModelObject[] createRoutesWithAllDataUsingCurveAndSegments(RouteInfo[] r_info, ModelObject context)
    {
        int len = r_info.length;
        ModelObject[] routes = new ModelObject[len];
        Vector<String> routeDetails = new Vector<String> ();
        try
        {
            for(int idx = 0; idx<len; idx++)
            {
                String routeDetail = "";
                // Route info
                r_info[idx] = new RouteInfo();
                r_info[idx].routeData = new RouteData();
                routesCount++;
                r_info[idx].routeData.clientId = "Route000" + routesCount;
                r_info[idx].routeData.name = "Route000" + routesCount;
                r_info[idx].context = context;

                /* route curve info */
                r_info[idx].routeData.curveData = new CurveData();
                RouteInfo[] curve_info = new RouteInfo[1];
                ModelObject[] curves = new ModelObject[1];

                curvesCount++;
                curve_info[0] = new RouteInfo();
                curve_info[0].curveData = new CurveData();
                curve_info[0].curveData.clientId = "Curve000"+ curvesCount;
                curve_info[0].curveData.name = "Curve000"+ curvesCount;

                curves = this.createRouteCurves(curve_info, context);
                r_info[idx].routeData.curveData.curve = curves[0];

                /* route segments info */
                r_info[idx].routeData.segmentData = new SegmentData[2];
                RouteInfo[] seg_info = new RouteInfo[2];
                ModelObject[] segments = new ModelObject[2];

                segmentsCount++;
                seg_info[0] = new RouteInfo();
                seg_info[0].segmentData = new SegmentData();
                seg_info[0].segmentData.clientId = "Segment000"+segmentsCount;
                seg_info[0].segmentData.name = "Segment000"+segmentsCount;

                segmentsCount++;
                seg_info[1] = new RouteInfo();
                seg_info[1].segmentData = new SegmentData();
                seg_info[1].segmentData.clientId = "Segment000"+segmentsCount;
                seg_info[1].segmentData.name = "Segment000"+segmentsCount;


                segments = this.createRouteSegmentsWithAllData(seg_info, context);
                r_info[idx].routeData.segmentData[0] = new SegmentData();
                r_info[idx].routeData.segmentData[0].segment = segments[0];
                r_info[idx].routeData.segmentData[1] = new SegmentData();
                r_info[idx].routeData.segmentData[1].segment = segments[1];


                /* Route nodes info */
                r_info[idx].routeData.nodeData = new NodeData[2];
                RouteInfo[] node_info = new RouteInfo[2];
                ModelObject[] nodes = new ModelObject[2];

                nodesCount++;
                node_info[0] = new RouteInfo();
                node_info[0].nodeData = new NodeData();
                node_info[0].nodeData.clientId = "Node000"+nodesCount;
                node_info[0].nodeData.name = "Node000"+nodesCount;

                nodesCount++;
                node_info[1] = new RouteInfo();
                node_info[1].nodeData = new NodeData();
                node_info[1].nodeData.clientId = "Node000"+nodesCount;
                node_info[1].nodeData.name = "Node000"+nodesCount;


                nodes = this.createRouteNodes(node_info, context);
                r_info[idx].routeData.nodeData[0] = new NodeData();
                r_info[idx].routeData.nodeData[0].node = nodes[0];
                r_info[idx].routeData.nodeData[1] = new NodeData();
                r_info[idx].routeData.nodeData[1].node = nodes[1];

                for( int i = 0;i<r_info[idx].routeData.nodeData.length; i++)
                {
                    routeDetail = routeDetail+r_info[idx].routeData.clientId +" has node with name: " + node_info[i].nodeData.name +"\n" ;
                }

                for( int i = 0;i<curve_info.length; i++)
                {
                    routeDetail = routeDetail+r_info[idx].routeData.clientId + " has curve with name: " + curve_info[i].curveData.name +"\n";
                }
                for( int i = 0;i<seg_info.length; i++)
                {
                    routeDetail = routeDetail+r_info[idx].routeData.clientId + " has segment with name: " + seg_info[i].segmentData.name +"\n";
                }
                routeDetails.add(routeDetail);


                /*
                 * Route display information
                 */
                r_info[idx].routeData.displayData = new DisplayData();
                r_info[idx].routeData.displayData.font = 23;
                r_info[idx].routeData.displayData.rgbValues = new double[3];
                r_info[idx].routeData.displayData.rgbValues[0] = 16.0;
                r_info[idx].routeData.displayData.rgbValues[1] = 17.0;
                r_info[idx].routeData.displayData.rgbValues[2] = 18.0;
                r_info[idx].routeData.displayData.width = 23.0;
            }
            CreateOrUpdateRouteObjectsResponse resp = whService.createOrUpdateRouteObjects(r_info);
            if ( ! whTestUtils.handleServiceData( resp.serviceData, "" ) )
            {
                return null;
            }

            for(int idx = 0;idx<routes.length; idx++)
            {
                if(resp.output[idx].route.route.clientId.equals(r_info[idx].routeData.clientId))
                {
                    routes[idx] = resp.output[idx].route.route.object;
                    LogFile.write("Route " + r_info[idx].routeData.clientId + " created successfully.\n");
                    LogFile.write(routeDetails.elementAt(idx));
                }
                else
                {
                    throw new Exception ( "Route " + r_info[idx].routeData.clientId + " creation failed.\n" );
                }
            }
        }
        catch ( Exception ex )
        {
            LogFile.write( ex.getMessage() + "\n" );
            LogFile.write( "SOA Service: createOrUpdateRouteObjects Failed." + "\n" );
        }
        return routes;
    }

    /**
     * Function to create routes. Number is equal to length of RouteInfo array.
     * In context is specified, it will be used. If null, creates a context
     * This function creates route objects with start node, endnode and a
     * and 2 route segments.
     * start node, endnode and route segments will be created.
     */
    public ModelObject[] createRoutesWithAllDataUsingSegments(RouteInfo[] r_info, ModelObject context)
    {
        int len = r_info.length;
        ModelObject[] routes = new ModelObject[len];
        Vector <String> routeDetails = new Vector<String>();
        try
        {

            for(int idx = 0; idx<len; idx++)
            {
                String routeDetail = "";
                // Route info
                r_info[idx] = new RouteInfo();
                r_info[idx].routeData = new RouteData();

                routesCount++;
                r_info[idx].routeData.clientId ="Route000"+ routesCount;
                r_info[idx].routeData.name = "Route000"+ routesCount;
                r_info[idx].context = context;

                /* route segments info */
                r_info[idx].routeData.segmentData = new SegmentData[2];
                RouteInfo[] seg_info = new RouteInfo[2];
                ModelObject[] segments = new ModelObject[2];

                segmentsCount++;
                seg_info[0] = new RouteInfo();
                seg_info[0].segmentData = new SegmentData();
                seg_info[0].segmentData.clientId = "Segment000"+ segmentsCount;
                seg_info[0].segmentData.name = "Segment000"+ segmentsCount;

                segmentsCount++;
                seg_info[1] = new RouteInfo();
                seg_info[1].segmentData = new SegmentData();
                seg_info[1].segmentData.clientId = "Segment000"+ segmentsCount;
                seg_info[1].segmentData.name = "Segment000"+ segmentsCount;

                segments = this.createRouteSegmentsWithAllData(seg_info, context);
                r_info[idx].routeData.segmentData[0] = new SegmentData();
                r_info[idx].routeData.segmentData[0].segment = segments[0];
                r_info[idx].routeData.segmentData[1] = new SegmentData();
                r_info[idx].routeData.segmentData[1].segment = segments[1];

                /* Route nodes info */
                r_info[idx].routeData.nodeData = new NodeData[2];
                RouteInfo[] node_info = new RouteInfo[2];
                ModelObject[] nodes = new ModelObject[2];

                nodesCount++;
                node_info[0] = new RouteInfo();
                node_info[0].nodeData = new NodeData();
                node_info[0].nodeData.clientId =  "Node000"+ nodesCount;
                node_info[0].nodeData.name =  "Node000"+ nodesCount;

                nodesCount++;
                node_info[1] = new RouteInfo();
                node_info[1].nodeData = new NodeData();
                node_info[1].nodeData.clientId =  "Node000"+ nodesCount;
                node_info[1].nodeData.name =  "Node000"+ nodesCount;


                nodes = this.createRouteNodes(node_info, context);
                r_info[idx].routeData.nodeData[0] = new NodeData();
                r_info[idx].routeData.nodeData[0].node = nodes[0];
                r_info[idx].routeData.nodeData[1] = new NodeData();
                r_info[idx].routeData.nodeData[1].node = nodes[1];

                for( int i = 0;i<r_info[idx].routeData.nodeData.length; i++)
                {
                    routeDetail = routeDetail+r_info[idx].routeData.clientId +" has node with name: " + node_info[i].nodeData.name +"\n" ;
                }

                for( int i = 0;i<seg_info.length; i++)
                {
                    routeDetail = routeDetail+r_info[idx].routeData.clientId + " has segment with name: " + seg_info[i].segmentData.name +"\n";
                }
                routeDetails.add(routeDetail);

            }
           CreateOrUpdateRouteObjectsResponse resp = whService.createOrUpdateRouteObjects(r_info);
           if ( ! whTestUtils.handleServiceData( resp.serviceData, "" ) )
           {
                return null;
           }

           for(int idx = 0;idx<routes.length; idx++)
           {
               if(resp.output[idx].route.route.clientId.equals(r_info[idx].routeData.clientId))
               {
                   routes[idx] = resp.output[idx].route.route.object;
                   LogFile.write("Route with clientId: " + r_info[idx].routeData.clientId + " name : "+r_info[idx].routeData.name + " created successfully.");
                   LogFile.write(routeDetails.elementAt(idx));
               }
               else
               {
                   throw new Exception ("Route " + r_info[idx].routeData.clientId + " creation failed.\n");
               }
           }
        }
        catch ( Exception ex )
        {
            LogFile.write(ex.getMessage() + "\n");
            LogFile.write("SOA Service: createOrUpdateRouteObjects Failed." + "\n");
        }
        return routes;
    }


    /**
     * Function to create routes. Number is equal to length of RouteInfo array.
     * In context is specified, it will be used. If null, creates a context
     */
    public ModelObject[] createRoutes(RouteInfo[] r_info, ModelObject context)
    {
        int len = r_info.length;
        ModelObject[] routes = new ModelObject[len];
        try
        {
            for(int idx = 0; idx<len; idx++)
            {
                // Route info
                r_info[idx] = new RouteInfo();
                r_info[idx].routeData = new RouteData();
                r_info[idx].routeData.clientId = "wh_route" + idx;
                r_info[idx].routeData.name = "route" + idx;
                r_info[idx].context = context;

            }
            CreateOrUpdateRouteObjectsResponse resp = whService.createOrUpdateRouteObjects(r_info);
            if ( ! whTestUtils.handleServiceData( resp.serviceData, "" ) )
            {
                return null;
            }

            for(int idx = 0;idx<routes.length; idx++)
            {
                if(resp.output[idx].route.route.clientId.equals(r_info[idx].routeData.clientId))
                {
                    routes[idx] = resp.output[idx].route.route.object;
                    LogFile.write("Route " + r_info[idx].routeData.clientId + " created successfully.\n");
                }
                else
                {
                    throw new Exception ("Route " + r_info[idx].routeData.clientId + " creation failed.\n");
                }
            }
        }
        catch ( Exception ex )
        {
            LogFile.write(ex.getMessage() + "\n");
            LogFile.write("SOA Service: createOrUpdateRouteObjects Failed." + "\n");
        }
        return routes;
    }

    /**
     * Function to create route segments. Number is equal to length of RouteInfo array.
     * In context is specified, it will be used. If null, creates a context
     * This function creates route objects with start node, endnode and a
     * and a curve.
     * start node, endnode and route curves will be created.
     */

    public ModelObject[] createRouteSegmentsWithAllData(RouteInfo[] r_info, ModelObject context)
    {
        int len = r_info.length;
        ModelObject[] segments = new ModelObject[len];
        Vector <String> segmentDetails = new Vector<String>();
        try
        {
            for(int idx = 0; idx<len; idx++)
            {
                String segmentDetail = "";
                // Route segment info

                r_info[idx].context = context;
                r_info[idx].segmentData.centerCurve = new CurveData();
                r_info[idx].segmentData.startNode = new NodeData();
                r_info[idx].segmentData.endNode = new NodeData();

                // Segment curve info
                ModelObject[] curves = new ModelObject[1];
                RouteInfo[] curve_info = new RouteInfo[1];

                curvesCount++;
                curve_info[0] = new RouteInfo();
                curve_info[0].curveData = new CurveData();
                curve_info[0].curveData.clientId =  "Curve000"+curvesCount;
                curve_info[0].curveData.name =  "Curve000"+curvesCount;

                curves = this.createRouteCurves(curve_info, context);
                r_info[idx].segmentData.centerCurve.curve = curves[0];

                // Segment node info
                ModelObject[] nodes = new ModelObject[2];
                RouteInfo[] node_info = new RouteInfo[2];

                nodesCount++;
                node_info[0] = new RouteInfo();
                node_info[0].nodeData = new NodeData();
                node_info[0].nodeData.clientId = "Node000"+nodesCount;
                node_info[0].nodeData.name = "Node000"+nodesCount;

                nodesCount++;
                node_info[1] = new RouteInfo();
                node_info[1].nodeData = new NodeData();
                node_info[1].nodeData.clientId = "Node000"+nodesCount;
                node_info[1].nodeData.name = "Node000"+nodesCount;

                nodes = this.createRouteNodes(node_info, context);
                r_info[idx].segmentData.startNode.node = nodes[0];
                r_info[idx].segmentData.endNode.node = nodes[1];

                segmentDetail = segmentDetail+r_info[idx].segmentData.clientId+" has start node with name: " + node_info[0].nodeData.clientId +"\n" ;
                segmentDetail = segmentDetail+r_info[idx].segmentData.clientId+" has end node with name: " + node_info[1].nodeData.clientId +"\n" ;
                segmentDetail = segmentDetail+r_info[idx].segmentData.clientId+" has curve with name: " + curve_info[0].curveData.clientId ;

                segmentDetails.add(segmentDetail);

            }

           CreateOrUpdateRouteObjectsResponse resp = whService.createOrUpdateRouteObjects(r_info);
           if ( ! whTestUtils.handleServiceData( resp.serviceData, "" ) )
           {
                return null;
           }

            for(int idx = 0;idx<segments.length; idx++)
            {
                if(resp.output[idx].segment.segment.clientId.equals(r_info[idx].segmentData.clientId))
                {
                    segments[idx] = resp.output[idx].segment.segment.object;
                    LogFile.write( "Route segment with clientId: " + r_info[idx].segmentData.clientId +" name :" + r_info[idx].segmentData.name+ " created successfully." );
                    LogFile.write(segmentDetails.elementAt(idx));
                }
                else
                {
                    throw new Exception ( "Route segment " + r_info[idx].segmentData.clientId + " creation failed.\n" );
                }
            }
        }
        catch ( Exception ex )
        {
            LogFile.write( ex.getMessage() + "\n" );
            LogFile.write( "SOA Service: createOrUpdateRouteObjects Failed." + "\n" );
        }
        return segments;
    }
}
