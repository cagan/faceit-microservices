package com.faceit.userservice.service;

import com.faceit.userservice.domain.model.User;
import com.faceit.userservice.rest.request.UserQueryRequest;
import com.faceit.userservice.rest.response.UserQueryResponse;
import com.faceit.userservice.rest.mapper.UserResponseMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserQueryService {
    private final MongoTemplate mongoTemplate;

    public UserQueryResponse searchUserByQuery(UserQueryRequest request) {
        Query query = new Query();
        var queryResponseBuilder = UserQueryResponse.builder();

        if (request.getCountry() != null) {
            query.addCriteria(Criteria.where("country").is(request.getCountry()));
        }

        if (request.getNickname() != null) {
            query.addCriteria(Criteria.where("nickname").is(request.getNickname()));
        }

        if (request.getEmail() != null) {
            query.addCriteria(Criteria.where("email").is(request.getEmail()));
        }

        if (request.getFirstName() != null) {
            query.addCriteria(Criteria.where("first_name").is(request.getFirstName()));
        }

        if (request.getLastName() != null) {
            query.addCriteria(Criteria.where("last_name").is(request.getLastName()));
        }

        long count = mongoTemplate.count(query, User.class);
        List<User> users = mongoTemplate.find(query, User.class);

        queryResponseBuilder
                .count(count)
                .data(UserResponseMapper.INSTANCE.toDTOList(users));

        return queryResponseBuilder.build();
    }
}
