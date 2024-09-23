cls
echo off

ECHO The first parameter is %1
ECHO The second parameter is %2
ECHO The third parameter is %3

set arg1=%1
set arg2=%2
set arg3=%3

ECHO The first parameter is %arg1%
ECHO The second parameter is %arg2%
ECHO The third parameter is %arg3%

del  create_item.obj  create_item.exe
Call D:\Apps\Siemens\tc_root\sample\setenv_sat.bat
Call D:\Apps\Siemens\tc_root\sample\compile -debug -DIPLIB=none create_item.cpp
Call D:\Apps\Siemens\tc_root\sample\linkitk -o create_item create_item.obj
create_item.exe -u=%arg1% -p=%arg2% -item_type=%arg3%
