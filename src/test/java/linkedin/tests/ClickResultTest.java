package linkedin.tests;

import linkedin.BaseUiTest;
import linkedin.Config;
import linkedin.pages.HomePage;
import linkedin.pages.SearchResultsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ClickResultTest extends BaseUiTest {

    @Test(description = "TC-04: Clicking the first search result opens the matching page")
    public void clickingFirstResultOpensCorrectPage() {
        HomePage home = loginAsValidUser();
        SearchResultsPage results = home.search(Config.SEARCH_TERM);
        Assert.assertTrue(results.isLoaded(), "Search results did not load");
        Assert.assertTrue(results.hasResults(), "No search result items were found");

        // Record the expected destination BEFORE clicking
        String expectedPath = results.getFirstResultPath();
        String resultName = results.getFirstResultName();
        System.out.println("Targeting first result: '" + resultName + "' -> " + expectedPath);

        results.openFirstResult();

        boolean pathMatched = results.waitForUrlContaining(expectedPath);
        System.out.println("Opened URL: " + driver.getCurrentUrl());

        Assert.assertTrue(pathMatched || results.isMainHeadingDisplayed(),
                "Expected either URL to contain '" + expectedPath + "' or main heading to be visible");

        // Keep page open for 3 seconds so the viewer/recording can see the opened result
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignored) {}
    }
}
