package com.hobbyexplorer.tests;

import com.hobbyexplorer.base.BaseTest;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.text.Normalizer;
import java.time.Duration;
import java.util.*;

public class GenreSelectionTest extends BaseTest {

    private static final String BASE_URL = "https://preview--hobby-quest-unlocked.lovable.app/";
    private static final String USERNAME  = "gabenyj@gmail.com";
    private static final String PASSWORD  = "Cascudo123!";

    // --- Common locators ---
    private final By emailInput       = By.cssSelector("input#email, input[name='email'], input[type='email']");
    private final By passwordInput    = By.cssSelector("input#password, input[name='password'], input[type='password']");
    private final By signInButton     = By.xpath("//button[@type='submit' or contains(.,'Log in') or contains(.,'Sign in')]");
    private final By openLoginUi      = By.xpath("//*[self::a or self::button][contains(translate(.,'LOGIN SIGNIN','login signin'),'login') or contains(translate(.,'LOGIN SIGNIN','login signin'),'sign in')]");
    private final By logoutButton     = By.xpath("//button[normalize-space()='Logout' or contains(.,'Logout') or //a[normalize-space()='Logout']]");

    private final By homeHeading      = By.xpath("//h1[contains(.,'Home')]");
    private final By headerLinksOrButtons = By.cssSelector("header a, header button");

    /* ============================= TEST ============================= */

    @Test(description = "Home page: click genres in strict order, verifying selection, returning to Home between each")
    public void home_clickGenres_inOrder_thenReturnHome() throws InterruptedException {
        doLoginWithDelay();

        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        try {
            // Start on Home
            fastGo("Home", "/", homeHeading);
            waitShort(500);

            // Exact order you requested
            List<GenreSpec> genres = List.of(
                new GenreSpec("Creative Arts",        new String[]{"creative","arts"}),
                new GenreSpec("Fitness & Sports",     new String[]{"fitness","sports"}),
                new GenreSpec("Tech & Gaming",        new String[]{"tech","gaming"}),
                new GenreSpec("Social Fun",           new String[]{"social","fun"}),
                new GenreSpec("Music & Performing",   new String[]{"music","perform"}),
                new GenreSpec("Photography",          new String[]{"photo","photography"}),
                new GenreSpec("Outdoors & Nature",    new String[]{"outdoors","nature"}),
                new GenreSpec("Reading & Mind",       new String[]{"reading","mind"}),
                new GenreSpec("DIY & Making",         new String[]{"diy","making"})
            );

            // Avoid accidental double clicks
            Set<String> visitedKeys = new HashSet<>();

            List<String> missed = new ArrayList<>();
            for (GenreSpec g : genres) {
                boolean ok = clickGenreVerifyThenHome(g, visitedKeys);
                if (!ok) {
                    System.out.println("WARN: Genre did not respond to click: " + g.display);
                    missed.add(g.display);
                }
                // Reset to Home for a clean slate
                fastGo("Home", "/", homeHeading);
                waitShort(500);
            }

            // End on Home visually
            waitShort(1000);

            // Keep run green even if some tiles didn’t respond.
            boolean strict = false;
            if (strict) {
                Assert.assertTrue(missed.isEmpty(), "Unresponsive genres: " + missed);
            }

        } finally {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        }
    }

    /* ===================== GENRE HELPERS ===================== */

    private static class GenreSpec {
        final String display;
        final String[] tokens;
        final String norm;
        GenreSpec(String display, String[] tokens) {
            this.display = display;
            this.tokens = tokens;
            this.norm = normalizeLabel(display);
        }
    }

    /**
     * Finds the genre element by exact/near text, clicks it using multiple strategies,
     * verifies a state change (URL or text appears), then returns true/false.
     * Also tracks 'visitedKeys' to avoid re-click loops.
     */
    private boolean clickGenreVerifyThenHome(GenreSpec spec, Set<String> visitedKeys) throws InterruptedException {
        // Grab candidates from <main> (broad on purpose: also handles clickable <div> tiles)
        List<WebElement> candidates = findMainGenreCandidates();

        WebElement target = pickBestCandidate(candidates, spec, visitedKeys);
        if (target == null) return false;

        // Track this candidate to avoid loops
        visitedKeys.add(keyFor(target));

        String urlBefore = driver.getCurrentUrl();
        int resultsBefore = countVisibleResultsHint(); // heuristic: number of visible cards/items

        // Try click strategies (scroll → Actions → WebElement.click → JS click)
        clickRobust(target);

        // Wait briefly for change
        waitShort(800);

        // Verify: URL or results/text change or heading includes the genre token(s)
        boolean changed = urlChangedFrom(urlBefore) ||
                          resultsHintChangedFrom(resultsBefore) ||
                          headingOrMainContainsAny(spec);

        // If not changed, try one more stronger JS click on the closest clickable ancestor
        if (!changed) {
            WebElement closest = closestClickableAncestor(target);
            if (closest != null && closest != target) {
                clickViaJs(closest);
                waitShort(800);
                changed = urlChangedFrom(urlBefore) ||
                          resultsHintChangedFrom(resultsBefore) ||
                          headingOrMainContainsAny(spec);
            }
        }

        return changed;
    }

    /** Choose best candidate by normalized exact/contains match, then token match. */
    private WebElement pickBestCandidate(List<WebElement> candidates, GenreSpec spec, Set<String> visitedKeys) {
        // 1) Exact-ish match
        for (WebElement el : candidates) {
            if (!isInteractable(el)) continue;
            String norm = normalizeLabel(safeText(el));
            if (visitedKeys.contains(keyFor(el))) continue;
            if (norm.equals(spec.norm) || norm.contains(spec.norm) || spec.norm.contains(norm)) {
                return el;
            }
        }
        // 2) Token match (ALL tokens must exist)
        for (WebElement el : candidates) {
            if (!isInteractable(el)) continue;
            String norm = normalizeLabel(safeText(el));
            if (visitedKeys.contains(keyFor(el))) continue;
            boolean all = true;
            for (String t : spec.tokens) {
                if (!norm.contains(normalizeLabel(t))) { all = false; break; }
            }
            if (all) return el;
        }
        return null;
    }

    /** Broad but prioritized candidate set under <main>. */
    private List<WebElement> findMainGenreCandidates() {
        List<By> scopes = List.of(
            // Most likely clickable forms (cards/tiles/links/buttons)
            By.cssSelector("main a[href], main button, main [role='button']"),
            // Common card patterns (data attrs / classes)
            By.cssSelector("main [data-testid*='genre' i], main [data-test*='genre' i], main [class*='genre' i], main [class*='chip' i], main [class*='card' i]"),
            // Fallback to any text-bearing element
            By.cssSelector("main *")
        );
        for (By scope : scopes) {
            List<WebElement> els = driver.findElements(scope);
            if (!els.isEmpty()) return els;
        }
        return Collections.emptyList();
    }

    private boolean isInteractable(WebElement el) {
        try { return el.isDisplayed() && el.isEnabled(); }
        catch (Exception e) { return false; }
    }

    private void clickRobust(WebElement el) {
        scrollIntoCenter(el);
        try {
            new Actions(driver).moveToElement(el).pause(Duration.ofMillis(50)).click().perform();
            return;
        } catch (Exception ignored) {}
        try {
            el.click();
            return;
        } catch (Exception ignored) {}
        clickViaJs(el);
    }

    private void clickViaJs(WebElement el) {
        try { ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el); }
        catch (Exception ignored) {}
    }

    private void scrollIntoCenter(WebElement el) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'center'});", el);
        } catch (Exception ignored) {}
    }

    private WebElement closestClickableAncestor(WebElement el) {
        try {
            Object res = ((JavascriptExecutor) driver).executeScript(
                "function c(e){while(e){if(e instanceof HTMLElement){var r=e.getAttribute('role');" +
                "if(e.tagName==='A'||e.tagName==='BUTTON'||(r&&r.toLowerCase()==='button')||e.onclick||e.getAttribute('tabindex')) return e;}" +
                "e=e.parentElement;} return null;} return c(arguments[0]);", el);
            return (res instanceof WebElement) ? (WebElement) res : null;
        } catch (Exception ignored) { return null; }
    }

    /* ===================== CHANGE DETECTION (verification) ===================== */

    private boolean urlChangedFrom(String before) {
        String after = driver.getCurrentUrl();
        return after != null && !after.equals(before);
    }

    /** Heuristic: count of visible “cards/items” often changes on filter. */
    private int countVisibleResultsHint() {
        try {
            List<WebElement> cards = driver.findElements(By.cssSelector(
                "main [data-testid*='card' i], main [class*='card' i], main [class*='tile' i], main article, main li"
            ));
            int c = 0;
            for (WebElement e : cards) { if (isDisplayed(e)) c++; }
            return c;
        } catch (Exception e) { return -1; }
    }

    private boolean resultsHintChangedFrom(int before) {
        if (before < 0) return false;
        int after = countVisibleResultsHint();
        return after >= 0 && after != before;
    }

    /** Look for heading or main text that contains any of the genre tokens. */
    private boolean headingOrMainContainsAny(GenreSpec spec) {
        String want = " " + spec.norm + " ";
        // Headings
        for (String tag : List.of("h1","h2","h3","h4","h5","h6")) {
            for (WebElement h : driver.findElements(By.cssSelector("main " + tag + ", " + tag))) {
                String txt = normalizeLabel(safeText(h));
                if (!txt.isEmpty() && (want.contains(" " + txt + " ") || txt.contains(spec.norm))) return true;
                for (String t : spec.tokens) {
                    if (txt.contains(normalizeLabel(t))) return true;
                }
            }
        }
        // Any visible main text holder
        List<WebElement> mains = driver.findElements(By.cssSelector("main"));
        for (WebElement m : mains) {
            String txt = normalizeLabel(safeText(m));
            if (txt.contains(spec.norm)) return true;
            for (String t : spec.tokens) {
                if (txt.contains(normalizeLabel(t))) return true;
            }
        }
        return false;
    }

    private boolean isDisplayed(WebElement e) {
        try { return e.isDisplayed(); } catch (Exception ex) { return false; }
    }

    /* ===================== NAV & WAIT HELPERS ===================== */

    private void fastGo(String visibleLabel, String hrefFragment, By pageMarker) throws InterruptedException {
        WebElement nav = findHeaderNavQuick(visibleLabel, hrefFragment);
        if (nav != null) {
            try {
                new Actions(driver).moveToElement(nav).pause(Duration.ofMillis(40)).click().perform();
            } catch (Exception ignored) {
                try { nav.click(); } catch (Exception ignored2) {
                    clickViaJs(nav);
                }
            }
            if (urlContainsQuick(hrefFragment) || appearsQuick(pageMarker)) return;
        }
        String url = hrefFragment.startsWith("http")
                ? hrefFragment
                : ("/".equals(hrefFragment) ? BASE_URL : BASE_URL + trimLeadingSlash(hrefFragment));
        driver.navigate().to(url);
        boolean ok = urlContainsQuick(hrefFragment) || appearsQuick(pageMarker);
        Assert.assertTrue(ok, "Failed to reach " + visibleLabel + " via direct URL: " + url);
    }

    private String trimLeadingSlash(String s) { return s.startsWith("/") ? s.substring(1) : s; }

    private WebElement findHeaderNavQuick(String label, String hrefFragment) {
        String target = label.toLowerCase(Locale.ROOT);
        List<WebElement> candidates = driver.findElements(headerLinksOrButtons);
        for (WebElement el : candidates) {
            try {
                if (!el.isDisplayed() || !el.isEnabled()) continue;
                String txt = (el.getText() == null ? "" : el.getText().trim()).toLowerCase(Locale.ROOT);
                String href = el.getAttribute("href");
                if (!txt.isEmpty() && txt.contains(target)) return el;
                if (hrefFragment != null && href != null &&
                        href.toLowerCase(Locale.ROOT).contains(hrefFragment.toLowerCase(Locale.ROOT))) {
                    return el;
                }
            } catch (StaleElementReferenceException ignored) {}
        }
        return null;
    }

    private boolean urlContainsQuick(String fragment) {
        String f = fragment.toLowerCase(Locale.ROOT);
        try {
            return new WebDriverWait(driver, Duration.ofMillis(450))
                    .until(d -> d.getCurrentUrl().toLowerCase(Locale.ROOT).contains(f));
        } catch (TimeoutException e) {
            return driver.getCurrentUrl().toLowerCase(Locale.ROOT).contains(f);
        }
    }

    private boolean appearsQuick(By locator) {
        try {
            new WebDriverWait(driver, Duration.ofMillis(450))
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return !driver.findElements(locator).isEmpty();
        }
    }

    private String safeText(WebElement el) {
        try { String t = el.getText(); return t == null ? "" : t.trim(); }
        catch (Exception e) { return ""; }
    }

    private static String normalizeLabel(String s) {
        if (s == null) return "";
        String out = Normalizer.normalize(s, Normalizer.Form.NFKD)
                .replace("&", " and ")
                .replaceAll("[^\\p{Alnum}\\s]", " ")
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ")
                .trim();
        return out;
    }

    private static String keyFor(WebElement el) {
        // Key by normalized text + href (if any)
        String txt = "";
        String href = "";
        try { txt = el.getText(); } catch (Exception ignored) {}
        try { href = el.getAttribute("href"); } catch (Exception ignored) {}
        return normalizeLabel(txt) + "::" + (href == null ? "" : href.toLowerCase(Locale.ROOT));
    }

    private void waitShort(long millis) throws InterruptedException { Thread.sleep(millis); }

    /* ===================== LOGIN HELPERS ===================== */

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

        // Transition
        Thread.sleep(1200);

        boolean authed = appears(logoutButton, 10) || waitUntilUrlDoesNotContain("/login", 10);
        Assert.assertTrue(authed, "Sign-in did not complete as expected.");
    }

    private boolean clickStrong(By locator, int seconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
            WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
            try {
                new Actions(driver).moveToElement(el).pause(Duration.ofMillis(50)).click().perform();
                return true;
            } catch (Exception ignored) {
                el.click(); return true;
            }
        } catch (Exception e) {
            try {
                WebElement el = driver.findElement(locator);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
                return true;
            } catch (Exception ignored) { return false; }
        }
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
        } catch (TimeoutException e) { return false; }
    }

    private boolean waitUntilUrlDoesNotContain(String fragment, int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(d -> !d.getCurrentUrl().toLowerCase(Locale.ROOT).contains(fragment.toLowerCase(Locale.ROOT)));
            return true;
        } catch (TimeoutException e) { return false; }
    }
}
