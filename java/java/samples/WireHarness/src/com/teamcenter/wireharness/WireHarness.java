//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================


package com.teamcenter.wireharness;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.teamcenter.clientx.WHSession;

public class WireHarness
{
    /**
     * @param args
     */
    public static void main( String[] args )
    {
        WHSession session = null;
        try
        {
            if ( args.length > 0 )
            {
                if (args[0].equals( "-help" ) || args[0].equals( "-h" ) )
                {
                    System.out.println( "usage: java [-Dhost=http://server:port/tc] com.teamcenter.wireharness.WireHarness" );
                    System.exit(0);
                }
            }

            // Get optional host information
            String serverHost = "http://localhost:7001/tc";
            String host = System.getProperty( "host" );
            if ( host != null && host.length() > 0 )
            {
                serverHost = host;
            }

            session = new WHSession( serverHost );

            LogFile.getInstance();
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "---------------------Login Test-----------------------------------" );

            // Establish a session with the Teamcenter Server
            session.login();
            LogFile.write( "Login successful.");
            LogFile.write( "------------------------------------------------------------------" );

            String pauseStr = System.getProperty( "Wire_Harness_Client_Pause_After_Each_UseCase" );
            if (pauseStr == null)
            {
                pauseStr = System.getenv("Wire_Harness_Client_Pause_After_Each_UseCase");
            }
            // Pause for user response ( Enter )
            WireHarness whObj = new WireHarness();
            whObj.pauseBetweenUseCases( pauseStr );

            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "                   Executing Usecase1" );

            // Create max complexity harness structure
            CreateHarness hrn_assy = new CreateHarness();
            hrn_assy.createHarnessStructure();

            LogFile.write( "\nUsecase1: Creation of Max complexity harness structure successful." );

            // Pause for user input ( Enter )
            whObj.pauseBetweenUseCases( pauseStr );

            // Create classic Options, Variant conditions and Variant rules
            CreateVariantRulesAndOptions createVarsAndOpts = new CreateVariantRulesAndOptions( hrn_assy );
            createVarsAndOpts.createVariantData();
            LogFile.write( "\nUsecase1: Adding variants on max complexity harness structure successful." );
            LogFile.write( "                    Usecase1 successful." );
            LogFile.write( "------------------------------------------------------------------" );

            // Pause for user input ( Enter )
            whObj.pauseBetweenUseCases( pauseStr );


            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "                   Executing Usecase2" );

            createVarsAndOpts.displayVariantRulesInfo();

            // Following are the variant rules that should've got created.
            //      VR_CompactRed
            //      VR_MediumGreen
            //      VR_LargeBlue
            // for getting the configured harness structure for any of
            //  them, set the value accordingly
            String vRuleNameAfterCreate = "VR_MediumGreen";
            // Get the configured Structure for Variant Rule - VR_MediumGreen
            GetConfiguredHarness getConfHarn1 = new GetConfiguredHarness( hrn_assy, vRuleNameAfterCreate );
            getConfHarn1.configureHarnessStructure();
            LogFile.write( "\nUsecase2: Get configured harness structure successful for variants: Size = Medium and Color = Green." );
            LogFile.write( "                    Usecase2 successful." );
            LogFile.write( "------------------------------------------------------------------" );

            // Pause for user input ( Enter )
            whObj.pauseBetweenUseCases( pauseStr );

            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "                   Executing Usecase3" );

            // Update variant data such as Options, Variant Rules and Variant conditions
            UpdateVariantRulesAndOptions updVarsAndOpts = new UpdateVariantRulesAndOptions( hrn_assy );
            updVarsAndOpts.updateVariantData();
            LogFile.write( "\nUsecase3: Update variants successful. Size changed from Medium to UltraMedium and  Color = LimeGreen added anew." );

            updVarsAndOpts.displayVariantRulesInfo();

            // Now with the variant data updated, get the configured
            //  structure based on this new variant configuration
            String vRuleNameAfterUpdate = "VR_UMedLGreen";
            GetConfiguredHarness getConfHarn2 = new GetConfiguredHarness( hrn_assy, vRuleNameAfterUpdate );
            getConfHarn2.configureHarnessStructure();
            LogFile.write( "\nUsecase3: Get configured harness structure successful for updated variants. Size=UltraMedium and Color=LimeGreen." );

            // Now update the harness structure
            // Remove Connector3, Connection3, Device3, Wire3, Connector6 and also the relations based on these components
            // Add Connector4, Connection4, Device4, Wire4 and add new relations as well
            UpdateHarness updHarn = new UpdateHarness( hrn_assy );
            updHarn.updateHarnessStructure();
            LogFile.write( "\nUsecase3: Update harness structure successful " +
                                            "(Removed connector0003, device0003, Wire0003 and connection0003 and associated relations. " +
                                            "Added connector0007, device0004, connection0004, Wire0004)" );

            LogFile.write( "                    Usecase3 successful.\n" );
            LogFile.write( "------------------------------------------------------------------\n" );

            // Pause for user input ( Enter )
            whObj.pauseBetweenUseCases( pauseStr );

            LogFile.write( "------------------------------------------------------------------\n" );
            LogFile.write( "                   Executing Usecase4\n" );

            // Create allocations between the connectors of harness and mechanical assemblies
            CreateRelStruct mech_assy = new CreateRelStruct();
            mech_assy.createMechStructure();
            ManageAllocations alloc = new ManageAllocations( hrn_assy, mech_assy );
            alloc.createAllocations();
            LogFile.write( "\nUsecase4: Creation of allocations between connectors of mechanical and harness structures successful." );

            // Pause for user input ( Enter )
            whObj.pauseBetweenUseCases( pauseStr );

            // Remove one allocations out of the two created.
            alloc.removeAllocations();
            LogFile.write( "\nUsecase4: Removal of one allocation between connectors of mechanical and harness structures successful." );

            // Close all the opened bom windows
            alloc.closeBomWindows();
            LogFile.write( "                    Usecase4 successful." );
            LogFile.write( "------------------------------------------------------------------" );
            // Pause for user input ( Enter )
            whObj.pauseBetweenUseCases( pauseStr );

            LogFile.write( "------------------------------------------------------------------\n" );
            LogFile.write( "                   Executing Signal Associations Tests \n" );
            SignalAssociations signalAsoc = new SignalAssociations();
            LogFile.write( "                   Executing Usecase5\n" );

            signalAsoc.testInStrucAssocSignalToSource();
            LogFile.write( "\nUsecase5: Association/Removal of Signal to/from Source succeeded." );
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "                   Executing Usecase6\n" );

            signalAsoc.testInStrucAssocSignalToTarget();
            LogFile.write( "\nUsecase6: Association/Removal of Signal to/from Target succeeded." );
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "                   Executing Usecase7\n" );

            signalAsoc.testInStrucAssocSignalToTransmitter();
            LogFile.write( "\nUsecase7: Association/Removal of Signal to/from tranmitter succeeded." );
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "                   Executing Usecase8\n" );

            signalAsoc.testInStrucAssocSignalToProcessVariable();
            LogFile.write( "\nUsecase8: Association/Removal of Signal to/from Process Variable succeeded" );
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( "                   Executing Usecase9\n" );

            signalAsoc.testInStrucAssocRedundantSignal();
            LogFile.write( "\nUsecase9: Association/Removal of Signal to/from Redundant Signal succeeded " );
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.write( " ------------------------Logout Test------------------------------" );

            // Terminate the session with the Teamcenter server
            session.logout();

            LogFile.write( "Log out success. Session terminated successfully." );
            LogFile.write( "------------------------------------------------------------------" );
            LogFile.close();
        }
        catch( Exception ex )
        {
            session.logout();
            LogFile.getInstance();
            LogFile.write( "Wireharness usecases execution failed." );
            LogFile.close();
        }
    }

    /**
     * Makes the flow to 'pause' between the use-cases
     * @param pauseStr
     * @throws Exception
     */
    public void pauseBetweenUseCases( String pauseStr ) throws Exception
    {
        try
        {
            if( pauseStr != null )
            {
                if( pauseStr.equals( "true" ) )
                {
                    BufferedReader stdin =
                        new BufferedReader( new InputStreamReader( System.in ) );

                    System.out.println( "Press Enter to continue." );
                    stdin.read();
                }
            }
        }
        catch( Exception ex )
        {
            LogFile.write( "pauseBetweenUseCases : " + ex.getMessage() );
            throw ex;
        }
    }
}
