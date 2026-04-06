package com.smartspend.dto;
import com.smartspend.entity.Expense;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
@Data
public class ExpenseRequest {
    @NotBlank public String title;
    @NotNull @Positive public BigDecimal amount;
    @NotNull public Expense.Category category;
    @NotNull public Expense.TransactionType type;
    @NotNull public LocalDate date;
    public String description;
}
