package com.smartspend.dto;
import com.smartspend.entity.Expense;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
@Data
public class BudgetRequest {
    @NotNull public Expense.Category category;
    @NotNull @Positive public BigDecimal limitAmount;
    @NotNull public Integer month;
    @NotNull public Integer year;
}
