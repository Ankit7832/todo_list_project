package com.ankit.todo_list.service;


import com.ankit.todo_list.dto.TaskRequestDto;
import com.ankit.todo_list.dto.TaskResponseDto;
import com.ankit.todo_list.dto.TaskUpdateDto;
import com.ankit.todo_list.entity.Task;
import com.ankit.todo_list.entity.User;
import com.ankit.todo_list.exception.ResourceNotFoundException;
import com.ankit.todo_list.repository.TaskRepository;
import com.ankit.todo_list.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public TaskResponseDto createTask(String username, TaskRequestDto requestDto) {
        User user =findUserByUsername(username);
        Task task= new Task();

        task.setTitle(requestDto.getTitle());
        task.setDescription(requestDto.getDescription());
        task.setUser(user);
        try{
            Task savedTask=taskRepository.save(task);
            return new TaskResponseDto(savedTask);
        }catch(DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"A data with same title and incomplete status already exist, please provide the unique title or mark the previous title as completed");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponseDto getTaskById(String username, Long taskId) {
        Task task=getTaskAndVerifyOwnership(taskId,username);
        return new TaskResponseDto(task);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskResponseDto> getAllTask(String username, Pageable pageable) {
        User user =findUserByUsername(username);
        Page<Task> taskPage=taskRepository.findAllByUser(user,pageable);
        return taskPage.map(TaskResponseDto::new);
    }

    @Override
    @Transactional
    public TaskResponseDto updateTask(String username, Long taskId, TaskUpdateDto updateDto) {

        Task task=getTaskAndVerifyOwnership(taskId,username);

        if (updateDto.getIsCompleted() != null) {
            task.setIsCompleted(updateDto.getIsCompleted());
        }

        Task updated=taskRepository.save(task);
        return new TaskResponseDto(updated);
    }

    @Override
    @Transactional
    public void deleteTask(String username, Long taskId) {
        Task task=getTaskAndVerifyOwnership(taskId,username);
        taskRepository.delete(task);
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    private Task getTaskAndVerifyOwnership(Long taskId, String username) {
        User user = findUserByUsername(username);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task doesn't exist with id: " + taskId));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to access this task.");
        }
        return task;
    }
}
