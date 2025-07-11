package tests;

import helpers.AuthHelper;
import helpers.dto.ProfileRequest;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static helpers.Wrappers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

@Epic("Post Tests")

public class PostTests {

    @Test
    @DisplayName("Create post successfully")
    @Story("POST post successfully")
    void createPostSuccessfully() throws Exception {
        String token = AuthHelper.getToken();

        ProfileRequest profile = new ProfileRequest(
                "Test Company",
                "https://test.com",
                "Berlin",
                "QA Engineer",
                "Java, REST Assured",
                "slavaGit",
                "Automation engineer",
                null
        );

        postJson("/api/profile", token, profile)
                .then()
                .statusCode(200);

        Map<String, Object> postBody = Map.of("title", "My New Post", "text", "This is a test post");

        postJson("/api/posts", token, postBody)
                .then()
                .statusCode(200)
                .body("text", equalTo("This is a test post"))
                .body(matchesJsonSchemaInClasspath("schemas/post-created-response-schema.json"));
    }

    @Test
    @DisplayName("Negative test: delete post with valid ID")
    @Story("Negative test: delete post with invalid ID")
    void deletePostWithInvalidIdShouldFail() {
        String token = AuthHelper.getToken();
        String invalidId = "nonexistent-id";

        deleteWithInvalidID("/api/posts/{id}", token, invalidId)
                .then()
                .statusCode(404)
                .body("msg", equalTo("Post not found!"));
    }
}