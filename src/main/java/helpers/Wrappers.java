package helpers;

import io.qameta.allure.Allure;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class Wrappers {

    public static Response login(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);

        Response response = given()
                .filter(CustomAllureListener.withCustomTemplates()) // 🧩 шаблоны Allure
                .log().all() // ⬅️ логируем весь запрос
                .contentType(JSON)
                .body(request)
                .when()
                .post("/api/auth")
                .then()
                .log().all() // ⬅️ логируем весь ответ
                .extract().response();
        Allure.addAttachment("Request body", "application/json",
                String.format("{\"email\":\"%s\", \"password\":\"%s\"}", email, password), ".json");

        Allure.addAttachment("Response body", "application/json", response.asPrettyString(), ".json");

        return response;
    }
}
