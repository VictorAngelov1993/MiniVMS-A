package com.minivms.paymentservice.controller;

import com.minivms.paymentservice.model.dto.PaymentRequestDto;
import com.minivms.paymentservice.model.dto.PaymentResponseDto;
import com.minivms.paymentservice.model.entities.enums.PaymentStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {



    @GetMapping("/{id}")
    public PaymentResponseDto getPayment(@PathVariable("id") String invoiceId) {
        return null;
    }

    @PostMapping("/process")
    public PaymentResponseDto processPayment(@RequestBody PaymentRequestDto dto) {

        // save payment

        // amend this
        return new PaymentResponseDto("", PaymentStatus.APPROVED);

    }

}
