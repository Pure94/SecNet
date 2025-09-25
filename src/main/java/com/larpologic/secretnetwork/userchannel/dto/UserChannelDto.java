package com.larpologic.secretnetwork.userchannel.dto;

import com.larpologic.secretnetwork.user.UserDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserChannelDto {
    private UserDto user;
    private Integer remainingLimit;
}