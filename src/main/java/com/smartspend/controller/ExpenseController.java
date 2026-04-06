package com.smartspend.controller;
import com.smartspend.dto.*;
import com.smartspend.entity.Expense;
import com.smartspend.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController @RequestMapping("/api/expenses") @RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ApiResponse<ExpenseDTO>> create(@Valid @RequestBody ExpenseRequest req, Authentication auth) {
        return ResponseEntity.ok(ApiResponse.ok("Created", expenseService.create(req, auth.getName())));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExpenseDTO>>> getAll(Authentication auth,
        @RequestParam(required = false) Expense.TransactionType type,
        @RequestParam(required = false) Expense.Category category,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(ApiResponse.ok("Fetched", expenseService.filter(auth.getName(), type, category, from, to)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseDTO>> update(@PathVariable Long id, @Valid @RequestBody ExpenseRequest req, Authentication auth) {
        return ResponseEntity.ok(ApiResponse.ok("Updated", expenseService.update(id, req, auth.getName())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id, Authentication auth) {
        expenseService.delete(id, auth.getName());
        return ResponseEntity.ok(ApiResponse.ok("Deleted", null));
    }
}
