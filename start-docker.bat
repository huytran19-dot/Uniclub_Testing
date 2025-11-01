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
docker exec uniclub-mysql mysql -u root -phuytran123 -e "SELECT 'MySQL connection successful!' as status;"

if %errorlevel% neq 0 (
    echo ERROR: MySQL connection failed!
    echo Please check if the container is running properly.
    pause
    exit /b 1
)

echo.
echo [4/5] Checking if database needs initialization...
docker exec uniclub-mysql mysql -u root -phuytran123 -e "USE uniclub; SELECT COUNT(*) as table_count FROM information_schema.tables WHERE table_schema = 'uniclub';" > temp_check.txt 2>nul

for /f "tokens=*" %%i in (temp_check.txt) do set table_count=%%i
del temp_check.txt 2>nul

if "%table_count%"=="0" (
    echo Database is empty, importing schema and sample data...
    docker exec -i uniclub-mysql mysql -u root -phuytran123 uniclub < mysql-init/init-database.sql
    
    if %errorlevel% neq 0 (
        echo ERROR: Database import failed!
        echo Please check the SQL file and try again.
        pause
        exit /b 1
    )
    echo Database imported successfully!
) else (
    echo Database already contains %table_count% tables, skipping import.
)

echo.
echo [5/5] Testing phpMyAdmin connection...
echo phpMyAdmin should be available at: http://localhost:8081
echo Username: root
echo Password: huytran123

echo.
echo ========================================
echo    SETUP COMPLETED SUCCESSFULLY!
echo ========================================
echo.
echo Database Information:
echo - Host: localhost
echo - Port: 3307
echo - Database: uniclub
echo - Username: root
echo - Password: huytran123
echo.
echo phpMyAdmin: http://localhost:8081
echo.
if "%table_count%"=="0" (
    echo Sample data has been imported:
    echo - 4 users (admin@uniclub.com, buyer@uniclub.com, etc.)
    echo - 6 brands, 6 categories, 11 colors, 6 sizes
    echo - 3 suppliers, 3 products, 9 variants
) else (
    echo Database already contains existing data.
    echo - %table_count% tables found
    echo - No new data imported to preserve existing data
)
echo.
echo You can now start your Spring Boot application!
echo.
pause