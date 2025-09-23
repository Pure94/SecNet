package com.larpologic.secretnetwork.controller;

import com.larpologic.secretnetwork.conversation.InformationService;
import com.larpologic.secretnetwork.conversation.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/info-api")
public class InformationController {

    private final InformationService informationService;

    public InformationController(InformationService informationService) {
        this.informationService = informationService;
    }

    @GetMapping("/user/{username}/channels")
    public ResponseEntity<List<ChannelLimitDto>> getUserChannels(@PathVariable String username) {
        List<ChannelLimitDto> channels = informationService.getUserChannelsWithLimit(username);
        return ResponseEntity.ok(channels);
    }

    @GetMapping("/channel/{channelName}/users")
    public ResponseEntity<List<UserInChannelDto>> getUsersInChannel(@PathVariable String channelName) {
        List<UserInChannelDto> users = informationService.getUsersInChannel(channelName);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/{username}/channel/{channelName}/history/{limit}")
    public ResponseEntity<List<ConversationDto>> getConversationHistory(
            @PathVariable String username,
            @PathVariable String channelName,
            @PathVariable int limit) {
        List<ConversationDto> history = informationService.getConversationHistory(username, channelName, limit);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/all-users")
    public ResponseEntity<List<UserWithChannelsDto>> getAllUsersWithChannelsAndLimits() {
        List<UserWithChannelsDto> users = informationService.getAllUsersWithChannelsAndLimits();
        return ResponseEntity.ok(users);
    }
}