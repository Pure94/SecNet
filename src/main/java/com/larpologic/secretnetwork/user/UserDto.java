package com.larpologic.secretnetwork.user;

import com.larpologic.secretnetwork.role.dto.RoleDto;
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