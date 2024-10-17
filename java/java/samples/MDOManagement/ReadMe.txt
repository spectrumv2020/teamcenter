This sample demonstrates the basic functionality of the Multi-Disciplinary Objects.
The project can be built with Java SDK 1.7.0, from the command line using Ant(1.7.0), or from
the Eclipse IDE (3.8).

Before running the sample application, ensure that Multi-Disciplinary Associations is installed along with 4th Generation Design, 4th Generation Design Change Management Integration, Change Management, Classification, Library Management features.  (These are the options during TEM install).
You must have a Teamcenter Web Tier server and Pool Manager up and running in case of 4tier.
If you are using 2 tier, Teamcenter server should be running.

To make use of MDOManagement, the FCC must be installed and running on your machine.
Verify that this includes the FMS_HOME environment variable being set.

Add subscriptions for Cpd0DesignElement, Ptn0Functional, and Rlz0RealizationContainer.
Refer to MDO Documentation for the list of classes and event types, and the
utility to add these subscriptions.
Subscription manager must be configured and be running for the notifications to be created.
Preference TC_subscription must be set with the value:
ON
Preference TC_subscriptionmgrd_sleep_minutes must be set with the value:
1
Performance testing will require the preference TC_subscriptionmgrd_max_subscriptions_to_dispatch
to be set with the value:
499  

Preference MDL_enable_preproduction_promote_to_history must be set to the value:
true

Domain preferences must be modified:
MECH_domain_types_Mechanical = Part Revision
MECH_domain_types_Automation = PSConnectionRevision, PSSignalRevision

ECN preference must be modified:
CM_change_space_enabled = true

Do not use infodba to login because it can't create a workflow to apply the release status,
you will need a login id in the dba group that has the following Authorization Rules:
MDOAdministration, MDIAdministration, MDONotificationAdministration
This login id should also be assigned the Project Administrator role.


From the Command line
Prerequisite :-  You need to have Ant set up. The Ant bin folder needs to be in your
PATH and ANT_HOME variable pointing to the Ant home directory.
1. Change directory to MDOManagement folder and build the application using Ant
   >ant -buildfile build.xml
   
2. Execute the client application
   >RunMe http://localhost:7001/tc
   Substitute the URI of the Teamcenter Web Tier server
   
3. By default, output is written to console. If the output needs to be in a log file,
   set the following environment variable to a file (with full path)
   MDOManagement_SOA_Sample_Client_Log_File. 
      
4. To pause the sample client after each use case and wait for user interaction,
   set the following environment variable to true
   MDOManagement_Client_Pause_After_Each_UseCase

5. To perform only performance testing, set the following environment variable to true,
   otherwise performance testing is skipped:
   MDOManagement_Client_Performance_Testing

From the Eclipse IDE
1. Add ClassPath variable for the root location of the soa_client folder
   Open the Preferences dialog (Window --> Preferences ... )
   Select the ClassPath Variable tab ( Java --> Build Path --> ClassPath Variables)
   Add the variable 'TEAMCENTER_SERVICES_HOME', set it to the root path of the 'soa_client' folder
   Add the variable 'FMS_HOME', set it to the path in the FMS_HOME environment variable.
 
2. Import the project
   Open the Import dialog ( File --> Import... )
   Select Existing Project into Workspace (General --> Existing Projects into Workspace )
   Click Next, then browse to .../soa_client/java/samples/MDOManagement, select Finish
   
3. Compile the project  
   If the IDE is not configured to Build Automatically, force a build of the project
   
4. Execute the client application
   Open the Debug/Run dialog ( Run --> Debug ...). Ensure the following:
   Project : MDOManagement
   Main class : com.teamcenter.mdomanagement.MDOManagement
 
5. The launch is configured to connect to the server on http://localhost:7001/tc. To change:
   Click Arguments tab and add the following under VM Arguments
   -Dhost="serverlocation"

6. By default, the messages are printed to console. To print the messages to a logfile,
   add the following under VM Arguments
   -DMDOManagement_SOA_Sample_Client_Log_File="logfilenamewithfullpath"

7. To pause the sample client after each use case and wait for user interaction,
   add the following command under VM Arguments
   -DMDOManagement_Client_Pause_After_Each_UseCase=true

8. To perform only performance testing, add the following command under VM Arguments,
   otherwise performance testing is skipped:
   -DMDOManagement_Client_Performance_Testing=true
   
The source file MDOManagement.java has the main function for the application. This is the 
best place to start browsing the code. The remaining classes in this sample demonstrate some basic features of 
Teamcenter MDOManagement. 
