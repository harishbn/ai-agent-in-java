package com.example.demo.service;

import com.example.demo.dto.ChatRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {

    private final OpenAiService openAiService;

    public ChatService(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    public String getChatResponse(ChatRequest chatRequest) {
        List<ChatMessage> messages = new ArrayList<>();
        
        if (chatRequest.getSystemMessage() != null && !chatRequest.getSystemMessage().trim().isEmpty()) {
            messages.add(new ChatMessage("system", chatRequest.getSystemMessage()));
        }
        messages.add(new ChatMessage("user", chatRequest.getMessage()));

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .build();

        return openAiService.createChatCompletion(request)
                .getChoices().get(0)
                .getMessage().getContent();
    }
} 