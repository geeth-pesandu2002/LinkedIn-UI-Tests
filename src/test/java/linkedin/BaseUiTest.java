package linkedin;

import linkedin.pages.LoginPage;
import linkedin.pages.HomePage;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class BaseUiTest {

    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        if (Config.EMAIL == null || Config.PASSWORD == null) {
            throw new SkipException(
                "Set LINKEDIN_EMAIL and LINKEDIN_PASSWORD environment variables. See README.");
        }

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");

        // Prevent LinkedIn from detecting the automated browser
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);

        // Disable "Save password?" prompt and credentials popup
        java.util.Map<String, Object> prefs = new java.util.HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        driver = new ChromeDriver(options);

        // Remove the navigator.webdriver flag so sites cannot detect Selenium
        ((ChromeDriver) driver).executeCdpCommand(
                "Page.addScriptToEvaluateOnNewDocument",
                java.util.Map.of("source",
                        "Object.defineProperty(navigator, 'webdriver', {get: () => undefined});"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (driver == null) {
            return;
        }
        // Always take a screenshot (for the report) — on failure AND success
        takeScreenshot(result.getName());
        driver.quit();
    }

    /** Logs in with the valid test account and returns the home page. */
    protected HomePage loginAsValidUser() {
        new LoginPage(driver).open().login(Config.EMAIL, Config.PASSWORD);
        return new HomePage(driver);
    }

    /** Saves a PNG to the screenshots/ folder in the project root. */
    protected void takeScreenshot(String testName) {
        try {
            String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
            Path dir = Paths.get("screenshots");
            Files.createDirectories(dir);
            Path file = dir.resolve(testName + "-" + stamp + ".png");
            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Files.write(file, bytes);
            System.out.println("Screenshot saved: " + file.toAbsolutePath());
        } catch (Exception e) {
            System.err.println("Could not save screenshot: " + e.getMessage());
        }
    }
}
