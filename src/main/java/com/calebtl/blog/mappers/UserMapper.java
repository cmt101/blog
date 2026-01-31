package com.calebtl.blog.mappers;

import com.calebtl.blog.dtos.UserDto;
import com.calebtl.blog.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
