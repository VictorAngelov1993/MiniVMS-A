package springbootapp.minivms.model.entities.workitems.timesheets;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import springbootapp.minivms.model.entities.enums.TimeSheetStatus;
import springbootapp.minivms.model.entities.workitems.Invoice;
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

    @NotNull(message = "Work order is required")
    @ManyToOne
    @JoinColumn(name = "work_order_id")
    private WorkOrder workOrder;

    @NotNull(message = "Week start date is required")
    @Column(name = "week_start_day")
    private LocalDate weekStartDay;

    @NotNull(message = "Week end date is required")
    @Column(name = "week_end_day")
    private LocalDate weekEndDay;

    @Valid
    @OneToMany(mappedBy = "timeSheet", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("date ASC")
    private List<TimeSheetDay> days = new ArrayList<>();

    @Size(max = 5000, message = "Notes cannot exceed 5000 characters")
    @Column(columnDefinition = "TEXT")
    private String notes;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    private TimeSheetStatus status;

    @NotBlank(message = "Timesheet ID is required")
    @Size(max = 50, message = "Timesheet ID cannot exceed 50 characters")
    @Column(name = "time_sheet_id", nullable = false, length = 50)
    private String timeSheetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = true)
    private Invoice invoice;

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

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}
