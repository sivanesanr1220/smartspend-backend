package com.smartspend.service;
import com.smartspend.dto.*;
import com.smartspend.entity.*;
import com.smartspend.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {
    @Mock ExpenseRepository expenseRepo;
    @Mock UserRepository userRepo;
    @Mock BudgetRepository budgetRepo;
    @Mock NotificationService notifService;
    @Mock EmailService emailService;
    @InjectMocks ExpenseService expenseService;

    @Test void create_expense_success() {
        User user = User.builder().id(1L).email("u@t.com").build();
        when(userRepo.findByEmail("u@t.com")).thenReturn(Optional.of(user));
        Expense saved = Expense.builder().id(1L).title("Lunch").amount(BigDecimal.TEN)
            .category(Expense.Category.FOOD).type(Expense.TransactionType.EXPENSE)
            .date(LocalDate.now()).user(user).build();
        when(expenseRepo.save(any())).thenReturn(saved);
        when(budgetRepo.findByUserAndCategoryAndMonthAndYear(any(), any(), anyInt(), anyInt())).thenReturn(Optional.empty());
        ExpenseRequest req = new ExpenseRequest(); req.setTitle("Lunch"); req.setAmount(BigDecimal.TEN);
        req.setCategory(Expense.Category.FOOD); req.setType(Expense.TransactionType.EXPENSE); req.setDate(LocalDate.now());
        ExpenseDTO result = expenseService.create(req, "u@t.com");
        assertEquals("Lunch", result.getTitle());
    }

    @Test void delete_unauthorized_throws() {
        User other = User.builder().id(2L).email("other@t.com").build();
        Expense e = Expense.builder().id(1L).user(other).build();
        when(expenseRepo.findById(1L)).thenReturn(Optional.of(e));
        assertThrows(RuntimeException.class, () -> expenseService.delete(1L, "me@t.com"));
    }
}
