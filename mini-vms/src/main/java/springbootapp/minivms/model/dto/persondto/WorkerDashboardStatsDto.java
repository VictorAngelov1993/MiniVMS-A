package springbootapp.minivms.model.dto.persondto;

public class WorkerDashboardStatsDto {
    private long assignedWorkOrderCount;
    private long pendingTimesheetCount;

    public WorkerDashboardStatsDto() {

    }

    public long getAssignedWorkOrderCount() {
        return assignedWorkOrderCount;
    }

    public void setAssignedWorkOrderCount(long assignedWorkOrderCount) {
        this.assignedWorkOrderCount = assignedWorkOrderCount;
    }

    public long getPendingTimesheetCount() {
        return pendingTimesheetCount;
    }

    public void setPendingTimesheetCount(long pendingTimesheetCount) {
        this.pendingTimesheetCount = pendingTimesheetCount;
    }
}
