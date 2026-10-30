package springbootapp.minivms.model.entities.workitems.timesheets;

import jakarta.persistence.*;
import springbootapp.minivms.model.entities.enums.TimeSheetStatus;
import springbootapp.minivms.model.entities.workitems.WorkOrder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "time_sheet")
public class TimeSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "work_order_id")
    private WorkOrder workOrder;

    @Column(name = "week_start_day")
    private LocalDate weekStartDay;

    @Column(name = "week_end_day")
    private LocalDate weekEndDay;


    @OneToMany(mappedBy = "timeSheet", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("date ASC")
    private List<TimeSheetDay> days = new ArrayList<>();


    @Column(columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.STRING)
    private TimeSheetStatus status;

    @Column(name = "time_sheet_id", nullable = false)
    private String timeSheetId;

    public TimeSheet() {

    }

    public UUID getUuid() {
        return uuid;
    }


    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public LocalDate getWeekStartDay() {
        return weekStartDay;
    }

    public void setWeekStartDay(LocalDate weekStartDay) {
        this.weekStartDay = weekStartDay;
    }

    public LocalDate getWeekEndDay() {
        return weekEndDay;
    }

    public void setWeekEndDay(LocalDate weekEndDay) {
        this.weekEndDay = weekEndDay;
    }

    public List<TimeSheetDay> getDays() {
        return days;
    }

    public void setDays(List<TimeSheetDay> days) {
        this.days = days;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public TimeSheetStatus getStatus() {
        return status;
    }

    public void setStatus(TimeSheetStatus status) {
        this.status = status;
    }

    public String getTimeSheetId() {
        return timeSheetId;
    }

    public void setTimeSheetId(String timeSheetId) {
        this.timeSheetId = timeSheetId;
    }
}
