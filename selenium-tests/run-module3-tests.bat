@echo off
REM Change to script directory
cd /d "%~dp0"

echo ========================================
echo Module 3: Order Checkout Tests
echo ========================================
echo.

echo [INFO] Running all test cases for Module 3: Đơn hàng
echo.
echo Test Cases:
echo   M3-01: Tạo đơn hàng thành công (COD)
echo   M3-02: Tạo đơn hàng thành công (VNPay)
echo   M3-03: Tạo đơn hàng với giỏ hàng rỗng
echo   M3-04: Tạo đơn hàng thiếu thông tin billing
echo   M3-05: Tạo đơn hàng khi sản phẩm hết hàng
echo   M3-06: Hủy đơn hàng (User)
echo.

REM Run tests
echo [1/2] Running OrderCheckoutTest...
echo ========================================
call mvn clean test -Dtest=OrderCheckoutTest -Dheadless=false

set TEST_EXIT_CODE=%ERRORLEVEL%

REM Generate report
echo.
echo [2/2] Generating Allure Report...
echo.

if %TEST_EXIT_CODE% EQU 0 (
    echo ✅ ALL TESTS PASSED!
) else (
    echo ❌ SOME TESTS FAILED
)

echo.
echo Opening Allure Report...
allure serve allure-results

echo.
echo ========================================
echo Test execution completed!
echo ========================================
pause
