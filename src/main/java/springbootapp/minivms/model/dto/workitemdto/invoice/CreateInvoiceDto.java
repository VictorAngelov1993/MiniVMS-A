package springbootapp.minivms.model.dto.workitemdto.invoice;


import java.util.UUID;

public class CreateInvoiceDto {
    private String invoiceId;
    private UUID supplierId;

    public CreateInvoiceDto() {

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
}
