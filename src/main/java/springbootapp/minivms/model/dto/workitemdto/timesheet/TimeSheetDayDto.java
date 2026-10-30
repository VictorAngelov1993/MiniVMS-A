package springbootapp.minivms.model.dto.workitemdto.timesheet;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TimeSheetDayDto {
    private LocalDate date;
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
