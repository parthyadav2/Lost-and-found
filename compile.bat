@echo off
echo Compiling Campus Lost and Found System...
if not exist bin mkdir bin
javac -d bin src\model\*.java src\service\*.java src\util\*.java src\Main.java
if %ERRORLEVEL% equ 0 (
    echo Compilation successful! Run run.bat to start.
) else (
    echo Compilation failed.
)
pause
