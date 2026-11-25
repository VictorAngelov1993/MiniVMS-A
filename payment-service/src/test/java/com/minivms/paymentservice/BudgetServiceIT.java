package com.minivms.paymentservice;

import com.minivms.paymentservice.model.dto.BudgetResponseDto;
import com.minivms.paymentservice.model.entities.BuyerBudget;
import com.minivms.paymentservice.repository.BudgetRepository;
import com.minivms.paymentservice.service.BudgetServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BudgetServiceIT {

    @Autowired
    private BudgetServiceImpl budgetService;
    @Autowired
    private BudgetRepository budgetRepository;

    @Test
    void addBudgetPersistBuyerBudgetInDatabase() {
        UUID buyerUuid = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("300.00");

        BudgetResponseDto response = this.budgetService.addBudget(buyerUuid, amount);

        assertEquals(0, amount.compareTo(response.getTotalBudget()));
        assertTrue(this.budgetRepository.getBuyerBudgetByBuyerUuid(buyerUuid).isPresent());
        BuyerBudget saved = this.budgetRepository.getBuyerBudgetByBuyerUuid(buyerUuid).orElseThrow();
        assertNotNull(saved.getUuid());
        assertEquals(0, amount.compareTo(saved.getAvailableBudget()));

    }
}
