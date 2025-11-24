# TEST PLAN
## UniClub - Website Thương Mại Điện Tử Bán Quần Áo

---

## 1. INTRODUCTION

### 1.1 Purpose (Mục đích)

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

### 1.2 Definitions, Acronyms, and Abbreviations (Định nghĩa, Từ viết tắt và Ký hiệu)

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

### 1.3 References (Tài liệu tham khảo)

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

### 1.4 Background Information (Bối cảnh)

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

### 1.5 Scope of Testing (Phạm vi kiểm thử)

Kế hoạch kiểm thử này mô tả toàn bộ hoạt động kiểm thử dành cho hệ thống **UniClub – Website thương mại điện tử bán quần áo**, bao gồm cả **phân hệ người dùng (User Site)** và **phân hệ quản trị (Admin Panel)**.

#### **1.5.1 In Scope (Trong phạm vi)**

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

#### **1.5.2 Out of Scope (Ngoài phạm vi)**

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

### 1.6 Constraints (Các ràng buộc)

#### **1.6.1 Time Constraints (Ràng buộc về thời gian)**
- Thời gian kiểm thử có giới hạn: **2-3 tuần** (bao gồm cả viết test case và thực thi)
- Deadline phát hành sản phẩm cố định, không thể kéo dài
- Thời gian regression testing sau mỗi lần fix bug: **1-2 ngày**

#### **1.6.2 Resource Constraints (Ràng buộc về nguồn lực)**
- **Nhân sự hạn chế**: 
  - 1-2 QA Tester
  - Lập trình viên tham gia part-time hỗ trợ kiểm thử
- **Thiết bị kiểm thử**:
  - Chỉ có laptop/desktop để test
  - Thiếu thiết bị mobile thật (chỉ test bằng DevTools emulator)
- **Công cụ tự động hóa**: Chưa có công cụ tự động hóa test (Selenium, Cypress) - Tất cả test case đều manual

#### **1.6.3 Environment Constraints (Ràng buộc về môi trường)**
- Kiểm thử chủ yếu trên **localhost** (Development Environment)
- Chưa có môi trường **Staging** riêng biệt
- VNPay chỉ test trên **Sandbox** (không test với tài khoản thật)
- Không có môi trường **production-like** để kiểm thử

#### **1.6.4 Technical Constraints (Ràng buộc kỹ thuật)**
- Hệ thống phụ thuộc vào **third-party services** (SendGrid, Cloudinary, VNPay) - Không kiểm soát được uptime của các service này
- **Database reset** cần thực hiện manual trước mỗi test cycle
- **Docker** cần được cài đặt và chạy ổn định (MySQL container)
- **Port conflicts**: Cần đảm bảo các port 3307, 5173, 5174, 8080 không bị chiếm dụng

#### **1.6.5 Data Constraints (Ràng buộc về dữ liệu)**
- Dữ liệu test phải được chuẩn bị thủ công hoặc import từ script SQL
- Không có **test data generator** tự động
- Dữ liệu production không được sử dụng để test (vì lý do bảo mật)

#### **1.6.6 Scope Constraints (Ràng buộc về phạm vi)**
- **Không test các trường hợp edge case** quá phức tạp do hạn chế thời gian
- **Không test compatibility** trên tất cả các trình duyệt cũ (chỉ test các phiên bản mới nhất)
- **Không test performance** chi tiết (response time, throughput, concurrent users)

---

### 1.7 Risk List (Danh sách rủi ro)

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

### 1.8 Training Needs (Nhu cầu đào tạo)

Để đảm bảo quá trình kiểm thử diễn ra hiệu quả, các thành viên trong team cần được đào tạo về các lĩnh vực sau:

#### **1.8.1 Domain Knowledge (Kiến thức nghiệp vụ)**

| **Chủ đề** | **Đối tượng** | **Nội dung** | **Thời gian** |
|-----------|---------------|--------------|---------------|
| **E-commerce Business Flow** | QA, Dev | Quy trình mua hàng online, quản lý đơn hàng, tồn kho, GRN | 2 giờ |
| **UniClub Functional Requirements** | QA, Dev | Đặc tả chức năng chi tiết của từng module | 3 giờ |
| **User Roles & Permissions** | QA | Phân quyền User/Admin, các hành động được phép | 1 giờ |

#### **1.8.2 Technical Skills (Kỹ năng kỹ thuật)**

| **Chủ đề** | **Đối tượng** | **Nội dung** | **Thời gian** |
|-----------|---------------|--------------|---------------|
| **Spring Boot Architecture** | QA, Dev | REST API, JPA/Hibernate, Transaction Management | 4 giờ |
| **React Basics** | QA | Component lifecycle, State management, Routing | 3 giờ |
| **MySQL Fundamentals** | QA | SQL queries, Database schema, Relationships | 2 giờ |
| **VNPay Integration** | QA, Dev | Payment flow, Sandbox testing, Return URL handling | 2 giờ |
| **Docker & Docker Compose** | QA, Dev | Container management, docker-compose.yml | 2 giờ |
| **Postman for API Testing** | QA | Request/Response, Collections, Environment variables | 2 giờ |
| **Git & Version Control** | QA | Branch management, Pull requests, Merge conflicts | 1 giờ |

#### **1.8.3 Testing Methodologies (Phương pháp kiểm thử)**

| **Chủ đề** | **Đối tượng** | **Nội dung** | **Thời gian** |
|-----------|---------------|--------------|---------------|
| **Test Case Design Techniques** | QA | Equivalence partitioning, Boundary value analysis, Decision table | 3 giờ |
| **Functional Testing** | QA | Black-box testing, Test scenarios, Expected vs Actual | 2 giờ |
| **Integration Testing** | QA, Dev | API testing, Database testing, End-to-End flows | 3 giờ |
| **Regression Testing** | QA | Impact analysis, Test suite prioritization | 2 giờ |
| **Defect Management** | QA | Bug reporting, Severity/Priority, Bug lifecycle | 2 giờ |
| **Traceability Matrix** | QA | Requirement-to-test mapping | 1 giờ |

#### **1.8.4 Tools Training (Đào tạo công cụ)**

| **Công cụ** | **Đối tượng** | **Nội dung** | **Thời gian** |
|-------------|---------------|--------------|---------------|
| **Postman** | QA | API testing, Collections, Automated tests | 3 giờ |
| **DBeaver** | QA | Database connection, Query execution, Data export | 2 giờ |
| **Chrome DevTools** | QA | Network tab, Console, Responsive design testing | 2 giờ |
| **VS Code** | QA | Code navigation, Search, Terminal usage | 1 giờ |
| **Browser DevTools** | QA | Debugging JavaScript, Inspecting elements | 2 giờ |

#### **1.8.5 Project-Specific Training (Đào tạo theo dự án)**

| **Chủ đề** | **Đối tượng** | **Nội dung** | **Thời gian** |
|-----------|---------------|--------------|---------------|
| **UniClub System Architecture** | QA, Dev | Frontend-Backend-Database integration, Deployment | 2 giờ |
| **Setup Development Environment** | QA | Follow SETUP_GUIDE.md, DOCKER_SETUP_GUIDE.md | 2 giờ |
| **Authentication Flow** | QA | Login/Register, JWT tokens, Session management | 1 giờ |
| **Payment Flow (COD & VNPay)** | QA | Checkout process, Payment status, Retry payment | 2 giờ |
| **Inventory Management Logic** | QA, Dev | Stock deduction/restoration, GRN impact | 2 giờ |
| **Order Lifecycle** | QA | PENDING → CONFIRMED → SHIPPING → DELIVERED → CANCELLED | 1 giờ |
| **Scheduled Tasks** | QA, Dev | PaymentExpirationScheduler, Auto-cancel orders | 1 giờ |

#### **1.8.6 Training Schedule (Lịch trình đào tạo)**

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

#### **1.8.7 Training Deliverables (Sản phẩm đào tạo)**

- ✅ **Training Materials**: Slides, Documents, Video recordings
- ✅ **Hands-on Labs**: Step-by-step exercises
- ✅ **Reference Guides**: Quick reference cards for tools and APIs
- ✅ **Knowledge Check**: Quiz sau mỗi session
- ✅ **Certification**: Certificate of completion (nếu cần)

#### **1.8.8 Continuous Learning (Học tập liên tục)**

- **Weekly Knowledge Sharing**: 30 phút mỗi tuần để chia sẻ kinh nghiệm
- **Bug Review Sessions**: Review các bug đã tìm thấy, học từ mistakes
- **Tool Updates**: Cập nhật khi có công cụ mới hoặc version mới
- **Best Practices**: Chia sẻ best practices trong kiểm thử

---

## 2. TEST STRATEGY (Chiến lược kiểm thử)

### 2.1 Introduction (Giới thiệu)

Chiến lược kiểm thử cho hệ thống UniClub được thiết kế dựa trên phương pháp **Risk-Based Testing** kết hợp với **Agile Testing Principles**. Mục tiêu chính là đảm bảo chất lượng của website thương mại điện tử thông qua việc kiểm tra toàn diện các chức năng cốt lõi, với ưu tiên cao nhất cho các module liên quan đến nghiệp vụ quan trọng như Giỏ hàng, Đặt hàng, Thanh toán và Quản lý tồn kho.

Chiến lược này phù hợp với bối cảnh đồ án môn học Kiểm thử phần mềm, tập trung vào các kỹ thuật kiểm thử thực tế và khả thi với nguồn lực hạn chế.

---

### 2.2 Test Levels (Các mức độ kiểm thử)

#### **2.2.1 Unit Testing (Kiểm thử đơn vị)**

**Mục tiêu:** Kiểm tra các đơn vị mã nguồn nhỏ nhất (methods, functions) hoạt động đúng độc lập.

**Phạm vi:**
- Service layer methods (Backend)
- Utility classes (VNPayUtil, DateUtil, StringUtil)
- Repository queries (Custom JPA queries)

**Người thực hiện:** Development Team

**Công cụ:** JUnit 5, Mockito, Spring Boot Test

**Tiêu chí chấp nhận:**
- Code coverage ≥ 70% cho service layer
- Tất cả critical business logic được cover

**Ví dụ test case:**
- Test `VNPayUtil.generateSecureHash()` với different inputs
- Test `OrderService.calculateTotal()` với nhiều items khác nhau
- Test `InventoryService.deductStock()` với stock availability scenarios

---

#### **2.2.2 Integration Testing (Kiểm thử tích hợp)**

**Mục tiêu:** Kiểm tra tích hợp giữa các components/modules khác nhau trong hệ thống.

**Phạm vi:**
- **Backend-Database Integration:** 
  - CRUD operations qua JPA/Hibernate
  - Transaction management (@Transactional)
  - Database constraints và triggers
  
- **Frontend-Backend Integration:**
  - REST API calls
  - Request/Response data format (JSON)
  - HTTP status codes
  - Error handling
  
- **Third-party Integration:**
  - VNPay payment gateway
  - SendGrid email service
  - Cloudinary image storage
  - Vietnam Provinces API

**Người thực hiện:** QA Team + Development Team

**Công cụ:** 
- Postman (API testing)
- DBeaver (Database verification)
- Browser DevTools (Network inspection)

**Tiêu chí chấp nhận:**
- Tất cả API endpoints trả về status code đúng
- Data được persist vào database chính xác
- Third-party services hoạt động ổn định (hoặc có fallback)

**Ví dụ test case:**
- Test API POST `/api/orders` → Verify order được tạo trong database
- Test VNPay payment flow → Verify payment status được update
- Test upload image → Verify image URL được lưu vào Cloudinary

---

#### **2.2.3 System Testing (Kiểm thử hệ thống)**

**Mục tiêu:** Kiểm tra toàn bộ hệ thống hoạt động như một thể thống nhất, đáp ứng các yêu cầu chức năng và phi chức năng.

**Phạm vi:**
- Functional Testing (Kiểm thử chức năng)
- UI Testing (Kiểm thử giao diện)
- Usability Testing (Kiểm thử tính khả dụng)
- Compatibility Testing (Kiểm thử tương thích)
- Security Testing cơ bản (Authentication, Authorization)

**Người thực hiện:** QA Team

**Công cụ:** Manual testing, Browser DevTools, Multiple browsers

**Tiêu chí chấp nhận:**
- Tất cả functional requirements được đáp ứng
- UI hiển thị đúng trên các trình duyệt chính (Chrome, Firefox, Edge)
- Không có critical/major bugs

---

#### **2.2.4 Acceptance Testing (Kiểm thử chấp nhận)**

**Mục tiêu:** Xác nhận hệ thống đáp ứng yêu cầu nghiệp vụ và sẵn sàng để sử dụng.

**Phạm vi:**
- End-to-End user scenarios
- Business workflows
- User experience validation

**Người thực hiện:** Product Owner, End Users (hoặc giảng viên hướng dẫn)

**Công cụ:** Manual testing trên môi trường gần giống production

**Tiêu chí chấp nhận:**
- Tất cả critical business flows hoạt động mượt mà
- User có thể hoàn thành mục tiêu nghiệp vụ (browse → add to cart → checkout → pay)
- Sign-off từ stakeholders

---

### 2.3 Test Types (Các loại kiểm thử)

#### **2.3.1 Functional Testing (Kiểm thử chức năng)**

**Định nghĩa:** Kiểm tra các chức năng của hệ thống hoạt động đúng theo đặc tả yêu cầu.

**Phương pháp:** Black-box testing

**Kỹ thuật thiết kế test case:**
- **Equivalence Partitioning (Phân vùng tương đương):** Chia input thành các nhóm tương đương
- **Boundary Value Analysis (Phân tích giá trị biên):** Test các giá trị ở ranh giới
- **Decision Table (Bảng quyết định):** Test các kết hợp điều kiện
- **State Transition (Chuyển trạng thái):** Test các trạng thái của đơn hàng

**Ví dụ:**
- Test login với valid/invalid credentials
- Test add to cart với stock available/out of stock
- Test checkout với COD/VNPay payment methods

**Công cụ:** Manual testing, Test case management (Excel/Google Sheets)

---

#### **2.3.2 API Testing (Kiểm thử API)**

**Định nghĩa:** Kiểm tra REST API endpoints của Backend.

**Nội dung kiểm tra:**
- Request methods (GET, POST, PUT, DELETE)
- Request headers (Authorization, Content-Type)
- Request body (JSON format, required fields)
- Response status codes (200, 201, 400, 401, 404, 500)
- Response body (JSON structure, data validation)
- Response time (acceptable performance)

**Công cụ:** Postman

**Ví dụ test case:**
```
Endpoint: POST /api/auth/login
Request Body: {"email": "user@example.com", "password": "validPassword"}
Expected Response: 200 OK, {"token": "...", "user": {...}}

Endpoint: POST /api/auth/login
Request Body: {"email": "user@example.com", "password": "wrongPassword"}
Expected Response: 401 Unauthorized, {"message": "Invalid credentials"}
```

---

#### **2.3.3 Database Testing (Kiểm thử cơ sở dữ liệu)**

**Định nghĩa:** Kiểm tra tính toàn vẹn, chính xác và nhất quán của dữ liệu trong database.

**Nội dung kiểm tra:**
- **Data Integrity:** Foreign key constraints, unique constraints, not null constraints
- **CRUD Operations:** Create, Read, Update, Delete records
- **Transaction Management:** Commit, Rollback, ACID properties
- **Data Accuracy:** Verify calculated values (order total, stock quantity)

**Công cụ:** DBeaver, MySQL Workbench, SQL queries

**Ví dụ test case:**
```sql
-- Test: Verify stock deducted after order confirmed
SELECT quantity FROM variants WHERE id = 1; -- Before: 100
-- Execute: Confirm order with 5 units of variant_id=1
SELECT quantity FROM variants WHERE id = 1; -- After: 95 (Expected)
```

---

#### **2.3.4 UI Testing (Kiểm thử giao diện người dùng)**

**Định nghĩa:** Kiểm tra giao diện người dùng hiển thị đúng và tương tác tốt.

**Nội dung kiểm tra:**
- Layout và design consistency
- Responsive design (Desktop, Tablet, Mobile)
- Form validation (client-side validation)
- Error messages hiển thị rõ ràng
- Navigation và routing
- Button states (disabled, loading)

**Công cụ:** Manual testing, Browser DevTools (Responsive mode)

**Ví dụ test case:**
- Verify login form hiển thị error khi submit empty fields
- Verify product images load correctly
- Verify cart icon update số lượng khi add product
- Verify responsive design trên mobile (375px width)

---

#### **2.3.5 End-to-End Testing (Kiểm thử đầu-cuối)**

**Định nghĩa:** Kiểm tra complete user journeys từ đầu đến cuối.

**Phạm vi:** Test toàn bộ workflow từ Frontend → Backend → Database → Third-party services

**Ví dụ scenarios:**

**Scenario 1: Guest User Purchases Product (COD)**
1. Access homepage
2. Browse products by category
3. View product detail, select size/color
4. Add to cart
5. Update cart quantity
6. Proceed to checkout
7. Fill shipping information
8. Select COD payment method
9. Confirm order
10. Verify order created with PENDING status
11. Admin confirms order → Status = CONFIRMED
12. Verify stock deducted
13. Admin updates to SHIPPING → DELIVERED
14. User receives product

**Scenario 2: User Purchases with VNPay**
1. Login as registered user
2. Browse products, add to cart
3. Proceed to checkout
4. Select VNPay payment method
5. Redirect to VNPay Sandbox
6. Complete payment
7. Redirect back to website
8. Verify payment SUCCESS, order CONFIRMED
9. Verify stock deducted
10. View order detail with payment info

**Scenario 3: VNPay Payment Timeout**
1. User creates order with VNPay
2. Do NOT complete payment
3. Wait 15 minutes
4. PaymentExpirationScheduler runs
5. Verify order auto-cancelled
6. Verify stock restored

**Công cụ:** Manual testing (may consider Selenium/Cypress for automation in future)

---

#### **2.3.6 Regression Testing (Kiểm thử hồi quy)**

**Định nghĩa:** Kiểm tra lại các chức năng đã test trước đó sau khi có thay đổi code (bug fix hoặc new feature).

**Mục tiêu:** Đảm bảo các thay đổi mới không làm hỏng chức năng cũ.

**Phương pháp:**
- Re-run critical test cases
- Focus on impacted areas (based on code changes)
- Use regression test suite (subset of all test cases)

**Khi nào thực hiện:**
- Sau khi fix bug
- Sau khi thêm feature mới
- Before release candidate

**Công cụ:** Manual re-testing (prioritize automation in future)

---

#### **2.3.7 Compatibility Testing (Kiểm thử tương thích)**

**Định nghĩa:** Kiểm tra hệ thống hoạt động đúng trên các môi trường khác nhau.

**Phạm vi:**

**Browser Compatibility:**
- Google Chrome (latest version)
- Mozilla Firefox (latest version)
- Microsoft Edge (latest version)
- Safari (nếu có MacOS)

**Device Compatibility:**
- Desktop (1920x1080, 1366x768)
- Tablet (768px width)
- Mobile (375px, 414px width)

**Operating System:**
- Windows 10/11
- MacOS (nếu có)
- Linux (Ubuntu)

**Công cụ:** Browser DevTools (Device emulation), Real devices

---

#### **2.3.8 Usability Testing (Kiểm thử tính khả dụng)**

**Định nghĩa:** Kiểm tra hệ thống có dễ sử dụng, trực quan và thân thiện với người dùng không.

**Tiêu chí đánh giá:**
- **Learnability:** User mới có thể học cách sử dụng nhanh không?
- **Efficiency:** User có thể hoàn thành task nhanh chóng không?
- **Memorability:** User có nhớ cách sử dụng sau một thời gian không dùng không?
- **Errors:** Hệ thống có giúp user tránh/sửa lỗi dễ dàng không?
- **Satisfaction:** User có hài lòng với trải nghiệm không?

**Phương pháp:**
- User observation (quan sát user thực hiện tasks)
- User feedback (thu thập ý kiến)
- Heuristic evaluation (đánh giá theo checklist)

**Ví dụ task:**
- "Hãy tìm và mua một chiếc áo size M màu đỏ"
- "Hãy hủy đơn hàng vừa đặt"
- "Hãy thay đổi mật khẩu tài khoản"

---

#### **2.3.9 Security Testing (Kiểm thử bảo mật - Cơ bản)**

**Lưu ý:** Đây chỉ là security testing cơ bản phù hợp với đồ án, không phải penetration testing chuyên sâu.

**Nội dung kiểm tra:**

**Authentication & Authorization:**
- Verify user không thể access admin pages
- Verify expired token được reject
- Verify password được hash (không lưu plaintext)

**Input Validation:**
- Test SQL Injection attempts (e.g., `' OR 1=1 --`)
- Test XSS attempts (e.g., `<script>alert('XSS')</script>`)
- Test special characters trong forms

**Session Management:**
- Verify session timeout
- Verify logout invalidates token
- Verify concurrent sessions handling

**Data Protection:**
- Verify HTTPS được sử dụng (nếu deploy)
- Verify sensitive data (password, payment info) được encrypt

**Công cụ:** Manual testing, Postman (API security checks)

---

### 2.4 Test Design Techniques (Kỹ thuật thiết kế kiểm thử)

#### **2.4.1 Equivalence Partitioning (Phân vùng tương đương)**

**Nguyên lý:** Chia input thành các nhóm có hành vi tương tự nhau. Chỉ cần test 1 giá trị đại diện cho mỗi nhóm.

**Ví dụ: Test field "Số lượng" khi thêm sản phẩm vào giỏ hàng**

| **Partition** | **Giá trị hợp lệ** | **Kết quả mong đợi** |
|---------------|-------------------|----------------------|
| Invalid (< 1) | 0, -1, -100 | Error: "Số lượng phải >= 1" |
| Valid (1-999) | 1, 50, 999 | Success: Add to cart |
| Invalid (> stock) | 1001 (nếu stock = 1000) | Error: "Vượt quá tồn kho" |

---

#### **2.4.2 Boundary Value Analysis (Phân tích giá trị biên)**

**Nguyên lý:** Test các giá trị ở ranh giới của các partition, vì bug thường xảy ra ở đây.

**Ví dụ: Test field "Giá sản phẩm" (Valid: 1,000 - 10,000,000 VNĐ)**

| **Boundary** | **Giá trị test** | **Kết quả mong đợi** |
|--------------|------------------|----------------------|
| Lower - 1 | 999 | Invalid |
| Lower | 1,000 | Valid |
| Lower + 1 | 1,001 | Valid |
| Upper - 1 | 9,999,999 | Valid |
| Upper | 10,000,000 | Valid |
| Upper + 1 | 10,000,001 | Invalid |

---

#### **2.4.3 Decision Table Testing (Kiểm thử bảng quyết định)**

**Nguyên lý:** Sử dụng bảng để mô tả các kết hợp điều kiện và hành động tương ứng.

**Ví dụ: Discount Logic**

| **Rule** | **1** | **2** | **3** | **4** |
|----------|-------|-------|-------|-------|
| **Conditions** |||||
| User is logged in? | Yes | Yes | No | No |
| Order total >= 500,000 VNĐ? | Yes | No | Yes | No |
| **Actions** |||||
| Apply 10% discount | ✓ | - | - | - |
| Apply 5% discount | - | ✓ | - | - |
| No discount | - | - | ✓ | ✓ |

---

#### **2.4.4 State Transition Testing (Kiểm thử chuyển trạng thái)**

**Nguyên lý:** Test các chuyển đổi giữa các trạng thái của object.

**Ví dụ: Order State Transitions**

```
PENDING → CONFIRMED → SHIPPING → DELIVERED
   ↓
CANCELLED (có thể cancel từ PENDING hoặc CONFIRMED)
```

**Test cases:**
- PENDING → CONFIRMED (Admin confirms order)
- PENDING → CANCELLED (User cancels, Auto-timeout)
- CONFIRMED → SHIPPING (Admin ships)
- CONFIRMED → CANCELLED (Admin cancels)
- SHIPPING → DELIVERED (Admin marks delivered)
- SHIPPING → CANCELLED (Invalid - should not allow)

---

#### **2.4.5 Error Guessing (Dự đoán lỗi)**

**Nguyên lý:** Dựa vào kinh nghiệm để dự đoán các lỗi có thể xảy ra.

**Ví dụ common errors:**
- Empty fields submission
- Special characters (', ", <, >, &)
- Very long strings (SQL injection attempts)
- Duplicate submissions (double click button)
- Invalid file formats (upload .exe instead of .jpg)
- Network interruption during transaction
- Browser back button during checkout
- Concurrent updates (2 users order last item simultaneously)

---

### 2.5 Entry and Exit Criteria (Tiêu chí bắt đầu và kết thúc)

#### **2.5.1 Entry Criteria (Điều kiện để bắt đầu kiểm thử)**

Kiểm thử chỉ được bắt đầu khi các điều kiện sau được đáp ứng:

✅ **Requirements:**
- Software Requirements Specification (SRS) đã được approve
- Functional requirements được document rõ ràng
- Acceptance criteria được định nghĩa

✅ **Environment:**
- Test environment được setup (localhost hoặc staging)
- Database được initialized với schema mới nhất
- All services running (Backend, Frontend, MySQL, Docker)

✅ **Test Preparation:**
- Test plan được approve
- Test cases được viết và review
- Test data được chuẩn bị

✅ **Code Quality:**
- Build successful (no compilation errors)
- Unit tests pass ≥ 70%
- Code được commit lên Git

✅ **Resources:**
- Testers available và đã được training
- Testing tools installed (Postman, DBeaver, Browsers)

---

#### **2.5.2 Exit Criteria (Điều kiện để kết thúc kiểm thử)**

Kiểm thử được coi là hoàn thành khi:

✅ **Test Execution:**
- 100% test cases được execute
- ≥ 95% CRITICAL test cases PASS
- ≥ 90% HIGH priority test cases PASS
- ≥ 80% MEDIUM priority test cases PASS

✅ **Defect Status:**
- 0 CRITICAL defects còn open
- ≤ 2 MAJOR defects còn open (và có workaround)
- All P0, P1 defects resolved

✅ **Coverage:**
- All critical business flows tested
- All functional requirements covered
- Traceability matrix 100% complete

✅ **Documentation:**
- Test execution report completed
- Defect log finalized
- Test summary report approved

✅ **Stakeholder Sign-off:**
- Product Owner/Giảng viên approve
- Test team sign-off

---

### 2.6 Suspension and Resumption Criteria (Tiêu chí tạm dừng và tiếp tục)

#### **2.6.1 Suspension Criteria (Khi nào tạm dừng kiểm thử?)**

Kiểm thử sẽ bị tạm dừng nếu:

🛑 **Critical Blockers:**
- Ứng dụng không thể start (build failed, port conflict)
- Database không connect được
- Hơn 50% test cases bị block bởi cùng 1 bug
- Critical functionality hoàn toàn broken (e.g., không thể login)

🛑 **Environment Issues:**
- Test environment down hoặc unstable
- Third-party services down (VNPay Sandbox)
- Major infrastructure issues

🛑 **Resource Issues:**
- Key personnel unavailable (tester sick, developer busy)
- Testing tools không available

🛑 **Build Quality:**
- Build quá unstable (too many crashes)
- > 10 critical bugs found trong 1 ngày

---

#### **2.6.2 Resumption Criteria (Khi nào tiếp tục kiểm thử?)**

Kiểm thử sẽ được tiếp tục khi:

✅ **Blockers Resolved:**
- Critical bugs được fix và verify
- Build stable (no crashes trong 2 hours)
- Environment issues resolved

✅ **Communication:**
- Development team confirms fixes deployed
- Updated build available với fix notes
- Regression test suite identified

✅ **Resources Ready:**
- Testers available
- Environment accessible

---

### 2.7 Test Deliverables (Sản phẩm bàn giao)

Sau khi hoàn thành kiểm thử, các tài liệu sau sẽ được bàn giao:

#### **2.7.1 Test Planning Phase**
- ✅ Test Plan (tài liệu này)
- ✅ Test Strategy Document
- ✅ Risk Analysis Report

#### **2.7.2 Test Design Phase**
- ✅ Test Case Document (Excel/Google Sheets)
  - Test Case ID, Title, Description, Preconditions, Steps, Expected Result, Priority
- ✅ Test Data Preparation Document
- ✅ Traceability Matrix (Requirement ID ↔ Test Case ID)

#### **2.7.3 Test Execution Phase**
- ✅ Test Execution Report
  - Test cases executed: PASS/FAIL/BLOCKED
  - Execution date, Tester name
  - Screenshots for failed cases
- ✅ Defect Log (Bug Report)
  - Bug ID, Title, Severity, Priority, Status, Assignee
  - Steps to reproduce, Actual vs Expected
  - Screenshots/logs
- ✅ Daily Test Status Report (during execution)

#### **2.7.4 Test Closure Phase**
- ✅ Test Summary Report
  - Total test cases, Pass rate, Fail rate
  - Defect summary (by severity, by module)
  - Test coverage analysis
  - Lessons learned
- ✅ Test Metrics
  - Defect density, Defect removal efficiency
  - Test execution productivity
- ✅ Sign-off Document

---



---

## 3. REQUIREMENTS FOR TEST (Yêu cầu cho kiểm thử)

### 3.1 Test Items (Các mục được kiểm thử)

Phần này xác định các thành phần cụ thể của hệ thống UniClub sẽ được kiểm thử, bao gồm các modules, features, APIs, và database schema.

---

#### **3.1.1 Backend Components (Spring Boot 3.5.6)**

| **Component** | **Description** | **Test Level** | **Priority** |
|---------------|-----------------|----------------|--------------|
| **Authentication Service** | Xử lý đăng ký, đăng nhập, JWT token generation | Unit, Integration, System | CRITICAL |
| **User Service** | Quản lý user profiles, addresses, password | Unit, Integration | HIGH |
| **Product Service** | CRUD products, variants, images | Unit, Integration | HIGH |
| **Category/Brand/Color/Size Service** | Quản lý master data | Unit, Integration | MEDIUM |
| **Cart Service** | Add/update/remove cart items, calculate total | Unit, Integration, E2E | CRITICAL |
| **Order Service** | Create orders, update status, handle cancellation | Unit, Integration, E2E | CRITICAL |
| **Payment Service** | VNPay integration, payment processing | Unit, Integration, E2E | CRITICAL |
| **Inventory Service** | Stock management, deduction, restoration | Unit, Integration | CRITICAL |
| **Supplier Service** | CRUD suppliers | Unit, Integration | MEDIUM |
| **GRN Service** | Create GRN, auto-update inventory | Unit, Integration | HIGH |
| **Review Service** | CRUD product reviews, calculate ratings | Unit, Integration | MEDIUM |
| **Scheduled Tasks** | PaymentExpirationScheduler (auto-cancel orders) | Integration | CRITICAL |

---

#### **3.1.2 Frontend Components (React 19.2.0)**

| **Component** | **Description** | **Test Level** | **Priority** |
|---------------|-----------------|----------------|--------------|
| **User Web (Port 5173)** | Customer-facing website | System, E2E, Compatibility | CRITICAL |
| - Authentication Pages | Login, Register, Forgot Password | UI, E2E | CRITICAL |
| - Product Pages | Product list, detail, search, filter | UI, E2E | HIGH |
| - Cart Page | View cart, update quantities, remove items | UI, E2E | CRITICAL |
| - Checkout Page | Shipping info, payment method selection | UI, E2E | CRITICAL |
| - Order Pages | Order list, order detail, cancel order | UI, E2E | HIGH |
| - Payment Return Page | VNPay callback handling | UI, E2E | CRITICAL |
| - Profile Pages | Update profile, change password, manage addresses | UI, E2E | MEDIUM |
| - Review Components | Add/edit/view reviews | UI | MEDIUM |
| **Admin Panel (Port 5174)** | Admin management interface | System, E2E | HIGH |
| - Dashboard | Statistics, overview | UI | MEDIUM |
| - Product Management | CRUD products, variants, stock | UI, E2E | HIGH |
| - Order Management | View orders, update status | UI, E2E | HIGH |
| - Supplier & GRN | Manage suppliers, create GRN | UI, E2E | MEDIUM |
| - Master Data Management | Categories, brands, colors, sizes | UI | MEDIUM |
| - User Management | View users, manage roles | UI | MEDIUM |

---

#### **3.1.3 Database Schema (MySQL 8.0)**

| **Table** | **Description** | **Test Focus** | **Priority** |
|-----------|-----------------|----------------|--------------|
| **users** | User accounts and credentials | Data integrity, constraints, password hashing | CRITICAL |
| **addresses** | User shipping addresses | CRUD, foreign key constraints | MEDIUM |
| **products** | Product master data | CRUD, relationships with categories/brands | HIGH |
| **variants** | Product variants (size + color + stock) | CRUD, stock calculations, constraints | CRITICAL |
| **categories** | Product categories | CRUD, uniqueness, cascade delete prevention | MEDIUM |
| **brands** | Product brands | CRUD, uniqueness, cascade delete prevention | MEDIUM |
| **colors** | Available colors | CRUD, uniqueness | LOW |
| **sizes** | Available sizes | CRUD, uniqueness | LOW |
| **cart_items** | Shopping cart items | CRUD, foreign keys, auto-cleanup | HIGH |
| **orders** | Customer orders | CRUD, status transitions, relationships | CRITICAL |
| **order_items** | Order line items | CRUD, foreign keys, cascade operations | CRITICAL |
| **payments** | Payment transactions | CRUD, VNPay transaction tracking | CRITICAL |
| **suppliers** | Supplier information | CRUD, uniqueness | MEDIUM |
| **grn** | Goods Receipt Notes | CRUD, inventory impact | HIGH |
| **grn_items** | GRN line items | CRUD, stock updates | HIGH |
| **reviews** | Product reviews and ratings | CRUD, rating calculations | MEDIUM |

---

#### **3.1.4 REST API Endpoints**

| **API Group** | **Endpoints** | **Test Focus** | **Priority** |
|---------------|---------------|----------------|--------------|
| **Authentication** | POST /api/auth/register<br>POST /api/auth/login<br>POST /api/auth/logout | Request validation, Response codes, Token generation | CRITICAL |
| **Users** | GET /api/users/profile<br>PUT /api/users/profile<br>PUT /api/users/password<br>GET/POST/PUT/DELETE /api/users/addresses | Authorization, Data validation, CRUD operations | HIGH |
| **Products** | GET /api/products<br>GET /api/products/{id}<br>GET /api/products/search<br>POST/PUT/DELETE /api/products (Admin) | Pagination, Filtering, Authorization, Image upload | HIGH |
| **Variants** | GET /api/variants<br>POST/PUT/DELETE /api/variants (Admin) | Stock validation, Uniqueness constraints | CRITICAL |
| **Cart** | GET /api/cart<br>POST /api/cart<br>PUT /api/cart/{id}<br>DELETE /api/cart/{id} | Stock validation, Total calculation, Concurrency | CRITICAL |
| **Orders** | POST /api/orders<br>GET /api/orders<br>GET /api/orders/{id}<br>PUT /api/orders/{id}/cancel<br>POST /api/orders/{id}/retry-payment | Payment method handling, Status transitions, Stock impact | CRITICAL |
| **Payments** | POST /api/vnpay/create-payment<br>GET /api/vnpay/return<br>GET /api/vnpay/ipn | VNPay integration, Signature verification, Status updates | CRITICAL |
| **Categories/Brands/Colors/Sizes** | GET/POST/PUT/DELETE /api/categories<br>GET/POST/PUT/DELETE /api/brands<br>GET/POST/PUT/DELETE /api/colors<br>GET/POST/PUT/DELETE /api/sizes | CRUD operations, Cascade delete prevention | MEDIUM |
| **Suppliers** | GET/POST/PUT/DELETE /api/suppliers | CRUD operations, Validation | MEDIUM |
| **GRN** | POST /api/grn<br>GET /api/grn<br>GET /api/grn/{id} | Inventory impact, Transaction management | HIGH |
| **Reviews** | GET /api/products/{id}/reviews<br>POST /api/reviews<br>PUT /api/reviews/{id}<br>DELETE /api/reviews/{id} | Purchase verification, Rating calculation | MEDIUM |

---

#### **3.1.5 Third-party Integrations**

| **Service** | **Description** | **Test Focus** | **Priority** |
|-------------|-----------------|----------------|--------------|
| **VNPay Sandbox** | Payment gateway | Payment URL generation, Return handling, IPN, Signature verification | CRITICAL |
| **Cloudinary** | Image storage and CDN | Image upload, URL generation, Error handling | HIGH |
| **SendGrid** | Email service | Email sending (optional - có thể mock) | LOW |
| **Vietnam Provinces API** | Address autocomplete | API integration, Data fetching (optional - có thể mock) | LOW |

**Lưu ý cho đồ án:** 
- VNPay Sandbox phải test thật vì đây là core feature
- Cloudinary test thật nếu có account free
- SendGrid và Provinces API có thể mock hoặc bỏ qua nếu không ảnh hưởng critical flows

---

#### **3.1.6 Business Logic & Workflows**

| **Workflow** | **Description** | **Test Focus** | **Priority** |
|--------------|-----------------|----------------|--------------|
| **User Registration Flow** | Register → Email verification (optional) → Login | Validation, Email uniqueness, Password hashing | HIGH |
| **Product Browsing Flow** | Home → Category → Product List → Product Detail | Filtering, Sorting, Pagination, Image loading | MEDIUM |
| **Add to Cart Flow** | Select variant → Add to cart → Update cart icon | Stock validation, Cart persistence | CRITICAL |
| **Checkout Flow (COD)** | Cart → Checkout → Fill info → Select COD → Confirm | Form validation, Order creation, Stock impact | CRITICAL |
| **Checkout Flow (VNPay)** | Cart → Checkout → Fill info → Select VNPay → Redirect → Pay → Return | VNPay integration, Payment status, Stock impact | CRITICAL |
| **Order Management Flow** | PENDING → CONFIRMED → SHIPPING → DELIVERED | Status transitions, Notifications, Stock updates | HIGH |
| **Order Cancellation Flow** | Cancel order → Restore stock → Update status | Stock restoration, Transaction rollback | CRITICAL |
| **Payment Timeout Flow** | VNPay PENDING > 15min → Auto-cancel → Restore stock | Scheduler execution, Lazy loading, Stock restoration | CRITICAL |
| **GRN Flow** | Create GRN → Auto-update inventory | Inventory calculation, Transaction management | HIGH |
| **Review Flow** | Purchase verification → Add review → Calculate rating | Authorization, Rating calculation | MEDIUM |

---

### 3.2 Features to be Tested (Các tính năng được kiểm thử)

#### **3.2.1 Functional Requirements**

Dựa trên Software Requirements Specification (SRS), các functional requirements sau sẽ được kiểm thử:

**FR-001: User Authentication**
- **Description:** Hệ thống phải cho phép user đăng ký, đăng nhập, đăng xuất
- **Acceptance Criteria:**
  - Email phải unique và valid format
  - Password ≥ 8 characters
  - JWT token được generate khi login thành công
  - Token expires sau 24 hours
- **Test Priority:** CRITICAL

**FR-002: Product Catalog Management**
- **Description:** Admin có thể CRUD products với variants (size + color)
- **Acceptance Criteria:**
  - Mỗi product phải có ít nhất 1 variant
  - Product images được upload lên Cloudinary
  - Variants có stock quantity riêng
  - Users có thể view, search, filter products
- **Test Priority:** HIGH

**FR-003: Shopping Cart**
- **Description:** User có thể add/update/remove items trong cart
- **Acceptance Criteria:**
  - Quantity không được vượt quá available stock
  - Cart total được calculate tự động
  - Cart được persist khi login/logout
  - Real-time stock validation
- **Test Priority:** CRITICAL

**FR-004: Order Management**
- **Description:** User có thể tạo orders với COD hoặc VNPay
- **Acceptance Criteria:**
  - COD orders có status PENDING ban đầu
  - VNPay orders redirect đến payment gateway
  - Stock chỉ deduct khi status = CONFIRMED
  - User có thể cancel orders với status PENDING/CONFIRMED
  - Admin có thể update order status
- **Test Priority:** CRITICAL

**FR-005: Payment Processing**
- **Description:** Hệ thống tích hợp VNPay payment gateway
- **Acceptance Criteria:**
  - Generate correct VNPay payment URL
  - Verify VNPay signature (HMAC SHA512)
  - Handle return URL và IPN
  - Auto-cancel orders sau 15 minutes timeout
  - Update payment status correctly
- **Test Priority:** CRITICAL

**FR-006: Inventory Management**
- **Description:** Hệ thống tự động quản lý tồn kho
- **Acceptance Criteria:**
  - Stock deduct khi order CONFIRMED
  - Stock restore khi order CANCELLED
  - GRN tự động cập nhật stock
  - Low stock alerts (optional)
- **Test Priority:** CRITICAL

**FR-007: Supplier & GRN Management**
- **Description:** Admin quản lý suppliers và phiếu nhập hàng
- **Acceptance Criteria:**
  - CRUD suppliers
  - Create GRN với multiple items
  - GRN tự động update inventory
  - Transaction management
- **Test Priority:** HIGH

**FR-008: Product Reviews**
- **Description:** User có thể review products đã mua
- **Acceptance Criteria:**
  - Chỉ user đã mua mới được review
  - Rating: 1-5 stars
  - Average rating tự động calculate
- **Test Priority:** MEDIUM

---

#### **3.2.2 Non-Functional Requirements**

**NFR-001: Performance (Basic)**
- **Description:** Hệ thống phải responsive với single user
- **Acceptance Criteria:**
  - Page load time < 3 seconds (trên localhost)
  - API response time < 1 second (trên localhost)
  - Database queries optimized (no N+1 queries)
- **Test Priority:** MEDIUM
- **Note:** Không test với multiple concurrent users (không phải load testing)

**NFR-002: Usability**
- **Description:** Giao diện thân thiện và dễ sử dụng
- **Acceptance Criteria:**
  - Consistent design across pages
  - Clear error messages
  - Intuitive navigation
  - Form validation with helpful hints
- **Test Priority:** MEDIUM

**NFR-003: Compatibility**
- **Description:** Hoạt động trên các trình duyệt chính
- **Acceptance Criteria:**
  - Chrome (latest version)
  - Firefox (latest version)
  - Edge (latest version)
  - Responsive design: Desktop, Tablet, Mobile
- **Test Priority:** HIGH

**NFR-004: Security (Basic)**
- **Description:** Bảo mật cơ bản cho user data
- **Acceptance Criteria:**
  - Passwords được hash (BCrypt)
  - JWT tokens được validate
  - Role-based access control (User/Admin)
  - Basic input validation (prevent SQL injection, XSS)
- **Test Priority:** HIGH
- **Note:** Không test penetration testing chuyên sâu

**NFR-005: Reliability**
- **Description:** Hệ thống stable và consistent
- **Acceptance Criteria:**
  - No crashes during normal operations
  - Transaction rollback on errors
  - Data consistency maintained
- **Test Priority:** HIGH

**NFR-006: Maintainability**
- **Description:** Code dễ maintain và extend
- **Acceptance Criteria:**
  - Code coverage ≥ 70% (Unit tests)
  - Clear API documentation
  - Database schema documented
- **Test Priority:** MEDIUM
- **Note:** Dev responsibility, QA chỉ verify documentation

---

### 3.3 Features NOT to be Tested (Các tính năng KHÔNG được kiểm thử)

Các features sau **nằm ngoài phạm vi** kiểm thử của đồ án này:

#### **3.3.1 Performance & Scalability**
❌ **Load Testing:** Test với 100+ concurrent users  
❌ **Stress Testing:** Test đến giới hạn hệ thống  
❌ **Volume Testing:** Test với millions of records  
❌ **Spike Testing:** Test với traffic đột ngột tăng  

**Lý do:** Đồ án môn học chỉ test với single user/tester, không có môi trường production-scale

---

#### **3.3.2 Advanced Security**
❌ **Penetration Testing:** Ethical hacking attempts  
❌ **Vulnerability Scanning:** OWASP ZAP, Burp Suite  
❌ **Security Audit:** Comprehensive security review  
❌ **GDPR Compliance:** Data privacy regulations  

**Lý do:** Cần security experts, ngoài scope đồ án

---

#### **3.3.3 Infrastructure & DevOps**
❌ **CI/CD Pipeline Testing:** Jenkins, GitHub Actions  
❌ **Container Orchestration:** Kubernetes, Docker Swarm  
❌ **Cloud Deployment:** AWS, Azure, GCP  
❌ **Monitoring & Logging:** Prometheus, ELK Stack  

**Lý do:** Đồ án chỉ test trên localhost/staging, không deploy production

---

#### **3.3.4 Mobile Applications**
❌ **Native Mobile Apps:** iOS (Swift), Android (Kotlin)  
❌ **Mobile-specific Features:** Push notifications, GPS, Camera  
❌ **App Store Testing:** iOS App Store, Google Play Store  

**Lý do:** UniClub là web application, không có native mobile app

---

#### **3.3.5 Internationalization & Localization**
❌ **Multi-language Support:** English, Chinese, Japanese  
❌ **Currency Conversion:** USD, EUR, JPY  
❌ **Time Zone Handling:** Global time zones  
❌ **RTL Languages:** Arabic, Hebrew  

**Lý do:** Hệ thống chỉ support tiếng Việt và VNĐ currency

---

#### **3.3.6 Third-party Service Internal Logic**
❌ **VNPay Internal Processing:** Payment gateway backend  
❌ **SendGrid Delivery Infrastructure:** Email routing  
❌ **Cloudinary Storage Reliability:** CDN uptime  
❌ **Provinces API Data Accuracy:** Address database  

**Lý do:** Chỉ test integration points (API calls, responses), không test internal của third-party

---

#### **3.3.7 Advanced Analytics & Reporting**
❌ **Business Intelligence:** Power BI, Tableau  
❌ **Data Mining:** Customer behavior analysis  
❌ **Predictive Analytics:** Sales forecasting  
❌ **A/B Testing:** Feature experiments  

**Lý do:** Không có yêu cầu analytics trong đồ án

---

#### **3.3.8 Disaster Recovery & Backup**
❌ **Backup & Restore:** Automated backups  
❌ **Disaster Recovery Plan:** Failover procedures  
❌ **Data Loss Prevention:** Redundancy systems  
❌ **Business Continuity:** 99.9% uptime SLA  

**Lý do:** Ngoài scope đồ án học thuật

---

#### **3.3.9 Accessibility (WCAG)**
❌ **Screen Reader Compatibility:** JAWS, NVDA  
❌ **Keyboard Navigation:** Tab order, shortcuts  
❌ **Color Contrast Ratios:** WCAG AA/AAA  
❌ **ARIA Labels:** Semantic HTML  

**Lý do:** Không có requirement accessibility trong đồ án hiện tại

---

#### **3.3.10 Legacy Systems & Browser Support**
❌ **Internet Explorer 11:** Deprecated browser  
❌ **Old Browser Versions:** Chrome < 90, Firefox < 88  
❌ **Legacy OS:** Windows XP, Windows 7  

**Lý do:** Chỉ test trên latest versions của modern browsers

---

### 3.4 Test Environment Requirements (Yêu cầu môi trường kiểm thử)

#### **3.4.1 Hardware Requirements**

**Minimum Configuration:**
- **CPU:** Intel Core i5 hoặc tương đương
- **RAM:** 8 GB (16 GB recommended)
- **Storage:** 20 GB available space
- **Network:** Stable internet connection (for VNPay, Cloudinary)

**Recommended Configuration:**
- **CPU:** Intel Core i7 hoặc tương đương
- **RAM:** 16 GB
- **Storage:** 50 GB SSD
- **Display:** 1920x1080 resolution

---

#### **3.4.2 Software Requirements**

| **Software** | **Version** | **Purpose** |
|--------------|-------------|-------------|
| **Operating System** | Windows 10/11, macOS, Ubuntu 20.04+ | Development & Testing |
| **Java JDK** | 17 or higher | Backend runtime |
| **Node.js** | 18.x or higher | Frontend runtime |
| **MySQL** | 8.0 | Database |
| **Docker** | Latest | Container management |
| **Git** | Latest | Version control |
| **VS Code** | Latest | Code editor |
| **Postman** | Latest | API testing |
| **DBeaver** | Latest | Database management |
| **Google Chrome** | Latest | Primary browser for testing |
| **Mozilla Firefox** | Latest | Secondary browser for testing |
| **Microsoft Edge** | Latest | Tertiary browser for testing |

---

#### **3.4.3 Test Data Requirements**

**Master Data:**
- 5-10 Categories (Áo, Quần, Phụ kiện, etc.)
- 10-15 Brands (Nike, Adidas, Puma, etc.)
- 10-15 Colors (Đỏ, Xanh, Vàng, etc.)
- 5-10 Sizes (S, M, L, XL, XXL)

**Product Data:**
- 20-50 Products với varied prices
- 100-200 Variants (combinations of size + color + product)
- Product images (stored in Cloudinary)

**User Data:**
- 5-10 Test users (regular users)
- 2-3 Admin users
- Various addresses (Hà Nội, TP.HCM, Đà Nẵng, etc.)

**Transaction Data:**
- 10-20 Test orders (various statuses)
- 5-10 Payments (SUCCESS, FAILED, PENDING)
- 5-10 GRNs (Goods Receipt Notes)
- 10-20 Reviews

**Test Accounts:**
```
Regular User:
- Email: user@test.com
- Password: Test@123

Admin:
- Email: admin@test.com
- Password: Admin@123

VNPay Sandbox Test Cards:
- Card Number: 9704198526191432198
- Name: NGUYEN VAN A
- Issue Date: 07/15
- OTP: 123456 (hoặc theo VNPay docs)
```

---

#### **3.4.4 Network & Access Requirements**

**Required Access:**
- ✅ Internet connection (for VNPay Sandbox, Cloudinary)
- ✅ Localhost access (ports 3307, 5173, 5174, 8080)
- ✅ VNPay Sandbox environment access
- ✅ Cloudinary free account (optional)

**Firewall Configuration:**
- Allow outbound HTTPS (port 443) for VNPay, Cloudinary
- Allow inbound HTTP (port 8080) for Backend API
- Allow inbound HTTP (ports 5173, 5174) for Frontend

---

### 3.5 Test Tools (Công cụ kiểm thử)

| **Tool** | **Type** | **Purpose** | **License** |
|----------|----------|-------------|-------------|
| **Postman** | API Testing | Test REST APIs, Collections, Automation | Free |
| **DBeaver** | Database Testing | SQL queries, Data verification | Free |
| **Chrome DevTools** | Browser Testing | Network inspection, Console logs, Responsive design | Free |
| **Firefox DevTools** | Browser Testing | Cross-browser testing | Free |
| **Excel / Google Sheets** | Test Management | Test case document, Traceability matrix | Free |
| **GitHub** | Version Control | Code repository, Issue tracking | Free |
| **Docker Desktop** | Infrastructure | MySQL container | Free |
| **VS Code** | Code Inspection | Code review, Log analysis | Free |

**Optional (for future enhancement):**
- Selenium WebDriver (UI automation)
- Cypress (E2E automation)
- JMeter (Performance testing - nếu có yêu cầu)

---

### 3.6 Assumptions and Dependencies (Giả định và phụ thuộc)

#### **3.6.1 Assumptions (Giả định)**

✅ **Environment Stability:**
- Localhost environment stable (no frequent crashes)
- Docker containers running smoothly
- Database initialized với schema mới nhất

✅ **Third-party Services:**
- VNPay Sandbox hoạt động ổn định
- Cloudinary free tier đủ quota
- SendGrid có thể mock nếu không có account

✅ **Test Data:**
- Test data được prepare trước khi test
- Database có thể reset về initial state
- No production data được sử dụng

✅ **Resources:**
- Testers có kiến thức cơ bản về web applications
- Access đến source code nếu cần debug
- Documentation đầy đủ (SRS, API docs, Database schema)

---

#### **3.6.2 Dependencies (Phụ thuộc)**

🔗 **Code Dependencies:**
- Backend code stable và buildable
- Frontend code stable và runnable
- All npm/maven dependencies installed

🔗 **Infrastructure Dependencies:**
- Docker installed và configured
- MySQL container running (port 3307)
- Backend running (port 8080)
- Frontend running (ports 5173, 5174)

🔗 **External Services:**
- VNPay Sandbox accessible
- Cloudinary API working (or mock)
- SendGrid API working (or mock)
- Vietnam Provinces API working (or mock)

🔗 **Documentation Dependencies:**
- SRS (Software Requirements Specification)
- API Documentation
- Database Schema
- Setup Guides (SETUP_GUIDE.md, DOCKER_SETUP_GUIDE.md)

🔗 **Human Resources:**
- Development team available for bug fixes
- Product Owner available for clarifications
- Testers trained và available

---

### 3.7 Test Scope Summary (Tóm tắt phạm vi kiểm thử)

#### **In Scope (Trong phạm vi):**

✅ **Functional Testing** của tất cả core modules:
- Authentication & User Management
- Product Catalog & Variants
- Shopping Cart
- Order Management (COD & VNPay)
- Payment Processing (VNPay Sandbox)
- Inventory Management (Stock deduction/restoration)
- Supplier & GRN Management
- Reviews & Ratings

✅ **Integration Testing:**
- Frontend ↔ Backend APIs
- Backend ↔ Database
- VNPay payment integration
- Cloudinary image integration

✅ **UI Testing:**
- Responsive design (Desktop, Tablet, Mobile)
- Form validation
- Navigation
- Error messages

✅ **Database Testing:**
- CRUD operations
- Constraints (foreign keys, unique, not null)
- Transaction management
- Data accuracy

✅ **End-to-End Testing:**
- Complete user journeys (browse → cart → checkout → payment)
- Critical business workflows

✅ **Compatibility Testing:**
- Chrome, Firefox, Edge (latest versions)
- Windows, macOS, Linux (nếu có)

✅ **Basic Security Testing:**
- Authentication & Authorization
- Input validation (SQL injection, XSS prevention)
- Password hashing

---

#### **Out of Scope (Ngoài phạm vi):**

❌ Performance & Load Testing (concurrent users, throughput)  
❌ Advanced Security Testing (penetration testing, vulnerability scanning)  
❌ Mobile Native Apps  
❌ Localization (multi-language, currency conversion)  
❌ Third-party Service Internal Testing  
❌ Advanced Analytics & Reporting  
❌ Disaster Recovery & Backup  
❌ Accessibility (WCAG compliance)  
❌ Legacy Browser Support (IE11, old Chrome/Firefox)  
❌ Production Deployment Testing  

---

## 4. FEATURES TO BE TESTED (Chi tiết các tính năng được kiểm thử)

### 4.1 Module-wise Features (Các tính năng theo module)

Phần này liệt kê chi tiết các tính năng cần kiểm thử, bao gồm Acceptance Criteria và Priority.

---

#### **4.1.1 Authentication & User Management Module**

| **Feature ID** | **Feature Name** | **Acceptance Criteria** | **Priority** |
|----------------|------------------|-------------------------|--------------|
| **AUTH-01** | User Registration | - User có thể đăng ký với email, password, fullname<br>- Email phải unique<br>- Password ≥ 8 ký tự<br>- Gửi verification email (nếu có)<br>- User được tạo với role USER | CRITICAL |
| **AUTH-02** | User Login | - User login với email + password đúng → Success<br>- Sai credentials → Error message<br>- Trả về JWT token<br>- Redirect đến homepage | CRITICAL |
| **AUTH-03** | Admin Login | - Admin login với admin credentials<br>- Trả về JWT token với role ADMIN<br>- Redirect đến admin dashboard<br>- User thường không thể access admin pages | CRITICAL |
| **AUTH-04** | Logout | - Clear JWT token<br>- Redirect đến login page<br>- Không thể access protected pages sau logout | MEDIUM |
| **AUTH-05** | Update Profile | - User có thể update fullname, phone, avatar<br>- Email không được thay đổi<br>- Changes được lưu vào database | MEDIUM |
| **AUTH-06** | Change Password | - Phải nhập old password đúng<br>- New password ≥ 8 ký tự<br>- Password được hash và lưu<br>- Hiển thị success message | MEDIUM |
| **AUTH-07** | Manage Addresses | - User có thể add/edit/delete shipping addresses<br>- Tích hợp Vietnam Provinces API<br>- Set default address | MEDIUM |

---

#### **4.1.2 Product Catalog Module**

| **Feature ID** | **Feature Name** | **Acceptance Criteria** | **Priority** |
|----------------|------------------|-------------------------|--------------|
| **PROD-01** | View Product List | - Hiển thị tất cả products với image, name, price<br>- Pagination (20 items/page)<br>- Sort by: newest, price asc/desc<br>- Performance: Load < 2s | HIGH |
| **PROD-02** | View Product Detail | - Hiển thị full info: images, description, price, sizes, colors<br>- Show available stock<br>- Display reviews và rating<br>- Variant selector working | HIGH |
| **PROD-03** | Search Products | - Search by product name<br>- Partial match supported<br>- Hiển thị "No results" nếu không tìm thấy | HIGH |
| **PROD-04** | Filter Products | - Filter by: Brand, Category, Color, Size, Price range<br>- Multiple filters combine với AND logic<br>- Reset filters button | HIGH |
| **PROD-05** | Create Product (Admin) | - Admin có thể tạo product với all required fields<br>- Upload multiple images (Cloudinary)<br>- Create variants (size + color combinations)<br>- Set initial stock per variant | HIGH |
| **PROD-06** | Update Product (Admin) | - Admin có thể update product info<br>- Update images<br>- Update variants và stock<br>- Changes reflected immediately | HIGH |
| **PROD-07** | Delete Product (Admin) | - Admin có thể delete product<br>- Cascade delete variants<br>- Cannot delete if có orders liên quan | MEDIUM |

---

#### **4.1.3 Shopping Cart Module**

| **Feature ID** | **Feature Name** | **Acceptance Criteria** | **Priority** |
|----------------|------------------|-------------------------|--------------|
| **CART-01** | Add to Cart | - User chọn variant (size + color) và quantity<br>- Không vượt quá available stock<br>- Cart icon update số lượng<br>- Item added to cart (database) | CRITICAL |
| **CART-02** | View Cart | - Hiển thị all items với image, name, variant, price, quantity<br>- Calculate subtotal per item<br>- Calculate total<br>- Show shipping fee (if any) | HIGH |
| **CART-03** | Update Cart Quantity | - User có thể tăng/giảm quantity<br>- Min = 1, Max = stock available<br>- Total update real-time<br>- Changes saved to database | CRITICAL |
| **CART-04** | Remove Cart Item | - User có thể xóa item khỏi cart<br>- Confirm dialog hiển thị<br>- Total recalculate<br>- Item removed from database | HIGH |
| **CART-05** | Clear Cart | - User có thể clear all items<br>- Confirm dialog hiển thị<br>- All items removed from database | MEDIUM |
| **CART-06** | Cart Persistence | - Cart được lưu khi user login/logout<br>- Cart sync giữa devices (cùng account)<br>- Guest cart converted khi login | HIGH |
| **CART-07** | Stock Validation | - Real-time check stock availability<br>- Disable quantity increase nếu exceed stock<br>- Show "Out of stock" message | CRITICAL |

---

#### **4.1.4 Order Management Module**

| **Feature ID** | **Feature Name** | **Acceptance Criteria** | **Priority** |
|----------------|------------------|-------------------------|--------------|
| **ORD-01** | Create Order (COD) | - User fill shipping info<br>- Select COD payment<br>- Order created với status PENDING<br>- Cart cleared<br>- Email confirmation sent | CRITICAL |
| **ORD-02** | Create Order (VNPay) | - User select VNPay payment<br>- Redirect to VNPay Sandbox<br>- Order created với status PENDING<br>- Payment record created | CRITICAL |
| **ORD-03** | View Order List | - User xem tất cả orders của mình<br>- Hiển thị: Order ID, Date, Total, Status<br>- Sort by date desc<br>- Filter by status | HIGH |
| **ORD-04** | View Order Detail | - Hiển thị full order info: items, quantities, prices, shipping, payment method<br>- Show order status history<br>- Show payment status<br>- Retry payment button (nếu VNPay PENDING) | HIGH |
| **ORD-05** | Cancel Order (User) | - User có thể cancel nếu status = PENDING hoặc CONFIRMED<br>- Confirm dialog hiển thị<br>- Status → CANCELLED<br>- Stock restored | HIGH |
| **ORD-06** | Update Order Status (Admin) | - Admin có thể update: PENDING → CONFIRMED → SHIPPING → DELIVERED<br>- Stock deducted khi CONFIRMED<br>- Email notification sent | HIGH |
| **ORD-07** | Auto-cancel Order | - PaymentExpirationScheduler chạy mỗi 5 phút<br>- Orders VNPay PENDING > 15 phút → CANCELLED<br>- Stock restored<br>- Payment status = FAILED | CRITICAL |
| **ORD-08** | Order Status Tracking | - User theo dõi real-time status<br>- Status labels clear: "Chờ thanh toán", "Chờ xác nhận", "Đang giao", "Hoàn thành" | HIGH |

---

#### **4.1.5 Payment Module**

| **Feature ID** | **Feature Name** | **Acceptance Criteria** | **Priority** |
|----------------|------------------|-------------------------|--------------|
| **PAY-01** | VNPay Payment URL | - Generate correct VNPay URL với all required params<br>- HMAC SHA512 signature correct<br>- Return URL = http://localhost:5173/payment/vnpay-return<br>- Redirect to VNPay Sandbox | CRITICAL |
| **PAY-02** | VNPay Return Handling | - Parse VNPay response params<br>- Verify signature<br>- Update order status: SUCCESS → CONFIRMED, FAILED → PENDING<br>- Update payment status<br>- Redirect to result page | CRITICAL |
| **PAY-03** | VNPay IPN | - Receive IPN from VNPay<br>- Verify signature<br>- Update payment status<br>- Return RspCode=00 | HIGH |
| **PAY-04** | Retry Payment | - User có thể retry payment cho VNPay PENDING orders<br>- Generate new payment URL<br>- Redirect to VNPay<br>- Maintain same order | HIGH |
| **PAY-05** | Payment Timeout | - Orders không thanh toán trong 15 phút → Auto-cancel<br>- Payment status = FAILED<br>- Stock restored | CRITICAL |
| **PAY-06** | Payment Transaction Log | - All payment transactions logged<br>- Store: vnpayTransactionId, amount, status, timestamps | MEDIUM |

---

#### **4.1.6 Inventory Management Module**

| **Feature ID** | **Feature Name** | **Acceptance Criteria** | **Priority** |
|----------------|------------------|-------------------------|--------------|
| **INV-01** | Stock Deduction (Order Confirmed) | - Khi order status → CONFIRMED<br>- Stock của từng variant giảm theo quantity ordered<br>- Transaction committed (ACID) | CRITICAL |
| **INV-02** | Stock Restoration (Manual Cancel) | - Khi user/admin cancel order<br>- Stock của từng variant tăng lại<br>- Transaction rollback nếu có lỗi | CRITICAL |
| **INV-03** | Stock Restoration (Auto Cancel) | - PaymentExpirationScheduler detect timeout orders<br>- Stock restored for all order variants<br>- LEFT JOIN FETCH to avoid lazy load exception | CRITICAL |
| **INV-04** | Stock Update via GRN | - Khi admin tạo GRN<br>- Stock của variants tăng theo quantity nhập<br>- Transaction committed | CRITICAL |
| **INV-05** | View Stock Levels | - Admin xem stock của tất cả variants<br>- Filter by product, size, color<br>- Low stock highlighted | HIGH |
| **INV-06** | Out of Stock Handling | - Product variant với stock = 0 → Disable "Add to Cart"<br>- Show "Hết hàng" badge<br>- Hide from filter results (optional) | HIGH |
| **INV-07** | Low Stock Alert | - Admin nhận alert khi stock < threshold (e.g., 10)<br>- Hiển thị trong dashboard | MEDIUM |

---

#### **4.1.7 Supplier & GRN Module**

| **Feature ID** | **Feature Name** | **Acceptance Criteria** | **Priority** |
|----------------|------------------|-------------------------|--------------|
| **SUP-01** | Create Supplier | - Admin nhập: name, contact, email, phone, address<br>- Name phải unique<br>- Supplier created in database | MEDIUM |
| **SUP-02** | Update Supplier | - Admin update supplier info<br>- Changes saved | MEDIUM |
| **SUP-03** | Delete Supplier | - Admin delete supplier<br>- Cannot delete nếu có GRN liên quan<br>- Cascade options | MEDIUM |
| **GRN-01** | Create GRN | - Admin chọn supplier, date, variants + quantities<br>- GRN created<br>- Stock auto-updated<br>- Transaction committed | HIGH |
| **GRN-02** | View GRN List | - Admin xem all GRNs<br>- Filter by supplier, date range<br>- Sort by date desc | MEDIUM |
| **GRN-03** | View GRN Detail | - Hiển thị: GRN ID, Supplier, Date, Items, Quantities, Total value | MEDIUM |

---

#### **4.1.8 Category, Brand, Color, Size Module**

| **Feature ID** | **Feature Name** | **Acceptance Criteria** | **Priority** |
|----------------|------------------|-------------------------|--------------|
| **CAT-01** | CRUD Category | - Admin create/read/update/delete categories<br>- Name unique<br>- Cannot delete nếu có products | MEDIUM |
| **BRD-01** | CRUD Brand | - Admin CRUD brands<br>- Name unique<br>- Cannot delete nếu có products | MEDIUM |
| **CLR-01** | CRUD Color | - Admin CRUD colors<br>- Name unique<br>- Store hex code | MEDIUM |
| **SIZ-01** | CRUD Size | - Admin CRUD sizes<br>- Name unique (S, M, L, XL, XXL) | MEDIUM |

---

#### **4.1.9 Review & Rating Module**

| **Feature ID** | **Feature Name** | **Acceptance Criteria** | **Priority** |
|----------------|------------------|-------------------------|--------------|
| **REV-01** | Add Review | - User phải đã mua product mới được review<br>- Rating: 1-5 stars<br>- Comment: 10-500 characters<br>- Review created | MEDIUM |
| **REV-02** | Edit Review | - User chỉ edit được review của mình<br>- Update rating và comment | LOW |
| **REV-03** | Delete Review | - User/Admin có thể delete review<br>- Confirm dialog | LOW |
| **REV-04** | View Reviews | - Hiển thị all reviews cho product<br>- Sort by: newest, highest rating<br>- Pagination | MEDIUM |
| **REV-05** | Calculate Rating | - Average rating tự động calculate<br>- Hiển thị số stars và (số reviews)<br>- Update khi có review mới | MEDIUM |

---

## 5. FEATURES NOT TO BE TESTED (Các tính năng không được kiểm thử)

Các tính năng sau **KHÔNG** nằm trong phạm vi kiểm thử của đồ án môn học này.

**Lưu ý:** Chi tiết đầy đủ về các features không được test đã được liệt kê tại **Section 3.3** (trong REQUIREMENTS FOR TEST). Phần này chỉ là tóm tắt.

### 5.1 Performance & Load Testing
- ❌ Load Testing (kiểm thử với nhiều users đồng thời)
- ❌ Stress Testing (kiểm thử giới hạn hệ thống)
- ❌ Volume Testing (kiểm thử với large data sets)
- ❌ Spike Testing (kiểm thử với traffic tăng đột ngột)
- ❌ Endurance Testing (kiểm thử trong thời gian dài)

**Lý do:** Đồ án môn học không yêu cầu performance testing chuyên sâu, hệ thống chỉ test với single user/tester.

---

### 4.2 Advanced Security Testing
- ❌ Penetration Testing (tấn công thử nghiệm)
- ❌ Vulnerability Scanning (quét lỗ hổng bảo mật)
- ❌ Security Audit
- ❌ OWASP Top 10 comprehensive testing

**Lý do:** Chỉ thực hiện basic security checks (authentication, authorization, input validation). Advanced security testing cần chuyên gia bảo mật.

---

### 4.3 Mobile Applications
- ❌ Native Mobile App Testing (iOS, Android)
- ❌ Mobile-specific features (Push notifications, GPS, Camera)

**Lý do:** UniClub là web application, không có native mobile app. Chỉ test responsive web design.

---

### 4.4 Localization & Internationalization
- ❌ Multi-language support testing
- ❌ Currency conversion
- ❌ Time zone handling
- ❌ Right-to-left (RTL) languages

**Lý do:** Hệ thống chỉ hỗ trợ tiếng Việt và VNĐ currency.

---

### 4.5 Third-party Service Internal Testing
- ❌ Testing VNPay internal logic
- ❌ Testing SendGrid email delivery infrastructure
- ❌ Testing Cloudinary storage reliability
- ❌ Testing Vietnam Provinces API accuracy

**Lý do:** Đây là third-party services, chúng ta chỉ test integration points (API calls, responses), không test internal workings của services này.

---

### 4.6 Disaster Recovery & Backup
- ❌ Backup and Restore Testing
- ❌ Disaster Recovery Planning
- ❌ Failover Testing
- ❌ Data Loss Prevention

**Lý do:** Ngoài phạm vi đồ án học thuật.

---

### 4.7 Advanced Analytics & Reporting
- ❌ Business Intelligence Reports
- ❌ Advanced Analytics Dashboard
- ❌ Data Mining
- ❌ Predictive Analytics

**Lý do:** Hệ thống không có yêu cầu về analytics chuyên sâu.

---

### 4.8 Accessibility Testing (WCAG Compliance)
- ❌ Screen reader compatibility
- ❌ Keyboard navigation
- ❌ Color contrast ratios
- ❌ ARIA labels

**Lý do:** Không có yêu cầu accessibility trong đồ án hiện tại. Có thể add trong future versions.

---

### 4.9 Legacy Browser Support
- ❌ Internet Explorer 11
- ❌ Old browser versions (Chrome < 90, Firefox < 88)

**Lý do:** Chỉ test trên latest versions của major browsers (Chrome, Firefox, Edge).

---

### 4.10 Concurrency & Race Conditions (Advanced)
- ❌ Advanced concurrency testing (thousands of concurrent transactions)
- ❌ Deadlock detection
- ❌ Race condition stress testing

**Lý do:** Basic concurrency (e.g., 2 users order last item) sẽ được test, nhưng không test advanced scenarios.

---



### 5.1 Test Strategy Overview (Tổng quan chiến lược kiểm thử)

Chiến lược kiểm thử cho UniClub được thiết kế theo mô hình **Risk-Based Testing** kết hợp với **Agile Testing Approach**, ưu tiên kiểm thử các chức năng có rủi ro cao và tác động lớn đến người dùng.

#### **5.1.1 Test Levels (Các cấp độ kiểm thử)**

1. **Unit Testing** (Dev thực hiện)
   - Test các method/function riêng lẻ
   - Coverage: Service layer, Utility classes

2. **Integration Testing** (QA + Dev)
   - Test tích hợp giữa Backend APIs và Database
   - Test tích hợp giữa Frontend và Backend APIs
   - Test tích hợp với Third-party services (VNPay, SendGrid, Cloudinary)

3. **System Testing** (QA)
   - Test toàn bộ hệ thống end-to-end
   - Verify business flows hoàn chỉnh

4. **User Acceptance Testing (UAT)** (Business Owner + End Users)
   - Verify hệ thống đáp ứng yêu cầu nghiệp vụ
   - Test trên môi trường gần giống production

#### **5.1.2 Test Types (Các loại kiểm thử)**

| **Test Type** | **Mô tả** | **Công cụ** | **Người thực hiện** |
|---------------|-----------|-------------|---------------------|
| **Functional Testing** | Kiểm tra các chức năng hoạt động đúng theo đặc tả | Manual + Postman | QA |
| **API Testing** | Kiểm tra REST APIs (Request/Response, Status codes, Data validation) | Postman | QA + Dev |
| **UI Testing** | Kiểm tra giao diện người dùng, responsive design | Manual + Browser DevTools | QA |
| **Database Testing** | Kiểm tra CRUD operations, Data integrity, Constraints | DBeaver + SQL | QA |
| **Integration Testing** | Kiểm tra tích hợp giữa các components | Postman + Manual | QA + Dev |
| **End-to-End Testing** | Kiểm tra complete user journeys | Manual | QA |
| **Regression Testing** | Kiểm tra lại sau khi fix bug hoặc thêm feature mới | Manual (rerun test cases) | QA |
| **Compatibility Testing** | Kiểm tra trên nhiều trình duyệt/thiết bị | Manual | QA |
| **Smoke Testing** | Kiểm tra nhanh các chức năng critical sau mỗi build | Manual | QA |
| **Exploratory Testing** | Tự do khám phá hệ thống để tìm bug | Manual | QA |

---

### 5.2 Feature-to-Strategy Mapping (Bảng ánh xạ Chức năng - Chiến lược kiểm thử)

Bảng dưới đây mô tả chi tiết **từng chức năng** sẽ được kiểm thử bằng **các phương pháp nào**, giúp team có cái nhìn rõ ràng về scope và effort cần thiết.

#### **Ký hiệu:**
- ✅ = **Bắt buộc** (Must have)
- 🔸 = **Nên có** (Should have)
- ⚪ = **Không áp dụng** (N/A)

---

#### **TABLE 1: AUTHENTICATION & USER MANAGEMENT**

| **#** | **Chức năng** | **Functional Testing** | **API Testing** | **UI Testing** | **Database Testing** | **Integration Testing** | **E2E Testing** | **Regression Testing** | **Compatibility Testing** | **Priority** |
|-------|---------------|:----------------------:|:---------------:|:--------------:|:--------------------:|:-----------------------:|:---------------:|:----------------------:|:-------------------------:|:------------:|
| 1.1 | User Registration | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | **HIGH** |
| 1.2 | User Login | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | **CRITICAL** |
| 1.3 | User Logout | ✅ | ✅ | ✅ | 🔸 | 🔸 | 🔸 | 🔸 | 🔸 | **MEDIUM** |
| 1.4 | Forgot Password | ✅ | ✅ | ✅ | ✅ | ✅ (SendGrid) | ✅ | 🔸 | 🔸 | **MEDIUM** |
| 1.5 | Update Profile | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | ✅ | 🔸 | **MEDIUM** |
| 1.6 | Change Password | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | ✅ | 🔸 | **MEDIUM** |
| 1.7 | Manage Addresses | ✅ | ✅ | ✅ | ✅ | ✅ (Provinces API) | 🔸 | ✅ | 🔸 | **MEDIUM** |
| 1.8 | Admin Login | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | **CRITICAL** |
| 1.9 | Role-based Access Control | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | **HIGH** |

**Test Notes:**
- Login/Registration phải test với nhiều edge cases (invalid email, weak password, SQL injection attempts)
- Integration testing với SendGrid (email verification) và Vietnam Provinces API (địa chỉ)
- Compatibility testing ưu tiên cho Login vì đây là entry point

---

#### **TABLE 2: PRODUCT CATALOG MANAGEMENT**

| **#** | **Chức năng** | **Functional Testing** | **API Testing** | **UI Testing** | **Database Testing** | **Integration Testing** | **E2E Testing** | **Regression Testing** | **Compatibility Testing** | **Priority** |
|-------|---------------|:----------------------:|:---------------:|:--------------:|:--------------------:|:-----------------------:|:---------------:|:----------------------:|:-------------------------:|:------------:|
| 2.1 | View Product List (User) | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | ✅ | **HIGH** |
| 2.2 | View Product Detail | ✅ | ✅ | ✅ | ✅ | ✅ (Cloudinary) | ✅ | 🔸 | ✅ | **HIGH** |
| 2.3 | Search Products | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | **HIGH** |
| 2.4 | Filter Products (Brand, Category, Color, Size, Price) | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | **HIGH** |
| 2.5 | Sort Products (Price, Name, Newest) | ✅ | ✅ | ✅ | ✅ | 🔸 | 🔸 | 🔸 | 🔸 | **MEDIUM** |
| 2.6 | Create Product (Admin) | ✅ | ✅ | ✅ | ✅ | ✅ (Cloudinary) | ✅ | ✅ | 🔸 | **HIGH** |
| 2.7 | Update Product (Admin) | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | ✅ | 🔸 | **HIGH** |
| 2.8 | Delete Product (Admin) | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | ✅ | 🔸 | **MEDIUM** |
| 2.9 | Manage Product Variants (Size + Color) | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | **CRITICAL** |
| 2.10 | Upload Product Images | ✅ | ✅ | ✅ | 🔸 | ✅ (Cloudinary) | 🔸 | ✅ | 🔸 | **HIGH** |

**Test Notes:**
- Filter/Search phải test performance với large dataset
- Variant management rất quan trọng vì liên quan trực tiếp đến inventory
- Cloudinary integration phải test với nhiều format ảnh (jpg, png, webp) và file size

---

#### **TABLE 3: SHOPPING CART**

| **#** | **Chức năng** | **Functional Testing** | **API Testing** | **UI Testing** | **Database Testing** | **Integration Testing** | **E2E Testing** | **Regression Testing** | **Compatibility Testing** | **Priority** |
|-------|---------------|:----------------------:|:---------------:|:--------------:|:--------------------:|:-----------------------:|:---------------:|:----------------------:|:-------------------------:|:------------:|
| 3.1 | Add Product to Cart | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | **CRITICAL** |
| 3.2 | Update Cart Item Quantity | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | **CRITICAL** |
| 3.3 | Remove Cart Item | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | 🔸 | **HIGH** |
| 3.4 | View Cart | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | ✅ | **HIGH** |
| 3.5 | Calculate Cart Total | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | **CRITICAL** |
| 3.6 | Clear Cart | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | 🔸 | 🔸 | **MEDIUM** |
| 3.7 | Cart Persistence (Login/Logout) | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | **HIGH** |
| 3.8 | Stock Validation in Cart | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | **CRITICAL** |

**Test Notes:**
- Cart là critical module vì ảnh hưởng trực tiếp đến conversion rate
- Phải test cart sync giữa client và server khi network unstable
- Stock validation phải real-time để tránh overselling

---

#### **TABLE 4: ORDER MANAGEMENT**

| **#** | **Chức năng** | **Functional Testing** | **API Testing** | **UI Testing** | **Database Testing** | **Integration Testing** | **E2E Testing** | **Regression Testing** | **Compatibility Testing** | **Priority** |
|-------|---------------|:----------------------:|:---------------:|:--------------:|:--------------------:|:-----------------------:|:---------------:|:----------------------:|:-------------------------:|:------------:|
| 4.1 | Create Order (COD) | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | **CRITICAL** |
| 4.2 | Create Order (VNPay) | ✅ | ✅ | ✅ | ✅ | ✅ (VNPay) | ✅ | ✅ | 🔸 | **CRITICAL** |
| 4.3 | View Order List (User) | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | ✅ | **HIGH** |
| 4.4 | View Order Detail | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | ✅ | **HIGH** |
| 4.5 | Cancel Order (User) | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | **HIGH** |
| 4.6 | Update Order Status (Admin) | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | **HIGH** |
| 4.7 | Order Status Tracking | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | ✅ | **HIGH** |
| 4.8 | Order History | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | 🔸 | 🔸 | **MEDIUM** |
| 4.9 | Retry Payment (VNPay PENDING) | ✅ | ✅ | ✅ | ✅ | ✅ (VNPay) | ✅ | ✅ | 🔸 | **HIGH** |
| 4.10 | Auto-cancel Order (15min timeout) | ✅ | ✅ | ⚪ | ✅ | ✅ | ✅ | ✅ | ⚪ | **CRITICAL** |

**Test Notes:**
- VNPay integration phải test cả success, failed, timeout scenarios
- Auto-cancel order phải verify scheduler hoạt động đúng (PaymentExpirationScheduler)
- Order status flow: PENDING → CONFIRMED → SHIPPING → DELIVERED → CANCELLED

---

#### **TABLE 5: PAYMENT & TRANSACTION**

| **#** | **Chức năng** | **Functional Testing** | **API Testing** | **UI Testing** | **Database Testing** | **Integration Testing** | **E2E Testing** | **Regression Testing** | **Compatibility Testing** | **Priority** |
|-------|---------------|:----------------------:|:---------------:|:--------------:|:--------------------:|:-----------------------:|:---------------:|:----------------------:|:-------------------------:|:------------:|
| 5.1 | Select Payment Method (COD/VNPay) | ✅ | ✅ | ✅ | 🔸 | ✅ | ✅ | 🔸 | ✅ | **HIGH** |
| 5.2 | Create VNPay Payment URL | ✅ | ✅ | ⚪ | ✅ | ✅ (VNPay) | ✅ | ✅ | ⚪ | **CRITICAL** |
| 5.3 | VNPay Return URL Handling | ✅ | ✅ | ✅ | ✅ | ✅ (VNPay) | ✅ | ✅ | 🔸 | **CRITICAL** |
| 5.4 | VNPay IPN (Instant Payment Notification) | ✅ | ✅ | ⚪ | ✅ | ✅ (VNPay) | ✅ | ✅ | ⚪ | **HIGH** |
| 5.5 | Payment Status Update | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | **CRITICAL** |
| 5.6 | Payment Transaction Logging | ✅ | ✅ | ⚪ | ✅ | ✅ | 🔸 | 🔸 | ⚪ | **MEDIUM** |
| 5.7 | Payment Failed Handling | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | **HIGH** |
| 5.8 | Payment Timeout Handling (15min) | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | **CRITICAL** |

**Test Notes:**
- VNPay phải test trên Sandbox với các test cards khác nhau
- Return URL phải verify redirect về đúng port (5173 cho User Web)
- Payment timeout phải test scheduler (runs every 5 minutes)
- Security: Verify HMAC SHA512 signature

---

#### **TABLE 6: INVENTORY MANAGEMENT**

| **#** | **Chức năng** | **Functional Testing** | **API Testing** | **UI Testing** | **Database Testing** | **Integration Testing** | **E2E Testing** | **Regression Testing** | **Compatibility Testing** | **Priority** |
|-------|---------------|:----------------------:|:---------------:|:--------------:|:--------------------:|:-----------------------:|:---------------:|:----------------------:|:-------------------------:|:------------:|
| 6.1 | View Stock Levels | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | 🔸 | 🔸 | **HIGH** |
| 6.2 | Stock Deduction (Order CONFIRMED) | ✅ | ✅ | ⚪ | ✅ | ✅ | ✅ | ✅ | ⚪ | **CRITICAL** |
| 6.3 | Stock Restoration (Order CANCELLED - Manual) | ✅ | ✅ | ⚪ | ✅ | ✅ | ✅ | ✅ | ⚪ | **CRITICAL** |
| 6.4 | Stock Restoration (Order CANCELLED - Auto) | ✅ | ✅ | ⚪ | ✅ | ✅ | ✅ | ✅ | ⚪ | **CRITICAL** |
| 6.5 | Low Stock Alert | ✅ | ✅ | ✅ | ✅ | 🔸 | 🔸 | 🔸 | 🔸 | **MEDIUM** |
| 6.6 | Out of Stock Handling | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | **HIGH** |
| 6.7 | Stock Update via GRN | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | **CRITICAL** |
| 6.8 | Stock History Tracking | ✅ | ✅ | ✅ | ✅ | 🔸 | 🔸 | 🔸 | 🔸 | **MEDIUM** |

**Test Notes:**
- Inventory là critical business logic - phải test kỹ transaction và rollback
- Phải verify @Transactional annotation hoạt động đúng
- Test lazy loading issue (LEFT JOIN FETCH cho orderVariants)
- Test concurrent orders với cùng variant để verify race condition

---

#### **TABLE 7: SUPPLIER & GRN MANAGEMENT**

| **#** | **Chức năng** | **Functional Testing** | **API Testing** | **UI Testing** | **Database Testing** | **Integration Testing** | **E2E Testing** | **Regression Testing** | **Compatibility Testing** | **Priority** |
|-------|---------------|:----------------------:|:---------------:|:--------------:|:--------------------:|:-----------------------:|:---------------:|:----------------------:|:-------------------------:|:------------:|
| 7.1 | Create Supplier (Admin) | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | ✅ | 🔸 | **MEDIUM** |
| 7.2 | Update Supplier (Admin) | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | ✅ | 🔸 | **MEDIUM** |
| 7.3 | Delete Supplier (Admin) | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | ✅ | 🔸 | **MEDIUM** |
| 7.4 | View Supplier List | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | 🔸 | 🔸 | **LOW** |
| 7.5 | Create GRN (Goods Receipt Note) | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | **HIGH** |
| 7.6 | View GRN List | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | 🔸 | 🔸 | **MEDIUM** |
| 7.7 | View GRN Detail | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | 🔸 | 🔸 | **MEDIUM** |
| 7.8 | GRN Auto-update Inventory | ✅ | ✅ | ⚪ | ✅ | ✅ | ✅ | ✅ | ⚪ | **CRITICAL** |

**Test Notes:**
- GRN tự động cập nhật tồn kho phải test kỹ
- Verify relationship giữa Supplier - GRN - Variant
- Test transaction consistency khi tạo GRN với nhiều variants

---

#### **TABLE 8: CATEGORY, BRAND, COLOR, SIZE MANAGEMENT**

| **#** | **Chức năng** | **Functional Testing** | **API Testing** | **UI Testing** | **Database Testing** | **Integration Testing** | **E2E Testing** | **Regression Testing** | **Compatibility Testing** | **Priority** |
|-------|---------------|:----------------------:|:---------------:|:--------------:|:--------------------:|:-----------------------:|:---------------:|:----------------------:|:-------------------------:|:------------:|
| 8.1 | CRUD Category (Admin) | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | ✅ | 🔸 | **MEDIUM** |
| 8.2 | CRUD Brand (Admin) | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | ✅ | 🔸 | **MEDIUM** |
| 8.3 | CRUD Color (Admin) | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | ✅ | 🔸 | **MEDIUM** |
| 8.4 | CRUD Size (Admin) | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | ✅ | 🔸 | **MEDIUM** |
| 8.5 | Validate Uniqueness | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | 🔸 | 🔸 | **MEDIUM** |
| 8.6 | Prevent Delete if in Use | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | ✅ | 🔸 | **HIGH** |

**Test Notes:**
- Các master data này quan trọng nhưng không critical như Cart/Order
- Phải test foreign key constraints (không cho xóa nếu còn sản phẩm đang dùng)
- Test cascade delete nếu có

---

#### **TABLE 9: REVIEW & RATING**

| **#** | **Chức năng** | **Functional Testing** | **API Testing** | **UI Testing** | **Database Testing** | **Integration Testing** | **E2E Testing** | **Regression Testing** | **Compatibility Testing** | **Priority** |
|-------|---------------|:----------------------:|:---------------:|:--------------:|:--------------------:|:-----------------------:|:---------------:|:----------------------:|:-------------------------:|:------------:|
| 9.1 | Add Product Review | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | 🔸 | **MEDIUM** |
| 9.2 | Edit Product Review | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | 🔸 | 🔸 | **LOW** |
| 9.3 | Delete Product Review | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | 🔸 | 🔸 | **LOW** |
| 9.4 | View Product Reviews | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | ✅ | **MEDIUM** |
| 9.5 | Calculate Average Rating | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | 🔸 | 🔸 | **MEDIUM** |
| 9.6 | Verify Purchase Before Review | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | 🔸 | **HIGH** |

**Test Notes:**
- Phải verify user đã mua sản phẩm mới được review
- Test rating calculation với nhiều reviews
- Test review pagination nếu có nhiều reviews

---

### 5.3 Test Priority Matrix (Ma trận ưu tiên kiểm thử)

Dựa trên **Risk** và **Business Impact**, các chức năng được ưu tiên như sau:

| **Priority Level** | **Modules/Features** | **Test Coverage Target** | **Test Effort** |
|-------------------|----------------------|--------------------------|-----------------|
| **CRITICAL** | - User Login<br>- Admin Login<br>- Add to Cart<br>- Update Cart Quantity<br>- Cart Total Calculation<br>- Stock Validation<br>- Create Order (COD)<br>- Create Order (VNPay)<br>- VNPay Payment URL<br>- VNPay Return Handling<br>- Payment Status Update<br>- Payment Timeout<br>- Stock Deduction/Restoration<br>- Auto-cancel Order<br>- GRN Auto-update Inventory<br>- Product Variants | **100%** | **60%** |
| **HIGH** | - User Registration<br>- Role-based Access<br>- View Product List<br>- Product Detail<br>- Search/Filter Products<br>- Create/Update Product<br>- Upload Images<br>- Remove Cart Item<br>- View Cart<br>- Cancel Order<br>- Update Order Status<br>- Retry Payment<br>- Out of Stock Handling<br>- Create GRN<br>- Prevent Delete if in Use | **90%** | **25%** |
| **MEDIUM** | - Logout<br>- Forgot Password<br>- Update Profile<br>- Change Password<br>- Manage Addresses<br>- Sort Products<br>- Delete Product<br>- Clear Cart<br>- Order History<br>- Low Stock Alert<br>- Supplier Management<br>- Category/Brand/Color/Size CRUD<br>- Add/View Review | **70%** | **10%** |
| **LOW** | - Stock History<br>- View Supplier List<br>- View GRN List/Detail<br>- Edit/Delete Review | **50%** | **5%** |

**Test Coverage Target**: Tỷ lệ test case cover các scenarios (happy path + negative cases)  
**Test Effort**: Phân bổ thời gian/effort cho mỗi nhóm

---

### 5.4 Test Execution Strategy (Chiến lược thực thi kiểm thử)

#### **Phase 1: Smoke Testing (Week 1)**
- Test các critical flows: Login → Browse → Add to Cart → Checkout → Payment
- Verify hệ thống có thể start up và các chức năng cơ bản hoạt động
- **Pass Criteria**: Tất cả critical flows pass

#### **Phase 2: Functional Testing (Week 2-3)**
- Test toàn bộ chức năng theo bảng mapping ở trên
- Priority: CRITICAL → HIGH → MEDIUM → LOW
- **Pass Criteria**: ≥95% test cases PASS cho CRITICAL, ≥90% cho HIGH

#### **Phase 3: Integration Testing (Week 3-4)**
- Test tích hợp với VNPay, SendGrid, Cloudinary, Vietnam Provinces API
- Test scheduler (PaymentExpirationScheduler)
- Test database transactions và rollback
- **Pass Criteria**: Tất cả integration points stable

#### **Phase 4: End-to-End Testing (Week 4)**
- Test complete user journeys:
  - Guest → Register → Login → Browse → Add to Cart → Checkout COD → Track Order
  - User → Browse → Add to Cart → Checkout VNPay → Complete Payment → Track Order
  - Admin → Create Product → Create GRN → Update Inventory → Process Order
- **Pass Criteria**: Tất cả E2E scenarios pass without manual intervention

#### **Phase 5: Regression Testing (Week 5)**
- Re-test sau khi fix bugs
- Re-test các chức năng liên quan sau khi thêm feature mới
- **Pass Criteria**: Không có regression defects

#### **Phase 6: UAT (Week 6)**
- Business Owner và End Users test
- Verify business requirements
- **Pass Criteria**: Sign-off từ stakeholders

---

### 5.5 Defect Management Strategy (Chiến lược quản lý lỗi)

#### **Severity Levels:**
- **Critical**: Hệ thống crash, mất dữ liệu, không thể checkout, payment failed
- **Major**: Chức năng quan trọng không hoạt động (cart sync, stock update)
- **Minor**: UI issues, incorrect error messages
- **Trivial**: Cosmetic issues, typos

#### **Priority Levels:**
- **P0**: Fix ngay lập tức (trong ngày)
- **P1**: Fix trong 1-2 ngày
- **P2**: Fix trong sprint hiện tại
- **P3**: Fix ở sprint sau hoặc backlog

#### **Bug Workflow:**
1. QA tìm thấy bug → Log vào bug tracking system (GitHub Issues)
2. Dev review và estimate effort
3. Fix bug → Commit với reference đến bug ID
4. QA verify fix → Close bug hoặc reopen nếu chưa fix đúng
5. Regression test để đảm bảo không có bug mới

---

## 6. ITEM PASS/FAIL CRITERIA (Tiêu chí đạt/không đạt)

_(Định nghĩa khi nào một test case/feature được coi là PASS hoặc FAIL - Sẽ được bổ sung trong phần tiếp theo)_

---

## 7. SUSPENSION CRITERIA AND RESUMPTION REQUIREMENTS (Tiêu chí tạm dừng và yêu cầu tiếp tục)

_(Khi nào cần tạm dừng kiểm thử và điều kiện để tiếp tục - Sẽ được bổ sung trong phần tiếp theo)_

---

## 8. TEST DELIVERABLES (Sản phẩm bàn giao)

_(Danh sách các tài liệu, báo cáo sẽ được tạo ra - Sẽ được bổ sung trong phần tiếp theo)_

---

## 9. TESTING TASKS (Các công việc kiểm thử)

_(Breakdown các task cụ thể, timeline - Sẽ được bổ sung trong phần tiếp theo)_

---

## 10. ENVIRONMENTAL NEEDS (Yêu cầu về môi trường)

_(Hardware, software, network requirements - Sẽ được bổ sung trong phần tiếp theo)_

---

## 11. RESPONSIBILITIES (Phân công trách nhiệm)

_(Roles and responsibilities của từng thành viên team - Sẽ được bổ sung trong phần tiếp theo)_

---

## 12. STAFFING AND TRAINING NEEDS (Nhu cầu nhân sự và đào tạo)

_(Đã cover ở mục 1.8 - Có thể mở rộng thêm nếu cần)_

---

## 13. SCHEDULE (Lịch trình)

_(Timeline chi tiết cho từng giai đoạn kiểm thử - Sẽ được bổ sung trong phần tiếp theo)_

---

## 14. RISKS AND CONTINGENCIES (Rủi ro và kế hoạch dự phòng)

_(Đã cover ở mục 1.7 - Có thể mở rộng thêm với mitigation plans chi tiết hơn)_

---

## 15. APPROVALS (Phê duyệt)

_(Chữ ký phê duyệt từ PM, QA Lead, Dev Lead - Sẽ được bổ sung khi hoàn thiện)_

---

**Document Version**: 1.0  
**Last Updated**: November 12, 2025  
**Status**: Draft - Section 1 Complete
