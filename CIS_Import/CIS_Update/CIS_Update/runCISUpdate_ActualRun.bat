@ECHO OFF
SETLOCAL EnableDelayedExpansion

cls
SET JAVA_HOME=D:\Apps\Java\jdk-11.0.8jre
SET PATH=%PATH%;%JAVA_HOME%\bin
set "CIS_BASE_DIR=%~dp0"
set "LOG_DIR=%CIS_BASE_DIR%\Logs"
if not exist %LOG_DIR% (
	mkdir %LOG_DIR%
)
set LOG_FILE=%LOG_DIR%\CIS_Automation_Scheduler.log
set TC_USER_NAME=infodba
set TC_PASSWORD="OD/cnflPr9HJVTmXX5ZIb1AiTxGQ0MlObi8cW3+TqHs="
set TC_GROUP=dba
set "CONFIG_FILE=%CIS_BASE_DIR%\inputConfig.txt"

%CIS_BASE_DIR%\CIS_Update_Exe\CIS_Update_nonSSO.exe -u=%TC_USER_NAME% -p=%TC_PASSWORD% -g=%TC_GROUP% -file=%CONFIG_FILE% -dryRun=false -GenReport=false >> %LOG_FILE%

ECHO "*********************************************************************************************"
ECHO "********************** Utility Exceuction Completed...! *************************************"
ECHO "************* Kindly Check for the Logs & Report Folder for more details....! ***************"
ECHO "*********************************************************************************************"

GOTO EOF
:writeLog
set "output=%1"
set output=%output:"=%
call :getLogTimeStamp
echo %output%
echo %TIMESTAMP% - %output% >> %log_file%
GOTO EOF

:getLogTimeStamp
set vars=Month Day Year hour min sec TIMESTAMP
REM Unset any of these variables used for the timestamp before setting them again.
REM Otherwise the variables might not get set correctly
for %%1 in (%vars%) do (
	set %%1=
)
REM Setting Date Portion
set Month=%DATE:~4,2%
set Day=%DATE:~7,2%
set Year=%DATE:~10,4%

REM Setting Time Portion
set hour=%TIME:~0,2%
REM If the %TIME% cmd is run in the morning, it returns the hour as ' 7' instead of '07'. This can 
REM cause errors in file names when this null char is present. So we need to check if that first char 
REM is null, if it is, set to '0'
if "%hour:~0,1%"==" " (
	set hour=0%hour:~1,1%
)
set min=%TIME:~3,2%
set sec=%TIME:~6,2%
set TIMESTAMP=%Year%/%Month%/%Day% %hour%:%min%:%sec%

GOTO EOF
:EOF
