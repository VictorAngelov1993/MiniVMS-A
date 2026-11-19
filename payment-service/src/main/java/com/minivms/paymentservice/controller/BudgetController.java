package com.minivms.paymentservice.controller;

import com.minivms.paymentservice.model.dto.BudgetRequestDto;
import com.minivms.paymentservice.model.dto.BudgetResponseDto;
import com.minivms.paymentservice.service.BudgetServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetServiceImpl budgetService;

    @Autowired
    public BudgetController(BudgetServiceImpl budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping("/add")
    public BudgetResponseDto addBudget(@RequestBody BudgetRequestDto requestDto) {
        return this.budgetService.addBudget(requestDto.getBuyerUuid(), requestDto.getAmountToAdd());
    }

    @GetMapping("/view")
    public BudgetResponseDto viewBudget(@RequestParam UUID buyerUuid) {
        return this.budgetService.viewBudget(buyerUuid);
    }


}
