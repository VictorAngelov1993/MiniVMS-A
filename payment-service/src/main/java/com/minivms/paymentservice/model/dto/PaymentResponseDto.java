package com.minivms.paymentservice.model.dto;

import com.minivms.paymentservice.model.entities.enums.PaymentStatus;

public class PaymentResponseDto {

    private String invoiceId;
    private PaymentStatus status;

    public String getInvoiceId() {
        return invoiceId;
    }

    public PaymentResponseDto(String invoiceId, PaymentStatus status) {
        this.setInvoiceId(invoiceId);
        this.setStatus(status);
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}
