package com.larpologic.secretnetwork.api;

import com.larpologic.secretnetwork.api.information.InformationService;
import com.larpologic.secretnetwork.api.information.dto.ChannelLimitDto;
import com.larpologic.secretnetwork.api.information.dto.UserInChannelDto;
import com.larpologic.secretnetwork.api.information.dto.UserWithChannelsDto;
import com.larpologic.secretnetwork.conversation.ConversationService;
import com.larpologic.secretnetwork.conversation.dto.ConversationDto;
import com.larpologic.secretnetwork.conversation.dto.MessageRequest;
import com.larpologic.secretnetwork.conversation.dto.MessageResponse;
import com.larpologic.secretnetwork.summary.SummaryService;
import com.larpologic.secretnetwork.summary.dto.SummaryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final ConversationService conversationService;
    private final InformationService informationService;
    private final SummaryService summaryService;


    public ApiController(ConversationService conversationService, InformationService informationService, SummaryService summaryService) {
        this.conversationService = conversationService;
        this.informationService = informationService;
        this.summaryService = summaryService;
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
    @GetMapping("/user/{username}/channel/{channelName}/summary")
    public ResponseEntity<SummaryDto> getSummaryForUserInChannel(@PathVariable String username, @PathVariable String channelName) {
        SummaryDto summary = summaryService.getSummaryForUserInChannel(username, channelName);
        return ResponseEntity.ok(summary);
    }
}