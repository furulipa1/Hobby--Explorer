package com.hobbyexplorer.tests;

import com.hobbyexplorer.base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginTest extends BaseTest {

    private static final String BASE_URL = "https://preview--hobby-quest-unlocked.lovable.app/";
    private static final String USERNAME  = "gabenyj@gmail.com";
    private static final String PASSWORD  = "Cascudo123!";

    // --- Locators (adjust if your DOM differs) ---
    private final By emailInput       = By.cssSelector("input#email, input[name='email'], input[type='email']");
    private final By passwordInput    = By.cssSelector("input#password, input[name='password'], input[type='password']");
    private final By signInButton     = By.xpath("//button[@type='submit' or contains(.,'Log in') or contains(.,'Sign in')]");
    private final By openLoginUi      = By.xpath("//*[self::a or self::button][contains(translate(.,'LOGIN SIGNIN','login signin'),'login') or contains(translate(.,'LOGIN SIGNIN','login signin'),'sign in')]");
    // Use the presence of a visible Logout button as the post-login marker:
    private final By logoutButton     = By.xpath("//button[normalize-space()='Logout' or contains(.,'Logout') or //a[normalize-space()='Logout']]");

    /* ============================= TESTS ============================= */

    // Test 1: Login only (adds a brief delay after clicking Sign in)
    @Test(description = "Logs in and confirms signed-in state (adds a brief delay after clicking Sign in)")
    public void login_only_confirms_signed_in() throws InterruptedException {
        doLoginWithDelay();

        // Confirm we’re signed in by waiting for Logout to appear
        boolean signedIn = appears(logoutButton, 12);
        Assert.assertTrue(signedIn, "Expected to see a Logout button after signing in.");

        // Optional: short pause to observe
        Thread.sleep(2000);
    }

    // Test 2: Login then Logout (and close the browser at the end)
    @Test(description = "Logs in the same way, then clicks Logout and verifies logged-out state")
    public void login_then_logout() throws InterruptedException {
        doLoginWithDelay();

        // Ensure we really are logged in first
        Assert.assertTrue(appears(logoutButton, 10), "Expected Logout button to confirm we’re logged in.");

        // Click Logout (robust click)
        Assert.assertTrue(click(logoutButton, 8), "Could not click the Logout button.");

        // Verify logged out: either login fields visible OR URL looks like login
        boolean loginFieldsVisible = appears(emailInput, 10);
        boolean urlLooksLoggedOut  = driver.getCurrentUrl().toLowerCase().contains("/login");
        Assert.assertTrue(loginFieldsVisible || urlLooksLoggedOut,
                "Expected to be logged out (login fields visible or on /login).");

        // Small pause to observe
        Thread.sleep(1500);

        // ✅ Close browser only for this test
        driver.quit();
    }

    /* ===================== Reusable steps ===================== */

    /** Opens the site, surfaces login UI if needed, fills creds, clicks Sign in, then waits & adds a small delay. */
    private void doLoginWithDelay() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(12));

        driver.get(BASE_URL);

        // Open login form if it’s behind a button/menu
        if (!isPresent(emailInput) || !isPresent(passwordInput)) {
            click(openLoginUi, 8); // best-effort
        }

        // Fill in email & password
        WebElement email = wait.until(ExpectedConditions.elementToBeClickable(emailInput));
        email.clear();
        email.sendKeys(USERNAME);

        WebElement pwd = wait.until(ExpectedConditions.elementToBeClickable(passwordInput));
        pwd.clear();
        pwd.sendKeys(PASSWORD);

        // Click Sign in
        WebElement signIn = wait.until(ExpectedConditions.elementToBeClickable(signInButton));
        signIn.click();

        // **Requested delay** to ensure the app completes sign-in animations/transitions:
        Thread.sleep(1200); // adjust to 1500–2000ms if needed

        // Also wait explicitly for a signed-in signal (Logout visible OR URL not containing /login)
        boolean authed = appears(logoutButton, 10) || waitUntilUrlDoesNotContain("/login", 10);
        if (!authed) {
            // One more brief wait if UI is a tad slower
            Thread.sleep(800);
            authed = appears(logoutButton, 4) || waitUntilUrlDoesNotContain("/login", 4);
        }
        Assert.assertTrue(authed, "Sign-in did not complete as expected.");
    }

    /* ===================== Small utility helpers ===================== */

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

    private boolean click(By locator, int seconds) {
        try {
            WebElement el = new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.elementToBeClickable(locator));
            scrollIntoView(el);
            el.click();
            return true;
        } catch (TimeoutException e) {
            return false;
        } catch (Exception e) {
            // Fallback to JS click if a normal click fails (overlay/animation)
            try {
                WebElement el = new WebDriverWait(driver, Duration.ofSeconds(2))
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
                scrollIntoView(el);
                jsClick(el);
                return true;
            } catch (Exception ignored) {
                return false;
            }
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

    private void jsClick(WebElement el) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        } catch (Exception ignored) {}
    }
}
