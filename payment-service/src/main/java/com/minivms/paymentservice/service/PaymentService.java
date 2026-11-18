package com.minivms.paymentservice.service;

import com.minivms.paymentservice.model.dto.PaymentRequestDto;
import com.minivms.paymentservice.model.dto.PaymentResponseDto;

import java.util.UUID;

public interface PaymentService {

    PaymentResponseDto processPayment(PaymentRequestDto request);
    PaymentResponseDto getPayment(UUID id);

}
