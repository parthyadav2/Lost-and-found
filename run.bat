@echo off
if not exist bin (
    echo Bin directory not found. Compiling first...
    call compile.bat
)
java -cp bin Main
pause
