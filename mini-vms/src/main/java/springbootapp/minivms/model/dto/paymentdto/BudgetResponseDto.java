package springbootapp.minivms.model.dto.paymentdto;

import java.math.BigDecimal;

public class BudgetResponseDto {

    private String response;
    private BigDecimal totalBudget;

    public BudgetResponseDto() {

    }

    public BudgetResponseDto(String response, BigDecimal totalBudget) {
        this.setResponse(response);
        this.setTotalBudget(totalBudget);
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public BigDecimal getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(BigDecimal totalBudget) {
        this.totalBudget = totalBudget;
    }
}
