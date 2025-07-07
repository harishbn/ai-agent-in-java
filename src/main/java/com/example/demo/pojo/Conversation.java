package com.example.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Conversation {
    private String chatId;
    private List<ConvMessage> messages = new ArrayList<>();

    public Conversation(String chatId) {
        this.chatId = chatId;
    }
}
