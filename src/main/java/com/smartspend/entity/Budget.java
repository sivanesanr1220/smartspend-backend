package com.smartspend.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name = "budgets", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","category","month","year"}))
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Budget {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Expense.Category category;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal limitAmount;
    @Column(nullable = false) private Integer month;
    @Column(nullable = false) private Integer year;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id") private User user;
    @Column(name = "created_at") private LocalDateTime createdAt;
    @PrePersist void prePersist() { createdAt = LocalDateTime.now(); }
}
