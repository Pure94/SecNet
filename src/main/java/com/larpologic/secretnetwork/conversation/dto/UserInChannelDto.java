package com.larpologic.secretnetwork.conversation.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInChannelDto {
    private String username;
    private Integer remainingLimit;
}