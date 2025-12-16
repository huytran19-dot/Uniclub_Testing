@echo off
echo ========================================
echo    Running Unit Tests (CI Mode)
echo ========================================
cd uniclub-be
call mvnw.cmd clean test
cd ..
echo.
echo ========================================
echo    Tests completed!
echo ========================================


