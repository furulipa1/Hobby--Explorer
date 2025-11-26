package com.hobbyexplorer.tests;

import com.hobbyexplorer.base.BaseTest;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class HomePageTest extends BaseTest {

    private static final String BASE_URL = "https://preview--hobby-quest-unlocked.lovable.app/";
    private static final String USERNAME  = "gabenyj@gmail.com";
    private static final String PASSWORD  = "Cascudo123!";

    // --- Login/UI locators ---
    private final By emailInput       = By.cssSelector("input#email, input[name='email'], input[type='email']");
    private final By passwordInput    = By.cssSelector("input#password, input[name='password'], input[type='password']");
    private final By signInButton     = By.xpath("//button[@type='submit' or contains(.,'Log in') or contains(.,'Sign in')]");
    private final By openLoginUi      = By.xpath("//*[self::a or self::button][contains(translate(.,'LOGIN SIGNIN','login signin'),'login') or contains(translate(.,'LOGIN SIGNIN','login signin'),'sign in')]");
    private final By logoutButton     = By.xpath("//button[normalize-space()='Logout' or contains(.,'Logout') or //a[normalize-space()='Logout']]");

    // --- Page markers (for verification) ---
    private final By exploreHeading   = By.xpath("//h1[contains(.,'Explore')]");
    private final By locationsHeading = By.xpath("//h1[contains(.,'Locations')]");
    private final By homeHeading      = By.xpath("//h1[contains(.,'Home')]");

    // --- Header scope (avoid matching content links) ---
    private final By headerLinksOrButtons = By.cssSelector("header a, header button");

    /* ============================= TEST ============================= */

    @Test(description = "Logs in, then Explore -> Locations -> Home (1s pauses; final Home before close)")
    public void navigate_header_explore_locations_home() throws InterruptedException {
        doLoginWithDelay(); // login

        // Make nav actions snappy
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        try {
            // Explore (+1s pause inside fastGo)
            fastGo("Explore", "/explore", exploreHeading);

            // Locations (+1s pause inside fastGo)
            fastGo("Locations", "/locations", locationsHeading);

            // FINAL: Home once
            fastGo("Home", "/", homeHeading);
            Thread.sleep(1000); // final pause on Home before close
        } finally {
            // Restore your BaseTest implicit wait (adjust if yours differs)
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        }
    }

    /* ===================== FAST header navigation core ===================== */

    /**
     * Quick header click (≤ ~400ms). If not enough, navigate directly.
     * Adds a 1s pause after Explore/Locations only.
     */
    private void fastGo(String visibleLabel, String hrefFragment, By pageMarker) throws InterruptedException {
        // 1) Try quick header click
        WebElement nav = findHeaderNavQuick(visibleLabel, hrefFragment);
        if (nav != null) {
            strongClickNoWait(nav);
            if (urlContainsQuick(hrefFragment) || appearsQuick(pageMarker)) {
                if (visibleLabel.equalsIgnoreCase("Explore") || visibleLabel.equalsIgnoreCase("Locations")) {
                    Thread.sleep(1000); // requested pause
                }
                return;
            }
        }

        // 2) Direct-route fallback (very fast)
        String url = hrefFragment.startsWith("http")
                ? hrefFragment
                : ("/".equals(hrefFragment) ? BASE_URL : BASE_URL + trimLeadingSlash(hrefFragment));
        driver.navigate().to(url);

        // Confirm with tiny waits
        boolean ok = urlContainsQuick(hrefFragment) || appearsQuick(pageMarker);
        Assert.assertTrue(ok, "Failed to reach " + visibleLabel + " via direct URL: " + url);

        if (visibleLabel.equalsIgnoreCase("Explore") || visibleLabel.equalsIgnoreCase("Locations")) {
            Thread.sleep(1000); // requested pause
        }
    }

    private String trimLeadingSlash(String s) { return s.startsWith("/") ? s.substring(1) : s; }

    /** Finds a header nav item quickly by label or href fragment. */
    private WebElement findHeaderNavQuick(String label, String hrefFragment) {
        String target = label.toLowerCase();
        List<WebElement> candidates = driver.findElements(headerLinksOrButtons);
        for (WebElement el : candidates) {
            try {
                if (!el.isDisplayed() || !el.isEnabled()) continue;
                String txt = (el.getText() == null ? "" : el.getText().trim()).toLowerCase();
                String href = el.getAttribute("href");
                if (!txt.isEmpty() && txt.contains(target)) return el;
                if (hrefFragment != null && href != null &&
                    href.toLowerCase().contains(hrefFragment.toLowerCase())) {
                    return el;
                }
            } catch (StaleElementReferenceException ignored) {}
        }
        return null;
    }

    /** Strong click without extra waits: Actions → element.click() → JS click. */
    private void strongClickNoWait(WebElement el) {
        try {
            new Actions(driver).moveToElement(el).pause(Duration.ofMillis(40)).click().perform();
        } catch (Exception e1) {
            try { el.click(); }
            catch (Exception e2) {
                try { ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el); }
                catch (Exception ignored) {}
            }
        }
    }

    private boolean urlContainsQuick(String fragment) {
        String f = fragment.toLowerCase();
        try {
            return new WebDriverWait(driver, Duration.ofMillis(400))
                    .until(d -> d.getCurrentUrl().toLowerCase().contains(f));
        } catch (TimeoutException e) {
            return driver.getCurrentUrl().toLowerCase().contains(f);
        }
    }

    private boolean appearsQuick(By locator) {
        try {
            new WebDriverWait(driver, Duration.ofMillis(400))
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return !driver.findElements(locator).isEmpty();
        }
    }

    /* ===================== Login & small helpers ===================== */

    private void doLoginWithDelay() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(12));
        driver.get(BASE_URL);

        if (!isPresent(emailInput) || !isPresent(passwordInput)) {
            clickStrong(openLoginUi, 8);
        }

        WebElement email = wait.until(ExpectedConditions.elementToBeClickable(emailInput));
        email.clear();
        email.sendKeys(USERNAME);

        WebElement pwd = wait.until(ExpectedConditions.elementToBeClickable(passwordInput));
        pwd.clear();
        pwd.sendKeys(PASSWORD);

        WebElement signIn = wait.until(ExpectedConditions.elementToBeClickable(signInButton));
        signIn.click();

        // small delay for UI transitions
        Thread.sleep(1200);

        boolean authed = appears(logoutButton, 10) || waitUntilUrlDoesNotContain("/login", 10);
        Assert.assertTrue(authed, "Sign-in did not complete as expected.");
    }

    private boolean isPresent(By locator) {
        try { return !driver.findElements(locator).isEmpty(); }
        catch (Exception ignored) { return false; }
    }

    private boolean appears(By locator, int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private boolean waitUntilUrlDoesNotContain(String fragment, int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(d -> !d.getCurrentUrl().toLowerCase().contains(fragment.toLowerCase()));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private void scrollIntoView(WebElement el) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'center'});", el);
        } catch (Exception ignored) {}
    }

    /** Click by locator with regular waits (used during login). */
    private boolean clickStrong(By locator, int seconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
            WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
            scrollIntoView(el);
            try {
                new Actions(driver).moveToElement(el).pause(Duration.ofMillis(50)).click().perform();
                return true;
            } catch (Exception ignored) {
                el.click();
                return true;
            }
        } catch (Exception e) {
            try {
                WebElement el = driver.findElement(locator);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
                return true;
            } catch (Exception ignored) {
                return false;
            }
        }
    }
}
