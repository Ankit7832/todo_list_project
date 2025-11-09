package com.ankit.todo_list.controller;

import com.ankit.todo_list.exception.UserNameAlreadyExistException;
import com.ankit.todo_list.repository.UserRepository;
import com.ankit.todo_list.security.dto.AuthRequest;
import com.ankit.todo_list.security.dto.AuthResponse;
import com.ankit.todo_list.security.dto.RegisterationResponse;
import com.ankit.todo_list.security.jwt.JwtService;
import com.ankit.todo_list.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest){
        log.info("Authentication attempt for user: {}", authRequest.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        final String token = jwtService.generateToken(userDetails);

        log.info("Authentication successful, token generated for user: {}", authRequest.getUsername());

        return ResponseEntity.ok(new AuthResponse(token));
    }
    @PostMapping("/register")
    public ResponseEntity<RegisterationResponse> register(@RequestBody AuthRequest authRequest) throws UserNameAlreadyExistException {
        log.info("Registration attempt for username: {}",authRequest.getUsername());

        RegisterationResponse response = authService.register(authRequest);
        log.info("Registration succesfull for user: {}",authRequest.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



}
