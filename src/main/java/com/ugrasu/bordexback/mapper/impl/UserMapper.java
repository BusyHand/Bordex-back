package com.ugrasu.bordexback.mapper.impl;

import com.ugrasu.bordexback.dto.full.UserDto;
import com.ugrasu.bordexback.dto.slim.UserSlimDto;
import com.ugrasu.bordexback.entity.User;
import com.ugrasu.bordexback.mapper.Mappable;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper extends Mappable<User, UserDto, UserSlimDto> {

}