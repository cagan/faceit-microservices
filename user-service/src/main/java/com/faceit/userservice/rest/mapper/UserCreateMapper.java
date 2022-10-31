package com.faceit.userservice.rest.mapper;

import com.faceit.userservice.domain.model.User;
import com.faceit.userservice.rest.request.UserCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserCreateMapper {
    UserCreateMapper INSTANCE = Mappers.getMapper(UserCreateMapper.class);

    User createRequestToUser(UserCreateRequest request);
}
