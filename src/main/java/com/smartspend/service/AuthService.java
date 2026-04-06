package com.smartspend.service;
import com.smartspend.dto.*;
import com.smartspend.entity.User;
import com.smartspend.repository.UserRepository;
import com.smartspend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service @RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    public String register(RegisterRequest req) {
        if (userRepo.existsByEmail(req.getEmail())) throw new RuntimeException("Email already registered");
        String token = UUID.randomUUID().toString();
        User user = User.builder()
            .name(req.getName()).email(req.getEmail())
            .password(encoder.encode(req.getPassword()))
            .verificationToken(token).enabled(false).build();
        userRepo.save(user);
        emailService.sendVerification(req.getEmail(), token);
        return "Registration successful. Please check your email to verify.";
    }

    public AuthResponse login(AuthRequest req) {
        User user = userRepo.findByEmail(req.getEmail()).orElseThrow(() -> new RuntimeException("Invalid credentials"));
        if (!encoder.matches(req.getPassword(), user.getPassword())) throw new RuntimeException("Invalid credentials");
        if (!user.isEnabled()) throw new RuntimeException("Please verify your email before logging in");
        String token = jwtUtil.generate(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getName(), user.getEmail(), user.getRole().name(), user.getId());
    }

    public String verify(String token) {
        User user = userRepo.findByVerificationToken(token).orElseThrow(() -> new RuntimeException("Invalid token"));
        user.setEnabled(true); user.setVerificationToken(null);
        userRepo.save(user); return "Email verified! You can now log in.";
    }

    public String forgotPassword(String email) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Email not found"));
        String token = UUID.randomUUID().toString();
        user.setResetToken(token); user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
        userRepo.save(user);
        emailService.sendPasswordReset(email, token);
        return "Password reset link sent to your email";
    }

    public String resetPassword(PasswordResetRequest req) {
        User user = userRepo.findByResetToken(req.getToken()).orElseThrow(() -> new RuntimeException("Invalid token"));
        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) throw new RuntimeException("Token expired");
        user.setPassword(encoder.encode(req.getNewPassword()));
        user.setResetToken(null); user.setResetTokenExpiry(null);
        userRepo.save(user); return "Password reset successfully";
    }
}
