package com.smartspend.controller;
import com.smartspend.dto.*;
import com.smartspend.repository.UserRepository;
import com.smartspend.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/dashboard") @RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;
    private final NotificationService notifService;
    private final UserRepository userRepo;

    @GetMapping
    public ResponseEntity<ApiResponse<DashboardDTO>> dashboard(Authentication auth) {
        return ResponseEntity.ok(ApiResponse.ok("Dashboard", dashboardService.getDashboard(auth.getName())));
    }

    @GetMapping("/notifications")
    public ResponseEntity<ApiResponse<List<NotificationDTO>>> notifications(Authentication auth) {
        var user = userRepo.findByEmail(auth.getName()).orElseThrow();
        return ResponseEntity.ok(ApiResponse.ok("Fetched", notifService.getAll(user)));
    }

    @PostMapping("/notifications/read")
    public ResponseEntity<ApiResponse<Void>> markRead(Authentication auth) {
        var user = userRepo.findByEmail(auth.getName()).orElseThrow();
        notifService.markAllRead(user);
        return ResponseEntity.ok(ApiResponse.ok("Marked read", null));
    }
}
