package springbootapp.minivms.model.dto.workitemdto.workorder;


import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class WorkOrderCreateDto {
    @NotBlank(message = "Job Posting Id is required")
    private String jobPostingId;
    @NotBlank(message = "Job Seeker is required")
    private String jobSeekerId;
    @Size(max = 5000, message = "Notes cannot exceed 5000 characters")
    private String notes;
    @NotNull(message = "Pay Rate is mandatory")
    @DecimalMin(value = "0.01", inclusive = true, message = "Pay rate must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Pay rate can have up to 10 integer digits and 2 decimal places")
    private BigDecimal payRate;

    public WorkOrderCreateDto() {

    }

    public String getJobSeekerId() {
        return jobSeekerId;
    }

    public void setJobSeekerId(String jobSeekerId) {
        this.jobSeekerId = jobSeekerId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public BigDecimal getPayRate() {
        return payRate;
    }

    public void setPayRate(BigDecimal payRate) {
        this.payRate = payRate;
    }

    public String getJobPostingId() {
        return jobPostingId;
    }

    public void setJobPostingId(String jobPostingId) {
        this.jobPostingId = jobPostingId;
    }
}
