package com.calebtl.blog.mappers;

import com.calebtl.blog.dtos.BlogPostDto;
import com.calebtl.blog.entities.BlogPost;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BlogPostMapper {

    @Mapping(source = "user.id", target = "author.id")
    @Mapping(source = "user.email", target = "author.email")
    @Mapping(source = "user.profile", target = "author.profile")
    BlogPostDto toDto(BlogPost blogPost);

    // TODO: This is bad. Probably need a separate dto specific to Blog creation and add an endpoint to add tags
    @Mapping(source = "author.id", target = "user.id")
    @Mapping(source = "author.email", target = "user.email")
    @Mapping(source = "author.profile", target = "user.profile")
    @Mapping(target = "likes", ignore = true)
    @Mapping(target = "dislikes", ignore = true)
    @Mapping(target = "tags", ignore = true)
    BlogPost toEntity(BlogPostDto blogPostDto);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "title", target = "title")
    @Mapping(source = "body", target = "body")
    @Mapping(source = "likes", target = "likes")
    @Mapping(source = "dislikes", target = "dislikes")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "comments", target = "comments")
    @Mapping(source = "tags", target = "tags")
    void update(BlogPost source, @MappingTarget BlogPost target);
}
