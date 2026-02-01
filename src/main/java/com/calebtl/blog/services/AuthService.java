package com.calebtl.blog.services;

import com.calebtl.blog.dtos.JwtResponse;
import com.calebtl.blog.dtos.LoginRequestDto;
import com.calebtl.blog.entities.User;
import com.calebtl.blog.exceptions.UnauthorizedException;
import com.calebtl.blog.exceptions.UserNotFoundException;
import com.calebtl.blog.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public String getJwtToken(String email) {
        User u = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return jwtService.generateToken(u);
    }

    public JwtResponse doLogin(LoginRequestDto requestDto) {
        User u = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(UserNotFoundException::new);
        if (!passwordEncoder.matches(requestDto.getPassword(), u.getPassword())) {
            throw new UnauthorizedException();
        }
        return new JwtResponse(jwtService.generateToken(u));
    }
}
