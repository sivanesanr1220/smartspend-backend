package com.smartspend.dto;
import com.smartspend.entity.Expense;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
public class ExpenseDTO {
    private Long id;
    private String title;
    private BigDecimal amount;
    private Expense.Category category;
    private Expense.TransactionType type;
    private LocalDate date;
    private String description;
    private LocalDateTime createdAt;
}
