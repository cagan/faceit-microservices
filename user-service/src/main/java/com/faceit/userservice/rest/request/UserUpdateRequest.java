package com.faceit.userservice.rest.request;


import com.faceit.userservice.domain.model.Country;
import com.faceit.userservice.rest.validator.EnumValidator;
import com.faceit.userservice.rest.validator.ValidPassword;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserUpdateRequest {
    @JsonProperty("first_name")
    @Size(max = 20, min = 2)
    private String firstName;

    @JsonProperty("last_name")
    @Size(max = 20, min = 2)
    private String lastName;

    @Size(max = 20, min = 3)
    private String nickname;

    @Email
    private String email;

    @JsonProperty("country")
    @EnumValidator(
            enumClazz = Country.class,
            message = "Invalid country value"
    )
    private String country;
}
