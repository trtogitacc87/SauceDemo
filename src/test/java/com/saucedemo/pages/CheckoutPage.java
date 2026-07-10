package com.saucedemo.pages;

import com.saucedemo.core.BaseAction;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage extends BaseAction {
    private final By firstNameField = By.id("first-name");
    private final By lastNameField = By.id("last-name");
    private final By postalCodeField = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By finishButton = By.id("finish");
    private final By completeHeader = By.className("complete-header");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public void enterCheckoutInformation(String firstName, String lastName, String postalCode) {
        type(firstNameField, firstName, "First name field");
        type(lastNameField, lastName, "Last name field");
        type(postalCodeField, postalCode, "Postal code field");
        click(continueButton, "Continue button");
    }

    public void finishCheckout() {
        click(finishButton, "Finish button");
    }

    public String getCompleteHeader() {
        return getText(completeHeader, "Order completion header");
    }
}
