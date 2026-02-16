package com.example.GEDS.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "expense_splits",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_expense_user",
                        columnNames = {"expense_id", "user_id"}
                )
        },
        indexes = {
                @Index(name = "idx_expense_splits_expense_id", columnList = "expense_id"),
                @Index(name = "idx_expense_splits_user_id", columnList = "user_id")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ExpenseSplit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "expense_id", nullable = false)
    private Expense expense;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;


    public void updateAmount(BigDecimal newAmount) {
        this.amount = newAmount;
    }

}
