@echo off
echo ========================================
echo    Running Unit Tests...
echo ========================================
cd uniclub-be
call mvnw.cmd test
cd ..
echo.
echo ========================================
echo    Tests completed!
echo ========================================
pause


