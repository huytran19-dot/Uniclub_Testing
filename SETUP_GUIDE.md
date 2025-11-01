# UNICLUB SETUP GUIDE

## 🚀 Quick Start

### 1. Database Setup
```bash
# Start MySQL and phpMyAdmin containers
start-docker.bat
```

### 2. Backend Setup
```bash
# Start Spring Boot application
cd uniclub-be
mvn spring-boot:run
```

### 3. Frontend Setup
```bash
# Start React development server
cd uniclub-fe/admin
pnpm dev
```

## 📋 Prerequisites

- Docker Desktop
- Java 17+
- Node.js 18+
- Maven 3.6+

## 🗄️ Database Information

- **Host:** localhost
- **Port:** 3307
- **Database:** uniclub
- **Username:** root
- **Password:** huytran123
- **phpMyAdmin:** http://localhost:8081

## 👥 Default Users

| Email | Password | Role |
|-------|----------|------|
| admin@uniclub.com | huytran123 | SysAdmin |
| buyer@uniclub.com | huytran123 | Buyer |

## 📊 Sample Data

The database includes:
- **4 users** with different roles
- **6 brands** (Nike, Adidas, Uniqlo, Zara, H&M, Bernini)
- **6 categories** (Áo thun, Quần jean, Áo sơ mi, Váy, Áo khoác, Áo Polo)
- **11 colors** (Đỏ, Xanh dương, Xanh lá, Vàng, Đen, Trắng, Xám, Tím, etc.)
- **6 sizes** (XS, S, M, L, XL, XXL)
- **3 suppliers** (Nhà cung cấp A, B, C)
- **3 products** with **9 variants**

## 🔧 Troubleshooting

### Database Connection Issues
```bash
# Check if containers are running
docker ps

# Restart containers
docker-compose down
docker-compose up -d

# Check MySQL logs
docker logs uniclub-mysql
```

### Backend Issues
```bash
# Clean and rebuild
cd uniclub-be
mvn clean install
mvn spring-boot:run
```

### Frontend Issues
```bash
# Clear cache and reinstall
cd uniclub-fe/admin
rm -rf node_modules
pnpm install
pnpm dev
```

## 📁 Project Structure

```
uniclub/
├── uniclub-be/          # Spring Boot Backend
├── uniclub-fe/         # React Frontend
├── mysql-init/         # Database initialization
├── docker-compose.yml  # Docker services
├── start-docker.bat    # Database setup script
└── SETUP_GUIDE.md      # This file
```

## 🌐 Access URLs

- **Frontend:** http://localhost:3000
- **Backend API:** http://localhost:8080
- **phpMyAdmin:** http://localhost:8081

## 🔐 Security Notes

- Default passwords are for development only
- Change passwords in production
- Configure proper CORS settings
- Use environment variables for sensitive data

## 📝 Development Notes

- Database is automatically initialized with sample data
- All tables are created with proper relationships
- Sample data includes realistic Vietnamese content
- Images are stored on Cloudinary (configure your own credentials)
