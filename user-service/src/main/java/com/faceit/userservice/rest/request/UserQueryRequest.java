package com.faceit.userservice.rest.request;

import com.faceit.userservice.domain.model.Country;
import com.faceit.userservice.rest.validator.EnumValidator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserQueryRequest {
    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private String nickname;

    @Email
    private String email;

    @EnumValidator(
            enumClazz = Country.class,
            message = "Invalid country value"
    )
    private String country;
}
