package helpers.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfileResponse {
    public String status;
    public String company;
    public String website;
    public String location;
    public List<String> skills;
    public String githubusername;
    public String bio;
    public Social social;
    public User user;
    public String date;
}