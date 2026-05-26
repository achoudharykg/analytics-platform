package com.analyticsplatform.analytics_platform.auth;

import com.analyticsplatform.analytics_platform.auth.dto.AuthResponse;
import com.analyticsplatform.analytics_platform.auth.dto.LoginRequest;
import com.analyticsplatform.analytics_platform.auth.dto.SignupRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest request) {
        authService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = authService.login(request);
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(
                token,
                user.getEmail(),
                user.getRole()
        ));
    }
}
