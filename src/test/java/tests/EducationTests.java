
package tests;

import helpers.AuthHelper;
import helpers.Config;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Education Tests")

public class EducationTests {

    @Test
    @DisplayName("Successfully add education with JSON schema validation")
    @Story("Add Education With Schema Validation")
    void AddEducationSuccessfully() {
        String token = AuthHelper.getToken();
        String body = """
                {
                  "school": "Test University",
                  "degree": "Bachelor",
                  "fieldofstudy": "Computer Science",
                  "from": "2021-07-09",
                  "to": "2024-07-08",
                  "current": false,
                  "description": "Test education"
                }
                """;

        Response response = given()
                .baseUri(Config.URL)
                .header("x-auth-token", token)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put("/api/profile/education")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/education-put-response-schema.json")) // 💡 проверка по схеме
                .extract().response();

        Allure.step("Проверка, что возвращается 200 и тело соответствует схеме");
    }

    @Test
    @DisplayName("Fail to add education without authentication token")
    @Story("Add Education Without Token")
    void NotAddEducationWithoutToken() {
        String body = """
                    {
                        "school": "Test University",
                        "degree": "Bachelor",
                        "fieldofstudy": "Computer Science",
                        "from": "2021-07-09",
                        "to": "2024-07-08",
                        "current": false,
                        "description": "Test education"
                    }
                """;
        Response response = given()
                .baseUri(Config.URL)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put("/api/profile/education")
                .then()
                .log().all()
                .extract().response();

        Allure.step("Проверка, что без токена возвращается 401", () -> {
            assertEquals(401, response.statusCode());
            String message = response.jsonPath().getString("msg");
            assertTrue(message.toLowerCase().contains("authorization denied"));
        });

    }

    @Test
    @DisplayName("Fail to delete education without authentication token")
    @Story("Delete Education Without Token")
    void NotDeleteEducationWithoutToken() {
        Response response = given()
                .baseUri(Config.URL)
                .when()
                .delete("/api/profile/education/someId")
                .then()
                .log().all()
                .extract().response();

        Allure.step("Проверка, что удаление без токена запрещено", () -> {
            assertEquals(401, response.statusCode());
            String message = response.jsonPath().getString("msg");
            assertTrue(message.toLowerCase().contains("authorization denied"));

        });
    }
}