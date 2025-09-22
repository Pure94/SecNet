package com.larpologic.secretnetwork.security.entity;

import com.larpologic.secretnetwork.conversation.Conversation;
import com.larpologic.secretnetwork.conversation.UserChannel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID uuid;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @OneToMany(mappedBy = "user")
    private Set<UserChannel> userChannels;

    @OneToMany(mappedBy = "user")
    private Set<Conversation> conversations;

}