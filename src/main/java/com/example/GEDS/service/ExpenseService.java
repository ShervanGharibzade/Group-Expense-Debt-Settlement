package com.example.GEDS.service;

import com.example.GEDS.config.UserNotFoundException;
import com.example.GEDS.dto.ExpenseReq;
import com.example.GEDS.dto.ExpenseRes;
import com.example.GEDS.dto.ExpenseSplitReq;
import com.example.GEDS.entity.Expense;
import com.example.GEDS.entity.Group;
import com.example.GEDS.entity.User;
import com.example.GEDS.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final GroupMemberRepo groupMemberRepo;
    private final ExpenseRep  expenseRep;
    private final UserRepo  userRepo;
    private final GroupRepo groupRepo;
    private final ExpenseSplitService expenseSplitService;


    @Transactional
    public ExpenseRes addExpense(ExpenseReq req) {



        Group group = groupRepo.findByName(req.getGroupName()).orElseThrow(() ->
                new IllegalArgumentException("Group with this name not found"));

        User user = userRepo.findById(req.getPaidByUserId()).orElseThrow(()->
                new UserNotFoundException("User not found"));
        groupMemberRepo.findByGroupIdAndUserId(group.getId(), req.getPaidByUserId()).orElseThrow(() ->
                new IllegalArgumentException("User with this name not found"));

        Expense expense = Expense.builder()
                .amount(req.getAmount())
                .paidBy(user)
                .description(req.getDescription())
                .group(group)
                .build();

        ExpenseSplitReq ex = ExpenseSplitReq.builder()
                .expenseId(expense.getId())
                .userId(user.getId())
                .amount(req.getAmount())
                .build();

        expenseSplitService.expenseSplit(ex);

        expenseRep.save(expense);

        return ExpenseRes.builder()
                .groupName(group.getName())
                .paidByUsername(user.getName())
                .description(req.getDescription())
                .amount(req.getAmount())
                .createdAt(expense.getCreatedAt())
                .build();
    }


    @Transactional
    public ExpenseRes updateExpense(ExpenseReq req,Long expenseId) {

        Expense expense = expenseRep.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found"));

        Group group = groupRepo.findByName(req.getGroupName())
                .orElseThrow(() -> new IllegalArgumentException("Group with this name not found"));

        if (!expense.getGroup().getId().equals(group.getId())) {
            throw new IllegalArgumentException("Expense does not belong to this group");
        }

        if (req.getPaidByUserId() != null &&
                !expense.getPaidBy().getId().equals(req.getPaidByUserId())) {

            User user = userRepo.findById(req.getPaidByUserId())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            groupMemberRepo.findByGroupIdAndUserId(group.getId(), user.getId())
                    .orElseThrow(() -> new IllegalArgumentException("User is not member of this group"));

            expense.setPaidBy(user);
        }

        if (req.getAmount() != null) {
            expense.setAmount(req.getAmount());
        }

        if (req.getDescription() != null) {
            expense.setDescription(req.getDescription());
        }

        expenseRep.save(expense);

        return ExpenseRes.builder()
                .groupName(group.getName())
                .paidByUsername(expense.getPaidBy().getName())
                .description(expense.getDescription())
                .amount(expense.getAmount())
                .createdAt(expense.getCreatedAt())
                .build();
    }

}
