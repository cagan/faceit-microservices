package com.faceit.userservice.domain.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.ZonedDateTime;

@Data
public class DateAudit {
    @Field("created_at")
    @CreatedDate
    private ZonedDateTime createdAt;
    @Field("updated_at")
    @LastModifiedDate
    private ZonedDateTime updatedAt;
}
