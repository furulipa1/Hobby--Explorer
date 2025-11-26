package com.hobbyexplorer.base;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;

    // Run with: -DkeepBrowserOpen=true  (keeps Chrome open after tests)
    private final boolean keepBrowserOpen = Boolean.getBoolean("keepBrowserOpen");

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        // Faster than "normal" – returns control once DOMContentLoaded fires
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);

        // OPTIONAL: persist login across runs by using a stable Chrome profile.
        // Replace the path with a valid folder on your machine.
        // options.addArguments("--user-data-dir=/Users/<you>/hobbyexplorer-chrome-profile");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (driver != null) {
            if (keepBrowserOpen) {
                System.out.println("[BaseTest] Keeping browser open (keepBrowserOpen=true). Close it manually when done.");
            } else {
                driver.quit();
            }
        }
    }
}
