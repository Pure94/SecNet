package com.larpologic.secretnetwork.user;

import com.larpologic.secretnetwork.role.RoleMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final RoleMapper roleMapper;

    public UserMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public UserDto convertToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUuid(user.getUuid());
        userDto.setUsername(user.getUsername());
        userDto.setRoles(user.getRoles().stream()
                .map(roleMapper::convertToRoleDto)
                .collect(Collectors.toSet()));
        return userDto;
    }
}
