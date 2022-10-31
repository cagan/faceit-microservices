package com.faceit.userservice.domain.listener;

import com.faceit.userservice.domain.event.UserEntityUpdated;
import com.faceit.userservice.domain.model.User;
import com.faceit.userservice.producer.UserEntityChangeMessage;
import com.faceit.userservice.rest.mapper.UserCreateMapper;
import com.faceit.userservice.rest.mapper.UserUpdateMapper;
import com.faceit.userservice.rest.request.UserCreateRequest;
import com.faceit.userservice.rest.request.UserUpdateRequest;
import lombok.AllArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserEntityUpdatedListener implements ApplicationListener<UserEntityUpdated> {
    private final StreamBridge bridge;

    @Override
    public void onApplicationEvent(UserEntityUpdated event) {
        UserEntityChangeMessage.UserEntityChangeMessageBuilder builder = UserEntityChangeMessage.builder();

        switch (event.getType()) {
            case CREATE -> {
                UserCreateRequest payload = (UserCreateRequest) event.getPayload();
                User user = UserCreateMapper.INSTANCE.createRequestToUser(payload);
                UserEntityChangeMessage message = builder
                        .message("New user created successfully")
                        .data(user)
                        .build();

                bridge.send("sendUserCreatedMessage-out-0", message);
            }
            case UPDATE -> {
                UserUpdateRequest payload = (UserUpdateRequest) event.getPayload();
                User user = UserUpdateMapper.INSTANCE.updateRequestToUser(payload);
                UserEntityChangeMessage message = builder.message("User has been updated")
                        .data(user)
                        .build();

                bridge.send("sendUserUpdatedMessage-out-0", message);
            }
            case DELETE -> {
                String id = (String) event.getPayload();
                UserEntityChangeMessage message = builder.message("User has been deleted")
                        .data(id)
                        .build();

                bridge.send("sendUserDeletedMessage-out-0", message);
            }
        }
    }
}
