package com.minivms.paymentservice.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
public class BuyerBudget {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;
    @NotNull(message = "Buyer UUID is required")
    @Column(name = "buyer_uuid", nullable = false, unique = true)
    private UUID buyerUuid;
    @NotNull(message = "Available budget is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Available budget cannot be negative")
    @Digits(integer = 12, fraction = 2, message = "Available budget can have up to 12 integer digits and 2 decimal places")
    @Column(name = "available_budget", precision = 14, scale = 2, nullable = false)
    private BigDecimal availableBudget;

    public BuyerBudget() {

    }

    public UUID getUuid() {
        return uuid;
    }


    public UUID getBuyerUuid() {
        return buyerUuid;
    }

    public void setBuyerUuid(UUID buyerUuid) {
        this.buyerUuid = buyerUuid;
    }

    public BigDecimal getAvailableBudget() {
        return availableBudget;
    }

    public void setAvailableBudget(BigDecimal availableBudget) {
        this.availableBudget = availableBudget;
    }
}
