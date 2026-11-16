package springbootapp.minivms.model.dto.workitemdto.invoice;

import springbootapp.minivms.model.entities.enums.InvoiceStatus;

import java.math.BigDecimal;

public class InvoiceCardDto {

    private String invoiceId;
    private InvoiceStatus status;
    private String supplierName;
    private int timeSheetCount;
    private BigDecimal totalAmount;

    public InvoiceCardDto() {

    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public int getTimeSheetCount() {
        return timeSheetCount;
    }

    public void setTimeSheetCount(int timeSheetCount) {
        this.timeSheetCount = timeSheetCount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
