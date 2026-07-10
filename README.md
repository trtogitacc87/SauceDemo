# SauceDemo Selenium Automation

A comprehensive Selenium automation test suite for the SauceDemo application using **Selenium 4**, **TestNG**, and **ExtentReports**. Includes tests for login flows, cart operations, session management, performance validation, and special user behavior scenarios.

## 📋 Test Coverage

### Test Categories

#### **Smoke Tests** (Fast path validation)
- `functionalUsersShouldLogin` - Login with valid users (5 user types)
- `addItemsAndCheckoutShouldCompletePurchase` - End-to-end checkout flow

#### **Full Regression Tests** (Comprehensive coverage)
All smoke tests plus:

**Session Management Tests:**
- `logoutAndReloginShouldWork` - Logout and re-login functionality
- `cartShouldClearAfterLogout` - Cart state after logout
- `shouldNotAccessInventoryAfterLogout` - Authentication enforcement

**Performance/Stress Tests:**
- `multipleLoginLogoutCyclesShouldMaintainStability` - 5 rapid login/logout cycles
- `sequentialLoginDifferentUsersShouldWork` - Multiple user types in sequence

**Special User Behavior Tests:**
- `problemUserShouldLoginAndAddItems` - UI glitch handling (add/remove items)
- `performanceGlitchUserLoginPerformance` - Performance measurement with timing
- `visualUserShouldCompletePurchase` - Visual rendering differences (full purchase flow)
- `errorUserShouldLoginAndHandleErrors` - Error message handling (add/remove items)
- `unfunctionalUsersShouldShowError` - Invalid credentials and edge cases

**Blocked Functions Tests:**
- `lockedOutUserCannotLogin` - Locked account blocking
- `lockedOutUserBlockedOnRetry` - Persistent account blocking
- `directInventoryAccessShouldRequireLogin` - Direct URL access protection
- `noCredentialsShouldShowError` - Authentication bypass check

### Special User Types (SauceLabs Demo)
- `standard_user` - Normal functionality
- `problem_user` - UI rendering issues
- `performance_glitch_user` - Performance delays
- `visual_user` - Visual rendering differences
- `error_user` - Error message generation
- `locked_out_user` - Account locked (cannot login)

## 🚀 Quick Start

### Prerequisites
- Java 18+
- Maven 3.8+
- Chrome/Firefox browser installed

### Local Execution

```bash
# Run all tests (fullRegression - default)
mvn clean test

# Run smoke tests only
mvn clean test -Dgroups=smoke

# Run full regression tests explicitly
mvn clean test -Dgroups=fullRegression

# Run tests with specific browser
mvn clean test -Dbrowser=chrome
mvn clean test -Dbrowser=firefox

# Run specific test class
mvn clean test -Dtest=SauceDemoLoginTest

# Run both Chrome and Firefox (parallel)
mvn clean test -DparallelTestExecution=true
```

## 🔄 Git Branching Strategy & CI/CD

### Automated Branch-Based Testing

| Branch | Test Group | Purpose |
|--------|-----------|---------|
| `dev` | smoke | Fast validation for development |
| `main`/`master` | fullRegression | Complete test suite for releases |

### GitHub Actions Workflow

**Automatic triggers:**
- Push to `dev` → Runs **smoke tests** (2-3 min)
- Push to `main`/`master` → Runs **fullRegression tests** (10-15 min)

**Manual trigger:**
- Go to `Actions` tab → Select "CI" workflow → Click "Run workflow"
- Choose test group from dropdown (defaults to `fullRegression`)

**Supported browsers:** Chrome and Firefox (parallel execution)

### Example Workflow Commands

```bash
# Commit to dev → Auto-runs smoke tests
git add .
git commit -m "feat: add new feature"
git push origin dev

# Commit to main → Auto-runs full regression
git add .
git commit -m "release: v1.0.0"
git push origin main
```

## 📊 Reports & Artifacts

### Report Location
After test execution, find reports in:
- **Main Report:** `ExtentReport/extent-report.html`
- **Screenshots:** `ExtentReport/screenshots/`

### Report Features
- ✅ Test results with step-by-step logs
- ✅ Browser information (Chrome/Firefox)
- ✅ Test group tags (smoke/fullRegression)
- ✅ Screenshot attachments for failures
- ✅ Execution timeline and statistics

### GitHub Actions Artifacts
Reports and screenshots are automatically uploaded after each workflow run:
- Navigate to Actions tab → Select workflow run → Download artifacts
- `extent-report` - Full HTML report
- `screenshots` - Failed test screenshots

## 🏗️ Project Structure

```
src/test/java/com/saucedemo/
├── core/
│   ├── BaseTest.java          # Test base class with driver management
│   └── BaseAction.java         # Selenium action helpers
├── pages/
│   ├── LoginPage.java
│   ├── InventoryPage.java
│   ├── CartPage.java
│   └── CheckoutPage.java
├── listeners/
│   └── TestListener.java       # ExtentReports integration
└── tests/
    ├── SauceDemoLoginTest.java       # Login & session tests
    └── SauceDemoCartCheckoutTest.java # Cart & checkout tests

testng.xml                      # TestNG configuration
pom.xml                         # Maven dependencies
.github/workflows/ci.yml        # GitHub Actions workflow
```

## 🛠️ Configuration Files

### `testng.xml`
- Defines test suites and test groups
- Configures Chrome and Firefox browser parameters
- Sets parallel execution settings

### `pom.xml`
- Maven dependencies (Selenium, TestNG, ExtentReports)
- Surefire plugin configuration for test execution
- JDK 18 compatibility settings

### `.github/workflows/ci.yml`
- Branch-based test group selection
- Artifact collection and upload
- Manual workflow dispatch with dropdown selector

## 📝 Test Execution Examples

### Example 1: Local Smoke Test Run
```bash
mvn clean test -Dgroups=smoke
```
**Expected:** 2 critical tests run, ~2 minutes

### Example 2: Full Regression (Both Browsers)
```bash
mvn clean test -Dgroups=fullRegression
```
**Expected:** 16+ tests × 2 browsers, ~20 minutes

### Example 3: Single Test Debugging
```bash
mvn clean test -Dtest=SauceDemoLoginTest#functionalUsersShouldLogin
```

### Example 4: GitHub Actions Manual Trigger
1. Go to GitHub repository → Actions tab
2. Select "CI" workflow
3. Click "Run workflow"
4. Choose "fullRegression" from dropdown
5. Click "Run workflow"

## 🐛 Troubleshooting

| Issue | Solution |
|-------|----------|
| Tests fail on Windows | Use `mvn clean test` instead of shell commands |
| Screenshot directory missing | Tests create it automatically; check file permissions |
| Report not generated | Ensure `ExtentReport/` folder exists in project root |
| Browser not found | Install Chrome/Firefox or specify path in BaseTest.java |
| Parallel execution conflicts | Reduce `thread-count` in testng.xml if needed |

## 📋 Test Features

- ✅ **Data-driven testing** - Multiple users, credentials, and scenarios
- ✅ **Parallel execution** - Chrome and Firefox tests run simultaneously
- ✅ **Smart wait strategies** - Explicit waits with timeouts
- ✅ **Comprehensive logging** - Step-by-step test execution logs
- ✅ **Screenshot on failure** - Automatic failure captures
- ✅ **Session management** - Logout/re-login validation
- ✅ **Performance tracking** - Execution time measurements
- ✅ **Group-based filtering** - Smoke vs full regression

## 🔐 Test Data

All test data is embedded in test files:
- Valid credentials: `standard_user`, `problem_user`, etc. (password: `secret_sauce`)
- Invalid credentials: Multiple edge cases (SQL injection, XSS, empty fields, etc.)
- Special users: Built-in to SauceLabs demo site

## 📚 Dependencies

```xml
<!-- Selenium WebDriver -->
<selenium-java>4.16.0</selenium-java>

<!-- TestNG Framework -->
<testng>7.8.0</testng>

<!-- ExtentReports -->
<extentreports>5.0.9</extentreports>

<!-- Utilities -->
<commons-io>2.13.0</commons-io>
```

## 🎯 Best Practices

1. **Always run smoke tests before committing** to dev
2. **Use meaningful commit messages** for CI tracking
3. **Check GitHub Actions logs** for detailed failure info
4. **Review ExtentReport** for step-level debugging
5. **Run locally before pushing** to avoid CI failures

## 📞 Support

For issues or questions:
1. Check the ExtentReport for detailed logs
2. Review GitHub Actions workflow logs
3. Verify browser installations
4. Ensure Java 18+ is installed

---

**Last Updated:** 2026-07-10  
**Test Framework Version:** 1.0  
**Status:** ✅ Production Ready
