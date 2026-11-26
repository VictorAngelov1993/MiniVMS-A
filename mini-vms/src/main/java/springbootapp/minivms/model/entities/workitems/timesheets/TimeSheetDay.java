package springbootapp.minivms.model.entities.workitems.timesheets;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "time_sheet_day")
public class TimeSheetDay {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @NotNull(message = "Timesheet is required")
    @ManyToOne
    @JoinColumn(name = "time_sheet_id", nullable = false)
    private TimeSheet timeSheet;

    @NotNull(message = "Date is required")
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull(message = "Hours are required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Hours must be at least 0.00")
    @DecimalMax(value = "24.00", inclusive = true, message = "Hours cannot exceed 24")
    @Digits(integer = 3, fraction = 2, message = "Hours can have up to 3 integer digits and 2 decimal places")
    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal hours;

    @Column(name = "editable", nullable = false)
    private boolean editable;


    public TimeSheetDay() {

    }

    public UUID getUuid() {
        return uuid;
    }


    public TimeSheet getTimeSheet() {
        return timeSheet;
    }

    public void setTimeSheet(TimeSheet timeSheet) {
        this.timeSheet = timeSheet;
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
