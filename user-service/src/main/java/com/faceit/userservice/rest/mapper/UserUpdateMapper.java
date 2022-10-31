package com.faceit.userservice.rest.mapper;

import com.faceit.userservice.domain.model.User;
import com.faceit.userservice.rest.request.UserUpdateRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserUpdateMapper {
    UserUpdateMapper INSTANCE = Mappers.getMapper( UserUpdateMapper.class );

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromRequest(UserUpdateRequest request, @MappingTarget User user);

    User updateRequestToUser(UserUpdateRequest request);
}
