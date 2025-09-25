package com.larpologic.secretnetwork.api.information.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelLimitDto {
    private String channelName;
    private Integer remainingLimit;
}