# UniClub Cart & Order API Documentation

## 📋 Overview

This documentation provides comprehensive API specifications and testing guidelines for the Cart and Order management functionality in the UniClub e-commerce system.

## 📁 Files Included

### 1. `cart-order-swagger.yml`
OpenAPI 3.0.3 specification file containing:
- Complete API endpoints for Cart, Order, Payment, and VNPay integration
- Request/Response schemas with examples
- Authentication requirements
- Error handling specifications

### 2. `cart-order-api-tests.md`
Detailed Postman testing guide including:
- 28 comprehensive test cases
- Authentication setup
- Test data preparation
- Negative test scenarios
- Performance testing guidelines
- Integration test scenarios

## 🚀 Quick Start

### Prerequisites
- Java 17+ and Spring Boot 3.5.6
- MySQL 8.0 database
- Postman or similar API testing tool
- VNPay sandbox account (for payment testing)

### Setup Steps

1. **Start the Application**
   ```bash
   cd uniclub-be
   ./mvnw spring-boot:run
   ```

2. **Database Setup**
   - Ensure MySQL is running on port 3307
   - Database: `uniclub`
   - Import test data if available

3. **Environment Variables**
   ```bash
   export DB_URL=jdbc:mysql://localhost:3307/uniclub
   export DB_USERNAME=root
   export DB_PASSWORD=huytran123
   export JWT_SECRET=uniclub-secret-key-for-jwt-token-generation-2024
   export VNPAY_TMN_CODE=CGEJ0TI4
   export VNPAY_HASH_SECRET=GNUPBN07YMSNSDZPJ1ZLBIKXVNDSOTUI
   ```

## 📖 API Documentation

### View Swagger UI
1. Start the application
2. Open browser: `http://localhost:8080/swagger-ui.html`
3. Navigate to Cart & Order APIs section

### Import OpenAPI Spec
1. Open Swagger Editor: https://editor.swagger.io/
2. Import `cart-order-swagger.yml`
3. View interactive API documentation

## 🧪 Testing with Postman

### Import Test Collection
1. Open Postman
2. Import `cart-order-api-tests.md` as raw text
3. Set up environment variables:
   ```
   base_url = http://localhost:8080
   jwt_token = your-jwt-token-here
   admin_jwt_token = your-admin-jwt-token-here
   test_user_id = 1
   ```

### Test Execution Order
1. **Authentication Setup** - Get JWT tokens
2. **Cart Management** - Create cart, add/remove items
3. **Order Creation** - Place orders with COD/VNPay
4. **Payment Processing** - Handle VNPay payments
5. **Order Management** - Update status, cancel orders
6. **Negative Tests** - Error scenarios
7. **Performance Tests** - Load testing

## 🔑 Key API Endpoints

### Cart Management
- `POST /api/carts` - Create cart
- `GET /api/carts/user/{userId}` - Get user's cart
- `POST /api/cart-items` - Add item to cart
- `DELETE /api/carts/user/{userId}/clear` - Clear cart

### Order Management
- `POST /api/orders` - Create order
- `GET /api/orders/user/{userId}` - Get user's orders
- `PUT /api/orders/{id}/status` - Update order status (Admin)
- `PUT /api/orders/{id}/cancel` - Cancel order (Customer)

### Payment Integration
- `POST /api/vnpay/create-payment` - Generate VNPay URL
- `GET /api/vnpay/ipn` - Handle VNPay IPN callback
- `GET /api/vnpay/return` - Handle VNPay return redirect

## 🔒 Authentication

All APIs require JWT Bearer token authentication:
```
Authorization: Bearer <your-jwt-token>
```

### Getting JWT Token
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

## 💳 VNPay Integration Testing

### Sandbox Configuration
- **TMN Code**: CGEJ0TI4
- **Hash Secret**: GNUPBN07YMSNSDZPJ1ZLBIKXVNDSOTUI
- **Return URL**: http://localhost:5174/payment/vnpay-return

### Test Cards
Use VNPay sandbox test cards for payment testing:
- **Card Number**: 9704198526191432198
- **Card Holder**: NGUYEN VAN A
- **Valid Date**: 03/27
- **CVV**: 123

## 📊 Test Coverage

### Functional Testing
- ✅ Cart CRUD operations
- ✅ Cart item management
- ✅ Order creation (COD & VNPay)
- ✅ Order status management
- ✅ Payment processing
- ✅ VNPay integration

### Integration Testing
- ✅ Database transactions
- ✅ Payment gateway integration
- ✅ Email notifications
- ✅ Stock management

### Negative Testing
- ✅ Invalid input validation
- ✅ Authorization checks
- ✅ Business rule enforcement
- ✅ Error handling

## 🚨 Common Issues

### Authentication Errors
- **401 Unauthorized**: Check JWT token validity
- **403 Forbidden**: Verify user permissions

### Database Errors
- **Connection refused**: Ensure MySQL is running
- **Foreign key violations**: Check data relationships

### Payment Errors
- **VNPay connection failed**: Verify sandbox credentials
- **Payment timeout**: Check 5-minute expiration logic

## 📈 Performance Benchmarks

- **Cart Operations**: < 200ms response time
- **Order Creation**: < 1s response time
- **Payment Processing**: < 2s response time
- **Concurrent Users**: Support 100+ simultaneous users

## 🔄 CI/CD Integration

### Automated Testing
```yaml
# GitHub Actions example
- name: Run API Tests
  run: |
    npm install -g newman
    newman run cart-order-postman-collection.json \
      --environment test-environment.json \
      --reporters cli,json \
      --reporter-json-export results.json
```

### Test Reporting
- Generate HTML reports from Postman collections
- Integrate with test management tools
- Set up automated regression testing

## 📞 Support

For issues or questions:
- Check application logs: `logs/spring.log`
- Verify database state
- Test with Swagger UI first
- Review error responses for details

## 📝 Change Log

### Version 1.0.0
- Initial release
- Complete Cart & Order API documentation
- Comprehensive Postman test collection
- VNPay integration testing guide