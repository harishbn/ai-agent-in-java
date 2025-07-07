package com.example.demo.controller;

import com.example.demo.pojo.ChatRequest;
import com.example.demo.pojo.ChatResponse;
import com.example.demo.service.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) throws JsonProcessingException {
        String chatId = request.getChatId()!=null ? request.getChatId() : UUID.randomUUID().toString();

        String aiResponse = chatService.getChatResponse(chatId, request);
        return new ChatResponse(chatId, aiResponse);
    }
} 