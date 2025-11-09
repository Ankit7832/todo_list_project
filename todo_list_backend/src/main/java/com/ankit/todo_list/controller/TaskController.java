package com.ankit.todo_list.controller;

import com.ankit.todo_list.dto.TaskRequestDto;
import com.ankit.todo_list.dto.TaskResponseDto;
import com.ankit.todo_list.dto.TaskUpdateDto;
import com.ankit.todo_list.entity.User;
import com.ankit.todo_list.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping()
    public ResponseEntity<TaskResponseDto> createTask(@RequestBody @Valid TaskRequestDto requestDto, Principal principal){
        TaskResponseDto responseDto=taskService.createTask(principal.getName(),requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable("id") Long taskId,Principal principal){
        TaskResponseDto responseDto=taskService.getTaskById(principal.getName(), taskId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping()
    public ResponseEntity<Page<TaskResponseDto>> getAllTask(Pageable pageable, Principal principal){
        Page<TaskResponseDto> pages=taskService.getAllTask(principal.getName(), pageable);
        return ResponseEntity.ok(pages);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable("id") Long pathId,@RequestBody @Valid TaskUpdateDto updateDto, Principal principal){
        TaskResponseDto responseDto=taskService.updateTask(principal.getName(),pathId,updateDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") Long taskId, Principal principal){
        taskService.deleteTask(principal.getName(), taskId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
