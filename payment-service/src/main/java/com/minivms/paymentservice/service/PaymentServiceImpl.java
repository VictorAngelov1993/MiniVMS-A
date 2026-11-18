package com.minivms.paymentservice.service;

import com.minivms.paymentservice.model.dto.PaymentRequestDto;
import com.minivms.paymentservice.model.dto.PaymentResponseDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService{

    @Override
    public PaymentResponseDto processPayment(PaymentRequestDto request) {
        return null;
    }
    @Override
     public PaymentResponseDto getPayment(UUID id) {
        return null;
    }


}
