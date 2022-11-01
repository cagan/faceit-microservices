package com.faceit.userservice.producer;

import com.faceit.userservice.domain.event.UserEntityUpdated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@Slf4j
public class UserEntityChangeProducer {

    @Bean
    public Function<UserEntityUpdated, UserEntityChangeMessage> sendUserCreatedMessage() {
        return event -> {
            System.out.println("EVENT: " + event);
            UserEntityChangeMessage message = UserEntityChangeMessage.builder()
                    .message("User created successfully")
                    .build();

            log.info("[Message: {}] has been sent", message);
            return message;
        };
    }
}
