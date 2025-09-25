package com.larpologic.secretnetwork.summary.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SummaryDto {
    private Long id;
    private UUID channelId;
    private UUID userId;
    private String summary;
    private LocalDateTime createdAt;
}
