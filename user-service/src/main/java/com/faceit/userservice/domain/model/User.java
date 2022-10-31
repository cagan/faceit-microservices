package com.faceit.userservice.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Email;
import java.util.Objects;

@Builder
@Getter
@Setter
@ToString
@Document("user")
@NoArgsConstructor
@AllArgsConstructor
public class User extends DateAudit {
    @Id
    private String id;
    @Field(value = "first_name", order = 1)
    private String firstName;
    @Field(value = "last_name", order = 2)
    private String lastName;
    @Indexed(unique = true)
    private String nickname;
    @ToString.Exclude
    private String password;
    @Email
    @Indexed(unique = true)
    private String email;
    private Country country;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(id, user.id) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(nickname, user.nickname) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && Objects.equals(country, user.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, firstName, lastName, nickname, password, email, country);
    }
}



