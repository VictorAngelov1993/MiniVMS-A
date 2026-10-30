package springbootapp.minivms.model.dto.workitemdto.workorder;

import springbootapp.minivms.model.entities.enums.WorkOrderStatus;

import java.time.LocalDate;

public class WorkOrderCardDto {
    private String id;
    private WorkOrderStatus status;
    private String assignedWorkerName;
    private LocalDate startDate;
    private LocalDate endDate;

    public WorkOrderCardDto() {

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
}
