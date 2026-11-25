package com.minivms.paymentservice;

import com.minivms.paymentservice.model.dto.BudgetResponseDto;
import com.minivms.paymentservice.model.entities.BuyerBudget;
import com.minivms.paymentservice.repository.BudgetRepository;
import com.minivms.paymentservice.service.BudgetServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BudgetServiceImplTest {

    @Mock
    private BudgetRepository budgetRepository;
    @InjectMocks
    private BudgetServiceImpl budgetService;

    @Test
    void addBudgetCreatesNewBudgetWhenNoneExists() {
        UUID buyerUuid = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("250.00");
        when(this.budgetRepository.getBuyerBudgetByBuyerUuid(buyerUuid)).thenReturn(Optional.empty());

        BudgetResponseDto response = this.budgetService.addBudget(buyerUuid, amount);

        assertEquals(amount, response.getTotalBudget());
        verify(this.budgetRepository).save(any(BuyerBudget.class));
    }
}
