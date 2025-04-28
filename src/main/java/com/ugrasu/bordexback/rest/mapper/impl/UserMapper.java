package com.ugrasu.bordexback.rest.mapper.impl;

import com.ugrasu.bordexback.rest.dto.full.UserDto;
import com.ugrasu.bordexback.rest.dto.slim.UserSlimDto;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.mapper.Mappable;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper extends Mappable<User, UserDto, UserSlimDto> {
}