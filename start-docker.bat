@echo off
echo ========================================
echo     UNICLUB DOCKER SETUP
echo ========================================

echo.
echo [1/6] Starting MySQL and phpMyAdmin containers...
docker-compose up -d

echo.
echo [2/6] Waiting for MySQL to be ready...
timeout /t 15 /nobreak > nul

echo.
echo [3/6] Testing MySQL connection...
docker exec uniclub-mysql mysql -u root -phuytran123 -e "SELECT 'MySQL connection successful!' as status;"

if %errorlevel% neq 0 (
    echo ERROR: MySQL connection failed!
    echo Please check if the container is running properly.
    pause
    exit /b 1
)

echo.
echo [4/6] Checking if 'uniclub' database exists...
set db_exists=0
docker exec uniclub-mysql mysql -u root -phuytran123 -e "SHOW DATABASES LIKE 'uniclub';" > db_check.txt
for /f %%i in ('find /v /c "" ^< db_check.txt') do if %%i GTR 1 set db_exists=1
del db_check.txt

set data_imported=0

if %db_exists% equ 0 (
    echo Database 'uniclub' not found. Creating and importing data...
    set data_imported=1
    docker exec -i uniclub-mysql mysql -u root -phuytran123 < mysql-init/init-database.sql
    if %errorlevel% neq 0 (
        echo ERROR: Database creation and import failed!
        pause
        exit /b 1
    )
    echo Database 'uniclub' created and imported successfully!
) else (
    echo Database 'uniclub' already exists.
    echo.
    echo [5/6] Checking if database is empty...
    docker exec uniclub-mysql mysql -u root -phuytran123 -e "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'uniclub';" > temp_check.txt 2>nul
    for /f "skip=1" %%i in (temp_check.txt) do set table_count=%%i
    del temp_check.txt 2>nul

    if "%table_count%"=="0" (
        echo Database is empty, importing sample data...
        set data_imported=1
        docker exec -i uniclub-mysql mysql -u root -phuytran123 uniclub < mysql-init/init-database.sql
        
        if %errorlevel% neq 0 (
            echo ERROR: Database import failed!
            echo Please check the SQL file and try again.
            pause
            exit /b 1
        )
        echo Data imported successfully!
    ) else (
        echo Database already contains %table_count% tables, skipping import.
    )
)

echo.
echo [6/6] Finalizing setup...
echo phpMyAdmin should be available at: http://localhost:8081
echo Username: root
echo Password: huytran123

echo.
echo ========================================
echo     SETUP COMPLETED SUCCESSFULLY!
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

if %data_imported% equ 1 (
    echo Sample data has been imported:
    echo - 2 users (admin@uniclub.com, buyer@uniclub.com)
    echo - 6 brands, 6 categories, 11 colors, 6 sizes
    echo - 3 suppliers, 3 products, 9 variants
) else (
    echo Database already contains existing data.
    echo - No new data imported to preserve existing data.
)
echo.
echo You can now start your Spring Boot application!
echo.
pause