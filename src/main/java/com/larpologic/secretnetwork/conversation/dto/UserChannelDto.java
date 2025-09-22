package com.larpologic.secretnetwork.conversation.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserChannelDto {
    private UserDto user;
    private Integer remainingLimit;
}