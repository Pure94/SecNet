package com.larpologic.secretnetwork.summary.mapper;

import com.larpologic.secretnetwork.summary.dto.SummaryDto;
import com.larpologic.secretnetwork.summary.entity.Summary;
import org.springframework.stereotype.Service;

@Service
public class SummaryMapper {

    public SummaryDto toDto(Summary summary) {
        SummaryDto dto = new SummaryDto();
        dto.setId(summary.getId());
        dto.setChannelId(summary.getChannel().getId());
        dto.setUserId(summary.getUser().getUuid());
        dto.setSummary(summary.getSummary());
        dto.setCreatedAt(summary.getCreatedAt());
        return dto;
    }
}
