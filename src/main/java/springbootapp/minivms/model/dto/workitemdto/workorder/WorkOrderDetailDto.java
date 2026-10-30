package springbootapp.minivms.model.dto.workitemdto.workorder;

import springbootapp.minivms.model.entities.enums.WorkOrderStatus;
import springbootapp.minivms.model.entities.persons.Worker;

import java.math.BigDecimal;
import java.time.LocalDate;

public class WorkOrderDetailDto {
    private String id;
    private WorkOrderStatus status;
    private String assignedWorkerName;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal payRate;
    private String notes;
    private Worker assignedWorker;

    public WorkOrderDetailDto() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public WorkOrderStatus getStatus() {
        return status;
    }

    public void setStatus(WorkOrderStatus status) {
        this.status = status;
    }

    public String getAssignedWorkerName() {
        return assignedWorkerName;
    }

    public void setAssignedWorkerName(String assignedWorkerName) {
        this.assignedWorkerName = assignedWorkerName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getPayRate() {
        return payRate;
    }

    public void setPayRate(BigDecimal payRate) {
        this.payRate = payRate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Worker getAssignedWorker() {
        return assignedWorker;
    }

    public void setAssignedWorker(Worker assignedWorker) {
        this.assignedWorker = assignedWorker;
    }
}
