package springbootapp.minivms.model.dto.workitemdto.worker;

import springbootapp.minivms.model.entities.workitems.WorkOrder;

import java.util.List;

public class WorkerDetailDto {
    private String id;
    private String fullName;
    private String email;
    private String phone;
    private List<WorkOrder> assignedWorkOrders;

    public WorkerDetailDto() {

    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<WorkOrder> getAssignedWorkOrders() {
        return assignedWorkOrders;
    }

    public void setAssignedWorkOrders(List<WorkOrder> assignedWorkOrders) {
        this.assignedWorkOrders = assignedWorkOrders;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
