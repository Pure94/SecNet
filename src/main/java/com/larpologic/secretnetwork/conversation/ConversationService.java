package com.larpologic.secretnetwork.conversation;


import com.larpologic.secretnetwork.chat.OpenRouterClient;
import com.larpologic.secretnetwork.chat.OpenRouterRequest;
import com.larpologic.secretnetwork.security.UserRepository;
import com.larpologic.secretnetwork.security.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ConversationService {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final UserChannelRepository userChannelRepository;
    private final ConversationRepository conversationRepository;
    private final OpenRouterClient openRouterClient;

    public ConversationService(UserRepository userRepository, ChannelRepository channelRepository, UserChannelRepository userChannelRepository, ConversationRepository conversationRepository, OpenRouterClient openRouterClient) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
        this.userChannelRepository = userChannelRepository;
        this.conversationRepository = conversationRepository;
        this.openRouterClient = openRouterClient;
    }

    @Transactional
    public MessageResponse handleMessage(MessageRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElse(null);

        if (user == null) {
            return new MessageResponse("User not found.", 0);
        }

        Channel channel = Optional.ofNullable(channelRepository.findByName(request.getChannelName()))
                .orElseGet(() -> channelRepository.findByName("default"));

        if (channel == null) {
            return new MessageResponse("Channel not found and no default channel is available.", 0);
        }

        UserChannelKey key = new UserChannelKey();
        key.setUser(user.getUuid());
        key.setChannel(channel.getId());

        Optional<UserChannel> userChannelOptional = userChannelRepository.findById(key);

        if (userChannelOptional.isEmpty()) {
            return new MessageResponse("User is not authorized to use this channel.", 0);
        }

        UserChannel userChannel = userChannelOptional.get();
        if (userChannel.getRemainingLimit() <= 0) {
            return new MessageResponse("User has exceeded the message limit for this channel.", 0);
        }

        userChannel.setRemainingLimit(userChannel.getRemainingLimit() - 1);
        userChannelRepository.save(userChannel);

        List<Conversation> history = conversationRepository.findLastConversationsByUserAndChannel(10, user.getUuid(), channel.getId());
        Collections.reverse(history);

        List<OpenRouterRequest.Message> messages = new ArrayList<>();

        String systemPrompt = Optional.ofNullable(channel.getSystemPrompt()).orElse("Jesteś pomocnym asystentem AI.");
        messages.add(new OpenRouterRequest.Message("system", List.of(new OpenRouterRequest.Content("text", systemPrompt, null))));

        for (Conversation conv : history) {
            messages.add(new OpenRouterRequest.Message("user", List.of(new OpenRouterRequest.Content("text", conv.getUserMessage(), null))));
            messages.add(new OpenRouterRequest.Message("assistant", List.of(new OpenRouterRequest.Content("text", conv.getAiResponse(), null))));
        }

        messages.add(new OpenRouterRequest.Message("user", List.of(new OpenRouterRequest.Content("text", request.getMessage(), null))));

        OpenRouterRequest openRouterRequest = new OpenRouterRequest();
        openRouterRequest.setModel("google/gemini-2.5-flash");
        openRouterRequest.setMessages(messages);

        String aiResponse = openRouterClient.getCompletion(openRouterRequest);

        Conversation newConversation = new Conversation();
        newConversation.setUser(user);
        newConversation.setChannel(channel);
        newConversation.setUserMessage(request.getMessage());
        newConversation.setAiResponse(aiResponse);
        newConversation.setCreatedAt(OffsetDateTime.now());
        conversationRepository.save(newConversation);

        return new MessageResponse(aiResponse, userChannel.getRemainingLimit());
    }
}