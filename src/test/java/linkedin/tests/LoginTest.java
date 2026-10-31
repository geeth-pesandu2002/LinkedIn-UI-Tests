package linkedin.tests;

import linkedin.BaseUiTest;
import linkedin.Config;
import linkedin.pages.HomePage;
import linkedin.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseUiTest {

    @Test(description = "TC-01: Valid login reaches the feed / navigation bar")
    public void validLoginSucceeds() {
        HomePage home = loginAsValidUser();
        Assert.assertTrue(home.isLoggedIn(),
                "Login failed: feed URL and navigation bar were not shown");
    }

    @Test(description = "TC-02: Invalid password shows an error message")
    public void invalidPasswordShowsError() {
        LoginPage login = new LoginPage(driver).open();
        login.login(Config.EMAIL, "WrongPassword!123");
        Assert.assertTrue(login.isErrorDisplayed(),
                "Expected an error message for an invalid password, but none was shown");
    }
}