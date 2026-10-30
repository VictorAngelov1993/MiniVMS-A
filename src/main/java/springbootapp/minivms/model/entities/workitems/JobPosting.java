package springbootapp.minivms.model.entities.workitems;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import springbootapp.minivms.model.entities.enums.JobPostingStatus;
import springbootapp.minivms.model.entities.persons.Buyer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "job_posting")
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    // I am creating the JP ID as String so that in the future we can introduce Company Codes.
    @Column(name = "job_posting_id", unique = true, nullable = false)
    @NotBlank
    private String jobPostingId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_date")
    @NotNull
    private LocalDate startDate;

    @Column(name = "end_date")
    @NotNull
    private LocalDate endDate;

    @OneToMany(mappedBy = "jobPosting")
    private List<WorkOrder> workOrders = new ArrayList<>();

    @Column(name = "pay_rate")
    private BigDecimal payRate;

    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private Buyer buyer;

    @OneToMany(mappedBy = "jobPosting")
    private List<JobSeekerSubmission> submissions = new ArrayList<>();

    private JobPostingStatus status;

    public JobPosting() {

    }

    public UUID getUuid() {
        return uuid;
    }


    public String getJobPostingId() {
        return jobPostingId;
    }

    public void setJobPostingId(String jobPostingId) {
        this.jobPostingId = jobPostingId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<WorkOrder> getWorkOrders() {
        return workOrders;
    }

    public void setWorkOrders(List<WorkOrder> workOrders) {
        this.workOrders = workOrders;
    }

    public BigDecimal getPayRate() {
        return payRate;
    }

    public void setPayRate(BigDecimal payRate) {
        this.payRate = payRate;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    public JobPostingStatus getStatus() {
        return status;
    }

    public void setStatus(JobPostingStatus status) {
        this.status = status;
    }

    public List<JobSeekerSubmission> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<JobSeekerSubmission> submissions) {
        this.submissions = submissions;
    }
}
