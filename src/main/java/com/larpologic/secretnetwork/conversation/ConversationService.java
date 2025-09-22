package com.larpologic.secretnetwork.conversation;

import com.larpologic.secretnetwork.security.UserRepository;
import com.larpologic.secretnetwork.security.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ConversationService {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final UserChannelRepository userChannelRepository;

    public ConversationService(UserRepository userRepository, ChannelRepository channelRepository, UserChannelRepository userChannelRepository) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
        this.userChannelRepository = userChannelRepository;
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

        String mockResponse = "test";
        return new MessageResponse(mockResponse, userChannel.getRemainingLimit());
    }
}