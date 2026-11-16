package springbootapp.minivms.model.entities.workitems;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;


import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(name = "invoice_id", unique = true, nullable = false)
    private String invoiceId;

    @Column(name = "supplier_name", nullable = false)
    @NotBlank
    private String supplierName;

    @Column(name = "time_sheet_id", nullable = false)
    @NotBlank
    private String timeSheetId;

    @Column(name = "worker_full_name", nullable = false)
    @NotBlank
    private String workerFullName;

    @Column(name = "total_hours")
    private BigDecimal totalHours;

    @Column(name = "worker_amount")
    private BigDecimal workerAmount;

    @Column(name = "total_amout")
    private BigDecimal totalAmount;

    public Invoice() {

    }

    public UUID getUuid() {
        return uuid;
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

    public String getTimeSheetId() {
        return timeSheetId;
    }

    public void setTimeSheetId(String timeSheetId) {
        this.timeSheetId = timeSheetId;
    }

    public String getWorkerFullName() {
        return workerFullName;
    }

    public void setWorkerFullName(String workerFullName) {
        this.workerFullName = workerFullName;
    }

    public BigDecimal getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(BigDecimal totalHours) {
        this.totalHours = totalHours;
    }

    public BigDecimal getWorkerAmount() {
        return workerAmount;
    }

    public void setWorkerAmount(BigDecimal workerAmount) {
        this.workerAmount = workerAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
