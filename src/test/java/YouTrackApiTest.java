import dto.Issue;
import dto.IssueRequest;
import dto.Project;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.jupiter.api.Assertions.*;

public class YouTrackApiTest {
    private static final String BASE_URL =
            "http://localhost:8081/api";

    private static String token;

    @BeforeAll
    public static void setUp() {
        token = System.getenv("YOUTRACK_TOKEN");
        assertNotNull(token, "Токен не задан");
    }
    //Позитивные тесты
    @Test
    public void testGetProjects(){
        Project[] projects =
                given()
                        .baseUri(BASE_URL)
                        .header("Authorization","Bearer " + token)
                        .accept(ContentType.JSON)
                .when()
                        .get("/admin/projects?fields=id,name,shortName")

                .then()
                        .statusCode(200)
                        .body(matchesJsonSchemaInClasspath("schemas/project-schema.json"))
                        .extract()
                        .as(Project[].class);

        for (Project project : projects) {
            System.out.println("Проект:");
            System.out.println("ID: " + project.getId());
            System.out.println("Название: " + project.getName());
            System.out.println("Короткое имя: " + project.getShortName());
            System.out.println("--------------------");
        }
    }

    @Test
    public void testGetProjectById(){
        Project project =
                given()
                        .baseUri(BASE_URL)
                        .header("Authorization", "Bearer " + token)
                        .accept(ContentType.JSON)
                .when()
                        .get("/admin/projects/0-1?fields=id,name,shortName")
                .then()
                        .statusCode(200)
                        .extract()
                        .as(Project.class);

        assertEquals("0-1", project.getId());
        assertEquals("qa_rest_assure", project.getName());

        System.out.println("Полученный проект: " + project.getName()+"\n");
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
                        .baseUri(BASE_URL)
                        .header("Authorization", "Bearer " + token)
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .body(request)
                .when()
                        .post("/issues?fields=id,idReadable,summary,description,project(id)")

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
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + token)
                .accept(ContentType.JSON)
        .when()
                .get("/issues/QA_-99999")
        .then()
                .statusCode(404);

        System.out.println("Несуществующая задача возвращает 404");
    }

    @Test
    public  void testCreateIssueWithoutSummary(){
        IssueRequest request = new IssueRequest(
                "",
                "Задача без названия",
                "0-1"
        );
        given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
        .when()
                .post("/issues")
        .then()
                .statusCode(400);
        System.out.println("YouTrack отклонил задачу без summary");
    }
}