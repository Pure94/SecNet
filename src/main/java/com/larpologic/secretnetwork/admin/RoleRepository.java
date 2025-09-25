package com.larpologic.secretnetwork.admin;

import com.larpologic.secretnetwork.admin.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Role findByName(String name);
}