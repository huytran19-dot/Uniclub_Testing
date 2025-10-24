# Hướng dẫn kết nối MySQL trong Docker

## Cấu hình hiện tại
- MySQL container chạy trên port **3307:3306**
- Database name: `uniclub`
- Username: `root`
- Password: `huytran123`

## Database Schema
Database đã được thiết lập với đầy đủ các bảng:
- **Bảng cơ bản**: size, color, brand, category, role
- **User**: user (với role)
- **Product**: product, variant (SKU)
- **Supplier**: supplier
- **Order**: orders, order_variant, payment, billing_detail
- **Review & Comment**: review, comment
- **Cart**: cart, cart_item
- **GRN (Nhập hàng)**: grn_header, grn_detail

## Các bước thực hiện

### 1. Khởi động MySQL container
```bash
# Chạy script tự động
start-docker.bat

# Hoặc chạy thủ công
docker-compose up -d mysql
```

### 2. Kiểm tra container đang chạy
```bash
docker ps
```

### 3. Test kết nối database
```bash
# Kiểm tra MySQL container
docker exec uniclub-mysql mysqladmin ping -h localhost -u root -phuytran123

# Kết nối vào MySQL
docker exec -it uniclub-mysql mysql -u root -phuytran123
```

### 4. Chạy Spring Boot application
```bash
cd uniclub-be
mvn spring-boot:run
```

### 5. Truy cập phpMyAdmin (tùy chọn)
- URL: http://localhost:8081
- Username: `root`
- Password: `huytran123`

## Cấu hình trong application.yml
File `application.yml` đã được cấu hình sẵn:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3307/uniclub
    username: root
    password: huytran123
    driver-class-name: com.mysql.cj.jdbc.Driver
```

## Troubleshooting

### Nếu gặp lỗi kết nối:
1. Kiểm tra container có đang chạy không:
   ```bash
   docker ps
   ```

2. Kiểm tra logs:
   ```bash
   docker-compose logs mysql
   ```

3. Restart container:
   ```bash
   docker-compose restart mysql
   ```

### Nếu muốn xóa hoàn toàn:
```bash
# Dừng và xóa containers
docker-compose down

# Xóa containers và volumes (mất hết data)
docker-compose down -v
```

## Các lệnh hữu ích

### Quản lý container:
```bash
# Khởi động
docker-compose up -d

# Dừng
docker-compose down

# Xem logs
docker-compose logs mysql

# Restart
docker-compose restart mysql
```

### Backup database:
```bash
docker exec uniclub-mysql mysqldump -u root -phuytran123 uniclub > backup.sql
```

### Restore database:
```bash
docker exec -i uniclub-mysql mysql -u root -phuytran123 uniclub < backup.sql
```
