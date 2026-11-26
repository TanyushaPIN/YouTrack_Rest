import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Specifications {
    public static RequestSpecification requestSpecification(String token) {
        return new RequestSpecBuilder()
                .setBaseUri("http://localhost:8081/api")
                .addHeader("Authorization", "Bearer " + token)
                .setAccept(ContentType.JSON)
                .build();
    }
}
