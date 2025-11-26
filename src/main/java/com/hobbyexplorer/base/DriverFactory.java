package com.hobbyexplorer.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverFactory {

    private static final ThreadLocal<WebDriver> TL = new ThreadLocal<>();

    public static void init() {
        ChromeOptions opts = new ChromeOptions();
        opts.addArguments("--disable-infobars", "--disable-notifications");
        WebDriverManager.chromedriver().setup();
        TL.set(new ChromeDriver(opts));
        get().manage().window().maximize();
    }

    public static WebDriver get() {
        return TL.get();
    }

    public static void quit() {
        WebDriver driver = TL.get();
        if (driver != null) {
            driver.quit();
            TL.remove();
        }
    }
}
