package com.saucedemo.pages;

import com.saucedemo.core.BaseAction;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage extends BaseAction {
    private final By checkoutButton = By.id("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public void clickCheckout() {
        click(checkoutButton, "Checkout button");
    }
}
