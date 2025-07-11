package com.example.demo.service;

import com.example.demo.pojo.ChatRequest;
import com.example.demo.pojo.ConvMessage;
import com.example.demo.pojo.Conversation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.service.FunctionExecutor;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final OpenAiService openAiService;
    private final ConversationMemoryService memoryService;
    private final WeatherService weatherService;
    private final CustomerInfoService customerInfoService;
    private final List<ChatFunction> chatFunctions;
    private final FunctionExecutor functionExecutor;

    public ChatService(OpenAiService openAiService, ConversationMemoryService memoryService, WeatherService weatherService, CustomerInfoService customerInfoService) {
        this.openAiService = openAiService;
        this.memoryService = memoryService;
        this.weatherService = weatherService;
        this.customerInfoService = customerInfoService;
        this.chatFunctions = buildChatFunctions();
        this.functionExecutor = new FunctionExecutor(chatFunctions);
    }

    public String getChatResponse(String chatId, ChatRequest chatRequest) throws JsonProcessingException {
        Conversation conversation = memoryService.getConversation(chatId);
        conversation.getMessages().add(new ConvMessage(ConvMessage.Role.USER, chatRequest.getMessage()));

        List<ChatMessage> messages = conversation.getMessages().stream().map(m-> new ChatMessage(
                m.getRole().name().toLowerCase(),
                m.getContent()
        )).collect(Collectors.toList());

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .functions(functionExecutor.getFunctions())
                .functionCall(ChatCompletionRequest.ChatCompletionRequestFunctionCall.of("auto"))
                .build();

        ChatCompletionResult result = openAiService.createChatCompletion(request);
        ChatCompletionChoice choice = result.getChoices().get(0);
        ChatMessage responseMessage = choice.getMessage();

        String aiResponse = "";
        if (responseMessage.getFunctionCall() != null) {
            aiResponse = handleFunctionCall(messages, responseMessage);
        } else {
            aiResponse = responseMessage.getContent();
        }
        conversation.getMessages().add(new ConvMessage(
                ConvMessage.Role.ASSISTANT,
                aiResponse
        ));
        memoryService.saveConversation(conversation);

        return aiResponse;
    }

    private String handleFunctionCall(List<ChatMessage> messages, ChatMessage message) {
        // Use FunctionExecutor to execute the function and get the response message
        ChatMessage functionResponseMessage = functionExecutor.executeAndConvertToMessageHandlingExceptions(message.getFunctionCall());
        messages.add(message); // assistant message with function call
        messages.add(functionResponseMessage); // function response message

        ChatCompletionRequest followupRequest = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .build();
        var followupResult = openAiService.createChatCompletion(followupRequest);
        return followupResult.getChoices().get(0).getMessage().getContent();
    }

    private List<ChatFunction> buildChatFunctions() {
        List<ChatFunction> functions = new ArrayList<>();

        // getWeather function
        functions.add(ChatFunction.builder()
                .name("getWeather")
                .description("Get the current weather for a location")
                .executor(WeatherService.WeatherRequest.class, req -> weatherService.getWeather(req))
                .build());

        // getCustomerInfo function
        functions.add(ChatFunction.builder()
                .name("getCustomerInfo")
                .description("Get customer details by customer name")
                .executor(CustomerInfoService.CustomerInfoRequest.class, req -> customerInfoService.getCustomerInfo(req))
                .build());
        return functions;
    }
} 