This sample demonstrates the basic functionality of the Teamcenter Services, using
Axis2 client bindings generated from WSDLs vs. the client libraries provided by Teamcenter.
This sample is functionally similar to the HelloTeamcenter sample.

The project can be built with Java SDK 1.7.0, from the command line using Ant(1.7.0), or from
the Eclipse IDE (3.8).

Before running the sample application, you must have the following:
1. Apache Axis2 installed on your system. You can down load this from
       http://ws.apache.org/axis2/download.cgi
   This Sample was developed and tested against Axis2 1.6.2
2. Teamcenter Web Tier server and Pool Manager up and running.

From the Command line

Prerequisite :-  You need to have Ant set up. The Ant bin folder needs to be in your
PATH and ANT_HOME variable pointing to the Ant home directory.

1. Set the environment variable AXIS2_HOME to the installed location of Axis
   >set AXIS2_HOME=D:\Apps\axis2-1.6.2
   
2. Build the application using Ant
   >ant
    
3. Execute the client application
   On Windows:
   >RunMe http://localhost:7001/tc
   On Unix platform:
   >./RunMe.sh http://localhost:7001/tc
   
   Substitute the URI of the Teamcenter Web Tier server.In case of running in TCCS mode,instead of 
   http://hostname:port/tc use tccs://<tccs_envname>
   
From the Eclipse IDE
1. Add ClassPath variable for the Axis
   Open the Preferences dialog (Window --> Preferences ... )
   Select the ClassPath Variable tab ( Java --> Build Path --> ClassPath Variables)
   Add the variable 'AXIS2_HOME', set it to the the installed location of Axis

2. Import the project
   Open the Import dialog ( File --> Import... )
   Select Existing Project into Workspace (General --> Existing Projects into Workspace )
   Click Next, then browse to .../soa_client/java/samples/HelloTcSoap, select Finish

3. Generate the Axis stub using Ant
   ( build.xml --> RMB --> Run as --> Ant Build...  )
   

     
4. Compile the project  
   If the IDE is not configured to Build Automatically, force a build of the project
   
5. Execute the client application
   Open the Debug/Run dialog ( Run --> Debug ...)
   Select the HelloTcSoap Java Application 
   The launch is configured to connect to the server on http://localhost:7001/tc to change this URI
   on the Args tab.
 
 The source file Hello.java has the main function for the application. This is the 
 best place to start browsing the code. There are several classes prefixed with 
 the name AppX (AppXCredentialManager), these classes are to perform some of the
 work done in the Teamcneter Services client bindings.

 
     AppXCredentialManager       Used to get user's credentials.
     AppXClientDataModel         Manages ModelObjects returned from services operations.
     AppXPartialErrorListener    Optional listener for notification when a service operation
                                 has returned partial errors.
     AppXChangeListener          Optional listener for notification when a service operation
                                 has returned ModelObject with updated property values.
     AppXDeleteListener          Optional listener for notification when a service operation
                                 has returned ModelObject that have been deleted from
                                 the database and from the client data model.
 
 The remaining classes in this sample the use of a few of the service operations 
 to demonstrate some basic features of Teamcenter Services.
 
     Session                 This class shows how to establish a session with the
                             Teamcenter Server using the SessionService login and 
                             logout methods. A session must be established before
                             any other service operation are called.
     HomeFolder              This class lists the contents of the user's home folder
     Query                   This class performs a simple query.
     DataManagement          This class creates, revises, and deletes a set of Items
     
 Each of these examples performs the same basic steps
     1. Construct the desired service stub.
     2. Gather the data for the opeation's input arguments,
     3. Call the service operation
     4. Process the results.
     

 
 
 
 
