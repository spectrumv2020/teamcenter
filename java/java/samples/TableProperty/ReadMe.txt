This sample demonstrates creation of a table rows for a custom Item that has Table property defined.
The project can be built with Java SDK 1.7.0, from the command line using Ant(1.7.0), or from
the Eclipse IDE (3.8).

Before running the sample application, you must have a Teamcenter Web Tier server
and Pool Manager up and running and your test database must contain the sample TableRow Business Object template.  This
template can be found at .../soa_clients/java/samples/TableProperty/businessdata.  Use the Business Modeler IDE 
to import the template and deploy to your test database.  Detailed instructions for importing 
a template, working with extensions and deploying changes to a database are available in Business Modeler IDE Help.

From the Command line

Prerequisite :-  You need to have Ant set up. The Ant bin folder needs to be in your
PATH and ANT_HOME variable pointing to the Ant home directory. Also, make sure JAVA_HOME 
is set to the location of the JDK. TEAMCENTER_SERVICES_HOME should be set to the location
of the SOA client kit.

1. Build the application using Ant
   >ant -buildfile build.xml
   
2. Execute the client application
   On Windows:
   >RunMe http://localhost:7001/tc
   On Unix platform:
   >./RunMe.sh http://localhost:7001/tc
   
   Substitute the URI of the Teamcenter Web Tier server
   In case of running in TCCS mode,instead of http://hostname:port/tc use tccs://<tccs_envname>
   
From the Eclipse IDE
1. Add ClassPath variable for the root location of the soa_client folder
   Open the Preferences dialog (Window --> Preferences ... )
   Select the ClassPath Variable tab ( Java --> Build Path --> ClassPath Variables)
   Add the variable 'TEAMCENTER_SERVICES_HOME', set it to the root path of the 'soa_client' folder

2. Import the project
   Open the Import dialog ( File --> Import... )
   Select Existing Project into Workspace (General --> Existing Projects into Workspace )
   Click Next, then browse to .../soa_client/java/sample, select Finish

3. Compile the project  
   If the IDE is not configured to Build Automatically, force a build of the project

4. Execute the client application
   Open the Debug/Run dialog ( Run --> Debug ...)
   Select the TableProperty Java Application 
   The launch is configured to connect to the server on http://localhost:7001/tc. To change this URI, either 
   Go to the Arguments tab in Run configuration and change value in VM Args   
   or 
   Change the value of "org.eclipse.jdt.launching.VM_ARGUMENTS" in TableProperty.launch file .
   
Running in TCCS mode
1. To run in TCCS mode, ensure that TCCS and its configuration files are installed and TCCS is 
   already running.

2. Set the FMS_HOME to correct value and ensure TCCS native libs are in PATH on Windows or LD_LIBRARY_PATH on Unix platforms. 

3. Use tccs://<tccs_envname> (TCCS protocol and environment name) as the 
   host URI instead of http://hostname:port/tc 

 
 The source file TablePropertyMain.java has the main function for the application. This is the 
 best place to start browsing the code. There are several classes prefixed with 
 the name AppX (AppXCredentialManager). These classes are implementations of 
 Teamcenter Services framework interfaces. The remaining classes in this sample 
 use a few service operations to demonstrate some basic features of TableProperty.
 
     TableProperty          This class creates and sets table property values on a Custom Item, 
                            updates attribute values for table rows and deletes selected table rows
     
 
 
 
 
