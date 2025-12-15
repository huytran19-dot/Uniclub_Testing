# Hướng dẫn chạy Unit Test

Dự án sử dụng Maven và JUnit 5 để chạy unit tests. Dưới đây là các cách để chạy tests:

## 🚀 Cách nhanh nhất (Khuyến nghị)

Chỉ cần chạy các lệnh sau từ thư mục gốc của project:

```bash
# Chạy test thông thường
test.bat

# Chạy test với clean (CI mode)
test-ci.bat

# Chạy test nhanh (không clean)
test-quick.bat
```

**Đơn giản chỉ cần gõ:** `test.bat` hoặc `test-ci.bat` và Enter! 🎉

---

## 📋 Mục lục
1. [Chạy tất cả tests](#1-chạy-tất-cả-tests)
2. [Chạy một test class cụ thể](#2-chạy-một-test-class-cụ-thể)
3. [Chạy một test method cụ thể](#3-chạy-một-test-method-cụ-thể)
4. [Chạy tests với Maven Wrapper](#4-chạy-tests-với-maven-wrapper)
5. [Chạy tests từ IDE](#5-chạy-tests-từ-ide)
6. [Xem kết quả test](#6-xem-kết-quả-test)
7. [Các options hữu ích](#7-các-options-hữu-ích)

---

## 1. Chạy tất cả tests

### Sử dụng Maven Wrapper (khuyến nghị)
```bash
cd uniclub-be
.\mvnw.cmd test
```

### Sử dụng Maven (nếu đã cài đặt Maven)
```bash
cd uniclub-be
mvn test
```

### Chạy tests và bỏ qua compilation (nếu code đã compile)
```bash
cd uniclub-be
.\mvnw.cmd test -DskipTests=false
```

---

## 2. Chạy một test class cụ thể

### Chạy ProductServiceImplTest
```bash
cd uniclub-be
.\mvnw.cmd test -Dtest=ProductServiceImplTest
```

### Chạy OrderServiceImplTest
```bash
cd uniclub-be
.\mvnw.cmd test -Dtest=OrderServiceImplTest
```

### Chạy CartItemServiceImplTest
```bash
cd uniclub-be
.\mvnw.cmd test -Dtest=CartItemServiceImplTest
```

### Chạy CartServiceImplTest
```bash
cd uniclub-be
.\mvnw.cmd test -Dtest=CartServiceImplTest
```

### Chạy VariantServiceImplTest
```bash
cd uniclub-be
.\mvnw.cmd test -Dtest=VariantServiceImplTest
```

---

## 3. Chạy một test method cụ thể

### Chạy một method test cụ thể
```bash
cd uniclub-be
.\mvnw.cmd test -Dtest=ProductServiceImplTest#searchByName_shouldReturnExactMatch
```

### Chạy nhiều methods trong cùng một class
```bash
cd uniclub-be
.\mvnw.cmd test -Dtest=ProductServiceImplTest#searchByName_*+searchByDescription_*
```

---

## 4. Chạy tests với Maven Wrapper

### Windows (PowerShell/CMD)
```bash
cd uniclub-be
.\mvnw.cmd test
```

### Linux/Mac
```bash
cd uniclub-be
./mvnw test
```

---

## 5. Chạy tests từ IDE

### IntelliJ IDEA
1. Mở project trong IntelliJ IDEA
2. Navigate đến test class (ví dụ: `ProductServiceImplTest.java`)
3. Click chuột phải vào:
   - **Test class** → `Run 'ProductServiceImplTest'` để chạy tất cả tests trong class
   - **Test method** → `Run 'methodName()'` để chạy một method cụ thể
4. Hoặc click vào icon ▶️ bên cạnh class/method name

### Visual Studio Code
1. Cài đặt extension "Java Test Runner"
2. Mở test file
3. Click vào icon ▶️ bên cạnh test class/method
4. Hoặc sử dụng Command Palette: `Ctrl+Shift+P` → "Java: Run Tests"

### Eclipse
1. Mở test class
2. Click chuột phải → `Run As` → `JUnit Test`
3. Hoặc sử dụng shortcut: `Alt+Shift+X, T`

---

## 6. Xem kết quả test

### Trong Terminal
Sau khi chạy `mvn test`, kết quả sẽ hiển thị:
```
[INFO] Tests run: 25, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

### Xem báo cáo chi tiết
Sau khi chạy tests, báo cáo được lưu tại:
```
uniclub-be/target/surefire-reports/
```

### Xem báo cáo HTML (nếu có)
```bash
cd uniclub-be
.\mvnw.cmd surefire-report:report
# Sau đó mở file: target/site/surefire-report.html
```

---

## 7. Các options hữu ích

### Chạy tests và bỏ qua compilation
```bash
.\mvnw.cmd test -DskipTests=false -Dmaven.test.skip=false
```

### Chạy tests với verbose output (chi tiết hơn)
```bash
.\mvnw.cmd test -X
```

### Chạy tests và dừng khi có lỗi đầu tiên
```bash
.\mvnw.cmd test -Dmaven.test.failure.ignore=false
```

### Chạy tests với pattern matching
```bash
# Chạy tất cả tests có tên chứa "Product"
.\mvnw.cmd test -Dtest=*Product*Test

# Chạy tất cả tests trong package service
.\mvnw.cmd test -Dtest=com.uniclub.service.*Test
```

### Chạy tests và generate coverage report (nếu có plugin)
```bash
.\mvnw.cmd test jacoco:report
```

### Chạy tests với parallel execution (nhanh hơn)
```bash
.\mvnw.cmd test -Dparallel=classes -DthreadCount=4
```

---

## 📝 Danh sách các Test Classes

Dự án có các test classes sau:

1. **ProductServiceImplTest** - Test cho ProductService
   - Module 1: Tìm kiếm và lọc sản phẩm
   - Test cases: M1-01 đến M1-09

2. **CartItemServiceImplTest** - Test cho CartItemService
   - Module 2: Quản lý giỏ hàng
   - Test cases: M2-01 đến M2-12

3. **CartServiceImplTest** - Test cho CartService
   - Module 2: Quản lý giỏ hàng

4. **OrderServiceImplTest** - Test cho OrderService
   - Module 3: Đơn hàng
   - Test cases: M3-01 đến M3-06

5. **VariantServiceImplTest** - Test cho VariantService
   - Module 1: Lọc theo màu và size

6. **OrderControllerIntegrationTest** - Integration test cho OrderController

---

## 🔧 Troubleshooting

### Lỗi: "mvnw.cmd is not recognized"
- Đảm bảo bạn đang ở trong thư mục `uniclub-be`
- Kiểm tra file `mvnw.cmd` có tồn tại không

### Lỗi: "JAVA_HOME is not set"
- Cài đặt Java 21 (theo yêu cầu trong pom.xml)
- Set biến môi trường JAVA_HOME

### Tests chạy chậm
- Sử dụng parallel execution: `-Dparallel=classes`
- Chỉ chạy tests cần thiết thay vì tất cả

### Tests fail nhưng code đúng
- Xóa thư mục `target` và chạy lại: `.\mvnw.cmd clean test`
- Kiểm tra dependencies: `.\mvnw.cmd dependency:resolve`

---

## 📊 Ví dụ Output

Khi chạy thành công, bạn sẽ thấy:
```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.uniclub.service.ProductServiceImplTest
[INFO] Tests run: 15, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.5 s
[INFO] Running com.uniclub.service.OrderServiceImplTest
[INFO] Tests run: 12, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.8 s
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 27, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

---

## 🎯 Quick Reference

### Sử dụng Script (Dễ nhất)
```bash
# Từ thư mục gốc project
test.bat          # Chạy test thông thường
test-ci.bat       # Chạy test với clean (CI mode)
test-quick.bat    # Chạy test nhanh
```

### Sử dụng Maven trực tiếp
```bash
# Chạy tất cả tests
cd uniclub-be
.\mvnw.cmd test

# Chạy một test class
.\mvnw.cmd test -Dtest=ProductServiceImplTest

# Chạy một test method
.\mvnw.cmd test -Dtest=ProductServiceImplTest#searchByName_shouldReturnExactMatch

# Clean và chạy lại
.\mvnw.cmd clean test

# Xem báo cáo
.\mvnw.cmd surefire-report:report
```

### Tạo alias (tùy chọn)
Nếu muốn tạo alias ngắn hơn, thêm vào PowerShell profile:
```powershell
# Mở PowerShell profile
notepad $PROFILE

# Thêm dòng sau:
function test { cd "C:\New folder\Uniclub_Testing"; .\test.bat }
function test-ci { cd "C:\New folder\Uniclub_Testing"; .\test-ci.bat }

# Sau đó chỉ cần gõ: test hoặc test-ci
```

