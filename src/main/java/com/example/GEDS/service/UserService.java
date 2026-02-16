package com.example.GEDS.service;

import com.example.GEDS.dto.UserRequest;
import com.example.GEDS.dto.UserResponse;
import com.example.GEDS.entity.User;
import com.example.GEDS.exception.EmailAlreadyExistsException;
import com.example.GEDS.exception.UserNotFoundException;
import com.example.GEDS.repository.UserRepo;
import com.example.GEDS.security.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final JwtUtil jwtUtil;

    @Transactional
    public UserResponse createUser(UserRequest req) {

        if (userRepo.findByEmail(req.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already in use: " + req.getEmail());
        }

        String hashedPassword = passwordEncoder.encode(req.getPassword());

        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(hashedPassword)
                .build();

        userRepo.save(user);

        String token = jwtUtil.generateToken(user.getEmail());

        return UserResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .token(token)
                .build();
    }

    public UserResponse loginUser(UserRequest req) {

        User user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new UserNotFoundException("No account found for: " + req.getEmail()));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            // FIX: Use generic message to avoid user enumeration attacks
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return UserResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .token(token)
                .build();
    }
}
