@ECHO OFF
SETLOCAL EnableDelayedExpansion

cls
SET JAVA_HOME=D:\Apps\Java\jdk-11.0.8jre
SET PATH=%PATH%;%JAVA_HOME%\bin
SET CUR_DIR=%cd%

ECHO.
ECHO.

%CUR_DIR%\EncryptPassword\Encrypt.exe

ECHO.
ECHO.
ECHO.
ECHO "*********************************************************************************************"
ECHO "********************** Utility Exceuction Completed...! *************************************"
ECHO "*********************************************************************************************"
pause