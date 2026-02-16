package com.example.GEDS.repository;

import com.example.GEDS.entity.ExpenseSplit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseSplitRepo extends JpaRepository<ExpenseSplit,Long> {
    boolean existsByExpenseId(Long id);
}
