package com.faceit.userservice.rest.request;

import com.faceit.userservice.domain.model.Country;
import com.faceit.userservice.rest.validator.EnumValidator;
import com.faceit.userservice.rest.validator.ValidPassword;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserCreateRequest {
    @JsonProperty("first_name")
    @NotBlank(message = "first_name should must not be blank")
    @Size(max = 20)
    private String firstName;

    @JsonProperty("last_name")
    @NotBlank(message = "last_name should must not be blank")
    @Size(max = 20)
    private String lastName;

    @NotNull
    @NotBlank
    @Size(min = 3, max = 20)
    private String nickname;

    @NotNull
    @NotBlank
    @ValidPassword
    private String password;

    @NotNull
    @Email
    private String email;

    @NotNull
    @JsonProperty("country")
    @EnumValidator(
            enumClazz = Country.class,
            message = "Invalid country value"
    )
    private String country;
}
