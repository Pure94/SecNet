package com.larpologic.secretnetwork.security;

import com.larpologic.secretnetwork.security.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Role findByName(String name);
}