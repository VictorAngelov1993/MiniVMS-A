package springbootapp.minivms.model.dto.workitemdto.invoice;

import springbootapp.minivms.model.dto.workitemdto.timesheet.TimeSheetDataForInvoiceDTO;

import java.math.BigDecimal;
import java.util.List;

public class InvoicePreviewDTO {
    private String invoiceId;
    private String supplierName;
    private List<TimeSheetDataForInvoiceDTO> timeSheets;
    private BigDecimal totalAmount;

    public InvoicePreviewDTO() {

    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public List<TimeSheetDataForInvoiceDTO> getTimeSheets() {
        return timeSheets;
    }

    public void setTimeSheets(List<TimeSheetDataForInvoiceDTO> timeSheets) {
        this.timeSheets = timeSheets;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
