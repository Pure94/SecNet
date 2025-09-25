package com.larpologic.secretnetwork.channel;

import com.larpologic.secretnetwork.channel.dto.ChannelDto;
import com.larpologic.secretnetwork.channel.repository.ChannelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final ChannelMapper channelMapper;

    public ChannelService(ChannelRepository channelRepository, ChannelMapper channelMapper) {
        this.channelRepository = channelRepository;
        this.channelMapper = channelMapper;
    }

    @Transactional
    public void createChannel(String name, String systemPrompt) {
        Channel channel = new Channel();
        channel.setName(name);
        channel.setSystemPrompt(systemPrompt);
        channelRepository.save(channel);
    }

    @Transactional
    public void updateChannelSystemPrompt(UUID id, String systemPrompt) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid channel Id: " + id));
        channel.setSystemPrompt(systemPrompt);
        channelRepository.save(channel);
    }

    @Transactional
    public void updateChannelName(UUID id, String name) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid channel Id: " + id));
        channel.setName(name);
        channelRepository.save(channel);
    }

    @Transactional
    public void deleteChannel(UUID channelId) {
        channelRepository.deleteById(channelId);
    }


    public ChannelDto getChannelByNameAsDto(String channelName) {
        return channelMapper.convertToChannelDto(channelRepository.findByName(channelName));
    }

    public UUID getChannelIdByChannelName(String channelName) {
        Channel channel = channelRepository.findByName(channelName);
        if (channel == null) {
            throw new IllegalArgumentException("Channel not found: " + channelName);
        }
        return channel.getId();
    }

    public List<ChannelDto> getAllChannelsAsDto() {
        return channelRepository.findAll().stream()
                .map(channelMapper::convertToChannelDto)
                .toList();
    }

    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    public Channel findById(UUID channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid channel Id: " + channelId));
    }

    public ChannelDto findByIdAsDto(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid channel Id: " + channelId));
        return channelMapper.convertToChannelDto(channel);
    }

    public Channel findByName(String name) {
        return channelRepository.findByName(name);
    }
}