package com.calebtl.blog.services;

import com.calebtl.blog.entities.User;
import com.calebtl.blog.exceptions.UserNotFoundException;
import com.calebtl.blog.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public String getJwtToken(String email) {
        User u = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return jwtService.generateToken(u);
    }
}
