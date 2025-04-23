package com.ugrasu.bordexback.mapper.impl;

import com.ugrasu.bordexback.dto.full.UserBoardRoleDto;
import com.ugrasu.bordexback.dto.slim.UserBoardRoleSlimDto;
import com.ugrasu.bordexback.entity.UserBoardRole;
import com.ugrasu.bordexback.mapper.Mappable;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserBoardRoleMapper extends Mappable<UserBoardRole, UserBoardRoleDto, UserBoardRoleSlimDto> {

}