@echo off
echo.
echo ========================================
echo   CLEANING UP SELENIUM TEST FILES
echo ========================================
echo.

:: Change to script directory
cd /d "%~dp0"

echo [1/6] Deleting allure-results...
if exist "allure-results" (
    rmdir /s /q "allure-results"
    echo    - Deleted allure-results/
) else (
    echo    - allure-results/ not found
)

echo.
echo [2/6] Deleting allure-report...
if exist "allure-report" (
    rmdir /s /q "allure-report"
    echo    - Deleted allure-report/
) else (
    echo    - allure-report/ not found
)

echo.
echo [3/6] Deleting Maven target...
if exist "target" (
    rmdir /s /q "target"
    echo    - Deleted target/
) else (
    echo    - target/ not found
)

echo.
echo [4/6] Deleting Chrome profile data...
if exist "D:\temp\selenium-chrome-profile" (
    rmdir /s /q "D:\temp\selenium-chrome-profile"
    echo    - Deleted D:\temp\selenium-chrome-profile/
) else (
    echo    - Chrome profile not found
)

echo.
echo [5/6] Deleting Chrome cache...
if exist "D:\temp\chrome-cache" (
    rmdir /s /q "D:\temp\chrome-cache"
    echo    - Deleted D:\temp\chrome-cache/
) else (
    echo    - Chrome cache not found
)

echo.
echo [6/8] Deleting Maven temp files...
if exist "D:\temp\maven-tests" (
    rmdir /s /q "D:\temp\maven-tests"
    echo    - Deleted D:\temp\maven-tests/
) else (
    echo    - Maven temp not found
)

echo.
echo [7/8] Deleting Allure cache...
if exist ".allure" (
    rmdir /s /q ".allure"
    echo    - Deleted .allure/
) else (
    echo    - Allure cache not found
)

echo.
echo [8/8] Killing remaining processes...
taskkill /F /IM chromedriver.exe /T >nul 2>&1
taskkill /F /IM chrome.exe /T >nul 2>&1
taskkill /F /IM allure.exe /T >nul 2>&1
echo    - Killed chromedriver, chrome, allure processes

echo.
echo ========================================
echo   ✅ CLEANUP COMPLETED!
echo ========================================
echo   ✓ All test files removed from project
echo   ✓ All temp files removed from D:\temp\
echo   ✓ All processes terminated
echo   ✓ Disk space freed
echo ========================================
echo.
echo You can now run tests fresh.
echo.
pause
