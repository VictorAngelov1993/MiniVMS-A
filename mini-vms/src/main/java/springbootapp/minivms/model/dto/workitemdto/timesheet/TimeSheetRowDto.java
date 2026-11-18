package springbootapp.minivms.model.dto.workitemdto.timesheet;

import springbootapp.minivms.model.entities.enums.TimeSheetStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TimeSheetRowDto {

    private String id;
    private LocalDate weekStartDate;
    private LocalDate weekEndDate;
    private  BigDecimal totalHours;
    private TimeSheetStatus status;

    public TimeSheetRowDto() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getWeekStartDate() {
        return weekStartDate;
    }

    public void setWeekStartDate(LocalDate weekStartDate) {
        this.weekStartDate = weekStartDate;
    }

    public LocalDate getWeekEndDate() {
        return weekEndDate;
    }

    public void setWeekEndDate(LocalDate weekEndDate) {
        this.weekEndDate = weekEndDate;
    }

    public BigDecimal getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(BigDecimal totalHours) {
        this.totalHours = totalHours;
    }

    public TimeSheetStatus getStatus() {
        return status;
    }

    public void setStatus(TimeSheetStatus status) {
        this.status = status;
    }
}
