package com.larpologic.secretnetwork.conversation.repository;


import com.larpologic.secretnetwork.channel.Channel;
import com.larpologic.secretnetwork.userchannel.UserChannel;
import com.larpologic.secretnetwork.userchannel.UserChannelKey;
import com.larpologic.secretnetwork.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserChannelRepository extends JpaRepository<UserChannel, UserChannelKey> {
    List<UserChannel> findByChannel(Channel channel);
    List<UserChannel> findByUser(User user);
    Optional<UserChannel> findByChannelIdAndUserUuid(UUID channelId, UUID userId);
}