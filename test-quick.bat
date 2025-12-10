@echo off
echo ========================================
echo    Running Quick Tests...
echo ========================================
cd uniclub-be
call mvnw.cmd test -DskipTests=false
cd ..
echo.
echo ========================================
echo    Tests completed!
echo ========================================
pause


