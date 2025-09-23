package com.larpologic.secretnetwork.conversation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    @Query(value = "SELECT * FROM conversations WHERE user_id = :userId AND channel_id = :channelId ORDER BY created_at DESC LIMIT :N", nativeQuery = true)
    List<Conversation> findLastConversationsByUserAndChannel(
            @Param("N") int N,
            @Param("userId") UUID userId,
            @Param("channelId") UUID channelId
    );
}