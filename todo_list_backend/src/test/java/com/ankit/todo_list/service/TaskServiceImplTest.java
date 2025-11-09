package com.ankit.todo_list.service;

import com.ankit.todo_list.dto.TaskRequestDto;
import com.ankit.todo_list.dto.TaskResponseDto;
import com.ankit.todo_list.dto.TaskUpdateDto;
import com.ankit.todo_list.entity.Task;
import com.ankit.todo_list.entity.User;
import com.ankit.todo_list.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private User  user;
    private Task task;
    private TaskRequestDto requestDto;
    private TaskUpdateDto updateDto;
    private TaskResponseDto responseDto;

    @BeforeEach
    void setup(){
        this.user=new User();
        this.user.setId(1L);
        this.user.setUsername("testUser");
        this.task=new Task(2L, "Testing Create","Testing create Api",false, Instant.now(),Instant.now(),user);

        this.requestDto=new TaskRequestDto("Testing Create","Testing create Api");

        this.updateDto=new TaskUpdateDto(true);

        this.responseDto=new TaskResponseDto(this.task);
    }


    @Nested
    class createTodoTest{

        @Test
        void createTaskTest(){

//            when(taskRepository.save(any(Task.class))).thenReturn(task) ;
//
//            final TaskResponseDto result=TaskServiceImplTest.this.taskService.createTask(requestDto);
//            assertNotNull(result);
//            assertEquals(responseDto.getId(),result.getId());
//
//            verify(taskRepository,times(1)).save(any(Task.class));

        }
    }
}