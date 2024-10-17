//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.importexport;

import com.teamcenter.clientx.AppXSession;
import com.teamcenter.services.strong.core.DataManagementService;
import com.teamcenter.services.strong.core._2007_01.DataManagement.GetItemFromIdPref;
import com.teamcenter.services.strong.core._2009_10.DataManagement.GetItemFromAttributeInfo;
import com.teamcenter.services.strong.core._2009_10.DataManagement.GetItemFromAttributeResponse;

/**
 * This sample client application demonstrates how to do data export and import
 * through PLMXML and briefcase.
 */
public class ImportExport {

    /**
     * @param args
     *            -help or -h will print out a Usage statement
     */
    public static void main(String[] args) {

        if (args.length > 0) {
            if (args[0].equals("-help") || args[0].equals("-h")) {
                printHelp();
                System.exit(0);
            }
        }

        // Get operation type - import or export
        String type = System.getProperty("type");
        if (type == null || type.isEmpty()) {
            printHelp();
            System.exit(0);
        }

        // Get item id or item key if operation type is export
        String key = null;
        boolean isExport = false;
        if (type.compareToIgnoreCase("export") == 0) {
            isExport = true;
            String id = System.getProperty("id");
            if (id != null && !id.isEmpty()) {
                key = "item_id=" + id;
            } else {
                key = System.getProperty("key");
                if (key == null || key.isEmpty()) {
                    System.out.println("You need to specify the id or key to identify which item to export.");
                    printHelp();
                    System.exit(0);
                }
            }

        } else if (type.compareToIgnoreCase("import") != 0) {
            System.out.println("You need to specify the operation type - import or export.");
            printHelp();
            System.exit(0);
        }

        // Get file path
        String file = System.getProperty("file");
        if (file == null || file.isEmpty()) {
            printHelp();
            System.exit(0);
        }

        // Get file extension to know the data format
        boolean isPLMXML = false;
        if (file.endsWith(".xml")) {
            isPLMXML = true;
        } else if (!file.endsWith(".bcz")) {
            System.out.println("The file path need to be end with .xml for PLMXML and .bcz for briefcase.");
            printHelp();
            System.exit(0);
        }

        // Get site name and OptionSet name for export.
        // Site name is mandatory for briefcase export. OptionSet is optional.
        String siteName = null;
        String optionSetName = "TIEUnconfiguredExportDefault";
        if (!isPLMXML && isExport) {
            siteName = System.getProperty("site");
            if (siteName == null || siteName.isEmpty()) {
                System.out.println("You need to specify the site name for briefcase export.");
                printHelp();
                System.exit(0);
            }
            String optionset = System.getProperty("optionset");
            if (optionset != null && !optionset.isEmpty()) {
                optionSetName = optionset;
            }
        }

        // Get optional host information
        String serverHost = "http://localhost:7001/tc";
        String host = System.getProperty("host");
        if (host != null && host.length() > 0) {
            serverHost = host;
        }

        // Establish a session with the Teamcenter Server
        AppXSession session = new AppXSession(serverHost);
        session.login();

        // Do the export or import by calling different functions
        if (isPLMXML) {
            if (isExport) {
                PIEImportExport.doExport(key, file);
            } else {
                PIEImportExport.doImport(file);
            }
        } else {
            if (isExport) {
                BriefcaseImportExport.doExport(key, siteName, optionSetName, file);
            } else {
                BriefcaseImportExport.doImport(file);
            }
        }

        // Terminate the session with the Teamcenter server
        session.logout();
    }

    /**
     * Print help message to standard output.
     */
    public static void printHelp() {
        System.out
                .println("usage: java -Dtype=import|export -Dfile=FilePath [-Did=ItemId | -Dkey=\"attr=value,attr2=value2...\"] [-Dsite=SiteName] [-Doptionset=OptionSetName] [-Dhost=http://server:port/tc] com.teamcenter.importexport.ImportExport");
    }

    /**
     * Get item from server by using the item key attributes.
     *
     * @param itemKey
     *            item key is of the form "attr=value,attr2=value2..."
     * @return response object from which item and item revision can be found
     */
    public static GetItemFromAttributeResponse getItemInfos(String itemKey) {
        GetItemFromAttributeInfo[] attrInfo = new GetItemFromAttributeInfo[1];
        attrInfo[0] = new GetItemFromAttributeInfo();
        String[] attrs = itemKey.split(",");
        for (int i = 0; i < attrs.length; ++i) {
            String[] keyValue = attrs[i].split("=");
            attrInfo[0].itemAttributes.put(keyValue[0], keyValue[1]);
        }

        GetItemFromIdPref idPref = new GetItemFromIdPref();

        DataManagementService dmService = DataManagementService.getService(AppXSession.getConnection());

        int nRev = -1; // Indicate that we want all revs by setting nRev to -1;
        GetItemFromAttributeResponse response = dmService.getItemFromAttribute(attrInfo, nRev, idPref);

        return response;
    }
}
