@echo off
echo ========================================
echo Quick OTP Test
echo ========================================
echo.

echo [STEP 1] Checking if backend is running...
curl -s http://localhost:8080/api/test/health >nul 2>&1
if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Backend is NOT running!
    echo [INFO] Please start backend first:
    echo        cd d:\uniclub\selenium-tests
    echo        START_BACKEND_FOR_TESTS.bat
    echo.
    pause
    exit /b 1
)

echo [OK] Backend is running
echo.

echo [STEP 2] Checking test endpoints...
curl http://localhost:8080/api/test/health
echo.
echo.

echo [STEP 3] Running OTP verification tests...
echo [INFO] Watch Chrome browser - You should see OTP being entered!
echo.

cd /d "%~dp0"
mvn clean test -Dtest=UserRegisterTest#testRegisterSuccessWithCorrectOTP,testRegisterSuccessWithIncorrectOTP -Dheadless=false

echo.
echo ========================================
echo Test completed!
echo ========================================
echo.
echo If you saw OTP being entered in Chrome, tests are working correctly!
echo If not, check:
echo   1. Backend is running (START_BACKEND_FOR_TESTS.bat)
echo   2. Frontend is running (npm run dev in uniclub-fe/web)
echo   3. Check console output for warnings
echo.

pause
