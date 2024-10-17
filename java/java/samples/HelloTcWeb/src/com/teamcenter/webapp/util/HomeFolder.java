//==================================================
// 
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.webapp.util;

import com.teamcenter.services.strong.core.DataManagementService;
import com.teamcenter.soa.client.Connection;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.client.model.ServiceData;
import com.teamcenter.soa.client.model.strong.Folder;
import com.teamcenter.soa.client.model.strong.User;
import com.teamcenter.soa.client.model.strong.WorkspaceObject;
import com.teamcenter.soa.exceptions.NotLoadedException;

public class HomeFolder
{
    /**
     * List the contents of the user's Home folder.
     *
     * @param userUid
     * @param connection  SOA connection to use
     * @return objects in the user's Home folder
     */
    public WorkspaceObject[] listHomeFolder(String userUid, Connection connection)
    {
        Folder home = null;
        WorkspaceObject[] contents = null;

        // Get the service stub
        DataManagementService dmService = DataManagementService.getService(connection);
        String[] uids = { userUid };
        ServiceData data = dmService.loadObjects(uids);
        User user = (User)data.getPlainObject(0);
        try
        {
            // User was a primary object returned from the login command
            // the Object Property Policy should be configured to include the
            // 'home_folder' property. However the actual 'home_folder' object
            // was a secondary object returned from the login request and
            // therefore does not have any properties associated with it. We will need to
            // get those properties explicitly with a 'getProperties' service request.
            home = user.get_home_folder();

            ModelObject[] objects = { home };
            String[] attributes = { "contents" };

            // *****************************
            // Execute the service operation
            // *****************************
            data  = dmService.getProperties(objects, attributes);
            home = (Folder)data.getPlainObject(0);

            // The above getProperties call returns a ServiceData object, but it
            // just has pointers to the same object we passed into the method, so the
            // input object have been updated with new property values
            contents = home.get_contents();
        }
        // This shouldn't be thrown, since we just explicitly asked for this property
        catch (NotLoadedException e)
        {
            e.printStackTrace();
        }

        return contents;
     }
}
