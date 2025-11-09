package com.ankit.todo_list.exception;

import org.aspectj.bridge.IMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserNameAlreadyExistException extends Exception {
    public UserNameAlreadyExistException(String message) {
        super(message);
    }
}
