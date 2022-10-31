package com.faceit.userservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api.password")
@Getter
@Setter
public class PasswordProperties {
    private Integer minLength;
    private Integer maxLength;
    private Integer upperCase;
    private Integer lowerCase;
    private Integer digit;
    private Integer special;
}
