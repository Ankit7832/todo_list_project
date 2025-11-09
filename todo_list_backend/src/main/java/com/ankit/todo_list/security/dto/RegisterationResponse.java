package com.ankit.todo_list.security.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterationResponse {
    private String username;
    private String message;

    public RegisterationResponse(String message, String username) {
        this.message = message;
        this.username = username;
    }
}
