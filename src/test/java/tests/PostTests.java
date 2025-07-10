package tests;

import helpers.AuthHelper;
import helpers.dto.ProfileRequest;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static helpers.Wrappers.deleteWithAllure;
import static helpers.Wrappers.postJson;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

public class PostTests {

    @Test
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
    @Story("Negative test: delete post with invalid ID")
    void deletePostWithInvalidIdShouldFail() {
        String token = AuthHelper.getToken();
        String invalidId = "nonexistent-id";

        deleteWithAllure("/api/posts/{id}", token, invalidId)
                .then()
                .statusCode(404)
                .body("msg", equalTo("Post not found!"));
    }
}