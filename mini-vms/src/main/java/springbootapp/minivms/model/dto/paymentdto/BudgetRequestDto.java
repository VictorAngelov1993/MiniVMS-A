package springbootapp.minivms.model.dto.paymentdto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public class BudgetRequestDto {

    @NotNull(message = "buyerUuid is required")
    private UUID buyerUuid;
    @NotNull(message = "amountToAdd is required")
    @DecimalMin(value = "0.01", message = "Value must be greater or equal to 0.01")
    private BigDecimal amountToAdd;

    public BudgetRequestDto() {

    }

    public UUID getBuyerUuid() {
        return buyerUuid;
    }

    public void setBuyerUuid(UUID buyerUuid) {
        this.buyerUuid = buyerUuid;
    }

    public BigDecimal getAmountToAdd() {
        return amountToAdd;
    }

    public void setAmountToAdd(BigDecimal amountToAdd) {
        this.amountToAdd = amountToAdd;
    }


}
