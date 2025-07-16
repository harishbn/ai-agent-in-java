package com.example.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ModelType {
    CREATIVE("llama3"),
    CODE("codellama"),
    GENERAL("qwen3:0.6b");

    @Getter
    private final String modelName;
}
