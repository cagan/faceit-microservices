package com.faceit.userservice.unit;

import com.faceit.userservice.domain.model.Country;
import com.faceit.userservice.domain.model.User;
import com.faceit.userservice.error.UserAlreadyExistsException;
import com.faceit.userservice.error.UserNotFoundException;
import com.faceit.userservice.repository.UserRepository;
import com.faceit.userservice.rest.request.UserCreateRequest;
import com.faceit.userservice.rest.request.UserUpdateRequest;
import com.faceit.userservice.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void itShouldSaveUserSuccessfully() {
        User user = User.builder()
                .nickname("johndoe")
                .firstName("john")
                .lastName("doe")
                .country(Country.EN)
                .password(passwordEncoder.encode("P@ssword12+"))
                .build();

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setFirstName("John");
        userCreateRequest.setLastName("Doe");
        userCreateRequest.setEmail("johndoe@email.com");
        userCreateRequest.setNickname("johndoe");
        userCreateRequest.setPassword("P@ssword12+");
        userCreateRequest.setCountry(String.valueOf(Country.FR));

        User createdUser = userService.createUser(userCreateRequest);

        assertThat(createdUser.getNickname()).isSameAs(user.getNickname());
        assertThat(createdUser.getEmail()).isSameAs(user.getEmail());
    }

    @Test
    public void itShouldThrowExceptionWhenUserAlreadyFound() {
        User user = User.builder()
                .nickname("johndoe")
                .firstName("john")
                .lastName("doe")
                .country(Country.EN)
                .password(passwordEncoder.encode("P@ssword12+"))
                .build();

        when(userRepository.findByNicknameOrEmail("johndoe", "johndoe@email.com")).thenReturn(Optional.of(user));

        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setFirstName("John");
        userCreateRequest.setLastName("Doe");
        userCreateRequest.setEmail("johndoe@email.com");
        userCreateRequest.setNickname("johndoe");
        userCreateRequest.setPassword("P@ssword12+");
        userCreateRequest.setCountry(String.valueOf(Country.FR));

        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userCreateRequest));
    }

    @Test
    public void itShouldThrowException_WhenUserNotFound() {
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setFirstName("John");
        userCreateRequest.setLastName("Doe");
        userCreateRequest.setEmail("johndoe@email.com");
        userCreateRequest.setNickname("johndoe");
        userCreateRequest.setPassword("P@ssword12+");
        userCreateRequest.setCountry(String.valueOf(Country.FR));

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();

        Mockito.when(userRepository.findById("1")).thenReturn(Optional.empty());

        Assertions.assertThrowsExactly(UserNotFoundException.class, () -> userService.updateUser("1", userUpdateRequest));
    }

    @Test
    public void itShouldThrowException_WhenUpdatedEmailOrNicknameAlreadyExists() {
        User user = User.builder()
                .nickname("johndoe")
                .firstName("john")
                .lastName("doe")
                .country(Country.EN)
                .password(passwordEncoder.encode("P@ssword12+"))
                .build();

        UserUpdateRequest request = new UserUpdateRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("johndoe@email.com");
        request.setNickname("johndoe");

        Mockito.when(userRepository.findById("1")).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByNicknameOrEmailAndIdNot(request.getNickname(), request.getEmail(), user.getId())).thenReturn(Optional.of(user));

        Assertions.assertThrowsExactly(UserAlreadyExistsException.class, () -> userService.updateUser("1", request), "Nickname or email already taken");
    }

    @Test
    public void itShould_UpdateUser_successfully() {
        User user = User.builder()
                .nickname("johndoe")
                .firstName("john")
                .lastName("doe")
                .country(Country.EN)
                .password(passwordEncoder.encode("P@ssword12+"))
                .build();

        UserUpdateRequest request = new UserUpdateRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("johndoe@email.com");
        request.setNickname("johndoe");

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByNicknameOrEmailAndIdNot(request.getNickname(), request.getEmail(), user.getId())).thenReturn(Optional.empty());

        userService.updateUser(user.getId(), request);

        verify(userRepository).save(any(User.class));
        verify(userRepository).findById(user.getId());
    }

    @Test
    public void itShould_throwExceptionWhen_User_notFound_onDelete() {
        Mockito.when(userRepository.findById("1")).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.deleteUser("1"));
    }

    @Test
    public void itShouldDeleteUserWhenUser_Found() {
        User user = User.builder()
                .nickname("johndoe")
                .firstName("john")
                .lastName("doe")
                .country(Country.EN)
                .password(passwordEncoder.encode("P@ssword12+"))
                .build();
        Mockito.when(userRepository.findById("1")).thenReturn(Optional.of(user));
        userService.deleteUser("1");
    }

    @Test
    public void itShouldFindUser() {
        User user = User.builder()
                .nickname("johndoe")
                .firstName("john")
                .lastName("doe")
                .country(Country.EN)
                .password(passwordEncoder.encode("P@ssword12+"))
                .build();
        Mockito.when(userRepository.findById("1")).thenReturn(Optional.of(user));
        Optional<User> user1 = userService.findUser("1");

        Assertions.assertEquals(Optional.of(user), user1);
    }
}
