package springbootapp.minivms.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import springbootapp.minivms.config.security.FeignSecurityConfig;
import springbootapp.minivms.model.dto.paymentdto.*;

import java.util.UUID;

@FeignClient(name = "payment-service", url = "http://localhost:8081", configuration = FeignSecurityConfig.class)

public interface PaymentFeignClient {


    @PostMapping("/api/budgets/add")
    BudgetResponseDto addBudget(@RequestBody BudgetRequestDto requestDto);

    @GetMapping("/api/budgets/view")
    BudgetResponseDto viewBudget(@RequestParam UUID buyerUuid);

    @PostMapping("/api/payments/process")
    PaymentResponseDto processPayment(@RequestBody PaymentRequestDto requestDto);

    @GetMapping("/api/payments/{id}")
    PaymentResponseFullDataDto getPaymentByInvoiceId(@PathVariable("id") String invoiceId);


}
