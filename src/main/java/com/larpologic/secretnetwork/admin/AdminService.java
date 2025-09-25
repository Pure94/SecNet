package com.larpologic.secretnetwork.admin;

import com.larpologic.secretnetwork.channel.ChannelService;
import com.larpologic.secretnetwork.channel.dto.ChannelDto;
import com.larpologic.secretnetwork.conversation.ConversationService;
import com.larpologic.secretnetwork.role.RoleService;
import com.larpologic.secretnetwork.role.dto.RoleDto;
import com.larpologic.secretnetwork.user.UserService;
import com.larpologic.secretnetwork.userchannel.UserChannelService;
import com.larpologic.secretnetwork.summary.SummaryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class AdminService {

    private final ConversationService conversationService;
    private final ChannelService channelService;
    private final RoleService roleService;
    private final UserChannelService userChannelService;
    private final UserService userService;
    private final SummaryService summaryService;

    public AdminService(ConversationService conversationService, ChannelService channelService, RoleService roleService, UserChannelService userChannelService, UserService userService, SummaryService summaryService) {
        this.conversationService = conversationService;
        this.channelService = channelService;
        this.roleService = roleService;
        this.userChannelService = userChannelService;
        this.userService = userService;
        this.summaryService = summaryService;
    }

    @Transactional
    public void summarizeConversations() {
        summaryService.summarizeConversations();
    }

    @Transactional
    public void createUser(String username, String password, String roleName) {
        userService.createUser(username, password, roleName);
    }

    @Transactional
    public void createRole(String roleName) {
        roleService.createRole(roleName);
    }

    @Transactional
    public void updateUserPassword(UUID userId, String newPassword) {
        userService.updateUserPassword(userId, newPassword);
    }

    @Transactional
    public void updateUserRoles(UUID userId, Set<String> newRoleNames) {
        userService.updateUserRoles(userId, newRoleNames);
    }

    @Transactional
    public void deleteUser(UUID userId) {
        userService.deleteUser(userId);
    }

    @Transactional
    public void createChannel(String name, String systemPrompt) {
        channelService.createChannel(name, systemPrompt);
    }

    @Transactional
    public void updateChannelSystemPrompt(UUID id, String systemPrompt) {
        channelService.updateChannelSystemPrompt(id, systemPrompt);
    }

    @Transactional
    public void updateChannelName(UUID id, String name) {
        channelService.updateChannelName(id, name);
    }

    @Transactional
    public void deleteChannel(UUID channelId) {
        channelService.deleteChannel(channelId);
    }

    public List<ChannelDto> getAllChannelsAsDto() {
        return channelService.getAllChannelsAsDto();
    }

    @Transactional
    public void assignUsersToChannel(UUID channelId, Map<String, String> formData) {
        userChannelService.assignUsersToChannel(channelId, formData);
    }

    @Transactional
    public void updateUserLimit(UUID channelId, UUID userId, Integer newLimit) {
        userChannelService.updateUserLimit(channelId, userId, newLimit);
    }


    @Transactional
    public void resetUserLimit(UUID channelId, UUID userId) {
        userChannelService.resetUserLimit(channelId, userId);
    }


    public List<RoleDto> getAllRolesAsDto() {
        return roleService.getAllRolesAsDto();
    }

    @Transactional
    public void clearChannelConversations(UUID channelId) {
        conversationService.clearChannelConversations(channelId);
        summaryService.deleteSummariesByChannelId(channelId);
    }

}
