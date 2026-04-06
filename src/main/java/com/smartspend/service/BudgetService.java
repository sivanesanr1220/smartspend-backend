package com.smartspend.service;
import com.smartspend.dto.*;
import com.smartspend.entity.*;
import com.smartspend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepo;
    private final ExpenseRepository expenseRepo;
    private final UserRepository userRepo;

    public BudgetDTO set(BudgetRequest req, String email) {
        User user = userRepo.findByEmail(email).orElseThrow();
        Budget b = budgetRepo.findByUserAndCategoryAndMonthAndYear(user, req.getCategory(), req.getMonth(), req.getYear())
            .orElse(Budget.builder().user(user).category(req.getCategory()).month(req.getMonth()).year(req.getYear()).build());
        b.setLimitAmount(req.getLimitAmount());
        return toDTO(budgetRepo.save(b), user);
    }

    public List<BudgetDTO> getForMonth(String email, int month, int year) {
        User user = userRepo.findByEmail(email).orElseThrow();
        return budgetRepo.findByUserAndMonthAndYear(user, month, year).stream()
            .map(b -> toDTO(b, user)).collect(Collectors.toList());
    }

    public void delete(Long id, String email) {
        Budget b = budgetRepo.findById(id).orElseThrow();
        if (!b.getUser().getEmail().equals(email)) throw new RuntimeException("Unauthorized");
        budgetRepo.delete(b);
    }

    public BudgetDTO toDTO(Budget b, User user) {
        BigDecimal spent = expenseRepo.sumByUserAndCategoryAndMonthAndYear(user, b.getCategory(), b.getMonth(), b.getYear());
        if (spent == null) spent = BigDecimal.ZERO;
        BigDecimal remaining = b.getLimitAmount().subtract(spent);
        double pct = b.getLimitAmount().compareTo(BigDecimal.ZERO) > 0
            ? spent.divide(b.getLimitAmount(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).doubleValue() : 0;
        BudgetDTO d = new BudgetDTO();
        d.setId(b.getId()); d.setCategory(b.getCategory()); d.setLimitAmount(b.getLimitAmount());
        d.setSpent(spent); d.setRemaining(remaining); d.setPercentage(pct);
        d.setMonth(b.getMonth()); d.setYear(b.getYear()); return d;
    }
}
