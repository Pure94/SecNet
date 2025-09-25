package com.larpologic.secretnetwork.summary;

import com.larpologic.secretnetwork.channel.Channel;
import com.larpologic.secretnetwork.channel.ChannelService;
import com.larpologic.secretnetwork.channel.dto.ChannelDto;
import com.larpologic.secretnetwork.chat.OpenRouterClient;
import com.larpologic.secretnetwork.chat.OpenRouterRequest;
import com.larpologic.secretnetwork.conversation.ConversationService;
import com.larpologic.secretnetwork.conversation.dto.ConversationDto;
import com.larpologic.secretnetwork.summary.dto.SummaryDto;
import com.larpologic.secretnetwork.summary.entity.Summary;
import com.larpologic.secretnetwork.summary.mapper.SummaryMapper;
import com.larpologic.secretnetwork.summary.repository.SummaryRepository;
import com.larpologic.secretnetwork.user.UserDto;
import com.larpologic.secretnetwork.user.UserService;
import com.larpologic.secretnetwork.userchannel.UserChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SummaryService {

    private final SummaryRepository summaryRepository;
    private final OpenRouterClient openRouterClient;
    private final SummaryMapper summaryMapper;
    private final ConversationService conversationService;
    private final UserService userService;
    private final ChannelService channelService;
    private final UserChannelService userChannelService;


    private static final String SYSTEM_PROMPT =
            """
            You are an expert summarizer.
            our task is to provide a concise, clear, and accurate summary of the given conversation.
            The summary should capture the main points, decisions, and action items. 
            Do not include any personal opinions or interpretations.
            The summary should be easy to read and understand for someone who has not read the full conversation.";
            """;

    public void summarizeConversations() {
        log.info("Starting conversation summarization process.");
        List<Channel> channels = channelService.getAllChannels();
        for (Channel channel : channels) {
            userChannelService.findByChannel(channel).forEach(userChannel -> {
                summarizeUserInChannel(userService.findByIdAsDto(userChannel.getUser().getUuid()), channelService.findByIdAsDto(channel.getId()));
            });
        }
        log.info("Finished conversation summarization process.");
    }


    private void summarizeUserInChannel(UserDto user, ChannelDto channel) {
        log.info("Summarizing conversation for user '{}' in channel '{}'", user.getUsername(), channel.getName());
        List<ConversationDto> conversations = conversationService.getConversationHistory(user.getUsername(), channel.getName(), 1000);
        if (conversations.isEmpty()) {
            log.info("No conversations to summarize for user '{}' in channel '{}'", user.getUsername(), channel.getName());
            return;
        }

        Summary lastSummary = summaryRepository.findByChannelIdAndUserId(channel.getId(), user.getUuid()).orElse(null);

        if (lastSummary != null && lastSummary.getCreatedAt().isAfter(conversations.get(conversations.size() - 1).getCreatedAt().toLocalDateTime())) {
            log.info("No new messages to summarize for user '{}' in channel '{}'", user.getUsername(), channel.getName());
            return;
        }

        StringBuilder conversationText = new StringBuilder();
        for (ConversationDto conversation : conversations) {
            conversationText.append(user.getUsername()).append(": ").append(conversation.getUserMessage()).append("\n");
            conversationText.append("Response: ").append(conversation.getAiResponse()).append("\n");
        }

        OpenRouterRequest request = new OpenRouterRequest("mistralai/mistral-7b-instruct", List.of(new OpenRouterRequest.Message("system", List.of(new OpenRouterRequest.Content("text", SYSTEM_PROMPT, null))), new OpenRouterRequest.Message("user", List.of(new OpenRouterRequest.Content("text", conversationText.toString(), null)))));
        String summaryText = openRouterClient.getCompletion(request);

        Summary summary = new Summary();
        summary.setChannel(channelService.findById(channel.getId()));
        summary.setUser(userService.findById(user.getUuid()).get());
        summary.setSummary(summaryText);
        summary.setCreatedAt(LocalDateTime.now());

        summaryRepository.save(summary);
        log.info("Successfully summarized conversation for user '{}' in channel '{}'", user.getUsername(), channel.getName());
    }

    public SummaryDto getSummaryForUserInChannel(String username, String channelName) {
        UUID userId = userService.getUserIdByUsername(username);
        UUID channelId = channelService.getChannelIdByChannelName(channelName);
        log.info("Requesting summary for user '{}' in channel '{}'", username, channelName);
        Summary summary = summaryRepository.findByChannelIdAndUserId(channelId, userId).orElse(null);
        if (summary == null) {
            log.info("No summary found for user '{}' in channel '{}'. Generating a new one.", username, channelName);
            UserDto user = userService.findByIdAsDto(userId);
            ChannelDto channel = channelService.findByIdAsDto(channelId);
            summarizeUserInChannel(user, channel);
            summary = summaryRepository.findByChannelIdAndUserId(channelId, userId).orElse(null);
        }
        return summaryMapper.toDto(summary);

    }

    public void deleteSummariesByChannelId(UUID channelId) {
        summaryRepository.deleteByChannelId(channelId);
    }
}
