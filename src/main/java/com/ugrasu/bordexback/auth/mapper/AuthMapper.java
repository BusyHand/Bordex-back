package com.ugrasu.bordexback.auth.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ugrasu.bordexback.auth.dto.AuthDto;
import com.ugrasu.bordexback.auth.dto.TelegramLoginInitDataRequest;
import com.ugrasu.bordexback.rest.dto.web.full.UserDto;
import com.ugrasu.bordexback.rest.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface AuthMapper {


    UserDto toDto(User user);

    User toEntity(AuthDto authDto);

    default User toEntity(TelegramLoginInitDataRequest telegramLoginInitDataRequest) {
        String initData = telegramLoginInitDataRequest.getInitData();

        Map<String, String> paramMap = Arrays.stream(initData.split("&"))
                .map(pair -> pair.split("=", 2))
                .collect(Collectors.toMap(
                        arr -> URLDecoder.decode(arr[0], StandardCharsets.UTF_8),
                        arr -> arr.length > 1 ? URLDecoder.decode(arr[1], StandardCharsets.UTF_8) : ""
                ));
        String userJson = paramMap.get("user");
        if (userJson == null) {
            return User.builder()
                    .chatId(-1L)
                    .build();
        }
        userJson = userJson.replace("id", "chatId");
        userJson = userJson.replace("username", "telegramUsername");
        try {
            return new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .readValue(userJson, User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
