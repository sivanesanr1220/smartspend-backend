package com.smartspend.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity @Table(name = "expenses")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Expense {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private String title;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal amount;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Category category;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private TransactionType type;
    @Column(nullable = false) private LocalDate date;
    @Column(columnDefinition = "TEXT") private String description;
    @Column(name = "created_at") private LocalDateTime createdAt;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id") private User user;
    @PrePersist void prePersist() { createdAt = LocalDateTime.now(); }
    public enum Category { FOOD, TRANSPORT, HOUSING, ENTERTAINMENT, HEALTHCARE, EDUCATION, SHOPPING, UTILITIES, SALARY, BUSINESS, OTHER }
    public enum TransactionType { EXPENSE, INCOME }
}
