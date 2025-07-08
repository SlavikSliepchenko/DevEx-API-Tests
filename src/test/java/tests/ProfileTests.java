package tests;

import helpers.Wrappers;
import helpers.dto.ProfileRequest;
import helpers.dto.ProfileResponse;
import helpers.dto.RegisterRequest;
import helpers.dto.Social;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static helpers.AuthHelper.loginAndGetToken;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("Profile")
public class ProfileTests {
    @Test
    @Story("Get Profile")
    void shouldGetProfileOfCurrentUser() {

        String email = "profi" + System.currentTimeMillis() + "@test.com";
        String password = "Password123!";

        RegisterRequest register = new RegisterRequest(
                email, password, "Profile Test", null, null, null
        );
        Wrappers.postJson("/api/users", register);

        String token = loginAndGetToken(email, password);
        Allure.step("Проверка: токен не null", () ->
                assertEquals(true, token != null && !token.isEmpty())
        );
        Social social = new Social(
                "youtube.com/profile",
                "twitter.com/profile",
                "facebook.com/profile",
                "linkedin.com/in/profile",
                "instagram.com/profile"
        );

        ProfileRequest profileRequest = new ProfileRequest(
                "TestCompany", "https://example.com", "TestCity", "QA Engineer",
                "Java,REST,Postman",
                "profilehub", "This is a bio", social
        );

        Wrappers.postJsonWithToken("/api/profile", profileRequest, token);

        Response response = Wrappers.getWithToken("/api/profile/me", token);

        Allure.step("Проверка: статус-код 200", () ->
                assertEquals(200, response.getStatusCode())
        );

        ProfileResponse profile = response.as(ProfileResponse.class);

        Allure.step("Проверка: email пользователя совпадает", () ->
                assertEquals(email, profile.user.email)
        );
    }

    @Test
    @Story("Successful delete Profile")
    void shouldDeleteProfileSuccessfully() {

        String email = "deleteuser_" + System.currentTimeMillis() + "@test.com";
        String password = "Password123!";
        RegisterRequest register = new RegisterRequest(email, password, "Delete User", null, null, null);
        Wrappers.postJson("/api/users", register);

        String token = loginAndGetToken(email, password);

        Social social = new Social(
                "https://youtube.com/profile",
                "https://twitter.com/profile",
                "https://facebook.com/profile",
                "https://linkedin.com/in/profile",
                "https://instagram.com/profile"
        );

        ProfileRequest profileRequest = new ProfileRequest(
                "DeleteCorp", "https://corp.com", "Nowhere", "Ex-QA",
                List.of("Nothing", "Null"), "nobody", "nothing", "empty", social
        );
        Wrappers.postJsonWithToken("/api/profile", profileRequest, token);

        Response response = Wrappers.deleteWithToken("/api/profile", token);

        Allure.step("Проверка: структура ошибки соответствует схеме", () ->
                response.then().assertThat()
                        .body(matchesJsonSchemaInClasspath("schemas/successful-profile-delete-schema.json"))
        );
    }
}