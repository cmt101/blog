package com.calebtl.blog.controllers;

import com.calebtl.blog.dtos.JwtResponse;
import com.calebtl.blog.dtos.LoginRequestDto;
import com.calebtl.blog.services.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {
    private final AuthService authService;

    @PostMapping
    @RequestMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequestDto data) {
        return ResponseEntity.ok(authService.doLogin(data));
    }

}
