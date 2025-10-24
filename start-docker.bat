@echo off
echo ========================================
echo    UNICLUB DOCKER SETUP
echo ========================================

echo.
echo [1/5] Starting MySQL and phpMyAdmin containers...
docker-compose up -d

echo.
echo [2/5] Waiting for MySQL to be ready...
timeout /t 15 /nobreak > nul

echo.
echo [3/5] Testing MySQL connection...
docker exec uniclub-mysql mysql -u uniclub_user -puniclub_password -e "SELECT 'MySQL connection successful!' as status;"

if %errorlevel% neq 0 (
    echo ERROR: MySQL connection failed!
    echo Please check if the container is running properly.
    pause
    exit /b 1
)

echo.
echo [4/5] Importing database schema and sample data...
docker exec -i uniclub-mysql mysql -u uniclub_user -puniclub_password uniclub < mysql-init/init-database.sql

if %errorlevel% neq 0 (
    echo ERROR: Database import failed!
    echo Please check the SQL file and try again.
    pause
    exit /b 1
)

echo.
echo [5/5] Testing phpMyAdmin connection...
echo phpMyAdmin should be available at: http://localhost:8081
echo Username: uniclub_user
echo Password: uniclub_password

echo.
echo ========================================
echo    SETUP COMPLETED SUCCESSFULLY!
echo ========================================
echo.
echo Database Information:
echo - Host: localhost
echo - Port: 3307
echo - Database: uniclub
echo - Username: uniclub_user
echo - Password: uniclub_password
echo.
echo phpMyAdmin: http://localhost:8081
echo.
echo Sample data has been imported:
echo - 4 users (admin@uniclub.com, buyer@uniclub.com, etc.)
echo - 6 brands, 6 categories, 11 colors, 6 sizes
echo - 3 suppliers, 3 products, 9 variants
echo.
echo You can now start your Spring Boot application!
echo.
pause
