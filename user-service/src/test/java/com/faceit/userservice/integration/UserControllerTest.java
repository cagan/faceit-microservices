package com.faceit.userservice.integration;

import com.faceit.userservice.domain.model.Country;
import com.faceit.userservice.rest.controller.UserController;
import com.faceit.userservice.rest.request.UserCreateRequest;
import com.faceit.userservice.rest.request.UserUpdateRequest;
import com.faceit.userservice.rest.response.UserResponse;
import com.faceit.userservice.service.UserQueryService;
import com.faceit.userservice.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Objects;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
@TestPropertySource(locations = "classpath:application-dev.properties")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserQueryService userQueryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void itShouldCreateNewUser() throws Exception {
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setFirstName("John");
        userCreateRequest.setLastName("Doe");
        userCreateRequest.setEmail("johndoe@email.com");
        userCreateRequest.setNickname("johndoe");
        userCreateRequest.setPassword("P@ssword12+");
        userCreateRequest.setCountry(String.valueOf(Country.FR));

        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateRequest)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public void whenInvalidCreateRequestSend_itShouldReturn400() throws Exception {
        UserCreateRequest request = new UserCreateRequest();
        request.setLastName("doe");
        request.setNickname("johndoe");
        request.setPassword("P@ssword12+");
        request.setCountry(String.valueOf(Country.EN));

        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        request.setFirstName("john");
        request.setEmail(null);

        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        request.setEmail("johndoe@email.com");
        request.setPassword("");

        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenInvalidPasswordEntered_itShouldReturn400() throws Exception {
        UserCreateRequest request = new UserCreateRequest();
        request.setFirstName("john");
        request.setLastName("doe");
        request.setNickname("johndoe");
        request.setPassword("abc");
        request.setEmail("johndoe@email.com");
        request.setCountry(String.valueOf(Country.EN));

        String message = Objects.requireNonNull(mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isBadRequest())
//                .andExpect(status().reason(containsString("Password must be 6 or more characters in length.")))
                .andDo(print())
                .andReturn().getResolvedException()).getMessage();

        Assertions.assertTrue(message.contains("Password must be 6 or more characters in length."));
    }

    @Test
    public void whenInvalidPasswordwhithoutUpperCase_itShouldReturn400() throws Exception {
        UserCreateRequest request = new UserCreateRequest();
        request.setFirstName("john");
        request.setLastName("doe");
        request.setNickname("johndoe");
        request.setPassword("password123rsfadsf+");
        request.setEmail("johndoe@email.com");
        request.setCountry(String.valueOf(Country.EN));

        String message = Objects.requireNonNull(mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andReturn().getResolvedException()).getMessage();

        Assertions.assertTrue(message.contains("Password must contain 1 or more uppercase characters."));
    }

    @Test
    public void whenInvalidPasswordwhithoutSpecialCase_itShouldReturn400() throws Exception {
        UserCreateRequest request = new UserCreateRequest();
        request.setFirstName("john");
        request.setLastName("doe");
        request.setNickname("johndoe");
        request.setPassword("Password123rsfadsf");
        request.setEmail("johndoe@email.com");
        request.setCountry(String.valueOf(Country.EN));

        String message = Objects.requireNonNull(mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andReturn().getResolvedException()).getMessage();

        Assertions.assertTrue(message.contains("Password must contain 1 or more special characters."));
    }

    @Test
    public void whenInvalidPasswordwhithoutLowerCase_itShouldReturn400() throws Exception {
        UserCreateRequest request = new UserCreateRequest();
        request.setFirstName("john");
        request.setLastName("doe");
        request.setNickname("johndoe");
        request.setPassword("PASSWORD123ASF");
        request.setEmail("johndoe@email.com");
        request.setCountry(String.valueOf(Country.EN));

        String message = Objects.requireNonNull(mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andReturn().getResolvedException()).getMessage();

        Assertions.assertTrue(message.contains("Password must contain 1 or more lowercase characters."));
    }

    @Test
    public void whenValidInput_thenMapsToBusinessModel() throws Exception {
        UserCreateRequest request = new UserCreateRequest();
        request.setFirstName("john");
        request.setLastName("doe");
        request.setNickname("johndoe");
        request.setPassword("P@ssword12+");
        request.setEmail("johndoe@email.com");
        request.setCountry(String.valueOf(Country.EN));

        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

        ArgumentCaptor<UserCreateRequest> userArgumentCaptor = ArgumentCaptor.forClass(UserCreateRequest.class);
        verify(userService, times(1)).createUser(userArgumentCaptor.capture());

        Assertions.assertEquals(userArgumentCaptor.getValue().getFirstName(), "john");
        Assertions.assertEquals(userArgumentCaptor.getValue().getLastName(), "doe");
        Assertions.assertEquals(userArgumentCaptor.getValue().getNickname(), "johndoe");
        Assertions.assertEquals(userArgumentCaptor.getValue().getEmail(), "johndoe@email.com");
    }

    @Test
    public void itShould_validateCountryValues() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setFirstName("john");
        request.setLastName("doe");
        request.setNickname("johndoe");
        request.setEmail("johndoe@email.com");
        request.setCountry("ASDAS");

        String message = Objects.requireNonNull(mockMvc.perform(patch("/api/v1/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andReturn().getResolvedException()).getMessage();

        Assertions.assertTrue(message.contains("Invalid country value"));
    }

    @Test
    public void itShould_validateEmail() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setFirstName("john");
        request.setLastName("doe");
        request.setNickname("johndoe");
        request.setEmail("invalidemailaddress");
        request.setCountry(String.valueOf(Country.EN));

        String message = Objects.requireNonNull(mockMvc.perform(patch("/api/v1/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andReturn().getResolvedException()).getMessage();

        Assertions.assertTrue(message.contains("must be a well-formed email address"));
    }

    @Test
    public void itShould_validateNicknameMaxValue() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setFirstName("john");
        request.setLastName("doe");
        request.setNickname("toolongnicknametoolongnicknametoolongnicknametoolongnickname");
        request.setEmail("cagansit@test.com");
        request.setCountry(String.valueOf(Country.EN));

        String message = Objects.requireNonNull(mockMvc.perform(patch("/api/v1/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andReturn().getResolvedException()).getMessage();

        Assertions.assertTrue(message.contains("size must be between 3 and 20"));
    }

    @Test
    public void itShould_validateNicknameMinValue() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setFirstName("john");
        request.setLastName("doe");
        request.setNickname("ni");
        request.setEmail("cagansit@test.com");
        request.setCountry(String.valueOf(Country.EN));

        String message = Objects.requireNonNull(mockMvc.perform(patch("/api/v1/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andReturn().getResolvedException()).getMessage();

        Assertions.assertTrue(message.contains("size must be between 3 and 20"));
    }

    @Test
    public void itShould_validateFirstNameMaxValue() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setFirstName("toolongjohndoeefirstnanmethisis");
        request.setLastName("doe");
        request.setNickname("nickname");
        request.setEmail("cagansit@test.com");
        request.setCountry(String.valueOf(Country.EN));

        String message = Objects.requireNonNull(mockMvc.perform(patch("/api/v1/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andReturn().getResolvedException()).getMessage();

        Assertions.assertTrue(message.contains("size must be between 2 and 20"));
    }

    @Test
    public void itShould_update_user_successfully_with204() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setFirstName("johndoe");
        request.setLastName("doe");
        request.setNickname("nickname");
        request.setEmail("cagansit@test.com");
        request.setCountry(String.valueOf(Country.EN));

        mockMvc.perform(patch("/api/v1/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void itShould_delete_user_successfully_with204() throws Exception {
        mockMvc.perform(delete("/api/v1/user/1"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
