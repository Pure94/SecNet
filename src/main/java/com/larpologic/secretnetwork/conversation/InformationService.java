package com.larpologic.secretnetwork.conversation;

import com.larpologic.secretnetwork.conversation.dto.*;
import com.larpologic.secretnetwork.conversation.entity.Channel;
import com.larpologic.secretnetwork.conversation.entity.Conversation;
import com.larpologic.secretnetwork.conversation.entity.UserChannel;
import com.larpologic.secretnetwork.conversation.repository.ChannelRepository;
import com.larpologic.secretnetwork.conversation.repository.ConversationRepository;
import com.larpologic.secretnetwork.conversation.repository.UserChannelRepository;
import com.larpologic.secretnetwork.security.UserRepository;
import com.larpologic.secretnetwork.security.entity.User;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InformationService {

    private final UserChannelRepository userChannelRepository;
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final ChannelRepository channelRepository;

    public InformationService(UserChannelRepository userChannelRepository, UserRepository userRepository, ConversationRepository conversationRepository, ChannelRepository channelRepository) {
        this.userChannelRepository = userChannelRepository;
        this.userRepository = userRepository;
        this.conversationRepository = conversationRepository;
        this.channelRepository = channelRepository;
    }

    public List<ChannelLimitDto> getUserChannelsWithLimit(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return List.of();
        }
        User user = userOptional.get();
        return userChannelRepository.findByUser(user).stream()
                .map(this::convertToChannelLimitDto) // Użycie nowego DTO
                .collect(Collectors.toList());
    }

    public List<UserInChannelDto> getUsersInChannel(String channelName) {
        Optional<Channel> channelOptional = Optional.ofNullable(channelRepository.findByName(channelName));
        if (channelOptional.isEmpty()) {
            return List.of();
        }
        Channel channel = channelOptional.get();
        return userChannelRepository.findByChannel(channel).stream()
                .map(this::convertToUserInChannelDto)
                .collect(Collectors.toList());
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

    public List<UserWithChannelsDto> getAllUsersWithChannelsAndLimits() {
        return userRepository.findAll().stream()
                .map(this::convertToUserWithChannelsDto)
                .collect(Collectors.toList());
    }

    private UserDto convertToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUuid(user.getUuid());
        userDto.setUsername(user.getUsername());
        return userDto;
    }

    private UserChannelDto convertToUserChannelDto(UserChannel userChannel) {
        UserChannelDto dto = new UserChannelDto();
        dto.setRemainingLimit(userChannel.getRemainingLimit());
        dto.setUser(convertToUserDto(userChannel.getUser()));
        return dto;
    }

    private ConversationDto convertToConversationDto(Conversation conversation) {
        ConversationDto dto = new ConversationDto();
        dto.setId(conversation.getId());
        dto.setUserMessage(conversation.getUserMessage());
        dto.setAiResponse(conversation.getAiResponse());
        dto.setCreatedAt(conversation.getCreatedAt());
        return dto;
    }

    private ChannelLimitDto convertToChannelLimitDto(UserChannel userChannel) {
        ChannelLimitDto dto = new ChannelLimitDto();
        dto.setChannelName(userChannel.getChannel().getName());
        dto.setRemainingLimit(userChannel.getRemainingLimit());
        return dto;
    }
    private UserInChannelDto convertToUserInChannelDto(UserChannel userChannel) {
        UserInChannelDto dto = new UserInChannelDto();
        dto.setUsername(userChannel.getUser().getUsername());
        dto.setRemainingLimit(userChannel.getRemainingLimit());
        return dto;
    }

    private UserWithChannelsDto convertToUserWithChannelsDto(User user) {
        UserWithChannelsDto dto = new UserWithChannelsDto();
        dto.setUuid(user.getUuid());
        dto.setUsername(user.getUsername());
        List<ChannelLimitDto> channels = userChannelRepository.findByUser(user).stream()
                .map(this::convertToChannelLimitDto)
                .collect(Collectors.toList());
        dto.setChannels(channels);
        return dto;
    }
}