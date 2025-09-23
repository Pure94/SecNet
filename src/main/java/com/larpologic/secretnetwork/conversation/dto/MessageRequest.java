package com.larpologic.secretnetwork.conversation.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequest {
    private String username;
    private String channelName;
    private String message;
}