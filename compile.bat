@echo off

rd /s /q "%USERPROFILE%\.m2\repository\ch\ethz"

set DEFAULT_BACKEND=db4o
set BACKEND=%1

if (%BACKEND%=="") set BACKEND=%DEFAULT_BACKEND%
echo %BACKEND%

mvn clean package -Dbackend=%BACKEND%