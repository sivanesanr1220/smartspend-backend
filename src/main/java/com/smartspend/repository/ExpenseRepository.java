package com.smartspend.repository;
import com.smartspend.entity.Expense;
import com.smartspend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserOrderByDateDesc(User user);
    List<Expense> findByUserAndTypeOrderByDateDesc(User user, Expense.TransactionType type);
    List<Expense> findByUserAndCategoryOrderByDateDesc(User user, Expense.Category category);
    List<Expense> findByUserAndDateBetweenOrderByDateDesc(User user, LocalDate from, LocalDate to);

    @Query("SELECT e FROM Expense e WHERE e.user = :user AND e.type = :type AND MONTH(e.date) = :month AND YEAR(e.date) = :year")
    List<Expense> findByUserAndTypeAndMonthAndYear(@Param("user") User user, @Param("type") Expense.TransactionType type, @Param("month") int month, @Param("year") int year);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user = :user AND e.category = :category AND e.type = 'EXPENSE' AND MONTH(e.date) = :month AND YEAR(e.date) = :year")
    BigDecimal sumByUserAndCategoryAndMonthAndYear(@Param("user") User user, @Param("category") Expense.Category category, @Param("month") int month, @Param("year") int year);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user = :user AND e.type = :type AND MONTH(e.date) = :month AND YEAR(e.date) = :year")
    BigDecimal sumByUserAndTypeAndMonthAndYear(@Param("user") User user, @Param("type") Expense.TransactionType type, @Param("month") int month, @Param("year") int year);

    @Query("SELECT e.category, SUM(e.amount) FROM Expense e WHERE e.user = :user AND e.type = 'EXPENSE' AND MONTH(e.date) = :month AND YEAR(e.date) = :year GROUP BY e.category")
    List<Object[]> sumByCategoryForMonth(@Param("user") User user, @Param("month") int month, @Param("year") int year);
}
