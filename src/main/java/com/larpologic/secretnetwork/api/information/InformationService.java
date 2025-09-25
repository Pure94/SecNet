package com.larpologic.secretnetwork.api.information;

import com.larpologic.secretnetwork.api.information.dto.ChannelLimitDto;
import com.larpologic.secretnetwork.api.information.dto.UserInChannelDto;
import com.larpologic.secretnetwork.api.information.dto.UserWithChannelsDto;
import com.larpologic.secretnetwork.channel.Channel;
import com.larpologic.secretnetwork.channel.ChannelService;
import com.larpologic.secretnetwork.user.User;
import com.larpologic.secretnetwork.user.UserService;
import com.larpologic.secretnetwork.userchannel.UserChannel;
import com.larpologic.secretnetwork.userchannel.UserChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InformationService {

    private final UserChannelService userChannelService;
    private final UserService userService;
    private final ChannelService channelService;

    public List<ChannelLimitDto> getUserChannelsWithLimit(String username) {
        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isEmpty()) {
            return List.of();
        }
        User user = userOptional.get();
        return userChannelService.findByUser(user).stream()
                .map(this::convertToChannelLimitDto)
                .toList();
    }

    public List<UserInChannelDto> getUsersInChannel(String channelName) {
        Optional<Channel> channelOptional = Optional.ofNullable(channelService.findByName(channelName));
        if (channelOptional.isEmpty()) {
            return List.of();
        }
        Channel channel = channelOptional.get();
        return userChannelService.findByChannel(channel).stream()
                .map(this::convertToUserInChannelDto)
                .toList();
    }


    public List<UserWithChannelsDto> getAllUsersWithChannelsAndLimits() {
        return userService.getAllUsers().stream()
                .map(this::convertToUserWithChannelsDto)
                .toList();
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
        List<ChannelLimitDto> channels = userChannelService.findByUser(user).stream()
                .map(this::convertToChannelLimitDto)
                .toList();
        dto.setChannels(channels);
        return dto;
    }
}
