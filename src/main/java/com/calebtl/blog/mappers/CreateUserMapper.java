package com.calebtl.blog.mappers;

import com.calebtl.blog.dtos.CreateUserRequest;
import com.calebtl.blog.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreateUserMapper {
    User toEntity(CreateUserRequest request);
}
