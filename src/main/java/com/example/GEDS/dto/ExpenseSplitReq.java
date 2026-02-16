package com.example.GEDS.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseSplitReq {

    @NotNull
    private Long expenseId;

    @NotNull
    private Long userId;

    @Positive
    @DecimalMin(value = "0.01",message = "Amount Must Positive and Bigger than Zero")
    @NotNull
    private BigDecimal amount;
}
