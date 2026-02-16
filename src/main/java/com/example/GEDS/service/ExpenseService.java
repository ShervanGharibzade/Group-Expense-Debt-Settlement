package com.example.GEDS.service;

import com.example.GEDS.dto.ExpenseReq;
import com.example.GEDS.dto.ExpenseRes;
import com.example.GEDS.entity.Expense;
import com.example.GEDS.entity.Group;
import com.example.GEDS.entity.User;
import com.example.GEDS.exception.GroupNotFoundException;
import com.example.GEDS.exception.UserNotFoundException;
import com.example.GEDS.repository.ExpenseRep;
import com.example.GEDS.repository.GroupMemberRepo;
import com.example.GEDS.repository.GroupRepo;
import com.example.GEDS.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final GroupMemberRepo groupMemberRepo;
    private final ExpenseRep expenseRep;
    private final UserRepo userRepo;
    private final GroupRepo groupRepo;
    private final ExpenseSplitService expenseSplitService;

    @Transactional
    public ExpenseRes addExpense(ExpenseReq req) {

        Group group = groupRepo.findByName(req.getGroupName()).orElseThrow(() ->
                new GroupNotFoundException("Group '" + req.getGroupName() + "' not found"));

        User user = userRepo.findById(req.getPaidByUserId()).orElseThrow(() ->
                new UserNotFoundException("User with id " + req.getPaidByUserId() + " not found"));

        // FIX 2: Correct method name (findByGroupIdAndMemberId)
        groupMemberRepo.findByGroupIdAndMemberId(group.getId(), req.getPaidByUserId()).orElseThrow(() ->
                new IllegalArgumentException("User is not a member of this group"));

        Expense expense = Expense.builder()
                .amount(req.getAmount())
                .paidBy(user)
                .description(req.getDescription())
                .group(group)
                .build();

        // FIX 3 & 4: Save expense FIRST so it gets an ID, THEN pass the ID to the split service
        Expense saved = expenseRep.save(expense);

        // Now the expense has a valid ID and can be found by expenseSplitService
        expenseSplitService.splitExpenseEvenly(saved);

        return ExpenseRes.builder()
                .groupName(group.getName())
                .paidByUsername(user.getName())
                .description(saved.getDescription())
                .amount(saved.getAmount())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    @Transactional
    public ExpenseRes updateExpense(ExpenseReq req, Long expenseId) {

        Expense expense = expenseRep.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Expense with id " + expenseId + " not found"));

        Group group = groupRepo.findByName(req.getGroupName())
                .orElseThrow(() -> new GroupNotFoundException("Group '" + req.getGroupName() + "' not found"));

        if (!expense.getGroup().getId().equals(group.getId())) {
            throw new IllegalArgumentException("Expense does not belong to this group");
        }

        if (req.getPaidByUserId() != null &&
                !expense.getPaidBy().getId().equals(req.getPaidByUserId())) {

            User user = userRepo.findById(req.getPaidByUserId())
                    .orElseThrow(() -> new UserNotFoundException("User with id " + req.getPaidByUserId() + " not found"));

            // FIX 2: Correct method name
            groupMemberRepo.findByGroupIdAndMemberId(group.getId(), user.getId())
                    .orElseThrow(() -> new IllegalArgumentException("User is not a member of this group"));

            expense.setPaidBy(user);
        }

        if (req.getAmount() != null) {
            expense.setAmount(req.getAmount());
        }

        if (req.getDescription() != null && !req.getDescription().isBlank()) {
            expense.setDescription(req.getDescription());
        }

        Expense updated = expenseRep.save(expense);

        return ExpenseRes.builder()
                .groupName(group.getName())
                .paidByUsername(updated.getPaidBy().getName())
                .description(updated.getDescription())
                .amount(updated.getAmount())
                .createdAt(updated.getCreatedAt())
                .build();
    }
}
