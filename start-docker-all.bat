@echo off
echo ========================================
echo STARTING UNICLUB - DOCKER MODE
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

echo [OK] Docker is installed and running!
echo.

echo Starting all containers in PRODUCTION mode...
echo This will start: MySQL, phpMyAdmin, Backend, and Frontend
echo.

docker-compose --profile production up -d

echo.
echo Waiting for services to start...
timeout /t 5 /nobreak > nul

echo.
echo ========================================
echo DOCKER MODE STARTED
echo ========================================
echo.
echo All services are running in Docker containers:
echo.
echo MySQL Database: localhost:3307
echo phpMyAdmin: http://localhost:8081/
echo Frontend (Web + Admin): http://localhost/
echo Admin Panel: http://localhost/admin/
echo Backend API: http://localhost/api/
echo Backend Health Check: http://localhost/api/actuator/health
echo.
echo Login credentials:
echo - Email: admin@uniclub.com
echo - Password: Admin@123
echo.
echo To view logs: docker-compose logs -f
echo To stop all: docker-compose down
echo.
pause
