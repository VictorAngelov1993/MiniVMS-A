package springbootapp.minivms.model.entities.persons;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import springbootapp.minivms.model.entities.workitems.WorkOrder;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Worker extends AbstractPerson{



    @Column(name = "worker_id")
    private String workerId;

    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL)
    private List<WorkOrder> workOrders = new ArrayList<>();

    @Column(name = "phone_number")
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
