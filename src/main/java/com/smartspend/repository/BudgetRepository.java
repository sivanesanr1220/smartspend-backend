package com.smartspend.repository;
import com.smartspend.entity.Budget;
import com.smartspend.entity.Expense;
import com.smartspend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByUserAndMonthAndYear(User user, int month, int year);
    Optional<Budget> findByUserAndCategoryAndMonthAndYear(User user, Expense.Category category, int month, int year);
    List<Budget> findByUser(User user);
}
