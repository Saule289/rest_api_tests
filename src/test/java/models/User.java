package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class User {
    private Integer id;
    private String email;

    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastname;

    private String avatar;
}
