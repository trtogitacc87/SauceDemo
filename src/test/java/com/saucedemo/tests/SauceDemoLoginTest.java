package com.saucedemo.tests;

import com.saucedemo.core.BaseTest;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SauceDemoLoginTest extends BaseTest {
    @DataProvider(name = "functionalUsers")
    public Object[][] functionalUsers() {
        return new Object[][]{
                {"standard_user", "secret_sauce"},
                {"problem_user", "secret_sauce"},
                {"performance_glitch_user", "secret_sauce"},
                {"visual_user", "secret_sauce"},
                {"error_user", "secret_sauce"}
        };
    }

    @DataProvider(name = "unfunctionalUsers")
    public Object[][] unfunctionalUsers() {
        return new Object[][]{
                {"locked_out_user", "secret_sauce", "user has been locked out."},
                {"", "secret_sauce", "Username is required"},
                {"standard_user", "", "Password is required"},
                {"", "", "Username is required"},
                {"invalid_user", "invalid_password", "Username and password do not match any user in this service"},
                {"<script>alert('xss')</script>", "secret_sauce", "Username and password do not match any user in this service"},
                {"' OR '1'='1", "secret_sauce", "Username and password do not match any user in this service"},
                {"   ", "secret_sauce", "Username and password do not match any user in this service"},
                {"standard_user", "   ", "Username and password do not match any user in this service"},
                {"standard_user", "secret_sauce ", "Username and password do not match any user in this service"},
                {"standard_user", " secret_sauce", "Username and password do not match any user in this service"},
                {"standard_user!@#", "secret_sauce", "Username and password do not match any user in this service"},
                {"日本語ユーザー", "secret_sauce", "Username and password do not match any user in this service"}
        };
    }

    @Test(description = "Verify functional login succeeds", dataProvider = "functionalUsers", groups = {"smoke", "fullRegression"})
    public void LoginFlow_UsersShouldLogin(String username, String password) {
        logStep("Executing functional login scenario for user: " + username);
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        loginPage.login(username, password);

        InventoryPage inventoryPage = new InventoryPage(getDriver());
        Assert.assertEquals(inventoryPage.getTitle(), "Products",
                "Expected login to navigate to the Products page for user: " + username);
    }

    @Test(description = "Verify unfunctional/security cases show expected errors", dataProvider = "unfunctionalUsers", groups = {"fullRegression"})
    public void LoginFlow_UsersShouldShowError(String username, String password, String expectedErrorMessage) {
        logStep("Executing unfunctional/security scenario: user='" + username + "', expectedError='" + expectedErrorMessage + "'");
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        loginPage.login(username, password);

        String actualErrorMessage = loginPage.getErrorMessage();
        Assert.assertTrue(actualErrorMessage.contains(expectedErrorMessage),
                "Expected error message for user " + username + " to contain: " + expectedErrorMessage
                        + ". Actual message: " + actualErrorMessage);
    }

    //@Test(description = "check report screenshot")
    public void checkReportScreenshot() {
        logStep("Executing scenario: Check report screenshot");
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        loginPage.login("standard_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(getDriver());
        Assert.assertEquals(inventoryPage.getTitle(), "Product",
                "Expected login to navigate to the Products page for user: standard_user");
    }

    // ============= SESSION MANAGEMENT TESTS =============

    @Test(description = "Verify logout functionality and re-login capability", groups = {"fullRegression"})
    public void LoginFlow_logoutAndReloginShouldWork() {
        logStep("Step 1: Login with standard_user");
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        loginPage.login("standard_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(getDriver());
        Assert.assertEquals(inventoryPage.getTitle(), "Products", "Login should succeed");

        logStep("Step 2: Logout");
        inventoryPage.logout();
        
        logStep("Step 3: Verify user is back at login page");
        Assert.assertEquals(loginPage.getTitle(), "Swag Labs", "User should be at login page after logout");

        logStep("Step 4: Re-login with same credentials");
        loginPage.login("standard_user", "secret_sauce");
        Assert.assertEquals(inventoryPage.getTitle(), "Products", "Re-login should succeed");
    }

    @Test(description = "Verify cart is cleared after logout and re-login", groups = {"fullRegression"})
    public void LoginFlow_cartShouldClearAfterLogout() {
        logStep("Step 1: Login and add items to cart");
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        loginPage.login("standard_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(getDriver());
        inventoryPage.addItemToCart("Sauce Labs Backpack");
        int cartCountBefore = inventoryPage.getCartCount();
        Assert.assertTrue(cartCountBefore > 0, "Cart should have items before logout");

        logStep("Step 2: Logout");
        inventoryPage.logout();

        logStep("Step 3: Re-login");
        loginPage.login("standard_user", "secret_sauce");
        
        logStep("Step 4: Verify cart is cleared");
        int cartCountAfter = inventoryPage.getCartCount();
        Assert.assertEquals(cartCountAfter, 0, "Cart should be empty after logout and re-login");
    }

    @Test(description = "Verify cannot access inventory without login after logout", groups = {"fullRegression"})
    public void LoginFlow_shouldNotAccessInventoryAfterLogout() {
        logStep("Step 1: Login normally");
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        loginPage.login("standard_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(getDriver());
        Assert.assertEquals(inventoryPage.getTitle(), "Products", "Login should succeed");

        logStep("Step 2: Logout");
        inventoryPage.logout();

        logStep("Step 3: Try to access inventory directly");
        inventoryPage.open();
        
        logStep("Step 4: Verify user is redirected to login");
        Assert.assertEquals(loginPage.getTitle(), "Swag Labs", 
                "User should not be able to access inventory without login");
    }

    // ============= PERFORMANCE/STRESS TESTS =============

    @Test(description = "Verify app stability with multiple rapid login/logout cycles", groups = {"fullRegression"})
    public void LoginFlow_multipleLoginLogoutCyclesShouldMaintainStability() {
        logStep("Starting multiple login/logout cycles - 5 iterations");
        LoginPage loginPage = new LoginPage(getDriver());
        InventoryPage inventoryPage = new InventoryPage(getDriver());

        for (int i = 1; i <= 5; i++) {
            logStep("Cycle " + i + ": Login");
            loginPage.open();
            loginPage.login("standard_user", "secret_sauce");
            Assert.assertEquals(inventoryPage.getTitle(), "Products", 
                    "Login failed on cycle " + i);

            logStep("Cycle " + i + ": Logout");
            inventoryPage.logout();
            
            try {
                Thread.sleep(500); // Brief pause between cycles
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        logStep("All 5 cycles completed successfully");
    }

   
    // ============= SPECIAL USER BEHAVIOR TESTS =============

    @Test(description = "Verify performance_glitch_user login completes within acceptable time", groups = {"fullRegression"})
    public void LoginFlow_performanceGlitchUserLoginPerformance() {
        logStep("Testing performance_glitch_user login performance");
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();

        long startTime = System.currentTimeMillis();
        loginPage.login("performance_glitch_user", "secret_sauce");
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        InventoryPage inventoryPage = new InventoryPage(getDriver());
        Assert.assertEquals(inventoryPage.getTitle(), "Products", 
                "performance_glitch_user should login successfully");

        logStep("Login completed in " + duration + "ms (acceptable even with glitches)");

        Assert.assertTrue(duration < 10000, 
                "Login for performance_glitch_user should complete within 10 seconds. Actual: " + duration + "ms");
    }


    // ============= BLOCKED FUNCTIONS TESTS =============

    @Test(description = "Verify inventory requires authentication (no direct URL access)", groups = {"fullRegression"})
    public void LoginFlow_directInventoryAccessShouldRequireLogin() {
        logStep("Attempting direct access to inventory URL without authentication");
        InventoryPage inventoryPage = new InventoryPage(getDriver());
        inventoryPage.open();

        LoginPage loginPage = new LoginPage(getDriver());
        String currentTitle = loginPage.getTitle();
        logStep("Current page title: " + currentTitle);

        logStep("Verify user is blocked and redirected/shown login");
        Assert.assertEquals(currentTitle, "Swag Labs", 
                "Direct URL access without login should be blocked");


    }
}
