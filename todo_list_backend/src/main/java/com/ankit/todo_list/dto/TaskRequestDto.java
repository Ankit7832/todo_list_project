package com.ankit.todo_list.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link com.ankit.todo_list.entity.Task}
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class TaskRequestDto  {
    @NotEmpty(message = "Title should not be empty")
    private String title;
    @NotEmpty(message = "Task description should not be empty ")
    private String description;
}