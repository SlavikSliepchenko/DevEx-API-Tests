package helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.dto.LoginRequest;
import io.qameta.allure.Allure;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class Wrappers {

    public static Response login(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);

        Response response = given()
                .filter(CustomAllureListener.withCustomTemplates())
                .log().all()
                .contentType(JSON)
                .body(request)
                .when()
                .post("/api/auth")
                .then()
                .log().all()
                .extract().response();
        Allure.addAttachment("Request body", "application/json",
                String.format("{\"email\":\"%s\", \"password\":\"%s\"}", email, password), ".json");

        Allure.addAttachment("Response body", "application/json", response.asPrettyString(), ".json");

        return response;
    }

    public static Response postJson(String endpoint, Object body) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String requestBody = mapper.writeValueAsString(body);

            Allure.addAttachment("Request JSON", "application/json", requestBody, ".json");

            Response response = given()
                    .baseUri(Config.URL)
                    .contentType(ContentType.JSON)
                    .filter(CustomAllureListener.withCustomTemplates())
                    .log().all()
                    .body(requestBody)
                    .post(endpoint)
                    .then()
                    .log().all()
                    .extract().response();

            Allure.addAttachment("Response JSON", "application/json", response.asPrettyString(), ".json");

            return response;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка сериализации объекта в JSON", e);
        }
    }

    public static Response getWithToken(String endpoint, String token) {
        return given()
                .baseUri(Config.URL)
                .header("x-auth-token", token)
                .contentType(ContentType.JSON)
                .filter(CustomAllureListener.withCustomTemplates())
                .log().all()
                .when()
                .get(endpoint)
                .then()
                .log().all()
                .extract().response();
    }

    public static Response postJsonWithToken(String endpoint, Object body, String token) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String requestBody = mapper.writeValueAsString(body);

            Allure.addAttachment("Request JSON", "application/json", requestBody, ".json");

            Response response = given()
                    .baseUri(Config.URL)
                    .header("x-auth-token", token)
                    .contentType(ContentType.JSON)
                    .filter(CustomAllureListener.withCustomTemplates())
                    .log().all()
                    .body(requestBody)
                    .post(endpoint)
                    .then()
                    .log().all()
                    .extract().response();

            Allure.addAttachment("Response JSON", "application/json", response.asPrettyString(), ".json");

            return response;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка сериализации JSON", e);
        }
    }

    public static Response deleteWithToken(String path, String token) {
        RequestSpecification spec = given();

        if (token != null && !token.isEmpty()) {
            spec.header("Authorization", token);
        }

        return spec
                .when()
                .delete(path)
                .then()
                .log().all()
                .extract().response();
    }
}
