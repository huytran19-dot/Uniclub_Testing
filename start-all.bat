@echo off
echo ========================================
echo STARTING UNICLUB APPLICATION
echo ========================================
echo.

echo Step 1: Starting MySQL Database...
call start-docker.bat

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
echo phpMyAdmin: http://localhost:8081
echo Backend API: http://localhost:8080
echo Frontend: http://localhost:5173
echo.
echo Please wait for all services to start...
echo Check the opened command windows for status.
echo.
echo Login credentials:
echo - Email: admin@uniclub.com
echo - Password: admin123
echo.
pause
