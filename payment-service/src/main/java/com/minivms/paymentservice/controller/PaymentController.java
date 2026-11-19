package com.minivms.paymentservice.controller;

import com.minivms.paymentservice.model.dto.BudgetResponseDto;
import com.minivms.paymentservice.model.dto.PaymentRequestDto;
import com.minivms.paymentservice.model.dto.PaymentResponseDto;
import com.minivms.paymentservice.model.entities.Payment;
import com.minivms.paymentservice.model.entities.enums.PaymentStatus;
import com.minivms.paymentservice.service.BudgetServiceImpl;
import com.minivms.paymentservice.service.PaymentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final BudgetServiceImpl budgetService;
    private final PaymentServiceImpl paymentService;

    @Autowired
    public PaymentController(BudgetServiceImpl budgetService,
                             PaymentServiceImpl paymentService) {
        this.budgetService = budgetService;
        this.paymentService = paymentService;
    }



    @GetMapping("/{id}")
    public PaymentResponseDto getPayment(@PathVariable("id") String invoiceId) {
        return null;
    }

    @PostMapping("/process")
    public PaymentResponseDto processPayment(@RequestBody PaymentRequestDto dto) {
        BudgetResponseDto budget = this.budgetService.viewBudget(dto.getBuyerUuid());
        PaymentResponseDto responseDto = new PaymentResponseDto();
        if(budget.getTotalBudget().compareTo(dto.getAmount()) < 0) {
            responseDto.setInvoiceId(dto.getInvoiceId());
            responseDto.setStatus(PaymentStatus.REJECTED);
            return responseDto;
        }
        return this.paymentService.processPayment(dto);
    }

}
