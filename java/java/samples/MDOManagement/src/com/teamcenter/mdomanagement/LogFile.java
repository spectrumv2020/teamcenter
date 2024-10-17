//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================


package com.teamcenter.mdomanagement;

public class LogFile
{
    private LogFile()
    {

    }
    private static LogFile Instance = null;
    private static java.io.FileWriter fw = null;
    private static boolean file_exists = false;

    /**
     * Get the Singleton instance
     */
    public static LogFile getInstance()
    {
        if(Instance == null)
        {
            Instance = new LogFile();
        }
        try
        {
            if(fw == null)
            {
                String logFile = System.getProperty("MDOManagement_SOA_Sample_Client_Log_File");
                if(logFile == null)
                {
                    logFile = System.getenv("MDOManagement_SOA_Sample_Client_Log_File");
                }
                if(logFile != null)
                {
                    if(logFile.length() > 0)
                    {
                        fw = new java.io.FileWriter( logFile, false );
                        file_exists = true;
                    }
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return Instance;
    }

    /**
     * Write log to the logfile and console
     */
    public static void write(String msg)
    {
        try
        {
            if(file_exists)
            {
                fw.write(msg + "\n");
                fw.flush();
            }

            System.out.println(msg);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Close the log file
     */
    public static void close()
    {
        try
        {
            if(file_exists)
            {
                fw.flush();
                fw.close();
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
