package com.example.GEDS.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseRes {


    private String groupName;

    private String paidByUsername;

    private String description;

    private BigDecimal amount;

    private LocalDateTime createdAt;
}