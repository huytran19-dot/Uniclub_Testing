@echo off
echo ========================================
echo STARTING UNICLUB DEVELOPMENT MODE
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

echo Step 1: Starting MySQL Database and phpMyAdmin (Docker)...
echo Starting only database containers...
docker-compose up -d mysql phpmyadmin

echo.
echo Waiting for MySQL to be ready...
timeout /t 5 /nobreak > nul

echo Testing MySQL connection...
:wait_mysql
docker exec uniclub-mysql mysql -u root -phuytran123 -e "SELECT 1" >nul 2>&1
if %errorlevel% neq 0 (
    echo MySQL is not ready yet, waiting...
    timeout /t 3 /nobreak > nul
    goto wait_mysql
)

echo [OK] MySQL is ready!
echo.
echo Step 2: Starting Spring Boot Backend (Dev Mode)...
start "Backend" cmd /k "cd /d %~dp0 && start-backend.bat"

echo.
echo Step 3: Starting React Frontend (Dev Mode)...
start "Frontend" cmd /k "cd /d %~dp0 && start-frontend.bat"

echo.
echo ========================================
echo DEVELOPMENT MODE STARTED
echo ========================================
echo.
echo MySQL Database: localhost:3307
echo phpMyAdmin: http://localhost:8081/
echo Backend API: http://localhost:8080/ (Dev Mode - Hot Reload)
echo Customer Frontend: http://localhost:5173/ (Dev Mode - Hot Reload)
echo Admin Frontend: http://localhost:5174/ (Dev Mode - Hot Reload)
echo.
echo NOTE: This is DEVELOPMENT mode with hot reload.
echo For PRODUCTION mode with Docker, run: docker-compose up -d
echo.
echo Please wait for all services to start...
echo Check the opened command windows for status.
echo.
echo Login credentials:
echo - Email: admin@uniclub.com
echo - Password: Admin@123
echo.
pause