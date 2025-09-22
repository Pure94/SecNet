package com.larpologic.secretnetwork.conversation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {
    Channel findByName(String name);
}