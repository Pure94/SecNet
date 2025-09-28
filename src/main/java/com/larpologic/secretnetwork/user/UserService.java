package com.larpologic.secretnetwork.user;

import com.larpologic.secretnetwork.role.RoleService;
import com.larpologic.secretnetwork.role.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public List<UserDto> getAllUsersAsDto() {
        return userRepository.findAll().stream()
                .map(userMapper::convertToUserDto)
                .toList();    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public UUID getUserIdByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        return user.getUuid();
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findById(UUID userId) {
        return userRepository.findById(userId);
    }

    public UserDto findByIdAsDto(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + userId));
        return userMapper.convertToUserDto(user);
    }

    @Transactional
    public void createUser(String username, String password, String roleName) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        Role role = roleService.findByName(roleName);
        if (role == null) {
            role = new Role();
            role.setName(roleName);
            roleService.save(role);
        }

        user.setRoles(new HashSet<>(Collections.singletonList(role)));
        userRepository.save(user);
    }

    @Transactional
    public void updateUserPassword(UUID userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + userId));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public void updateUserRoles(UUID userId, Set<String> newRoleNames) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + userId));

        Set<Role> newRoles = new HashSet<>();
        for (String roleName : newRoleNames) {
            Role role = roleService.findByName(roleName);
            if (role == null) {
                throw new IllegalArgumentException("Role not found: " + roleName);
            }
            newRoles.add(role);
        }
        user.setRoles(newRoles);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }
}