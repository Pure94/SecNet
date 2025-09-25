package com.larpologic.secretnetwork.api.information.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UserWithChannelsDto {
    private UUID uuid;
    private String username;
    private List<ChannelLimitDto> channels;
}