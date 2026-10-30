package springbootapp.minivms.model.dto.workitemdto.timesheet;

import springbootapp.minivms.model.entities.enums.TimeSheetStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TimeSheetEditBuyerViewDto {
    private String workerName;
    private TimeSheetStatus status;
    private String notes;
    private String id;
    private LocalDate weekStartDate;
    private List<TimeSheetDayDto> days;
    private BigDecimal totalHours;

    public TimeSheetEditBuyerViewDto() {

    }

    public BigDecimal getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(BigDecimal totalHours) {
        this.totalHours = totalHours;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public TimeSheetStatus getStatus() {
        return status;
    }

    public void setStatus(TimeSheetStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public List<TimeSheetDayDto> getDays() {
        return days;
    }

    public void setDays(List<TimeSheetDayDto> days) {
        this.days = days;
    }
}
