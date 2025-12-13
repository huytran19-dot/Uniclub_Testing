# UniClub E-commerce API Testing Guide
## Cart & Order Management - Postman Test Cases

### 📋 **Test Environment Setup**
- **Base URL**: `http://localhost:8080`
- **Authentication**: JWT Bearer Token
- **Content-Type**: `application/json`
- **Test User**: Regular user with cart/order permissions
- **Test Admin**: Admin user with full permissions

---

## 🔐 **Authentication Setup**

### **Pre-request Script** (Add to all requests)
```javascript
// Set JWT token in environment variable
pm.environment.set("jwt_token", "your-jwt-token-here");

// Add Authorization header
pm.request.headers.add({
    key: 'Authorization',
    value: 'Bearer ' + pm.environment.get("jwt_token")
});
```

### **Test Data Setup**
```javascript
// Common test data
pm.environment.set("test_user_id", "1");
pm.environment.set("test_cart_id", "1");
pm.environment.set("test_order_id", "123");
pm.environment.set("test_variant_sku", "1001");
pm.environment.set("test_payment_id", "456");
```

---

## 🛒 **CART MANAGEMENT TEST CASES**

### **1. Create Cart**
```
Method: POST
URL: {{base_url}}/api/carts
Headers:
  - Authorization: Bearer {{jwt_token}}
  - Content-Type: application/json

Body (raw JSON):
{
  "userId": {{test_user_id}},
  "note": "Test shopping cart"
}
```

**Expected Response:**
- Status: 201 Created
- Body contains cart ID and creation timestamp

**Test Assertions:**
```javascript
pm.test("Cart created successfully", function () {
    pm.response.to.have.status(201);
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('id');
    pm.expect(jsonData.userId).to.eql(parseInt(pm.environment.get("test_user_id")));
    pm.environment.set("created_cart_id", jsonData.id);
});
```

### **2. Get User's Cart**
```
Method: GET
URL: {{base_url}}/api/carts/user/{{test_user_id}}
Headers:
  - Authorization: Bearer {{jwt_token}}
```

**Expected Response:**
- Status: 200 OK
- Returns cart details with items

### **3. Add Item to Cart**
```
Method: POST
URL: {{base_url}}/api/cart-items
Headers:
  - Authorization: Bearer {{jwt_token}}
  - Content-Type: application/json

Body (raw JSON):
{
  "cartId": {{created_cart_id}},
  "variantSku": {{test_variant_sku}},
  "quantity": 2,
  "unitPrice": 150000
}
```

**Expected Response:**
- Status: 201 Created
- Item added to cart successfully

### **4. Update Cart Item Quantity**
```
Method: PUT
URL: {{base_url}}/api/cart-items/{{cart_item_id}}
Headers:
  - Authorization: Bearer {{jwt_token}}
  - Content-Type: application/json

Body (raw JSON):
{
  "quantity": 3,
  "unitPrice": 150000
}
```

**Expected Response:**
- Status: 200 OK
- Quantity updated to 3

### **5. Get Cart Items**
```
Method: GET
URL: {{base_url}}/api/cart-items/cart/{{created_cart_id}}
Headers:
  - Authorization: Bearer {{jwt_token}}
```

**Expected Response:**
- Status: 200 OK
- Returns array of cart items

### **6. Remove Item from Cart**
```
Method: DELETE
URL: {{base_url}}/api/cart-items/{{cart_item_id}}
Headers:
  - Authorization: Bearer {{jwt_token}}
```

**Expected Response:**
- Status: 204 No Content
- Item removed from cart

### **7. Clear Cart**
```
Method: DELETE
URL: {{base_url}}/api/carts/user/{{test_user_id}}/clear
Headers:
  - Authorization: Bearer {{jwt_token}}
```

**Expected Response:**
- Status: 204 No Content
- All items removed from cart

---

## 📦 **ORDER MANAGEMENT TEST CASES**

### **8. Create Order (COD)**
```
Method: POST
URL: {{base_url}}/api/orders
Headers:
  - Authorization: Bearer {{jwt_token}}
  - Content-Type: application/json

Body (raw JSON):
{
  "userId": {{test_user_id}},
  "note": "Test order with COD",
  "recipientName": "Nguyen Van Test",
  "recipientPhone": "0123456789",
  "shippingAddress": "123 Test Street, Test District, Ho Chi Minh City",
  "paymentMethod": "COD",
  "orderVariants": [
    {
      "variantSku": 1001,
      "quantity": 2,
      "unitPrice": 150000
    },
    {
      "variantSku": 1002,
      "quantity": 1,
      "unitPrice": 200000
    }
  ]
}
```

**Expected Response:**
- Status: 201 Created
- Order created with COD payment method

**Test Assertions:**
```javascript
pm.test("Order created successfully", function () {
    pm.response.to.have.status(201);
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('id');
    pm.expect(jsonData.paymentMethod).to.eql('COD');
    pm.expect(jsonData.status).to.eql('PENDING');
    pm.environment.set("created_order_id", jsonData.id);
});
```

### **9. Create Order (VNPay)**
```
Method: POST
URL: {{base_url}}/api/orders
Headers:
  - Authorization: Bearer {{jwt_token}}
  - Content-Type: application/json

Body (raw JSON):
{
  "userId": {{test_user_id}},
  "note": "Test order with VNPay",
  "recipientName": "Nguyen Van Test",
  "recipientPhone": "0123456789",
  "shippingAddress": "123 Test Street, Test District, Ho Chi Minh City",
  "paymentMethod": "VNPAY",
  "orderVariants": [
    {
      "variantSku": 1001,
      "quantity": 1,
      "unitPrice": 150000
    }
  ]
}
```

**Expected Response:**
- Status: 201 Created
- Order created with VNPay payment method
- Includes payment expiration time

### **10. Get User's Orders**
```
Method: GET
URL: {{base_url}}/api/orders/user/{{test_user_id}}
Headers:
  - Authorization: Bearer {{jwt_token}}
```

**Expected Response:**
- Status: 200 OK
- Returns array of user's orders

### **11. Get Order Details**
```
Method: GET
URL: {{base_url}}/api/orders/{{created_order_id}}
Headers:
  - Authorization: Bearer {{jwt_token}}
```

**Expected Response:**
- Status: 200 OK
- Returns complete order details with variants

### **12. Update Order Status (Admin)**
```
Method: PUT
URL: {{base_url}}/api/orders/{{created_order_id}}/status
Headers:
  - Authorization: Bearer {{admin_jwt_token}}
  - Content-Type: application/json

Body (raw JSON):
{
  "status": "CONFIRMED"
}
```

**Expected Response:**
- Status: 200 OK
- Order status changed to CONFIRMED

### **13. Cancel Order (Customer)**
```
Method: PUT
URL: {{base_url}}/api/orders/{{created_order_id}}/cancel
Headers:
  - Authorization: Bearer {{jwt_token}}
```

**Expected Response:**
- Status: 200 OK (if order can be cancelled)
- Status: 400 Bad Request (if order cannot be cancelled)

### **14. Retry VNPay Payment**
```
Method: POST
URL: {{base_url}}/api/orders/{{created_order_id}}/retry-payment
Headers:
  - Authorization: Bearer {{jwt_token}}
```

**Expected Response:**
- Status: 200 OK
- Returns new VNPay payment URL

---

## 💳 **PAYMENT MANAGEMENT TEST CASES**

### **15. Create Payment Record**
```
Method: POST
URL: {{base_url}}/api/payments
Headers:
  - Authorization: Bearer {{jwt_token}}
  - Content-Type: application/json

Body (raw JSON):
{
  "orderId": {{created_order_id}},
  "amount": 330000,
  "paymentMethod": "VNPAY",
  "status": "PENDING"
}
```

**Expected Response:**
- Status: 201 Created
- Payment record created

### **16. Get Payments by Order**
```
Method: GET
URL: {{base_url}}/api/payments/order/{{created_order_id}}
Headers:
  - Authorization: Bearer {{jwt_token}}
```

**Expected Response:**
- Status: 200 OK
- Returns payments for the order

### **17. Process Payment**
```
Method: POST
URL: {{base_url}}/api/payments/{{payment_id}}/process
Headers:
  - Authorization: Bearer {{jwt_token}}
```

**Expected Response:**
- Status: 200 OK
- Payment processed successfully

---

## 💰 **VNPAY INTEGRATION TEST CASES**

### **18. Create VNPay Payment URL**
```
Method: POST
URL: {{base_url}}/api/vnpay/create-payment
Headers:
  - Authorization: Bearer {{jwt_token}}
  - Content-Type: application/json

Body (raw JSON):
{
  "orderId": {{created_order_id}},
  "orderInfo": "Payment for Order #{{created_order_id}}",
  "locale": "vn"
}
```

**Expected Response:**
- Status: 200 OK
- Returns VNPay payment URL

**Test Assertions:**
```javascript
pm.test("VNPay URL created successfully", function () {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('paymentUrl');
    pm.expect(jsonData.paymentUrl).to.include('vnpayment.vn');
    pm.environment.set("vnpay_url", jsonData.paymentUrl);
});
```

### **19. Simulate VNPay IPN (Payment Success)**
```
Method: GET
URL: {{base_url}}/api/vnpay/ipn?vnp_Amount=330000&vnp_BankCode=NCB&vnp_BankTranNo=20241125120000&vnp_CardType=ATM&vnp_OrderInfo=Payment+for+Order+%23{{created_order_id}}&vnp_PayDate=20241125120000&vnp_ResponseCode=00&vnp_TmnCode=CGEJ0TI4&vnp_TransactionNo=14000000&vnp_TxnRef={{created_order_id}}&vnp_SecureHash={{calculated_hash}}
```

**Expected Response:**
- Status: 200 OK
- IPN processed successfully

### **20. Simulate VNPay Return (User Redirect)**
```
Method: GET
URL: {{base_url}}/api/vnpay/return?vnp_Amount=330000&vnp_BankCode=NCB&vnp_BankTranNo=20241125120000&vnp_CardType=ATM&vnp_OrderInfo=Payment+for+Order+%23{{created_order_id}}&vnp_PayDate=20241125120000&vnp_ResponseCode=00&vnp_TmnCode=CGEJ0TI4&vnp_TransactionNo=14000000&vnp_TxnRef={{created_order_id}}&vnp_SecureHash={{calculated_hash}}
```

**Expected Response:**
- Status: 200 OK
- Payment result processed

---

## 🧪 **NEGATIVE TEST CASES**

### **21. Create Cart - Invalid User ID**
```
Method: POST
URL: {{base_url}}/api/carts
Headers:
  - Authorization: Bearer {{jwt_token}}
  - Content-Type: application/json

Body (raw JSON):
{
  "userId": 99999,
  "note": "Invalid user test"
}
```

**Expected Response:**
- Status: 400 Bad Request

### **22. Add Item - Insufficient Stock**
```
Method: POST
URL: {{base_url}}/api/cart-items
Headers:
  - Authorization: Bearer {{jwt_token}}
  - Content-Type: application/json

Body (raw JSON):
{
  "cartId": {{created_cart_id}},
  "variantSku": {{test_variant_sku}},
  "quantity": 999,
  "unitPrice": 150000
}
```

**Expected Response:**
- Status: 400 Bad Request
- Error message about insufficient stock

### **23. Create Order - Invalid Payment Method**
```
Method: POST
URL: {{base_url}}/api/orders
Headers:
  - Authorization: Bearer {{jwt_token}}
  - Content-Type: application/json

Body (raw JSON):
{
  "userId": {{test_user_id}},
  "recipientName": "Test User",
  "recipientPhone": "0123456789",
  "shippingAddress": "Test Address",
  "paymentMethod": "INVALID_METHOD",
  "orderVariants": []
}
```

**Expected Response:**
- Status: 400 Bad Request

### **24. Cancel Order - Already Shipped**
```
Method: PUT
URL: {{base_url}}/api/orders/{{shipped_order_id}}/cancel
Headers:
  - Authorization: Bearer {{jwt_token}}
```

**Expected Response:**
- Status: 400 Bad Request
- Error message: Order cannot be cancelled

### **25. Access Admin Endpoint - Regular User**
```
Method: PUT
URL: {{base_url}}/api/orders/{{order_id}}/status
Headers:
  - Authorization: Bearer {{jwt_token}}
  - Content-Type: application/json

Body (raw JSON):
{
  "status": "CONFIRMED"
}
```

**Expected Response:**
- Status: 403 Forbidden

---

## 📊 **PERFORMANCE TEST CASES**

### **26. Load Test - Multiple Cart Operations**
- Create 100 carts simultaneously
- Add items to carts concurrently
- Measure response times

### **27. Stress Test - Order Creation Peak**
- Create 50 orders per minute
- Monitor system performance
- Check for race conditions

### **28. Payment Timeout Test**
- Create VNPay order
- Wait for payment expiration (5 minutes)
- Verify automatic order cancellation

---

## 🔄 **INTEGRATION TEST SCENARIOS**

### **Complete E-commerce Flow Test**

1. **User Registration/Login**
2. **Browse Products** (external API call)
3. **Add to Cart** → **Update Quantity** → **Remove Item**
4. **Create Order** (COD or VNPay)
5. **Payment Processing** (VNPay flow)
6. **Order Status Updates**
7. **Email Notifications** (SendGrid integration)

### **Database Transaction Tests**

1. **Stock Deduction Test**
   - Verify inventory decreases when order is confirmed
   - Test rollback on order cancellation

2. **Payment Status Sync**
   - Ensure payment status matches order status
   - Test VNPay IPN updates both payment and order tables

---

## 📋 **TEST EXECUTION CHECKLIST**

### **Pre-test Setup:**
- [ ] Start MySQL database
- [ ] Start Spring Boot application
- [ ] Import test data (users, products, variants)
- [ ] Configure VNPay sandbox credentials
- [ ] Set up SendGrid email service

### **Test Execution:**
- [ ] Authentication tests
- [ ] Cart management tests
- [ ] Order creation tests
- [ ] Payment processing tests
- [ ] VNPay integration tests
- [ ] Negative test cases
- [ ] Performance tests

### **Post-test Cleanup:**
- [ ] Clear test data
- [ ] Reset database state
- [ ] Archive test results
- [ ] Generate test report

---

## 📈 **TEST METRICS TO TRACK**

- **Response Time**: < 500ms for cart operations, < 2s for order creation
- **Success Rate**: > 99% for happy path scenarios
- **Error Rate**: < 1% for valid requests
- **Throughput**: Support 100 concurrent users
- **Payment Success Rate**: > 95% for VNPay transactions

---

## 🐛 **COMMON ISSUES & TROUBLESHOOTING**

### **Authentication Issues:**
- Check JWT token expiration
- Verify user roles and permissions
- Ensure correct Authorization header format

### **Database Issues:**
- Check MySQL connection
- Verify foreign key constraints
- Monitor transaction rollbacks

### **Payment Issues:**
- Verify VNPay sandbox credentials
- Check payment timeout logic
- Monitor IPN callback handling

### **Performance Issues:**
- Check database query optimization
- Monitor memory usage
- Verify caching mechanisms