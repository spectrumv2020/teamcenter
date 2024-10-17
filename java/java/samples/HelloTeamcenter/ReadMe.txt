This sample demonstrates the basic functionality of the Teamcenter Services.
The project can be built with Java SDK 1.7.0, from the command line using Ant(1.7.0), or from
the Eclipse IDE (3.8).

Before running the sample application, you must have a Teamcenter Web Tier server
and Pool Manager up and running.

From the Command line

Prerequisite :-  You need to have Ant set up. The Ant bin folder needs to be in your
PATH and ANT_HOME variable pointing to the Ant home directory.

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
   Select the HelloTeamcenter Java Application 
   The launch is configured to connect to the server on http://localhost:7001/tc. To change this URI, either 
   Go to the Arguments tab in Run configuration and change value in VM Args   
   or 
   Change the value of "org.eclipse.jdt.launching.VM_ARGUMENTS" in HelloTeamcenter.launch file .
   
Running in TCCS mode
1. To run in TCCS mode, ensure that TCCS and its configuration files are installed and TCCS is 
   already running.

2. Set the FMS_HOME to correct value and ensure TCCS native libs are in PATH on Windows or LD_LIBRARY_PATH on Unix platforms. 

3. Use tccs://<tccs_envname> (TCCS protocol and environment name) as the 
   host URI instead of http://hostname:port/tc 

 
 The source file Hello.java has the main function for the application. This is the 
 best place to start browsing the code. There are several classes prefixed with 
 the name AppX (AppXCredentialManager). These classes are implementations of 
 Teamcenter Services framework interfaces. Each client application is responsible 
 for providing an implementation to these interfaces:
 
     CredentialManager       Used by the framework to re-authenticate as user.
     ExceptionHandler        Provide a mechanism for the client application to
                             handle low level exception in the communication framework.
     PartialErrorListener    Optional listener for notification when a service operation
                             has returned partial errors.
     ModelEventListener      Optional listener for notification when a service operation
                             has returned ModelObject with updated property values, 
                             or when objects have been deleted from the database.

 
 The remaining classes in this sample use a few service operations to demonstrate some 
 basic features of Teamcenter Services.
 
     Session                 This class shows how to establish a session with the
                             Teamcenter Server using the SessionService login and 
                             logout methods. A session must be established before
                             any other service operation are called.
     HomeFolder              This class lists the contents of the user's home folder
     Query                   This class performs a simple query.
     DataManagement          This class creates, revises, and deletes a set of Items
     
 Each of these examples performs the same basic steps
     1. Construct the desired service stub.
     2. Gather the data for the operation's input arguments,
     3. Call the service operation
     4. Process the results.
     
 A few of the service operations will make use of the ModelEventListener.
 Under normal circumstances the ExeptionHandler and PartialErrorListner will not
 be called.
 
 
 
 
