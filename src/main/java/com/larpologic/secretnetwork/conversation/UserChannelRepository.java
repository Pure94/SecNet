package com.larpologic.secretnetwork.conversation;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserChannelRepository extends JpaRepository<UserChannel, UserChannelKey> {
    List<UserChannel> findByChannel(Channel channel);
    Optional<UserChannel> findByChannelIdAndUserUuid(UUID channelId, UUID userId);
}