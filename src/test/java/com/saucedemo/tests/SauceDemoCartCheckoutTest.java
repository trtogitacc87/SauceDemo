package com.saucedemo.tests;

import com.saucedemo.core.BaseTest;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.CheckoutPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SauceDemoCartCheckoutTest extends BaseTest {

    @Test(description = "Add items to cart and complete checkout", groups = {"smoke", "fullRegression"})
    public void CardFlow_addItemsAndCheckoutShouldCompletePurchase() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        loginPage.login("standard_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(getDriver());
        Assert.assertEquals(inventoryPage.getTitle(), "Products", "Login should succeed before adding items.");

        inventoryPage.addItemToCart("Sauce Labs Backpack");
        inventoryPage.addItemToCart("Sauce Labs Bike Light");
        inventoryPage.openCart();

        CartPage cartPage = new CartPage(getDriver());
        cartPage.clickCheckout();

        CheckoutPage checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.enterCheckoutInformation("Maria", "Tran", "10001");
        checkoutPage.finishCheckout();

        Assert.assertTrue(checkoutPage.getCompleteHeader().toLowerCase().contains("thank you for your order"), "Checkout should complete successfully.");
    }

    // ============= SPECIAL USER BEHAVIOR TESTS =============
    @Test(description = "Verify problem_user can login and add items to cart", groups = {"fullRegression"})
    public void CardFlow_problemUserShouldLoginAndAddItems() {
        logStep("Testing problem_user - known to have UI issues");
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        loginPage.login("problem_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(getDriver());
        Assert.assertEquals(inventoryPage.getTitle(), "Products", "problem_user should login successfully");

        logStep("Attempting to add items with problem_user");
        inventoryPage.addItemToCart("Sauce Labs Backpack");
        int cartCountAfterAdd = inventoryPage.getCartCount();
        Assert.assertTrue(cartCountAfterAdd > 0, "problem_user should be able to add items despite UI issues");
        Assert.assertEquals(cartCountAfterAdd, 1, "Cart should contain 1 item after first add");

        logStep("Add second item to verify multiple items");
        inventoryPage.addItemToCart("Sauce Labs Bike Light");
        int cartCountAfterSecondAdd = inventoryPage.getCartCount();
        Assert.assertEquals(cartCountAfterSecondAdd, 2, "Cart should contain 2 items after second add");

        logStep("Remove first item from cart");
        inventoryPage.removeItemFromCart("Sauce Labs Backpack");
        int cartCountAfterRemove = inventoryPage.getCartCount();
        Assert.assertEquals(cartCountAfterRemove, 1, "Cart should contain 1 item after removing first item");

        logStep("Remove remaining item");
        inventoryPage.removeItemFromCart("Sauce Labs Bike Light");
        int cartCountAfterAllRemoved = inventoryPage.getCartCount();
        Assert.assertEquals(cartCountAfterAllRemoved, 0, "Cart should be empty after removing all items");

        logStep("Add/remove operations completed successfully for problem_user");
    }

    @Test(description = "Verify visual_user can complete purchase flow with visual differences", groups = {"fullRegression"})
    public void CardFlow_visualUserShouldCompletePurchase() {
        logStep("Testing visual_user - known to have visual rendering differences");
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        loginPage.login("visual_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(getDriver());
        Assert.assertEquals(inventoryPage.getTitle(), "Products", "visual_user should login successfully");

        logStep("Adding items with visual_user");
        inventoryPage.addItemToCart("Sauce Labs Backpack");
        int cartCount1 = inventoryPage.getCartCount();
        Assert.assertEquals(cartCount1, 1, "First item should be in cart");

        inventoryPage.addItemToCart("Sauce Labs Bike Light");
        int cartCount2 = inventoryPage.getCartCount();
        Assert.assertEquals(cartCount2, 2, "Second item should be in cart");

        logStep("Open cart and proceed to checkout");
        inventoryPage.openCart();

        CartPage cartPage = new CartPage(getDriver());
        cartPage.clickCheckout();

        CheckoutPage checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.enterCheckoutInformation("John", "Doe", "12345");
        checkoutPage.finishCheckout();

        Assert.assertTrue(checkoutPage.getCompleteHeader().toLowerCase().contains("thank you for your order"), 
                "visual_user should complete purchase despite visual differences");
        
        logStep("Purchase completed successfully with all items");
    }

    
    @Test(description = "Verify error_user can login and trigger error messages", groups = {"fullRegression"})
    public void CardFlow_errorUserShouldLoginAndHandleErrors() {
        logStep("Testing error_user - known to generate error messages");
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        loginPage.login("error_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(getDriver());
        Assert.assertEquals(inventoryPage.getTitle(), "Products", "error_user should login successfully");

        logStep("Adding item with error_user (may generate errors in background)");
        inventoryPage.addItemToCart("Sauce Labs Backpack");
        int cartCountAfterFirstAdd = inventoryPage.getCartCount();
        Assert.assertTrue(cartCountAfterFirstAdd > 0, "error_user should still be able to add items");
        Assert.assertEquals(cartCountAfterFirstAdd, 1, "Cart should have 1 item");

        logStep("Add another item with error_user");
        inventoryPage.addItemToCart("Sauce Labs Bike Light");
        int cartCountAfterSecondAdd = inventoryPage.getCartCount();
        Assert.assertEquals(cartCountAfterSecondAdd, 2, "Cart should have 2 items");

        logStep("Remove one item with error_user");
        inventoryPage.removeItemFromCart("Sauce Labs Backpack");
        int cartCountAfterRemove = inventoryPage.getCartCount();
        Assert.assertEquals(cartCountAfterRemove, 1, "Cart should have 1 item after removal");

        logStep("Remove remaining item");
        inventoryPage.removeItemFromCart("Sauce Labs Bike Light");
        int cartCountAfterAllRemoved = inventoryPage.getCartCount();
        Assert.assertEquals(cartCountAfterAllRemoved, 0, "Cart should be empty after removing all items");

        logStep("All add/remove operations completed successfully despite error_user behavior");
    }
}
