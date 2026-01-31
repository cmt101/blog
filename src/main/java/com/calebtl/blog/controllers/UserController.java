package com.calebtl.blog.controllers;

import com.calebtl.blog.dtos.CreateUserRequest;
import com.calebtl.blog.dtos.ProfileDto;
import com.calebtl.blog.dtos.UserDto;
import com.calebtl.blog.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsersSorted());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest data, UriComponentsBuilder uriBuilder) {
        UserDto userDto = userService.createUser(data);
        URI uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<ProfileDto> updateProfile(@PathVariable(name = "id") Long id, @Valid @RequestBody ProfileDto data) {
        return ResponseEntity.ok(userService.updateUserProfile(id, data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
