@echo off
echo ========================================
echo STARTING CUSTOMER FRONTEND (WEB)
echo ========================================
echo.

cd uniclub-fe\web

echo Installing dependencies...
call pnpm install

echo.
echo Starting Customer development server...
echo Customer Frontend: http://localhost:5173
echo.

call pnpm dev
