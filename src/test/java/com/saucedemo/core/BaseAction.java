package com.saucedemo.core;

import com.saucedemo.listeners.TestListener;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BaseAction {
    private static final Duration DEFAULT_WAIT = Duration.ofSeconds(15);
    protected final WebDriver driver;

    protected BaseAction(WebDriver driver) {
        this.driver = driver;
    }

    protected void openUrl(String url, String description) {
        logStep("Open URL: " + url + " - " + description);
        driver.get(url);
    }

    protected void click(By locator, String description) {
        logStep("Click on: " + description + " [" + locator + "]");
        driver.findElement(locator).click();
    }

    protected void clickbyJs(By locator, String description) {
        logStep("Click on: " + description + " [" + locator + "]");
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(locator));
    }

    protected void type(By locator, CharSequence value, String description) {
        logStep("Enter text into: " + description + " [" + locator + "] value='" + value + "'");
        WebElement element = driver.findElement(locator);
        element.clear();
        element.sendKeys(value);
    }

    protected String getText(By locator, String description) {
        WebElement element = driver.findElement(locator);
        String text = element.getText().trim();
        logStep("Get text from: " + description + " [" + locator + "] -> '" + text + "'");
        return text;
    }

    protected WebElement find(By locator, String description) {
        logStep("Locate element: " + description + " [" + locator + "]");
        return driver.findElement(locator);
    }

    protected WebElement waitForVisibility(By locator, String description) {
        logStep("Wait for visibility: " + description + " [" + locator + "]");
        return new WebDriverWait(driver, DEFAULT_WAIT)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator, String description) {
        logStep("Wait for clickable: " + description + " [" + locator + "]");
        return new WebDriverWait(driver, DEFAULT_WAIT)
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected boolean waitForText(By locator, String expectedText, String description) {
        logStep("Wait for text: " + description + " [" + locator + "] -> '" + expectedText + "'");
        return new WebDriverWait(driver, DEFAULT_WAIT)
                .until(ExpectedConditions.textToBe(locator, expectedText));
    }

    protected boolean waitForTitle(String expectedTitle) {
        logStep("Wait for title: '" + expectedTitle + "'");
        return new WebDriverWait(driver, DEFAULT_WAIT)
                .until(ExpectedConditions.titleIs(expectedTitle));
    }

    protected boolean waitForUrlContains(String partialUrl) {
        logStep("Wait for URL contains: '" + partialUrl + "'");
        return new WebDriverWait(driver, DEFAULT_WAIT)
                .until(ExpectedConditions.urlContains(partialUrl));
    }

    protected void logStep(String message) {
        TestListener.logStep(message);
    }

    
}
