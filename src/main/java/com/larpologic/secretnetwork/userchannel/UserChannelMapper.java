package com.larpologic.secretnetwork.userchannel;

import com.larpologic.secretnetwork.user.UserMapper;
import com.larpologic.secretnetwork.userchannel.dto.UserChannelDto;
import org.springframework.stereotype.Component;

@Component
public class UserChannelMapper {
    private final UserMapper userMapper;

    public UserChannelMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public UserChannelDto convertToUserChannelDto(UserChannel userChannel) {
        UserChannelDto userChannelDto = new UserChannelDto();
        userChannelDto.setUser(userMapper.convertToUserDto(userChannel.getUser()));
        userChannelDto.setRemainingLimit(userChannel.getRemainingLimit());
        return userChannelDto;
    }
}
