package com.example.GEDS.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseReq {

    @NotNull
    private String groupName;

    @NotNull
    private Long paidByUserId;

    @NotBlank
    @Size(min = 1, max = 200)
    private String description;

    @NotNull
    @DecimalMin(value = "0.01",message = "Amount Must Positive and Bigger than Zero")
    @Positive
    private BigDecimal amount;
}