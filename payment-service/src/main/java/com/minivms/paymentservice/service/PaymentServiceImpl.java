package com.minivms.paymentservice.service;

import com.minivms.paymentservice.model.dto.PaymentRequestDto;
import com.minivms.paymentservice.model.dto.PaymentResponseDto;
import com.minivms.paymentservice.model.dto.PaymentResponseFullDataDto;
import com.minivms.paymentservice.model.entities.Payment;
import com.minivms.paymentservice.model.entities.enums.PaymentStatus;
import com.minivms.paymentservice.repository.PaymentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService{

    private final BudgetServiceImpl budgetService;
    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PaymentServiceImpl(BudgetServiceImpl budgetService, PaymentRepository paymentRepository,
                              ModelMapper modelMapper) {
        this.budgetService = budgetService;
        this.paymentRepository = paymentRepository;
        this.modelMapper = modelMapper;
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
     public PaymentResponseFullDataDto getPaymentFullData(String invoiceID) {
        Payment payment = this.paymentRepository.getPaymentByInvoiceId(invoiceID);
        return this.modelMapper.map(payment, PaymentResponseFullDataDto.class);
    }


}
