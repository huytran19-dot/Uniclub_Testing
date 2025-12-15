# 🧪 Chạy Unit Test - Hướng dẫn nhanh

## ⚡ Cách nhanh nhất

Từ thư mục gốc project, chỉ cần chạy:

```bash
test.bat
```

Hoặc:

```bash
test-ci.bat
```

## 📝 Các lệnh có sẵn

| Lệnh | Mô tả |
|------|-------|
| `test.bat` | Chạy test thông thường |
| `test-ci.bat` | Chạy test với clean (dùng cho CI/CD) |
| `test-quick.bat` | Chạy test nhanh (không clean) |

## 🔍 Chạy test cụ thể

Nếu muốn chạy một test class cụ thể:

```bash
cd uniclub-be
.\mvnw.cmd test -Dtest=ProductServiceImplTest
```

## 📊 Xem kết quả

Sau khi chạy, kết quả sẽ hiển thị trong terminal. Báo cáo chi tiết tại:
```
uniclub-be/target/surefire-reports/
```

## ❓ Cần thêm thông tin?

Xem file `TESTING_GUIDE.md` để biết thêm chi tiết.


