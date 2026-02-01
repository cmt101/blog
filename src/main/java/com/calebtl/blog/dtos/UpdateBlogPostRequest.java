package com.calebtl.blog.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateBlogPostRequest {
    private String title;

    private String body;
}
