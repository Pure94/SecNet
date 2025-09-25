package com.larpologic.secretnetwork.summary.repository;

import com.larpologic.secretnetwork.summary.entity.Summary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SummaryRepository extends JpaRepository<Summary, Long> {
    Optional<Summary> findByChannelIdAndUserId(UUID channelId, UUID userId);
    void deleteByChannelIdAndUserId(UUID channelId, UUID userId);
    void deleteByChannelId(UUID channelId);
}
