package com.larpologic.secretnetwork.summary.repository;

import com.larpologic.secretnetwork.summary.entity.Summary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SummaryRepository extends JpaRepository<Summary, Long> {
    Optional<Summary> findByChannelIdAndUserUuid(UUID channelId, UUID userId);
    void deleteByChannelIdAndUserUuid(UUID channelId, UUID userId);
}
