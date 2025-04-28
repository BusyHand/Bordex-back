package com.ugrasu.bordexback.rest.mapper.impl;

import com.ugrasu.bordexback.rest.dto.full.UserBoardRoleDto;
import com.ugrasu.bordexback.rest.dto.slim.UserBoardRoleSlimDto;
import com.ugrasu.bordexback.rest.entity.UserBoardRole;
import com.ugrasu.bordexback.rest.mapper.Mappable;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserBoardRoleMapper extends Mappable<UserBoardRole, UserBoardRoleDto, UserBoardRoleSlimDto> {

}