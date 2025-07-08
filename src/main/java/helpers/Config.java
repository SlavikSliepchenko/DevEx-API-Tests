package helpers;

import io.restassured.RestAssured;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    public static final String URL;
    public static final String EMAIL;
    public static final String PASSWORD;

    static {
        Properties props = new Properties();
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            props.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось загрузить config.properties", e);
        }

        URL = props.getProperty("url");
        EMAIL = props.getProperty("email");
        PASSWORD = props.getProperty("password");

        RestAssured.baseURI = URL;
    }
}