package com.larpologic.secretnetwork.role;

import com.larpologic.secretnetwork.role.dto.RoleDto;
import com.larpologic.secretnetwork.role.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public RoleDto convertToRoleDto(Role role) {
        RoleDto roleDto = new RoleDto();
        roleDto.setUuid(role.getUuid());
        roleDto.setName(role.getName());
        return roleDto;
    }
}
