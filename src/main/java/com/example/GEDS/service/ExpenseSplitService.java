package com.example.GEDS.service;

import com.example.GEDS.dto.ExpenseSplitRes;
import com.example.GEDS.entity.Expense;
import com.example.GEDS.entity.ExpenseSplit;
import com.example.GEDS.entity.GroupMember;
import com.example.GEDS.repository.ExpenseSplitRepo;
import com.example.GEDS.repository.GroupMemberRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseSplitService {

    private final ExpenseSplitRepo expenseSplitRepo;
    private final GroupMemberRepo groupMemberRepo;

    /**
     * Splits an expense evenly among all group members.
     *
     * Convention: positive amount = this member OWES money.
     *             negative amount = this member is OWED money (the payer).
     *
     * FIX 3/4: Accepts the already-persisted Expense object directly,
     *           so no ID lookup is needed and no null-ID crash can occur.
     *
     * FIX 5: Correct payer amount:
     *   - Each member owes their share (splitAmount, positive)
     *   - Payer is OWED the total minus their own share:
     *     net = -(totalAmount - splitAmount) = splitAmount - totalAmount (negative)
     */
    @Transactional
    public ExpenseSplitRes splitExpenseEvenly(Expense expense) {

        if (expenseSplitRepo.existsByExpenseId(expense.getId())) {
            throw new IllegalStateException("Expense has already been split");
        }

        List<GroupMember> members = groupMemberRepo.findByGroupId(expense.getGroup().getId());

        if (members.isEmpty()) {
            throw new IllegalArgumentException("No members found in the group");
        }

        int count = members.size();
        BigDecimal splitAmount = calculateSplitAmount(expense.getAmount(), count);

        List<ExpenseSplit> splits = members.stream()
                .map(member -> {
                    boolean isPayer = member.getMember().getId()
                            .equals(expense.getPaidBy().getId());

                    // FIX 5: Payer gets: splitAmount - totalAmount (negative = they are owed)
                    // Others get: splitAmount (positive = they owe)
                    BigDecimal memberAmount = isPayer
                            ? splitAmount.subtract(expense.getAmount())
                            : splitAmount;

                    return ExpenseSplit.builder()
                            .expense(expense)
                            .user(member.getMember())
                            .amount(memberAmount)
                            .build();
                })
                .toList();

        expenseSplitRepo.saveAll(splits);

        return ExpenseSplitRes.builder()
                .expenseId(expense.getId())
                .groupName(expense.getGroup().getName())
                .totalAmount(expense.getAmount())
                .splitCount(count)
                .amountPerMember(splitAmount)
                .build();
    }

    private BigDecimal calculateSplitAmount(BigDecimal total, int memberCount) {
        return total.divide(
                BigDecimal.valueOf(memberCount),
                2,
                RoundingMode.HALF_UP
        );
    }
}
