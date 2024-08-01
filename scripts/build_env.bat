rem Script to setup environment
set TARGET_OS=Windows_NT_64
set PROJECT_ROOT=C:\Code\DataExtractor
rem set TC_ROOT=C:\Code\TC
rem set TC_DATA=C:\Apps\Siemens\tcdata_local
rem call %TC_DATA%\tc_profilevars
set TC_ROOT=C:\Code\TC
set TC_INCLUDE=C:\Code\TC\include
set JAVA_HOME=C:\App\Java
set ANT_HOME=C:\Code\apache-ant\apache-ant-1.10.14
set DEPLOY_ROOT=C:\Code\DataExtractor\R2



call "C:\Program Files (x86)\Microsoft Visual Studio\2017\Community\VC\Auxiliary\Build\vcvarsall.bat" x86_amd64
set PATH=%ANT_HOME%\bin;%JAVA_HOME%\bin;%PATH%
title DataExtractor

