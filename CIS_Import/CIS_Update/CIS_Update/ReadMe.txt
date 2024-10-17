Launch_CIS_Automation_background.vbs --> CIS_Automation_Scheduler ---->runCISUpdate_ActualRun

For Manual Run.
Just use runCISUpdate_ActualRun.bat


"-------------------------------
BIG ECHO  - One Time CIS Update Usage
"-------------------------------

1. generate Encrypted password using EncryptPassword.bat file.
2. copy the encrypted password and configure in runCISUpdate_ActualRun.bat
3. copy all inputCSV files in CISInput directory.
4. Launch runCISUpdate_ActualRun.bat
5. Reports will be available in Report directory(DD-MM-YYYY_Detailed_Report.csv).


"-------------------------------
ONGOING  - Automated  CIS Update Usage 
"-------------------------------

1. Ensure Staging directory is accessible to the External CIS Library.
2. Configure windows schedular task to run, Launch_CIS_Automation_background.vbs. Ensure this task is listed in task manager(CIS_Automation_Scheduler.bat)
3. As soon as an Input CSV file is copied in to staging directory, the automated CIS update program will process.
4. Once processed, the input files will be moved to processed directory.
5. Reports will be available in Report directory(DD-MM-YYYY_Detailed_Report.csv).