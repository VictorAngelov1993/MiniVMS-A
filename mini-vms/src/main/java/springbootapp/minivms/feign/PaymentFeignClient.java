package springbootapp.minivms.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import springbootapp.minivms.model.dto.paymentdto.BudgetRequestDto;
import springbootapp.minivms.model.dto.paymentdto.BudgetResponseDto;
import springbootapp.minivms.model.dto.paymentdto.PaymentRequestDto;
import springbootapp.minivms.model.dto.paymentdto.PaymentResponseDto;

@FeignClient(name = "payment-service", url = "http://localhost:8081")

public interface PaymentFeignClient {

    @PostMapping("/api/payments")
    PaymentResponseDto createPayment(@RequestBody PaymentRequestDto requestDto);


    @PostMapping("/api/budgets/add")
    BudgetResponseDto addBudget(@RequestBody BudgetRequestDto requestDto);


}
