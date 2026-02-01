package com.calebtl.blog.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TagDto {
    @NotBlank
    private String name;

    @NotBlank
    private String value;
}
