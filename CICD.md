# CICD & Deployment Notes

This file documents the CI and CD workflows added to the repository and explains how to configure, run and customize them locally and on GitHub.

**Files added**
- `./.github/workflows/ci.yml` — CI: build & test backend (Maven) and build frontend (pnpm). Runs on push/PR to `CICD` and `main`.
- `./.github/workflows/cd.yml` — CD: build Docker images and push to GitHub Container Registry (GHCR) on push to `main`.

**Quick summary**
- CI job `backend` uses Java 21 and runs Maven from `uniclub-be`.
- CI job `frontend` uses Node 18 and `pnpm@9.0.0` to build the frontend.
- CD job builds backend and frontend Docker images and pushes to `ghcr.io/${{ github.repository_owner }}` with tags `latest` and the commit SHA.

Required repository configuration
- GHCR: `GITHUB_TOKEN` is provided automatically by GitHub Actions and the `cd.yml` workflow sets `permissions.packages: write`, so pushing to GHCR works without extra secrets for the same account that owns the repo.
- Docker Hub (optional): if you prefer Docker Hub, create repository-level secrets `DOCKERHUB_USERNAME` and `DOCKERHUB_TOKEN` and update `cd.yml` to use Docker Hub login and tags.

How to trigger workflows
- CI: push to or open PR against `CICD` or `main`.
- CD: push to `main` (will build and push images).

Local verification commands (PowerShell)

- Start only MySQL (recommended for development):
```powershell
cd 'C:\New folder\Uniclub_Testing'
docker-compose -f 'C:\New folder\Uniclub_Testing\docker-compose.yml' up -d mysql
docker-compose -f 'C:\New folder\Uniclub_Testing\docker-compose.yml' ps
```

- Run backend tests locally (unit tests use in-memory H2):
```powershell
cd 'C:\New folder\Uniclub_Testing\uniclub-be'
.\mvnw -DskipTests=false test
```

- Build backend (package):
```powershell
.\mvnw -B -DskipTests=true package
```

- Build frontend (pnpm):
```powershell
cd 'C:\New folder\Uniclub_Testing\uniclub-fe'
corepack enable
corepack prepare pnpm@9.0.0 --activate
pnpm install --frozen-lockfile
pnpm build
```

- Run the full stack (recommended):
```powershell
cd 'C:\New folder\Uniclub_Testing'
docker-compose up --build -d
```

Notes about tests and CI
- The `UniclubApplicationTests` test is configured to use an in-memory H2 database so the Spring context starts on CI without requiring a MySQL service. Integration tests that require a real MySQL should either be:
  - run in a separate GitHub Actions job that starts a MySQL service, or
  - use Testcontainers for ephemeral DB instances during tests.

Customizing CD to push to Docker Hub
1. Add repo secrets: `DOCKERHUB_USERNAME`, `DOCKERHUB_TOKEN`.
2. Replace the `docker/login-action` block in `cd.yml` to use `registry: docker.io` and `username/password` from secrets.
3. Update tags to `yourdockerhubuser/uniclub-backend:latest` and similar for frontend.

Troubleshooting tips
- If CI fails with `MissingProjectException`, ensure `ci.yml` is configured to run `mvnw` with `working-directory: ./uniclub-be` (this repository already uses that).
- If tests fail with DB connection issues, confirm unit tests use H2 (see `UniclubApplicationTests`) or add a CI MySQL service for integration tests.
- For Docker Compose issues, run with `-f` and the full path when working directory contains spaces.

If you'd like, I can:
- add a `CICD` or `ci/` section in `README.md` and link to this file, or
- update CI to run a MySQL service for integration tests, or
- switch CD to Docker Hub and show the patch for `cd.yml`.

---
Last updated: 2025-11-25
