# 💾 Hướng Dẫn Chạy Test An Toàn - Bảo Vệ Ổ Cứng

## 🎯 Vấn Đề & Giải Pháp

### ⚠️ **Vấn đề:**
- Chrome lưu data vào ổ C → Làm đầy ổ cứng
- Test chạy nhiều → Tạo nhiều file tạm
- Screenshot + log files → Chiếm dung lượng

### ✅ **Giải pháp đã áp dụng:**

1. **Chrome data chuyển sang ổ D:**
   ```java
   // BaseTest.java - Line 162
   --user-data-dir=D:\temp\selenium-chrome-profile
   --disk-cache-dir=D:\temp\chrome-cache
   ```
   
2. **Screenshot chỉ khi FAIL:**
   - Test PASSED: ❌ Không chụp (tiết kiệm 95% dung lượng)
   - Test FAILED: ✅ Chụp để debug

3. **Auto cleanup sau mỗi lần chạy:**
   - Xóa Chrome temp files
   - Xóa test results cũ
   - Xóa allure-results

---

## 🚀 Cách Chạy Test An Toàn

### **Phương pháp 1: SAFE MODE (Khuyến nghị)**

```bash
cd D:\uniclub\selenium-tests
.\run-test-safe.bat
```

**Tính năng:**
- ✅ Kiểm tra dung lượng ổ đĩa trước khi chạy
- ✅ Xóa Chrome temp files TRƯỚC test
- ✅ Xóa Chrome temp files SAU test
- ✅ Hiển thị báo cáo dung lượng
- ✅ Tự động cleanup hoàn toàn

**Kết quả:**
```
[1/5] Pre-test cleanup...
  - Cleaned Chrome profile data
  - Cleaned Chrome cache
  ✓ Pre-cleanup completed

[2/5] Checking available disk space...
  C: drive free space: 15 GB
  D: drive free space: 250 GB
  ✓ Disk space check completed

[3/5] Running tests...
  ... tests running ...

[4/5] Post-test cleanup...
  - Cleaned Chrome profile data
  - Cleaned Chrome cache
  ✓ Post-cleanup completed

✅ TESTS PASSED - All Safe!
💾 Disk Space Protection Summary:
  - Chrome profile: D:\temp (auto-cleaned)
  - Chrome cache:   D:\temp (auto-cleaned)
  - C: drive remains protected ✓
```

---

### **Phương pháp 2: Chạy với Allure Report**

```bash
.\run-tests-with-allure.bat
```

**Tính năng:**
- Chạy test + Tạo report đẹp
- Cleanup tự động
- Mở browser xem kết quả

**Lưu ý:** Sau khi xem report xong, nhấn `Ctrl+C` để tắt server

---

### **Phương pháp 3: Chạy thủ công (Advanced)**

```bash
# Cleanup trước
rmdir /s /q D:\temp\selenium-chrome-profile
rmdir /s /q D:\temp\chrome-cache

# Chạy test
mvn test -Dtest=UserLoginTest

# Cleanup sau
rmdir /s /q D:\temp\selenium-chrome-profile
rmdir /s /q allure-results
```

---

## 📊 So Sánh Dung Lượng

### **TRƯỚC khi tối ưu:**
| Vị trí | Dung lượng | Ảnh hưởng |
|--------|-----------|-----------|
| C:\Users\...\AppData\Local\Temp | 5-10 GB | ⚠️ Đầy ổ C |
| C:\Users\...\Chrome\User Data | 2-5 GB | ⚠️ Đầy ổ C |
| Screenshots (mọi test) | 50-100 MB/run | ⚠️ Lãng phí |
| **Tổng** | **10-15 GB** | **❌ Nguy hiểm** |

### **SAU khi tối ưu:**
| Vị trí | Dung lượng | Ảnh hưởng |
|--------|-----------|-----------|
| D:\temp\selenium-chrome-profile | 100-500 MB | ✅ Auto-cleaned |
| D:\temp\chrome-cache | 50-200 MB | ✅ Auto-cleaned |
| Screenshots (chỉ failures) | 2-5 MB/run | ✅ Tiết kiệm 95% |
| **Tổng** | **< 1 GB** | **✅ An toàn** |

---

## 🛡️ Các Biện Pháp Bảo Vệ

### **1. Chrome Data → Ổ D**
```java
// Trong BaseTest.java
chromeOptions.addArguments("--user-data-dir=D:\\temp\\selenium-chrome-profile");
chromeOptions.addArguments("--disk-cache-dir=D:\\temp\\chrome-cache");
```

**Kết quả:** Chrome KHÔNG bao giờ ghi vào ổ C

---

### **2. Screenshot Thông Minh**
```java
// Trong AllureTestListener.java
@Override
public void onTestFailure(ITestResult result) {
    // Chỉ chụp khi FAIL
    takeScreenshot();
}

@Override
public void onTestSuccess(ITestResult result) {
    // KHÔNG chụp khi PASS
}
```

**Tiết kiệm:** 95% dung lượng so với chụp mọi test

---

### **3. Auto Cleanup**

**Trong run-test-safe.bat:**
```batch
REM Trước test: Xóa Chrome temp
rmdir /s /q "D:\temp\selenium-chrome-profile"

REM Chạy test
mvn test

REM Sau test: Xóa Chrome temp + results
rmdir /s /q "D:\temp\selenium-chrome-profile"
rmdir /s /q "allure-results"
```

---

## ⚙️ Cấu Hình Nâng Cao

### **Nếu vẫn lo lắng về dung lượng:**

1. **Giới hạn số lượng screenshot:**
   ```java
   // Trong AllureTestListener.java
   private static int screenshotCount = 0;
   private static final int MAX_SCREENSHOTS = 10;
   
   if (screenshotCount < MAX_SCREENSHOTS) {
       takeScreenshot();
       screenshotCount++;
   }
   ```

2. **Chạy test ở chế độ headless (không mở Chrome):**
   ```properties
   # Trong config/config.properties
   headless=true
   ```
   **Lợi ích:** Ít tốn RAM, chạy nhanh hơn, ít file tạm hơn

3. **Tự động xóa file cũ hơn 7 ngày:**
   ```batch
   REM Thêm vào run-test-safe.bat
   forfiles /p "D:\temp" /s /m *.* /d -7 /c "cmd /c del @path" 2>nul
   ```

---

## 🔍 Cách Kiểm Tra Dung Lượng

### **Kiểm tra nhanh:**
```powershell
# Xem dung lượng các thư mục test
$folders = @(
    "D:\uniclub\selenium-tests\target",
    "D:\uniclub\selenium-tests\allure-results",
    "D:\temp\selenium-chrome-profile",
    "D:\temp\chrome-cache"
)
foreach ($folder in $folders) {
    if (Test-Path $folder) {
        $size = (Get-ChildItem $folder -Recurse | Measure-Object -Property Length -Sum).Sum / 1MB
        Write-Host "$($folder): $([math]::Round($size, 2)) MB"
    }
}
```

### **Cleanup thủ công nếu cần:**
```powershell
# Xóa tất cả Chrome temp data
Remove-Item "D:\temp\selenium-chrome-profile" -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item "D:\temp\chrome-cache" -Recurse -Force -ErrorAction SilentlyContinue

# Xóa test results cũ
Remove-Item "D:\uniclub\selenium-tests\allure-results" -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item "D:\uniclub\selenium-tests\test-output" -Recurse -Force -ErrorAction SilentlyContinue
```

---

## ✅ Checklist An Toàn

- [x] Chrome data lưu vào ổ D (không phải C)
- [x] Screenshot chỉ khi test FAIL
- [x] Auto cleanup sau mỗi lần chạy
- [x] Kiểm tra dung lượng trước khi chạy test
- [x] Singleton driver pattern (giảm số lần mở Chrome)
- [x] Batch file an toàn: run-test-safe.bat

---

## 🎯 Khuyến Nghị Cuối Cùng

### **Để chạy test an toàn nhất:**

1. **Luôn dùng `run-test-safe.bat`:**
   ```bash
   .\run-test-safe.bat
   ```

2. **Kiểm tra dung lượng định kỳ:**
   ```bash
   # Mỗi tuần chạy 1 lần
   cleanmgr /d D:
   ```

3. **Nếu có nhiều test case, chạy headless:**
   ```properties
   # config.properties
   headless=true
   ```

4. **Sau khi xem Allure report, nhớ tắt server:**
   - Nhấn `Ctrl+C` trong terminal
   - Hoặc đóng terminal

---

## 📞 Tóm Tắt Lệnh

| Mục đích | Lệnh |
|----------|------|
| Chạy test an toàn | `.\run-test-safe.bat` |
| Chạy test + report | `.\run-tests-with-allure.bat` |
| Cleanup thủ công | `rmdir /s /q D:\temp\selenium-chrome-profile` |
| Kiểm tra dung lượng | `dir D:\temp /s` |
| Disk cleanup | `cleanmgr /d D:` |

---

## 🎉 Kết Luận

Với cấu hình hiện tại:
- ✅ **Ổ C: An toàn** - Chrome không ghi vào C:
- ✅ **Ổ D: Tự động dọn** - Cleanup sau mỗi lần chạy
- ✅ **Screenshot: Thông minh** - Chỉ chụp khi fail
- ✅ **Performance: Tối ưu** - Singleton driver pattern

**Chạy test thoải mái không lo đầy ổ cứng!** 🚀
