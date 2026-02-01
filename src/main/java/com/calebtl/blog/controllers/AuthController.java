package com.calebtl.blog.controllers;

import com.calebtl.blog.dtos.JwtResponse;
import com.calebtl.blog.dtos.LoginRequestDto;
import com.calebtl.blog.services.AuthService;
import com.calebtl.blog.services.JwtService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    @PostMapping
    @RequestMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequestDto data) {

        // TODO: Refactor this and authService.getJwtToken so that only one user look up is necessary
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword())
        );

        String token = authService.getJwtToken(data.getEmail());
        return ResponseEntity.ok(new JwtResponse(token));
    }

}
