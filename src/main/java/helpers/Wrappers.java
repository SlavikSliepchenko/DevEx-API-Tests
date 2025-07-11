package helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.dto.LoginRequest;
import io.qameta.allure.Allure;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static helpers.Config.URL;
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
                    .baseUri(URL)
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
                .baseUri(URL)
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
                    .baseUri(URL)
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

    public static Response postJson(String endpoint, String token, Object body) {
        try {
            String requestBody = new ObjectMapper().writeValueAsString(body);

            Allure.addAttachment("Request JSON", "application/json", requestBody, ".json");

            Response response = given()
                    .contentType(ContentType.JSON)
                    .header("x-auth-token", token)
                    .filter(CustomAllureListener.withCustomTemplates())
                    .log().all()
                    .body(requestBody)
                    .when()
                    .post(endpoint)
                    .then()
                    .log().all()
                    .extract().response();

            Allure.addAttachment("Response JSON", "application/json", response.asPrettyString(), ".json");

            return response;
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize request body for POST " + endpoint, e);
        }
    }

    public static ValidatableResponse patchJson(String endpoint, String token, Object body) {
        try {
            String requestBody = new ObjectMapper().writeValueAsString(body);

            Allure.addAttachment("PATCH Request JSON", "application/json", requestBody, ".json");

            Response response = given()
                    .header("x-auth-token", token)
                    .contentType(ContentType.JSON)
                    .filter(CustomAllureListener.withCustomTemplates())
                    .log().all()
                    .body(requestBody)
                    .when()
                    .patch(endpoint)
                    .then()
                    .log().all()
                    .extract()
                    .response();

            Allure.addAttachment("PATCH Response JSON", "application/json", response.asPrettyString(), ".json");

            return response.then();
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize request body for PATCH " + endpoint, e);
        }
    }


    public static Response deleteWithInvalidID(String path, String token, Object... pathParams) {
        RequestSpecification spec = given()
                .filter(CustomAllureListener.withCustomTemplates())
                .log().all();

        if (token != null && !token.isEmpty()) {
            spec.header("x-auth-token", token);
        }

        Response response = spec
                .when()
                .delete(path, pathParams)
                .then()
                .log().all()
                .extract().response();

        Allure.addAttachment("DELETE Response JSON", "application/json", response.asPrettyString(), ".json");

        return response;
    }


}
