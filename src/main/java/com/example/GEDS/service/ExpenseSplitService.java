package com.example.GEDS.service;

import com.example.GEDS.dto.ExpenseReq;
import com.example.GEDS.dto.ExpenseSplitReq;
import com.example.GEDS.entity.Expense;
import com.example.GEDS.entity.ExpenseSplit;
import com.example.GEDS.entity.GroupMember;
import com.example.GEDS.entity.User;
import com.example.GEDS.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseSplitService {

    private final ExpenseRep expenseRep;
    private final ExpenseSplitRepo expenseSplitRepo;
    private final GroupMemberRepo groupMemberRepo;

    @Transactional
    public void expenseSplit(ExpenseSplitReq req) {

        Expense expense = expenseRep.findById(req.getExpenseId())
                .orElseThrow(() -> new IllegalArgumentException("Expense not found"));

        if (expenseSplitRepo.existsByExpenseId(expense.getId())) {
            throw new IllegalStateException("Expense already split");
        }

        List<GroupMember> members =
                groupMemberRepo.findByGroupId(expense.getGroup().getId());

        if (members.isEmpty()) {
            throw new IllegalArgumentException("No members in this group");
        }

        BigDecimal splitAmount = calculateSplitAmount(
                expense.getAmount(),
                members.size()
        );

        List<ExpenseSplit> splits = members.stream()
                .map(member -> {

                    boolean isPayer =
                            member.getMember().getId()
                                    .equals(expense.getPaidBy().getId());

                    BigDecimal amount = isPayer
                            ? splitAmount.subtract(expense.getAmount())
                            : splitAmount;

                    return ExpenseSplit.builder()
                            .expense(expense)
                            .user(member.getMember())
                            .amount(amount)
                            .build();
                })
                .toList();

        expenseSplitRepo.saveAll(splits);
    }


    private BigDecimal calculateSplitAmount(BigDecimal total, int memberCount) {
        return total.divide(
                BigDecimal.valueOf(memberCount),
                2, // scale (2 decimal places for money)
                RoundingMode.HALF_UP
        );
    }
}
