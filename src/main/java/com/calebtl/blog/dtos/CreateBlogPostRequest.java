package com.calebtl.blog.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateBlogPostRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String body;
}
