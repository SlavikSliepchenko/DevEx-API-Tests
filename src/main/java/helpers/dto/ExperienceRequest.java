package helpers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExperienceRequest {

    @JsonProperty("title")
    private String title;

    @JsonProperty("company")
    private String company;

    @JsonProperty("location")
    private String location;

    @JsonProperty("from")
    private String from;

    @JsonProperty("to")
    private String to;

    @JsonProperty("current")
    private boolean current;

    @JsonProperty("description")
    private String description;

    public ExperienceRequest(String title, String company, String location,
                             String from, String to, boolean current, String description) {
        this.title = title;
        this.company = company;
        this.location = location;
        this.from = from;
        this.to = to;
        this.current = current;
        this.description = description;
    }
}