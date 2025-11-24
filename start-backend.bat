@echo off
echo ========================================
echo STARTING SPRING BOOT BACKEND
echo ========================================
echo.

cd uniclub-be

echo Building Spring Boot application...
call mvn clean compile

echo.
echo Starting Spring Boot application...
echo Backend will be available at: http://localhost:8080
echo API endpoints: http://localhost:8080/api
echo.

call mvn spring-boot:run -X
