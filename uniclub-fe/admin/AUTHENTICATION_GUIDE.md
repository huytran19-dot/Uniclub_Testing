# Hướng dẫn Authentication cho Admin Panel

## 🚀 Cách chạy và test authentication

### 1. Backend (Spring Boot)

```bash
cd uniclub-be
mvn clean install
mvn spring-boot:run
```

Backend sẽ chạy trên: `http://localhost:8080`

### 2. Frontend Admin

```bash
cd uniclub-fe/admin
npm install
npm run dev
```

Admin panel sẽ chạy trên: `http://localhost:5173`

### 3. Database Setup

Đảm bảo MySQL đang chạy và có database `uniclub`:

```sql
CREATE DATABASE uniclub;
```

### 4. Test Authentication

#### Login Credentials:
- **Admin**: 
  - Email: `admin@uniclub.vn`
  - Password: `admin123`
- **Customer**: 
  - Email: `user@uniclub.vn`
  - Password: `user123`

#### Test Flow:
1. Truy cập `http://localhost:5173`
2. Sẽ được redirect đến `/login`
3. Nhập credentials admin
4. Sau khi login thành công, sẽ redirect đến `/dashboard`
5. Tất cả routes khác đều được protect
6. Click vào user menu (icon người) để logout

## 🔧 API Endpoints

### Authentication
- `POST /api/auth/login` - Login endpoint

### Protected Routes
Tất cả routes khác đều cần authentication token trong header:
```
Authorization: Bearer <jwt_token>
```

## 🛠️ Cấu trúc Authentication

### Backend:
- `JwtConfig.java` - JWT configuration
- `SecurityConfig.java` - Spring Security config
- `AuthController.java` - Login endpoint
- `AuthService.java` - Authentication logic
- `DataInitializer.java` - Tạo dữ liệu mẫu

### Frontend:
- `auth.js` - Auth context và logic
- `Login.jsx` - Login form
- `ProtectedRoute.jsx` - Route protection
- `Topbar.jsx` - Logout functionality

## 🔒 Security Features

1. **JWT Token**: 24h expiration
2. **Password Encryption**: BCrypt
3. **Protected Routes**: Tất cả admin routes cần authentication
4. **Auto Logout**: Khi token expired
5. **Role-based Access**: Admin vs Customer roles

## 🐛 Troubleshooting

### Backend Issues:
- Kiểm tra MySQL connection trong `application.yml`
- Đảm bảo database `uniclub` tồn tại
- Check logs để xem lỗi cụ thể

### Frontend Issues:
- Kiểm tra CORS settings
- Đảm bảo backend đang chạy
- Check browser console cho errors

### Database Issues:
- Đảm bảo MySQL service đang chạy
- Check connection string trong `application.yml`
- Verify database permissions
