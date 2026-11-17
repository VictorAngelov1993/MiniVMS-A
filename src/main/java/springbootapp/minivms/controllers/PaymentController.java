package springbootapp.minivms.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springbootapp.minivms.model.dto.paymentdto.PaymentRequestDto;
import springbootapp.minivms.model.dto.paymentdto.PaymentResponseDto;
import springbootapp.minivms.model.entities.enums.PaymentStatus;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @PostMapping
    public PaymentResponseDto processPayment(@RequestBody PaymentRequestDto dto) {

        // save payment

        // amend this
        return new PaymentResponseDto("", PaymentStatus.PAID);

    }
}
