package com.example.GEDS.controller;

import com.example.GEDS.dto.UserRequest;
import com.example.GEDS.dto.UserResponse;
import com.example.GEDS.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // FIX 8: Added @Valid to trigger bean validation on UserRequest
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid UserRequest userRequest) {
        UserResponse response = userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(@RequestBody @Valid UserRequest userRequest) {
        UserResponse response = userService.loginUser(userRequest);
        return ResponseEntity.ok(response);
    }
}
