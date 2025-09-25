package com.larpologic.secretnetwork.channel;

import com.larpologic.secretnetwork.conversation.entity.Conversation;
import com.larpologic.secretnetwork.userchannel.UserChannel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "channels")
@Getter
@Setter
@JsonIgnoreProperties({"conversations"})
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