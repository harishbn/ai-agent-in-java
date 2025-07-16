package com.example.demo.controller;

import com.example.demo.pojo.ChatRequest;
import com.example.demo.pojo.ChatResponse;
import com.example.demo.pojo.ModelType;
import com.example.demo.service.ChatService;
import com.example.demo.service.OllamaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ChatController {

    private final ChatService chatService;
    private final OllamaService ollamaService;

    public ChatController(ChatService chatService, OllamaService ollamaService) {
        this.chatService = chatService;
        this.ollamaService = ollamaService;
    }

    @PostMapping("/ollama")
    public ChatResponse ollamaChat(@RequestBody ChatRequest request) {
        ModelType modelType = ollamaService.pickOptimalModel(request.getMessage());
        String aiResponse = ollamaService.getChatResponse(modelType, request.getMessage());
        return ChatResponse.builder()
                .modelType(modelType)
                .message(aiResponse)
                .build();
    }

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) throws JsonProcessingException {
        String chatId = request.getChatId() != null ? request.getChatId() : UUID.randomUUID().toString();
        String aiResponse = chatService.getChatResponse(chatId, request);
        return ChatResponse.builder().chatId(chatId).message(aiResponse).build();
    }
} 