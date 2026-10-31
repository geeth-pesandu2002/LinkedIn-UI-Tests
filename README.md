# LinkedIn UI Test Suite

Automated UI tests for LinkedIn using **Selenium WebDriver 4**, **TestNG**, and **Java 17+**.  
Covers: Login (positive & negative), Search, and Clicking a search result.

---

## Prerequisites

| Tool | Version |
|------|---------|
| Java | 17 or later |
| Maven | 3.6 or later |
| Chrome | Latest stable |

> Selenium Manager (bundled in Selenium 4.6+) downloads ChromeDriver automatically — no manual driver setup needed.

---

## 1. Configure Credentials

The tests read credentials from **environment variables** — never hardcode them.

### Windows (PowerShell) — set once, persists across sessions:
```powershell
[System.Environment]::SetEnvironmentVariable("LINKEDIN_EMAIL",    "your@email.com", "User")
[System.Environment]::SetEnvironmentVariable("LINKEDIN_PASSWORD", "yourPassword",   "User")
```

After setting, **restart your terminal / IDE** so the new values are loaded.

### Optional — override the search term (default: `software testing`):
```powershell
[System.Environment]::SetEnvironmentVariable("LINKEDIN_SEARCH", "Elon Musk", "User")
```

---

## 2. Run the Tests

```bash
mvn test
```

This runs all three test classes (Login, Search, ClickResult) via TestNG.

### Run a single test class:
```bash
mvn test -Dtest=LoginTest
mvn test -Dtest=SearchTest
mvn test -Dtest=ClickResultTest
```

---

## 3. Test Reports

| Report | Location |
|--------|----------|
| TestNG HTML report | `target/surefire-reports/index.html` |
| Emailable summary | `target/surefire-reports/emailable-report.html` |
| Auto-screenshots (on failure) | `screenshots/` folder in project root |

Open `target/surefire-reports/index.html` in a browser after the run.

---

## 4. CAPTCHA / Security Checkpoints

LinkedIn may show a CAPTCHA or verification step. If this happens:
- The browser will **pause automatically** (up to 2 minutes).
- **Complete the check manually** in the open Chrome window.
- The test will resume once the checkpoint URL disappears.

Document any CAPTCHA encounters in your test report as required by the assignment.

---

## 5. Project Structure

```
linkedin-ui-tests/
├── src/test/java/linkedin/
│   ├── Config.java               # Constants and env-var credentials
│   ├── BaseUiTest.java           # @BeforeMethod / @AfterMethod, screenshot helper
│   ├── pages/
│   │   ├── BasePage.java         # Shared WebDriverWait, security checkpoint handler
│   │   ├── LoginPage.java        # Login page POM
│   │   ├── HomePage.java         # Home/feed page POM (search box)
│   │   └── SearchResultsPage.java# Search results POM
│   └── tests/
│       ├── LoginTest.java        # TC-01 valid login, TC-02 invalid password
│       ├── SearchTest.java       # TC-03 search returns results
│       └── ClickResultTest.java  # TC-04 first result opens correct page
├── src/test/resources/
│   └── testng.xml                # TestNG suite definition
├── screenshots/                  # Auto-captured on test failure
├── pom.xml
└── README.md
```

