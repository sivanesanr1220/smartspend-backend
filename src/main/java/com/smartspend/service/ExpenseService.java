package com.smartspend.service;
import com.smartspend.dto.*;
import com.smartspend.entity.*;
import com.smartspend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepo;
    private final UserRepository userRepo;
    private final BudgetRepository budgetRepo;
    private final NotificationService notifService;
    private final EmailService emailService;

    public ExpenseDTO create(ExpenseRequest req, String email) {
        User user = userRepo.findByEmail(email).orElseThrow();
        Expense e = Expense.builder()
            .title(req.getTitle()).amount(req.getAmount()).category(req.getCategory())
            .type(req.getType()).date(req.getDate()).description(req.getDescription()).user(user).build();
        Expense saved = expenseRepo.save(e);
        if (req.getType() == Expense.TransactionType.EXPENSE) checkBudget(user, req.getCategory());
        return toDTO(saved);
    }

    public List<ExpenseDTO> getAll(String email) {
        User user = userRepo.findByEmail(email).orElseThrow();
        return expenseRepo.findByUserOrderByDateDesc(user).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ExpenseDTO> filter(String email, Expense.TransactionType type, Expense.Category category, LocalDate from, LocalDate to) {
        User user = userRepo.findByEmail(email).orElseThrow();
        if (from != null && to != null) return expenseRepo.findByUserAndDateBetweenOrderByDateDesc(user, from, to).stream()
            .filter(e -> type == null || e.getType() == type)
            .filter(e -> category == null || e.getCategory() == category)
            .map(this::toDTO).collect(Collectors.toList());
        if (type != null && category != null) return expenseRepo.findByUserAndTypeOrderByDateDesc(user, type).stream()
            .filter(e -> e.getCategory() == category).map(this::toDTO).collect(Collectors.toList());
        if (type != null) return expenseRepo.findByUserAndTypeOrderByDateDesc(user, type).stream().map(this::toDTO).collect(Collectors.toList());
        if (category != null) return expenseRepo.findByUserAndCategoryOrderByDateDesc(user, category).stream().map(this::toDTO).collect(Collectors.toList());
        return expenseRepo.findByUserOrderByDateDesc(user).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ExpenseDTO update(Long id, ExpenseRequest req, String email) {
        Expense e = expenseRepo.findById(id).orElseThrow(() -> new RuntimeException("Expense not found"));
        if (!e.getUser().getEmail().equals(email)) throw new RuntimeException("Unauthorized");
        e.setTitle(req.getTitle()); e.setAmount(req.getAmount()); e.setCategory(req.getCategory());
        e.setType(req.getType()); e.setDate(req.getDate()); e.setDescription(req.getDescription());
        return toDTO(expenseRepo.save(e));
    }

    public void delete(Long id, String email) {
        Expense e = expenseRepo.findById(id).orElseThrow(() -> new RuntimeException("Expense not found"));
        if (!e.getUser().getEmail().equals(email)) throw new RuntimeException("Unauthorized");
        expenseRepo.delete(e);
    }

    private void checkBudget(User user, Expense.Category category) {
        LocalDate now = LocalDate.now();
        budgetRepo.findByUserAndCategoryAndMonthAndYear(user, category, now.getMonthValue(), now.getYear()).ifPresent(budget -> {
            BigDecimal spent = expenseRepo.sumByUserAndCategoryAndMonthAndYear(user, category, now.getMonthValue(), now.getYear());
            if (spent == null) spent = BigDecimal.ZERO;
            BigDecimal pct = spent.divide(budget.getLimitAmount(), 2, java.math.RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
            double p = pct.doubleValue();
            if (p >= 100) {
                String msg = "🚨 You've exceeded your " + category + " budget!";
                notifService.create(user, msg, "BUDGET_EXCEEDED");
                emailService.sendBudgetAlert(user.getEmail(), category.name(), p);
            } else if (p >= 80) {
                String msg = "⚠️ You've used " + String.format("%.0f", p) + "% of your " + category + " budget.";
                notifService.create(user, msg, "BUDGET_WARNING");
            }
        });
    }

    public ExpenseDTO toDTO(Expense e) {
        ExpenseDTO d = new ExpenseDTO();
        d.setId(e.getId()); d.setTitle(e.getTitle()); d.setAmount(e.getAmount());
        d.setCategory(e.getCategory()); d.setType(e.getType()); d.setDate(e.getDate());
        d.setDescription(e.getDescription()); d.setCreatedAt(e.getCreatedAt()); return d;
    }
}
