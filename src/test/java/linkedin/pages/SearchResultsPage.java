package linkedin.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.net.URI;

public class SearchResultsPage extends BasePage {

    // Matches search result cards or links to jobs, profiles, or companies in any search layout
    private final By resultItems = By.xpath(
            "//a[contains(@href, '/jobs/view/') or contains(@href, '/in/') or contains(@href, '/company/')] | " +
            "//*[contains(@class, 'reusable-search__result-container') or contains(@class, 'entity-result') or contains(@class, 'search-results-container')]");

    // The first clickable entity result (job, person, or company)
    private final By firstResultLink = By.xpath(
            "(//a[contains(@href, '/jobs/view/') or contains(@href, '/in/') or contains(@href, '/company/')])[1]");

    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }

    /** True if the URL is a search results URL. */
    public boolean isLoaded() {
        return waitForUrlContaining("/search/results/");
    }

    /** True if at least one result element is visible on the page. */
    public boolean hasResults() {
        try {
            return !wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(resultItems)).isEmpty();
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Returns the URL path of the first result, for example "/jobs/view/123..." or "/in/username".
     * Read before clicking so the test can verify the landing page.
     */
    public String getFirstResultPath() {
        WebElement link = wait.until(ExpectedConditions.presenceOfElementLocated(firstResultLink));
        String href = link.getAttribute("href");
        if (href == null || href.isEmpty()) {
            return "";
        }
        String path = URI.create(href).getPath();
        return path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
    }

    public String getFirstResultName() {
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(firstResultLink)).getText().trim();
        } catch (Exception e) {
            return "First Result";
        }
    }

    public void openFirstResult() {
        WebElement link = wait.until(ExpectedConditions.presenceOfElementLocated(firstResultLink));
        
        // Scroll element into center of screen before clicking
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", link);
        
        try {
            Thread.sleep(500);
            wait.until(ExpectedConditions.elementToBeClickable(link)).click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
        }

        // If LinkedIn opened the link in a new tab, switch to the active tab
        for (String windowHandle : driver.getWindowHandles()) {
            driver.switchTo().window(windowHandle);
        }
    }
}
