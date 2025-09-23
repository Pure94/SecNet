package com.larpologic.secretnetwork.controller;

import com.larpologic.secretnetwork.conversation.ConversationService;
import com.larpologic.secretnetwork.conversation.dto.MessageRequest;
import com.larpologic.secretnetwork.conversation.dto.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ConversationController {

    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping("/send-message")
    public ResponseEntity<MessageResponse> sendMessage(@RequestBody MessageRequest request) {
        MessageResponse response = conversationService.handleMessage(request);
        return ResponseEntity.ok(response);
    }
}