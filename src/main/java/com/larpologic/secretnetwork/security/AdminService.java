package com.larpologic.secretnetwork.security;

import com.larpologic.secretnetwork.conversation.dto.ChannelDto;
import com.larpologic.secretnetwork.conversation.dto.RoleDto;
import com.larpologic.secretnetwork.conversation.dto.UserChannelDto;
import com.larpologic.secretnetwork.conversation.dto.UserDto;
import com.larpologic.secretnetwork.conversation.entity.Channel;
import com.larpologic.secretnetwork.conversation.entity.UserChannel;
import com.larpologic.secretnetwork.conversation.entity.UserChannelKey;
import com.larpologic.secretnetwork.conversation.repository.ChannelRepository;
import com.larpologic.secretnetwork.conversation.repository.ConversationRepository;
import com.larpologic.secretnetwork.conversation.repository.UserChannelRepository;
import com.larpologic.secretnetwork.security.entity.Role;
import com.larpologic.secretnetwork.security.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ChannelRepository channelRepository;
    private final UserChannelRepository userChannelRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConversationRepository conversationRepository;

    public AdminService(UserRepository userRepository, RoleRepository roleRepository, ChannelRepository channelRepository, UserChannelRepository userChannelRepository, PasswordEncoder passwordEncoder, ConversationRepository conversationRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.channelRepository = channelRepository;
        this.userChannelRepository = userChannelRepository;
        this.passwordEncoder = passwordEncoder;
        this.conversationRepository = conversationRepository;
    }

    @Transactional
    public void createUser(String username, String password, String roleName) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }

        user.setRoles(new HashSet<>(Collections.singletonList(role)));
        userRepository.save(user);
    }

    @Transactional
    public void createRole(String roleName) {
        Role role = new Role();
        role.setName(roleName);
        roleRepository.save(role);
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
            Role role = roleRepository.findByName(roleName);
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

    @Transactional
    public void createChannel(String name, String systemPrompt) {
        Channel channel = new Channel();
        channel.setName(name);
        channel.setSystemPrompt(systemPrompt);
        channelRepository.save(channel);
    }

    @Transactional
    public void updateChannelSystemPrompt(UUID id, String systemPrompt) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid channel Id: " + id));
        channel.setSystemPrompt(systemPrompt);
        channelRepository.save(channel);
    }

    @Transactional
    public void updateChannelName(UUID id, String name) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid channel Id: " + id));
        channel.setName(name);
        channelRepository.save(channel);
    }

    @Transactional
    public void deleteChannel(UUID channelId) {
        channelRepository.deleteById(channelId);
    }

    @Transactional
    public void assignUsersToChannel(UUID channelId, Map<String, String> formData) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid channel Id: " + channelId));

        userChannelRepository.deleteAll(userChannelRepository.findByChannel(channel));

        for (Map.Entry<String, String> entry : formData.entrySet()) {
            if (entry.getKey().startsWith("user_") && entry.getValue().equals("on")) {
                UUID userId = UUID.fromString(entry.getKey().substring(5));
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + userId));

                String limitKey = "limit_" + userId.toString();
                Integer limit = 100;
                if (formData.containsKey(limitKey)) {
                    limit = Integer.parseInt(formData.get(limitKey));
                }

                UserChannelKey userChannelKey = new UserChannelKey();
                userChannelKey.setUser(user.getUuid());
                userChannelKey.setChannel(channel.getId());

                UserChannel userChannel = new UserChannel();
                userChannel.setId(userChannelKey);
                userChannel.setUser(user);
                userChannel.setChannel(channel);
                userChannel.setRemainingLimit(limit);
                userChannelRepository.save(userChannel);
            }
        }
    }

    @Transactional
    public void updateUserLimit(UUID channelId, UUID userId, Integer newLimit) {
        UserChannelKey userChannelKey = new UserChannelKey();
        userChannelKey.setChannel(channelId);
        userChannelKey.setUser(userId);

        Optional<UserChannel> optionalUserChannel = userChannelRepository.findById(userChannelKey);

        if (optionalUserChannel.isPresent()) {
            UserChannel userChannel = optionalUserChannel.get();
            userChannel.setRemainingLimit(newLimit);
            userChannelRepository.save(userChannel);
        } else {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found."));
            Channel channel = channelRepository.findById(channelId)
                    .orElseThrow(() -> new IllegalArgumentException("Channel not found."));
            UserChannel newUserChannel = new UserChannel();
            newUserChannel.setId(userChannelKey);
            newUserChannel.setUser(user);
            newUserChannel.setChannel(channel);
            newUserChannel.setRemainingLimit(newLimit);
            userChannelRepository.save(newUserChannel);
        }
    }


    @Transactional
    public void resetUserLimit(UUID channelId, UUID userId) {
        UserChannelKey userChannelKey = new UserChannelKey();
        userChannelKey.setChannel(channelId);
        userChannelKey.setUser(userId);
        UserChannel userChannel = userChannelRepository.findById(userChannelKey)
                .orElseThrow(() -> new IllegalArgumentException("User-Channel association not found."));

        userChannel.setRemainingLimit(100);
        userChannelRepository.save(userChannel);
    }

    // Konwersja na DTO
    public List<UserDto> getAllUsersAsDto() {
        return userRepository.findAll().stream()
                .map(this::convertToUserDto)
                .collect(Collectors.toList());
    }

    public List<RoleDto> getAllRolesAsDto() {
        return roleRepository.findAll().stream()
                .map(this::convertToRoleDto)
                .collect(Collectors.toList());
    }

    public List<ChannelDto> getAllChannelsAsDto() {
        return channelRepository.findAll().stream()
                .map(this::convertToChannelDto)
                .collect(Collectors.toList());
    }

    private UserDto convertToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUuid(user.getUuid());
        userDto.setUsername(user.getUsername());
        userDto.setRoles(user.getRoles().stream()
                .map(this::convertToRoleDto)
                .collect(Collectors.toSet()));
        return userDto;
    }

    private RoleDto convertToRoleDto(Role role) {
        RoleDto roleDto = new RoleDto();
        roleDto.setUuid(role.getUuid());
        roleDto.setName(role.getName());
        return roleDto;
    }

    private ChannelDto convertToChannelDto(Channel channel) {
        ChannelDto channelDto = new ChannelDto();
        channelDto.setId(channel.getId());
        channelDto.setName(channel.getName());
        channelDto.setSystemPrompt(channel.getSystemPrompt());
        channelDto.setUserChannels(channel.getUserChannels().stream()
                .map(this::convertToUserChannelDto)
                .collect(Collectors.toSet()));
        return channelDto;
    }

    private UserChannelDto convertToUserChannelDto(UserChannel userChannel) {
        UserChannelDto userChannelDto = new UserChannelDto();
        userChannelDto.setUser(convertToUserDto(userChannel.getUser()));
        userChannelDto.setRemainingLimit(userChannel.getRemainingLimit());
        return userChannelDto;
    }


    @Transactional
    public void clearChannelConversations(UUID channelId) {
        conversationRepository.deleteByChannelId(channelId);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }
}