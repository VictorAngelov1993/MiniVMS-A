package springbootapp.minivms.model.dto.workitemdto.timesheet;

import java.util.List;

public class TimeSheetSubmitDto {
    private String timeSheetId;
    private List<TimeSheetDayDto> days;
    private String notes;

    public TimeSheetSubmitDto() {

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

    public String getTimeSheetId() {
        return timeSheetId;
    }

    public void setTimeSheetId(String timeSheetId) {
        this.timeSheetId = timeSheetId;
    }
}
