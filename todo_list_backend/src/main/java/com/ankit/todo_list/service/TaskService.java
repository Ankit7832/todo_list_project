package com.ankit.todo_list.service;

import com.ankit.todo_list.dto.TaskRequestDto;
import com.ankit.todo_list.dto.TaskResponseDto;
import com.ankit.todo_list.dto.TaskUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {


    TaskResponseDto createTask(String username, TaskRequestDto requestDto);
    TaskResponseDto getTaskById(String username, Long taskId);
    Page<TaskResponseDto> getAllTask(String username, Pageable pageable);
    TaskResponseDto updateTask(String username, Long taskId, TaskUpdateDto updateDto);
    void deleteTask(String username, Long taskId);

}
