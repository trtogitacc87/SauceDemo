package com.saucedemo.core;

import com.saucedemo.listeners.TestListener;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

public abstract class BaseTest {
    protected static final ThreadLocal<WebDriver> threadDriver = new ThreadLocal<>();

    public WebDriver getDriver() {
        return threadDriver.get();
    }

    @Parameters({"browser"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(String browser) {
        String targetBrowser = browser != null ? browser.toLowerCase() : System.getProperty("browser", "chrome").toLowerCase();

        if (targetBrowser.contains("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("-width=1920");
            options.addArguments("-height=1080");
            options.addArguments("-private");
            options.addPreference("signon.rememberSignons", false);
            options.addPreference("signon.autofillForms", false);
            options.addPreference("signon.autofillForms.http", false);
            options.addPreference("browser.privatebrowsing.autostart", true);
            options.addPreference("browser.savePassword", false);
            options.addPreference("browser.formfill.enable", false);
            threadDriver.set(new FirefoxDriver(options));
        } else {
            ChromeOptions options = new ChromeOptions();
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("profile.password_manager_enabled", false);
            prefs.put("browser.password_manager_enabled", false);
            prefs.put("credentials_enable_service", false);
            prefs.put("password_manager_enabled", false);
            prefs.put("profile.passwordmanager_enabled", false);
            prefs.put("profile.default_content_setting_values.notifications", 2);
            prefs.put("profile.default_content_setting_values.popups", 0);
            options.setExperimentalOption("prefs", prefs);
            options.addArguments("--disable-save-password-bubble");
            options.addArguments("--disable-password-manager-reauthentication");
            options.addArguments("--disable-features=PasswordManager,Autofill,AutofillProfileEnabled,AutofillCreditCardEnabled,AutofillServerCommunication");
            options.addArguments("--disable-credentials-manager");
            options.addArguments("--disable-blink-features=CredentialManagement,PasswordManager");
            options.addArguments("--incognito");
            // options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
            // options.addArguments("--disable-gpu");
            // options.addArguments("--no-sandbox");
            threadDriver.set(new ChromeDriver(options));
        }
       // getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        getDriver().manage().window().maximize();
    }

    protected void logStep(String message) {
        TestListener.logStep(message);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
            threadDriver.remove();
        }
    }
}
