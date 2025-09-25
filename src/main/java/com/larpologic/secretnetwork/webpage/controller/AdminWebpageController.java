package com.larpologic.secretnetwork.webpage.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.larpologic.secretnetwork.admin.dto.ChannelDto;
import com.larpologic.secretnetwork.admin.dto.RoleDto;
import com.larpologic.secretnetwork.user.UserDto;
import com.larpologic.secretnetwork.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Controller
@RequestMapping("/admin-panel")
public class AdminWebpageController {

    private final AdminService adminService;
    private final ObjectMapper objectMapper;

    @Autowired
    public AdminWebpageController(AdminService adminService, ObjectMapper objectMapper) {
        this.adminService = adminService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public String showAdminPanel(Model model) throws JsonProcessingException {
        List<UserDto> users = adminService.getAllUsersAsDto();
        List<RoleDto> roles = adminService.getAllRolesAsDto();
        List<ChannelDto> channels = adminService.getAllChannelsAsDto();
        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        model.addAttribute("channels", channels);
        model.addAttribute("usersJson", objectMapper.writeValueAsString(users));
        model.addAttribute("channelsJson", objectMapper.writeValueAsString(channels));
        return "admin/index";
    }

    @PostMapping("/add-user")
    public String addUser(@RequestParam String username, @RequestParam String password, @RequestParam String role) {
        adminService.createUser(username, password, role);
        return "redirect:/admin-panel";
    }

    @PostMapping("/add-role")
    public String addRole(@RequestParam String roleName) {
        adminService.createRole(roleName);
        return "redirect:/admin-panel";
    }

    @PostMapping("/update-password")
    public String updatePassword(@RequestParam UUID userId, @RequestParam String newPassword) {
        adminService.updateUserPassword(userId, newPassword);
        return "redirect:/admin-panel";
    }

    @PostMapping("/update-roles")
    public String updateRoles(@RequestParam UUID userId, @RequestParam Set<String> newRoles) {
        adminService.updateUserRoles(userId, newRoles);
        return "redirect:/admin-panel";
    }

    @PostMapping("/delete-user")
    public String deleteUser(@RequestParam UUID userId) {
        adminService.deleteUser(userId);
        return "redirect:/admin-panel";
    }

    @PostMapping("/add-channel")
    public String addChannel(@RequestParam String channelName, @RequestParam String systemPrompt) {
        adminService.createChannel(channelName, systemPrompt);
        return "redirect:/admin-panel";
    }

    @PostMapping("/update-system-prompt")
    public String updateSystemPrompt(@RequestParam UUID channelId, @RequestParam String systemPrompt) {
        adminService.updateChannelSystemPrompt(channelId, systemPrompt);
        return "redirect:/admin-panel";
    }

    @PostMapping("/update-channel-name")
    public String updateChannelName(@RequestParam UUID channelId, @RequestParam String channelName) {
        adminService.updateChannelName(channelId, channelName);
        return "redirect:/admin-panel";
    }

    @PostMapping("/delete-channel")
    public String deleteChannel(@RequestParam UUID channelId) {
        adminService.deleteChannel(channelId);
        return "redirect:/admin-panel";
    }

    @PostMapping("/assign-users-to-channel")
    public String assignUsersToChannel(
            @RequestParam UUID channelId,
            @RequestParam Map<String, String> formData) {
        adminService.assignUsersToChannel(channelId, formData);
        return "redirect:/admin-panel";
    }

    @PostMapping("/reset-user-limit")
    public String resetUserLimit(@RequestParam UUID channelId, @RequestParam UUID userId, RedirectAttributes redirectAttributes) {
        try {
            adminService.resetUserLimit(channelId, userId);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin-panel";
    }

    @PostMapping("/update-user-limit")
    public String updateUserLimit(@RequestParam UUID channelId, @RequestParam UUID userId, @RequestParam Integer newLimit, RedirectAttributes redirectAttributes) {
        try {
            adminService.updateUserLimit(channelId, userId, newLimit);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin-panel";
    }

    @PostMapping("/clear-channel-conversations")
    public String clearChannelConversations(@RequestParam UUID channelId) {
        adminService.clearChannelConversations(channelId);
        return "redirect:/admin-panel";
    }
}