package com.example.security;

import com.example.LoginRequest;
import com.example.entity.UserEntity;
import com.example.repository.UserRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class AuthenticateService {
    @Inject
    private UserRepository userRepository;
    @Inject
    private BcryptEncoderService bcryptEncoderService;

    public boolean authenticateUser(LoginRequest request) {
        try {
            Optional<UserEntity> userOpt = userRepository.findByUserName(request.getUserName());
            if (userOpt.isPresent()) {
                UserEntity user = userOpt.get();
                return bcryptEncoderService.verifyPassword(request.getPassword(), user.getPassword());
            } else {
                // User not found
                return false;
            }
        } catch (Exception e) {
            // Log the error for debugging purposes
            e.printStackTrace(); // You can use a proper logging framework instead

            // Handle the exception and return a failure response
            return false;
        }
    }
}
