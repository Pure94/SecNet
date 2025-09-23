package com.larpologic.secretnetwork.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OpenRouterRequest {
    private String model;
    private List<Message> messages;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Message {
        private String role;
        private List<Content> content;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Content {
        private String type;
        private String text;
        private ImageUrl imageUrl;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ImageUrl {
        private String url;
    }
}