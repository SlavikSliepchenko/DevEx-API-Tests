package helpers.dto;

public class RegisterRequest {
    public String email;
    public String password;
    public String name;
    public String google;
    public String facebook;
    public String github;

    public RegisterRequest(String email, String password, String name,
                           String google, String facebook, String github) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.google = google;
        this.facebook = facebook;
        this.github = github;
    }
}