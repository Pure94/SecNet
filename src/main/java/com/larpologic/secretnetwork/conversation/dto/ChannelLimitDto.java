package com.larpologic.secretnetwork.conversation.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelLimitDto {
    private String channelName;
    private Integer remainingLimit;
}