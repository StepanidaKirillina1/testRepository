import config.TestPropertiesConfig;
import io.qameta.allure.Allure;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Map;

public class HomePageTest {
    WebDriver driver;
    TestPropertiesConfig configProperties = ConfigFactory.create(TestPropertiesConfig.class, System.getProperties());

    @BeforeEach
    public void setUp() {
        driver = initDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    private WebDriver initDriver() {
        String remoteUrl = System.getenv("SELENIUM_REMOTE_URL");
        if (remoteUrl == null || remoteUrl.isEmpty()) {
            remoteUrl = configProperties.getSeleniumRemoteUrl();
        }
        if (remoteUrl != null) {
            Allure.addAttachment("RemoteUrl", remoteUrl);
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");  // Add headless mode
            options.addArguments("--disable-gpu"); // Switch off GPU, because we don't need it in headless mode
            options.addArguments("--no-sandbox"); // Switch off sandbox to prevent access rights issues
            options.addArguments("--disable-dev-shm-usage"); // Use /tmp instead of /dev/shm
            options.setCapability("goog:loggingPrefs", Map.of("browser", "ALL"));
            try {
                driver = new RemoteWebDriver(new URL(remoteUrl), options);
            } catch (MalformedURLException e) {
                throw new RuntimeException("Malformed URL for Selenium Remote WebDriver", e);
            }
        } else {
            Allure.addAttachment("Local run", "No remote driver");
            driver = new ChromeDriver();
        }
        driver.manage().window().maximize();
        return driver;
    }

    @Test
    public void testTitle() {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");

        Assertions.assertEquals("Hands-On Selenium WebDriver with Java", driver.getTitle());
    }
}
