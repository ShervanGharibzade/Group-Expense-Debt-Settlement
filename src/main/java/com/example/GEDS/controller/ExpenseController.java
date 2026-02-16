package com.example.GEDS.controller;

import com.example.GEDS.dto.ExpenseReq;
import com.example.GEDS.dto.ExpenseRes;
import com.example.GEDS.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expense")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    // FIX 9: Was missing @PostMapping — endpoint was completely unreachable
    @PostMapping
    public ResponseEntity<ExpenseRes> add(@RequestBody @Valid ExpenseReq expenseReq) {
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseService.addExpense(expenseReq));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseRes> update(
            @PathVariable Long id,
            @RequestBody @Valid ExpenseReq expenseReq) {
        return ResponseEntity.ok(expenseService.updateExpense(expenseReq, id));
    }
}
