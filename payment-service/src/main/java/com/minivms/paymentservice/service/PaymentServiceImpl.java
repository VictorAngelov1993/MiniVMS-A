package com.minivms.paymentservice.service;

import com.minivms.paymentservice.model.dto.PaymentRequestDto;
import com.minivms.paymentservice.model.dto.PaymentResponseDto;
import com.minivms.paymentservice.model.entities.Payment;
import com.minivms.paymentservice.model.entities.enums.PaymentStatus;
import com.minivms.paymentservice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService{

    private final BudgetServiceImpl budgetService;
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(BudgetServiceImpl budgetService, PaymentRepository paymentRepository) {
        this.budgetService = budgetService;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentResponseDto processPayment(PaymentRequestDto request) {
        Payment payment = new Payment();
        payment.setInvoiceId(request.getInvoiceId());
        payment.setAmount(request.getAmount());
        payment.setStatus(PaymentStatus.PAID);
        payment.setSupplierName(request.getSupplierName());
        payment.setPaidAt(LocalDateTime.now());
        this.budgetService.decreaseBudget(request.getBuyerUuid(), request.getAmount());
        this.paymentRepository.save(payment);
        return new PaymentResponseDto(request.getInvoiceId(), PaymentStatus.PAID);
    }
    @Override
     public PaymentResponseDto getPayment(UUID id) {
        return null;
    }


}
