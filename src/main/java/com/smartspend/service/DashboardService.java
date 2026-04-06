package com.smartspend.service;
import com.smartspend.dto.*;
import com.smartspend.entity.*;
import com.smartspend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class DashboardService {
    private final UserRepository userRepo;
    private final ExpenseRepository expenseRepo;
    private final BudgetRepository budgetRepo;
    private final NotificationService notifService;
    private final BudgetService budgetService;
    private final ExpenseService expenseService;

    public DashboardDTO getDashboard(String email) {
        User user = userRepo.findByEmail(email).orElseThrow();
        LocalDate now = LocalDate.now();
        int month = now.getMonthValue(), year = now.getYear();
        BigDecimal income = expenseRepo.sumByUserAndTypeAndMonthAndYear(user, Expense.TransactionType.INCOME, month, year);
        BigDecimal expense = expenseRepo.sumByUserAndTypeAndMonthAndYear(user, Expense.TransactionType.EXPENSE, month, year);
        if (income == null) income = BigDecimal.ZERO;
        if (expense == null) expense = BigDecimal.ZERO;

        List<Object[]> catData = expenseRepo.sumByCategoryForMonth(user, month, year);
        Map<String, BigDecimal> byCategory = new LinkedHashMap<>();
        catData.forEach(row -> byCategory.put(row[0].toString(), (BigDecimal) row[1]));

        List<ExpenseDTO> recent = expenseRepo.findByUserOrderByDateDesc(user).stream()
            .limit(5).map(expenseService::toDTO).collect(Collectors.toList());

        List<BudgetDTO> budgets = budgetRepo.findByUserAndMonthAndYear(user, month, year).stream()
            .map(b -> budgetService.toDTO(b, user)).collect(Collectors.toList());

        DashboardDTO d = new DashboardDTO();
        d.setTotalIncome(income); d.setTotalExpense(expense);
        d.setBalance(income.subtract(expense)); d.setRecentTransactions(recent);
        d.setExpenseByCategory(byCategory); d.setBudgets(budgets);
        d.setUnreadNotifications(notifService.countUnread(user)); return d;
    }
}
