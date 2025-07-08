package tests;

import helpers.Config;
import helpers.CustomAllureListener;
import helpers.Wrappers;
import helpers.dto.RegisterRequest;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import io.qameta.allure.junit5.AllureJunit5;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Epic("User")
@ExtendWith(AllureJunit5.class)
public class UserTests {

    @Test
    @Story("Login")
    public void shouldLoginSuccessfully() {
        Allure.step("Отправка запроса на логин", () -> {
            Response response = Wrappers.login(Config.EMAIL, Config.PASSWORD);

            Allure.step("Проверка: статус-код == 200", () ->
                    response.then().statusCode(200)
            );

            Allure.step("Проверка: токен не null", () ->
                    response.then().body("token", notNullValue())
            );
        });
    }

    @Test
    @Story("Register")
    void shouldRegisterUserSuccessfully() {
        String email = "user" + System.currentTimeMillis() + "@example.com";

        RegisterRequest request = new RegisterRequest(
                email,
                "qwertz",
                "Test User",
                "testuser",
                "fb.testuser1",
                "gh-testuser1"
        );

        Response response = Wrappers.postJson("/api/users", request);

        Allure.step("Проверка: статус-код == 200", () ->
                response.then().statusCode(200)
        );

        Allure.step("Проверка: токен присутствует", () -> {
            String token = response.jsonPath().getString("token");
            assertNotNull(token, "Токен отсутствует в ответе");
        });

    }

    @Test
    @Story("Login")
    void shouldNotLoginWithInvalidPassword() {
        JSONObject body = new JSONObject();
        body.put("email", Config.EMAIL);
        body.put("password", "Test");

        Allure.step("Формируем тело запроса", () ->
                System.out.println("Request body:\n" + body.toString(2))
        );

        Response response = Allure.step("Отправка запроса", () ->
                given()
                        .baseUri(Config.URL)
                        .contentType(JSON)
                        .filter(CustomAllureListener.withCustomTemplates())
                        .body(body.toString())
                        .post("/api/auth")
        );

        Allure.step("Проверка: статус-код == 400", () ->
                response.then().statusCode(400)
        );

        Allure.step("Проверка: структура ошибки соответствует схеме", () ->
                response.then().assertThat()
                        .body(matchesJsonSchemaInClasspath("schemas/invalid-login-schema.json"))
        );
    }

}