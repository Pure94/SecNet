package com.larpologic.secretnetwork.conversation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MessageResponse {
    private String response;
    private Integer remainingLimit;
}