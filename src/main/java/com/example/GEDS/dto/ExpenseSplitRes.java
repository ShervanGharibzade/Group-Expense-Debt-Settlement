package com.example.GEDS.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseSplitRes {

    private Long expenseId;
    private String groupName;
    private BigDecimal totalAmount;
    private int splitCount;
    private BigDecimal amountPerMember;
}
