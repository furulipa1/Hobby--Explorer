package com.hobbyexplorer.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class HomePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final String baseUrl = "https://preview--hobby-quest-unlocked.lovable.app/";
    private final By exploreLink = By.linkText("Explore");
    private final By exploreLinkFallback =
            By.cssSelector("a[href*='explore'], [data-test='nav-explore'], [data-test='explore']");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public HomePage open() {
        driver.get(baseUrl);
        return this;
    }

    public ExplorePage goToExplore() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(exploreLink)).click();
        } catch (TimeoutException e) {
            wait.until(ExpectedConditions.elementToBeClickable(exploreLinkFallback)).click();
        }
        return new ExplorePage(driver);
    }

    public String title() {
        return driver.getTitle();
    }
}
