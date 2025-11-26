package com.hobbyexplorer.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ExplorePage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By exploreHeader = By.xpath("//*[self::h1 or self::h2][contains(.,'Explore')]");

    public ExplorePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.or(
            ExpectedConditions.urlContains("explore"),
            ExpectedConditions.visibilityOfElementLocated(exploreHeader)
        ));
    }

    public boolean isAt() {
        return driver.getCurrentUrl().toLowerCase().contains("explore")
            || !driver.findElements(exploreHeader).isEmpty();
    }
}
