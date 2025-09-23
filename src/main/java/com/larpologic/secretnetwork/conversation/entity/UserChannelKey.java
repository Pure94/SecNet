package com.larpologic.secretnetwork.conversation.entity;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class UserChannelKey implements Serializable {
    private UUID user;
    private UUID channel;
}