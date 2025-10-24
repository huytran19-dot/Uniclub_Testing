@echo off
echo ========================================
echo STARTING REACT FRONTEND
echo ========================================
echo.

cd uniclub-fe\admin

echo Installing dependencies...
call pnpm install

echo.
echo Starting React development server...
echo Frontend will be available at: http://localhost:5173
echo.

call pnpm dev
