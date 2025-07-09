// src/main/java/helpers/AuthHelper.java
package helpers;

import helpers.dto.LoginRequest;
import io.restassured.response.Response;

import static helpers.Config.URL;
import static io.restassured.RestAssured.given;

public class AuthHelper {

    public static String loginAndGetToken(String email, String password) {
        LoginRequest login = new LoginRequest(email, password);

        Response response = given()
                .baseUri(URL)
                .contentType("application/json")
                .body(login)
                .post("/api/auth");

        return response.jsonPath().getString("token");
    }

    public static String getToken() {
        String email = Config.EMAIL;
        String password = Config.PASSWORD;
        return loginAndGetToken(email, password);
    }

}