package springbootapp.minivms.model.entities.workitems.timesheets;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "time_sheet_day")
public class TimeSheetDay {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "time_sheet_id", nullable = false)
    private TimeSheet timeSheet;

    private LocalDate date;

    @Column(precision = 5, scale = 2)
    private BigDecimal hours;

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
