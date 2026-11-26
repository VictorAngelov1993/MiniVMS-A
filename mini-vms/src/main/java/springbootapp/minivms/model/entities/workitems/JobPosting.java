package springbootapp.minivms.model.entities.workitems;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "Job posting ID is required")
    @Size(max = 50, message = "Job posting ID cannot exceed 50 characters")
    @Column(name = "job_posting_id", unique = true, nullable = false, length = 50)
    private String jobPostingId;

    @NotBlank(message = "Title is required")
    @Size(max = 150, message = "Title cannot exceed 150 characters")
    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Size(max = 5000, message = "Description cannot exceed 5000 characters")
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Start date is required")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @OneToMany(mappedBy = "jobPosting")
    private List<WorkOrder> workOrders = new ArrayList<>();

    @NotNull(message = "Pay rate is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Pay rate must be at least 0.01")
    @Digits(integer = 8, fraction = 2, message = "Pay rate can have up to 8 integer digits and 2 decimal places")
    @Column(name = "pay_rate", precision = 10, scale = 2, nullable = false)
    private BigDecimal payRate;

    @NotNull(message = "Buyer is required")
    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private Buyer buyer;

    @OneToMany(mappedBy = "jobPosting")
    private List<JobSeekerSubmission> submissions = new ArrayList<>();

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
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
