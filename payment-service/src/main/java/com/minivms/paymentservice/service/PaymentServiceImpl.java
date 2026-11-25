package com.minivms.paymentservice.service;

import com.minivms.paymentservice.model.dto.PaymentRequestDto;
import com.minivms.paymentservice.model.dto.PaymentResponseDto;
import com.minivms.paymentservice.model.dto.PaymentResponseFullDataDto;
import com.minivms.paymentservice.model.entities.Payment;
import com.minivms.paymentservice.model.entities.enums.PaymentStatus;
import com.minivms.paymentservice.repository.PaymentRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class PaymentServiceImpl implements PaymentService{

    private final BudgetServiceImpl budgetService;
    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;
    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    public PaymentServiceImpl(BudgetServiceImpl budgetService, PaymentRepository paymentRepository,
                              ModelMapper modelMapper) {
        this.budgetService = budgetService;
        this.paymentRepository = paymentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PaymentResponseDto processPayment(PaymentRequestDto request) {
        log.info("Starting the process Payment process");
        Payment payment = new Payment();
        payment.setInvoiceId(request.getInvoiceId());
        payment.setAmount(request.getAmount());
        payment.setStatus(PaymentStatus.PAID);
        payment.setSupplierName(request.getSupplierName());
        payment.setPaidAt(LocalDateTime.now());
        this.budgetService.decreaseBudget(request.getBuyerUuid(), request.getAmount());
        this.paymentRepository.save(payment);
        log.info("Payment successfully created and processed");
        return new PaymentResponseDto(request.getInvoiceId(), PaymentStatus.PAID);
    }
    @Override
     public PaymentResponseFullDataDto getPaymentFullData(String invoiceID) {
        log.info("Retrieving data for Invoice with ID {}", invoiceID);
        Payment payment = this.paymentRepository.getPaymentByInvoiceId(invoiceID);
        return this.modelMapper.map(payment, PaymentResponseFullDataDto.class);
    }


}
