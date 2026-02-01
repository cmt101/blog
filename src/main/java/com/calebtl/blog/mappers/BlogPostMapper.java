package com.calebtl.blog.mappers;

import com.calebtl.blog.dtos.BlogPostDto;
import com.calebtl.blog.dtos.UpdateBlogPostRequest;
import com.calebtl.blog.entities.BlogPost;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BlogPostMapper {

    @Mapping(source = "user.id", target = "authorId")
    BlogPostDto toDto(BlogPost blogPost);

    BlogPost toEntity(UpdateBlogPostRequest blogPostDto);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    void update(BlogPost source, @MappingTarget BlogPost target);
}
