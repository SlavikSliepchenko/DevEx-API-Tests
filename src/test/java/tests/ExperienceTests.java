package tests;

import helpers.AuthHelper;
import helpers.dto.ExperienceRequest;
import helpers.dto.ProfileRequest;
import helpers.dto.Social;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static helpers.Wrappers.patchJson;
import static helpers.Wrappers.postJson;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

public class ExperienceTests {

    private void createProfile(String token) {
        ProfileRequest profile = new ProfileRequest(
                "Test Company",
                "https://test.com",
                "Berlin",
                "QA Engineer",
                "Java, Selenium",
                "testgit",
                "Test bio",
                new Social("yt", "tw", "fb", "ln", "inst")
        );

        postJson("/api/profile", token, profile)
                .then()
                .statusCode(anyOf(equalTo(200), equalTo(201)));
    }

    @Test
    @Story("POST experience successfully")
    void createExperienceSuccessfully() throws Exception {
        String token = AuthHelper.getToken();
        createProfile(token);

        ExperienceRequest experience = new ExperienceRequest(
                "Junior QA", "Google", "Cologne",
                "2024-01-01", "2025-01-01", false,
                "Worked on test automation"
        );
        String response = postJson("/api/profile/experience", token, experience)
                .then()
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath("schemas/experience-post-success-schema.json"))
                .extract()
                .body()
                .asString();

        System.out.println("Response body:\n" + response);
    }

    @Test
    @Story("POST experience without title should fail")
    void createExperienceWithoutTitleShouldFail() throws Exception {
        String token = AuthHelper.getToken();
        createProfile(token);

        ExperienceRequest experience = new ExperienceRequest(
                null, // title отсутствует
                "Google",
                "Cologne",
                "2024-01-01",
                "2025-01-01",
                false,
                "Worked on test automation"
        );

        postJson("/api/profile/experience", token, experience)
                .then()
                .statusCode(400);
    }

    @Test
    @Story("PATCH experience title successfully")
    void updateExperienceTitleSuccessfully() throws Exception {
        String token = AuthHelper.getToken();
        createProfile(token);

        ExperienceRequest experience = new ExperienceRequest(
                "Middle QA",
                "Google",
                "Remote",
                "2022-01-01",
                "2023-01-01",
                false,
                "Manual + automation testing"
        );

        int id = postJson("/api/profile/experience", token, experience)
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getInt("id");
        System.out.println("Created experience ID: " + id);
        Map<String, Object> patchBody = Map.of("title", "Senior QA");

        patchJson("/api/profile/experience/" + id, token, patchBody)
                .statusCode(204);
    }
}