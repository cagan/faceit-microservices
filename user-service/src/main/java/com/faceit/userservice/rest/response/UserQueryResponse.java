package com.faceit.userservice.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonPropertyOrder(value = {"count", "data"})
public class UserQueryResponse {
    @JsonProperty("count")
    Long count;

    @JsonProperty("data")
    List<UserResponse> data;
}
