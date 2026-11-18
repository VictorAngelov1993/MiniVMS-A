package com.minivms.paymentservice.repository;

import com.minivms.paymentservice.model.entities.BuyerBudget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BudgetRepository extends JpaRepository<BuyerBudget, UUID> {
    Optional <BuyerBudget> getBuyerBudgetByBuyerUuid(UUID buyerUuid);


}
