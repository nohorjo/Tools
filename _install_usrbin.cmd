@echo off
for %%a in ("%path:;=";"%") do (
    if "%CD%"==%%a (
		echo %%a found in path
    	GOTO alreadyInstalledpath
    ) 
)

echo Installing in path
setx PATH "%CD%;%path%" /M

:alreadyInstalledpath

for %%a in ("%PATH:;=";"%") do (
    if "%CD%"==%%a (
		echo %%a found in PATH
    	GOTO alreadyInstalledPATH
    ) 
)

echo Installing in PATH
setx PATH "%CD%;%PATH%" /M

:alreadyInstalledPATH

echo Install complete

pause