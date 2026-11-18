package springbootapp.minivms.model.entities.persons;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import springbootapp.minivms.model.entities.workitems.Invoice;
import springbootapp.minivms.model.entities.workitems.JobPosting;


import java.util.ArrayList;
import java.util.List;

@Entity
public class Buyer extends AbstractPerson{


    @OneToMany(mappedBy = "buyer")
    private List<JobPosting> jobPostings = new ArrayList<>();

    @OneToMany(mappedBy = "buyer")
    private List<Invoice> invoices = new ArrayList<>();

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    public List<JobPosting> getJobPostings() {
        return jobPostings;
    }

    public void setJobPostings(List<JobPosting> jobPostings) {
        this.jobPostings = jobPostings;
    }
}
