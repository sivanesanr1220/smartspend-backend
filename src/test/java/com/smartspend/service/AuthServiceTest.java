package com.smartspend.service;
import com.smartspend.dto.*;
import com.smartspend.entity.User;
import com.smartspend.repository.UserRepository;
import com.smartspend.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock UserRepository userRepo;
    @Mock PasswordEncoder encoder;
    @Mock JwtUtil jwtUtil;
    @Mock EmailService emailService;
    @InjectMocks AuthService authService;

    @Test void login_success() {
        User user = User.builder().id(1L).email("a@b.com").password("enc").name("A").role(User.Role.USER).enabled(true).build();
        when(userRepo.findByEmail("a@b.com")).thenReturn(Optional.of(user));
        when(encoder.matches("pass", "enc")).thenReturn(true);
        when(jwtUtil.generate(anyString(), anyString())).thenReturn("token");
        AuthRequest req = new AuthRequest(); req.setEmail("a@b.com"); req.setPassword("pass");
        AuthResponse res = authService.login(req);
        assertEquals("token", res.getToken());
    }

    @Test void login_disabled_throws() {
        User user = User.builder().email("a@b.com").password("enc").enabled(false).build();
        when(userRepo.findByEmail("a@b.com")).thenReturn(Optional.of(user));
        when(encoder.matches(anyString(), anyString())).thenReturn(true);
        AuthRequest req = new AuthRequest(); req.setEmail("a@b.com"); req.setPassword("pass");
        assertThrows(RuntimeException.class, () -> authService.login(req));
    }

    @Test void register_duplicate_email_throws() {
        when(userRepo.existsByEmail("dup@test.com")).thenReturn(true);
        RegisterRequest req = new RegisterRequest(); req.setEmail("dup@test.com"); req.setName("X"); req.setPassword("pass123");
        assertThrows(RuntimeException.class, () -> authService.register(req));
    }
}
