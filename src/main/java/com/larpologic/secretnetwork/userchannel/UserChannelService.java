package com.larpologic.secretnetwork.userchannel;

import com.larpologic.secretnetwork.channel.Channel;
import com.larpologic.secretnetwork.channel.ChannelService;
import com.larpologic.secretnetwork.user.User;
import com.larpologic.secretnetwork.user.UserService;
import com.larpologic.secretnetwork.userchannel.repository.UserChannelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserChannelService {

    private final UserChannelRepository userChannelRepository;
    private final UserService userService;
    private final ChannelService channelService;

    public UserChannelService(UserChannelRepository userChannelRepository, UserService userService, ChannelService channelService) {
        this.userChannelRepository = userChannelRepository;
        this.userService = userService;
        this.channelService = channelService;
    }

    @Transactional
    public void assignUsersToChannel(UUID channelId, Map<String, String> formData) {
        Channel channel = channelService.findById(channelId);

        userChannelRepository.deleteAll(userChannelRepository.findByChannel(channel));

        for (Map.Entry<String, String> entry : formData.entrySet()) {
            if (entry.getKey().startsWith("user_") && entry.getValue().equals("on")) {
                UUID userId = UUID.fromString(entry.getKey().substring(5));
                User user = userService.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + userId));

                String limitKey = "limit_" + userId.toString();
                Integer limit = 100;
                if (formData.containsKey(limitKey)) {
                    limit = Integer.parseInt(formData.get(limitKey));
                }

                UserChannelKey userChannelKey = new UserChannelKey();
                userChannelKey.setUser(user.getUuid());
                userChannelKey.setChannel(channel.getId());

                UserChannel userChannel = new UserChannel();
                userChannel.setId(userChannelKey);
                userChannel.setUser(user);
                userChannel.setChannel(channel);
                userChannel.setRemainingLimit(limit);
                userChannelRepository.save(userChannel);
            }
        }
    }

    @Transactional
    public void updateUserLimit(UUID channelId, UUID userId, Integer newLimit) {
        UserChannelKey userChannelKey = new UserChannelKey();
        userChannelKey.setChannel(channelId);
        userChannelKey.setUser(userId);

        Optional<UserChannel> optionalUserChannel = userChannelRepository.findById(userChannelKey);

        if (optionalUserChannel.isPresent()) {
            UserChannel userChannel = optionalUserChannel.get();
            userChannel.setRemainingLimit(newLimit);
            userChannelRepository.save(userChannel);
        } else {
            User user = userService.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found."));
            Channel channel = channelService.findById(channelId);
            UserChannel newUserChannel = new UserChannel();
            newUserChannel.setId(userChannelKey);
            newUserChannel.setUser(user);
            newUserChannel.setChannel(channel);
            newUserChannel.setRemainingLimit(newLimit);
            userChannelRepository.save(newUserChannel);
        }
    }


    @Transactional
    public void resetUserLimit(UUID channelId, UUID userId) {
        UserChannelKey userChannelKey = new UserChannelKey();
        userChannelKey.setChannel(channelId);
        userChannelKey.setUser(userId);
        UserChannel userChannel = userChannelRepository.findById(userChannelKey)
                .orElseThrow(() -> new IllegalArgumentException("User-Channel association not found."));

        userChannel.setRemainingLimit(100);
        userChannelRepository.save(userChannel);
    }

    public List<UserChannel> findByUser(User user) {
        return userChannelRepository.findByUser(user);
    }

    public List<UserChannel> findByChannel(Channel channel) {
        return userChannelRepository.findByChannel(channel);
    }
}