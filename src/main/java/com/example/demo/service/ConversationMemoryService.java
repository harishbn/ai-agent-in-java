package com.example.demo.service;

import com.example.demo.pojo.Conversation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ConversationMemoryService {
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public ConversationMemoryService(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void saveConversation(Conversation conv) throws JsonProcessingException {
        redisTemplate.opsForValue().set(
                conv.getChatId(),
                objectMapper.writeValueAsString(conv)
        );
    }

    public Conversation getConversation(String chatId) throws JsonProcessingException {
        String json = redisTemplate.opsForValue().get(chatId);
        return json != null ?
                objectMapper.readValue(json, Conversation.class) :
                new Conversation(chatId);
    }
}
