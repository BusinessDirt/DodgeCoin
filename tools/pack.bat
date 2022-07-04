@echo off
setlocal

set /p version=Enter Version:

CHOICE /C YN /M "If you continue, the old build will be deleted. Do you want to continue?"
IF ERRORLEVEL 2 GOTO END

rmdir /s /q "./out-windows64"
java -jar packr-all-4.0.0.jar --classpath ../desktop/build/libs/desktop-%version%.jar -- pack-config.json

:END
pause
endlocal
