import config.TestPropertiesConfig;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Map;

public class HomePageTest {
    WebDriver driver;
    TestPropertiesConfig config = ConfigFactory.create(TestPropertiesConfig.class, System.getProperties());

    @BeforeEach
    public void setUp() {
        driver = initDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(6));
        driver.get(config.getBaseUrl());
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    private WebDriver initDriver() {
        String remoteUrl = System.getenv("SELENIUM_REMOTE_URL");

        if (remoteUrl != null && !remoteUrl.isEmpty()) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");  // Add headless mode
            options.addArguments("--disable-gpu"); // Switch off GPU, because we don't need it in headless mode
            options.addArguments("--no-sandbox"); // Switch off sandbox to prevent access rights issues
            options.addArguments("--disable-dev-shm-usage");
            options.setCapability("goog:loggingPrefs", Map.of("browser", "ALL"));
            try {
                driver = new RemoteWebDriver(new URL(remoteUrl), options);
            } catch (MalformedURLException e) {
                throw new RuntimeException("Malformed URL for Selenium Remote WebDriver", e);
            }
        } else {
            driver = new ChromeDriver();
        }
        return driver;
    }

    @Test
    public void testTitle() {
        Assertions.assertEquals("Hands-On Selenium WebDriver with Java", driver.getTitle());
    }

    @Test
    public void testWebForm() {
        driver.findElement(By.cssSelector("a[href='web-form.html']")).click();

        Assertions.assertEquals("Web form", driver.findElement(By.cssSelector("h1.display-6")).getText());
    }

    @Test
    public void testDropDownPage() throws InterruptedException {
        Thread.sleep(6000);
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='dropdown-menu.html']")))
                .click();

        Assertions.assertEquals("Dropdown menu", driver.findElement(By.cssSelector("h1.display-6")).getText());
    }
}
