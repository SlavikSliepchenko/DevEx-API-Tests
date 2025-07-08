package tests;

import helpers.Config;
import helpers.CustomAllureListener;
import helpers.Wrappers;
import io.qameta.allure.*;
import io.qameta.allure.junit5.AllureJunit5;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@Epic("User")
@Feature("Authentication")
@ExtendWith(AllureJunit5.class)
public class UserTests {

    @Test
    @Story("Login")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Успешный логин с валидным email и паролем")
    @Description("Проверка кода ответа 200, наличия токена и соответствия JSON-схеме")
    public void shouldLoginSuccessfully() {
        Allure.step("Отправка запроса на логин", () -> {
            Response response = Wrappers.login(Config.EMAIL, Config.PASSWORD);

            Allure.step("Проверка: статус-код == 200", () ->
                    response.then().statusCode(200)
            );

            Allure.step("Проверка: токен не null", () ->
                    response.then().body("token", notNullValue())
            );

            Allure.step("Проверка: ответ соответствует JSON-схеме", () ->
                    response.then().body(matchesJsonSchemaInClasspath("schemas/successful-login-schema.json"))
            );
        });
    }

    @Test
    @Story("Register")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Успешная регистрация пользователя")
    @Description("Регистрация пользователя с валидными данными (по данным из Postman)")
    void shouldRegisterUserSuccessfully() {

        String email = "user" + System.currentTimeMillis() + "@example.com";

        JSONObject body = new JSONObject();
        body.put("email", email);
        body.put("password", "qwertz");
        body.put("name", "Test User");
        body.put("google", "testuser");
        body.put("facebook", "fb.testuser1");
        body.put("github", "gh-testuser1");

        Allure.step("Формируем тело запроса", () -> {
            String requestBody = body.toString(2);
            System.out.println("Request body:\n" + requestBody);
            Allure.addAttachment("Request JSON", "application/json", requestBody, ".json");
        });

        Response response = Allure.step("Отправка запроса", () ->
                given()
                        .baseUri(Config.URL)
                        .contentType(JSON)
                        .filter(CustomAllureListener.withCustomTemplates()) // 🧩 Allure templates
                        .log().all() // ⬅️ лог запроса
                        .body(body.toString())
                        .post("/api/users")
                        .then()
                        .log().all() // ⬅️ лог ответа
                        .extract().response()
        );

        Allure.step("Проверка: статус-код == 200", () ->
                response.then().statusCode(200)
        );

        Allure.step("Проверка: токен присутствует", () -> {
            String token = response.jsonPath().getString("token");
            assertNotNull(token, "Токен отсутствует в ответе");
        });

        Allure.addAttachment("Response JSON", "application/json", response.asPrettyString(), ".json");

        Allure.step("Проверка: ответ соответствует JSON-схеме регистрации", () ->
                response.then().body(matchesJsonSchemaInClasspath("schemas/successful-register-schema.json"))
        );
    }

}