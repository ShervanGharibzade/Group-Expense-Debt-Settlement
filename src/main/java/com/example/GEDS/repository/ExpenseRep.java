package com.example.GEDS.repository;

import com.example.GEDS.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRep extends JpaRepository<Expense,Long> {
}
