package springbootapp.minivms.model.entities.persons;

import jakarta.persistence.*;
import springbootapp.minivms.model.entities.workitems.JobSeeker;
import springbootapp.minivms.model.entities.workitems.WorkOrder;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Supplier extends AbstractPerson{

    @OneToMany(mappedBy = "supplier")
    private List<JobSeeker> jobSeekers = new ArrayList<>();

    @OneToMany(mappedBy = "supplier")
    private List<WorkOrder> workOrders;


    public List<JobSeeker> getJobSeekers() {
        return jobSeekers;
    }

    public void setJobSeekers(List<JobSeeker> jobSeekers) {
        this.jobSeekers = jobSeekers;
    }

    public List<WorkOrder> getWorkOrders() {
        return workOrders;
    }

    public void setWorkOrders(List<WorkOrder> workOrders) {
        this.workOrders = workOrders;
    }
}
