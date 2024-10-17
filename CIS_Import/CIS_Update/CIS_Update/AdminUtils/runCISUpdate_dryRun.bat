@ECHO OFF
SETLOCAL EnableDelayedExpansion

cls
SET JAVA_HOME=D:\Apps\Java\jdk-11.0.8jre
SET PATH=%PATH%;%JAVA_HOME%\bin
SET CUR_DIR=%cd%

set TC_USER_NAME=infodba
set TC_PASSWORD="AIQ7ogglEoI+GcJlaYAZlToG9QXKYrlo7CxXEo3Dc9E="
set TC_GROUP=Productions
set CONFIG_FILE="%CUR_DIR%\inputConfig.txt"

%CUR_DIR%\CIS_Update_Exe\CIS_Update_nonSSO.exe -u=%TC_USER_NAME% -p=%TC_PASSWORD% -g=%TC_GROUP% -file=%CONFIG_FILE% -dryRun=true -GenReport=false

ECHO "*********************************************************************************************"
ECHO "********************** Utility Exceuction Completed...! *************************************"
ECHO "************* Kindly Check for the Logs & Report Folder for more details....! ***************"
ECHO "*********************************************************************************************"