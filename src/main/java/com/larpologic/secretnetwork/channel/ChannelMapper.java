package com.larpologic.secretnetwork.channel;

import com.larpologic.secretnetwork.channel.dto.ChannelDto;
import com.larpologic.secretnetwork.userchannel.UserChannelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ChannelMapper {

    private final UserChannelMapper userChannelMapper;

    public ChannelMapper(UserChannelMapper userChannelMapper) {
        this.userChannelMapper = userChannelMapper;
    }

    public ChannelDto convertToChannelDto(Channel channel) {
        ChannelDto channelDto = new ChannelDto();
        channelDto.setId(channel.getId());
        channelDto.setName(channel.getName());
        channelDto.setSystemPrompt(channel.getSystemPrompt());
        channelDto.setUserChannels(channel.getUserChannels().stream()
                .map(userChannelMapper::convertToUserChannelDto)
                .collect(Collectors.toSet()));
        return channelDto;
    }
}