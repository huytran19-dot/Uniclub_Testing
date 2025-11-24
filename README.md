# 🛍️ Uniclub E-Commerce System

A full-stack e-commerce platform built with Spring Boot and React, featuring admin management dashboard and customer shopping website.

## 🚀 Quick Setup

```bash
# 1. Clone repository
git clone https://github.com/huytran19-dot/Uniclub_Testing.git
cd Uniclub_Testing

# 2. Start database
start-docker.bat

# 3. Set environment variable (IMPORTANT!)
set SENDGRID_API_KEY=your-sendgrid-api-key

# 4. Start backend
cd uniclub-be
mvn spring-boot:run

# 5. Start frontend (admin)
cd uniclub-fe/admin
pnpm install && pnpm dev

# 6. Start frontend (web)
cd uniclub-fe/web
pnpm install && pnpm dev
```

## 📖 Full Documentation

See **[SETUP_GUIDE.md](SETUP_GUIDE.md)** for detailed setup instructions, troubleshooting, and configuration.

## ✨ Features

- 🔐 User Authentication & Authorization (JWT + Email OTP)
- 🛒 Shopping Cart & Checkout
- 💳 Payment Integration (COD & VNPay)
- 📦 Order Management & Tracking
- 👔 Product Management with Variants
- 📊 Admin Dashboard
- 📧 Email Notifications (SendGrid)
- 🚚 Shipping Fee Calculation

## 🌐 Access URLs

- **Customer Website:** http://localhost:5174
- **Admin Dashboard:** http://localhost:5173
- **Backend API:** http://localhost:8080
- **phpMyAdmin:** http://localhost:8081

## 🔑 Default Login

```
Admin: admin@uniclub.com / huytran123
Buyer: buyer@uniclub.com / huytran123
```

## 🛠️ Tech Stack

**Backend:**
- Spring Boot 3.x
- Spring Security + JWT
- MySQL 8.0
- SendGrid API
- VNPay Payment Gateway

**Frontend:**
- React 19.2.0
- React Router
- Tailwind CSS
- Lucide Icons

## 📝 Important Notes

⚠️ **Before running the project:**
1. Install prerequisites: Docker, Java 17+, Node.js 18+, Maven, pnpm
2. Get SendGrid API key from https://sendgrid.com/
3. Set environment variable: `SENDGRID_API_KEY`
4. See [SETUP_GUIDE.md](SETUP_GUIDE.md) for details

## 📦 Project Structure

```
Uniclub_Testing/
├── uniclub-be/           # Spring Boot Backend
├── uniclub-fe/
│   ├── admin/           # Admin Dashboard
│   └── web/             # Customer Website
├── mysql-init/          # Database Schema
├── docker-compose.yml   # MySQL + phpMyAdmin
└── SETUP_GUIDE.md       # Detailed Setup Guide
```

## 🤝 Contributing

1. Fork the repository
2. Create feature branch: `git checkout -b feature/new-feature`
3. Commit changes: `git commit -m 'Add new feature'`
4. Push to branch: `git push origin feature/new-feature`
5. Submit pull request

## 📄 License

This project is for educational purposes.

---

For detailed setup instructions, see **[SETUP_GUIDE.md](SETUP_GUIDE.md)**
