@echo off
echo ========================================
echo STARTING UNICLUB APPLICATION
echo ========================================
echo.

REM Check prerequisites
echo Checking prerequisites...

where docker >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Docker is not installed or not running!
    echo Please install Docker Desktop from: https://www.docker.com/products/docker-desktop
    pause
    exit /b 1
)

where java >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Java is not installed!
    echo Please install Java 17+ from: https://adoptium.net/
    pause
    exit /b 1
)

where pnpm >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] pnpm is not installed!
    echo Please install pnpm: npm install -g pnpm
    pause
    exit /b 1
)

echo [OK] All prerequisites are installed!
echo.

echo Step 1: Starting MySQL Database...
start "Database" cmd /k "cd /d %~dp0 && start-docker.bat"

echo.
echo Waiting 10 seconds for database to initialize...
timeout /t 10 /nobreak > nul

echo.
echo Step 2: Starting Spring Boot Backend...
start "Backend" cmd /k "cd /d %~dp0 && start-backend.bat"

echo.
echo Step 3: Starting React Frontend...
start "Frontend" cmd /k "cd /d %~dp0 && start-frontend.bat"

echo.
echo ========================================
echo ALL SERVICES STARTING...
echo ========================================
echo.
echo MySQL Database: localhost:3307
echo phpMyAdmin: http://localhost:8081/
echo Backend API: http://localhost:8080/
echo Customer Frontend: http://localhost:5173/
echo Admin Frontend: http://localhost:5174/
echo.
echo Please wait for all services to start...
echo Check the opened command windows for status.
echo.
echo Login credentials:
echo - Email: admin@uniclub.com
echo - Password: 123456
echo.
pause