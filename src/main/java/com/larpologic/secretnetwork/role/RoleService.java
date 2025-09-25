package com.larpologic.secretnetwork.role;

import com.larpologic.secretnetwork.role.dto.RoleDto;
import com.larpologic.secretnetwork.role.entity.Role;
import com.larpologic.secretnetwork.role.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Transactional
    public void createRole(String roleName) {
        Role role = new Role();
        role.setName(roleName);
        roleRepository.save(role);
    }

    public List<RoleDto> getAllRolesAsDto() {
        return roleRepository.findAll().stream()
                .map(roleMapper::convertToRoleDto)
                .collect(Collectors.toList());
    }

    public Role findByName(String roleName) {
        return roleRepository.findByName(roleName);
    }

    public Role save(Role role) {
        return roleRepository.save(role);
    }
}