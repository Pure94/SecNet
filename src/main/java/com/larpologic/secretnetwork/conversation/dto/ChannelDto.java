package com.larpologic.secretnetwork.conversation.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class ChannelDto {
    private UUID id;
    private String name;
    private String systemPrompt;
    private Set<UserChannelDto> userChannels;
}