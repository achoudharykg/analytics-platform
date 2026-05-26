package com.analyticsplatform.analytics_platform.auth;

import com.analyticsplatform.analytics_platform.auth.dto.LoginRequest;
import com.analyticsplatform.analytics_platform.auth.dto.SignupRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
        passwordEncoder = new BCryptPasswordEncoder();
    }

    public User signUp(SignupRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("A user with same email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");

        return userRepository.save(user);
    }

    public User login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail());

        if(user == null) {
            throw new RuntimeException("Invalid email or password");
        }

        if(!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }

        return user;
    }
}
