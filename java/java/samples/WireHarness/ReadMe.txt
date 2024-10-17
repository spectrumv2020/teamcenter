This sample demonstrates the basic functionality of the Wire Harness Service.
The project can be built with Java SDK 1.7.0, from the command line using Ant(1.7.0), or from
the Eclipse IDE (3.8).

Before running the sample application, ensure that Wire Harness Configuration is installed
(This is an option during TEM install). You must have a Teamcenter Web Tier server
and Pool Manager up and running in case of 4tier. If you are using 2 tier, Teamcenter
server should be running.

To make use of Wire Harness, the FCC must be installed and running on your machine.
Verify that this includes the FMS_HOME environment variable being set.

From the Command line
1. Build the application using Ant
   >ant -buildfile build.xml
2. Execute the client application
   >RunMe http://localhost:7001/tc
   Substitute the URI of the Teamcenter Web Tier server
3. By default, output is written to console. If the output needs to be in a log file, set the following environment varaible to a file (with full path)
   Wire_Harness_SOA_Sample_Client_Log_File
4. To pause the sample client after each use case and wait for user interaction, set the following environment variable to true
   Wire_Harness_Client_Pause_After_Each_UseCase

From the Eclipse IDE
1. Add ClassPath variable for the root location of the soa_client folder
   Open the Preferences dialog (Window --> Preferences ... )
   Select the ClassPath Variable tab ( Java --> Build Path --> ClassPath Variables)
   Add the variable 'TEAMCENTER_SERVICES_HOME', set it to the root path of the 'soa_client' folder
   Add the variable 'FMS_HOME', set it to the path in the FMS_HOME environment variable.
2. Import the project
   Open the Import dialog ( File --> Import... )
   Select Existing Project into Workspace (General --> Existing Projects into Workspace )
   Click Next, then browse to .../soa_client/java/samples/WireHarness, select Finish
3. Compile the project  
   If the IDE is not configured to Build Automatically, force a build of the project
4. Execute the client application
   Open the Debug/Run dialog ( Run --> Debug ...). Ensure the following:
   Project : WireHarness
   Main class : com.teamcenter.wireharness.WireHarness
 
5. The launch is configured to connect to the server on http://localhost:7001/tc. To change:
   Click Arguments tab and add the following under VM Arguments
   -Dhost="serverlocation"

6. By default, the messages are printed to console. To print the messages to a logfile,
   add the following under VM Arguments
   -DWire_Harness_SOA_Sample_Client_Log_File="logfilenamewithfullpath"

7. To pause the sample client after each use case and wait for user interaction, add the following command
   under VM Arguments
   -DWire_Harness_Client_Pause_After_Each_UseCase=true
       
The source file WireHarness.java has the main function for the application. This is the 
best place to start browsing the code. The remaining classes in this sample demonstrate some basic features of 
Teamcenter Wire Harness Services. 
