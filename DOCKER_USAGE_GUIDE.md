# 🐳 Hướng Dẫn Chạy UniClub với Docker

## 📋 Yêu Cầu Hệ Thống

- Docker Desktop (Windows/Mac) hoặc Docker Engine (Linux)
- Docker Compose v2.0+
- Ít nhất 4GB RAM khả dụng
- Ít nhất 10GB dung lượng ổ cứng

---

## 🚀 Cách 1: Chạy Toàn Bộ Hệ Thống (Recommended)

### Bước 1: Build và khởi động tất cả containers

```cmd
docker-compose up --build -d
```

**Giải thích:**
- `--build`: Build lại images từ Dockerfile
- `-d`: Chạy ở chế độ background (detached)

### Bước 2: Kiểm tra trạng thái containers

```cmd
docker-compose ps
```

Bạn sẽ thấy 4 containers:
- ✅ `uniclub-mysql` - Port 3307
- ✅ `uniclub-phpmyadmin` - Port 8081
- ✅ `uniclub-backend` - Port 8080
- ✅ `uniclub-frontend` - Port 80

### Bước 3: Xem logs (nếu cần debug)

```cmd
# Xem tất cả logs
docker-compose logs -f

# Xem logs của một service cụ thể
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f mysql
```

### Bước 4: Truy cập ứng dụng

- 🌐 **Web User**: http://localhost/
- 👨‍💼 **Admin Panel**: http://localhost/admin
- 🔧 **Backend API**: http://localhost:8080/api
- 🗄️ **PHPMyAdmin**: http://localhost:8081

### Bước 5: Dừng hệ thống

```cmd
# Dừng nhưng giữ lại data
docker-compose stop

# Dừng và xóa containers (giữ volumes)
docker-compose down

# Dừng và xóa tất cả (bao gồm database data)
docker-compose down -v
```

---

## 🛠️ Cách 2: Development Mode (Chạy từng phần)

### Chỉ chạy Database + PHPMyAdmin

```cmd
docker-compose up -d mysql phpmyadmin
```

Sau đó chạy backend và frontend bằng command line như bình thường:

```cmd
# Terminal 1 - Backend
cd uniclub-be
mvnw.cmd spring-boot:run

# Terminal 2 - Admin
cd uniclub-fe\admin
pnpm dev

# Terminal 3 - Web
cd uniclub-fe\web
pnpm dev
```

---

## 📊 Kiểm Tra Tình Trạng Hệ Thống

### Health Checks

```cmd
# Kiểm tra backend health
curl http://localhost:8080/actuator/health

# Hoặc mở trình duyệt
http://localhost:8080/actuator/health
```

### Thống kê tài nguyên

```cmd
docker stats
```

---

## 🐛 Troubleshooting

### 1. Container không start được

```cmd
# Xem logs chi tiết
docker-compose logs backend
docker-compose logs frontend

# Restart một container cụ thể
docker-compose restart backend
```

### 2. Port đã được sử dụng

Nếu gặp lỗi "port is already allocated", sửa trong `docker-compose.yml`:

```yaml
ports:
  - "8080:8080"  # Đổi thành "8081:8080" nếu port 8080 đã dùng
```

### 3. Database connection failed

```cmd
# Kiểm tra MySQL đã sẵn sàng chưa
docker-compose logs mysql

# Restart backend sau khi MySQL đã sẵn sàng
docker-compose restart backend
```

### 4. Frontend không connect được backend

Kiểm tra biến môi trường trong file `.env` của frontend:

```bash
# uniclub-fe/admin/.env
VITE_API_URL=http://localhost:8080/api

# uniclub-fe/web/.env
VITE_API_URL=http://localhost:8080/api
```

### 5. Rebuild lại một service cụ thể

```cmd
# Rebuild chỉ backend
docker-compose build backend
docker-compose up -d backend

# Rebuild chỉ frontend
docker-compose build frontend
docker-compose up -d frontend
```

---

## 🔄 Update Code và Rebuild

### Khi thay đổi code Backend:

```cmd
docker-compose build backend
docker-compose up -d backend
```

### Khi thay đổi code Frontend:

```cmd
docker-compose build frontend
docker-compose up -d frontend
```

### Rebuild toàn bộ:

```cmd
docker-compose up --build -d
```

---

## 📦 Quản Lý Data

### Backup Database

```cmd
docker exec uniclub-mysql mysqldump -u root -phuytran123 uniclub > backup.sql
```

### Restore Database

```cmd
docker exec -i uniclub-mysql mysql -u root -phuytran123 uniclub < backup.sql
```

### Xem volumes

```cmd
docker volume ls
docker volume inspect uniclub_testing_mysql_data
```

---

## 🧹 Dọn Dẹp

### Xóa containers và images không dùng

```cmd
# Xóa containers đã dừng
docker container prune

# Xóa images không dùng
docker image prune -a

# Xóa tất cả (cẩn thận!)
docker system prune -a --volumes
```

---

## 📝 Cấu Trúc Hệ Thống Docker

```
Uniclub_Testing/
├── docker-compose.yml          # Orchestration file
├── application-docker.yml      # Spring Boot config cho Docker
├── uniclub-be/
│   ├── Dockerfile             # Backend image definition
│   └── .dockerignore
├── uniclub-fe/
│   ├── Dockerfile             # Frontend image definition
│   ├── nginx.conf             # Nginx configuration
│   └── .dockerignore
└── mysql-init/
    └── init-database.sql      # Database initialization
```

---

## 🎯 Tips & Best Practices

1. **Development**: Sử dụng Cách 2 (chỉ chạy DB trong Docker)
2. **Testing**: Sử dụng Cách 1 (chạy toàn bộ trong Docker)
3. **Production**: Thêm reverse proxy (Nginx) và SSL certificate
4. **Monitoring**: Sử dụng `docker stats` để theo dõi tài nguyên
5. **Logs**: Luôn kiểm tra logs khi gặp lỗi: `docker-compose logs -f`

---

## 🌐 Environment Variables

Tạo file `.env` trong thư mục gốc nếu cần custom:

```env
# Database
DB_PASSWORD=your-password
MYSQL_ROOT_PASSWORD=your-password

# Backend
JWT_SECRET=your-secret-key
CLOUDINARY_CLOUD_NAME=your-cloudinary-name
VNPAY_TMN_CODE=your-vnpay-code

# Ports
BACKEND_PORT=8080
FRONTEND_PORT=80
MYSQL_PORT=3307
PHPMYADMIN_PORT=8081
```

Sau đó update `docker-compose.yml` để sử dụng `${VARIABLE_NAME}`.

---

## 📞 Hỗ Trợ

Nếu gặp vấn đề, kiểm tra:

1. Docker Desktop đã chạy chưa?
2. Ports có bị chiếm không? (`netstat -an | findstr :8080`)
3. Đủ RAM và disk space không?
4. Logs có thông báo lỗi gì? (`docker-compose logs`)

**Happy Dockerizing! 🐳**
