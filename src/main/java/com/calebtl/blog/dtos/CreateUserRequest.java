package com.calebtl.blog.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateUserRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    private ProfileDto profile;
}
