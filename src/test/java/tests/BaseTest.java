package tests;

import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class BaseTest {
    protected static String token;

    @BeforeAll
    public static void setUp() {
        token = System.getenv("YOUTRACK_TOKEN");
        assertNotNull(token, "Токен не задан");
    }

}
