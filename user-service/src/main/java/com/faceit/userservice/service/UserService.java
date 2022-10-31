package com.faceit.userservice.service;

import com.faceit.userservice.aspect.NotificationOnType;
import com.faceit.userservice.aspect.NotifyUserChange;
import com.faceit.userservice.domain.model.Country;
import com.faceit.userservice.domain.model.User;
import com.faceit.userservice.error.UserAlreadyExistsException;
import com.faceit.userservice.error.UserNotFoundException;
import com.faceit.userservice.repository.UserRepository;
import com.faceit.userservice.rest.mapper.UserUpdateMapper;
import com.faceit.userservice.rest.request.UserCreateRequest;
import com.faceit.userservice.rest.request.UserUpdateRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @NotifyUserChange(type = NotificationOnType.CREATE)
    public User createUser(UserCreateRequest request) {
        var user = userRepository.findByNicknameOrEmail(request.getNickname(), request.getEmail());

        if (user.isPresent()) {
            log.info("[User: {}] nickname or email already taken", user.get());
            throw new UserAlreadyExistsException("Nickname or email already taken");
        }

        var newUser = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .nickname(request.getNickname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .country(Country.valueOf(request.getCountry()))
                .build();

        var createdUser = userRepository.save(newUser);
        log.info("[User: {}] created successfully", createdUser);

        return createdUser;
    }

    public List<User> getUsersPaginated(Pageable pageable) {
        return userRepository.findAll(pageable).toList();
    }

    public Optional<User> findUser(String id) {
        return userRepository.findById(id);
    }

    @Transactional
    @NotifyUserChange(type = NotificationOnType.UPDATE)
    public void updateUser(String id, UserUpdateRequest request) {
        var user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        var currentUser = userRepository
                .findByNicknameOrEmailAndIdNot(request.getNickname(), request.getEmail(), user.getId());

        if (currentUser.isPresent()) {
            log.info("New [User: {}] nickname or email already taken", user);
            throw new UserAlreadyExistsException("Nickname or email already taken");
        }

        UserUpdateMapper.INSTANCE.updateUserFromRequest(request, user);
        userRepository.save(user);
    }

    @NotifyUserChange(type = NotificationOnType.DELETE)
    public void deleteUser(String id) {
        var user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
        log.info("New [User: {}] has been deleted.", user);
    }
}
