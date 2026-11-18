package springbootapp.minivms.model.dto.paymentdto;

import java.math.BigDecimal;
import java.util.UUID;

public class BudgetRequestDto {

    private UUID buyerUuid;
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
