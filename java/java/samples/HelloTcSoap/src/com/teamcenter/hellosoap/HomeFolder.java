//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.hellosoap;

import java.rmi.RemoteException;

import com.teamcenter.schemas.core._2006_03.datamanagement.GetPropertiesInput;
import com.teamcenter.schemas.soa._2006_03.base.ModelObject;
import com.teamcenter.schemas.soa._2006_03.base.ServiceData;
import com.teamcenter.services.core._2006_03.Core0603DataManagementService;
import com.teamcenter.services.core._2006_03.InternalServerFaultFault;
import com.teamcenter.services.core._2006_03.InvalidUserFaultFault;

public class HomeFolder
{

    /**
     * List the contents of the Home folder.
     *
     */
    public void listHomeFolder(ModelObject user )
    {
        ModelObject home = null;
        ModelObject[] contents = null;

        // Get the service stub
        Core0603DataManagementService dmService = (Core0603DataManagementService)Session.createServiceStub( Core0603DataManagementService.class );

        try
        {
            // User was a primary object returned from the login command
            // the Object Property Policy should be configured to include the
            // 'home_folder' property. However the actual 'home_folder' object
            // was a secondary object returned from the login request and
            // therefore does not have any properties associated with it. We will need to
            // get those properties explicitly with a 'getProperties' service request.
            home = ClientDataModel.getPropertyModelObjectValue( user, "home_folder");
        }
        catch (NotLoadedException e)
        {
            System.out.println(e.getMessage());
            System.out.println("The Object Property Policy ($TC_DATA/soa/policies/Default.xml) is not configured with this property.");
            return;
        }

        try
        {
            ModelObject[] objects = { home };
            String[] attributes = { "contents" };
            GetPropertiesInput in = new GetPropertiesInput();
            in.setObjects(objects);
            in.setAttributes(attributes);

            // *****************************
            // Execute the service operation
            // *****************************
            ServiceData sd = dmService.getProperties(in);
            ClientDataModel.addObjects( sd );


            // The above getProperties call returns a ServiceData object, but it
            // just has pointers to the same object we passed into the method, so the
            // input objects have been updated with new property values
            // WSDL-DIFFERENCE - Model Manager
            // By default this is not the case in a in a WSDL based client. The input
            // and output data are completely separate. In this sample we can do this
            // becasue we have a cache (AppXClientDataModel.addObjects) of all the
            // ModelObject.
            contents = ClientDataModel.getPropertyModelObjectValues(home,"contents");

            System.out.println("");
            System.out.println("Home Folder:");
            Session.printObjects( contents );

        }
        // This should never be thrown, since we just explicitly asked for this
        // property
        catch (NotLoadedException e)        { System.out.println( e.getMessage());}

        // WSDL-DIFFERENCE - Session Management
        // In the Teamcenter client bindings this exception(user not logged in)
        // is handled automatically with the CredentialManager. The client framework
        // will catch this exception, get the credentials from the client's implementation
        // of the CredentialManager interface and re-authenticate with the
        // SessionService.login operation. Once complete the original service
        // request is resent.
        // In a WSDL based client, the client application would have to account
        // for the loss of session and re-authenticate with the server. This sample
        // does not do this, and instead only logs the error message.
        catch (InvalidUserFaultFault e)     { System.out.println( e.getFaultMessage().getInvalidUserFault().getMessage());}


        // WSDL-DIFFERENCE - Exception Handling
        // In the Teamcenter client bindings these exceptions (error outside of the
        // service business logic) are handled with the ExceptionHandler. The client
        // framework will catch these exceptions and call the client's implementation
        // of the ExcpeitonHandler interface.
        // In a WSDL based client, the client application would have to account
        // for these exceptions. This sample does not do this, and instead only logs
        // the error message.
        catch (InternalServerFaultFault e)  { System.out.println( e.getFaultMessage().getInternalServerFault().getMessages()[0]);}
        catch( RemoteException e )          { System.out.println( e.getMessage());}


    }

}
