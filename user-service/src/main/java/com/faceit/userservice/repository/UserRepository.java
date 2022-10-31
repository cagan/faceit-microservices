package com.faceit.userservice.repository;

import com.faceit.userservice.domain.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, String> {
    Optional<User> findByNicknameOrEmail(String nickname, String email);
    Optional<User> findByNicknameOrEmailAndIdNot(String nickname, String email, String id);
}
