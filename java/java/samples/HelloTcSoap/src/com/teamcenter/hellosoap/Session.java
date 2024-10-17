//==================================================
// 
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================


package com.teamcenter.hellosoap;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

import com.teamcenter.schemas.core._2006_03.datamanagement.GetPropertiesInput;
import com.teamcenter.schemas.core._2006_03.session.LoginInput;
import com.teamcenter.schemas.core._2006_03.session.LoginResponse;
import com.teamcenter.schemas.core._2006_03.session.LogoutInput;
import com.teamcenter.schemas.soa._2006_03.base.ModelObject;
import com.teamcenter.schemas.soa._2006_03.base.ServiceData;
import com.teamcenter.services.core._2006_03.Core0603DataManagementService;
import com.teamcenter.services.core._2006_03.Core0603SessionService;
import com.teamcenter.services.core._2006_03.InternalServerFaultFault;
import com.teamcenter.services.core._2006_03.InvalidCredentialsFaultFault;
import com.teamcenter.services.core._2006_03.InvalidUserFaultFault;
import com.teamcenter.services.core._2006_03.ServiceFaultFault;



public class Session
{
    /**
     * Single instance of the host URI that is shared throughout
     * the application. This URI is needed whenever a Service
     * stub is instantiated.
     */
    private static String  hostURI = "http://localhost:7001/tc/services/";

    /**
     * Use a shared ConfigurationContext to allow the different services
     * used in the application to share a single session.
     */
    private static ConfigurationContext configurationContext = null;


    /**
     * WSDL-DIFFERENCE - Session Management
     * In the Teamcenter client bindings, the CredentialManger is used to re-authenticate
     * the user after a server session has been lost (time-out due to inactivity .etc).
     * Here the AppXCredentialManager is used only by the Session class to get user credentials.
     * Any service may throw a InvlidUserFault, the client application will need to handle this
     * before resuming any service requests.
     *
     */
    private static CredentialManager credentials;


    /**
     * This is a convenience function to instantiate a Axis2 stub.
     * The stub is created using the shared ConfiguationContext.
     *
     * @param serviceInterface  Class of the desired service interface
     *
     * @return Stub instance of the service interface
     *
     */
    public static Object createServiceStub(  Class<?> serviceInterface )
    {
        try
        {
            // Use the default constructor for the Stub so we can get the
            // service end-point URL that was compiled into the stub.
            // We really just need the tail of the URL (port) as the
            // the real host port and web application name may have changed
            // since the Service/WSDL was authored.
            Class<?> stub = Class.forName(serviceInterface.getName()+"Stub");
            Class<?>[]  types = null;
            Object[] args  = null;
            
            Constructor<?> stubConstructor = stub.getConstructor(types);
            Object serviceStub = stubConstructor.newInstance(args);
            types = null;

            Method getServiceClient = stub.getMethod("_getServiceClient", types);
            ServiceClient sc = (ServiceClient)getServiceClient.invoke(serviceStub, args);
            String url  = sc.getOptions().getTo().getAddress();
            String port = url.substring(url.lastIndexOf('/')+1);

            // Now create the Service stub with the URL configured in this application
            // and the shared ConfigurationContext
            Class<?>[]  types2 = { ConfigurationContext.class, String.class };
            Object[] args2  = { configurationContext, hostURI+port };
            stubConstructor =stub.getConstructor(types2);
            serviceStub = stubConstructor.newInstance(args2);

            return serviceStub;

        }
        catch(Exception e)
        {
            System.out.println( e.getMessage());
            System.exit(-1);
        }
        return null;
    }
    /**
     * Create an instance of the Session with a connection to the specified
     * server.
     *
     * Add implementations of the ExceptionHandler, PartialErrorListener,
     * ChangeListener, and DeleteListeners.
     *
     * @param host      Address of the host to connect to, http://serverName:port/tc
     */
    public Session(String host)
    {
        // Create an instance of the CredentialManager, this is used
        // by the SOA Framework to get the user's credentials when
        // challenged by the server (session timeout on the web tier).
        credentials = new CredentialManager();

        if(!host.startsWith("http"))
        {
            throw new java.lang.IllegalArgumentException("HTTP and HTTPS are the only supported protocols");
        }
        // Append services to the URI for the Axis servlet mapping
        hostURI = host + "/services/";


        // WSDL-DIFFERENCE - Session Management
        // The Teamcenter client bindings manage session (HTTP cookies) between
        // service request. By default Axis (and most SOAP engines) treat service
        // operations as state-less and do not maintain a session. While the Axis
        // Options.setManageSession will maintain HTTP cookies per service interface,
        // it will not manage a session between service interfaces ( Query0603SavedQueryService
        // and Core0603DataManagement). By using a single instance of the HttpClient
        // we can share session information between service stubs.
        try
        {
            configurationContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem("bin/axis2.xml");
            MultiThreadedHttpConnectionManager conmgr = new MultiThreadedHttpConnectionManager();
            conmgr.getParams().setDefaultMaxConnectionsPerHost(10);
            HttpClient client = new HttpClient(conmgr);
            configurationContext.setProperty(HTTPConstants.CACHED_HTTP_CLIENT, client);
            configurationContext.setProperty(HTTPConstants.REUSE_HTTP_CLIENT,  true);
        }
        catch(AxisFault e)
        {
            System.out.println( e.getMessage());
            System.exit(0);
        }




    }


    /**
     * Login to the Teamcenter Server
     *
     * @return The User object for the current session.
     * WSDL-DIFFERENCE - Client Data Model
     * The Teamcenter client bindings provide a strongly typed data model, there
     * is a concrete class for every type defined in the Teamcenter Business Model.
     * This allows client application to take advantage of compile and run-time
     * type checking. The type bindings also provide getter methods for all the
     * properties defined on that type.
     * The WSDL bindings use the generic ModelObject for every type in the Teamcenter
     * Business Model. While it is possible to describe all of the Business Model types
     * in XSD schema, these definitions change from release-to-release of Teamcenter
     * and are further modified by each customer. These changes in Business Model
     * schema would necessitate changes in the WSDL XSD thus making the WSDL inoperable
     * between releases. Because of this limitation the Teamcenter Services WSDL
     * are defined with a generic ModelObject type that will describe any type
     * in the Teamcenter Business Model.
     * The Teamcenter client bindings will convert the generic ModelObject to the
     * correct strongly typed instance (in this case User). WSDL clients are forced
     * to work with the generic ModelObject class.
     *
     */
    public ModelObject login()
    {

        // Get the service stub
        Core0603SessionService sessionService = (Core0603SessionService)createServiceStub( Core0603SessionService.class );


        // Prompt for credentials until they are right, or until user cancels
        LoginInput  in = credentials.promptForCredentials();
        while (true)
        {
            try
            {

                // *****************************
                // Execute the service operation
                // *****************************
                LoginResponse out = sessionService.login(in);

                // WSDL-DIFFERENCE - Model Manager
                // The Teamcenter client bindings use the ModelManager (part of
                // the Connection object) to cache all instances ModelObjects returned
                // form any service. A WSDL binding treats each service request as
                // independent operations, and will only hold on to the returned
                // ModelObjects as long the client application keeps the returned
                // data structure.
                // Here an explicit call to the AppXClientDataModel will cache the
                // returned ModelObjects and merge property values with instances
                // already stored in the client.
                ClientDataModel.addObjects( out.getServiceData());

                // WSDL-DIFFERENCE - Model Manager
                // In a service response structure there may be multiple instances of
                // a single ModelObject. To minimize the size of the XML document,
                // these secondary instances (LoginResponse.getUser) only contain
                // basic information, UID & Type. The primary instance of the ModelObject
                // is always returned in the ServiceData (LoginResponse.getServicData).
                // The primary instance has all of the property values for the given
                // object.
                // In the Teamcenter client bindings the ModelManager will replace
                // all of these secondary instances with pointers to the primary
                // instance in ServiceData, all of which are cached in the Client
                // Data Model. With a WSDL client binding the client application is
                // forced to correlate the secondary instances with the primary
                // instance in the ServiceData.
                // The call below will find the fully populated instance of the User
                // ModelObject in the cache. This will only work if the corresponding
                // ServiceData has been previously added to the cache (AppXClientDataModel.addObjects)
                return ClientDataModel.getObjectWithProperties( out.getUser() );

            }
            catch (InvalidCredentialsFaultFault e)
            {
                in = credentials.getCredentials(e);
            }
            catch (InternalServerFaultFault e)
            {
                System.out.println( e.getFaultMessage().getInternalServerFault().getMessages()[0]);
                System.exit(-1);
            }
            catch( RemoteException e )
            {
                System.out.println( e.getMessage());
                System.exit(-1);
            }
        }



    }

    /**
     * Terminate the session with the Teamcenter Server
     *
     */
    public void logout()
    {
        // Get the service stub
        Core0603SessionService sessionService = (Core0603SessionService)createServiceStub( Core0603SessionService.class );

        try
        {
            LogoutInput in = new LogoutInput();

            // *****************************
            // Execute the service operation
            // *****************************
            sessionService.logout(in);
        }
        // Ignore all of these, we are logging out
        catch (ServiceFaultFault e)
        {
            System.out.println(e.getFaultMessage().getServiceFault().getMessages()[0]);
        }
        catch (InvalidUserFaultFault e)   {}
        catch (InternalServerFaultFault e)
        {
            System.out.println(e.getFaultMessage().getInternalServerFault().getMessages()[0]);
        }
        catch( RemoteException e )
        {
            System.out.println(e.getMessage());
        }
   }

    /**
     * Print some basic information for a list of objects
     *
     * @param objects
     */
    public static void printObjects(ModelObject[] objects )
    {
        if(objects == null)
            return;

        SimpleDateFormat format = new SimpleDateFormat("M/d/yyyy h:mm a", new Locale("en", "US")); // Simple no time zone

        // Ensure that the referenced User objects that we will use below are loaded
        getUsers( objects );

        System.out.println("Name\t\tOwner\t\tLast Modified");
        System.out.println("====\t\t=====\t\t=============");
        for (int i = 0; i < objects.length; i++)
        {
            // WSDL-DIFFERENCE - Client Data Model
            // Applications using the Teamcenter client bindings may cast the
            // generic ModelObject to a specific type(i.e. WorkspaceObject) and
            // then use the getter methods on that type to get specific property
            // values.
            // WorkspaceObject wo = (WorkspaceObject)objects[i];
            // WSDL based application do not have an equivalent functionality



            ModelObject wo = ClientDataModel.getObjectWithProperties( objects[i] );
            try
            {
                // WSDL-DIFFERENCE - Data Model
                // The Teamcenter client binding provided getter methods for all
                // properties on a given type (WorkspaceObject.get_object_string),
                // thus allowing the client application to easily pull any property
                // value from the object. In the generic ModelObject of the WSDL
                // client bindings, property values are stored on the objects as a
                // array of name/value pairs. The client application must search
                // through the array for the desired property name.
                // In this sample the AppXClientDataModel provides functions to do this.
                String name = ClientDataModel.getPropertyStringValue( wo, "object_string");

                // WSDL-DIFFERENCE - Data Model
                // In Teamcenter client bindings the non string property values are
                // automatically converted from a string (used in XML transport) to
                // the appropriate data type (int, float, Calendar, ModelObject .etc).
                // WSDL based client must perform this conversion manually.
                // In this sample the AppXClientDataModel provides functions to do this.
                ModelObject owner = ClientDataModel.getPropertyModelObjectValue( wo, "owning_user");
                Calendar lastModified = ClientDataModel.getPropertyDateValue(wo, "last_mod_date");

                System.out.println(name + "\t" + ClientDataModel.getPropertyStringValue(owner, "user_name") + "\t"
                        + format.format(lastModified.getTime()));
            }
            catch (NotLoadedException e)
            {
                // Print out a message, and skip to the next item in the folder
                // Could do a DataManagementService.getProperties call at this point
                System.out.println(e.getMessage());
                System.out.println("The Object Property Policy ($TC_DATA/soa/policies/Default.xml) is not configured with this property.");
            }
        }

    }


    private static void getUsers( ModelObject[] objects )
    {
        if(objects == null)
            return;


         Core0603DataManagementService dmService = (Core0603DataManagementService)createServiceStub( Core0603DataManagementService.class );


        List<ModelObject> unKnownUsers = new Vector<ModelObject>();
        for (int i = 0; i < objects.length; i++)
        {


            ModelObject wo = ClientDataModel.getObjectWithProperties( objects[i] );

            ModelObject owner = null;
            try
            {
                // WSDL-DIFFERENCE - Model Manager
                // In both applications using the Teamcenter client bindings and this
                // WSDL/SOAP sample application, we are taking advantage of  User
                // object previously downloaded (current User in the Login operation),
                // and only getting property values for User objects that do not have
                // them. If this sample did not cache ModelObjects in the AppXClientDataModel
                // this getUsers method would send the DataManagementService.getProperties
                // request for every Owning User found in this set of objects.
                owner = ClientDataModel.getPropertyModelObjectValue(wo,"owning_user");
                ClientDataModel.getPropertyStringValue(owner,"user_name");
            }
            catch (NotLoadedException e)
            {
                if(owner != null)
                    unKnownUsers.add(owner);
            }
        }

        // check to see if we need properties for any users, if not just return
        // without making the call to DataManagementService.getProperties
        if(unKnownUsers.size() == 0)
            return;

        ModelObject[] users = (ModelObject[])unKnownUsers.toArray(new ModelObject[unKnownUsers.size()]);
        String[] attributes = { "user_name" };


        // *****************************
        // Execute the service operation
        // *****************************
        GetPropertiesInput in = new GetPropertiesInput();
        in.setObjects(users);
        in.setAttributes(attributes);
        try
        {
            ServiceData sd = dmService.getProperties(in);
            ClientDataModel.addObjects( sd );
        }
        catch (InvalidUserFaultFault e)   {}
        catch (InternalServerFaultFault e){}
        catch( RemoteException e )        {}


    }



}
