package com.larpologic.secretnetwork.conversation;


import com.larpologic.secretnetwork.channel.repository.ChannelRepository;
import com.larpologic.secretnetwork.chat.OpenRouterClient;
import com.larpologic.secretnetwork.chat.OpenRouterRequest;
import com.larpologic.secretnetwork.conversation.dto.ConversationDto;
import com.larpologic.secretnetwork.conversation.dto.MessageRequest;
import com.larpologic.secretnetwork.conversation.dto.MessageResponse;
import com.larpologic.secretnetwork.channel.Channel;
import com.larpologic.secretnetwork.conversation.entity.Conversation;
import com.larpologic.secretnetwork.userchannel.UserChannel;
import com.larpologic.secretnetwork.userchannel.UserChannelKey;

import com.larpologic.secretnetwork.conversation.repository.ConversationRepository;
import com.larpologic.secretnetwork.user.UserRepository;
import com.larpologic.secretnetwork.user.User;
import com.larpologic.secretnetwork.userchannel.repository.UserChannelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

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


    public List<ConversationDto> getConversationHistory(String username, String channelName, int limit) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        Optional<Channel> channelOptional = Optional.ofNullable(channelRepository.findByName(channelName));

        if (userOptional.isEmpty() || channelOptional.isEmpty()) {
            return List.of();
        }

        User user = userOptional.get();
        Channel channel = channelOptional.get();

        return conversationRepository.findLastConversationsByUserAndChannel(limit, user.getUuid(), channel.getId()).stream()
                .map(this::convertToConversationDto)
                .collect(Collectors.toList());
    }

    private ConversationDto convertToConversationDto(Conversation conversation) {
        ConversationDto dto = new ConversationDto();
        dto.setId(conversation.getId());
        dto.setUserMessage(conversation.getUserMessage());
        dto.setAiResponse(conversation.getAiResponse());
        dto.setCreatedAt(conversation.getCreatedAt());
        return dto;
    }

    @Transactional
    public void clearChannelConversations(UUID channelId) {
        conversationRepository.deleteByChannelId(channelId);
    }
}