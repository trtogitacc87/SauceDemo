package com.saucedemo.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.saucedemo.core.BaseTest;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class TestListener implements ITestListener {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
    private static ExtentReports extent;
    private static final Object LOCK = new Object();
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private final Map<String, String> reportScreenshots = new HashMap<>();

    @Override
    public void onStart(ITestContext context) {
        // Initialize ExtentReports only once for all browsers
        synchronized (LOCK) {
            if (extent == null) {
                File reportDir = new File("ExtentReport");
                reportDir.mkdirs();
                
                ExtentSparkReporter sparkReporter = new ExtentSparkReporter(new File(reportDir, "extent-report.html"));
                sparkReporter.config().setReportName("SauceDemo Test Report");
                sparkReporter.config().setDocumentTitle("Test Execution Report");
                
                extent = new ExtentReports();
                extent.attachReporter(sparkReporter);
                extent.setSystemInfo("Environment", "QA");
            }
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        Object[] params = result.getParameters();
        String testName = result.getMethod().getMethodName();

        if (params != null && params.length > 0) {
            testName = String.format("%s [%s]", testName, safeParam(params[0]));
        }

        ExtentTest test = extent.createTest(testName, result.getMethod().getDescription());
        String browser = result.getTestContext().getCurrentXmlTest().getParameter("browser");
        if (browser != null) {
            test.assignCategory(browser);
        }

        // Add test groups/tags as categories
        String[] groups = result.getMethod().getGroups();
        if (groups != null && groups.length > 0) {
            for (String group : groups) {
                test.assignCategory(group);
            }
        }

        extentTest.set(test);
    }

    private String safeParam(Object param) {
        if (param == null) {
            return "null";
        }
        String value = escapeHtml(param.toString().trim());
        if (value.length() > 50) {
            return value.substring(0, 47) + "...";
        }
        return value;
    }

    private static String escapeHtml(String text) {
        if (text == null) {
            return null;
        }
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    public static void logStep(String message) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.info(escapeHtml(message));
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Object testInstance = result.getInstance();
        if (!(testInstance instanceof BaseTest)) {
            return;
        }

        WebDriver driver = ((BaseTest) testInstance).getDriver();
        if (driver == null) {
            return;
        }

        String methodName = result.getMethod().getMethodName();
        String browser = result.getTestContext().getCurrentXmlTest().getParameter("browser");
        String timestamp = LocalDateTime.now().format(FORMATTER);
        File screenshotDir = new File("ExtentReport/screenshots");
        File outputFile = new File(screenshotDir, methodName + "-" + browser + "-" + timestamp + ".png");

        try {
            FileUtils.forceMkdirParent(outputFile);
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot, outputFile);
            reportScreenshots.put(result.getMethod().getMethodName(), outputFile.getAbsolutePath());
            extentTest.get().fail(result.getThrowable(), MediaEntityBuilder.createScreenCaptureFromPath(outputFile.getAbsolutePath()).build());
        } catch (IOException e) {
            extentTest.get().fail("Failed to capture screenshot: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().pass("Test passed");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        extentTest.get().skip(result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        synchronized (LOCK) {
            if (extent != null) {
                extent.flush();
            }
        }
    }

    @Override public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}
}
