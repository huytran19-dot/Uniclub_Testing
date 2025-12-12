# 🚀 Hướng Dẫn Chạy User Login/Logout Tests

## ✅ Yêu Cầu Trước Khi Chạy Test

### **1. Khởi động Backend (Port 8080)**
```powershell
cd d:\uniclub\uniclub-be
mvn spring-boot:run
```

### **2. Khởi động Web Frontend (Port 5173) - BẮT BUỘC!**
```powershell
cd d:\uniclub\uniclub-fe\web
npm run dev
```

Hoặc sử dụng start script:
```powershell
cd d:\uniclub
.\start-frontend-web.bat
```

### **3. Khởi động Admin Frontend (Port 5174) - Nếu test Admin**
```powershell
cd d:\uniclub\uniclub-fe\admin
npm run dev
```

---

## 🧪 Chạy Tests

### **Test User Login/Logout (Web - Port 5173):**
```powershell
cd d:\uniclub\selenium-tests
mvn test -Dtest=UserLoginTest
```

### **Test Admin Login/Logout (Admin - Port 5174):**
```powershell
cd d:\uniclub\selenium-tests
mvn test -Dtest=LoginTest
```

### **Chạy TẤT CẢ tests (Admin + User):**
```powershell
cd d:\uniclub\selenium-tests
mvn test
```

---

## 📊 Test Cases Cho User Login/Logout

| Test ID | Test Case | Mô tả |
|---------|-----------|-------|
| **USER_AUTH_01** | User login thành công | Login với credentials hợp lệ |
| **USER_AUTH_02** | Login thất bại - sai password | Test với wrong password |
| **USER_AUTH_03** | Login thất bại - sai email | Test với wrong email |
| **USER_AUTH_04** | Validation - form rỗng | Submit form không có data |
| **USER_AUTH_05** | Validation - password rỗng | Email có, password rỗng |
| **USER_AUTH_06** | Validation - email rỗng | Password có, email rỗng |
| **USER_AUTH_07** | UI elements hiển thị | Kiểm tra tất cả elements |
| **USER_AUTH_08** | User logout thành công | Logout và clear session |
| **USER_AUTH_09** | Password visibility toggle | Show/hide password |
| **USER_AUTH_10** | SQL injection prevention | Security test |

**Tổng: 10 test cases**

---

## 🎯 Kiểm Tra Trước Khi Chạy Test

### **1. Kiểm tra Backend đang chạy:**
```powershell
curl http://localhost:8080
```

### **2. Kiểm tra Web Frontend đang chạy:**
```powershell
curl http://localhost:5173
```

### **3. Kiểm tra Admin Frontend đang chạy:**
```powershell
curl http://localhost:5174
```

---

## ⚙️ Cấu Hình Test Data

File: `selenium-tests/config/config.properties`

```properties
# Web User Credentials (Port 5173)
user.email=user@uniclub.vn
user.password=user123

# Admin Credentials (Port 5174)
admin.email=admin@uniclub.vn
admin.password=admin123

# URLs
web.url=http://localhost:5173
admin.url=http://localhost:5174
backend.url=http://localhost:8080
```

---

## 🐛 Xử Lý Lỗi Thường Gặp

### **Lỗi: `ERR_CONNECTION_REFUSED`**
```
unknown error: net::ERR_CONNECTION_REFUSED
```

**Nguyên nhân:** Web Frontend (port 5173) chưa chạy

**Giải pháp:**
```powershell
cd d:\uniclub\uniclub-fe\web
npm run dev
```

---

### **Lỗi: `Element not found`**
**Nguyên nhân:** Page chưa load xong hoặc locator sai

**Giải pháp:** Tăng thời gian wait trong config:
```properties
explicit.wait=30
```

---

### **Lỗi: Login thất bại**
**Nguyên nhân:** User credentials không đúng hoặc user chưa tồn tại

**Giải pháp:** 
1. Kiểm tra user đã tồn tại trong database
2. Hoặc tạo user mới qua Register page
3. Hoặc cập nhật credentials trong `config.properties`

---

## 📝 Lưu Ý

1. **Phải khởi động Web Frontend** trước khi chạy User Login tests
2. **Phải khởi động Admin Frontend** trước khi chạy Admin Login tests
3. **Backend phải chạy** cho cả 2 loại tests
4. Tests có thể chạy song song nếu cả 2 frontend đều chạy

---

## ✅ Kết Quả Mong Đợi

```
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS

✅ USER_AUTH_01: testUserLoginSuccess - PASSED
✅ USER_AUTH_02: testUserLoginWithInvalidPassword - PASSED
✅ USER_AUTH_03: testUserLoginWithInvalidEmail - PASSED
✅ USER_AUTH_04: testUserLoginWithEmptyCredentials - PASSED
✅ USER_AUTH_05: testUserLoginWithEmptyPassword - PASSED
✅ USER_AUTH_06: testUserLoginWithEmptyEmail - PASSED
✅ USER_AUTH_07: testUserLoginPageElements - PASSED
✅ USER_AUTH_08: testUserLogoutSuccess - PASSED
✅ USER_AUTH_09: testPasswordVisibilityToggle - PASSED
✅ USER_AUTH_10: testSQLInjectionPrevention - PASSED
```

---

## 🚀 Quick Start

```powershell
# Terminal 1: Start Backend
cd d:\uniclub\uniclub-be
mvn spring-boot:run

# Terminal 2: Start Web Frontend
cd d:\uniclub\uniclub-fe\web
npm run dev

# Terminal 3: Run Tests
cd d:\uniclub\selenium-tests
mvn test -Dtest=UserLoginTest

# Terminal 4: View Report
start target\surefire-reports\index.html
```
