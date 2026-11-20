package com.minivms.paymentservice.model.dto;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public class PaymentRequestDto {
    @NotNull(message = "invoiceId is required")
    private String invoiceId;
    @NotNull(message = "supplierName is required")
    private String supplierName;
    @NotNull(message = "Amount is required")
    private BigDecimal amount;
    @NotNull(message = "buyerUuid is required")
    private UUID buyerUuid;

    public PaymentRequestDto() {

    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public UUID getBuyerUuid() {
        return buyerUuid;
    }

    public void setBuyerUuid(UUID buyerUuid) {
        this.buyerUuid = buyerUuid;
    }
}
