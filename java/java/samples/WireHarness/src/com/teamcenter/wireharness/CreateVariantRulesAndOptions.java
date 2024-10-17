//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================


package com.teamcenter.wireharness;

import java.util.Map;

import com.teamcenter.clientx.WHSession;
import com.teamcenter.services.strong.cad.StructureManagementService;
import com.teamcenter.services.strong.cad._2007_01.StructureManagement.GetVariantRulesResponse;
import com.teamcenter.services.strong.cad._2007_06.StructureManagement.ClassicOptionInfo;
import com.teamcenter.services.strong.cad._2007_06.StructureManagement.CreateUpdateClassicOptionsInput;
import com.teamcenter.services.strong.cad._2007_09.StructureManagement.CreateOrUpdateVariantCondInput;
import com.teamcenter.services.strong.cad._2007_09.StructureManagement.VariantCondInfo;
import com.teamcenter.services.strong.cad._2008_06.StructureManagement.CreateVariantRulesInfo;
import com.teamcenter.services.strong.cad._2008_06.StructureManagement.CreateVariantRulesResponse;
import com.teamcenter.services.strong.cad._2008_06.StructureManagement.OptionsInfo;
import com.teamcenter.soa.client.model.ServiceData;
import com.teamcenter.soa.client.model.strong.BOMLine;
import com.teamcenter.soa.client.model.strong.ItemRevision;
import com.teamcenter.soa.client.model.strong.VariantRule;
import com.teamcenter.wireharness.HarnessClientIDInfo.HRNObjInfo;
import com.teamcenter.wireharness.HarnessOccBomMapInfo.HarnessStructureData;

public class CreateVariantRulesAndOptions
{
    protected StructureManagementService smService = null;

    private ItemRevision topItemRev = null;
    private BOMLine topLine = null;
    private ClassicOptionInfo[] optionsInfoArray = null;
    private CreateHarness createHarnessObj = null;
    private HarnessObjects hrn_struct = null;
    private Map<String, HRNObjInfo> clientOccurrenceMap = null;
    private HarnessClientIDInfo hrnAssyOccInfo = null;
    private HarnessStructureData hrnStructData;
    private WireHarnessTestUtils whTestUtils = null;

    /**
     * Constructor
     * @param createHarnObj
     */
    public CreateVariantRulesAndOptions( CreateHarness createHarnObj )
    {
        smService = StructureManagementService.getService( WHSession.getConnection() );

        createHarnessObj = createHarnObj;
        clientOccurrenceMap = createHarnessObj.getClientOccurrenceMap();
        hrnAssyOccInfo = createHarnessObj.getHarnessClientIDInfo();
        hrnStructData = createHarnessObj.getHarnessStructureData();

        // Get TopItemRev
        hrn_struct = createHarnessObj.getHarnessObjects();
        topItemRev = (ItemRevision)hrn_struct.hrn_assy_rev;
        // Get Topline now
        topLine = hrnStructData.getHarnessTopLine();
        whTestUtils = new WireHarnessTestUtils();
    }


    /**
     * Create VariantRules And Options (Options sets as well) for Harness structure.
     * Also create Variant Conditions and apply them to structure objects
     * @return
     */
    public void createVariantData() throws Exception
    {
        try
        {
            // Create classic options (and option Set)
            // --------------------------
            // |   |  Color  |  Size    |
            // --------------------------
            // | 1 | Blue    | Large    |
            // | 2 | Green   | Medium   |
            // | 3 | Red     | Compact  |
            // --------------------------
            ServiceData optSvcData = this.createOptionsInHarnessStruct( topLine );
            if ( ! whTestUtils.handleServiceData( optSvcData, "createVariantData" ) )
            {
                return;
            }
            LogFile.write( "createVariantData : Created Option sets and " +
                                                    "Options successfully" );

            //**********************************
            // Now lets create variant rules
            //**********************************
            // create three Variant rules
            // VR_CompactRed    size = compact color=red
            // VR_MediumGreen   size = medium  color=green
            // VR_LargeBlue     size = large   color=blue

            // First create OptionsInfoObjs
            ClassicOptionInfo cOptInfoObjSize = new ClassicOptionInfo();
            ClassicOptionInfo cOptInfoObjColor = new ClassicOptionInfo();
            cOptInfoObjSize = optionsInfoArray[0];
            cOptInfoObjColor = optionsInfoArray[1];

            OptionsInfo[] optsInfoArrVR1 = new OptionsInfo[2];
            optsInfoArrVR1[0] = this.populateOptsInfoForVarRule(
                                        topItemRev, cOptInfoObjSize.optionName,
                                        cOptInfoObjSize.values[2] );    //size = compact
            optsInfoArrVR1[1] = this.populateOptsInfoForVarRule(
                                        topItemRev, cOptInfoObjColor.optionName,
                                        cOptInfoObjColor.values[2] );   //color= red

            OptionsInfo[] optsInfoArrVR2 = new OptionsInfo[2];
            optsInfoArrVR2[0] = this.populateOptsInfoForVarRule(
                                        topItemRev, cOptInfoObjSize.optionName,
                                        cOptInfoObjSize.values[1] );    //size = medium
            optsInfoArrVR2[1] = this.populateOptsInfoForVarRule(
                                        topItemRev, cOptInfoObjColor.optionName,
                                        cOptInfoObjColor.values[1] );   //color= green

            OptionsInfo[] optsInfoArrVR3 = new OptionsInfo[2];
            optsInfoArrVR3[0] = this.populateOptsInfoForVarRule(
                                        topItemRev, cOptInfoObjSize.optionName,
                                        cOptInfoObjSize.values[0] );    //size = large
            optsInfoArrVR3[1] = this.populateOptsInfoForVarRule(
                                        topItemRev, cOptInfoObjColor.optionName,
                                        cOptInfoObjColor.values[0] );   //color= blue

            //create Variant Rules Info Objs
            CreateVariantRulesInfo varRulesInfoVR1 =
                        this.createVariantRulesInfoObj( "varRulesInfoVr_CompactRed", "VR_CompactRed",
                                                            topItemRev, optsInfoArrVR1 );
            CreateVariantRulesInfo varRulesInfoVR2 =
                        this.createVariantRulesInfoObj( "varRulesInfoVR2", "VR_MediumGreen",
                                                            topItemRev, optsInfoArrVR2 );
            CreateVariantRulesInfo varRulesInfoVR3 =
                        this.createVariantRulesInfoObj( "varRulesInfoVR3", "VR_LargeBlue",
                                                            topItemRev, optsInfoArrVR3 );

            CreateVariantRulesInfo[] varRulesInfoArr = new CreateVariantRulesInfo[3];
            varRulesInfoArr[0] = varRulesInfoVR1;
            varRulesInfoArr[1] = varRulesInfoVR2;
            varRulesInfoArr[2] = varRulesInfoVR3;

            CreateVariantRulesResponse cVarRulesResp = smService.createVariantRules( varRulesInfoArr );
            if ( ! whTestUtils.handleServiceData( cVarRulesResp.serviceData, "createVariantData" ) )
            {
                return;
            }
            LogFile.write( "createVariantData : Created Variant Rules successfully" );

            // Now create and apply the variant conditions on objects in harness assembly
            // ------------------------------------
            //     Name         Variant Condition
            // ------------------------------------
            // connector0003    Size=Large
            // connector0002    Size=Medium
            // connector0001    Size=Compact
            // connection0003   Size=Large
            // connection0002   Size=Medium
            // connection0001   Size=Compact
            // device0003       Size=Large
            // device0002       Size=Medium
            // device0001       Size=Compact
            // Wire0003         Size=Large
            // Wire0002         Size=Medium
            // Wire0001         Size=Compact
            // Complex Cable    Color=Red
            // Simple Cable     Color=Green
            // Complex Shield   Color=Red
            // Simple Shield    Color=Blue

            CreateOrUpdateVariantCondInput[]
                    varCondInputInfoArr = this.populateVariantConditionsInput();
            ServiceData varCondSvcData =
                    smService.createOrUpdateVariantConditions2( varCondInputInfoArr );
            if ( ! whTestUtils.handleServiceData( varCondSvcData, "createVariantData" ) )
            {
                return;
            }
            LogFile.write( "createVariantData : Created Variant Conditions successfully" );
        }
        catch( Exception ex )
        {
            LogFile.write( "createVariantData : " + ex.getMessage() );
            throw ex;
        }
    }

    /**
     * Create Classic Options in Harness Structure for the topline
     */
    private ServiceData createOptionsInHarnessStruct( BOMLine topLine ) throws Exception
    {
        ServiceData serviceDataForOptions = null;
        try
        {
            // Create objects of ClassicOptionInfo
            // Size option
            ClassicOptionInfo cOptInfoObjSize = new ClassicOptionInfo();
            cOptInfoObjSize.optionName = "Size";
            cOptInfoObjSize.optionDesc = "Size Option for Wire Harness Structure";
            cOptInfoObjSize.values =  new String[3];
            cOptInfoObjSize.values[0] = new String( "Large" );
            cOptInfoObjSize.values[1] = new String( "Medium" );
            cOptInfoObjSize.values[2] = new String( "Compact" );

            // Color option
            ClassicOptionInfo cOptInfoObjColor = new ClassicOptionInfo();
            cOptInfoObjColor.optionName = "Color";
            cOptInfoObjColor.optionDesc = "Color Option for Wire Harness Structure";
            cOptInfoObjColor.values =  new String[3];
            cOptInfoObjColor.values[0] = new String( "Blue" );
            cOptInfoObjColor.values[1] = new String( "Green" );
            cOptInfoObjColor.values[2] = new String( "Red" );

            optionsInfoArray = new ClassicOptionInfo[2];
            optionsInfoArray[0] = cOptInfoObjSize;
            optionsInfoArray[1] = cOptInfoObjColor;

            // Now create Classic Options Input object
            CreateUpdateClassicOptionsInput optionsInput =
                                        new CreateUpdateClassicOptionsInput();
            optionsInput.clientId = "whOptsSizeAndcolor";
            optionsInput.bomLine = topLine;
            optionsInput.operation = "CreateOption";
            optionsInput.options = optionsInfoArray;

            // Now create Classic options input array
            CreateUpdateClassicOptionsInput[] createOptsInputArr =
                                        new CreateUpdateClassicOptionsInput[1];
            createOptsInputArr[0] = optionsInput;

            //Create classic options (and option Set)
            serviceDataForOptions = smService.createOrUpdateClassicOptions( createOptsInputArr );
        }
        catch( Exception ex )
        {
            LogFile.write( "createOptionsInHarnessStruct : " + ex.getMessage() );
            throw ex;
        }
        return serviceDataForOptions;
    }


    /**
     * Creates the Variant Rules Info Object
     * @param clientId
     * @param varRuleName
     * @param itemRev
     * @param options
     * @return
     */
    private CreateVariantRulesInfo createVariantRulesInfoObj(
                                                    String clientId,
                                                    String varRuleName,
                                                    ItemRevision itemRev,
                                                    OptionsInfo[] options )
    {
        CreateVariantRulesInfo varRulesInfo = new CreateVariantRulesInfo();
        varRulesInfo.clientID = clientId;
        varRulesInfo.options = options;
        varRulesInfo.parent = null;
        varRulesInfo.relation = "IMAN_reference";
        varRulesInfo.rev = itemRev;
        varRulesInfo.vruleDescription = "Variant rule : " + varRuleName;
        varRulesInfo.vruleName = varRuleName;
        return varRulesInfo;
    }

    /**
     * Populate Options Info for Variant Rule
     * @param itemRev
     * @param optionName
     * @param optionValue
     * @return
     */
    private OptionsInfo populateOptsInfoForVarRule (    ItemRevision itemRev,
                                                        String optionName,
                                                        String optionValue )
    {
        OptionsInfo optsInfoObj = new OptionsInfo();
        optsInfoObj.assocRev = itemRev;
        optsInfoObj.optionName = optionName;
        optsInfoObj.optionValue = optionValue;
        return optsInfoObj;
    }

    /**
     * Populate Variant Conditions Input object Array
     * @return
     */
    private CreateOrUpdateVariantCondInput[] populateVariantConditionsInput() throws Exception
    {
        CreateOrUpdateVariantCondInput[] varCondInputInfoArr = null;
        try
        {
            ClassicOptionInfo cOptInfoObjSize = new ClassicOptionInfo();
            ClassicOptionInfo cOptInfoObjColor = new ClassicOptionInfo();
            cOptInfoObjSize = optionsInfoArray[0];
            cOptInfoObjColor = optionsInfoArray[1];

            // Create variant condition info objects

            // Size = Compact
            VariantCondInfo variantCondSizeCompact = new VariantCondInfo();
            variantCondSizeCompact.optionName = cOptInfoObjSize.optionName;
            variantCondSizeCompact.joinOperator = "AND";
            variantCondSizeCompact.compOperator = "EQ";
            variantCondSizeCompact.value = cOptInfoObjSize.values[2];
            VariantCondInfo[] variantCondSizeCompactArr = new VariantCondInfo[1];
            variantCondSizeCompactArr[0] = variantCondSizeCompact;

            // Size = Medium
            VariantCondInfo variantCondSizeMedium = new VariantCondInfo();
            variantCondSizeMedium.optionName = cOptInfoObjSize.optionName;
            variantCondSizeMedium.joinOperator = "AND";
            variantCondSizeMedium.compOperator = "EQ";
            variantCondSizeMedium.value = cOptInfoObjSize.values[1];
            VariantCondInfo[] variantCondSizeMediumArr = new VariantCondInfo[1];
            variantCondSizeMediumArr[0] = variantCondSizeMedium;

            // Size = Large
            VariantCondInfo variantCondSizeLarge = new VariantCondInfo();
            variantCondSizeLarge.optionName = cOptInfoObjSize.optionName;
            variantCondSizeLarge.joinOperator = "AND";
            variantCondSizeLarge.compOperator = "EQ";
            variantCondSizeLarge.value = cOptInfoObjSize.values[0];
            VariantCondInfo[] variantCondSizeLargeArr = new VariantCondInfo[1];
            variantCondSizeLargeArr[0] = variantCondSizeLarge;

            // Color = Red
            VariantCondInfo variantCondColorRed = new VariantCondInfo();
            variantCondColorRed.optionName = cOptInfoObjColor.optionName;
            variantCondColorRed.joinOperator = "AND";
            variantCondColorRed.compOperator = "EQ";
            variantCondColorRed.value = cOptInfoObjColor.values[2];
            VariantCondInfo[] variantCondColorRedArr = new VariantCondInfo[1];
            variantCondColorRedArr[0] = variantCondColorRed;

            // Color = Green
            VariantCondInfo variantCondColorGreen = new VariantCondInfo();
            variantCondColorGreen.optionName = cOptInfoObjColor.optionName;
            variantCondColorGreen.joinOperator = "AND";
            variantCondColorGreen.compOperator = "EQ";
            variantCondColorGreen.value = cOptInfoObjColor.values[1];
            VariantCondInfo[] variantCondColorGreenArr = new VariantCondInfo[1];
            variantCondColorGreenArr[0] = variantCondColorGreen;

            // Color = Blue
            VariantCondInfo variantCondColorBlue = new VariantCondInfo();
            variantCondColorBlue.optionName = cOptInfoObjColor.optionName;
            variantCondColorBlue.joinOperator = "AND";
            variantCondColorBlue.compOperator = "EQ";
            variantCondColorBlue.value = cOptInfoObjColor.values[0];
            VariantCondInfo[] variantCondColorBlueArr = new VariantCondInfo[1];
            variantCondColorBlueArr[0] = variantCondColorBlue;

            // Now create 16 Variant Condition input info objects
            //Connector 3 - Large
            CreateOrUpdateVariantCondInput varCondInputInfo01 = new CreateOrUpdateVariantCondInput();
            varCondInputInfo01.bomLine = (BOMLine)clientOccurrenceMap.get( hrnAssyOccInfo.ctr3_id ).bomline;
            varCondInputInfo01.clientId = "SizeCompact";
            varCondInputInfo01.operation = "Create";
            varCondInputInfo01.variantCondInfo = variantCondSizeLargeArr;

            //Connector 2 - Medium
            CreateOrUpdateVariantCondInput varCondInputInfo02 = new CreateOrUpdateVariantCondInput();
            varCondInputInfo02.bomLine = (BOMLine)clientOccurrenceMap.get( hrnAssyOccInfo.ctr2_id ).bomline;
            varCondInputInfo02.clientId = "SizeCompact";
            varCondInputInfo02.operation = "Create";
            varCondInputInfo02.variantCondInfo = variantCondSizeMediumArr;

            //Connector 1 - Compact
            CreateOrUpdateVariantCondInput varCondInputInfo03 = new CreateOrUpdateVariantCondInput();
            varCondInputInfo03.bomLine = (BOMLine)clientOccurrenceMap.get( hrnAssyOccInfo.ctr1_id ).bomline;
            varCondInputInfo03.clientId = "SizeCompact";
            varCondInputInfo03.operation = "Create";
            varCondInputInfo03.variantCondInfo = variantCondSizeCompactArr;

            // Connection 3 - Large
            CreateOrUpdateVariantCondInput varCondInputInfo04 = new CreateOrUpdateVariantCondInput();
            varCondInputInfo04.bomLine = (BOMLine)clientOccurrenceMap.get( hrnAssyOccInfo.conn3_id ).bomline;
            varCondInputInfo04.clientId = "SizeCompact";
            varCondInputInfo04.operation = "Create";
            varCondInputInfo04.variantCondInfo = variantCondSizeLargeArr;

            // Connection 2 - Medium
            CreateOrUpdateVariantCondInput varCondInputInfo05 = new CreateOrUpdateVariantCondInput();
            varCondInputInfo05.bomLine = (BOMLine)clientOccurrenceMap.get( hrnAssyOccInfo.conn2_id ).bomline;
            varCondInputInfo05.clientId = "SizeCompact";
            varCondInputInfo05.operation = "Create";
            varCondInputInfo05.variantCondInfo = variantCondSizeMediumArr;

            // Connection 1 - Compact
            CreateOrUpdateVariantCondInput varCondInputInfo06 = new CreateOrUpdateVariantCondInput();
            varCondInputInfo06.bomLine = (BOMLine)clientOccurrenceMap.get( hrnAssyOccInfo.conn1_id ).bomline;
            varCondInputInfo06.clientId = "SizeCompact";
            varCondInputInfo06.operation = "Create";
            varCondInputInfo06.variantCondInfo = variantCondSizeCompactArr;

            // Device 3 - Large
            CreateOrUpdateVariantCondInput varCondInputInfo07 = new CreateOrUpdateVariantCondInput();
            varCondInputInfo07.bomLine = (BOMLine)clientOccurrenceMap.get( hrnAssyOccInfo.device3_id ).bomline;
            varCondInputInfo07.clientId = "SizeCompact";
            varCondInputInfo07.operation = "Create";
            varCondInputInfo07.variantCondInfo = variantCondSizeLargeArr;

            // Device 2 - Medium
            CreateOrUpdateVariantCondInput varCondInputInfo08 = new CreateOrUpdateVariantCondInput();
            varCondInputInfo08.bomLine = (BOMLine)clientOccurrenceMap.get( hrnAssyOccInfo.device2_id ).bomline;
            varCondInputInfo08.clientId = "SizeCompact";
            varCondInputInfo08.operation = "Create";
            varCondInputInfo08.variantCondInfo = variantCondSizeMediumArr;

            // Device 1 - Compact
            CreateOrUpdateVariantCondInput varCondInputInfo09 = new CreateOrUpdateVariantCondInput();
            varCondInputInfo09.bomLine = (BOMLine)clientOccurrenceMap.get( hrnAssyOccInfo.device1_id ).bomline;
            varCondInputInfo09.clientId = "SizeCompact";
            varCondInputInfo09.operation = "Create";
            varCondInputInfo09.variantCondInfo = variantCondSizeCompactArr;

            // Wire 3 - Large
            CreateOrUpdateVariantCondInput varCondInputInfo10 = new CreateOrUpdateVariantCondInput();
            varCondInputInfo10.bomLine = (BOMLine)clientOccurrenceMap.get( hrnAssyOccInfo.w3_id ).bomline;
            varCondInputInfo10.clientId = "SizeCompact";
            varCondInputInfo10.operation = "Create";
            varCondInputInfo10.variantCondInfo = variantCondSizeLargeArr;

            // Wire 2 - Medium
            CreateOrUpdateVariantCondInput varCondInputInfo11 = new CreateOrUpdateVariantCondInput();
            varCondInputInfo11.bomLine = (BOMLine)clientOccurrenceMap.get( hrnAssyOccInfo.w2_id ).bomline;
            varCondInputInfo11.clientId = "SizeCompact";
            varCondInputInfo11.operation = "Create";
            varCondInputInfo11.variantCondInfo = variantCondSizeMediumArr;

            // Wire 1 - Compact
            CreateOrUpdateVariantCondInput varCondInputInfo12 = new CreateOrUpdateVariantCondInput();
            varCondInputInfo12.bomLine = (BOMLine)clientOccurrenceMap.get( hrnAssyOccInfo.w1_id ).bomline;
            varCondInputInfo12.clientId = "SizeCompact";
            varCondInputInfo12.operation = "Create";
            varCondInputInfo12.variantCondInfo = variantCondSizeCompactArr;

            // Complex Cable - Red
            CreateOrUpdateVariantCondInput varCondInputInfo13 = new CreateOrUpdateVariantCondInput();
            varCondInputInfo13.bomLine = (BOMLine)clientOccurrenceMap.get( hrnAssyOccInfo.cb2_id ).bomline;
            varCondInputInfo13.clientId = "SizeCompact";
            varCondInputInfo13.operation = "Create";
            varCondInputInfo13.variantCondInfo = variantCondColorRedArr;

            // Simple Cable - Green
            CreateOrUpdateVariantCondInput varCondInputInfo14 = new CreateOrUpdateVariantCondInput();
            varCondInputInfo14.bomLine = (BOMLine)clientOccurrenceMap.get( hrnAssyOccInfo.cb1_id ).bomline;
            varCondInputInfo14.clientId = "SizeCompact";
            varCondInputInfo14.operation = "Create";
            varCondInputInfo14.variantCondInfo = variantCondColorGreenArr;

            // Complex Shield - Red
            CreateOrUpdateVariantCondInput varCondInputInfo15 = new CreateOrUpdateVariantCondInput();
            varCondInputInfo15.bomLine = (BOMLine)clientOccurrenceMap.get( hrnAssyOccInfo.sh2_id ).bomline;
            varCondInputInfo15.clientId = "SizeCompact";
            varCondInputInfo15.operation = "Create";
            varCondInputInfo15.variantCondInfo = variantCondColorRedArr;

            // Simple Shield - Blue
            CreateOrUpdateVariantCondInput varCondInputInfo16 = new CreateOrUpdateVariantCondInput();
            varCondInputInfo16.bomLine = (BOMLine)clientOccurrenceMap.get( hrnAssyOccInfo.sh1_id ).bomline;
            varCondInputInfo16.clientId = "SizeCompact";
            varCondInputInfo16.operation = "Create";
            varCondInputInfo16.variantCondInfo = variantCondColorBlueArr;

            // Now create an array of Variant condition inputs
            varCondInputInfoArr = new CreateOrUpdateVariantCondInput[16];
            varCondInputInfoArr[0] = varCondInputInfo01;
            varCondInputInfoArr[1] = varCondInputInfo02;
            varCondInputInfoArr[2] = varCondInputInfo03;
            varCondInputInfoArr[3] = varCondInputInfo04;
            varCondInputInfoArr[4] = varCondInputInfo05;
            varCondInputInfoArr[5] = varCondInputInfo06;
            varCondInputInfoArr[6] = varCondInputInfo07;
            varCondInputInfoArr[7] = varCondInputInfo08;
            varCondInputInfoArr[8] = varCondInputInfo09;
            varCondInputInfoArr[9] = varCondInputInfo10;
            varCondInputInfoArr[10] = varCondInputInfo11;
            varCondInputInfoArr[11] = varCondInputInfo12;
            varCondInputInfoArr[12] = varCondInputInfo13;
            varCondInputInfoArr[13] = varCondInputInfo14;
            varCondInputInfoArr[14] = varCondInputInfo15;
            varCondInputInfoArr[15] = varCondInputInfo16;
        }
        catch( Exception ex )
        {
            LogFile.write( "populateVariantConditionsInput : " + ex.getMessage() );
            throw ex;
        }
        return varCondInputInfoArr;
    }

    /**
     * Display available Variant Rules information
     */
    public void displayVariantRulesInfo() throws Exception
    {
        try
        {
            if( topItemRev == null )
            {
                LogFile.write( "displayVariantRulesInfo : Invalid Item Revision" );
                return;
            }

            ItemRevision[] revArr = new ItemRevision[1];
            revArr[0] = topItemRev;
            GetVariantRulesResponse getVarResp = smService.getVariantRules( revArr );
            if ( ! whTestUtils.handleServiceData( getVarResp.serviceData, "displayVariantRulesInfo" ) )
            {
                return;
            }

            // Only interested in Variant Rules at Topline (harness Assembly)
            VariantRule[] varRulesArr = ( VariantRule[] )getVarResp.inputItemRevToVarRules.get( topItemRev );

            if( varRulesArr.length <= 0 )
            {
                return;
            }

            LogFile.write( "----------------------------------------------------" );
            LogFile.write( "Variant Rules Info" );
            LogFile.write( "----------------------------------------------------" );
            for( int inx =0; inx < varRulesArr.length; inx++ )
            {
                String vRuleName = varRulesArr[inx].get_object_string();
                LogFile.write( (inx + 1) + " : Variant Rule Name : " + vRuleName );
                VariantRule desiredVarRule = varRulesArr[inx];
                LogFile.write( "Options for " + vRuleName + " : " );
                int contSize =  desiredVarRule.get_contents().length;
                String[] vRuleContents = new String[contSize];
                vRuleContents = desiredVarRule.get_contents();
                for( int jnx = 0; jnx < contSize ; jnx++ )
                {
                    String val = vRuleContents[jnx];
                    int pos = val.lastIndexOf(')');
                    LogFile.write( val.substring( pos + 1 ) );
                }
            }
            LogFile.write( "----------------------------------------------------" );
        }
        catch( Exception ex )
        {
            LogFile.write( "displayVariantRulesInfo : " + ex.getMessage() );
            throw ex;
        }
    }
}


