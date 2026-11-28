package springbootapp.minivms.model.dto.persondto;

public class BuyerDashboardStatsDto {
    private int jobPostingCount;
    private int workOrderCount;
    private long activeWorkerCount;
    private long pendingTimesheetCount;
    private long invoiceCount;


    public BuyerDashboardStatsDto() {

    }

    public int getJobPostingCount() {
        return jobPostingCount;
    }

    public void setJobPostingCount(int jobPostingCount) {
        this.jobPostingCount = jobPostingCount;
    }

    public int getWorkOrderCount() {
        return workOrderCount;
    }

    public void setWorkOrderCount(int workOrderCount) {
        this.workOrderCount = workOrderCount;
    }

    public long getActiveWorkerCount() {
        return activeWorkerCount;
    }

    public void setActiveWorkerCount(long activeWorkerCount) {
        this.activeWorkerCount = activeWorkerCount;
    }

    public long getPendingTimesheetCount() {
        return pendingTimesheetCount;
    }

    public void setPendingTimesheetCount(long pendingTimesheetCount) {
        this.pendingTimesheetCount = pendingTimesheetCount;
    }

    public long getInvoiceCount() {
        return invoiceCount;
    }

    public void setInvoiceCount(long invoiceCount) {
        this.invoiceCount = invoiceCount;
    }
}
