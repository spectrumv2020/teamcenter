Set WshShell = WScript.CreateObject("WScript.Shell")
scriptPath = WScript.ScriptFullName
folderPath = Left(scriptPath, InStrRev(scriptPath, "\") - 1)
batFilePath = folderPath & "\CIS_Automation_Scheduler.bat"
WshShell.Run "cmd /c " & batFilePath, 0, False
