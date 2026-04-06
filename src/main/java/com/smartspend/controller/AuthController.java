package com.smartspend.controller;
import com.smartspend.dto.*;
import com.smartspend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/auth") @RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.ok(ApiResponse.ok("Registered", authService.register(req)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody AuthRequest req) {
        return ResponseEntity.ok(ApiResponse.ok("Login successful", authService.login(req)));
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<String>> verify(@RequestParam String token) {
        return ResponseEntity.ok(ApiResponse.ok("Verified", authService.verify(token)));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgot(@RequestParam String email) {
        return ResponseEntity.ok(ApiResponse.ok("Sent", authService.forgotPassword(email)));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> reset(@RequestBody PasswordResetRequest req) {
        return ResponseEntity.ok(ApiResponse.ok("Reset", authService.resetPassword(req)));
    }
}
