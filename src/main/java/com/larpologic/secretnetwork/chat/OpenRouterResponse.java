package com.larpologic.secretnetwork.chat;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class OpenRouterResponse {
    private List<Choice> choices;

    @Getter
    @Setter
    public static class Choice {
        private Message message;
    }

    @Getter
    @Setter
    public static class Message {
        private String content;
    }
}