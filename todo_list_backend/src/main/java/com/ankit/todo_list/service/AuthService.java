package com.ankit.todo_list.service;

import com.ankit.todo_list.entity.User;
import com.ankit.todo_list.exception.UserNameAlreadyExistException;
import com.ankit.todo_list.repository.UserRepository;
import com.ankit.todo_list.security.dto.AuthRequest;
import com.ankit.todo_list.security.dto.RegisterationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterationResponse register(AuthRequest authRequest) throws UserNameAlreadyExistException {
        if(userRepository.findByUsername(authRequest.getUsername()).isPresent()){
            throw new UserNameAlreadyExistException("User already exist with username : "+authRequest.getUsername());
        }
        User user=new User();
        user.setUsername(authRequest.getUsername());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        try {
            userRepository.save(user);
            return new RegisterationResponse("User registered with username", user.getUsername());
        } catch (DataIntegrityViolationException e){
            log.warn("Race condition during registration for username: {}", authRequest.getUsername());
            throw new UserNameAlreadyExistException("User already exist with username : "+authRequest.getUsername());
        }}
}
