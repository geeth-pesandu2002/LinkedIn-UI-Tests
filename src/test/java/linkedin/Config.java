package linkedin;

public final class Config {

    // Your dedicated TEST account. Set these as environment variables (see README step).
    public static final String EMAIL = System.getenv("LINKEDIN_EMAIL");
    public static final String PASSWORD = System.getenv("LINKEDIN_PASSWORD");

    // Search term for Tasks 3 and 4. Can be overridden with LINKEDIN_SEARCH.
    public static final String SEARCH_TERM =
            System.getenv().getOrDefault("LINKEDIN_SEARCH", "cyber security");

    public static final String LOGIN_URL = "https://www.linkedin.com/login";
    public static final int TIMEOUT_SECONDS = 30;

    private Config() { }
}