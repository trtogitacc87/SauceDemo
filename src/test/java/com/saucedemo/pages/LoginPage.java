package com.saucedemo.pages;

import com.saucedemo.core.BaseAction;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BaseAction {
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("div.error-message-container");
    private final By errorButton = By.cssSelector("[class*='error'] button");
    private final String url = "https://www.saucedemo.com/";
    
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        openUrl(url, "Sauce Demo login page");
    }

    public void login(String username, String password) {
        type(usernameField, username, "Username field");
        type(passwordField, password, "Password field");
        click(loginButton, "Login button");
    }

    public String getErrorMessage() {
        return getText(errorMessage, "Login error message");
    }

    public String getTitle() {
        waitForTitle("Swag Labs");
        return driver.getTitle();
    }

    public void clearErrorMessage() {
        try {
            click(errorButton, "Close error message button");
            logStep("Error message cleared");
        } catch (Exception e) {
            logStep("No error message to clear");
        }
    }
}
