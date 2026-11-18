package com.minivms.paymentservice.service;

import com.minivms.paymentservice.model.dto.BudgetResponseDto;
import com.minivms.paymentservice.model.entities.BuyerBudget;
import com.minivms.paymentservice.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class BudgetServiceImpl implements BudgetService{

    private final BudgetRepository budgetRepository;

    @Autowired
    public BudgetServiceImpl(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    @Override
    public BudgetResponseDto addBudget(UUID buyerUuid, BigDecimal amount) {
       Optional <BuyerBudget> buyerBudget = this.budgetRepository.getBuyerBudgetByBuyerUuid(buyerUuid);
       if(buyerBudget.isEmpty()) {
           BuyerBudget newBudget = new BuyerBudget();
           newBudget.setBuyerUuid(buyerUuid);
           newBudget.setAvailableBudget(amount);
           this.budgetRepository.save(newBudget);
           return new BudgetResponseDto("Budget for Buyer " + buyerUuid + " added",
                   newBudget.getAvailableBudget());
       }
       buyerBudget.get().setAvailableBudget(buyerBudget.get().getAvailableBudget().add(amount));
       this.budgetRepository.save(buyerBudget.get());
       return new BudgetResponseDto("Budget for Buyer " + buyerUuid + " added",
               buyerBudget.get().getAvailableBudget());

    }
}
