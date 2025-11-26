package hobbyexplorer;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Homepagetest {

    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        System.setProperty("webdriver.chrome.driver",
            "/opt/homebrew/Caskroom/chromedriver/139.0.7258.66/chromedriver-mac-arm64/chromedriver");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-blink-features=AutomationControlled");

        driver = new ChromeDriver(options);
    }

    @Test
    public void openHomePage() throws InterruptedException {
        driver.get("https://preview--hobby-quest-unlocked.lovable.app/");

        // Wait 3 seconds so you can see the homepage before it closes
        Thread.sleep(3000);

        System.out.println("Opened homepage: " + driver.getCurrentUrl());
    }

    @Test
    public void clickQuizLink() throws InterruptedException {
        driver.get("https://preview--hobby-quest-unlocked.lovable.app/");
        driver.findElement(By.cssSelector("a[href='/quiz']")).click();

        // Wait 5 seconds so you can see the quiz page before it closes
        Thread.sleep(5000);

        System.out.println("Opened quiz page: " + driver.getCurrentUrl());
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
