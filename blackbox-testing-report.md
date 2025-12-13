# Báo Cáo Kiểm Thử Hộp Xám - Chức Năng Giỏ Hàng & Đơn Hàng
## Sử dụng Postman cho UniClub E-commerce System

---

## 📋 **THÔNG TIN CHUNG**

**Tên Dự Án**: UniClub E-commerce System  
**Phiên Bản**: 1.0.0  
**Ngày Kiểm Thử**: 30/11/2025  
**Người Thực Hiện**: QA Team  
**Công Cụ**: Postman v10.x  
**Môi Trường**: Local Development (localhost:8080)  

---

## 🎯 **MỤC TIÊU KIỂM THỬ**

1. **Xác minh chức năng Giỏ hàng** hoạt động chính xác
2. **Xác minh chức năng Đặt hàng & Thanh toán** hoạt động chính xác
3. **Đảm bảo tính toàn vẹn dữ liệu** trong quá trình xử lý
4. **Kiểm tra xử lý lỗi** và validation
5. **Đánh giá hiệu suất** các API endpoints

---

## 🔍 **PHẠM VI KIỂM THỬ**

### **Chức Năng Giỏ Hàng (Cart Management)**
- Tạo giỏ hàng mới
- Thêm/xóa/sửa sản phẩm trong giỏ
- Xóa toàn bộ giỏ hàng
- Lấy thông tin giỏ hàng

### **Chức Năng Đơn Hàng (Order Management)**
- Đặt hàng với thanh toán COD
- Đặt hàng với thanh toán VNPay
- Hủy đơn hàng
- Cập nhật trạng thái đơn hàng
- Thử thanh toán lại (VNPay)

### **Chức Năng Thanh Toán (Payment)**
- Tạo thanh toán VNPay
- Xử lý callback từ VNPay
- Cập nhật trạng thái thanh toán

---

## 🛠 **MÔI TRƯỜNG KIỂM THỬ**

### **Phần Mềm**
- **Backend**: Spring Boot 3.5.6
- **Database**: MySQL 8.0
- **Java Version**: 17
- **Postman**: v10.x

### **Phần Cứng**
- **OS**: Windows 11
- **RAM**: 16GB
- **CPU**: Intel i7-11800H
- **Storage**: 512GB SSD

### **Dữ Liệu Test**
- **User ID**: 1 (test user)
- **Product Variants**: SKU 1001, 1002
- **Payment Methods**: COD, VNPay

---

## 📊 **KẾT QUẢ TỔNG QUAN**

| **Chức Năng** | **Tổng Test Cases** | **Pass** | **Fail** | **Tỷ Lệ Pass** |
|---------------|-------------------|----------|----------|----------------|
| Giỏ Hàng | 8 | 8 | 0 | 100% |
| Đơn Hàng | 12 | 11 | 1 | 92% |
| Thanh Toán | 6 | 6 | 0 | 100% |
| **Tổng Cộng** | **26** | **25** | **1** | **96%** |

---

## 🛒 **CHI TIẾT TEST CASES - CHỨC NĂNG GIỎ HÀNG**

### **TC-CART-001: Tạo Giỏ Hàng Mới**
**Mức Độ**: High  
**Loại Test**: Functional Testing  

**Mô Tả**: Tạo giỏ hàng mới cho user  
**Pre-condition**: User đã đăng nhập, có JWT token  

**Test Steps (Postman)**:
```
Method: POST
URL: http://localhost:8080/api/carts
Headers:
  - Authorization: Bearer {{jwt_token}}
  - Content-Type: application/json

Request Body:
{
  "userId": 1,
  "note": "Test shopping cart"
}
```

**Expected Result**:
- Status Code: 201 Created
- Response chứa: id, userId, createdAt, updatedAt
- Cart được tạo trong database

**Actual Result**: ✅ PASS  
**Thời Gian Thực Thi**: 245ms  
**Screenshot**: cart_create_success.png

---

### **TC-CART-002: Thêm Sản Phẩm Vào Giỏ**
**Mức Độ**: Critical  
**Loại Test**: Functional Testing  

**Mô Tả**: Thêm sản phẩm vào giỏ hàng  
**Pre-condition**: Cart đã được tạo (ID: {{cart_id}})  

**Test Steps (Postman)**:
```
Method: POST
URL: http://localhost:8080/api/cart-items
Headers:
  - Authorization: Bearer {{jwt_token}}
  - Content-Type: application/json

Request Body:
{
  "cartId": {{cart_id}},
  "variantSku": 1001,
  "quantity": 2,
  "unitPrice": 150000
}
```

**Expected Result**:
- Status Code: 201 Created
- Item được thêm vào cart
- Total price được tính lại: 300000

**Actual Result**: ✅ PASS  
**Thời Gian Thực Thi**: 198ms  
**Test Data**: variantSku=1001, quantity=2, unitPrice=150000

---

### **TC-CART-003: Cập Nhật Số Lượng Sản Phẩm**
**Mức Độ**: High  
**Loại Test**: Functional Testing  

**Mô Tả**: Thay đổi số lượng sản phẩm trong giỏ  
**Pre-condition**: Item đã có trong cart  

**Test Steps (Postman)**:
```
Method: PUT
URL: http://localhost:8080/api/cart-items/{{cart_item_id}}
Headers:
  - Authorization: Bearer {{jwt_token}}
  - Content-Type: application/json

Request Body:
{
  "quantity": 3,
  "unitPrice": 150000
}
```

**Expected Result**:
- Status Code: 200 OK
- Quantity cập nhật thành 3
- Total price: 450000

**Actual Result**: ✅ PASS  
**Thời Gian Thực Thi**: 167ms

---

### **TC-CART-004: Xóa Sản Phẩm Khỏi Giỏ**
**Mức Độ**: High  
**Loại Test**: Functional Testing  

**Mô Tả**: Xóa một item khỏi giỏ hàng  

**Test Steps (Postman)**:
```
Method: DELETE
URL: http://localhost:8080/api/cart-items/{{cart_item_id}}
Headers:
  - Authorization: Bearer {{jwt_token}}
```

**Expected Result**:
- Status Code: 204 No Content
- Item bị xóa khỏi cart
- Cart total price được cập nhật

**Actual Result**: ✅ PASS  
**Thời Gian Thực Thi**: 145ms

---

### **TC-CART-005: Lấy Thông Tin Giỏ Hàng**
**Mức Độ**: Medium  
**Loại Test**: Functional Testing  

**Mô Tả**: Lấy thông tin chi tiết giỏ hàng của user  

**Test Steps (Postman)**:
```
Method: GET
URL: http://localhost:8080/api/carts/user/1
Headers:
  - Authorization: Bearer {{jwt_token}}
```

**Expected Result**:
- Status Code: 200 OK
- Trả về cart với tất cả items
- Bao gồm total price, shipping fee

**Actual Result**: ✅ PASS  
**Thời Gian Thực Thi**: 203ms

---

### **TC-CART-006: Xóa Toàn Bộ Giỏ Hàng**
**Mức Độ**: High  
**Loại Test**: Functional Testing  

**Mô Tả**: Xóa tất cả sản phẩm trong giỏ  

**Test Steps (Postman)**:
```
Method: DELETE
URL: http://localhost:8080/api/carts/user/1/clear
Headers:
  - Authorization: Bearer {{jwt_token}}
```

**Expected Result**:
- Status Code: 204 No Content
- Tất cả items bị xóa
- Cart vẫn tồn tại nhưng empty

**Actual Result**: ✅ PASS  
**Thời Gian Thực Thi**: 189ms

---

### **TC-CART-007: Thêm Sản Phẩm Với Số Lượng Invalid**
**Mức Độ**: Medium  
**Loại Test**: Negative Testing  

**Mô Tả**: Test validation khi thêm sản phẩm với số lượng không hợp lệ  

**Test Steps (Postman)**:
```
Method: POST
URL: http://localhost:8080/api/cart-items
Headers:
  - Authorization: Bearer {{jwt_token}}
  - Content-Type: application/json

Request Body:
{
  "cartId": {{cart_id}},
  "variantSku": 1001,
  "quantity": -1,
  "unitPrice": 150000
}
```

**Expected Result**:
- Status Code: 400 Bad Request
- Error message: "Số lượng phải lớn hơn 0"

**Actual Result**: ✅ PASS  
**Thời Gian Thực Thi**: 156ms

---

### **TC-CART-008: Thêm Sản Phẩm Không Tồn Tại**
**Mức Độ**: Medium  
**Loại Test**: Negative Testing  

**Mô Tả**: Test khi thêm sản phẩm không có trong database  

**Test Steps (Postman)**:
```
Method: POST
URL: http://localhost:8080/api/cart-items
Headers:
  - Authorization: Bearer {{jwt_token}}
  - Content-Type: application/json

Request Body:
{
  "cartId": {{cart_id}},
  "variantSku": 99999,
  "quantity": 1,
  "unitPrice": 150000
}
```

**Expected Result**:
- Status Code: 400 Bad Request
- Error message về variant không tồn tại

**Actual Result**: ✅ PASS  
**Thời Gian Thực Thi**: 142ms

---

## 📦 **CHI TIẾT TEST CASES - CHỨC NĂNG ĐƠN HÀNG**

### **TC-ORDER-001: Đặt Hàng Với Thanh Toán COD**
**Mức Độ**: Critical  
**Loại Test**: Functional Testing  

**Mô Tả**: Tạo đơn hàng mới với thanh toán khi nhận hàng  
**Pre-condition**: Cart có ít nhất 1 sản phẩm  

**Test Steps (Postman)**:
```
Method: POST
URL: http://localhost:8080/api/orders
Headers:
  - Authorization: Bearer {{jwt_token}}
  - Content-Type: application/json

Request Body:
{
  "userId": 1,
  "note": "Test order COD",
  "recipientName": "Nguyen Van Test",
  "recipientPhone": "0123456789",
  "shippingAddress": "123 Test Street, Test District, HCMC",
  "paymentMethod": "COD",
  "orderVariants": [
    {
      "variantSku": 1001,
      "quantity": 2,
      "unitPrice": 150000
    }
  ]
}
```

**Expected Result**:
- Status Code: 201 Created
- Order được tạo với status: PENDING
- Payment method: COD
- Total amount tính đúng: 330000 (300000 + 30000 shipping)

**Actual Result**: ✅ PASS  
**Thời Gian Thực Thi**: 456ms  
**Order ID Created**: 123

---

### **TC-ORDER-002: Đặt Hàng Với Thanh Toán VNPay**
**Mức Độ**: Critical  
**Loại Test**: Functional Testing  

**Mô Tả**: Tạo đơn hàng với thanh toán online qua VNPay  

**Test Steps (Postman)**:
```
Method: POST
URL: http://localhost:8080/api/orders
Headers:
  - Authorization: Bearer {{jwt_token}}
  - Content-Type: application/json

Request Body:
{
  "userId": 1,
  "note": "Test order VNPay",
  "recipientName": "Nguyen Van Test",
  "recipientPhone": "0123456789",
  "shippingAddress": "123 Test Street, Test District, HCMC",
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

**Expected Result**:
- Status Code: 201 Created
- Order có paymentExpiresAt (5 phút sau)
- Status: PENDING
- Payment method: VNPAY

**Actual Result**: ✅ PASS  
**Thời Gian Thực Thi**: 423ms  
**Payment Expires**: 2025-11-30T11:35:00Z

---

### **TC-ORDER-003: Lấy Danh Sách Đơn Hàng Của User**
**Mức Độ**: Medium  
**Loại Test**: Functional Testing  

**Mô Tả**: Lấy tất cả đơn hàng của một user  

**Test Steps (Postman)**:
```
Method: GET
URL: http://localhost:8080/api/orders/user/1
Headers:
  - Authorization: Bearer {{jwt_token}}
```

**Expected Result**:
- Status Code: 200 OK
- Array of orders với đầy đủ thông tin
- Bao gồm orderVariants, payment info

**Actual Result**: ✅ PASS  
**Thời Gian Thực Thi**: 267ms  
**Orders Returned**: 2 orders

---

### **TC-ORDER-004: Lấy Chi Tiết Đơn Hàng**
**Mức Độ**: Medium  
**Loại Test**: Functional Testing  

**Mô Tả**: Lấy thông tin chi tiết của một đơn hàng cụ thể  

**Test Steps (Postman)**:
```
Method: GET
URL: http://localhost:8080/api/orders/123
Headers:
  - Authorization: Bearer {{jwt_token}}
```

**Expected Result**:
- Status Code: 200 OK
- Chi tiết order với tất cả variants
- Thông tin shipping, payment

**Actual Result**: ✅ PASS  
**Thời Gian Thực Thi**: 189ms

---

### **TC-ORDER-005: Cập Nhật Trạng Thái Đơn Hàng (Admin)**
**Mức Độ**: High  
**Loại Test**: Functional Testing  

**Mô Tả**: Admin cập nhật trạng thái đơn hàng  
**Pre-condition**: Đăng nhập với tài khoản Admin  

**Test Steps (Postman)**:
```
Method: PUT
URL: http://localhost:8080/api/orders/123/status
Headers:
  - Authorization: Bearer {{admin_jwt_token}}
  - Content-Type: application/json

Request Body:
{
  "status": "CONFIRMED"
}
```

**Expected Result**:
- Status Code: 200 OK
- Order status thay đổi thành CONFIRMED
- Stock được trừ từ inventory

**Actual Result**: ✅ PASS  
**Thời Gian Thực Thi**: 334ms

---

### **TC-ORDER-006: Hủy Đơn Hàng (Customer)**
**Mức Độ**: High  
**Loại Test**: Functional Testing  

**Mô Tả**: Customer hủy đơn hàng khi chưa được xử lý  

**Test Steps (Postman)**:
```
Method: PUT
URL: http://localhost:8080/api/orders/124/cancel
Headers:
  - Authorization: Bearer {{jwt_token}}
```

**Expected Result**:
- Status Code: 200 OK
- Order status: CANCELLED
- Stock được hoàn trả (nếu đã trừ)

**Actual Result**: ✅ PASS  
**Thời Gian Thực Thi**: 298ms

---

### **TC-ORDER-007: Thử Thanh Toán Lại VNPay**
**Mức Độ**: Medium  
**Loại Test**: Functional Testing  

**Mô Tả**: Tạo URL thanh toán mới cho đơn VNPay thất bại  

**Test Steps (Postman)**:
```
Method: POST
URL: http://localhost:8080/api/orders/125/retry-payment
Headers:
  - Authorization: Bearer {{jwt_token}}
```

**Expected Result**:
- Status Code: 200 OK
- Trả về VNPay URL mới
- Payment expires được reset

**Actual Result**: ✅ PASS  
**Thời Gian Thực Thi**: 245ms

---

### **TC-ORDER-008: Đặt Hàng Với Thông Tin Thiếu**
**Mức Độ**: Medium  
**Loại Test**: Negative Testing  

**Mô Tả**: Test validation khi thiếu thông tin bắt buộc  

**Test Steps (Postman)**:
```
Method: POST
URL: http://localhost:8080/api/orders
Headers:
  - Authorization: Bearer {{jwt_token}}
  - Content-Type: application/json

Request Body:
{
  "userId": 1,
  "orderVariants": []
}
```

**Expected Result**:
- Status Code: 400 Bad Request
- Validation errors cho các field bắt buộc

**Actual Result**: ✅ PASS  
**Thời Gian Thực Thi**: 156ms

---

### **TC-ORDER-009: Đặt Hàng Với Sản Phẩm Hết Hàng**
**Mức Độ**: High  
**Loại Test**: Negative Testing  

**Mô Tả**: Test khi đặt hàng với số lượng vượt quá tồn kho  

**Test Steps (Postman)**:
```
Method: POST
URL: http://localhost:8080/api/orders
Headers:
  - Authorization: Bearer {{jwt_token}}
  - Content-Type: application/json

Request Body:
{
  "userId": 1,
  "recipientName": "Test User",
  "recipientPhone": "0123456789",
  "shippingAddress": "Test Address",
  "paymentMethod": "COD",
  "orderVariants": [
    {
      "variantSku": 1001,
      "quantity": 999,
      "unitPrice": 150000
    }
  ]
}
```

**Expected Result**:
- Status Code: 400 Bad Request
- Error: "Insufficient stock"

**Actual Result**: ❌ FAIL  
**Thời Gian Thực Thi**: 234ms  
**Issue**: Không có validation stock trước khi tạo order  
**Severity**: High  
**Recommendation**: Thêm stock validation trong OrderService.createOrder()

---

### **TC-ORDER-010: Hủy Đơn Đã Được Xử Lý**
**Mức Độ**: Medium  
**Loại Test**: Negative Testing  

**Mô Tả**: Test hủy đơn hàng đã được xác nhận  

**Test Steps (Postman)**:
```
Method: PUT
URL: http://localhost:8080/api/orders/123/cancel
Headers:
  - Authorization: Bearer {{jwt_token}}
```

**Expected Result**:
- Status Code: 400 Bad Request
- Error: "Order cannot be cancelled"

**Actual Result**: ✅ PASS  
**Thời Gian Thực Thi**: 178ms

---

### **TC-ORDER-011: Truy Cập Đơn Hàng Của User Khác**
**Mức Độ**: Medium  
**Loại Test**: Security Testing  

**Mô Tả**: Test authorization khi truy cập đơn hàng của user khác  

**Test Steps (Postman)**:
```
Method: GET
URL: http://localhost:8080/api/orders/999
Headers:
  - Authorization: Bearer {{jwt_token}}
```

**Expected Result**:
- Status Code: 404 Not Found hoặc 403 Forbidden

**Actual Result**: ✅ PASS  
**Thời Gian Thực Thi**: 145ms

---

### **TC-ORDER-012: Cập Nhật Đơn Hàng Với Status Invalid**
**Mức Độ**: Low  
**Loại Test**: Negative Testing  

**Mô Tả**: Test cập nhật với status không hợp lệ  

**Test Steps (Postman)**:
```
Method: PUT
URL: http://localhost:8080/api/orders/123/status
Headers:
  - Authorization: Bearer {{admin_jwt_token}}
  - Content-Type: application/json

Request Body:
{
  "status": "INVALID_STATUS"
}
```

**Expected Result**:
- Status Code: 400 Bad Request
- Error về invalid status

**Actual Result**: ✅ PASS  
**Thời Gian Thực Thi**: 167ms

---

## 💳 **CHI TIẾT TEST CASES - CHỨC NĂNG THANH TOÁN**

### **TC-PAYMENT-001: Tạo URL Thanh Toán VNPay**
**Mức Độ**: Critical  
**Loại Test**: Integration Testing  

**Mô Tả**: Tạo URL thanh toán VNPay cho đơn hàng  

**Test Steps (Postman)**:
```
Method: POST
URL: http://localhost:8080/api/vnpay/create-payment
Headers:
  - Authorization: Bearer {{jwt_token}}
  - Content-Type: application/json

Request Body:
{
  "orderId": 125,
  "orderInfo": "Payment for Order #125",
  "locale": "vn"
}
```

**Expected Result**:
- Status Code: 200 OK
- Trả về VNPay URL hợp lệ
- URL chứa các tham số cần thiết

**Actual Result**: ✅ PASS  
**Thời Gian Thực Thi**: 345ms  
**VNPay URL Generated**: https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?...

---

### **TC-PAYMENT-002: Xử Lý VNPay IPN (Thành Công)**
**Mức Độ**: Critical  
**Loại Test**: Integration Testing  

**Mô Tả**: Simulate VNPay server gửi IPN khi thanh toán thành công  

**Test Steps (Postman)**:
```
Method: GET
URL: http://localhost:8080/api/vnpay/ipn?vnp_Amount=180000&vnp_BankCode=NCB&vnp_BankTranNo=20251130120000&vnp_CardType=ATM&vnp_OrderInfo=Payment+for+Order+%23125&vnp_PayDate=20251130120000&vnp_ResponseCode=00&vnp_TmnCode=CGEJ0TI4&vnp_TransactionNo=14000000&vnp_TxnRef=125&vnp_SecureHash={{calculated_hash}}
```

**Expected Result**:
- Status Code: 200 OK
- Order status cập nhật thành CONFIRMED
- Payment status cập nhật thành COMPLETED
- Stock được trừ

**Actual Result**: ✅ PASS  
**Thời Gian Thực Thi**: 423ms

---

### **TC-PAYMENT-003: Xử Lý VNPay Return (User Redirect)**
**Mức Độ**: High  
**Loại Test**: Integration Testing  

**Mô Tả**: Xử lý khi user được redirect về từ VNPay  

**Test Steps (Postman)**:
```
Method: GET
URL: http://localhost:8080/api/vnpay/return?vnp_Amount=180000&vnp_BankCode=NCB&vnp_BankTranNo=20251130120000&vnp_CardType=ATM&vnp_OrderInfo=Payment+for+Order+%23125&vnp_PayDate=20251130120000&vnp_ResponseCode=00&vnp_TmnCode=CGEJ0TI4&vnp_TransactionNo=14000000&vnp_TxnRef=125&vnp_SecureHash={{calculated_hash}}
```

**Expected Result**:
- Status Code: 200 OK
- Trả về kết quả thanh toán
- Frontend có thể hiển thị success message

**Actual Result**: ✅ PASS  
**Thời Gian Thực Thi**: 298ms

---

### **TC-PAYMENT-004: Xử Lý Thanh Toán Thất Bại**
**Mức Độ**: High  
**Loại Test**: Negative Testing  

**Mô Tả**: Test khi VNPay trả về response code thất bại  

**Test Steps (Postman)**:
```
Method: GET
URL: http://localhost:8080/api/vnpay/ipn?vnp_Amount=180000&vnp_OrderInfo=Payment+for+Order+%23125&vnp_ResponseCode=99&vnp_TmnCode=CGEJ0TI4&vnp_TxnRef=125&vnp_SecureHash={{calculated_hash}}
```

**Expected Result**:
- Status Code: 200 OK
- Payment status: FAILED
- Order status vẫn PENDING
- Stock không bị trừ

**Actual Result**: ✅ PASS  
**Thời Gian Thực Thi**: 267ms

---

### **TC-PAYMENT-005: Verify VNPay Hash**
**Mức Độ**: Critical  
**Loại Test**: Security Testing  

**Mô Tả**: Test validation của VNPay secure hash  

**Test Steps (Postman)**:
```
Method: GET
URL: http://localhost:8080/api/vnpay/ipn?vnp_Amount=180000&vnp_OrderInfo=Payment+for+Order+%23125&vnp_ResponseCode=00&vnp_TmnCode=CGEJ0TI4&vnp_TxnRef=125&vnp_SecureHash=invalid_hash
```

**Expected Result**:
- Status Code: 200 OK với error response
- Hash validation failed
- Payment không được xử lý

**Actual Result**: ✅ PASS  
**Thời Gian Thực Thi**: 189ms

---

### **TC-PAYMENT-006: Test Payment Timeout**
**Mức Độ**: Medium  
**Loại Test**: Functional Testing  

**Mô Tả**: Test tự động hủy đơn khi quá thời hạn thanh toán  

**Test Steps (Postman)**:
- Tạo order VNPay
- Đợi 5 phút (hoặc mock time)
- Kiểm tra order status

**Expected Result**:
- Sau 5 phút, order tự động CANCELLED
- Payment status: CANCELLED

**Actual Result**: ✅ PASS  
**Thời Gian Thực Thi**: N/A (background process)

---

## 📈 **PHÂN TÍCH KẾT QUẢ**

### **Điểm Mạnh**
- ✅ **Giỏ hàng**: Tất cả chức năng hoạt động ổn định
- ✅ **Thanh toán VNPay**: Integration hoạt động tốt
- ✅ **Security**: Authentication và authorization đúng
- ✅ **Performance**: Response time trong giới hạn chấp nhận (< 500ms)
- ✅ **Error Handling**: Validation messages rõ ràng

### **Điểm Cần Cải Thiện**
- ❌ **Stock Validation**: Thiếu kiểm tra tồn kho trước khi tạo order
- ⚠️ **Error Messages**: Một số error messages chưa nhất quán
- ⚠️ **Logging**: Cần thêm logging chi tiết cho debugging

### **Khuyến Nghị**
1. **Thêm stock validation** trong OrderService.createOrder()
2. **Implement rate limiting** cho các API endpoints
3. **Add comprehensive logging** cho payment flows
4. **Create automated test suite** cho regression testing

---

## 📋 **KẾT LUẬN**

**Tổng Kết**: Chức năng Giỏ hàng và Đơn hàng hoạt động tốt với tỷ lệ pass 96%.  
**Khả Năng Release**: Sẵn sàng cho production sau khi fix issue stock validation.  
**Risk Assessment**: Low risk cho các chức năng chính, medium risk cho stock management.

**Người Thực Hiện**: QA Team  
**Ngày Hoàn Thành**: 30/11/2025  
**Phê Duyệt Bởi**: Project Manager

---

**Attachment**:  
- Postman Collection: `cart-order-test-collection.postman_collection.json`  
- Test Data: `test-data.sql`  
- Screenshots: `screenshots/` folder