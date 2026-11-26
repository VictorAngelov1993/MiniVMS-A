package springbootapp.minivms.model.dto.workitemdto.timesheet;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TimeSheetDayDto {
    @NotNull(message = "Date is required")
    private LocalDate date;
    @NotNull(message = "Hours are required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Hours must be at least 0.00")
    @DecimalMax(value = "24.00", inclusive = true, message = "Hours cannot exceed 24")
    @Digits(integer = 2, fraction = 2, message = "Hours can have up to 2 integer digits and 2 decimal places")
    private BigDecimal hours;
    private boolean editable;

    public TimeSheetDayDto() {

    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getHours() {
        return hours;
    }

    public void setHours(BigDecimal hours) {
        this.hours = hours;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}
