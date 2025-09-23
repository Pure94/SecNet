package com.larpologic.secretnetwork.conversation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.larpologic.secretnetwork.security.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "user_channels")
@Getter
@Setter
public class UserChannel {

    @EmbeddedId
    private UserChannelKey id;

    @ManyToOne
    @MapsId("user")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("channel")
    @JoinColumn(name = "channel_id")
    @JsonIgnore
    private Channel channel;

    @Column(name = "remaining_limit")
    private Integer remainingLimit;
}