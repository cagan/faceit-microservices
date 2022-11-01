package com.faceit.userservice.rest.controller;

import com.faceit.userservice.domain.model.User;
import com.faceit.userservice.rest.mapper.UserResponseMapper;
import com.faceit.userservice.rest.request.UserCreateRequest;
import com.faceit.userservice.rest.request.UserQueryRequest;
import com.faceit.userservice.rest.request.UserUpdateRequest;
import com.faceit.userservice.rest.response.UserQueryResponse;
import com.faceit.userservice.rest.response.UserResponse;
import com.faceit.userservice.service.UserQueryService;
import com.faceit.userservice.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
@Tag(name = "User Service", description = "User CRUD Services")
public class UserController {
    private final UserService userService;
    private final UserQueryService userQueryService;

    /**
     * {@code POST  /api/v1/user} : Create new user
     *
     * @param request the request to create new user
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body of the new created user, or with status {@code 400 (Bad Request) if the user already exists.}
     */
    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserCreateRequest request) {
        var createdUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponseMapper.INSTANCE.toDTO(createdUser));
    }

    /**
     * {@code GET  /api/v1/user} : Get users
     *
     * @param page default = 0, page number of the given user list.
     * @param size default = 10, number of elements to user list.
     * @return the {@link ResponseEntity} with status {@code 200 (Ok)} and with body of the list of users
     */
    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        List<User> users = userService.getUsersPaginated(pageable);
        return ResponseEntity.ok(UserResponseMapper.INSTANCE.toDTOList(users));
    }

    /**
     * {@code GET  /api/v1/user/:id} : Get users
     *
     * @param id id of the user
     * @return the {@link ResponseEntity} with status {@code 200 (Ok)} body of the found user
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") String id) {
        var user = userService.findUser(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found with id: " + id));

        return ResponseEntity.ok(UserResponseMapper.INSTANCE.toDTO(user));
    }

    /**
     * {@code POST  /api/v1/user/search} : Search user
     *
     * @param request search by firstname, lastname, email or country. It looks only for equality for now.
     * @return the {@link ResponseEntity} with status {@code 200 (Ok)} body of the found users with count
     */
    @PostMapping("/search")
    public ResponseEntity<UserQueryResponse> search(@RequestBody UserQueryRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(userQueryService.searchUserByQuery(request));
    }

    /**
     * {@code PATCH  /api/v1/user/:id} : Change user partially
     *
     * @param request update fields: first_name, last_name, nickname, email, country.
     * @param id id of the user.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}}
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateUser(@Valid @RequestBody UserUpdateRequest request, @PathVariable("id") String id) {
        userService.updateUser(id, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * {@code DELETE  /api/v1/user/:id} : Delete user
     *
     * @param id of the user.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@Valid @PathVariable("id") String id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}