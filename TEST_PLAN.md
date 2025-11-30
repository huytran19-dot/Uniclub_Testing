# TEST PLAN
## UniClub - Website Thương Mại Điện Tử Bán Quần Áo

---

## 3.1 INTRODUCTION

### 3.1.1 Purpose (Mục đích)

Tài liệu Kế hoạch Kiểm thử (Test Plan) cho dự án UniClub – Website bán quần áo trực tuyến được xây dựng với các mục tiêu sau:

- **Xác định các thông tin hiện có của dự án** và các thành phần phần mềm của hệ thống UniClub cần được kiểm thử, bao gồm cả Frontend (ReactJS) và Backend (Spring Boot) tích hợp với cơ sở dữ liệu MySQL.

- **Liệt kê các yêu cầu chức năng** cấp cao cần được kiểm thử nhằm đảm bảo website vận hành đúng như mong đợi trên tất cả các phân hệ nghiệp vụ cốt lõi, bao gồm:
  - Quản lý danh mục sản phẩm
  - Giỏ hàng
  - Quản lý đơn hàng
  - Thanh toán (COD & VNPay)
  - Quản lý tồn kho & biến thể
  - Quản lý nhà cung cấp
  - Quản lý phiếu nhập hàng (GRN)
  - Đánh giá & Bình luận sản phẩm
  - Quản lý người dùng & phân quyền truy cập

- **Đề xuất và mô tả các chiến lược kiểm thử** sẽ được áp dụng, bao gồm:
  - Kiểm thử chức năng (Functional Testing)
  - Kiểm thử giao diện người dùng (UI Testing)
  - Kiểm thử tích hợp (Integration Testing)
  - Kiểm thử đầu-cuối (End-to-End Testing)
  - Kiểm thử khả năng tương thích (Compatibility Testing) trên nhiều trình duyệt và thiết bị

- **Xác định các nguồn lực cần thiết**, bao gồm:
  - Nhân sự (Kỹ sư kiểm thử, Lập trình viên, Quản trị hệ thống)
  - Công cụ hỗ trợ (Postman, Selenium, JMeter, DBeaver, v.v.)
  - Ước tính tổng khối lượng công việc kiểm thử

- **Liệt kê các sản phẩm bàn giao (deliverables)** sẽ được tạo ra sau khi hoàn tất quá trình kiểm thử:
  - Bộ test case
  - Báo cáo thực thi kiểm thử
  - Nhật ký lỗi (defect log)
  - Ma trận bao phủ yêu cầu (traceability matrix)
  - Báo cáo tổng kết kiểm thử cuối cùng

---

### 3.1.2 Definitions, Acronyms, and Abbreviations (Định nghĩa, Từ viết tắt và Ký hiệu)

| **Thuật ngữ** | **Định nghĩa** |
|---------------|----------------|
| **UniClub** | Tên dự án – Website thương mại điện tử bán quần áo và phụ kiện thời trang |
| **SUT** | System Under Test – Hệ thống được kiểm thử |
| **UAT** | User Acceptance Testing – Kiểm thử chấp nhận người dùng |
| **API** | Application Programming Interface – Giao diện lập trình ứng dụng |
| **UI** | User Interface – Giao diện người dùng |
| **Backend** | Phần máy chủ của ứng dụng (Spring Boot) |
| **Frontend** | Phần giao diện người dùng (ReactJS) |
| **DBMS** | Database Management System – Hệ quản trị cơ sở dữ liệu (MySQL) |
| **COD** | Cash On Delivery – Thanh toán khi nhận hàng |
| **VNPay** | Cổng thanh toán trực tuyến của Việt Nam |
| **GRN** | Goods Receipt Note – Phiếu nhập hàng |
| **CRUD** | Create, Read, Update, Delete – Các thao tác cơ bản trên dữ liệu |
| **REST** | Representational State Transfer – Kiến trúc API |
| **DTO** | Data Transfer Object – Đối tượng truyền dữ liệu |
| **JPA** | Java Persistence API – API lưu trữ dữ liệu Java |
| **Variant** | Biến thể sản phẩm (kết hợp size + màu sắc + sản phẩm) |
| **Lazy Load** | Tải dữ liệu theo yêu cầu (không tải trước toàn bộ) |
| **Transaction** | Giao dịch cơ sở dữ liệu đảm bảo tính ACID |
| **E2E** | End-to-End – Kiểm thử đầu-cuối |
| **Regression** | Kiểm thử hồi quy – Kiểm tra lại các chức năng cũ sau khi sửa lỗi |
| **Defect** | Lỗi phần mềm |
| **Test Case** | Trường hợp kiểm thử |
| **Test Suite** | Bộ kiểm thử – Tập hợp các test case |
| **Traceability Matrix** |    |

---

### 3.1.3 References (Tài liệu tham khảo)

| **STT** | **Tên tài liệu** | **Mô tả** | **Nguồn/Đường dẫn** |
|---------|------------------|-----------|---------------------|
| 1 | Software Requirements Specification (SRS) | Đặc tả yêu cầu phần mềm UniClub | [Nội bộ dự án] |
| 2 | System Design Document | Tài liệu thiết kế hệ thống | [Nội bộ dự án] |
| 3 | API Documentation | Tài liệu mô tả các API endpoint (Backend) | `README.md`, Postman Collection |
| 4 | Database Schema | Sơ đồ cơ sở dữ liệu MySQL | `mysql-init/init-database.sql` |
| 5 | Setup Guide | Hướng dẫn cài đặt môi trường phát triển | `SETUP_GUIDE.md`, `DOCKER_SETUP_GUIDE.md` |
| 6 | Authentication Guide | Hướng dẫn xác thực và phân quyền | `uniclub-fe/admin/AUTHENTICATION_GUIDE.md` |
| 7 | VNPay Integration Documentation | Tài liệu tích hợp cổng thanh toán VNPay | VNPay Sandbox Documentation |
| 8 | Spring Boot Documentation | Tài liệu chính thức Spring Boot 3.5.6 | https://spring.io/projects/spring-boot |
| 9 | React Documentation | Tài liệu chính thức React 19.2.0 | https://react.dev/ |
| 10 | MySQL Documentation | Tài liệu MySQL 8.0 | https://dev.mysql.com/doc/ |

---

### 3.1.4 Background Information (Bối cảnh)

**UniClub** là một website chuyên bán quần áo và phụ kiện thời trang trực tuyến, được phát triển với mục tiêu cung cấp trải nghiệm mua sắm tiện lợi, hiện đại và đáng tin cậy cho người dùng.

#### **Công nghệ sử dụng:**
- **Backend**: Java Spring Boot 3.5.6 với JPA/Hibernate
- **Frontend**: React 19.2.0 (2 ứng dụng riêng biệt)
  - User Web: Cổng 5173 (Trang người dùng)
  - Admin Panel: Cổng 5174 (Trang quản trị)
- **Database**: MySQL 8.0 (Cổng 3307)
- **Payment Gateway**: VNPay Sandbox (Merchant: 4EM6TS4E)
- **Additional Services**: 
  - SendGrid (Email)
  - Cloudinary (Quản lý hình ảnh)
  - Vietnam Provinces API (Địa chỉ)

#### **Chức năng chính:**

**Phân hệ người dùng (User Site):**
- Đăng ký, đăng nhập, quản lý hồ sơ cá nhân
- Duyệt sản phẩm theo danh mục, thương hiệu, màu sắc, kích cỡ
- Xem chi tiết sản phẩm, chọn biến thể (size + màu)
- Thêm sản phẩm vào giỏ hàng, cập nhật số lượng
- Đặt hàng với 2 phương thức thanh toán: COD và VNPay
- Theo dõi trạng thái đơn hàng, hủy đơn hàng
- Đánh giá và bình luận sản phẩm

**Phân hệ quản trị (Admin Panel):**
- Quản lý danh mục, thương hiệu, màu sắc, kích cỡ
- Quản lý sản phẩm và biến thể sản phẩm
- Quản lý đơn hàng (xác nhận, giao hàng, hủy)
- Quản lý nhà cung cấp (Supplier)
- Quản lý phiếu nhập hàng (GRN - Goods Receipt Note)
- Quản lý tồn kho tự động
- Quản lý người dùng và phân quyền
- Xem báo cáo, thống kê

#### **Đặc điểm kỹ thuật quan trọng:**
- **Quản lý tồn kho tự động**: 
  - Trừ tồn kho khi đơn hàng được xác nhận (CONFIRMED)
  - Hoàn trả tồn kho khi đơn hàng bị hủy (CANCELLED)
  - Tự động hủy đơn VNPay sau 15 phút nếu chưa thanh toán
- **Lazy Loading**: JPA sử dụng chiến lược tải dữ liệu theo yêu cầu
- **Transaction Management**: Sử dụng @Transactional để đảm bảo tính nhất quán dữ liệu
- **Scheduled Tasks**: PaymentExpirationScheduler chạy mỗi 5 phút để kiểm tra đơn hàng quá hạn

---

### 3.1.5 Scope of Testing (Phạm vi kiểm thử)

Kế hoạch kiểm thử này mô tả toàn bộ hoạt động kiểm thử dành cho hệ thống **UniClub – Website thương mại điện tử bán quần áo**, bao gồm cả **phân hệ người dùng (User Site)** và **phân hệ quản trị (Admin Panel)**.

#### **3.1.5.1 In Scope (Trong phạm vi)**

**Các module/chức năng được kiểm thử:**

1. **Authentication & Authorization (Xác thực & Phân quyền)**
   - Đăng ký, đăng nhập, đăng xuất
   - Phân quyền User/Admin
   - Quản lý session và token

2. **Product Management (Quản lý sản phẩm)**
   - CRUD sản phẩm
   - Quản lý biến thể (Variant: size + màu + sản phẩm)
   - Upload hình ảnh sản phẩm
   - Tìm kiếm và lọc sản phẩm

3. **Category, Brand, Color, Size Management**
   - CRUD danh mục, thương hiệu, màu sắc, kích cỡ

4. **Shopping Cart (Giỏ hàng)**
   - Thêm/xóa/cập nhật sản phẩm trong giỏ hàng
   - Tính toán tổng tiền tự động
   - Đồng bộ giỏ hàng giữa client và server

5. **Order Management (Quản lý đơn hàng)**
   - Tạo đơn hàng (COD & VNPay)
   - Xem danh sách đơn hàng
   - Cập nhật trạng thái đơn hàng
   - Hủy đơn hàng (manual & auto)
   - Retry payment cho đơn VNPay pending

6. **Payment Integration (Tích hợp thanh toán)**
   - Thanh toán COD
   - Thanh toán VNPay (create payment, return URL, IPN)
   - Xử lý timeout thanh toán (15 phút)

7. **Inventory Management (Quản lý tồn kho)**
   - Tự động trừ tồn kho khi đơn hàng CONFIRMED
   - Tự động hoàn trả tồn kho khi đơn hàng CANCELLED
   - Cập nhật tồn kho từ GRN

8. **Supplier Management (Quản lý nhà cung cấp)**
   - CRUD nhà cung cấp

9. **GRN Management (Quản lý phiếu nhập hàng)**
   - Tạo phiếu nhập hàng
   - Tự động cập nhật tồn kho khi GRN được tạo

10. **Review & Rating (Đánh giá & Bình luận)**
    - Thêm/xóa/sửa đánh giá sản phẩm
    - Hiển thị rating trung bình

11. **User Management (Quản lý người dùng)**
    - Quản lý thông tin cá nhân
    - Quản lý địa chỉ giao hàng

**Các loại kiểm thử được thực hiện:**
- ✅ **Functional Testing** (Kiểm thử chức năng)
- ✅ **Integration Testing** (Kiểm thử tích hợp Backend-Frontend-Database)
- ✅ **UI Testing** (Kiểm thử giao diện)
- ✅ **API Testing** (Kiểm thử REST API)
- ✅ **Database Testing** (Kiểm thử cơ sở dữ liệu)
- ✅ **End-to-End Testing** (Kiểm thử đầu-cuối)
- ✅ **Compatibility Testing** (Kiểm thử tương thích trình duyệt)
- ✅ **Regression Testing** (Kiểm thử hồi quy sau khi sửa lỗi)

**Môi trường kiểm thử:**
- Development Environment (Localhost)
- Staging Environment (nếu có)
- Multiple Browsers: Chrome, Firefox, Edge, Safari
- Multiple Devices: Desktop, Tablet, Mobile

#### **3.1.5.2 Out of Scope (Ngoài phạm vi)**

Các hoạt động/module **KHÔNG** nằm trong phạm vi kiểm thử hiện tại:

- ❌ **Performance Testing** (Kiểm thử hiệu năng/tải) - Sẽ thực hiện ở giai đoạn sau
- ❌ **Security Testing** chuyên sâu (Penetration Testing, Vulnerability Scanning)
- ❌ **Load Testing** và **Stress Testing** trên môi trường production
- ❌ **Disaster Recovery Testing** (Khôi phục sau thảm họa)
- ❌ **Third-party Service Testing** (SendGrid, Cloudinary, Vietnam Provinces API) - Giả định các service này hoạt động ổn định
- ❌ **Mobile App Testing** (Chỉ kiểm thử responsive web, không có native app)
- ❌ **Internationalization (i18n) Testing** - Hệ thống chỉ hỗ trợ tiếng Việt
- ❌ **Accessibility Testing (WCAG compliance)** - Không có yêu cầu này trong phiên bản hiện tại

---

### 3.1.6 Constraints (Các ràng buộc)

#### **3.1.6.1 Time Constraints (Ràng buộc về thời gian)**
- Thời gian kiểm thử có giới hạn: **2-3 tuần** (bao gồm cả viết test case và thực thi)
- Deadline phát hành sản phẩm cố định, không thể kéo dài
- Thời gian regression testing sau mỗi lần fix bug: **1-2 ngày**

#### **3.1.6.2 Resource Constraints (Ràng buộc về nguồn lực)**
- **Nhân sự hạn chế**: 
  - 1-2 QA Tester
  - Lập trình viên tham gia part-time hỗ trợ kiểm thử
- **Thiết bị kiểm thử**:
  - Chỉ có laptop/desktop để test
  - Thiếu thiết bị mobile thật (chỉ test bằng DevTools emulator)
- **Công cụ tự động hóa**: Chưa có công cụ tự động hóa test (Selenium, Cypress) - Tất cả test case đều manual

#### **3.1.6.3 Environment Constraints (Ràng buộc về môi trường)**
- Kiểm thử chủ yếu trên **localhost** (Development Environment)
- Chưa có môi trường **Staging** riêng biệt
- VNPay chỉ test trên **Sandbox** (không test với tài khoản thật)
- Không có môi trường **production-like** để kiểm thử

#### **3.1.6.4 Technical Constraints (Ràng buộc kỹ thuật)**
- Hệ thống phụ thuộc vào **third-party services** (SendGrid, Cloudinary, VNPay) - Không kiểm soát được uptime của các service này
- **Database reset** cần thực hiện manual trước mỗi test cycle
- **Docker** cần được cài đặt và chạy ổn định (MySQL container)
- **Port conflicts**: Cần đảm bảo các port 3307, 5173, 5174, 8080 không bị chiếm dụng

#### **3.1.6.5 Data Constraints (Ràng buộc về dữ liệu)**
- Dữ liệu test phải được chuẩn bị thủ công hoặc import từ script SQL
- Không có **test data generator** tự động
- Dữ liệu production không được sử dụng để test (vì lý do bảo mật)

#### **3.1.6.6 Scope Constraints (Ràng buộc về phạm vi)**
- **Không test các trường hợp edge case** quá phức tạp do hạn chế thời gian
- **Không test compatibility** trên tất cả các trình duyệt cũ (chỉ test các phiên bản mới nhất)
- **Không test performance** chi tiết (response time, throughput, concurrent users)

---

### 3.1.7 Risk List (Danh sách rủi ro)

| **ID** | **Rủi ro** | **Mức độ** | **Tác động** | **Biện pháp giảm thiểu** |
|--------|-----------|------------|--------------|--------------------------|
| **R01** | **VNPay Sandbox không ổn định** | High | Không thể test luồng thanh toán VNPay | - Chuẩn bị test data cho cả COD và VNPay<br>- Kiểm tra trạng thái VNPay Sandbox trước khi test<br>- Có plan B: Test với Mock VNPay response |
| **R02** | **Lazy Loading Exception** | Medium | Lỗi khi truy cập quan hệ entity chưa được load | - Sử dụng LEFT JOIN FETCH trong các query<br>- Thêm @Transactional cho các method cần thiết<br>- Kiểm tra kỹ scheduler và background tasks |
| **R03** | **Inventory không được restore khi order bị hủy** | High | Mất dữ liệu tồn kho, ảnh hưởng nghiệp vụ | - Viết test case kiểm tra inventory restore trong cả manual cancel và auto-cancel<br>- Verify log của scheduler<br>- Test kỹ transaction rollback |
| **R04** | **Port conflict** | Low | Không thể start ứng dụng | - Document rõ các port được sử dụng (3307, 5173, 5174, 8080)<br>- Check port availability trước khi start<br>- Sử dụng Docker để isolate services |
| **R05** | **Test data không nhất quán** | Medium | Kết quả test không reliable | - Sử dụng SQL script để reset database trước mỗi test cycle<br>- Document test data requirements<br>- Tạo backup của database state tốt |
| **R06** | **Thiếu nhân sự kiểm thử** | Medium | Không đủ thời gian test hết tất cả use case | - Ưu tiên test các critical flows (checkout, payment, inventory)<br>- Developer hỗ trợ test API<br>- Automation testing ở giai đoạn sau |
| **R07** | **Thay đổi requirements giữa chừng** | High | Test case cần viết lại, mất thời gian | - Freeze requirements trước khi bắt đầu kiểm thử<br>- Change request phải được approve bởi PM<br>- Regression testing cho mọi thay đổi |
| **R08** | **Third-party service downtime** (SendGrid, Cloudinary) | Low | Một số tính năng không test được | - Mock third-party responses khi cần<br>- Test offline khi service down<br>- Document dependencies |
| **R09** | **Browser compatibility issues** | Medium | UI không hiển thị đúng trên một số trình duyệt | - Test trên ít nhất 3 trình duyệt chính (Chrome, Firefox, Edge)<br>- Sử dụng CSS prefix và polyfills<br>- Test responsive design |
| **R10** | **Database connection timeout** | Low | Mất kết nối database trong quá trình test | - Configure connection pool đúng cách<br>- Restart Docker container nếu cần<br>- Monitor MySQL logs |
| **R11** | **Session timeout khi test** | Low | Phải login lại nhiều lần | - Tăng session timeout trong config<br>- Sử dụng tool để tự động refresh token<br>- Prepare test accounts |
| **R12** | **Deployment issues** | Medium | Không thể deploy lên staging/production | - Test deployment process trên local trước<br>- Document deployment steps<br>- Sử dụng Docker để đảm bảo consistency |

**Mức độ rủi ro:**
- **High**: Ảnh hưởng nghiêm trọng đến kế hoạch kiểm thử, cần xử lý ngay
- **Medium**: Ảnh hưởng trung bình, cần theo dõi và có kế hoạch dự phòng
- **Low**: Ảnh hưởng nhỏ, có thể chấp nhận hoặc xử lý sau

---

### 3.1.8 Training Needs (Nhu cầu đào tạo)

Để đảm bảo quá trình kiểm thử diễn ra hiệu quả, các thành viên trong team cần được đào tạo về các lĩnh vực sau:

#### **3.1.8.1 Domain Knowledge (Kiến thức nghiệp vụ)**

| **Chủ đề** | **Đối tượng** | **Nội dung** | **Thời gian** |
|-----------|---------------|--------------|---------------|
| **E-commerce Business Flow** | QA, Dev | Quy trình mua hàng online, quản lý đơn hàng, tồn kho, GRN | 2 giờ |
| **UniClub Functional Requirements** | QA, Dev | Đặc tả chức năng chi tiết của từng module | 3 giờ |
| **User Roles & Permissions** | QA | Phân quyền User/Admin, các hành động được phép | 1 giờ |

#### **3.1.8.2 Technical Skills (Kỹ năng kỹ thuật)**

| **Chủ đề** | **Đối tượng** | **Nội dung** | **Thời gian** |
|-----------|---------------|--------------|---------------|
| **Spring Boot Architecture** | QA, Dev | REST API, JPA/Hibernate, Transaction Management | 4 giờ |
| **React Basics** | QA | Component lifecycle, State management, Routing | 3 giờ |
| **MySQL Fundamentals** | QA | SQL queries, Database schema, Relationships | 2 giờ |
| **VNPay Integration** | QA, Dev | Payment flow, Sandbox testing, Return URL handling | 2 giờ |
| **Docker & Docker Compose** | QA, Dev | Container management, docker-compose.yml | 2 giờ |
| **Postman for API Testing** | QA | Request/Response, Collections, Environment variables | 2 giờ |
| **Git & Version Control** | QA | Branch management, Pull requests, Merge conflicts | 1 giờ |

#### **3.1.8.3 Testing Methodologies (Phương pháp kiểm thử)**

| **Chủ đề** | **Đối tượng** | **Nội dung** | **Thời gian** |
|-----------|---------------|--------------|---------------|
| **Test Case Design Techniques** | QA | Equivalence partitioning, Boundary value analysis, Decision table | 3 giờ |
| **Functional Testing** | QA | Black-box testing, Test scenarios, Expected vs Actual | 2 giờ |
| **Integration Testing** | QA, Dev | API testing, Database testing, End-to-End flows | 3 giờ |
| **Regression Testing** | QA | Impact analysis, Test suite prioritization | 2 giờ |
| **Defect Management** | QA | Bug reporting, Severity/Priority, Bug lifecycle | 2 giờ |
| **Traceability Matrix** | QA | Requirement-to-test mapping | 1 giờ |

#### **3.1.8.4 Tools Training (Đào tạo công cụ)**

| **Công cụ** | **Đối tượng** | **Nội dung** | **Thời gian** |
|-------------|---------------|--------------|---------------|
| **Postman** | QA | API testing, Collections, Automated tests | 3 giờ |
| **DBeaver** | QA | Database connection, Query execution, Data export | 2 giờ |
| **Chrome DevTools** | QA | Network tab, Console, Responsive design testing | 2 giờ |
| **VS Code** | QA | Code navigation, Search, Terminal usage | 1 giờ |
| **Browser DevTools** | QA | Debugging JavaScript, Inspecting elements | 2 giờ |

#### **3.1.8.5 Project-Specific Training (Đào tạo theo dự án)**

| **Chủ đề** | **Đối tượng** | **Nội dung** | **Thời gian** |
|-----------|---------------|--------------|---------------|
| **UniClub System Architecture** | QA, Dev | Frontend-Backend-Database integration, Deployment | 2 giờ |
| **Setup Development Environment** | QA | Follow SETUP_GUIDE.md, DOCKER_SETUP_GUIDE.md | 2 giờ |
| **Authentication Flow** | QA | Login/Register, JWT tokens, Session management | 1 giờ |
| **Payment Flow (COD & VNPay)** | QA | Checkout process, Payment status, Retry payment | 2 giờ |
| **Inventory Management Logic** | QA, Dev | Stock deduction/restoration, GRN impact | 2 giờ |
| **Order Lifecycle** | QA | PENDING → CONFIRMED → SHIPPING → DELIVERED → CANCELLED | 1 giờ |
| **Scheduled Tasks** | QA, Dev | PaymentExpirationScheduler, Auto-cancel orders | 1 giờ |

#### **3.1.8.6 Training Schedule (Lịch trình đào tạo)**

**Week 1: Foundation**
- Ngày 1-2: Domain knowledge + Functional requirements
- Ngày 3-4: Spring Boot + React basics
- Ngày 5: MySQL + Docker

**Week 2: Testing & Tools**
- Ngày 1-2: Testing methodologies + Test case design
- Ngày 3-4: Postman + DBeaver + DevTools
- Ngày 5: Project-specific training

**Week 3: Hands-on Practice**
- Ngày 1-2: Setup environment + Explore codebase
- Ngày 3-4: Write sample test cases
- Ngày 5: Review + Q&A

#### **3.1.8.7 Training Deliverables (Sản phẩm đào tạo)**

- ✅ **Training Materials**: Slides, Documents, Video recordings
- ✅ **Hands-on Labs**: Step-by-step exercises
- ✅ **Reference Guides**: Quick reference cards for tools and APIs
- ✅ **Knowledge Check**: Quiz sau mỗi session
- ✅ **Certification**: Certificate of completion (nếu cần)

#### **3.1.8.8 Continuous Learning (Học tập liên tục)**

- **Weekly Knowledge Sharing**: 30 phút mỗi tuần để chia sẻ kinh nghiệm
- **Bug Review Sessions**: Review các bug đã tìm thấy, học từ mistakes
- **Tool Updates**: Cập nhật khi có công cụ mới hoặc version mới
- **Best Practices**: Chia sẻ best practices trong kiểm thử

---

## 3.2 TEST PLAN DETAILS (Chi tiết kế hoạch kiểm thử)

### 3.2.1 Test Strategy (Chiến lược kiểm thử)

Chiến lược kiểm thử áp dụng phương pháp **Risk-Based Testing**, tập trung vào các chức năng quan trọng nhất đối với nghiệp vụ.

- **Test Levels:** Unit Testing (Dev), Integration Testing (Dev/QA), System Testing (QA), Acceptance Testing (User/PO).
- **Test Types:** Functional, API, Database, UI, Compatibility, Basic Security.
- **Approach:** Agile Testing, thực hiện kiểm thử liên tục trong quá trình phát triển.

### 3.2.2 Test Process (Quy trình kiểm thử)

Quy trình kiểm thử tuân theo các bước sau:

#### **3.2.2.1 Test Items (Các hạng mục kiểm thử)**

| **Category** | **Items** | **Priority** |
|--------------|-----------|--------------|
| **Backend Services** | Authentication, Product, Cart, Order, Payment (VNPay), Inventory, Supplier, GRN, Review | CRITICAL |
| **Frontend** | User Web (Port 5173): Product browsing, Cart, Checkout, Orders<br>Admin Panel (Port 5174): Product/Order/Supplier management | HIGH |
| **Database** | MySQL 8.0: users, products, variants, orders, payments, cart_items, grn | CRITICAL |
| **APIs** | REST endpoints: /api/auth, /api/products, /api/cart, /api/orders, /api/vnpay | CRITICAL |
| **Third-party** | VNPay Sandbox (Payment), Cloudinary (Image storage) | HIGH |

#### **3.2.2.2 Test Design (Thiết kế kiểm thử)**
Sử dụng các kỹ thuật thiết kế test case:
- **Equivalence Partitioning:** Phân vùng tương đương cho input data.
- **Boundary Value Analysis:** Kiểm tra các giá trị biên.
- **Decision Table:** Kiểm tra logic nghiệp vụ phức tạp.
- **State Transition:** Kiểm tra luồng trạng thái đơn hàng (Order Status).

#### **3.2.2.3 Test Cases (Ca kiểm thử)**
Test cases được quản lý trong file Excel/Google Sheets với các trường:
- **ID:** Mã định danh duy nhất.
- **Description:** Mô tả mục tiêu kiểm thử.
- **Pre-conditions:** Điều kiện tiên quyết.
- **Steps:** Các bước thực hiện.
- **Expected Result:** Kết quả mong đợi.
- **Priority:** Mức độ ưu tiên (Critical, High, Medium, Low).

#### **3.2.2.4 Test Execution & Bug Reports (Thực thi và Báo cáo lỗi)**
- **Execution:** Thực hiện test case trên môi trường Staging/Localhost.
- **Bug Reporting:** Khi phát hiện lỗi, log bug với đầy đủ thông tin:
    - **Summary:** Tóm tắt lỗi.
    - **Severity:** Mức độ nghiêm trọng (Critical, Major, Minor).
    - **Steps to Reproduce:** Các bước tái hiện lỗi.
    - **Actual vs Expected Result:** Kết quả thực tế so với mong đợi.
    - **Screenshots/Logs:** Hình ảnh hoặc log đính kèm.

#### **3.2.2.5 Test Summary (Tổng hợp kết quả)**
Sau mỗi đợt kiểm thử, tổng hợp báo cáo bao gồm:
- Số lượng Test Cases Pass/Fail.
- Số lượng Bugs tìm thấy (theo mức độ nghiêm trọng).
- Các vấn đề tồn đọng và rủi ro.

### 3.2.3 Resources (Tài nguyên)

#### **3.2.3.1 Environments (Môi trường)**
- **Hardware:** PC/Laptop (Core i5+, 8GB RAM+).
- **Software:** Windows/MacOS/Linux, Docker, Java JDK 17+, Node.js 18+, MySQL 8.0.
- **Network:** Internet ổn định để truy cập VNPay Sandbox, Cloudinary.

#### **3.2.3.2 Tools (Công cụ)**
- **Test Management:** Excel, Google Sheets.
- **API Testing:** Postman.
- **Database:** DBeaver, MySQL Workbench.
- **Browser:** Chrome DevTools.
- **Communication:** Zalo, Discord, Trello/Jira (nếu có).

#### **3.2.3.3 Human Resources (Nhân sự)**
- **QA Team:** Thực hiện thiết kế test case, execution, log bug.
- **Dev Team:** Fix bug, support môi trường, unit testing.
- **Project Manager/Leader:** Review kế hoạch, giám sát tiến độ.

### 3.2.4 Deliverables (Sản phẩm bàn giao)

Các tài liệu và kết quả cần bàn giao sau giai đoạn kiểm thử:
1.  **Test Plan:** Kế hoạch kiểm thử (tài liệu này).
2.  **Test Cases:** Bộ test case chi tiết (Excel/Sheets).
3.  **Bug Reports:** Danh sách các lỗi đã log và trạng thái.
4.  **Test Summary Report:** Báo cáo tổng kết kết quả kiểm thử.

## 3.3 FEATURES TO BE TESTED (Chi tiết các tính năng được kiểm thử)

Dưới đây là danh sách các tính năng chính cần kiểm thử, được phân loại theo module và mức độ ưu tiên.

### 3.3.1 Core Features (Các tính năng cốt lõi)

| **Module** | **Features** | **Priority** |
|------------|--------------|--------------|
| **Authentication & User Management** | User Registration, Login (User/Admin), Logout, Update Profile, Change Password, Manage Addresses | CRITICAL |
| **Product Catalog** | View Product List, Product Detail, Search, Filter by Brand/Category/Color/Size, CRUD Products (Admin), Manage Variants | HIGH |
| **Shopping Cart** | Add to Cart, View Cart, Update Quantity, Remove Item, Clear Cart, Cart Persistence, Stock Validation | CRITICAL |
| **Order Management** | Create Order (COD/VNPay), View Orders, Order Detail, Cancel Order, Update Status (Admin), Auto-cancel Timeout Orders | CRITICAL |
| **Payment (VNPay)** | Generate Payment URL, Handle Return Callback, VNPay IPN, Retry Payment, Payment Timeout, Transaction Logging | CRITICAL |
| **Inventory Management** | Stock Deduction (Order Confirmed), Stock Restoration (Cancel), Stock Update via GRN, View Stock Levels, Out of Stock Handling | CRITICAL |
| **Supplier & GRN** | CRUD Suppliers, Create GRN, View GRN List/Detail, Auto-update Stock from GRN | HIGH |
| **Master Data** | CRUD Categories, Brands, Colors, Sizes | MEDIUM |
| **Reviews & Ratings** | Add/Edit/Delete Review, View Reviews, Auto-calculate Average Rating | MEDIUM |

**Lưu ý:** Chi tiết Acceptance Criteria cho từng feature sẽ được định nghĩa trong Test Case Document.

## 3.4 FEATURES NOT TO BE TESTED (Các tính năng không được kiểm thử)

Các features sau **KHÔNG** nằm trong phạm vi kiểm thử của đồ án môn học:

- **Performance & Load Testing:** Load testing, stress testing, volume testing với nhiều concurrent users.
- **Advanced Security Testing:** Penetration testing, vulnerability scanning, OWASP comprehensive testing.
- **Mobile Native Applications:** iOS/Android native apps, mobile-specific features.
- **Localization & Internationalization:** Multi-language support, currency conversion, timezone handling.
- **Third-party Service Internals:** VNPay/Cloudinary/SendGrid internal processing logic.
- **Advanced Analytics:** Business intelligence, data mining, predictive analytics.
- **Infrastructure & DevOps:** CI/CD pipelines, Kubernetes, cloud deployment (AWS/Azure/GCP).
- **Accessibility (WCAG):** Screen reader compatibility, keyboard navigation compliance.
- **Legacy Browser Support:** Internet Explorer, old versions of Chrome/Firefox.
- **Disaster Recovery:** Backup/restore procedures, business continuity planning.

**Lý do:** Các tính năng trên nằm ngoài phạm vi đồ án môn học, yêu cầu chuyên môn cao hoặc môi trường production-scale không khả dụng.

---

## 3.5 TEST SCHEDULE (Lịch trình kiểm thử)

Lịch trình kiểm thử dự kiến trong 6 tuần, bao gồm các giai đoạn chính:

| **Phase** | **Activities** | **Duration** | **Deliverables** |
|-----------|----------------|--------------|------------------|
| **Phase 1: Test Planning** | Viết Test Plan, thiết kế Test Cases | Week 1 | Test Plan, Test Case Document |
| **Phase 2: Test Preparation** | Chuẩn bị môi trường, Test Data | Week 1-2 | Test Environment Setup, Test Data |
| **Phase 3: Test Execution** | Thực thi test cases (Functional, API, UI, DB) | Week 2-4 | Test Execution Reports, Bug Reports |
| **Phase 4: Defect Fixing** | Dev fix bugs, QA re-test | Week 4-5 | Updated Bug Reports |
| **Phase 5: Regression Testing** | Re-test sau khi fix bugs | Week 5 | Regression Test Report |
| **Phase 6: UAT & Sign-off** | User Acceptance Testing, Approval | Week 6 | Test Summary Report, Sign-off |

---

## 3.6 RISKS AND MITIGATION (Rủi ro và biện pháp giảm thiểu)

| **Risk** | **Impact** | **Probability** | **Mitigation Plan** |
|----------|-----------|-----------------|---------------------|
| VNPay Sandbox không khả dụng | HIGH | MEDIUM | Sử dụng mock data cho payment testing tạm thời |
| Thiếu test data đầy đủ | MEDIUM | LOW | Chuẩn bị test data scripts để generate nhanh |
| Team member không available | MEDIUM | MEDIUM | Cross-training để backup |
| Phát hiện nhiều Critical bugs | HIGH | MEDIUM | Ưu tiên fix Critical/High bugs trước |
| Môi trường test không ổn định | HIGH | LOW | Setup Docker environment để đảm bảo consistency |

---

## 3.7 APPROVALS (Phê duyệt)

| **Role** | **Name** | **Signature** | **Date** |
|----------|----------|---------------|----------|
| **QA Lead** | _____________ | _____________ | ____/____/____ |
| **Dev Lead** | _____________ | _____________ | ____/____/____ |
| **Project Manager** | _____________ | _____________ | ____/____/____ |

---

**Document Version**: 1.0  
**Last Updated**: November 25, 2025  
**Status**: Approved
