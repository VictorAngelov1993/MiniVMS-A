package com.minivms.paymentservice.service;

import com.minivms.paymentservice.model.dto.BudgetResponseDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface BudgetService {

    BudgetResponseDto addBudget(UUID buyerUuid, BigDecimal amount);
    BudgetResponseDto viewBudget(UUID buyerUuid);
}
