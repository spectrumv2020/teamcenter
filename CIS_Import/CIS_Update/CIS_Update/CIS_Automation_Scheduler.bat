@echo off
Setlocal

Title CIS_Automation_Scheduler
rem set the Folder Paths in below:
set "CIS_BASE_DIR=%~dp0"
set "CIS_INPUT_BASE_DIR=%CIS_BASE_DIR%\FileInput"
set "CIS_INPUT_DIR=%CIS_INPUT_BASE_DIR%\Input
set "CIS_STAGE_DIR=%CIS_BASE_DIR%\Staging"
set "CIS_PROCESS_DIR=%CIS_BASE_DIR%\Processed"
set "LOG_DIR=%CIS_BASE_DIR%\Logs"
if not exist %LOG_DIR% (
	mkdir %LOG_DIR%
)
set LOG_FILE=%LOG_DIR%\CIS_Automation_Scheduler.log
set "IMPORT_SCRIPT=%CIS_BASE_DIR%\runCISUpdate_ActualRun.bat"
call :writeLog "Starting CIS_Automation_Scheduler"
rem Check if staging_dir, input_dir, and Processed_dir exist
if not exist "%CIS_STAGE_DIR%\" (
    call :writeLog "Error: Staging directory %CIS_STAGE_DIR% does not exist."
	cscript MessageBox.vbs "Error: Staging directory does not exist."
    rem pause
    exit /b 1
)

if not exist "%CIS_INPUT_DIR%\" (
    call :writeLog "Error: Input directory %CIS_INPUT_DIR% does not exist."
	cscript MessageBox.vbs "Error: Input directory does not exist."
    rem pause
    exit /b 1
)

if not exist "%CIS_PROCESS_DIR%\" (
    call :writeLog "Error: Processed directory %CIS_PROCESS_DIR% does not exist."
	cscript MessageBox.vbs "Error: Processed directory does not exist."
    rem pause
    exit /b 1
)

:mainLoop
rem echo CIS_STAGE_DIR
echo "CIS Automation Scheduler is looking for input files...Polling for every 5 seconds..."

rem Check if there are files in the staging directory
For /F %%A in ('dir /b /a %CIS_STAGE_DIR%') Do (
    call :writeLog "The folder is NOT empty"
    goto :ok
)
timeout /t 5 > nul
goto mainLoop
Echo The folder is empty
:ok
rem Move the file from staging to input directory
for %%F in ("%CIS_STAGE_DIR%\*") do (
	call :writeLog "Moving %%~nxF to %CIS_INPUT_DIR%."
    move "%%F" "%CIS_INPUT_DIR%\" > nul
)
rem pause

rem Check if there is a file in the input directory
dir /b "%CIS_INPUT_DIR%\*" > nul 2>&1
if %errorlevel% equ 1 (
    call :writeLog "No files found in the %CIS_INPUT_DIR%."
    goto skipTestBatch
)

rem Run the test batch file
call :writeLog "Running %import_script%"
call "%import_script%"

rem Move the file from input to Processed directory with date and time appended
for %%F in ("%CIS_INPUT_DIR%\*") do (
	call :writeLog "Moving %%~nxF to %CIS_PROCESS_DIR%."
    setlocal enabledelayedexpansion
    set "datetime=!date:/=-!_!time::=-!"
    set "filename=%%~nF"
    set "extension=%%~xF"
    move "%%F" "%CIS_PROCESS_DIR%\!filename!_!datetime!!extension!" > nul
    endlocal
)
goto mainLoop
:skipTestBatch
goto mainLoop

rem Move the file from input to Processed directory
for %%F in ("%CIS_INPUT_DIR%\*") do (
    call :writeLog "Moving %%~nxF to %CIS_PROCESS_DIR%."
	move "%%F" "%CIS_PROCESS_DIR%\" > nul
)
:writeLog
set "output=%1"
set output=%output:"=%
call :getLogTimeStamp
echo %output%
echo %TIMESTAMP% - %output% >> %LOG_FILE%
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


