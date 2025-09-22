package com.larpologic.secretnetwork.conversation.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class UserDto {
    private UUID uuid;
    private String username;
    private Set<RoleDto> roles;
}