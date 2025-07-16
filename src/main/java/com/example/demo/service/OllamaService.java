package com.example.demo.service;

import com.example.demo.pojo.ModelType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Service
public class OllamaService {
    private final HttpClient httpClient;
    private final String OLLAMA_BASE_URL = "http://localhost:11434/api/chat";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OllamaService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public String getChatResponse(ModelType modelType, String prompt) {
        try {
            String json = """
                    {
                      "model": "%s",
                      "stream": false,
                      "think": false,
                      "messages": [
                        { "role": "user", "content": "%s" }
                      ]
                    }
                    """.formatted(modelType.getModelName(), escapeJson(prompt));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(OLLAMA_BASE_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            OllamaChatResponse chatResponse = objectMapper.readValue(response.body(), OllamaChatResponse.class);
            return chatResponse.message != null ? chatResponse.message.content : "No message content found";
        } catch (Exception e) {
            throw new RuntimeException("Failed to get response from Ollama: " + e.getMessage(), e);
        }
    }

    public ModelType pickOptimalModel(String message) {
        // Invoke llama to pick optimal model
        String prompt = "Classify this query as 'CREATIVE', 'CODE' or 'GENERAL'. " +
                "Query: " + message + ". " +
                "Respond with single word, either  CREATIVE, CODE or GENERAL";
        String type = getChatResponse(ModelType.CREATIVE, prompt);
        return ModelType.valueOf(type);
    }

    // Escapes double quotes in user prompt
    private String escapeJson(String text) {
        return text.replace("\"", "\\\"");
    }
}


@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
class OllamaChatResponse {
    public OllamaMessage message;

}

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
class OllamaMessage {
    public String role;
    public String content;
}

