package com.minivms.paymentservice.service;

import com.minivms.paymentservice.exception.custom.InvalidAmountException;
import com.minivms.paymentservice.model.dto.BudgetResponseDto;
import com.minivms.paymentservice.model.entities.BuyerBudget;
import com.minivms.paymentservice.repository.BudgetRepository;
import jakarta.persistence.EntityNotFoundException;
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

        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Value must be greater or equal to 0.01");
        }

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
    @Override
    public BudgetResponseDto viewBudget(UUID buyerUuid) {
        Optional<BuyerBudget> buyerBudget = this.budgetRepository.getBuyerBudgetByBuyerUuid(buyerUuid);
        if(buyerBudget.isPresent()) {
            return new BudgetResponseDto("Current Budger", buyerBudget.get().getAvailableBudget());
        }
        // TODO below should be custom BudgetNotFoundException
        throw new IllegalArgumentException("No budget account found.");
    }

    @Override
    public void decreaseBudget(UUID buyerUuid, BigDecimal value) {
       BuyerBudget budget = this.budgetRepository.getBuyerBudgetByBuyerUuid(buyerUuid)
               .orElseThrow(() -> new EntityNotFoundException("Budget not found"));

       budget.setAvailableBudget(budget.getAvailableBudget().subtract(value));
       this.budgetRepository.save(budget);

    }

}
