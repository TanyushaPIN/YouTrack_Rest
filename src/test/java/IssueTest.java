import dto.Issue;
import dto.IssueRequest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class IssueTest {
    private static final String BASE_URL =
            "http://localhost:8081/api";

    private static String token;

    @BeforeAll
    public static void setUp() {
        token = System.getenv("YOUTRACK_TOKEN");
        assertNotNull(token, "Токен не задан");
    }


    @ParameterizedTest
    @CsvFileSource(
            resources = "/testdata/issues.csv",
            numLinesToSkip = 1
    )

    public void testCreateIssue(
            String summary,
            String description,
            String projectId
    ) {
        IssueRequest request = new IssueRequest(
                summary,
                description,
                projectId
        );

        Issue issue =
                given()
                        .spec(Specifications.requestSpecification(token))
                        .contentType(ContentType.JSON)
                        .body(request)
                .when()
                        .post(Endpoints.ISSUES + "?fields=id,idReadable,summary,description,project(id)")
                .then()
                        .statusCode(200)
                        .extract()
                        .as(Issue.class);

        assertNotNull(issue.getId());
        assertNotNull(issue.getIdReadable());

        assertEquals(summary, issue.getSummary());
        assertEquals(description, issue.getDescription());
        assertEquals(projectId, issue.getProject().getId());

        System.out.println("ТЕСТ ПРОЙДЕН: создана задача " + issue.getIdReadable());
    }

    //Негативные тесты
    @Test
    public void testGetNotCreateIssue() {

        given()
                .spec(Specifications.requestSpecification(token))
                .accept(ContentType.JSON)
        .when()
                .get(Endpoints.ISSUE_BY_ID)
        .then()
                .statusCode(404);

        System.out.println("Несуществующая задача возвращает 404");
    }

    @Test
    public void testCreateIssueWithoutSummary() {
        IssueRequest request = new IssueRequest(
                "",
                "Задача без названия",
                "0-1"
        );
        given()
                .spec(Specifications.requestSpecification(token))
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post(Endpoints.ISSUES)
        .then()
                .statusCode(400);
        System.out.println("YouTrack отклонил задачу без summary");
    }
}