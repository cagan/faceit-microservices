package com.faceit.userservice.producer;

import com.faceit.userservice.domain.model.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEntityChangeMessage {
    private String message;
    private Object data;
    private String id;
}
