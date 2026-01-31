package com.calebtl.blog.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDto {

    @NotBlank
    private Long id;

    @NotBlank
    @Email
    private String email;

    @NotNull
    private ProfileDto profile;
}
