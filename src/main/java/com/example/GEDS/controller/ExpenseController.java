package com.example.GEDS.controller;

import com.example.GEDS.dto.ExpenseReq;
import com.example.GEDS.dto.ExpenseRes;
import com.example.GEDS.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expense")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    public ResponseEntity<ExpenseRes> add (@RequestBody @Valid ExpenseReq expenseReq) {
        return ResponseEntity.ok(expenseService.addExpense(expenseReq));
    }

    @PostMapping("/{id}")
    public ResponseEntity<ExpenseRes> update (
            @PathVariable Long id,
            @RequestBody @Valid ExpenseReq expenseReq) {
        return ResponseEntity.ok(expenseService.updateExpense(expenseReq,id));
    }
}
