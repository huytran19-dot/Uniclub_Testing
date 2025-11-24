# Stage 1: Build Admin
FROM node:20-alpine AS build-admin

WORKDIR /app

# Copy admin source
COPY admin/ ./

# Install dependencies and build
RUN npm install && npm run build

# Stage 2: Build Web
FROM node:20-alpine AS build-web

WORKDIR /app

# Copy web source
COPY web/ ./

# Install dependencies and build
RUN npm install && npm run build

# Stage 3: Runtime with Nginx
FROM nginx:alpine

# Remove default nginx config
RUN rm -rf /usr/share/nginx/html/*

# Copy built files
COPY --from=build-admin /app/dist /usr/share/nginx/html/admin
COPY --from=build-web /app/dist /usr/share/nginx/html/web

# Copy nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose ports
EXPOSE 80

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost/ || exit 1

# Start nginx
CMD ["nginx", "-g", "daemon off;"]
