package com.smartspend.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity @Table(name = "users")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private String name;
    @Column(unique = true, nullable = false) private String email;
    @Column(nullable = false) private String password;
    @Enumerated(EnumType.STRING) private Role role;
    private boolean enabled = false;
    private String verificationToken;
    private String resetToken;
    private LocalDateTime resetTokenExpiry;
    @Column(name = "created_at") private LocalDateTime createdAt;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL) private List<Expense> expenses;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL) private List<Budget> budgets;
    @PrePersist void prePersist() { createdAt = LocalDateTime.now(); if (role == null) role = Role.USER; }
    public enum Role { USER, ADMIN }
}
