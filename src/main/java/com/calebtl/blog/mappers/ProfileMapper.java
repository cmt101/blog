package com.calebtl.blog.mappers;

import com.calebtl.blog.dtos.ProfileDto;
import com.calebtl.blog.entities.Profile;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    ProfileDto toDto(Profile profile);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "bio", target = "bio")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "birthDate", target = "birthDate")
    void update(ProfileDto request, @MappingTarget Profile product);

}
