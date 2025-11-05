@echo off
echo ========================================
echo STARTING ADMIN FRONTEND
echo ========================================
echo.

cd uniclub-fe\admin

echo Installing dependencies...
call pnpm install

echo.
echo Starting Admin development server...
echo Admin Frontend: http://localhost:5174
echo.

call pnpm dev
