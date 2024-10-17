//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================


package com.teamcenter.wireharness;

import java.util.ArrayList;
import java.util.List;

import com.teamcenter.clientx.WHSession;
import com.teamcenter.schemas.soa._2006_03.exceptions.ServiceException;
import com.teamcenter.services.strong.bom._2008_06.StructureManagement.AddOrUpdateChildrenToParentLineResponse;
import com.teamcenter.services.strong.cad._2007_01.StructureManagement.CreateBOMWindowsResponse;
import com.teamcenter.services.strong.core.SessionService;
import com.teamcenter.services.strong.core.StructureManagementService;
import com.teamcenter.services.strong.core._2006_03.DataManagement.CreateItemsResponse;
import com.teamcenter.services.strong.core._2008_06.DataManagement.CreateOrUpdateItemElementsResponse;
import com.teamcenter.services.strong.core._2008_06.StructureManagement.CreateInStructureAssociationResponse;
import com.teamcenter.services.strong.core._2008_06.StructureManagement.GetPrimariesOfInStructureAssociationInfo;
import com.teamcenter.services.strong.core._2008_06.StructureManagement.GetPrimariesOfInStructureAssociationResponse;
import com.teamcenter.services.strong.core._2008_06.StructureManagement.GetSecondariesOfInStructureAssociationInfo;
import com.teamcenter.services.strong.core._2008_06.StructureManagement.GetSecondariesOfInStructureAssociationResponse;
import com.teamcenter.services.strong.core._2008_06.StructureManagement.InStructureAssociationInfo;
import com.teamcenter.services.strong.core._2008_06.StructureManagement.RemoveInStructureAssociationsInfo;
import com.teamcenter.services.strong.core._2008_06.StructureManagement.RemoveInStructureAssociationsResponse;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.client.model.strong.BOMLine;
import com.teamcenter.soa.client.model.strong.BOMWindow;
import com.teamcenter.soa.client.model.strong.Item;
import com.teamcenter.soa.client.model.strong.ItemRevision;

public class SignalAssociations
{

    protected StructureManagementService smService;
    protected com.teamcenter.services.strong.cad.StructureManagementService smCadService;

    private static final String SIGNAL_TO_SOURCE = "SignalToSource";
    private static final String SIGNAL_TO_TARGET = "SignalToTarget";
    private static final String SIGNAL_TO_TRANSMITTER= "SignalToTransmitter";
    private static final String PROCESS_VARIABLE = "ProcessVariable";
    private static final String REDUNDANT_SIGNAL = "RedundantSignal";

    private WireHarnessTestUtils whTestUtils = null;

    /**
     * Constructor
     */
    public SignalAssociations()
    {
        smService = StructureManagementService.getService( WHSession.getConnection() );
        smCadService = com.teamcenter.services.strong.cad.StructureManagementService.getService( WHSession.getConnection() );



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
     * Test no 1 :
     *       This test tests the creation of In structure associations
     *       using "SignalToSource" relation
     * @throws Exception
     **/

    public void testInStrucAssocSignalToSource() throws Exception
    {
        try
        {
            List<Item> topLineItemsList = new ArrayList<Item>();
            List<ItemRevision> topLineRevsList = new ArrayList<ItemRevision>();
            List <String> item_ids = new ArrayList<String>();
            int num_itms = 1;

            String[] item_names = new String[1];
            item_names[0] = "TopLine_SigToSource1";

            whTestUtils.createItems( num_itms, item_names, "Item", item_ids, topLineItemsList, topLineRevsList );
            LogFile.write( "Signal Assembly - id:  " + item_ids.get(0) + "   name: " + item_names[0] + "   created successfully." );
            ItemRevision itemRevForTopLine = topLineRevsList.get(0);

            //Now create BOMWindow
            CreateBOMWindowsResponse createBOMWindowsResponse =
                    whTestUtils.createBomWindow( itemRevForTopLine );
            BOMWindow[] bomWindowArr = new BOMWindow[1];
            bomWindowArr[0] = createBOMWindowsResponse.output[0].bomWindow;

            //Get the topline
            BOMLine topLine = createBOMWindowsResponse.output[0].bomLine;

            //Create Item Element
            int num_gdes = 1;
            String [] gde_names = new String[num_gdes];
            gde_names[0] = "CT1";
            List <ModelObject> itemelems = new ArrayList<ModelObject>();
            CreateOrUpdateItemElementsResponse itemElemResp = whTestUtils.createItemElements( num_gdes, gde_names,
                    "Connection_Terminal", itemelems );
            LogFile.write( "Connection Terminal - name:" + gde_names[0] + "   created successfully.");


            whTestUtils.setDirectionOnItemElement(itemElemResp.output[0].itemElem, "");

            //Same way, create Signal object and add to Structure
            List <String> signal_ids = new ArrayList<String>();
            String[] signal_names = new String[1];
            signal_names[0] = "Mecha_Signal";
            CreateItemsResponse createItemResp =
                    whTestUtils.createItems( 1, signal_names, "Signal",  signal_ids, new ArrayList<Item>(), new ArrayList<ItemRevision>() );
            LogFile.write( "Signal - id:  " + signal_ids.get(0) + "  name: " + signal_names[0] + "   created successfully." );
            AddOrUpdateChildrenToParentLineResponse addOrUpdateChildrentoParentResp = whTestUtils.addOrUpdateChildrenToParentLine( itemElemResp, createItemResp, null,
                    "view", topLine );

            BOMLine secCTLine_1 = addOrUpdateChildrentoParentResp.itemelementLines[0].bomline;
            BOMLine primConnLine_1 = addOrUpdateChildrentoParentResp.itemLines[0].bomline;
            ModelObject[] secondariesArr1 = new ModelObject[1];
            secondariesArr1[0] = secCTLine_1;

            //*************************************************************************************
            //Create associations
            //*************************************************************************************
            InStructureAssociationInfo[] inStructAssocInfoArr =
                    new InStructureAssociationInfo[1];

            InStructureAssociationInfo inStructAssocInfo1 =
                    new InStructureAssociationInfo();
            inStructAssocInfo1.primaryBOMLine = primConnLine_1;
            inStructAssocInfo1.secondaries = secondariesArr1;
            inStructAssocInfo1.contextBOMLine = topLine;
            inStructAssocInfo1.associationType = SIGNAL_TO_SOURCE;

            inStructAssocInfoArr[0] = inStructAssocInfo1;
            CreateInStructureAssociationResponse inStrucAssocResp = smService.createInStructureAssociations(inStructAssocInfoArr);
            if ( ! whTestUtils.handleServiceData( inStrucAssocResp.serviceData, "testInStrucAssocSignalToSource: Create associations" ) )
            {
                return;
            }
            LogFile.write( "Added Signal to Source successfully.");
            //*************************************************************************************
            // Get the Primaries of relation
            //*************************************************************************************
            GetPrimariesOfInStructureAssociationInfo priStrucInfo1 =
                    new GetPrimariesOfInStructureAssociationInfo();
            priStrucInfo1.associationType = SIGNAL_TO_SOURCE;
            priStrucInfo1.contextBOMLine = topLine;
            priStrucInfo1.secondary = secondariesArr1[0];


            GetPrimariesOfInStructureAssociationInfo[] priStrucInfoArr =
                    new GetPrimariesOfInStructureAssociationInfo[1];
            priStrucInfoArr[0] = priStrucInfo1;

            GetPrimariesOfInStructureAssociationResponse pristructAssocResp = smService.getPrimariesOfInStructureAssociation( priStrucInfoArr );
            if ( ! whTestUtils.handleServiceData( pristructAssocResp.serviceData, "testInStrucAssocSignalToSource: Get the Primaries of relation" ) )
            {
                return;
            }
            if ( pristructAssocResp.primariesInfo[0] == null ||  pristructAssocResp.primariesInfo[0].primaryBOMLine  == null  || pristructAssocResp.primariesInfo[0].primaryBOMLine != primConnLine_1 )
            {
                LogFile.write( "testInStrucAssocSignalToSource: Get the Primaries of relation failed" );
                return;
            }
            LogFile.write( "Getting Primaries for relation SignalToSource succeeded.");
            //*************************************************************************************
            //Now find secondaries in relation
            //*************************************************************************************
            GetSecondariesOfInStructureAssociationInfo secStructInfo1 =
                    new GetSecondariesOfInStructureAssociationInfo();
            secStructInfo1.associationType = SIGNAL_TO_SOURCE;
            secStructInfo1.contextBOMLine = topLine;
            secStructInfo1.primaryBOMLine = primConnLine_1;

            GetSecondariesOfInStructureAssociationInfo[] secStructInfoArr1 =
                    new GetSecondariesOfInStructureAssociationInfo[1];
            secStructInfoArr1[0] = secStructInfo1;

            GetSecondariesOfInStructureAssociationResponse secstructResp = smService.getSecondariesOfInStructureAssociation( secStructInfoArr1 );
            if ( ! whTestUtils.handleServiceData( secstructResp.serviceData, "testInStrucAssocSignalToSource: Get the Secondaries of relation" ) )
            {
                return;
            }
            if ( secstructResp.secondariesInfo[0] == null ||  secstructResp.secondariesInfo[0].secondaries[0]  == null  ||  secstructResp.secondariesInfo[0].secondaries[0] != secondariesArr1[0] )
            {
                LogFile.write( "testInStrucAssocSignalToSource: Get the Secondaries of relation failed" );
                return;
            }
            LogFile.write( "Getting Secondaries for relation SignalToSource succeeded.");
            //*************************************************************************************
            //Remove the association now
            //*************************************************************************************
            RemoveInStructureAssociationsInfo remStructInfo1 =
                    new RemoveInStructureAssociationsInfo();
            remStructInfo1.associationType = SIGNAL_TO_SOURCE;
            remStructInfo1.contextBOMLine = topLine;
            remStructInfo1.primaryBOMLine = primConnLine_1;
            remStructInfo1.secondaries = secondariesArr1;

            RemoveInStructureAssociationsInfo[] remStructInfo1Arr =
                    new RemoveInStructureAssociationsInfo[1];
            remStructInfo1Arr[0] = remStructInfo1;

            RemoveInStructureAssociationsResponse remStructResp = smService.removeInStructureAssociations( remStructInfo1Arr );
            if ( ! whTestUtils.handleServiceData( remStructResp.serviceData, "testInStrucAssocSignalToSource:Remove Association" ) )
            {
                return;
            }
            LogFile.write( "Removed Signal from Source successfully.");
            //Close the BOMWindow
            smCadService.closeBOMWindows( bomWindowArr );

            //*************************************************************************************
            // Cleanup :
            // Delete the objects we created for this test
            //*************************************************************************************
            ModelObject[] objectsToBeDeletedArr = new ModelObject[3];
            objectsToBeDeletedArr[0] = topLineItemsList.get(0);
            objectsToBeDeletedArr[1] = createItemResp.output[0].item;
            objectsToBeDeletedArr[2] = itemElemResp.output[0].itemElem;
            whTestUtils.deleteObjects( objectsToBeDeletedArr );
        }
        catch ( Exception ex )
        {
            LogFile.write( "testInStrucAssocSignalToSource : " + ex.getMessage() );
            throw ex;

        }
    }

    /**
     * Test no 2 :
     *       This test tests the creation of In structure associations
     *       using "SignalToTarget" relation
     * @throws Exception
     **/

    public void testInStrucAssocSignalToTarget() throws Exception
    {
        try
        {
            List<Item> topLineItemsList = new ArrayList<Item>();
            List<ItemRevision> topLineRevsList = new ArrayList<ItemRevision>();
            List <String> item_ids = new ArrayList<String>();
            int num_itms = 1;

            String[] item_names = new String[1];
            item_names[0] = "TopLine_SigToTarget";

            whTestUtils.createItems( num_itms, item_names, "Item", item_ids, topLineItemsList, topLineRevsList );
            LogFile.write( "Signal Assembly - id:  " + item_ids.get(0) + "   name:" + item_names[0] + "   created successfully." );

            ItemRevision itemRevForTopLine = topLineRevsList.get(0);

            //Now create BOMWindow
            CreateBOMWindowsResponse createBOMWindowsResponse =
                    whTestUtils.createBomWindow( itemRevForTopLine );
            BOMWindow[] bomWindowArr = new BOMWindow[1];
            bomWindowArr[0] = createBOMWindowsResponse.output[0].bomWindow;
            //Get the topline
            BOMLine topLine = createBOMWindowsResponse.output[0].bomLine;

            //Create Item Element
            int num_gdes = 1;
            String [] gde_names = new String[num_gdes];
            gde_names[0] = "CT2";
            List <ModelObject> itemelems = new ArrayList<ModelObject>();
            CreateOrUpdateItemElementsResponse itemElemResp = whTestUtils.createItemElements( num_gdes, gde_names,
                    "Connection_Terminal", itemelems );
            LogFile.write( "Connection Terminal - name:" + gde_names[0] + "   created successfully.");

            whTestUtils.setDirectionOnItemElement(itemElemResp.output[0].itemElem, "");

            //Same way, create Signal object and add to Structure
            List <String> signal_ids = new ArrayList<String>();
            String[] signal_names = new String[1];
            signal_names[0] = "Mecha_Signal1";
            CreateItemsResponse createItemResp = whTestUtils.createItems( 1, signal_names, "Signal",  signal_ids, new ArrayList<Item>(), new ArrayList<ItemRevision>() );
            LogFile.write( "Signal - id:  " + signal_ids.get(0) + "  name: " + signal_names[0] + "   created successfully." );
            AddOrUpdateChildrenToParentLineResponse addOrUpdateChildrentoParentResp = whTestUtils.addOrUpdateChildrenToParentLine( itemElemResp, createItemResp, null, "view", topLine );

            BOMLine secCTLine_1 = addOrUpdateChildrentoParentResp.itemelementLines[0].bomline;
            BOMLine primConnLine_1 = addOrUpdateChildrentoParentResp.itemLines[0].bomline;

            ModelObject[] secondariesArr1 = new ModelObject[1];
            secondariesArr1[0] = secCTLine_1;

            //*************************************************************************************
            //Create association
            //*************************************************************************************
            InStructureAssociationInfo[] inStructAssocInfoArr =
                    new InStructureAssociationInfo[1];

            InStructureAssociationInfo inStructAssocInfo1 =
                    new InStructureAssociationInfo();
            inStructAssocInfo1.primaryBOMLine = primConnLine_1;
            inStructAssocInfo1.secondaries = secondariesArr1;
            inStructAssocInfo1.contextBOMLine = topLine;
            inStructAssocInfo1.associationType = SIGNAL_TO_TARGET;

            inStructAssocInfoArr[0] = inStructAssocInfo1;

            CreateInStructureAssociationResponse inStrucAssocResp = smService.createInStructureAssociations(inStructAssocInfoArr);
            if ( ! whTestUtils.handleServiceData( inStrucAssocResp.serviceData, "testInStrucAssocSignalToTarget: Create associations" ) )
            {
                return;
            }
            LogFile.write( "Added Signal to Target successfully.");
            //*************************************************************************************
            // Get the Primaries of relation
            //*************************************************************************************
            GetPrimariesOfInStructureAssociationInfo priStrucInfo1 =
                    new GetPrimariesOfInStructureAssociationInfo();
            priStrucInfo1.associationType = SIGNAL_TO_TARGET;
            priStrucInfo1.contextBOMLine = topLine;
            priStrucInfo1.secondary = secondariesArr1[0];


            GetPrimariesOfInStructureAssociationInfo[] priStrucInfoArr =
                    new GetPrimariesOfInStructureAssociationInfo[1];
            priStrucInfoArr[0] = priStrucInfo1;

            GetPrimariesOfInStructureAssociationResponse pristructAssocResp = smService.getPrimariesOfInStructureAssociation( priStrucInfoArr );
            if ( ! whTestUtils.handleServiceData( pristructAssocResp.serviceData, "testInStrucAssocSignalToTarget: Get the Primaries of relation" ) )
            {
                return;
            }
            if ( pristructAssocResp.primariesInfo[0] == null ||  pristructAssocResp.primariesInfo[0].primaryBOMLine  == null  || pristructAssocResp.primariesInfo[0].primaryBOMLine != primConnLine_1 )
            {
                LogFile.write( "testInStrucAssocSignalToTarget: Get the Primaries of relation failed" );
                return;
            }
            LogFile.write( "Getting Primaries for relation SignalToTarget succeeded.");

            //*************************************************************************************
            //Now find secondaries in relation
            //*************************************************************************************
            GetSecondariesOfInStructureAssociationInfo secStructInfo1 =
                    new GetSecondariesOfInStructureAssociationInfo();
            secStructInfo1.associationType = SIGNAL_TO_TARGET;
            secStructInfo1.contextBOMLine = topLine;
            secStructInfo1.primaryBOMLine = primConnLine_1;

            GetSecondariesOfInStructureAssociationInfo[] secStructInfoArr1 =
                    new GetSecondariesOfInStructureAssociationInfo[1];
            secStructInfoArr1[0] = secStructInfo1;

            GetSecondariesOfInStructureAssociationResponse secstructResp = smService.getSecondariesOfInStructureAssociation( secStructInfoArr1 );
            if ( ! whTestUtils.handleServiceData( secstructResp.serviceData, "testInStrucAssocSignalToTarget: Get the Secondaries of relation" ) )
            {
                return;
            }
            if ( secstructResp.secondariesInfo[0] == null ||  secstructResp.secondariesInfo[0].secondaries[0]  == null  ||  secstructResp.secondariesInfo[0].secondaries[0] != secondariesArr1[0] )
            {
                LogFile.write( "testInStrucAssocSignalToTarget: Get the Secondaries of relation failed" );
                return;
            }
            LogFile.write( "Getting Secondaries for relation SignalToTarget succeeded.");

            //*************************************************************************************
            //Remove the association now
            //*************************************************************************************
            RemoveInStructureAssociationsInfo remStructInfo1 =
                    new RemoveInStructureAssociationsInfo();
            remStructInfo1.associationType = SIGNAL_TO_TARGET;
            remStructInfo1.contextBOMLine = topLine;
            remStructInfo1.primaryBOMLine = primConnLine_1;
            remStructInfo1.secondaries = secondariesArr1;

            RemoveInStructureAssociationsInfo[] remStructInfo1Arr =
                    new RemoveInStructureAssociationsInfo[1];
            remStructInfo1Arr[0] = remStructInfo1;

            RemoveInStructureAssociationsResponse remStructResp = smService.removeInStructureAssociations( remStructInfo1Arr );
            if ( ! whTestUtils.handleServiceData( remStructResp.serviceData, "testInStrucAssocSignalToTarget: Remove Association" ) )
            {
                return;
            }
            LogFile.write( "Removed Signal from Target successfully.");

            //Close the BOMWindow
            smCadService.closeBOMWindows( bomWindowArr );

            //*************************************************************************************
            // Cleanup :
            // Delete the objects we created for this test
            //*************************************************************************************
            ModelObject[] objectsToBeDeletedArr = new ModelObject[3];
            objectsToBeDeletedArr[0] = topLineItemsList.get(0);
            objectsToBeDeletedArr[1] = createItemResp.output[0].item;
            objectsToBeDeletedArr[2] = itemElemResp.output[0].itemElem;


            whTestUtils.deleteObjects( objectsToBeDeletedArr );

        }
        catch ( Exception ex )
        {
            LogFile.write( "testInStrucAssocSignalToTarget : " + ex.getMessage() );
            throw ex;
        }
    }

    /**
     * Test no 3 :
     *       This test tests the creation of In structure associations
     *       using "SignalToTransmitter" relation
     * @throws Exception
     **/

    public void testInStrucAssocSignalToTransmitter() throws Exception
    {
        try
        {
            List<Item> topLineItemsList = new ArrayList<Item>();
            List<ItemRevision> topLineRevsList = new ArrayList<ItemRevision>();
            List <String> item_ids = new ArrayList<String>();
            int num_itms = 1;

            String[] item_names = new String[1];
            item_names[0] = "TopLine_SigToTransmitter";

            whTestUtils.createItems( num_itms, item_names, "Item", item_ids, topLineItemsList, topLineRevsList );
            LogFile.write( "Signal Assembly - id:  " + item_ids.get(0) + "   name:" + item_names[0] + "   created successfully." );

            ItemRevision itemRevForTopLine = topLineRevsList.get(0);

            //Now create BOMWindow
            CreateBOMWindowsResponse createBOMWindowsResponse =
                    whTestUtils.createBomWindow( itemRevForTopLine );
            BOMWindow[] bomWindowArr = new BOMWindow[1];
            bomWindowArr[0] = createBOMWindowsResponse.output[0].bomWindow;
            //Get the topline
            BOMLine topLine = createBOMWindowsResponse.output[0].bomLine;

            //Create PSConnection
            List <String> conn_ids = new ArrayList<String>();
            String[] conn_names = new String[1];
            conn_names[0] = "Mecha_PSConnection";
            CreateItemsResponse createItemResp1 =
                    whTestUtils.createItems( 1, conn_names, "PSConnection",  conn_ids, new ArrayList<Item>(), new ArrayList<ItemRevision>() );
            LogFile.write( "PSConnection - id:  " + conn_ids.get(0) + "  name: " + conn_names[0] + "   created successfully." );


            //Create Signal
            List <String> signal_ids = new ArrayList<String>();
            String[] signal_names = new String[1];
            signal_names[0] = "Mecha_Signal2";
            CreateItemsResponse createItemResp2 =
                    whTestUtils.createItems( 1, signal_names, "Signal",  signal_ids, new ArrayList<Item>(), new ArrayList<ItemRevision>() );
            LogFile.write( "Signal - id:  " + signal_ids.get(0) + "  name: " + signal_names[0] + "   created successfully." );


            AddOrUpdateChildrenToParentLineResponse addOrUpdateChildrentoParentResp1 = whTestUtils.addOrUpdateChildrenToParentLine( null, createItemResp1, null, "view", topLine );

            AddOrUpdateChildrenToParentLineResponse addOrUpdateChildrentoParentResp2 = whTestUtils.addOrUpdateChildrenToParentLine( null, createItemResp2, null, "view", topLine );

            BOMLine secCTLine_1 = addOrUpdateChildrentoParentResp1.itemLines[0].bomline;
            BOMLine primLine_1 = addOrUpdateChildrentoParentResp2.itemLines[0].bomline;

            ModelObject[] secondariesArr1 = new ModelObject[1];
            secondariesArr1[0] = secCTLine_1;

            //*************************************************************************************
            //Create association
            //*************************************************************************************
            InStructureAssociationInfo[] inStructAssocInfoArr =
                    new InStructureAssociationInfo[1];

            InStructureAssociationInfo inStructAssocInfo1 =
                    new InStructureAssociationInfo();
            inStructAssocInfo1.primaryBOMLine = primLine_1;
            inStructAssocInfo1.secondaries = secondariesArr1;
            inStructAssocInfo1.contextBOMLine = topLine;
            inStructAssocInfo1.associationType = SIGNAL_TO_TRANSMITTER;

            inStructAssocInfoArr[0] = inStructAssocInfo1;

            CreateInStructureAssociationResponse inStrucAssocResp = smService.createInStructureAssociations(inStructAssocInfoArr);
            if ( ! whTestUtils.handleServiceData( inStrucAssocResp.serviceData, "testInStrucAssocSignalToTransmitter: Create associations" ) )
            {
                return;
            }
            LogFile.write( "Added Signal to Transmitter successfully.");

            //*************************************************************************************
            // Get the Primaries of relation
            //*************************************************************************************
            GetPrimariesOfInStructureAssociationInfo priStrucInfo1 =
                    new GetPrimariesOfInStructureAssociationInfo();
            priStrucInfo1.associationType = SIGNAL_TO_TRANSMITTER;
            priStrucInfo1.contextBOMLine = topLine;
            priStrucInfo1.secondary = secondariesArr1[0];


            GetPrimariesOfInStructureAssociationInfo[] priStrucInfoArr =
                    new GetPrimariesOfInStructureAssociationInfo[1];
            priStrucInfoArr[0] = priStrucInfo1;

            GetPrimariesOfInStructureAssociationResponse pristructAssocResp = smService.getPrimariesOfInStructureAssociation( priStrucInfoArr );
            if ( ! whTestUtils.handleServiceData( pristructAssocResp.serviceData, "testInStrucAssocSignalToTransmitter: Get the Primaries of relation" ) )
            {
                return;
            }
            if ( pristructAssocResp.primariesInfo[0] == null ||  pristructAssocResp.primariesInfo[0].primaryBOMLine  == null  || pristructAssocResp.primariesInfo[0].primaryBOMLine != primLine_1 )
            {
                LogFile.write( "testInStrucAssocSignalToTransmitter: Get the Primaries of relation failed" );
                return;
            }
            LogFile.write( "Getting Primaries for relation SignalToTransmitter succeeded.");

            //*************************************************************************************
            //Now find secondaries in relation
            //*************************************************************************************
            GetSecondariesOfInStructureAssociationInfo secStructInfo1 =
                    new GetSecondariesOfInStructureAssociationInfo();
            secStructInfo1.associationType = SIGNAL_TO_TRANSMITTER;
            secStructInfo1.contextBOMLine = topLine;
            secStructInfo1.primaryBOMLine = primLine_1;

            GetSecondariesOfInStructureAssociationInfo[] secStructInfoArr1 =
                    new GetSecondariesOfInStructureAssociationInfo[1];
            secStructInfoArr1[0] = secStructInfo1;

            GetSecondariesOfInStructureAssociationResponse secstructResp = smService.getSecondariesOfInStructureAssociation( secStructInfoArr1 );
            if ( ! whTestUtils.handleServiceData( secstructResp.serviceData, "testInStrucAssocSignalToTransmitter: Get the Secondaries of relation" ) )
            {
                return;
            }
            if ( secstructResp.secondariesInfo[0] == null ||  secstructResp.secondariesInfo[0].secondaries[0]  == null  ||  secstructResp.secondariesInfo[0].secondaries[0] != secondariesArr1[0] )
            {
                LogFile.write( "testInStrucAssocSignalToTransmitter: Get the Secondaries of relation failed" );
                return;
            }
            LogFile.write( "Getting Secondaries for relation SignalToTransmitter succeeded.");

            //*************************************************************************************
            //Remove the association now
            //*************************************************************************************
            RemoveInStructureAssociationsInfo remStructInfo1 =
                    new RemoveInStructureAssociationsInfo();
            remStructInfo1.associationType = SIGNAL_TO_TRANSMITTER;
            remStructInfo1.contextBOMLine = topLine;
            remStructInfo1.primaryBOMLine = primLine_1;
            remStructInfo1.secondaries = secondariesArr1;

            RemoveInStructureAssociationsInfo[] remStructInfo1Arr =
                    new RemoveInStructureAssociationsInfo[1];
            remStructInfo1Arr[0] = remStructInfo1;

            RemoveInStructureAssociationsResponse remStructResp = smService.removeInStructureAssociations( remStructInfo1Arr );
            if ( ! whTestUtils.handleServiceData( remStructResp.serviceData, "testInStrucAssocSignalToTransmitter: Remove Association" ) )
            {
                return;
            }
            LogFile.write( "Removed Signal from Tranmitter successfully.");
            //Close the BOMWindow
            smCadService.closeBOMWindows( bomWindowArr );

            //*************************************************************************************
            // Cleanup :
            // Delete the objects we created for this test
            //*************************************************************************************
            ModelObject[] objectsToBeDeletedArr = new ModelObject[3];
            objectsToBeDeletedArr[0] = topLineItemsList.get(0);
            objectsToBeDeletedArr[1] = createItemResp1.output[0].item;
            objectsToBeDeletedArr[2] = createItemResp2.output[0].item;


            whTestUtils.deleteObjects( objectsToBeDeletedArr );
        }
        catch ( Exception ex )
        {
            LogFile.write( "testInStrucAssocSignalToTransmitter : " + ex.getMessage() );
            throw ex;
        }
    }

    /**
     * Test no 4 :
     *       This test tests the creation of In structure associations
     *       using "ProcessVariable" relation
     * @throws Exception
     **/

    public void testInStrucAssocSignalToProcessVariable() throws Exception
    {
        try
        {
            List<Item> topLineItemsList = new ArrayList<Item>();
            List<ItemRevision> topLineRevsList = new ArrayList<ItemRevision>();
            List <String> item_ids = new ArrayList<String>();
            int num_itms = 1;

            String[] item_names = new String[1];
            item_names[0] = "TopLine_SigToProcessVariable";

            whTestUtils.createItems( num_itms, item_names, "Item", item_ids, topLineItemsList, topLineRevsList );
            LogFile.write( "Signal Assembly - id:  " + item_ids.get(0) + "   name:" + item_names[0] + "   created successfully." );

            ItemRevision itemRevForTopLine = topLineRevsList.get(0);

            //Now create BOMWindow
            CreateBOMWindowsResponse createBOMWindowsResponse =
                    whTestUtils.createBomWindow( itemRevForTopLine );
            BOMWindow[] bomWindowArr = new BOMWindow[1];
            bomWindowArr[0] = createBOMWindowsResponse.output[0].bomWindow;
            //Get the topline
            BOMLine topLine = createBOMWindowsResponse.output[0].bomLine;


            //Create Item Element
            int num_gdes = 1;
            String [] gde_names = new String[num_gdes];
            gde_names[0] = "ProcVar1";
            List <ModelObject> itemelems = new ArrayList<ModelObject>();
            CreateOrUpdateItemElementsResponse itemElemResp = whTestUtils.createItemElements( num_gdes, gde_names,
                    "ProcessVariable", itemelems );
            LogFile.write( "ProcessVariable - name:" + gde_names[0] + "   created successfully.");

            //Create Signal
            List <String> signal_ids = new ArrayList<String>();
            String[] signal_names = new String[1];
            signal_names[0] = "Mecha_Signal3";
            CreateItemsResponse createItemResp =
                    whTestUtils.createItems( 1, signal_names, "Signal",  signal_ids, new ArrayList<Item>(), new ArrayList<ItemRevision>() );
            LogFile.write( "Signal - id:  " + signal_ids.get(0) + "  name: " + signal_names[0] + "   created successfully." );

            AddOrUpdateChildrenToParentLineResponse addOrUpdateChildrentoParentResp = whTestUtils.addOrUpdateChildrenToParentLine( itemElemResp, createItemResp, null, "view", topLine );

            BOMLine secCTLine_1 = addOrUpdateChildrentoParentResp.itemelementLines[0].bomline;
            BOMLine primConnLine_1 = addOrUpdateChildrentoParentResp.itemLines[0].bomline;

            ModelObject[] secondariesArr1 = new ModelObject[1];
            secondariesArr1[0] = secCTLine_1;

            //*************************************************************************************
            //Create association
            //*************************************************************************************
            InStructureAssociationInfo[] inStructAssocInfoArr =
                    new InStructureAssociationInfo[1];

            InStructureAssociationInfo inStructAssocInfo1 =
                    new InStructureAssociationInfo();
            inStructAssocInfo1.primaryBOMLine = primConnLine_1;
            inStructAssocInfo1.secondaries = secondariesArr1;
            inStructAssocInfo1.contextBOMLine = topLine;
            inStructAssocInfo1.associationType = PROCESS_VARIABLE;

            inStructAssocInfoArr[0] = inStructAssocInfo1;

            CreateInStructureAssociationResponse inStrucAssocResp = smService.createInStructureAssociations(inStructAssocInfoArr);
            if ( ! whTestUtils.handleServiceData( inStrucAssocResp.serviceData, "testInStrucAssocSignalToProcessVariable: Create associations" ) )
            {
                return;
            }
            LogFile.write( "Added Signal to Process Variable successfully.");

            //*************************************************************************************
            // Get the Primaries of relation
            //*************************************************************************************
            GetPrimariesOfInStructureAssociationInfo priStrucInfo1 =
                    new GetPrimariesOfInStructureAssociationInfo();
            priStrucInfo1.associationType = PROCESS_VARIABLE;
            priStrucInfo1.contextBOMLine = topLine;
            priStrucInfo1.secondary = secondariesArr1[0];


            GetPrimariesOfInStructureAssociationInfo[] priStrucInfoArr =
                    new GetPrimariesOfInStructureAssociationInfo[1];
            priStrucInfoArr[0] = priStrucInfo1;

            GetPrimariesOfInStructureAssociationResponse pristructAssocResp = smService.getPrimariesOfInStructureAssociation( priStrucInfoArr );
            if ( ! whTestUtils.handleServiceData( pristructAssocResp.serviceData, "testInStrucAssocSignalToProcessVariable: Get the Primaries of relation" ) )
            {
                return;
            }
            if ( pristructAssocResp.primariesInfo[0] == null ||  pristructAssocResp.primariesInfo[0].primaryBOMLine  == null  || pristructAssocResp.primariesInfo[0].primaryBOMLine != primConnLine_1 )
            {
                LogFile.write( "testInStrucAssocSignalToProcessVariable: Get the Primaries of relation failed" );
                return;
            }
            LogFile.write( "Getting Primaries for relation ProcessVariable succeeded.");

            //*************************************************************************************
            //Now find secondaries in relation
            //*************************************************************************************
            GetSecondariesOfInStructureAssociationInfo secStructInfo1 =
                    new GetSecondariesOfInStructureAssociationInfo();
            secStructInfo1.associationType = PROCESS_VARIABLE;
            secStructInfo1.contextBOMLine = topLine;
            secStructInfo1.primaryBOMLine = primConnLine_1;

            GetSecondariesOfInStructureAssociationInfo[] secStructInfoArr1 =
                    new GetSecondariesOfInStructureAssociationInfo[1];
            secStructInfoArr1[0] = secStructInfo1;


            GetSecondariesOfInStructureAssociationResponse secstructResp = smService.getSecondariesOfInStructureAssociation( secStructInfoArr1 );
            if ( ! whTestUtils.handleServiceData( secstructResp.serviceData, "testInStrucAssocSignalToProcessVariable: Get the Secondaries of relation" ) )
            {
                return;
            }
            if ( secstructResp.secondariesInfo[0] == null ||  secstructResp.secondariesInfo[0].secondaries[0]  == null  ||  secstructResp.secondariesInfo[0].secondaries[0] != secondariesArr1[0] )
            {
                LogFile.write( "testInStrucAssocSignalToProcessVariable: Get the Secondaries of relation failed" );
                return;
            }
            LogFile.write( "Getting Secondaries for relation ProcessVariable succeeded.");

            //*************************************************************************************
            //Remove the association now
            //*************************************************************************************
            RemoveInStructureAssociationsInfo remStructInfo1 =
                    new RemoveInStructureAssociationsInfo();
            remStructInfo1.associationType = PROCESS_VARIABLE;
            remStructInfo1.contextBOMLine = topLine;
            remStructInfo1.primaryBOMLine = primConnLine_1;
            remStructInfo1.secondaries = secondariesArr1;

            RemoveInStructureAssociationsInfo[] remStructInfo1Arr =
                    new RemoveInStructureAssociationsInfo[1];
            remStructInfo1Arr[0] = remStructInfo1;

            RemoveInStructureAssociationsResponse remStructResp = smService.removeInStructureAssociations( remStructInfo1Arr );
            if ( ! whTestUtils.handleServiceData( remStructResp.serviceData, "testInStrucAssocSignalToProcessVariable: Remove Association" ) )
            {
                return;
            }
            LogFile.write( "Removed Signal from Process Variable successfully.");

            //Close the BOMWindow
            smCadService.closeBOMWindows( bomWindowArr );

            //*************************************************************************************
            // Cleanup :
            // Delete the objects we created for this test
            //*************************************************************************************
            ModelObject[] objectsToBeDeletedArr = new ModelObject[3];
            objectsToBeDeletedArr[0] = topLineItemsList.get(0);
            objectsToBeDeletedArr[1] = createItemResp.output[0].item;
            objectsToBeDeletedArr[2] = itemElemResp.output[0].itemElem;

            whTestUtils.deleteObjects( objectsToBeDeletedArr );

        }
        catch ( Exception ex )
        {
            LogFile.write( "testInStrucAssocSignalToProcessVariable : " + ex.getMessage() );
            throw ex;
        }
    }

    /**
     * Test no 5 :
     *       This test tests the creation of In structure associations
     *       using "RedundantSignal" relation
     * @throws Exception
     **/
    public void testInStrucAssocRedundantSignal() throws Exception
    {
        try
        {
            List<Item> topLineItemsList = new ArrayList<Item>();
            List<ItemRevision> topLineRevsList = new ArrayList<ItemRevision>();
            List <String> item_ids = new ArrayList<String>();
            int num_itms = 1;

            String[] item_names = new String[1];
            item_names[0] = "TopLine_RedundantSignal";

            whTestUtils.createItems( num_itms, item_names, "Item", item_ids, topLineItemsList, topLineRevsList );
            LogFile.write( "Signal Assembly - id:  " + item_ids.get(0) + "   name:" + item_names[0] + "   created successfully." );

            ItemRevision itemRevForTopLine = topLineRevsList.get(0);

            //Now create BOMWindow
            CreateBOMWindowsResponse createBOMWindowsResponse =
                    whTestUtils.createBomWindow( itemRevForTopLine );
            BOMWindow[] bomWindowArr = new BOMWindow[1];
            bomWindowArr[0] = createBOMWindowsResponse.output[0].bomWindow;
            //Get the topline
            BOMLine topLine = createBOMWindowsResponse.output[0].bomLine;

            //Create Signal1
            List <String> signal_ids = new ArrayList<String>();
            String[] signal_names = new String[1];
            signal_names[0] = "Mecha_Signal4";
            CreateItemsResponse createItemResp1 =
                    whTestUtils.createItems( 1, signal_names, "Signal",  signal_ids, new ArrayList<Item>(), new ArrayList<ItemRevision>() );
            LogFile.write( "Signal - id:  " + signal_ids.get(0) + "  name: " + signal_names[0] + "   created successfully." );

            //Signal2
            List <String> signal_ids1 = new ArrayList<String>();
            String[] signal_names1 = new String[1];
            signal_names1[0] = "Mecha_Signal5";
            CreateItemsResponse createItemResp2 =
                    whTestUtils.createItems( 1, signal_names1, "Signal",  signal_ids1, new ArrayList<Item>(), new ArrayList<ItemRevision>() );
            LogFile.write( "Signal - id:  " + signal_ids1.get(0) + "  name: " + signal_names1[0] + "   created successfully." );


            AddOrUpdateChildrenToParentLineResponse addOrUpdateChildrentoParentResp1 = whTestUtils.addOrUpdateChildrenToParentLine( null, createItemResp1, null, "view", topLine );

            AddOrUpdateChildrenToParentLineResponse addOrUpdateChildrentoParentResp2 = whTestUtils.addOrUpdateChildrenToParentLine( null, createItemResp2, null, "view", topLine );

            BOMLine secCTLine_1 = addOrUpdateChildrentoParentResp1.itemLines[0].bomline;
            BOMLine primLine_1 = addOrUpdateChildrentoParentResp2.itemLines[0].bomline;

            ModelObject[] secondariesArr1 = new ModelObject[1];
            secondariesArr1[0] = secCTLine_1;

            //*************************************************************************************
            //Create association
            //*************************************************************************************
            InStructureAssociationInfo[] inStructAssocInfoArr =
                    new InStructureAssociationInfo[1];

            InStructureAssociationInfo inStructAssocInfo1 =
                    new InStructureAssociationInfo();
            inStructAssocInfo1.primaryBOMLine = primLine_1;
            inStructAssocInfo1.secondaries = secondariesArr1;
            inStructAssocInfo1.contextBOMLine = topLine;
            inStructAssocInfo1.associationType = REDUNDANT_SIGNAL;

            inStructAssocInfoArr[0] = inStructAssocInfo1;

            CreateInStructureAssociationResponse inStrucAssocResp = smService.createInStructureAssociations(inStructAssocInfoArr);
            if ( ! whTestUtils.handleServiceData( inStrucAssocResp.serviceData, "testInStrucAssocRedundantSignal: Create associations" ) )
            {
                return;
            }
            LogFile.write( "Added Signal to Redundant Signal successfully.");
            //*************************************************************************************
            // Get the Primaries of relation
            //*************************************************************************************
            GetPrimariesOfInStructureAssociationInfo priStrucInfo1 =
                    new GetPrimariesOfInStructureAssociationInfo();
            priStrucInfo1.associationType = REDUNDANT_SIGNAL;
            priStrucInfo1.contextBOMLine = topLine;
            priStrucInfo1.secondary = secondariesArr1[0];


            GetPrimariesOfInStructureAssociationInfo[] priStrucInfoArr =
                    new GetPrimariesOfInStructureAssociationInfo[1];
            priStrucInfoArr[0] = priStrucInfo1;

            GetPrimariesOfInStructureAssociationResponse pristructAssocResp = smService.getPrimariesOfInStructureAssociation( priStrucInfoArr );
            if ( ! whTestUtils.handleServiceData( pristructAssocResp.serviceData, "testInStrucAssocRedundantSignal: Get the Primaries of relation" ) )
            {
                return;
            }
            if ( pristructAssocResp.primariesInfo[0] == null ||  pristructAssocResp.primariesInfo[0].primaryBOMLine  == null  || pristructAssocResp.primariesInfo[0].primaryBOMLine != primLine_1 )
            {
                LogFile.write( "testInStrucAssocRedundantSignal: Get the Primaries of relation failed" );
                return;
            }
            LogFile.write( "Getting Primaries for relation RedundantSignal succeeded.");

            //*************************************************************************************
            //Now find secondaries in relation
            //*************************************************************************************
            GetSecondariesOfInStructureAssociationInfo secStructInfo1 =
                    new GetSecondariesOfInStructureAssociationInfo();
            secStructInfo1.associationType = REDUNDANT_SIGNAL;
            secStructInfo1.contextBOMLine = topLine;
            secStructInfo1.primaryBOMLine = primLine_1;

            GetSecondariesOfInStructureAssociationInfo[] secStructInfoArr1 =
                    new GetSecondariesOfInStructureAssociationInfo[1];
            secStructInfoArr1[0] = secStructInfo1;


            GetSecondariesOfInStructureAssociationResponse secstructResp = smService.getSecondariesOfInStructureAssociation( secStructInfoArr1 );
            if ( ! whTestUtils.handleServiceData( secstructResp.serviceData, "testInStrucAssocRedundantSignal: Get the Secondaries of relation" ) )
            {
                return;
            }
            if ( secstructResp.secondariesInfo[0] == null ||  secstructResp.secondariesInfo[0].secondaries[0]  == null  ||  secstructResp.secondariesInfo[0].secondaries[0] != secondariesArr1[0] )
            {
                LogFile.write( "testInStrucAssocRedundantSignal: Get the Secondaries of relation failed" );
                return;
            }
            LogFile.write( "Getting Secondaries for relation RedundantSignal succeeded.");

            //*************************************************************************************
            //Remove the association now
            //*************************************************************************************
            RemoveInStructureAssociationsInfo remStructInfo1 =
                    new RemoveInStructureAssociationsInfo();
            remStructInfo1.associationType = REDUNDANT_SIGNAL;
            remStructInfo1.contextBOMLine = topLine;
            remStructInfo1.primaryBOMLine = primLine_1;
            remStructInfo1.secondaries = secondariesArr1;

            RemoveInStructureAssociationsInfo[] remStructInfo1Arr =
                    new RemoveInStructureAssociationsInfo[1];
            remStructInfo1Arr[0] = remStructInfo1;

            RemoveInStructureAssociationsResponse remStructResp = smService.removeInStructureAssociations( remStructInfo1Arr );
            if ( ! whTestUtils.handleServiceData( remStructResp.serviceData, "testInStrucAssocRedundantSignal: Remove Association" ) )
            {
                return;
            }
            LogFile.write( "Removed Signal from Redundant Signal successfully.");
            //Close the BOMWindow
            smCadService.closeBOMWindows( bomWindowArr );

            //*************************************************************************************
            // Cleanup :
            // Delete the objects we created for this test
            //*************************************************************************************
            ModelObject[] objectsToBeDeletedArr = new ModelObject[3];
            objectsToBeDeletedArr[0] = topLineItemsList.get(0);
            objectsToBeDeletedArr[1] = createItemResp1.output[0].item;
            objectsToBeDeletedArr[2] = createItemResp2.output[0].item;

            whTestUtils.deleteObjects( objectsToBeDeletedArr );
        }
        catch ( Exception ex )
        {
            LogFile.write( "testInStrucAssocRedundantSignal : " + ex.getMessage() );
            throw ex;

        }
    }
}
