package com.calebtl.blog.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Date;

@AllArgsConstructor
@Getter
public class ProfileDto {

    private String firstName;

    private String lastName;

    private String bio;

    private String phoneNumber;

    // TODO: Can I just have the date inserted into the DB without spring assuming it needs to be in UTC?
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date birthDate;

}
