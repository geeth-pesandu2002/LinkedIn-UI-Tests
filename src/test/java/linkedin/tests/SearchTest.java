package linkedin.tests;

import linkedin.BaseUiTest;
import linkedin.Config;
import linkedin.pages.HomePage;
import linkedin.pages.SearchResultsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SearchTest extends BaseUiTest {

    @Test(description = "TC-03: Searching shows a results page with visible results")
    public void searchShowsResults() {
        HomePage home = loginAsValidUser();
        SearchResultsPage results = home.search(Config.SEARCH_TERM);

        Assert.assertTrue(results.isLoaded(), "URL is not a search results page");
        Assert.assertTrue(results.hasResults(), "No result elements were visible");
    }
}