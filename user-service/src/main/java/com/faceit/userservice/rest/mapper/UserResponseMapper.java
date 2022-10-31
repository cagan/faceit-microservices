package com.faceit.userservice.rest.mapper;


import com.faceit.userservice.domain.model.User;
import com.faceit.userservice.rest.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {
    UserResponseMapper INSTANCE = Mappers.getMapper( UserResponseMapper.class );

    UserResponse toDTO(User user);

    User toUser(UserResponse userDTO);

    List<UserResponse> toDTOList(List<User> user);
}
