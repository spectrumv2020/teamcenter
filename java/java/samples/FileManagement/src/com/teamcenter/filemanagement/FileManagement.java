//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.filemanagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.teamcenter.clientx.AppXSession;
import com.teamcenter.services.loose.core._2006_03.FileManagement.DatasetFileInfo;
import com.teamcenter.services.loose.core._2006_03.FileManagement.GetDatasetWriteTicketsInputData;
import com.teamcenter.services.strong.core.DataManagementService;
import com.teamcenter.services.strong.core._2006_03.DataManagement.CreateDatasetsResponse;
import com.teamcenter.services.strong.core._2008_06.DataManagement.DatasetProperties2;
import com.teamcenter.soa.client.FileManagementUtility;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.client.model.ServiceData;

/**
 * Use the FileManagementService to transfer files
 *
 */
public class FileManagement
{
    /** The number of datasets to upload in the multiple file example. */
    static final int NUMBER_OF_DATASETS = 120;

    /** The number of files per dataset to upload in the multiple file example. */
    static final int NUMBER_OF_FILES_PER_DATASET = 3;

    /** Upload some files using the FileManagement utilities. */
    public void uploadFiles()
    {
        FileManagementUtility fMSFileManagement = new FileManagementUtility(AppXSession.getConnection());
        DataManagementService dmService = DataManagementService.getService(AppXSession.getConnection());
        try
        {
            uploadSingleFile(fMSFileManagement, dmService);
            uploadMultipleFiles(fMSFileManagement, dmService);
        }
        finally
        {
            // Close FMS connection when done
            fMSFileManagement.term();
        }
    }

    /** Uploads a single file using the FileManagement utilities. */
    public void uploadSingleFile(final FileManagementUtility fMSFileManagement, final DataManagementService dmService)
    {
        GetDatasetWriteTicketsInputData[] inputs = { getGetDatasetWriteTicketsInputData(dmService) };
        ServiceData response = fMSFileManagement.putFiles(inputs);

        if (response.sizeOfPartialErrors() > 0)
            System.out.println("FileManagementService upload returned partial errors: " + response.sizeOfPartialErrors());

        // Delete all objects created
        ModelObject [] datasets = { inputs[0].dataset };
        dmService.deleteObjects(datasets);
    }

    /** Uploads multiple files using the FileManagement utilities. */
    public void uploadMultipleFiles(final FileManagementUtility fMSFileManagement, final DataManagementService dmService)
    {
        GetDatasetWriteTicketsInputData[] inputs = getMultipleGetDatasetWriteTicketsInputData(dmService);
        ServiceData response = fMSFileManagement.putFiles(inputs);

        if (response.sizeOfPartialErrors() > 0)
            System.out.println("FileManagementService multiple upload returned partial errors: " + response.sizeOfPartialErrors());

        // Delete all objects created
        ModelObject[] datasets = new ModelObject[inputs.length];
        for (int i = 0; i < inputs.length; ++i)
        {
            datasets[i] = inputs[i].dataset;
        }
        dmService.deleteObjects(datasets);
    }

    /** @return A single GetDatasetWriteTicketsInputData for uploading ReadMe.txt. */
    private GetDatasetWriteTicketsInputData getGetDatasetWriteTicketsInputData(final DataManagementService dmService)
    {
        // Create a Dataset
        DatasetProperties2 props = new DatasetProperties2();
        props.clientId = "datasetWriteTixTestClientId";
        props.type = "Text";
        props.name = "Sample-FMS-Upload";
        props.description = "Testing put File";
        DatasetProperties2[] currProps = { props };

        CreateDatasetsResponse resp =  dmService.createDatasets2(currProps);

        // Assume this file is in current dir
        File file1 = new File("ReadMe.txt");

        // Create a file to associate with dataset
        DatasetFileInfo fileInfo = new DatasetFileInfo();
        fileInfo.clientId            = "file_1";
        fileInfo.fileName            = file1.getAbsolutePath();
        fileInfo.namedReferencedName = "Text";
        fileInfo.isText              = true;
        fileInfo.allowReplace        = false;
        DatasetFileInfo[] fileInfos = { fileInfo };

        GetDatasetWriteTicketsInputData inputData = new GetDatasetWriteTicketsInputData();
        inputData.dataset = resp.output[0].dataset;
        inputData.createNewVersion = false;
        inputData.datasetFileInfos = fileInfos;

        return inputData;
    }

    /**
     * @return An array of NUMBER_OF_DATASETS GetDatasetWriteTicketsInputData objects
     * for uploading NUMBER_OF_FILES_PER_DATASET copies of ReadMe.txt to each Dataset.
     */
    private GetDatasetWriteTicketsInputData[] getMultipleGetDatasetWriteTicketsInputData(final DataManagementService dmService)
    {
        GetDatasetWriteTicketsInputData[] inputs = new GetDatasetWriteTicketsInputData[NUMBER_OF_DATASETS];
        DatasetProperties2[] currProps = new DatasetProperties2[inputs.length];

        // Create a bunch of Datasets
        for (int i = 0; i < inputs.length; ++i)
        {
            DatasetProperties2 props = new DatasetProperties2();
            props.clientId = "datasetWriteTixTestClientId " + i;
            props.type = "Text";
            props.name = "Sample-FMS-Upload-" + i;
            props.description = "Testing Multiple put File";
            currProps[i] = props;
        }

        CreateDatasetsResponse resp =  dmService.createDatasets2(currProps);

        // Create files to associate with each Dataset
        for (int i = 0; i < inputs.length; ++i)
        {
            DatasetFileInfo fileInfos[] = new DatasetFileInfo[NUMBER_OF_FILES_PER_DATASET];
            for (int j = 0; j < fileInfos.length; ++j)
            {
                DatasetFileInfo fileInfo = new DatasetFileInfo();

                // We apparently cannot upload two files with the same name to
                //      the same Dataset because this creates a relation name
                //      conflict.
//                File file1 = new File("ReadMe.txt");

                // Create or use this file is in current dir
                File file1 = new File("ReadMeCopy" + j + ".txt");
                assureFileCreated(file1);

                fileInfo.clientId            = "Dataset " + i + " File " + j;
                fileInfo.fileName            = file1.getAbsolutePath();
                fileInfo.namedReferencedName = "Text";
                fileInfo.isText              = true;
                fileInfo.allowReplace        = false;
                fileInfos[j] = fileInfo;
            }

            GetDatasetWriteTicketsInputData inputData = new GetDatasetWriteTicketsInputData();
            inputData.dataset = resp.output[i].dataset;
            inputData.createNewVersion = false;
            inputData.datasetFileInfos = fileInfos;

            inputs[i] = inputData;
        }
        return inputs;
    }

    /**
     * Assures that the file exists on the file system.
     * If not, this method copies "ReadMe.txt" to create the file.
     * @param file1 (File) The file to be created if it does not already exist.
     */
    private void assureFileCreated(final File file1)
    {
        if (file1.exists())
        {
            return;
        }
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try
        {
            try
            {
                byte[] buffer = new byte[16384];

                // Assume this file is in current dir
                fis = new FileInputStream(new File("ReadMe.txt"));
                // Assume that we can copy it in the current dir
                fos = new FileOutputStream(file1);

                while (true)
                {
                    int bytesRead = fis.read(buffer);
                    if (bytesRead <= 0)
                    {
                        // EOF
                        break;
                    }
                    fos.write(buffer, 0, bytesRead);
                }
            }
            finally
            {
                if (fos != null)
                {
                    fos.close();
                }
                if (fis != null)
                {
                    fis.close();
                }
            }
        }
        catch (IOException ioe)
        {
            System.out.println("Could not copy 'ReadMe.txt' to '" + file1.getName()
                + "' - " + ioe.getClass().getSimpleName() + ": " + ioe.getMessage());
        }
    }
}
