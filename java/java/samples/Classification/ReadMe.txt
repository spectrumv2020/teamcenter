This sample demonstrates the basic functionality of the Teamcenter Classification Services.
The project can be built with Java SDK 1.7.0, from the command line using Ant(1.7.0), or from
the Eclipse IDE (3.8).

Before running the sample application, you must have a Teamcenter Web Tier server
and Pool Manager up and running.

Data creation:

1) Create an attribute with ID 4001 in data dictionary. ( Refer "creating attribute dictionary definitions" in TDoc ).

2) Create a class with ID ICM4001, and assign the attribute 4001 to this class.( Refer "Create the class" and "Assigning attributes to the class"  in TDoc..) 

From the Command line
1. Build the application using Ant
   >ant -buildfile build.xml
2. Execute the client application
   >RunMe http://localhost:7001/tc
   Substitute the URI of the Teamcenter Web Tier server
   
From the Eclipse IDE
1. Add ClassPath variable for the root location of the soa_client folder
   Open the Preferences dialog (Window --> Preferences ... )
   Select the ClassPath Variable tab ( Java --> Build Path --> ClassPath Variables)
   Add the variable 'TEAMCENTER_SERVICES_HOME', set it to the root path of the 'soa_client' folder
2. Import the project
   Open the Import dialog ( File --> Import... )
   Select Existing Project into Workspace (General --> Existing Projects into Workspace )
   Click Next, then browse to .../soa_client/java/sample, select Classification project and then select Finish
3. Compile the project  
   If the IDE is not configured to Build Automatically, force a build of the project
4. Execute the client application
   Open the Debug/Run dialog ( Run --> Debug ...)
   Select the Classification Java Application 
   The launch is configured to connect to the server on http://localhost:7001/tc to change this URI
   on the Args tab.
 
 

 
 
 
