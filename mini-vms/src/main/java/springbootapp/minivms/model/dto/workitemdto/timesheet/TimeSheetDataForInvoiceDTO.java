package springbootapp.minivms.model.dto.workitemdto.timesheet;

import java.math.BigDecimal;

public class TimeSheetDataForInvoiceDTO {
    private String timesheetId;
    private String workerFullName;
    private BigDecimal totalHours;
    private BigDecimal totalAmount;

    public TimeSheetDataForInvoiceDTO() {

    }

    public String getTimesheetId() {
        return timesheetId;
    }

    public void setTimesheetId(String timesheetId) {
        this.timesheetId = timesheetId;
    }

    public String getWorkerFullName() {
        return workerFullName;
    }

    public void setWorkerFullName(String workerFullName) {
        this.workerFullName = workerFullName;
    }

    public BigDecimal getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(BigDecimal totalHours) {
        this.totalHours = totalHours;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
