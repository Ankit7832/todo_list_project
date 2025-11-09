package com.ankit.todo_list.dto;

import com.ankit.todo_list.entity.Task;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

/**
 * DTO for {@link com.ankit.todo_list.entity.Task}
 */
@Getter
@Setter
public class TaskResponseDto {
    private Long id;
    private String title;
    private String description;
    private Boolean isCompleted;
    private Instant createdAt;

    public TaskResponseDto(Task task){
        this.id= task.getId();
        this.title=task.getTitle();
        this.description= task.getDescription();
        this.isCompleted=task.getIsCompleted();
        this.createdAt=task.getCreatedAt();
    }
}