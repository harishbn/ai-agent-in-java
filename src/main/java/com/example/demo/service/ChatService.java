package com.example.demo.service;

import com.example.demo.pojo.ChatRequest;
import com.example.demo.pojo.ConvMessage;
import com.example.demo.pojo.Conversation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final OpenAiService openAiService;
    private final ConversationMemoryService memoryService;

    public ChatService(OpenAiService openAiService, ConversationMemoryService memoryService) {
        this.openAiService = openAiService;
        this.memoryService = memoryService;
    }

    public String getChatResponse(String chatId, ChatRequest chatRequest) throws JsonProcessingException {
        Conversation conversation = memoryService.getConversation(chatId);

        conversation.getMessages().add(new ConvMessage(ConvMessage.Role.USER, chatRequest.getMessage()));

        // build open ai's chat messages list
        List<ChatMessage> messages = conversation.getMessages().stream().map(m-> new ChatMessage(
                m.getRole().name().toLowerCase(),
                m.getContent()
        )).collect(Collectors.toList());

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .build();

        String aiResponse = openAiService.createChatCompletion(request)
                .getChoices().get(0)
                .getMessage().getContent();

        conversation.getMessages().add(new ConvMessage(
                ConvMessage.Role.ASSISTANT,
                aiResponse
        ));
        memoryService.saveConversation(conversation);
        return aiResponse;
    }
} 