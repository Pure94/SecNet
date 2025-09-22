package com.larpologic.secretnetwork.conversation;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "channels")
@Getter
@Setter
public class Channel {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "system_prompt")
    private String systemPrompt;

    @OneToMany(mappedBy = "channel")
    private Set<UserChannel> userChannels;

    @OneToMany(mappedBy = "channel")
    private Set<Conversation> conversations;
}