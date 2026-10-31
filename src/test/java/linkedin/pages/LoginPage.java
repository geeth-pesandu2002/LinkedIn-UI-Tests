package linkedin.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import linkedin.Config;

public class LoginPage extends BasePage {

    // Dynamic input selectors
    private final By usernameSelector = By.cssSelector(
            "input[type='email'], input#username, input[name='session_key'], input[type='text']");
    private final By passwordSelector = By.cssSelector(
            "input[type='password'], input#password, input[name='session_password']");

    // Exact match for the blue "Sign in" button at the bottom (excluding "Sign in with Microsoft/Apple")
    private final By signInSelector = By.xpath(
            "//button[normalize-space()='Sign in' and not(contains(., 'Microsoft')) and not(contains(., 'Apple'))] | " +
            "//button[@type='submit' and not(contains(., 'Microsoft')) and not(contains(., 'Apple'))]");

    // Error message selectors
    private final By errorMessage = By.xpath(
            "//*[@id='error-for-password' or @id='error-for-username' or contains(@class, 'error') or @role='alert' " +
            "or contains(text(), 'Wrong email or password') or contains(text(), 'not the right password') " +
            "or contains(normalize-space(), 'Wrong email or password')]");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Finds the first visible element matching the given locator.
     */
    private WebElement getVisibleElement(By locator) {
        return wait.until(d -> {
            for (WebElement el : d.findElements(locator)) {
                try {
                    if (el.isDisplayed() && el.isEnabled()) {
                        return el;
                    }
                } catch (Exception ignored) {}
            }
            return null;
        });
    }

    public LoginPage open() {
        driver.get(Config.LOGIN_URL);
        getVisibleElement(usernameSelector);
        return this;
    }

    public void login(String username, String password) {
        WebElement emailField = getVisibleElement(usernameSelector);
        emailField.clear();
        emailField.sendKeys(username);

        WebElement passField = getVisibleElement(passwordSelector);
        passField.clear();
        passField.sendKeys(password);

        // Find and click the visible Sign in button, with ENTER key as a reliable fallback
        WebElement submitBtn = null;
        try {
            submitBtn = new WebDriverWait(driver, Duration.ofSeconds(5)).until(d -> {
                for (WebElement el : d.findElements(signInSelector)) {
                    try {
                        if (el.isDisplayed() && el.isEnabled()) {
                            return el;
                        }
                    } catch (Exception ignored) {}
                }
                return null;
            });
        } catch (Exception ignored) {}

        if (submitBtn != null) {
            try {
                submitBtn.click();
            } catch (Exception e) {
                // If standard click is intercepted or blocked, click via JavaScript
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitBtn);
            }
        } else {
            // Direct form submission using ENTER key
            passField.sendKeys(Keys.ENTER);
        }

        pauseIfSecurityCheck();
    }

    /** True if an error message is shown (used by the negative login test). */
    public boolean isErrorDisplayed() {
        try {
            return wait.until(d -> {
                for (WebElement el : d.findElements(errorMessage)) {
                    try {
                        if (el.isDisplayed()) {
                            return true;
                        }
                    } catch (Exception ignored) {}
                }
                return false;
            });
        } catch (TimeoutException e) {
            return false;
        }
    }
}
