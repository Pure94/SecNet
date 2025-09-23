package com.larpologic.secretnetwork.conversation.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
public class ConversationDto {
    private UUID id;
    private String userMessage;
    private String aiResponse;
    private OffsetDateTime createdAt;
}