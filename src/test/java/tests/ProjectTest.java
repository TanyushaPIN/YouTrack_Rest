package tests;

import dto.Project;
import endpoints.Endpoints;
import org.junit.jupiter.api.Test;
import specifications.Specifications;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProjectTest extends BaseTest{
    //Позитивные тесты
    @Test
    public void testGetProjects() {
        Project[] projects =
                given()
                        .spec(Specifications.requestSpecification(token))
                .when()
                        .get(Endpoints.PROJECTS + "?fields=id,name,shortName")
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
    public void testGetProjectById() {
        Project project =
                given()
                        .spec(Specifications.requestSpecification(token))
                .when()
                        .get(Endpoints.PROJECT_BY_ID + "?fields=id,name,shortName")
                .then()
                        .statusCode(200)
                        .extract()
                        .as(Project.class);

        assertEquals("0-1", project.getId());
        assertEquals("qa_rest_assure", project.getName());

        System.out.println("Полученный проект: " + project.getName() + "\n");
    }
}
