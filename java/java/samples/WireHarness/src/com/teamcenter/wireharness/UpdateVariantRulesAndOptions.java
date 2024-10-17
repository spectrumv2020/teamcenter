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
import com.teamcenter.soa.exceptions.NotLoadedException;
import com.teamcenter.wireharness.HarnessClientIDInfo.HRNObjInfo;
import com.teamcenter.wireharness.HarnessOccBomMapInfo.HarnessStructureData;

public class UpdateVariantRulesAndOptions
{
    protected StructureManagementService smService = null;

    private CreateHarness createHarnessObj = null;
    private ItemRevision topItemRev = null;
    private BOMLine topLine = null;
    private HarnessObjects hrn_struct = null;
    private Map<String, HRNObjInfo> clientOccurrenceMap = null;
    private HarnessClientIDInfo hrnAssyOccInfo = null;
    private HarnessStructureData hrnStructData = null;
    private ClassicOptionInfo[] optionsInfoArrayColorAdd = null;
    private ClassicOptionInfo[] optionsInfoArraySizeReplace = null;
    private WireHarnessTestUtils whTestUtils = null;

    /**
     * Constructor
     */
    UpdateVariantRulesAndOptions( CreateHarness createHarnObj )
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
     * This method will Add a new Option Value "LimeGreen" to the "color" option,
     * Replace the "Medium" value of "Size" option with "UltraMedium"
     * It will also add one more variant rule, VR_UMedLGreen (Size=UltraMedium, Color=LimeGreen)
     * Finally update the Simple Cable to have Color=LimeGreen and update the
     * components having Size=Medium condition to Size=UltraMedium
     */
    public void updateVariantData() throws Exception
    {
        try
        {
            // ************************
            // First update the Option
            // ************************
            ServiceData serviceDataForOptions = updateOptions();
            if ( ! whTestUtils.handleServiceData( serviceDataForOptions, "updateVariantData" ) )
            {
                return;
            }
            LogFile.write(" updateVariantData : Option values updated successfully" );

            // Now lets update the variant rule
            // Add a new variant rule
            // VR_UMedLGreen  (Size=UltraMedium, Color=LimeGreen)

            ClassicOptionInfo cOptInfoObjColorNew = optionsInfoArrayColorAdd[0];
            ClassicOptionInfo cOptInfoObjSizeReplace = optionsInfoArraySizeReplace[0];

            // Create a new Variant Rule with Size=UltraMedium and Color=LimeGreen
            OptionsInfo[] optsInfoArrVR4 = new OptionsInfo[2];
            optsInfoArrVR4[0] = this.populateOptsInfoForVarRule(
                                        topItemRev, cOptInfoObjSizeReplace.optionName,
                                        cOptInfoObjSizeReplace.values[0] ); //size = UltraMedium
            optsInfoArrVR4[1] = this.populateOptsInfoForVarRule(
                                        topItemRev, cOptInfoObjColorNew.optionName,
                                        cOptInfoObjColorNew.values[0] );    //color= LimeGreen

            CreateVariantRulesInfo varRulesInfo = new CreateVariantRulesInfo();
            varRulesInfo.clientID = "varRulesInfoVR4";
            varRulesInfo.options = optsInfoArrVR4;
            varRulesInfo.parent = null;
            varRulesInfo.relation = "IMAN_reference";
            varRulesInfo.rev = topItemRev;
            varRulesInfo.vruleDescription = "Variant rule : VR_UMedLGreen";
            varRulesInfo.vruleName = "VR_UMedLGreen";

            CreateVariantRulesInfo[] varRulesInfoArr = new CreateVariantRulesInfo[1];
            varRulesInfoArr[0] = varRulesInfo;

            CreateVariantRulesResponse cVarRulesResp = smService.createVariantRules( varRulesInfoArr );
            if ( ! whTestUtils.handleServiceData( cVarRulesResp.serviceData, "updateVariantData" ) )
            {
                return;
            }
            LogFile.write(" updateVariantData : Variant Rules updated successfully" );

            // Now update the variant conditions. After updation, it should look as follows.
            // ------------------------------------
            //     Name         Variant Condition
            // ------------------------------------
            // connector0003    Size=Large
            // connector0002    Size=UltraMedium
            // connector0001    Size=Compact
            // connection0003   Size=Large
            // connection0002   Size=UltraMedium
            // connection0001   Size=Compact
            // device0003       Size=Large
            // device0002       Size=UltraMedium
            // device0001       Size=Compact
            // Wire0003         Size=Large
            // Wire0002         Size=UltraMedium
            // Wire0001         Size=Compact
            // Complex Cable    Color=Red
            // Simple Cable     Color=LimeGreen
            // Complex Shield   Color=Red
            // Simple Shield    Color=Blue

            CreateOrUpdateVariantCondInput[] varCondInputInfoArr = this.populateVariantConditionsInput();

            ServiceData varCondSvcData =
                smService.createOrUpdateVariantConditions2( varCondInputInfoArr );
            if ( ! whTestUtils.handleServiceData( varCondSvcData, "updateVariantData" ) )
            {
                return;
            }
            LogFile.write("updateVariantData : Variant Conditions updated successfully" );
        }
        catch( Exception ex )
        {
            LogFile.write( "updateVariantData : " + ex.getMessage() );
            throw ex;
        }
    }

    /**
     * Updates the Existing Options
     * @return
     */
    private ServiceData updateOptions() throws Exception
    {
        ServiceData serviceDataForOptions = null;
        try
        {
            // Color option
            // Add
            ClassicOptionInfo cOptInfoObjColorAdd = new ClassicOptionInfo();
            cOptInfoObjColorAdd.optionName = "Color";
            cOptInfoObjColorAdd.optionDesc = "Color Option for Wire Harness Structure";
            cOptInfoObjColorAdd.values =  new String[1];
            cOptInfoObjColorAdd.values[0] = new String("LimeGreen");   //this value will be newly added

            optionsInfoArrayColorAdd = new ClassicOptionInfo[1];
            optionsInfoArrayColorAdd[0] = cOptInfoObjColorAdd;

            // Replace
            ClassicOptionInfo cOptInfoObjSizeReplace = new ClassicOptionInfo();
            cOptInfoObjSizeReplace.optionName = "Size";
            cOptInfoObjSizeReplace.optionDesc = "Size Option for Wire Harness Structure Replaced";
            // Large will be replaced by XLarge
            cOptInfoObjSizeReplace.existingValues = new String[1];
            cOptInfoObjSizeReplace.existingValues[0] = new String("Medium");
            cOptInfoObjSizeReplace.values = new String[1];
            cOptInfoObjSizeReplace.values[0] = new String("UltraMedium");

            optionsInfoArraySizeReplace = new ClassicOptionInfo[1];
            optionsInfoArraySizeReplace[0] = cOptInfoObjSizeReplace;

            // Now create Classic Options Input object
            // Add one new option
            CreateUpdateClassicOptionsInput optionsInputColorAdd =
                                        new CreateUpdateClassicOptionsInput();
            optionsInputColorAdd.clientId = "whOptsNewColorAdded";
            optionsInputColorAdd.bomLine = topLine;
            optionsInputColorAdd.operation = "AddValue";    //enum
            optionsInputColorAdd.options = optionsInfoArrayColorAdd;

            // And Replace one exiting option with new one.
            CreateUpdateClassicOptionsInput optionsInputSizeReplace =
                new CreateUpdateClassicOptionsInput();
            optionsInputSizeReplace.clientId = "whOptsExistingSizeReplaced";
            optionsInputSizeReplace.bomLine = topLine;
            optionsInputSizeReplace.operation = "ReplaceValue"; //enum
            optionsInputSizeReplace.options = optionsInfoArraySizeReplace;

            // Now create Classic options input array
            CreateUpdateClassicOptionsInput[] createOptsInputArr =
                                        new CreateUpdateClassicOptionsInput[2];
            createOptsInputArr[0] = optionsInputColorAdd;
            createOptsInputArr[1] = optionsInputSizeReplace;

            // Update classic options (and option Set) to have one newly
            // added option value ( LimeGreen ) and one Size option value replaced
            // with new value (from Medium to UltraMedium)
            serviceDataForOptions =  smService.createOrUpdateClassicOptions( createOptsInputArr );
        }
        catch( Exception ex )
        {
            LogFile.write( "updateOptions : " + ex.getMessage() );
            throw ex;
        }
        return serviceDataForOptions;
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
     * Populate Variant Conditions Input information
     */
    private CreateOrUpdateVariantCondInput[] populateVariantConditionsInput()
                                                                throws Exception
    {
        CreateOrUpdateVariantCondInput[] varCondInputInfoArr = null;
        try
        {
            ClassicOptionInfo cOptInfoObjSizeReplace = new ClassicOptionInfo();
            ClassicOptionInfo cOptInfoObjColorAdd = new ClassicOptionInfo();
            cOptInfoObjSizeReplace = optionsInfoArraySizeReplace[0];
            cOptInfoObjColorAdd = optionsInfoArrayColorAdd[0];

            // Color = Orange
            VariantCondInfo variantCondColorOrange = new VariantCondInfo();
            variantCondColorOrange.optionName = cOptInfoObjColorAdd.optionName;
            variantCondColorOrange.joinOperator = "AND";
            variantCondColorOrange.compOperator = "EQ";
            variantCondColorOrange.value = cOptInfoObjColorAdd.values[0];

            VariantCondInfo[] variantCondColorOrangeArr = new VariantCondInfo[1];
            variantCondColorOrangeArr[0] = variantCondColorOrange;

            // Size = XLarge
            VariantCondInfo variantCondSizeXLarge = new VariantCondInfo();
            variantCondSizeXLarge.optionName = cOptInfoObjSizeReplace.optionName;
            variantCondSizeXLarge.joinOperator = "AND";
            variantCondSizeXLarge.compOperator = "EQ";
            variantCondSizeXLarge.value = cOptInfoObjSizeReplace.values[0];

            VariantCondInfo[] variantCondSizeXLargeArr = new VariantCondInfo[1];
            variantCondSizeXLargeArr[0] = variantCondSizeXLarge;


            // Complex Shield - Red to Orange
            CreateOrUpdateVariantCondInput varCondInputInfoOra = new CreateOrUpdateVariantCondInput();
            varCondInputInfoOra.bomLine = (BOMLine)clientOccurrenceMap.get( hrnAssyOccInfo.cb1_id ).bomline;
            varCondInputInfoOra.clientId = "ColorLimeGreen";
            varCondInputInfoOra.operation = "Update";   //enum
            varCondInputInfoOra.variantCondInfo = variantCondColorOrangeArr;

            //Connector 3 - Large to XLarge
            CreateOrUpdateVariantCondInput varCondInputInfoSize01 = new CreateOrUpdateVariantCondInput();
            varCondInputInfoSize01.bomLine = (BOMLine)clientOccurrenceMap.get( hrnAssyOccInfo.ctr2_id ).bomline;
            varCondInputInfoSize01.clientId = "SizeUltraMedium";
            varCondInputInfoSize01.operation = "Create";
            varCondInputInfoSize01.variantCondInfo = variantCondSizeXLargeArr;

            // Connection 3 - Large to XLarge
            CreateOrUpdateVariantCondInput varCondInputInfoSize02 = new CreateOrUpdateVariantCondInput();
            varCondInputInfoSize02.bomLine = (BOMLine)clientOccurrenceMap.get( hrnAssyOccInfo.conn2_id ).bomline;
            varCondInputInfoSize02.clientId = "SizeUltraMedium";
            varCondInputInfoSize02.operation = "Create";
            varCondInputInfoSize02.variantCondInfo = variantCondSizeXLargeArr;

            // Device 3 - Large to XLarge
            CreateOrUpdateVariantCondInput varCondInputInfoSize03 = new CreateOrUpdateVariantCondInput();
            varCondInputInfoSize03.bomLine = (BOMLine)clientOccurrenceMap.get( hrnAssyOccInfo.device2_id ).bomline;
            varCondInputInfoSize03.clientId = "SizeUltraMedium";
            varCondInputInfoSize03.operation = "Create";
            varCondInputInfoSize03.variantCondInfo = variantCondSizeXLargeArr;

            // Wire 3 - Large to XLarge
            CreateOrUpdateVariantCondInput varCondInputInfoSize04 = new CreateOrUpdateVariantCondInput();
            varCondInputInfoSize04.bomLine = (BOMLine)clientOccurrenceMap.get( hrnAssyOccInfo.w2_id ).bomline;
            varCondInputInfoSize04.clientId = "SizeUltraMedium";
            varCondInputInfoSize04.operation = "Create";
            varCondInputInfoSize04.variantCondInfo = variantCondSizeXLargeArr;

            varCondInputInfoArr = new CreateOrUpdateVariantCondInput[5];
            varCondInputInfoArr[0] = varCondInputInfoOra;
            varCondInputInfoArr[1] = varCondInputInfoSize01;
            varCondInputInfoArr[2] = varCondInputInfoSize02;
            varCondInputInfoArr[3] = varCondInputInfoSize03;
            varCondInputInfoArr[4] = varCondInputInfoSize04;
        }
        catch( Exception ex )
        {
            LogFile.write( "populateVariantConditionsInput : " + ex.getMessage() );
            throw ex;
        }
        return varCondInputInfoArr;
    }

    /**
     * Display Variant Rules Information
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
                LogFile.write( "displayVariantRulesInfo : No Variant Rules found." );
                return;
            }

            LogFile.write("----------------------------------------------------");
            LogFile.write("Variant Rules Info");
            LogFile.write("----------------------------------------------------");
            for( int inx =0; inx < varRulesArr.length; inx++ )
            {
                try
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
                catch( NotLoadedException nlEx )
                {
                    LogFile.write( "displayVariantRulesInfo : " + nlEx.getMessage() );
                }
            }
            LogFile.write("----------------------------------------------------");
        }
        catch( Exception ex )
        {
            LogFile.write( "displayVariantRulesInfo : " + ex.getMessage() );
            throw ex;
        }
    }
}

