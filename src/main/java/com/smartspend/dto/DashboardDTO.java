package com.smartspend.dto;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
@Data
public class DashboardDTO {
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal balance;
    private List<ExpenseDTO> recentTransactions;
    private Map<String, BigDecimal> expenseByCategory;
    private List<BudgetDTO> budgets;
    private long unreadNotifications;
}
