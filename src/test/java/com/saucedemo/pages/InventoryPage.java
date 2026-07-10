package com.saucedemo.pages;

import com.saucedemo.core.BaseAction;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class InventoryPage extends BaseAction {
    private final By pageTitle = By.className("title");
    private final By cartButton = By.id("shopping_cart_container");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By menuButton = By.id("react-burger-menu-btn");
    private final By logoutButton = By.id("logout_sidebar_link");
    private final String url = "https://www.saucedemo.com/inventory.html";
    private final String itemXpathTemplate = "//div[text()='%s']/ancestor::div[@class='inventory_item']//button";

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        openUrl(url, "Products page");
    }

    public String getTitle() {
        waitForVisibility(pageTitle, "Wait for page title");
        return getText(pageTitle, "Inventory page title");
    }

    public void addItemToCart(String itemName) {
        String xpath = String.format(itemXpathTemplate, itemName);
        click(By.xpath(xpath), "Add item to cart: " + itemName);
    }

    public void openCart() {
        click(cartButton, "Open cart button");
    }

    public void logout() {
        logStep("Opening menu to access logout");
        clickbyJs(menuButton, "Menu button");
        waitForVisibility(logoutButton, "Wait for logout button to be visible");
        logStep("Clicking logout button");
        click(logoutButton, "Logout button");
    }

    public int getCartCount() {
        try {
            String badgeText = getText(cartBadge, "Cart item count badge");
            return Integer.parseInt(badgeText);
        } catch (Exception e) {
            logStep("No items in cart (badge not visible)");
            return 0;
        }
    }

    public void removeItemFromCart(String itemName) {
        String xpath = String.format(itemXpathTemplate, itemName);
        click(By.xpath(xpath), "Remove item from cart: " + itemName);
    }
}
