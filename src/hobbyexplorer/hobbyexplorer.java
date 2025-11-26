package hobbyexplorer;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class hobbyexplorer {

    public static void main(String[] args) {
        // Set ChromeDriver executable path
    	System.setProperty("webdriver.chrome.driver", "/opt/homebrew/Caskroom/chromedriver/139.0.7258.66/chromedriver-mac-arm64/chromedriver");

        // Setup Chrome options to avoid automation detection
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-blink-features=AutomationControlled");

        // Initialize ChromeDriver
        WebDriver driver = new ChromeDriver(options);

        try {
            // Open your website
            driver.get("https://preview--hobby-quest-unlocked.lovable.app/");

            // Wait for page to load
            Thread.sleep(3000);


        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            // Close the browser regardless of success/failure
            driver.quit();
        }
    }
}
