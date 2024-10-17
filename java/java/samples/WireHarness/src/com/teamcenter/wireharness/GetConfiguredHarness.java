//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================


package com.teamcenter.wireharness;

import com.teamcenter.clientx.WHSession;
import com.teamcenter.schemas.soa._2006_03.exceptions.ServiceException;
import com.teamcenter.services.strong.cad.StructureManagementService;
import com.teamcenter.services.strong.cad._2007_01.StructureManagement.CreateBOMWindowsInfo;
import com.teamcenter.services.strong.cad._2007_01.StructureManagement.CreateBOMWindowsResponse;
import com.teamcenter.services.strong.cad._2007_01.StructureManagement.GetVariantRulesResponse;
import com.teamcenter.services.strong.core.SessionService;
import com.teamcenter.soa.client.model.strong.BOMWindow;
import com.teamcenter.soa.client.model.strong.ItemRevision;
import com.teamcenter.soa.client.model.strong.VariantRule;
import com.teamcenter.wireharness.HarnessOccBomMapInfo.HarnessStructureData;

public class GetConfiguredHarness
{
    protected StructureManagementService smService = null;

    private CreateHarness createHarnObj = null;
    private ItemRevision topItemRev = null;
    private String varRuleNameForConfig = null;
    private WireHarnessTestUtils whTestUtils = null;

    /**
     * Constructor
     * @param createHarnObj
     * @param varRulesAndOptsObj
     */
    public GetConfiguredHarness( CreateHarness createHarnObj,
                                    String vRuleName )
    {
        smService = StructureManagementService.getService( WHSession.getConnection() );

        this.createHarnObj = createHarnObj;
        this.varRuleNameForConfig = vRuleName;
        setObjectPropPolicy();
        whTestUtils = new WireHarnessTestUtils();
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
     * Gets the configured structure based on specified variant rule name
     * The structure will be configured for that Variant rule.
     */
    public void configureHarnessStructure() throws Exception
    {
        try
        {
            topItemRev = createHarnObj.getHarnessStructureData().getHarnessRevision();
            if( topItemRev == null )
            {
                LogFile.write( "configureHarnessStructure : Invalid Item Revision" );
                return;
            }

            ItemRevision[] revArr = new ItemRevision[1];
            revArr[0] = topItemRev;
            GetVariantRulesResponse getVarResp =  smService.getVariantRules( revArr );
            if ( ! whTestUtils.handleServiceData( getVarResp.serviceData, "configureHarnessStructure" ) )
            {
                return;
            }

            // Only interested in Variant Rules at Topline (harness Assembly)
            VariantRule[] varRulesArr = ( VariantRule[] )getVarResp.inputItemRevToVarRules.get( topItemRev );

            VariantRule desiredVarRule = null;
            boolean matchFound = false;
            for( int inx =0; inx < varRulesArr.length; inx++ )
            {
                String vRuleName = varRulesArr[inx].get_object_string();
                if ( vRuleName.equals( varRuleNameForConfig ) )
                {
                    desiredVarRule = varRulesArr[inx];
                    matchFound = true;
                    break;
                }
            }

            if( matchFound )
            {
                CreateBOMWindowsInfo bomInfo = new CreateBOMWindowsInfo();
                bomInfo.itemRev = topItemRev;
                bomInfo.objectForConfigure = desiredVarRule; //Variant Rule

                CreateBOMWindowsInfo[] bomInfoArr = new CreateBOMWindowsInfo[1];
                bomInfoArr[0] = bomInfo;
                CreateBOMWindowsResponse resp = smService.createBOMWindows( bomInfoArr );
                if ( ! whTestUtils.handleServiceData( resp.serviceData, "configureHarnessStructure" ) )
                {
                    return;
                }
                BOMWindow bomWindow = resp.output[0].bomWindow;

                HarnessOccBomMapInfo hrnOccInfo = new HarnessOccBomMapInfo();
                HarnessStructureData hrnStructData = hrnOccInfo.getHarnessStructure( bomWindow );

                LogFile.write( "Total no of lines in configured structure : " +
                                                    hrnStructData.getNoOfConfiguredLines() );
            }
        }
        catch( Exception ex )
        {
            LogFile.write( "configureHarnessStructure : " + ex.getMessage() );
            throw ex;
        }
    }
}
