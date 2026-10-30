package springbootapp.minivms.model.dto.workitemdto.timesheet;

import springbootapp.minivms.model.entities.enums.TimeSheetStatus;
import springbootapp.minivms.model.entities.workitems.timesheets.TimeSheetDay;

import java.time.LocalDate;
import java.util.List;

public class TimeSheetEditDto {

    private LocalDate weekStartDate;
    private String workOrderId;
    private LocalDate weekEndDate;
    private TimeSheetStatus status;
    private String id;
    private List<TimeSheetDayDto> days;
    private String notes;


    public TimeSheetEditDto() {

    }

    public LocalDate getWeekStartDate() {
        return weekStartDate;
    }

    public void setWeekStartDate(LocalDate weekStartDate) {
        this.weekStartDate = weekStartDate;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public LocalDate getWeekEndDate() {
        return weekEndDate;
    }

    public void setWeekEndDate(LocalDate weekEndDate) {
        this.weekEndDate = weekEndDate;
    }

    public TimeSheetStatus getStatus() {
        return status;
    }

    public void setStatus(TimeSheetStatus status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<TimeSheetDayDto> getDays() {
        return days;
    }

    public void setDays(List<TimeSheetDayDto> days) {
        this.days = days;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
