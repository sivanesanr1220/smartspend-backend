package com.smartspend.dto;
import com.smartspend.entity.Expense;
import lombok.Data;
import java.math.BigDecimal;
@Data
public class BudgetDTO {
    private Long id;
    private Expense.Category category;
    private BigDecimal limitAmount;
    private BigDecimal spent;
    private BigDecimal remaining;
    private double percentage;
    private Integer month;
    private Integer year;
}
