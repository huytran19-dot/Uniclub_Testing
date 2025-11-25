# UNICLUB SETUP GUIDE

## � Clone Project

```bash
git clone https://github.com/huytran19-dot/Uniclub_Testing.git
cd Uniclub_Testing
```

## 📋 Prerequisites

Trước khi bắt đầu, hãy cài đặt các công cụ sau:

### 1. Docker Desktop
- Download: https://www.docker.com/products/docker-desktop
- Cài đặt và khởi động Docker Desktop

### 2. Java 17+
- Download: https://adoptium.net/
- Verify: `java -version`

### 3. Maven 3.6+
- Download: https://maven.apache.org/download.cgi
- Hoặc dùng Maven wrapper trong project (mvnw)
- Verify: `mvn -version`

### 4. Node.js 18+
- Download: https://nodejs.org/
- Verify: `node -version` và `npm -version`

### 5. pnpm (Package Manager cho Frontend)
```bash
# Cài đặt pnpm globally
npm install -g pnpm

# Verify installation
pnpm -version
```

**Lưu ý:** Batch scripts (`.bat` files) sẽ tự động kiểm tra và báo lỗi nếu thiếu các công cụ trên.

## �🚀 Quick Start

### 1. Database Setup
```bash
# Start MySQL and phpMyAdmin containers
start-docker.bat

# Wait for MySQL to initialize (about 30 seconds)
# Database will be auto-created with sample data
```

### 2. Backend Environment Variables Setup

Create environment variables or use default values in `application.yml`:

```bash
# Option 1: Set environment variables (Recommended)
set SENDGRID_API_KEY=your-sendgrid-api-key-here
set DB_URL=jdbc:mysql://localhost:3307/uniclub
set DB_USERNAME=root
set DB_PASSWORD=REPLACE_ME_DB_PASSWORD

# Option 2: Create application-local.yml (not tracked by git)
# Copy application.yml and rename to application-local.yml
# Replace placeholder values with your actual credentials
```

**Important:** Get your SendGrid API key from https://sendgrid.com/

### 3. Backend Setup
```bash
# Navigate to backend folder
cd uniclub-be

# Build and run Spring Boot application
mvn clean install
mvn spring-boot:run

# Or use the batch script
start-backend.bat
```

Backend will run on: http://localhost:8080

### 4. Frontend Setup (Admin Dashboard)
```bash
# Navigate to admin frontend
cd uniclub-fe/admin

# Install dependencies
pnpm install

# Start development server
pnpm dev

# Or use the batch script from root folder
start-frontend-admin.bat
```

Admin Dashboard: http://localhost:5173

### 5. Frontend Setup (Customer Website)
```bash
# Navigate to web frontend
cd uniclub-fe/web

# Install dependencies
pnpm install

# Start development server
pnpm dev

# Or use the batch script from root folder
start-frontend-web.bat
```

Customer Website: http://localhost:5174

## 🗄️ Database Information

- **Host:** localhost
- **Port:** 3307
- **Database:** uniclub
- **Username:** root
-- **Password:** REPLACE_ME_DB_PASSWORD
- **phpMyAdmin:** http://localhost:8081

## � API Keys & Secrets

### SendGrid (Email Service)
1. Sign up at https://sendgrid.com/
2. Create an API key from Settings > API Keys
3. Set environment variable: `set SENDGRID_API_KEY=your-key-here`
4. Configure sender email in `application.yml` (must be verified in SendGrid)

### VNPay (Payment Gateway - Sandbox)
Already configured in `VNPayConfig.java`:
-- **TMN Code:** REPLACE_ME_VNPAY_TMN
- **Hash Secret:** (configured in code)
- **Payment URL:** https://sandbox.vnpayment.vn/paymentv2/vpcpay.html
- **Return URL:** http://localhost:5174/payment/vnpay-return

For production, register at: https://vnpay.vn/

## �👥 Default Users

| Email | Password | Role | Purpose |
|-------|----------|------|---------|
| admin@uniclub.com | REPLACE_ME_ADMIN_PASSWORD | SysAdmin | Admin Dashboard |
| buyer@uniclub.com | REPLACE_ME_USER_PASSWORD | Buyer | Customer Website |

**Note:** Email verification is required. Check your SendGrid for OTP codes during registration.

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

# Check if port 8080 is already in use
netstat -ano | findstr :8080
```

### Frontend Issues
```bash
# Clear cache and reinstall
cd uniclub-fe/admin
rm -rf node_modules
pnpm install
pnpm dev

# Or for web
cd uniclub-fe/web
rm -rf node_modules
pnpm install
pnpm dev
```

### Environment Variable Issues
```bash
# Windows CMD
set SENDGRID_API_KEY=your-key
echo %SENDGRID_API_KEY%

# Windows PowerShell
$env:SENDGRID_API_KEY="your-key"
echo $env:SENDGRID_API_KEY
```

### Common Errors

**1. "Failed to connect to database"**
- Ensure Docker containers are running
- Check port 3307 is not used by another service
- Verify database credentials in `application.yml`

**2. "SendGrid authentication failed"**
- Check if `SENDGRID_API_KEY` environment variable is set
- Verify API key is active in SendGrid dashboard
- Ensure sender email is verified in SendGrid

**3. "Port already in use"**
- Backend (8080): Another Java app might be running
- Admin Frontend (5173): Stop other Vite dev servers
- Web Frontend (5174): Stop other Vite dev servers
- MySQL (3307): Change port in docker-compose.yml

**4. "Cannot find module" errors**
- Run `pnpm install` in the respective frontend folder
- Clear node_modules and reinstall

## 📁 Project Structure

```
Uniclub_Testing/
├── uniclub-be/                    # Spring Boot Backend
│   ├── src/main/java/com/uniclub/
│   │   ├── config/               # VNPay, Security, CORS
│   │   ├── controller/           # REST API
│   │   ├── dto/                  # Request/Response DTOs
│   │   ├── entity/               # JPA Entities
│   │   ├── repository/           # Data Access
│   │   ├── service/              # Business Logic
│   │   └── util/                 # Utilities
│   ├── src/main/resources/
│   │   └── application.yml       # Configuration
│   └── pom.xml
├── uniclub-fe/
│   ├── admin/                    # Admin Dashboard
│   │   ├── src/pages/           # Products, Orders, Users
│   │   └── package.json
│   └── web/                      # Customer Website
│       ├── src/pages/           # Shop, Cart, Checkout
│       └── package.json
├── mysql-init/
│   └── init-database.sql         # Schema + Sample Data
├── start-*.bat                   # Quick start scripts
└── SETUP_GUIDE.md

## 🌐 Access URLs

- **Admin Dashboard:** http://localhost:5173
- **Customer Website:** http://localhost:5174
- **Backend API:** http://localhost:8080
- **phpMyAdmin:** http://localhost:8081
- **VNPay Sandbox:** https://sandbox.vnpayment.vn

- **VNPay Sandbox:** https://sandbox.vnpayment.vn

## 🔐 Security Notes

### For Development
- Default passwords are for development only
- SendGrid API key should be set via environment variable
- VNPay is using sandbox credentials

### For Production
- **Change all default passwords**
- **Revoke and create new API keys**
- **Register for VNPay production account**
- **Configure proper CORS settings**
- **Use HTTPS for all endpoints**
- **Store secrets in environment variables or secret management service**
- **Enable database backup**
- **Review and update JWT secret key**

### What NOT to commit to Git
- `application-local.yml` (if you create one)
- `.env` files
- Any file containing real API keys or passwords
- Database credentials for production

## 📝 Development Notes

- Database is automatically initialized with sample data on first run
- All tables have proper foreign key relationships
- Sample data includes realistic Vietnamese content
- User passwords are hashed with BCrypt
- JWT tokens expire after 24 hours
- OTP codes expire after 5 minutes
- Orders can be cancelled if status is PENDING or CONFIRMED
- Free shipping for orders over 499,000 VND

## 🧪 Testing Accounts

Use these accounts for testing different features:

**Admin Account:**
```
Email: admin@uniclub.com
Password: REPLACE_ME_DB_PASSWORD
Role: SysAdmin
Access: Admin Dashboard + Full CRUD
```

**Buyer Account:**
```
Email: buyer@uniclub.com
Password: REPLACE_ME_DB_PASSWORD
Role: Buyer
Access: Customer Website + Shopping Features
```

## 🛒 Test Shopping Flow

1. Login as buyer@uniclub.com
2. Browse products at http://localhost:5174
3. Add items to cart
4. Proceed to checkout
5. Choose payment method (COD or VNPay)
6. For VNPay testing, use sandbox test cards from VNPay documentation

## 📞 Support

For issues or questions:
- Check Troubleshooting section above
- Review error logs in terminal/console
- Check backend logs: `mvn spring-boot:run` output
- Check Docker logs: `docker logs uniclub-mysql`

---

**Last Updated:** November 2025
