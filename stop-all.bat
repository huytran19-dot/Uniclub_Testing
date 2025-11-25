@echo off
echo ========================================
echo STOPPING ALL UNICLUB SERVICES
echo ========================================
echo.

echo Stopping Docker containers...
docker-compose down

echo.
echo Killing Java processes (Backend)...
taskkill /F /IM java.exe 2>nul
if %ERRORLEVEL% EQU 0 (
    echo [OK] Backend stopped
) else (
    echo [INFO] No backend process found
)

echo.
echo Killing Node processes (Frontend)...
taskkill /F /IM node.exe 2>nul
if %ERRORLEVEL% EQU 0 (
    echo [OK] Frontend stopped
) else (
    echo [INFO] No frontend process found
)

echo.
echo ========================================
echo ALL SERVICES STOPPED
echo ========================================
echo.
pause
