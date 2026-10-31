package linkedin.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage {

    private final By globalNav = By.id("global-nav");
    private final By searchBox = By.cssSelector(
            "input.search-global-typeahead__input, " +
            "input[placeholder*='Search'], " +
            "input[aria-label*='Search']");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    /** Login is confirmed if the URL is the feed OR the top navigation bar or search box is present. */
    public boolean isLoggedIn() {
        try {
            wait.until(d -> d.getCurrentUrl().contains("/feed")
                    || !d.findElements(globalNav).isEmpty()
                    || !d.findElements(searchBox).isEmpty()
                    || !d.findElements(By.cssSelector("nav.global-nav, .global-nav")).isEmpty());
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Clicks the global search box, types the term, and presses Enter. */
    public SearchResultsPage search(String term) {
        WebElement box = wait.until(ExpectedConditions.elementToBeClickable(searchBox));
        box.click();
        box.clear();
        box.sendKeys(term);
        box.sendKeys(Keys.ENTER);

        // If LinkedIn displays transient "Something went wrong", click "Try again"
        handleTransientErrors();

        return new SearchResultsPage(driver);
    }

    private void handleTransientErrors() {
        try {
            By tryAgain = By.xpath("//button[contains(., 'Try again') or contains(., 'Refresh')]");
            for (WebElement btn : driver.findElements(tryAgain)) {
                if (btn.isDisplayed()) {
                    System.out.println("LinkedIn showed transient error. Clicking 'Try again'...");
                    btn.click();
                    break;
                }
            }
        } catch (Exception ignored) {}
    }
}
