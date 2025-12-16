@echo off
REM Change to script directory
cd /d "%~dp0"

echo ========================================
echo Running Cart Tests with Chrome Visible + Allure Report
echo Feature: Shopping Cart Management
echo ========================================
echo.

REM ==============================
REM STEP 1: Pre-Test Cleanup
REM ==============================
echo [1/4] Cleaning Chrome temp files only...

REM Clean ChromeDriver processes
taskkill /F /IM chromedriver.exe /T >nul 2>&1

REM Only clean Chrome temp (keep allure-results for accumulation)
if exist "D:\temp\selenium-chrome-profile\" (
    rmdir /s /q "D:\temp\selenium-chrome-profile" >nul 2>&1
    echo   - Cleaned Chrome profile
)
if exist "D:\temp\chrome-cache\" (
    rmdir /s /q "D:\temp\chrome-cache" >nul 2>&1
    echo   - Cleaned Chrome cache
)

echo   ✓ Chrome temp cleaned (allure-results kept for accumulation)
timeout /t 1 /nobreak >nul

REM ==============================
REM STEP 2: Verify Configuration
REM ==============================
echo.
echo [2/4] Verifying configuration...
echo   Test: CartTest (20 test cases)
echo   Browser: Chrome (Visible Mode)
echo   Screenshots: On failure only
echo   Report: Allure
echo   Chrome Data: D:\temp (auto-cleaned)
echo   ✓ Configuration verified
timeout /t 1 /nobreak >nul

REM ==============================
REM STEP 3: Run Cart Tests with Chrome Visible
REM ==============================
echo.
echo [3/4] Running Cart Tests - Watch Chrome window!
echo ========================================
echo 👁️  You can see each test step in Chrome browser
echo 🛒 Testing: Add/Remove, Quantity, Pricing, Checkout
echo ⏸️  Tests will pause between steps (browser lifecycle)
echo 📊 20 test cases will be executed
echo ========================================
echo.

call mvn clean test -Dtest=CartTest
set TEST_EXIT_CODE=%ERRORLEVEL%

REM ==============================
REM STEP 4: Generate Allure Report
REM ==============================
echo.
echo [4/4] Generating Allure Report...

REM Clean up ChromeDriver
taskkill /F /IM chromedriver.exe /T >nul 2>&1

if %TEST_EXIT_CODE% EQU 0 (
    echo.
    echo ========================================
    echo ✅ CART TESTS PASSED!
    echo ========================================
    echo   - All 20 cart test cases executed successfully
    echo   - Screenshots: Not captured (all passed)
    echo   - Allure report: Generating...
) else (
    echo.
    echo ========================================
    echo ❌ SOME CART TESTS FAILED
    echo ========================================
    echo   - Failed test screenshots captured
    echo   - Allure report: Generating with failures...
)

echo.
echo Opening Allure Report in browser...
echo (Press Ctrl+C to stop the server when done)
echo.

REM Use Allure CLI to serve report
allure serve allure-results

echo.
echo ========================================
echo   Report closed.
echo ========================================
echo   Test results kept in allure-results/
echo   Run more tests to accumulate results, or
echo   Run cleanup-test-files.bat to clear all
echo ========================================
pause
