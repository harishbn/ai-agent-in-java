package com.example.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConvMessage {

    public enum Role {SYSTEM, USER, ASSISTANT}

    private Role role;
    private String content;
}
