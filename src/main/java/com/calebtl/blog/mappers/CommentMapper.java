package com.calebtl.blog.mappers;

import com.calebtl.blog.dtos.CommentDto;
import com.calebtl.blog.entities.Comment;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentDto toDto(Comment comment);
    Comment toEntity(CommentDto commentDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "body", target = "body")
    void update(CommentDto request, @MappingTarget Comment product);
}
