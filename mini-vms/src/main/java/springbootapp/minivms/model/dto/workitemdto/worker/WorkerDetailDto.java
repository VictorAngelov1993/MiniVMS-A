package springbootapp.minivms.model.dto.workitemdto.worker;

import springbootapp.minivms.model.entities.workitems.WorkOrder;

import java.util.List;

public class WorkerDetailDto {
    private String id;
    private String fullName;
    private String email;
    private String phone;
    // below is not very good approach because I am passing the entity where a DTO should be sent,
    //I’ll leave it as it is for now since this project is mainly for testing and I’m running a bit behind schedule.
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
