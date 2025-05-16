package com.ugrasu.bordexback.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TelegramLoginInitDataRequest {
    String initData;
}
