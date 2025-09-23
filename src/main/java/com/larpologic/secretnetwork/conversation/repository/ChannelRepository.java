package com.larpologic.secretnetwork.conversation.repository;

import com.larpologic.secretnetwork.conversation.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {
    Channel findByName(String name);
}