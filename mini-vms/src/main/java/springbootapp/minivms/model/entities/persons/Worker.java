package springbootapp.minivms.model.entities.persons;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import springbootapp.minivms.model.entities.workitems.WorkOrder;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Worker extends AbstractPerson{



    @NotBlank(message = "Worker ID is required")
    @Size(max = 50, message = "Worker ID cannot exceed 50 characters")
    @Column(name = "worker_id", length = 50)
    private String workerId;

    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL)
    private List<WorkOrder> workOrders = new ArrayList<>();

    @Column(name = "phone_number", length = 30)
    private String phoneNumber;


    public List<WorkOrder> getWorkOrders() {
        return workOrders;
    }

    public void setWorkOrders(List<WorkOrder> workOrders) {
        this.workOrders = workOrders;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
