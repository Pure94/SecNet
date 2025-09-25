package com.larpologic.secretnetwork.api;

import com.larpologic.secretnetwork.api.information.dto.ChannelLimitDto;
import com.larpologic.secretnetwork.api.information.dto.UserInChannelDto;
import com.larpologic.secretnetwork.api.information.dto.UserWithChannelsDto;
import com.larpologic.secretnetwork.conversation.ConversationService;
import com.larpologic.secretnetwork.api.information.InformationService;
import com.larpologic.secretnetwork.conversation.dto.ConversationDto;
import com.larpologic.secretnetwork.conversation.dto.MessageRequest;
import com.larpologic.secretnetwork.conversation.dto.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final ConversationService conversationService;
    private final InformationService informationService;


    public ApiController(ConversationService conversationService, InformationService informationService) {
        this.conversationService = conversationService;
        this.informationService = informationService;
    }

    @PostMapping("/send-message")
    public ResponseEntity<MessageResponse> sendMessage(@RequestBody MessageRequest request) {
        MessageResponse response = conversationService.handleMessage(request);
        return ResponseEntity.ok(response);
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
        List<ConversationDto> history = conversationService.getConversationHistory(username, channelName, limit);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/all-users")
    public ResponseEntity<List<UserWithChannelsDto>> getAllUsersWithChannelsAndLimits() {
        List<UserWithChannelsDto> users = informationService.getAllUsersWithChannelsAndLimits();
        return ResponseEntity.ok(users);
    }
}