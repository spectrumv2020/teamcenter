//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.importexport;

import java.io.File;

import com.teamcenter.clientx.AppXSession;
import com.teamcenter.schemas.soa._2006_03.exceptions.ServiceException;
import com.teamcenter.services.strong.core.FileManagementService;
import com.teamcenter.services.strong.core.SessionService;
import com.teamcenter.services.strong.core._2007_01.FileManagement.GetTransientFileTicketsResponse;
import com.teamcenter.services.strong.core._2007_01.FileManagement.TransientFileInfo;
import com.teamcenter.services.strong.core._2007_01.Session.GetTCSessionInfoResponse;
import com.teamcenter.services.strong.core._2009_10.DataManagement.GetItemFromAttributeResponse;
import com.teamcenter.services.strong.globalmultisite.ImportExportService;
import com.teamcenter.services.strong.globalmultisite._2007_06.ImportExport.GetAvailableTransferOptionSetsInputData;
import com.teamcenter.services.strong.globalmultisite._2007_06.ImportExport.GetAvailableTransferOptionSetsResponse;
import com.teamcenter.services.strong.globalmultisite._2007_06.ImportExport.NamesAndValue;
import com.teamcenter.services.strong.globalmultisite._2008_06.ImportExport.ExportObjectsToOfflinePackageResponse;
import com.teamcenter.services.strong.globalmultisite._2008_06.ImportExport.ImportObjectsFromOfflinePackageResponse;
import com.teamcenter.soa.client.FileManagementUtility;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.client.model.ServiceData;
import com.teamcenter.soa.client.model.strong.Item;
import com.teamcenter.soa.client.model.strong.POM_imc;
import com.teamcenter.soa.client.model.strong.TransferOptionSet;
import com.teamcenter.soa.exceptions.NotLoadedException;

/**
 * This class is for briefcase import/export. It has two main methods,
 * one is for import, the other is for export.
 */
public class BriefcaseImportExport {

    /**
     * Export an item to a remote site using specific OptionSet by briefcase.
     * 
     * @param itemKey
     *            the item key to identify which item to export
     * @param siteName
     *            the remote site which is to be the target site
     * @param optionSetName
     *            the OptionSet to do the briefcase export
     * @param filePath
     *            the file path of the output briefcase
     */
    public static void doExport(String itemKey, String siteName, String optionSetName, String filePath) {
        GetItemFromAttributeResponse itemResponse = ImportExport.getItemInfos(itemKey);
        if (itemResponse.output.length == 0) {
            System.out.println("Can not find the item for export.");
            return;
        }
        Item topItem = itemResponse.output[0].item;

        // Get target site
        POM_imc site = null;
        ImportExportService importExportService = ImportExportService.getService(AppXSession.getConnection());
        ServiceData serviceData = importExportService.getRemoteSites("OfflineGMS");
        for (int i = 0; i < serviceData.sizeOfPlainObjects(); i++) {
            ModelObject modelObject = serviceData.getPlainObject(i);
            if (modelObject instanceof POM_imc) {
                POM_imc theSite = (POM_imc) modelObject;
                try {
                    if (theSite.get_name().compareTo(siteName) == 0) {
                        site = theSite;
                        break;
                    }
                } catch (NotLoadedException ex) {
                    // Print out a message, and skip to the next site.
                    // Could do a DataManagementService.getProperties call at
                    // this point.
                    System.out.println(ex.getMessage());
                    System.out
                            .println("The Object Property Policy ($TC_DATA/soa/policies/Default.xml) is not configured with this property.");
                }
            }
        }
        if (site == null) {
            System.out.println("Can not find the site for export.");
            return;
        }

        ExportObjectsToOfflinePackageResponse response = null;
        try {

            // Get export OptionSet
            TransferOptionSet optionSet = getOptionSet(importExportService, optionSetName, true, site);
            if (optionSet == null) {
                System.out.println("Can not find the TransferOptionSet - " + optionSetName + ".");
                return;
            }

            // Export the Briefcase package
            NamesAndValue[] options = {};
            NamesAndValue[] sessionOptions = {};
            response = importExportService.exportObjectsToOfflinePackage("Reason: Test", new ModelObject[] { site },
                    new ModelObject[] { topItem }, optionSet, options, sessionOptions);

        } catch (ServiceException ex) {
            ex.printStackTrace();
            return;
        }

        File bczFile = new File(filePath);
        bczFile = bczFile.getAbsoluteFile();
        String bczFileDir = bczFile.getParent();
        String bczFileName = bczFile.getName();

        // Download the briefcase file
        FileManagementUtility fMSFileManagement = new FileManagementUtility(AppXSession.getConnection());

        if (response.briefcaseFileFMSTicket != null && !response.briefcaseFileFMSTicket.trim().equals("")) {
            fMSFileManagement.getTransientFile(response.briefcaseFileFMSTicket, bczFile.getPath());
        } else {
            System.out.println("There is no breifcase file has been created.");
            return;
        }

        // Download the log file
        if (response.exportLogFileFMSTickets != null && response.exportLogFileFMSTickets.length > 0
                && !response.exportLogFileFMSTickets[0].trim().equals("")) {
            String logFileName = bczFileName.substring(0, bczFileName.indexOf(".") + 1) + "_exporter.log";
            fMSFileManagement.getTransientFile(response.exportLogFileFMSTickets[0], bczFileDir.concat(File.separator)
                    .concat(logFileName));
        } else {
            System.out.println("There is no log file has been created.");
        }

        if (!bczFile.exists()) {
            System.out.println("There is no breifcase file has been downloaded sucessfully.");
        }
        return;

    }

    /**
     * Import a specific briefcase file.
     * 
     * @param filePath
     *            the full path name of a briefcase
     */
    public static void doImport(String filePath) {
        File offlinePackageFile = new File(filePath);
        offlinePackageFile = offlinePackageFile.getAbsoluteFile();

        TransientFileInfo[] fileInfos = new TransientFileInfo[1];
        fileInfos[0] = new TransientFileInfo();
        fileInfos[0].fileName = offlinePackageFile.getName();
        fileInfos[0].isBinary = true;
        fileInfos[0].deleteFlag = true;

        FileManagementService fileManagementService = FileManagementService.getService(AppXSession.getConnection());
        GetTransientFileTicketsResponse fileTicketsResponse = fileManagementService
                .getTransientFileTicketsForUpload(fileInfos);
        String impBczFileTicket = fileTicketsResponse.transientFileTicketInfos[0].ticket;

        FileManagementUtility fMSFileManagement = new FileManagementUtility(AppXSession.getConnection());
        fMSFileManagement.putFileViaTicket(impBczFileTicket, offlinePackageFile);

        try {
            final String importOptionSet = "TIEImportOptionSetDefault";
            ImportExportService importExportService = ImportExportService.getService(AppXSession.getConnection());
            TransferOptionSet optionSet = getOptionSet(importExportService, importOptionSet, false, null);
            if (optionSet == null) {
                System.out.println("The OptionSet - " + importOptionSet + " - is not found.\nImport abort.");
                return;
            }
            NamesAndValue[] options = {};
            NamesAndValue[] sessionOptions = {};
            ImportObjectsFromOfflinePackageResponse response = importExportService.importObjectsFromOfflinePackage(
                    impBczFileTicket, optionSet, options, sessionOptions);

            int soaErrorCount = response.serviceData.sizeOfPartialErrors();
            if (soaErrorCount > 0) {
                System.out.println(response.serviceData.getPartialError(soaErrorCount - 1).toString());
            }
        } catch (ServiceException ex) {
            ex.printStackTrace();
            return;
        }
    }

    /**
     * Get a OptionSet by name.
     * 
     * @param importExportService
     *            an ImportExportService instance which can do the search of
     *            OptionSet
     * @param optionSetName
     *            the name of the OptionSet to get
     * @param isExport
     *            the OptionSet is for export or not
     * @param site
     *            the remote site which can apply the OptionSet
     * @return a TransferOptionSet object that match the criteria
     * @throws Exception
     */
    public static TransferOptionSet getOptionSet(ImportExportService importExportService, String optionSetName,
            boolean isExport, POM_imc site) throws ServiceException {
        TransferOptionSet[] optionSet = getAvailableOptionSets(importExportService, isExport, site);

        if (optionSet != null && optionSet.length > 0) {
            for (int i = 0; i < optionSet.length; i++) {
                try {
                    if (optionSet[i].get_object_name().equals(optionSetName)) {
                        return optionSet[i];
                    }
                } catch (NotLoadedException ex) {
                    // Print out a message, and skip to the next OptionSet.
                    // Could do a DataManagementService.getProperties call at
                    // this point.
                    System.out.println(ex.getMessage());
                    System.out
                            .println("The Object Property Policy ($TC_DATA/soa/policies/Default.xml) is not configured with this property.");
                }
            }
        }
        return null;
    }

    /**
     * Get all available OptionSets that can apply to specific remote site for
     * export or import.
     * 
     * @param importExportService
     * @param isExport
     *            a flag to indicate if to get OptionSet for export or not
     * @param mSupplierSite
     *            a remote site that can apply the OptionSet
     * @return available OptionSets that match the criteria
     * @throws Exception
     */
    public static TransferOptionSet[] getAvailableOptionSets(ImportExportService importExportService, boolean isExport,
            POM_imc mSupplierSite) throws ServiceException {
        SessionService sessionService = SessionService.getService(AppXSession.getConnection());
        GetTCSessionInfoResponse sessionInfoResponse = sessionService.getTCSessionInfo();

        GetAvailableTransferOptionSetsInputData inputData = new GetAvailableTransferOptionSetsInputData();
        inputData.user = sessionInfoResponse.user;
        inputData.group = sessionInfoResponse.group;
        inputData.role = sessionInfoResponse.role;
        inputData.site = new POM_imc[1];
        inputData.site[0] = mSupplierSite;
        inputData.isPush = true;
        inputData.isExport = isExport;
        GetAvailableTransferOptionSetsResponse response = importExportService.getAvailableTransferOptionSets(inputData);

        return response.optionSetObjects;
    }
}
