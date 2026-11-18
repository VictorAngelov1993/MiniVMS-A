package springbootapp.minivms.model.dto.workitemdto.jobposting;

import springbootapp.minivms.model.entities.enums.JobPostingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class JobPostingDetailsDto {
    private String title;
    private JobPostingStatus status;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal payRate;
    // The below will be used if a WorkOrder is created.
    private String workOrderId;
    private String jobPostingId;

    public JobPostingDetailsDto() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public JobPostingStatus getStatus() {
        return status;
    }

    public void setStatus(JobPostingStatus status) {
        this.status = status;
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

    public BigDecimal getPayRate() {
        return payRate;
    }

    public void setPayRate(BigDecimal payRate) {
        this.payRate = payRate;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getJobPostingId() {
        return jobPostingId;
    }

    public void setJobPostingId(String jobPostingId) {
        this.jobPostingId = jobPostingId;
    }
}
