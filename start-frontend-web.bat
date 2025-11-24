@echo off
echo ========================================
echo STARTING CUSTOMER FRONTEND (WEB)
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

cd uniclub-fe\web

echo Installing dependencies...
call pnpm install

echo.
echo Starting Customer development server...
echo Customer Frontend: http://localhost:5173
echo.

call pnpm dev
