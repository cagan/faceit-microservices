package com.faceit.userservice.domain.event;

import com.faceit.userservice.aspect.NotificationOnType;
import org.springframework.context.ApplicationEvent;

public class UserEntityUpdated extends ApplicationEvent {
    private final NotificationOnType type;
    private final Object payload;

    public UserEntityUpdated(Object source, Object payload, NotificationOnType type) {
        super(source);
        this.type = type;
        this.payload  = payload;
    }

    public NotificationOnType getType() {
        return type;
    }

    public Object getPayload() {
        return payload;
    }
}
