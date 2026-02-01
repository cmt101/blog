package com.calebtl.blog.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Getter
public class BlogPostDto {
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String body;

    private Integer likes = 0;

    private Integer dislikes = 0;

    private UserDto author;

    private List<CommentDto> comments;

    private Set<TagDto> tags;
}
