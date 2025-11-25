# 🎯 Hướng dẫn chạy Uniclub - Chọn chế độ phù hợp

## 📌 TÓM TẮT

Có **2 cách** để chạy ứng dụng:

### 1️⃣ Development Mode (Môi trường Dev - Hot Reload)
**Khi nào dùng:**
- ✅ Đang code và cần xem thay đổi ngay lập tức
- ✅ Cần debug trong IDE
- ✅ Đang phát triển tính năng mới

**Cách chạy:**
```bash
start-all.bat
```

**Truy cập:**
- Web: http://localhost:5173
- Admin: http://localhost:5174
- Backend: http://localhost:8080

---

### 2️⃣ Docker Mode (Production-like)
**Khi nào dùng:**
- ✅ Demo cho giáo viên/nhóm
- ✅ Testing môn "Kiểm thử phần mềm"
- ✅ Muốn chạy như production

**Cách chạy:**
```bash
start-docker-all.bat
```
Hoặc:
```bash
docker-compose up -d
```

**Truy cập:**
- Web: http://localhost
- Admin: http://localhost/admin
- Backend API: http://localhost/api

---

## 🛑 Stop tất cả

```bash
stop-all.bat
```

---

## ⚠️ LƯU Ý QUAN TRỌNG

### ❌ KHÔNG được chạy cả 2 cùng lúc!

Nếu đang chạy Docker mode và muốn chuyển sang Dev mode:

```bash
# 1. Stop Docker trước
docker-compose down

# 2. Sau đó mới chạy Dev mode
start-all.bat
```

Nếu đang chạy Dev mode và muốn chuyển sang Docker mode:

```bash
# 1. Stop Dev mode
stop-all.bat

# 2. Sau đó mới chạy Docker
start-docker-all.bat
```

---

## 📊 So sánh 2 chế độ

| Tính năng | Dev Mode | Docker Mode |
|-----------|----------|-------------|
| **Hot Reload** | ✅ Có | ❌ Không |
| **Debug trong IDE** | ✅ Dễ | ❌ Khó |
| **Tốc độ khởi động** | 🐢 Chậm hơn | 🚀 Nhanh hơn |
| **Giống Production** | ❌ Không | ✅ Có |
| **URL Frontend** | localhost:5173, :5174 | localhost, /admin |
| **URL Backend** | localhost:8080 | localhost/api |

---

## 🔑 Tài khoản đăng nhập

- **Email:** admin@uniclub.com
- **Password:** REPLACE_ME_ADMIN_PASSWORD

---

## 📝 Files quan trọng

- `start-all.bat` - Chạy Dev Mode (MySQL Docker + Backend + Frontend dev)
- `start-docker-all.bat` - Chạy Docker Mode (toàn bộ trong Docker)
- `stop-all.bat` - Stop tất cả services
- `docker-compose.yml` - Cấu hình Docker containers

---

## 🆘 Gặp lỗi?

### Lỗi: Port 8080 đã bị chiếm

**Nguyên nhân:** Đang chạy cả 2 chế độ cùng lúc.

**Giải pháp:**
```bash
# Stop tất cả
stop-all.bat

# Chọn 1 trong 2 chế độ để chạy lại
```

### Lỗi: Frontend không kết nối được Backend

**Dev Mode:** Kiểm tra Backend đang chạy ở port 8080
**Docker Mode:** Kiểm tra tất cả containers đang chạy:
```bash
docker-compose ps
```

### Xem logs

**Dev Mode:** Xem trong terminal windows
**Docker Mode:**
```bash
docker-compose logs -f backend
docker-compose logs -f frontend
```
