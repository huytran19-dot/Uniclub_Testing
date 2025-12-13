# 🧪 UniClub Selenium Test Automation

Automated testing framework for UniClub e-commerce platform using Selenium WebDriver, TestNG, and Page Object Model design pattern.

## 📋 Overview

This project contains automated tests for:
- **Admin Authentication** (Port 5174) - 10 test cases
- **User Authentication** (Port 5173) - 10 test cases
- **Total: 20 test cases**

### Technology Stack

- **Selenium WebDriver** 4.15.0 - Browser automation
- **TestNG** 7.8.0 - Test framework
- **Maven** - Build tool
- **WebDriverManager** 5.6.2 - Automatic driver management
- **Allure** 2.24.0 - Test reporting
- **Java** 17+ - Programming language
- **Page Object Model (POM)** - Design pattern

## 🧪 Test Cases

### **Admin Authentication (Port 5174)**

| Test ID | Test Case | Priority | Status |
|---------|-----------|----------|--------|
| AUTH_01 | Admin login with valid credentials | Critical | ✅ |
| AUTH_02 | Admin login with invalid password | Critical | ✅ |
| AUTH_03 | Admin login with invalid email | Critical | ✅ |
| AUTH_04 | Login with empty credentials | Normal | ✅ |
| AUTH_05 | Login with empty password | Normal | ✅ |
| AUTH_06 | Login with empty email | Normal | ✅ |
| AUTH_07 | Verify login page elements | Normal | ✅ |
| AUTH_08 | Admin logout successfully | Critical | ✅ |
| AUTH_09 | Verify session persistence | Normal | ✅ |
| AUTH_10 | SQL injection prevention | Critical | ✅ |

**Total: 10 test cases**

### **User Authentication (Port 5173)**

| Test ID | Test Case | Priority | Status |
|---------|-----------|----------|--------|
| USER_AUTH_01 | User login with valid credentials | Critical | ✅ |
| USER_AUTH_02 | User login with invalid password | Critical | ✅ |
| USER_AUTH_03 | User login with invalid email | Critical | ✅ |
| USER_AUTH_04 | Login with empty credentials | Normal | ✅ |
| USER_AUTH_05 | Login with empty password | Normal | ✅ |
| USER_AUTH_06 | Login with empty email | Normal | ✅ |
| USER_AUTH_07 | Verify login page elements | Normal | ✅ |
| USER_AUTH_08 | User logout successfully | Critical | ✅ |
| USER_AUTH_09 | Password visibility toggle | Normal | ✅ |
| USER_AUTH_10 | SQL injection prevention | Critical | ✅ |

**Total: 10 test cases**

### **📊 TỔNG KẾT: 20 test cases (Admin + User)**

## 📁 Project Structure

```
selenium-tests/
├── pom.xml                           # Maven configuration
├── testng.xml                        # TestNG suite configuration
├── config/
│   └── config.properties             # Test configuration
├── src/
│   ├── main/java/com/uniclub/
│   │   ├── base/
│   │   │   ├── BasePage.java         # Base class for Page Objects
│   │   │   └── BaseTest.java         # Base class for Tests
│   │   ├── pages/
│   │   │   ├── LoginPage.java        # Admin login page (port 5174)
│   │   │   ├── DashboardPage.java    # Admin dashboard
│   │   │   ├── UserLoginPage.java    # User login page (port 5173)
│   │   │   └── UserHomePage.java     # User home page
│   │   └── utils/
│   │       ├── ConfigReader.java     # Configuration reader
│   │       └── WaitUtils.java        # Wait utilities
│   └── test/java/com/uniclub/tests/
│       ├── LoginTest.java            # Admin authentication tests
│       └── UserLoginTest.java        # User authentication tests
├── README.md                         # This file
├── QUICK_START.md                    # Quick start guide
└── USER_LOGIN_TEST_GUIDE.md          # User test troubleshooting
```

## ⚙️ Setup Instructions

### 1. Start UniClub Application

**Backend:**
```powershell
cd d:\uniclub\uniclub-be
mvn spring-boot:run
```

**Admin Frontend (Port 5174):**
```powershell
cd d:\uniclub\uniclub-fe\admin
npm run dev
```

**Web Frontend (Port 5173):**
```powershell
cd d:\uniclub\uniclub-fe\web
npm run dev
```

### 2. Navigate to selenium-tests directory

```powershell
cd d:\uniclub\selenium-tests
```

### 3. Install dependencies (first time only)

```powershell
mvn clean install -DskipTests
```

### 4. Verify configuration

Edit `config/config.properties` if needed:

```properties
# URLs
admin.url=http://localhost:5174
web.url=http://localhost:5173
backend.url=http://localhost:8080

# Admin credentials
admin.email=admin@uniclub.vn
admin.password=admin123

# User credentials
user.email=user@uniclub.vn
user.password=user123

# Browser
browser=chrome
```

## 🚀 Running Tests

### Run ALL tests (Admin + User)

```powershell
mvn test
```

### Run Admin tests only

```powershell
mvn test -Dtest=LoginTest
```

### Run User tests only

```powershell
mvn test -Dtest=UserLoginTest
```

### Run specific test method

```powershell
# Admin login test
mvn test -Dtest=LoginTest#testLoginSuccess

# User login test
mvn test -Dtest=UserLoginTest#testUserLoginSuccess

# Admin logout test
mvn test -Dtest=LoginTest#testLogoutSuccess

# User logout test
mvn test -Dtest=UserLoginTest#testUserLogoutSuccess
```

### Run using TestNG XML

```powershell
mvn test -DsuiteXmlFile=testng.xml
```

## 📊 Viewing Test Reports

### Console Output

Test results will be displayed in the console with:
- ✅ Passed tests
- ❌ Failed tests
- Test execution time

### Allure Report

```powershell
# Generate Allure report
mvn allure:report

# Open Allure report in browser
mvn allure:serve
```

### TestNG Report

After running tests, open:
```
selenium-tests/target/surefire-reports/index.html
```

## 🐛 Troubleshooting

### Chrome version mismatch warnings

**Warning:**
```
DevTools protocol version (143.x) does not match Chrome version (143.y)
```

**Solution:** These warnings are non-critical and don't affect test execution. Tests run successfully despite these warnings.

### ERR_CONNECTION_REFUSED

**Error:**
```
unknown error: net::ERR_CONNECTION_REFUSED
```

**Cause:** Frontend service not running

**Solution:**
- For Admin tests: Start Admin Frontend on port 5174
- For User tests: Start Web Frontend on port 5173

See `USER_LOGIN_TEST_GUIDE.md` for detailed troubleshooting.

### Test timeout

If tests fail with TimeoutException, check:
1. Frontend is running and accessible in browser
2. Backend API is responding (port 8080)
3. Database is running (port 3307)
4. No network issues or firewalls

## 🎯 Test Execution Flow

### Admin Authentication Tests (LoginTest.java)

1. **Setup Phase**
   - WebDriver initialization (Chrome)
   - Navigate to http://localhost:5174
   - Wait for page load

2. **Test Execution**
   - Execute test cases (AUTH_01 to AUTH_10)
   - Take screenshots on failure

3. **Teardown Phase**
   - Close browser
   - Generate test report

### User Authentication Tests (UserLoginTest.java)

1. **Setup Phase**
   - WebDriver initialization (Chrome)
   - Navigate to http://localhost:5173
   - Wait for page load

2. **Test Execution**
   - Execute test cases (USER_AUTH_01 to USER_AUTH_10)
   - Take screenshots on failure

3. **Teardown Phase**
   - Close browser
   - Generate test report

## 📝 Test Credentials

### Admin Portal
- URL: http://localhost:5174
- Email: admin@uniclub.vn
- Password: admin123

### User Portal
- URL: http://localhost:5173
- Email: user@uniclub.vn
- Password: user123

## 🎨 Design Patterns

### Page Object Model (POM)

Each page is represented as a Java class with:
- **Locators:** Web element identifiers
- **Methods:** Actions that can be performed on the page

**Benefits:**
- Code reusability
- Easy maintenance
- Reduced duplication
- Better readability

### Example:

```java
public class LoginPage extends BasePage {
    // Locators
    private By emailInput = By.id("email");
    private By passwordInput = By.id("password");
    private By loginButton = By.cssSelector("button[type='submit']");
    
    // Methods
    public void login(String email, String password) {
        type(emailInput, email);
        type(passwordInput, password);
        click(loginButton);
    }
}
```

## 🔧 Configuration Management

### config.properties

All test configurations in one place:
```properties
# Application URLs
admin.url=http://localhost:5174
web.url=http://localhost:5173
backend.url=http://localhost:8080

# Test credentials
admin.email=admin@uniclub.vn
admin.password=admin123
user.email=user@uniclub.vn
user.password=user123

# Browser settings
browser=chrome
headless=false
implicit.wait=10
explicit.wait=20
```

### ConfigReader.java

Utility class to read configuration:
```java
String adminUrl = ConfigReader.getProperty("admin.url");
String adminEmail = ConfigReader.getProperty("admin.email");
```

## ✅ Test Results Summary

### Latest Test Run: Admin Tests

```
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

**All 10 admin authentication tests PASSED! ✅**

### Latest Test Run: User Tests

```
[INFO] User tests require Web Frontend on port 5173
[INFO] See USER_LOGIN_TEST_GUIDE.md for setup
```

## 📚 Documentation

- `README.md` - This comprehensive guide
- `QUICK_START.md` - Quick start guide (Vietnamese)
- `USER_LOGIN_TEST_GUIDE.md` - User test troubleshooting guide (Vietnamese)

## 🚀 Next Steps

1. **Start Web Frontend** for user tests:
   ```powershell
   cd d:\uniclub\uniclub-fe\web
   npm run dev
   ```

2. **Run user tests:**
   ```powershell
   cd d:\uniclub\selenium-tests
   mvn test -Dtest=UserLoginTest
   ```

3. **Implement additional modules:**
   - Products Management
   - Orders Management
   - Categories Management
   - Shopping Cart
   - Checkout Process

4. **Add advanced features:**
   - Cross-browser testing (Firefox, Edge)
   - Parallel test execution
   - API testing integration
   - Database validation

## 📞 Support

For issues or questions:
1. Check `USER_LOGIN_TEST_GUIDE.md` for troubleshooting
2. Review console logs and screenshots in `target/screenshots/`
3. Check Allure reports for detailed test execution

## 📄 License

This test automation framework is part of the UniClub project.

---

**Happy Testing! 🚀**
