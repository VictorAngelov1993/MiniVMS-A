package springbootapp.minivms.model.dto.workitemdto.invoice;


import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class CreateInvoiceDto {
    private String invoiceId;
    private UUID supplierId;
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
