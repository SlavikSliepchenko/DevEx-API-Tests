package helpers.dto;

import java.util.List;

public class ProfileRequest {
    public String company;
    public String website;
    public String location;
    public String status;
    public String skills;
    public String githubusername;
    public String bio;
    public Social social;

    public ProfileRequest(String company, String website, String location, String status,
                          String skills, String githubusername, String bio, Social social) {
        this.company = company;
        this.website = website;
        this.location = location;
        this.status = status;
        this.skills = skills;
        this.githubusername = githubusername;
        this.bio = bio;
        this.social = social;
    }

    public ProfileRequest(String deleteCorp, String url, String nowhere, String status, List<String> nothing, String nobody, String nothing1, String empty, Social social) {
    }
}
