package springbootapp.minivms.model.dto.workitemdto.invoice;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class CreateInvoiceDto {
    private String invoiceId;
    private UUID supplierId;
    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Total amount must be at least 0.01")
    @Digits(integer = 10, fraction = 2, message = "Total amount can have up to 10 integer digits and 2 decimal places")
    private BigDecimal totalAmount;
    private List<String> timeSheetIds;



    public CreateInvoiceDto() {

    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public UUID getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(UUID supplierId) {
        this.supplierId = supplierId;
    }

    public List<String> getTimeSheetIds() {
        return timeSheetIds;
    }

    public void setTimeSheetIds(List<String> timeSheetIds) {
        this.timeSheetIds = timeSheetIds;
    }


}
