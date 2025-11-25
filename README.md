# 🛍️ Uniclub E-Commerce System

A full-stack e-commerce platform built with Spring Boot and React, featuring admin management dashboard and customer shopping website.

## 🚀 Quick Start

### Option 1: Development Mode (với Hot Reload)
Dành cho lúc đang code và cần thay đổi nhanh:

```bash
# Clone repository
git clone https://github.com/huytran19-dot/Uniclub_Testing.git
cd Uniclub_Testing

# Chạy tất cả (MySQL Docker + Backend Dev + Frontend Dev)
start-all.bat
```

**Sau khi chạy:**
- Frontend Web: http://localhost:5173 (Hot reload enabled)
- Frontend Admin: http://localhost:5174 (Hot reload enabled)
- Backend API: http://localhost:8080 (Hot reload enabled)
- phpMyAdmin: http://localhost:8081

### Option 2: Docker Mode (Production-like)
Dành cho testing và demo, giống môi trường production:

```bash
# Cách 1: Dùng script
start-docker-all.bat

# Cách 2: Dùng lệnh trực tiếp
docker-compose up -d
```

**Sau khi chạy:**
- Frontend (Web + Admin): http://localhost
- Admin Panel: http://localhost/admin
- Backend API: http://localhost/api
- phpMyAdmin: http://localhost:8081

### Stop All Services

```bash
# Stop tất cả (Docker + Dev processes)
stop-all.bat

# Hoặc chỉ stop Docker
docker-compose down
```

## 📖 Full Documentation

See **[SETUP_GUIDE.md](SETUP_GUIDE.md)** for detailed setup instructions, troubleshooting, and configuration.
See **[DOCKER_SETUP_GUIDE.md](DOCKER_SETUP_GUIDE.md)** for Docker-specific documentation.

## ✨ Features

- 🔐 User Authentication & Authorization (JWT + Email OTP)
- 🛒 Shopping Cart & Checkout
- 💳 Payment Integration (COD & VNPay)
- 📦 Order Management & Tracking
- 👔 Product Management with Variants
- 📊 Admin Dashboard
- 📧 Email Notifications (SendGrid)
- 🚚 Shipping Fee Calculation

## 🔑 Login / Setup

This repository previously contained example credentials — those were removed for security.
Before running the app locally or in CI please copy `.env.example` -> `.env` and fill in real secure values (or set CI/GitHub secrets). Do NOT commit secrets into the repo.

phpMyAdmin (local): http://localhost:8081

## 🛠️ Tech Stack

**Backend:**
- Spring Boot 3.x
- Spring Security + JWT
- MySQL 8.0
- SendGrid API
- VNPay Payment Gateway

**Frontend:**
- React 19.2.0
## 🧪 Running tests & CI (local + repository)

Quick notes so contributors can run the same checks locally that GitHub Actions runs in CI.

Prerequisites (developer machine):
- Java 21+ (backend build/tests)
- Maven (or use the repository's `./mvnw` wrapper)
- Node.js 18+ and pnpm (frontend)

Install pnpm globally (if not already):
```powershell
npm install -g pnpm
```

Backend (run tests locally):
```powershell
cd uniclub-be
./mvnw -B -DskipTests=false test
# For coverage (same plugin used in CI):
./mvnw -B org.jacoco:jacoco-maven-plugin:0.8.8:prepare-agent test org.jacoco:jacoco-maven-plugin:0.8.8:report
```

Frontend (pnpm workspace + Vitest):
```powershell
cd uniclub-fe
pnpm install
pnpm --filter web run test:unit
pnpm --filter web run test:integration
pnpm --filter web run test:ci -- --coverage  # run all tests + produce lcov
```

CI notes:
- CI uses GitHub Actions workflows in `.github/workflows/`:
	- `ci.yml` — runs backend tests (Java/Maven) and frontend tests (pnpm/Vitest) and uploads coverage to Codecov.
	- `web-tests.yml` — runs frontend unit & integration suites (monorepo/pnpm friendly).
- Add the following repository secrets (GitHub → Settings → Secrets → Actions):
	- `CODECOV_TOKEN` — to upload coverage reports to Codecov
	- Any deployment or registry tokens you need for CD later (GHCR_PAT, RAILWAY_API_KEY, etc.)

If you want to run workflows locally consider using `act` or run the commands above manually.

Security reminder: Do not commit real credentials. Use the `.env.example` as a template and add real values to your CI provider's secret store.

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
