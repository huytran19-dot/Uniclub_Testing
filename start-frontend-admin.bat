@echo off
echo ========================================
echo STARTING ADMIN FRONTEND
echo ========================================
echo.

REM Check if pnpm is installed
where pnpm >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] pnpm is not installed!
    echo Please install pnpm first:
    echo   npm install -g pnpm
    echo.
    pause
    exit /b 1
)

cd uniclub-fe\admin

echo Installing dependencies...
call pnpm install

echo.
echo Starting Admin development server...
echo Admin Frontend: http://localhost:5174
echo.

call pnpm dev
