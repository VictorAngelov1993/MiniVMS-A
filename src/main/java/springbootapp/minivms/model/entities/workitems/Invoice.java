package springbootapp.minivms.model.entities.workitems;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import springbootapp.minivms.model.entities.enums.InvoiceStatus;
import springbootapp.minivms.model.entities.persons.Buyer;
import springbootapp.minivms.model.entities.workitems.timesheets.TimeSheet;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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

    @OneToMany(mappedBy = "invoice")
    private List<TimeSheet> timeSheets = new ArrayList<>();

    @Column(name = "worker_full_name", nullable = false)
    @NotBlank
    private String workerFullName;

    @Column(name = "total_hours")
    private BigDecimal totalHours;

    @Column(name = "worker_amount")
    private BigDecimal workerAmount;

    @Column(name = "total_amout")
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private Buyer buyer;

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

    public List<TimeSheet> getTimeSheets() {
        return timeSheets;
    }

    public void setTimeSheets(List<TimeSheet> timeSheets) {
        this.timeSheets = timeSheets;
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

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }


}
