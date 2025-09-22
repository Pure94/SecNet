package com.larpologic.secretnetwork.conversation;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.larpologic.secretnetwork.security.entity.User;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "conversations")
@Getter
@Setter
public class Conversation {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @Column(name = "user_message", columnDefinition = "TEXT", nullable = false)
    private String userMessage;

    @Column(name = "ai_response", columnDefinition = "TEXT", nullable = false)
    private String aiResponse;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    private OffsetDateTime createdAt;
}