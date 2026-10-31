package linkedin.pages;

import linkedin.Config;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Config.TIMEOUT_SECONDS));
    }

    /** Waits until the URL contains the given text. Returns false instead of throwing. */
    public boolean waitForUrlContaining(String fragment) {
        try {
            return wait.until(ExpectedConditions.urlContains(fragment));
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Checks that a main heading (h1 or h2) is visible. */
    public boolean isMainHeadingDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(
                    org.openqa.selenium.By.xpath("//h1 | //h2"))).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * If LinkedIn shows a security checkpoint, pause and let YOU complete it by hand.
     * This does not try to bypass anything. It waits up to 2 minutes.
     */
    protected void pauseIfSecurityCheck() {
        try {
            // Briefly wait to observe if URL navigates to a security checkpoint
            new WebDriverWait(driver, Duration.ofSeconds(3)).until(d ->
                    d.getCurrentUrl().contains("checkpoint")
                            || d.getCurrentUrl().contains("challenge")
                            || d.getCurrentUrl().contains("/feed")
                            || !d.getCurrentUrl().contains("/login"));
        } catch (Exception ignored) {}

        String url = driver.getCurrentUrl();
        if (url.contains("checkpoint") || url.contains("challenge")) {
            System.out.println("LinkedIn security check at: " + url);
            System.out.println("Complete it MANUALLY in the browser. Waiting up to 2 minutes...");
            new WebDriverWait(driver, Duration.ofMinutes(2)).until(d ->
                    !d.getCurrentUrl().contains("checkpoint")
                            && !d.getCurrentUrl().contains("challenge"));
        }
    }
}
