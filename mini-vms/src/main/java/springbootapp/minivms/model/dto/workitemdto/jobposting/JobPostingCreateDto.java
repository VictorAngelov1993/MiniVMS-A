package springbootapp.minivms.model.dto.workitemdto.jobposting;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class JobPostingCreateDto {

    @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters")
    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Description is required")
    @Size(min = 3, max = 5000, message = "Description must be between 3 and 5000 characters")
    private String description;
    @NotNull(message = "Start Date is required")
    private LocalDate startDate;
    @NotNull(message = "End Date is required")
    private LocalDate endDate;
    @NotNull(message = "Pay Rate is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Pay rate must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Pay rate can have up to 10 integer digits and 2 decimal places")
    private BigDecimal payRate;

    public JobPostingCreateDto() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
