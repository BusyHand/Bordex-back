package com.ugrasu.bordexback.auth.mapper;

import com.ugrasu.bordexback.auth.dto.AuthDto;
import com.ugrasu.bordexback.rest.dto.web.full.UserDto;
import com.ugrasu.bordexback.rest.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuthMapper {


    UserDto toDto(User user);

    User toEntity(AuthDto authDto);

}
