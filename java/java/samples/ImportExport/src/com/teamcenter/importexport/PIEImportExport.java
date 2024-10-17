//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.importexport;

import java.io.File;

import com.teamcenter.clientx.AppXSession;
import com.teamcenter.schemas.soa._2006_03.exceptions.ServiceException;
import com.teamcenter.services.strong.cad.StructureManagementService;
import com.teamcenter.services.strong.cad._2007_01.StructureManagement.GetRevisionRulesResponse;
import com.teamcenter.services.strong.cad._2007_01.StructureManagement.RevisionRuleInfo;
import com.teamcenter.services.strong.core.FileManagementService;
import com.teamcenter.services.strong.core._2007_01.FileManagement.GetTransientFileTicketsResponse;
import com.teamcenter.services.strong.core._2007_01.FileManagement.TransientFileInfo;
import com.teamcenter.services.strong.core._2009_10.DataManagement.GetItemFromAttributeResponse;
import com.teamcenter.services.strong.globalmultisite.ImportExportService;
import com.teamcenter.services.strong.globalmultisite._2007_06.ImportExport.GetPLMXMLRuleInputData;
import com.teamcenter.services.strong.globalmultisite._2007_06.ImportExport.GetTransferModesResponse;
import com.teamcenter.services.strong.globalmultisite._2007_12.ImportExport.NamesAndValues;
import com.teamcenter.services.strong.globalmultisite._2010_04.ImportExport.ExportObjectsToPLMXMLResponse;
import com.teamcenter.soa.client.FileManagementUtility;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.client.model.strong.Item;
import com.teamcenter.soa.client.model.strong.RevisionRule;
import com.teamcenter.soa.client.model.strong.TransferMode;
import com.teamcenter.soa.exceptions.NotLoadedException;

/**
 * This class is for data export and import by using PLMXML. It has two main
 * functions. One is for export, the other is for import. The output or input
 * file should be a XML file in PLXML format.
 */
public class PIEImportExport {

    /**
     * Export an item to a PLMXML format file. It will use the OOTB export
     * OptionSet - ConfiguredDataFilesExportDefault.
     * 
     * @param itemKey
     *            the item key to identify which item to export
     * @param filePath
     *            the file path of the output PLMXML file
     */
    public static void doExport(String itemKey, String filePath) {
        File outputFile = new File(filePath);
        outputFile = outputFile.getAbsoluteFile();
        String fileName = outputFile.getName();

        GetItemFromAttributeResponse itemResponse = ImportExport.getItemInfos(itemKey);
        if (itemResponse.output.length == 0) {
            System.out.println("Can not find the item for export.");
            return;
        }
        Item topItem = itemResponse.output[0].item;

        /*
         * Set an language array for export. Here we just create an empty array.
         * If you want to select languages for export, you can add them to this
         * array. To get supported languages: LanguageInformationService
         * lnService = LanguageInformationService.getService( session );
         * LanguageInformation.Language[] allLanguages =
         * lnService.getLanguagesList( "supportedLanguages" ).languageList;
         */
        String[] languages = new String[0];

        // Get revision rule
        RevisionRule revRule = null;
        try {
            StructureManagementService smService = StructureManagementService.getService(AppXSession.getConnection());
            GetRevisionRulesResponse revisionRulesResponse = smService.getRevisionRules();
            RevisionRuleInfo[] revisionRuleInfos = revisionRulesResponse.output;
            revRule = revisionRuleInfos[0].revRule;
        } catch (ServiceException e) {
            System.out.println("Can't find any RevisionRule for export.");
            return;
        }

        ExportObjectsToPLMXMLResponse response = null;
        try {
            final String exportTransferMode = "ConfiguredDataFilesExportDefault";
            ImportExportService importExportService = ImportExportService.getService(AppXSession.getConnection());
            TransferMode transferMode = getTransferModeByName(exportTransferMode, "Export", importExportService);
            if (transferMode == null) {
                System.out.println("The TransferMode - " + exportTransferMode + " - is not found.\nExport abort.");
                return;
            }

            NamesAndValues[] sessionOptions = new NamesAndValues[0];

            // Call SOA to do export
            response = importExportService.exportObjectsToPLMXML(new ModelObject[] { topItem }, transferMode, revRule, languages,
                    fileName, sessionOptions);

        } catch (ServiceException ex) {
            ex.printStackTrace();
            return;
        }

        // Download the exported XML file and log file
        String xmlFileDir = outputFile.getParent();
        FileManagementUtility fMSFileManagement = new FileManagementUtility(AppXSession.getConnection());
        fMSFileManagement.getTransientFile(response.xmlFileTicket.ticket,
                xmlFileDir.concat(File.separator).concat(response.xmlFileTicket.fileName));
        fMSFileManagement.getTransientFile(response.logFileTicket.ticket,
                xmlFileDir.concat(File.separator).concat(response.logFileTicket.fileName));

        // Download the dataset files if exist
        if (response.namedRefFileTickets != null && response.namedRefFileTickets.length > 0) {
            // Construct the dataset file folder
            String subDir = fileName.substring(0, fileName.indexOf("."));
            String subDiretory = xmlFileDir.concat(File.separator).concat(subDir);
            File tmpFile = new File(subDiretory);
            tmpFile.mkdir();

            // Download the dataset files
            for (int i = 0; i < response.namedRefFileTickets.length; ++i) {
                fMSFileManagement.getTransientFile(response.namedRefFileTickets[i].ticket,
                        subDiretory.concat(File.separator).concat(response.namedRefFileTickets[i].fileName));
            }
        }
    }

    /**
     * Import a PLMXML file specified by input parameter. It will use the OOTB
     * import OptionSet - ConfiguredDataImportDefault.
     * 
     * @param filePath
     *            the PLMXML file to be imported
     */
    public static void doImport(String filePath) {
        File xmlImportFile = new File(filePath);
        xmlImportFile = xmlImportFile.getAbsoluteFile();
        if (!(xmlImportFile.exists() && xmlImportFile.isFile() && xmlImportFile.canRead())) {
            System.out.println("The import file for test doesn't exist!");
            return;
        }

        final String xmlImportName = xmlImportFile.getName();
        final String xmlImportDir = xmlImportFile.getParent();

        File[] extFiles = getExternalRefFilePaths(xmlImportDir, xmlImportName);

        // Construct the files, include the X file and dataset files, to
        // upload
        String[] fileNames = null;
        TransientFileInfo[] fileInfos = null;

        if (extFiles != null && extFiles.length > 0) {
            fileNames = new String[extFiles.length + 1];
            fileInfos = new TransientFileInfo[extFiles.length + 1];
        } else {
            fileNames = new String[1];
            fileInfos = new TransientFileInfo[1];
        }

        fileInfos[0] = new TransientFileInfo();
        fileInfos[0].fileName = xmlImportName;
        fileInfos[0].isBinary = true;
        fileInfos[0].deleteFlag = true;
        fileNames[0] = xmlImportFile.getAbsolutePath();

        if (extFiles != null) {
            for (int i = 0; i < extFiles.length; i++) {
                fileInfos[i + 1] = new TransientFileInfo();
                fileInfos[i + 1].fileName = extFiles[i].getName();
                fileInfos[i + 1].isBinary = true;
                fileInfos[i + 1].deleteFlag = true;
                fileNames[i + 1] = extFiles[i].getAbsolutePath();
            }
        }

        // Upload the files to transient volume
        FileManagementService fileService = FileManagementService.getService(AppXSession.getConnection());
        GetTransientFileTicketsResponse fileTicketsResponse = fileService.getTransientFileTicketsForUpload(fileInfos);

        FileManagementUtility fMSFileManagement = new FileManagementUtility(AppXSession.getConnection());
        fMSFileManagement.putFileViaTicket(fileTicketsResponse.transientFileTicketInfos[0].ticket, xmlImportFile);
        for (int i = 1; i < fileTicketsResponse.transientFileTicketInfos.length; ++i) {
            fMSFileManagement.putFileViaTicket(fileTicketsResponse.transientFileTicketInfos[i].ticket, extFiles[i - 1]);
        }

        // Use the tickets to do import (first ticket is for PLMXML file)
        String xmlFileTicket = fileTicketsResponse.transientFileTicketInfos[0].ticket;
        String[] extFileTickets = new String[fileTicketsResponse.transientFileTicketInfos.length - 1];
        for (int i = 0; i < extFileTickets.length; i++) {
            extFileTickets[i] = fileTicketsResponse.transientFileTicketInfos[i + 1].ticket;
        }

        try {
            // Call SOA to do import
            final String importTransferMode = "ConfiguredDataImportDefault";

            ImportExportService importExportService = ImportExportService.getService(AppXSession.getConnection());
            TransferMode transferMode = getTransferModeByName(importTransferMode, "Import", importExportService);
            if (transferMode == null) {
                System.out.println("The TransferMode - " + importTransferMode + " - is not found.\nImport abort.");
                return;
            }

            NamesAndValues[] sessionOptions = new NamesAndValues[0];

            importExportService.importObjectsFromPLMXML(xmlFileTicket, extFileTickets, transferMode, null,
                    sessionOptions);

            /*
             * Better to do a validation after data import if applicable. Like
             * searching the item or such.
             */

        } catch (ServiceException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Get external files which are referenced by the XML import file. The
     * referenced files are in a sub-directory of XML import folder. That
     * sub-directory has the same name as the XML file but without the file
     * extension.
     * 
     * @param xmlImportDir
     *            the directory where the XML is located
     * @param xmlImportName
     *            the XML file name to be imported
     * @return the files referenced by XML file
     */
    private static File[] getExternalRefFilePaths(String xmlImportDir, String xmlImportName) {
        File[] extFilePaths = null;
        String subDir = xmlImportName.substring(0, xmlImportName.indexOf("."));
        String subDirPath = xmlImportDir + File.separator + subDir;
        File subDirFile = new File(subDirPath);
        if (subDirFile.exists() && subDirFile.isDirectory()) {
            extFilePaths = subDirFile.listFiles();
        }
        return extFilePaths;
    }

    /**
     * Find a TransferMode by name and scope. Scope has two values - Export and
     * Import. If no matching TransferMode is found, null will return.
     * 
     * @param modeName
     *            name of the TransferMode to find
     * @param scope
     *            the scope of the TransferMode - Export or Import
     * @param importExportSvc
     *            the service object
     * @return the TransferMode object found
     */
    private static TransferMode getTransferModeByName(String modeName, String scope, ImportExportService importExportSvc) {
        GetPLMXMLRuleInputData ruleInputData = new GetPLMXMLRuleInputData();
        ruleInputData.scope = scope;
        ruleInputData.schemaFormat = "PLMXML";
        TransferMode myTransferMode = null;
        try {
            GetTransferModesResponse transferModeResponse = importExportSvc.getTransferModes(ruleInputData);
            TransferMode[] allTransferModes = transferModeResponse.transferModeObjects;

            for (int modesIndex = 0; modesIndex < allTransferModes.length; modesIndex++) {
                try {
                    if (allTransferModes[modesIndex].get_context_string().equals(modeName)
                            || allTransferModes[modesIndex].get_object_name().equalsIgnoreCase(modeName)) {
                        myTransferMode = allTransferModes[modesIndex];
                        break;
                    }
                } catch (NotLoadedException ex) {
                    // Print out a message, and skip to the next TransferMode.
                    // Could do a DataManagementService.getProperties call at
                    // this point.
                    System.out.println(ex.getMessage());
                    System.out
                            .println("The Object Property Policy ($TC_DATA/soa/policies/Default.xml) is not configured with this property.");
                }
            }
        } catch (ServiceException ex) {
            ex.printStackTrace();
        }
        return myTransferMode;
    }
}
