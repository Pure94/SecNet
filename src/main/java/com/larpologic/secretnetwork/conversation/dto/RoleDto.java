package com.larpologic.secretnetwork.conversation.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(of = "uuid")
public class RoleDto {
    private UUID uuid;
    private String name;
}