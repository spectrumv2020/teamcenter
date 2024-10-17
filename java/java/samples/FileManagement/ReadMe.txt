This sample demonstrates the basic functionality of the File Management Service.
The project can be built with Java SDK 1.7.0, from the command line using Ant(1.7.0), or from
the Eclipse IDE (3.8).

Before running the sample application, you must have a Teamcenter Web Tier server
and Pool Manager up and running.

To make use of File Management, the FCC must be installed and running on your machine.
Verify that this includes the FMS_HOME environment variable being set. ALso, the FMS
native libs should be in PATH on Windows or LD_LIBRARY_PATH on Unix platforms.

From the Command line:

Prerequisite :-  You  need to have Ant set up. The Ant bin folder needs to be in your
PATH and ANT_HOME variable set to the Ant root directory.

1. Build the application using Ant
   >ant -buildfile build.xml
   
2. Execute the client application
   On Windows:
   >RunMe http://localhost:7001/tc
   On Unix platform:
   >./RunMe.sh http://localhost:7001/tc
   
   Substitute the URI of the Teamcenter Web Tier server
   In case of running in TCCS mode,instead of http://hostname:port/tc use tccs://<tccs_envname>. 
   The prerequisite to TCCS mode is the TCCS must already be up and running.

From the Eclipse IDE:
1. Add Classpath variable for the root location of the soa_client folder
   Open the Preferences dialog (Window --> Preferences ... )
   Select the Classpath Variable tab ( Java --> Build Path --> Classpath Variables)
   Add the variable 'TEAMCENTER_SERVICES_HOME', set it to the root path of the 'soa_client' folder
   Add the variable 'FMS_HOME', set it to the path in the FMS_HOME environment variable.
   
2. Import the project
   Open the Import dialog ( File --> Import... )
   Select Existing Project into Workspace (General --> Existing Projects into Workspace )
   Click Next, then browse to .../soa_client/java/sample, select Finish
   
3. Compile the project  
   If the IDE is not configured to Build Automatically, force a build of the project
   
4. Execute the client application
   Open the Debug/Run dialog ( Run --> Debug ...)
   Select the FileManagement Java Application 
   The launch is configured to connect to the server on http://localhost:7001/tc. To change this URI, either 
   Go to the Arguments tab in Run configuration and change value of host in VM Args   
   or 
   Change the value of host in "org.eclipse.jdt.launching.VM_ARGUMENTS" in FileManagement.launch file.
   
Running in TCCS mode
1. To run in TCCS mode, ensure that TCCS and its configuration files are installed and TCCS is 
   already running.

2. Set the FMS_HOME to correct value and ensure TCCS native libs are in PATH on Windows or LD_LIBRARY_PATH on Unix platforms. 

3. Use tccs://<tccs_envname> (TCCS protocol and environment name) as the 
   host URI instead of http://hostname:port/tc 
 
 
 The source file FMS.java has the main function for the application. This is the 
 best place to start browsing the code. The FileManagement class performs basic 
 File Management operations,  mostly using the FileManagementUtility class provided
 as part of the SOA Java client framework.
 
