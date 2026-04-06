package com.smartspend.controller;
import com.smartspend.dto.*;
import com.smartspend.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController @RequestMapping("/api/budgets") @RequiredArgsConstructor
public class BudgetController {
    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<ApiResponse<BudgetDTO>> set(@Valid @RequestBody BudgetRequest req, Authentication auth) {
        return ResponseEntity.ok(ApiResponse.ok("Budget set", budgetService.set(req, auth.getName())));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BudgetDTO>>> get(Authentication auth,
        @RequestParam(defaultValue = "0") int month,
        @RequestParam(defaultValue = "0") int year) {
        LocalDate now = LocalDate.now();
        int m = month == 0 ? now.getMonthValue() : month;
        int y = year == 0 ? now.getYear() : year;
        return ResponseEntity.ok(ApiResponse.ok("Fetched", budgetService.getForMonth(auth.getName(), m, y)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id, Authentication auth) {
        budgetService.delete(id, auth.getName());
        return ResponseEntity.ok(ApiResponse.ok("Deleted", null));
    }
}
