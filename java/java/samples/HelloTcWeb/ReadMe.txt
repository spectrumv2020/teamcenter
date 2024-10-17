This sample demonstrates the basic functionality of the Teamcenter Soa Services
over a Web Deployment.

The project can be built with Java SDK 1.7.0,  using the Eclipse IDE (3.8).

From the Eclipse IDE
1) Start 4 tier, both server manager and web server.

2) In Eclipse, add ClassPath variable for the soa_client folder:
   Open the Preferences dialog (Window --> Preferences ...)
   Select the ClassPath Variable tab (Java --> Build Path --> ClassPath Variables)
   Add the variable 'TEAMCENTER_SERVICES_HOME', set it to the path of the root 'soa_client' folder

3) Import the project:
   Open the Import dialog (File --> Import...)
   Select "Existing Projects into Workspace" (under General folder)
   Click Next, then browse to .../soa_client/java/samples, select Finish

4) Copy the following required jars from <TEAMCENTER_SERVICES_HOME>\java\libs to 
      <TEAMCENTER_SERVICES_HOME>\java\HelloTcWeb\WebContent\WEB-INF\lib
    commons-codec.jar
    httpclient-4.5.2.jar 
	httpcore-4.4.4.jar
    fccclient.jar
    fscclient.jar
    log4j.jar
    tcmemjavabinding.jar
    tcserverjavabinding.jar
    TcSoaClient_13000.0.0.jar
    TcSoaCommon_13000.0.0.jar
    TcSoaCoreStrong_13000.0.0.jar
    TcSoaQueryStrong_13000.0.0.jar
    TcSoaStrongModel_13000.0.0.jar
    xercesImpl.jar

5) If the Teamcenter Web tier has different URI than http://localhost:7001/tc, make appropriate changes to
   various <context-param> in WebContent/Web-INF/web.xml

6) If .jsp files give compile errors. Specify a jdk home folder in Windows --> Preferences --> Java --> Installed JREs.

7) Either deploy the web application manually
   
   -or-
   
   If one has the Web Tools Project installed in Eclipse:
   
   1) Add servlet-api.jar and jsp-api.jar in build path 
   2) Create a new J2ee Server in Eclipse:
   3) On the Eclipse menu bar, select File --> New --> Other, then click Next.
   4) On the Select a wizard panel, select Server --> Server, and then Next.
   5) On the New Server panel, make sure the Server host name is set to localhost. 
   6) For Server type, Basic --> J2EE Preview and click Next.
   7) On New Server panel, select HelloTcWeb and click Add>, then click Finish.
   8) In the Run --> Run Configurations --> Arguments, in VM Arguments specify 
      -Dorg.apache.commons.logging.Log=org.apache.commons.logging.impl.SimpleLog
      This tells commons.logging used in J2EE AppServer to always use the default "SimpleLog" logger instead of letting the
      configuration algorithm incorrectly "discover" the Log4J in web app. 
   9) On the Servers tab, right click on the server that was just added and select Start.
    
    
8) From a browser, go to http://localhost:8080/HelloTcWeb/Login.jsp where http://localhost:8080 is the base address of the webserver hosting the HelloTcWeb application.

9) Login and click the appropriate buttons to navigate the sample web app.
